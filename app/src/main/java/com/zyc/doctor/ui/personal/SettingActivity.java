package com.zyc.doctor.ui.personal;

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

import com.yht.frame.ui.BaseActivity;
import com.yht.frame.utils.LogUtils;
import com.yht.frame.widgets.dialog.HintDialog;
import com.zyc.doctor.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/6/10 13:39
 * @des
 */
public class SettingActivity extends BaseActivity {
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

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_setting;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        getAppVersionCode();
    }

    @OnClick({ R.id.layout_about, R.id.layout_version, R.id.tv_exit })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_about:
                startActivity(new Intent(this, AboutActivity.class));
                break;
            case R.id.layout_version:
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

    private void getAppVersionCode() {
        try {
            String name = getPackageManager().getPackageInfo(getPackageName(), 0).versionName + "";
            if (!TextUtils.isEmpty(name)) {
                tvVersionName.setText("V" + name);
            }
        }
        catch (PackageManager.NameNotFoundException e) {
            LogUtils.w(TAG, "Exception error!", e);
        }
    }

    /**
     * 退出登录
     */
    private void exit() {
    }
}
