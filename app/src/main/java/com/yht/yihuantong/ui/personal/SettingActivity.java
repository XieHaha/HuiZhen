package com.yht.yihuantong.ui.personal;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationManagerCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.yht.frame.data.CommonData;
import com.yht.frame.data.bean.VersionBean;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.utils.HuiZhenLog;
import com.yht.frame.utils.ToastUtil;
import com.yht.frame.widgets.dialog.HintDialog;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ui.dialog.UpdateDialog;
import com.yht.yihuantong.version.presenter.VersionPresenter;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Notification.EXTRA_CHANNEL_ID;
import static android.provider.Settings.EXTRA_APP_PACKAGE;

/**
 * @author 顿顿
 * @date 19/6/10 13:39
 * @des
 */
public class SettingActivity extends BaseActivity
        implements VersionPresenter.VersionViewListener, UpdateDialog.OnEnterClickListener,
                   CompoundButton.OnCheckedChangeListener {
    private static final String TAG = "SettingActivity";
    @BindView(R.id.sw_message_control)
    Switch swMessageControl;
    @BindView(R.id.layout_about)
    LinearLayout layoutAbout;
    @BindView(R.id.dot_new_version)
    RelativeLayout dotNewVersion;
    @BindView(R.id.tv_version_name)
    TextView tvVersionName;
    @BindView(R.id.layout_version)
    LinearLayout layoutVersion;
    @BindView(R.id.tv_exit)
    TextView tvExit;
    /**
     * 版本检测
     */
    private VersionPresenter mVersionPresenter;
    /**
     * 版本弹窗
     */
    private UpdateDialog updateDialog;

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_setting;
    }

    @Override
    protected void onResume() {
        super.onResume();
        initNotify();
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        swMessageControl.setOnCheckedChangeListener(this);
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        //检查更新
        mVersionPresenter = new VersionPresenter(this, loginBean.getToken());
        mVersionPresenter.setVersionViewListener(this);
        getAppVersionCode();
    }

    /**
     * 权限开关
     */
    private void initNotify() {
        boolean notify = sharePreferenceUtil.getBoolean(CommonData.KEY_NOTIFICATION_CONTROL);
        if (hasNotify()) {
            swMessageControl.setChecked(notify);
        }
        else {
            swMessageControl.setChecked(false);
        }
    }

    /**
     * 检测通知权限
     */
    private boolean hasNotify() {
        NotificationManagerCompat manager = NotificationManagerCompat.from(getApplicationContext());
        return manager.areNotificationsEnabled();
    }

    @OnClick({ R.id.layout_about, R.id.layout_version, R.id.tv_exit })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_about:
                startActivity(new Intent(this, AboutActivity.class));
                break;
            case R.id.layout_version:
                mVersionPresenter.init();
                break;
            case R.id.tv_exit:
                new HintDialog(this).setTitleString(getString(R.string.txt_app_name))
                                    .setContentString(getString(R.string.txt_set_exit_sure))
                                    .setEnterBtnTxt(getString(R.string.txt_exit))
                                    .setOnEnterClickListener(() -> exit())
                                    .show();
                break;
            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            if (!hasNotify()) {
                HintDialog dialog = new HintDialog(this);
                dialog.setEnterBtnTxt(getString(R.string.txt_open));
                dialog.setEnterSelect(true);
                dialog.setOnEnterClickListener(() -> openNotifySetting());
                dialog.setContentString(getString(R.string.dialog_no_notify_tip));
                dialog.show();
            }
        }
        sharePreferenceUtil.putBoolean(CommonData.KEY_NOTIFICATION_CONTROL, isChecked);
    }

    private void openNotifySetting() {
        try {
            // 根据isOpened结果，判断是否需要提醒用户跳转AppInfo页面，去打开App通知权限
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            //这种方案适用于 API 26, 即8.0（含8.0）以上可以用
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                intent.putExtra(EXTRA_CHANNEL_ID, getApplicationInfo().uid);
                intent.putExtra(EXTRA_APP_PACKAGE, getPackageName());
            }
            else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //这种方案适用于 API21——25，即 5.0——7.1 之间的版本可以使用
                intent.putExtra("app_package", getPackageName());
                intent.putExtra("app_uid", getApplicationInfo().uid);
            }
            startActivity(intent);
        }
        catch (Exception e) {
            e.printStackTrace();
            // 出现异常则跳转到应用设置界面：锤子坚果3——OC105 API25
            Intent intent = new Intent();
            //下面这种方案是直接跳转到当前应用的设置界面。
            //https://blog.csdn.net/ysy950803/article/details/71910806
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivity(intent);
        }
    }

    /*********************版本更新回调*************************/
    @Override
    public void updateVersion(VersionBean version, int mode, boolean isDownLoading) {
        if (mode == -1) {
            ToastUtil.toast(this, R.string.toast_version_update_hint);
            return;
        }
        updateDialog = new UpdateDialog(this);
        updateDialog.setCancelable(false);
        updateDialog.setUpdateMode(mode).setData(version.getNotes());
        updateDialog.setOnEnterClickListener(this);
        updateDialog.show();
    }

    @Override
    public void updateLoading(long total, long current) {
        if (updateDialog != null && updateDialog.isShowing()) {
            updateDialog.setProgressValue(total, current);
        }
    }

    /**
     * 当无可用网络时回调
     */
    @Override
    public void updateNetWorkError() {
    }

    @Override
    public void onEnter(boolean isMustUpdate) {
        mVersionPresenter.getNewAPK(isMustUpdate);
        ToastUtil.toast(this, R.string.txt_download_hint);
    }

    private void getAppVersionCode() {
        try {
            String name = getPackageManager().getPackageInfo(getPackageName(), 0).versionName + "";
            if (!TextUtils.isEmpty(name)) {
                tvVersionName.setText("V" + name);
            }
        }
        catch (PackageManager.NameNotFoundException e) {
            HuiZhenLog.w(TAG, "Exception error!", e);
        }
    }
}
