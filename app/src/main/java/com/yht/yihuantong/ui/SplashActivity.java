package com.yht.yihuantong.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import android.text.TextUtils;

import com.hyphenate.chat.EMClient;
import com.yht.frame.data.CommonData;
import com.yht.frame.data.type.DocAuthStatus;
import com.yht.frame.ui.BaseActivity;
import com.yht.yihuantong.BuildConfig;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ZycApplication;
import com.yht.yihuantong.ui.auth.AuthDoctorActivity;
import com.yht.yihuantong.ui.login.LoginOptionsActivity;
import com.yht.yihuantong.ui.main.MainActivity;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.io.IOException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * 启动界面
 *
 * @author DUNDUN
 */
public class SplashActivity extends BaseActivity implements DocAuthStatus {
    @BindView(R.id.gif_image)
    GifImageView gifImage;
    private ScheduledExecutorService executorService;
    private int time = 0;

    @Override
    public int getLayoutID() {
        return R.layout.act_splash;
    }

    private Handler handler = new Handler(message -> {
        if (time <= 0) {
            initPage();
        }
        return true;
    });

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        EMClient.getInstance().groupManager().loadAllGroups();
        EMClient.getInstance().chatManager().loadAllConversations();
        if (loginBean != null) {
            // update current user's display name for APNs
            EMClient.getInstance().pushManager().updatePushNickname(loginBean.getDoctorName());
        }
        try {
            GifDrawable gifDrawable = new GifDrawable(getResources(), R.mipmap.pic_splash_gif);
            initScheduledThread(gifDrawable.getDuration());
            gifImage.setImageDrawable(gifDrawable);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        //自定义url 只在调试模式下
        if (ZycApplication.getInstance().debugMode) {
            String url = sharePreferenceUtil.getAlwaysString(CommonData.KEY_BASE_URL);
            if (!TextUtils.isEmpty(url) && !TextUtils.equals(url, BuildConfig.BASE_BASIC_URL)) {
                ZycApplication.getInstance().updateBaseUrl(url);
            }
        }
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
                    overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_fade_out);
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

    /**
     * 跳转登录界面
     */
    private void startLoginPage() {
        startActivity(new Intent(this, LoginOptionsActivity.class));
        finish();
        overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_fade_out);
    }

    private void initScheduledThread(int duration) {
        time = Math.round(duration / 1000f) + 2;
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
}
