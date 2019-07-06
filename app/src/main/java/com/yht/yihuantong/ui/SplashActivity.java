package com.yht.yihuantong.ui;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yanzhenjie.nohttp.Headers;
import com.yanzhenjie.nohttp.download.DownloadListener;
import com.yht.frame.api.DirHelper;
import com.yht.frame.api.FileTransferServer;
import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.CommonData;
import com.yht.frame.data.DocAuthStatus;
import com.yht.frame.data.Tasks;
import com.yht.frame.http.retrofit.RequestUtils;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.utils.LogUtils;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ui.auth.AuthDoctorActivity;
import com.yht.yihuantong.ui.login.LoginOptionsActivity;
import com.yht.yihuantong.ui.main.MainActivity;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.io.File;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;

/**
 * 启动界面
 *
 * @author DUNDUN
 */
public class SplashActivity extends BaseActivity implements DocAuthStatus {
    @BindView(R.id.iv_start)
    ImageView ivBg;
    @BindView(R.id.act_splash_time_hint)
    TextView tvTimeHint;
    private ScheduledExecutorService executorService;
    private final String filePath = DirHelper.getPathImage() + "/splash.png";
    private int time = 0;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            tvTimeHint.setText(String.format(getString(R.string.txt_splash_time_hint), time));
            if (time <= 0) {
                initPage();
            }
            return true;
        }
    });

    @Override
    public int getLayoutID() {
        return R.layout.act_splash;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        hideBottomUIMenu();
        if (isExist()) {
            initSplashImg();
        }
        else {
            initScheduledThread();
        }
        //        getSplash();
    }

    @Override
    public void initListener() {
        super.initListener();
        tvTimeHint.setOnClickListener(this);
    }

    /**
     * 跳转登录界面
     */
    private void startLoginPage() {
        startActivity(new Intent(this, LoginOptionsActivity.class));
        finish();
    }

    /**
     * 页面初始化
     */
    private void initPage() {
        if (loginBean != null) {
            int checked = loginBean.getApprovalStatus();
            switch (checked) {
                case AUTH_NONE:
                case AUTH_WAITTING:
                case AUTH_FAILD:
                    startActivity(new Intent(this, LoginOptionsActivity.class));
                    startActivity(new Intent(this, AuthDoctorActivity.class));
                    finish();
                    break;
                case AUTH_SUCCESS:
                default:
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.putExtra(CommonData.KEY_EASE_LOGIN_STATUS, true);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_fade_out);
                    break;
            }
        }
        else {
            startLoginPage();
        }
    }

    private void initSplashImg() {
        File file = new File(filePath);
        Glide.with(this).load(file).into(ivBg);
        initScheduledThread();
    }

    private void initScheduledThread() {
        time = MAX_WAIT_TIME;
        executorService = new ScheduledThreadPoolExecutor(1, new BasicThreadFactory.Builder().namingPattern(
                "yht-thread-pool-%d").daemon(true).build());
        executorService.scheduleAtFixedRate(() -> {
            time--;
            if (time < 0) {
                time = 0;
                executorService.shutdownNow();
            }
            else {
                handler.sendEmptyMessage(0);
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    /**
     * 广告页
     */
    private void getSplash() {
        try {
            String name = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            RequestUtils.getSplash(this, "doctor", "android", name, this);
        }
        catch (PackageManager.NameNotFoundException e) {
            LogUtils.w(TAG, "Exception error!", e);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.act_splash_time_hint:
                if (executorService != null && !executorService.isShutdown()) {
                    executorService.shutdownNow();
                }
                initPage();
                break;
            default:
                break;
        }
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        switch (task) {
            case GET_SPLASH:
                String url = (String)response.getData();
                String oldUrl = sharePreferenceUtil.getString(CommonData.KEY_SPLASH_IMG_URL);
                boolean bool = !TextUtils.isEmpty(url) && !url.equals(oldUrl) || !isExist();
                if (bool) {
                    sharePreferenceUtil.putString(CommonData.KEY_SPLASH_IMG_URL, url);
                    downloadImg(url);
                }
                break;
            default:
                break;
        }
    }

    private void downloadImg(String url) {
        FileTransferServer.getInstance(this)
                          .downloadFile(0, url, DirHelper.getPathImage(), "splash.png", new DownloadListener() {
                              @Override
                              public void onDownloadError(int what, Exception exception) {
                              }

                              @Override
                              public void onStart(int what, boolean isResume, long rangeSize, Headers responseHeaders,
                                      long allCount) {
                              }

                              @Override
                              public void onProgress(int what, int progress, long fileCount, long speed) {
                              }

                              @Override
                              public void onFinish(int what, String filePath) {
                              }

                              @Override
                              public void onCancel(int what) {
                              }
                          });
    }

    /**
     * 判断本地文件是否存在
     *
     * @return bool
     */
    private boolean isExist() {
        File file = new File(filePath);
        return file.exists();
    }

    /**
     * 隐藏虚拟按键，并且全屏
     */
    protected void hideBottomUIMenu() {
        //状态栏透明
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }
}
