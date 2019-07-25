package com.yht.yihuantong.ui.personal;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.yht.frame.data.bean.VersionBean;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.utils.HuiZhenLog;
import com.yht.frame.utils.ToastUtil;
import com.yht.frame.widgets.dialog.HintDialog;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ui.dialog.UpdateDialog;
import com.yht.yihuantong.utils.NotifySettingUtils;
import com.yht.yihuantong.version.presenter.VersionPresenter;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/6/10 13:39
 * @des
 */
public class SettingActivity extends BaseActivity
        implements VersionPresenter.VersionViewListener, UpdateDialog.OnEnterClickListener {
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
        swMessageControl.setFocusable(false);
        swMessageControl.setClickable(false);
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
        if (NotifySettingUtils.hasNotify(this)) {
            swMessageControl.setChecked(true);
        }
        else {
            swMessageControl.setChecked(false);
        }
    }

    @OnClick({ R.id.layout_notify, R.id.layout_about, R.id.layout_version, R.id.tv_exit })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_about:
                startActivity(new Intent(this, AboutActivity.class));
                break;
            case R.id.layout_notify:
                NotifySettingUtils.openNotifySetting(this);
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
