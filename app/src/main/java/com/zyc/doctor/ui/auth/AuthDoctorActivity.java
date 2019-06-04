package com.zyc.doctor.ui.auth;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yht.frame.ui.BaseActivity;
import com.zyc.doctor.R;
import com.zyc.doctor.ui.auth.fragment.AuthBaseFragment;
import com.zyc.doctor.ui.auth.fragment.AuthLicenseFragment;
import com.zyc.doctor.ui.auth.fragment.AuthResultFragment;
import com.zyc.doctor.ui.auth.listener.OnAuthStepListener;

import butterknife.BindView;

/**
 * @author 顿顿
 * @date 19/6/3 16:24
 * @des 医生认证
 */
public class AuthDoctorActivity extends BaseActivity implements OnAuthStepListener {
    @BindView(R.id.iv_auth_base)
    ImageView ivAuthBase;
    @BindView(R.id.tv_auth_base)
    TextView tvAuthBase;
    @BindView(R.id.layout_auth_base)
    LinearLayout layoutAuthBase;
    @BindView(R.id.iv_auth_license)
    ImageView ivAuthLicense;
    @BindView(R.id.tv_auth_license)
    TextView tvAuthLicense;
    @BindView(R.id.layout_auth_license)
    LinearLayout layoutAuthLicense;
    @BindView(R.id.iv_auth_result)
    ImageView ivAuthResult;
    @BindView(R.id.tv_auth_result)
    TextView tvAuthResult;
    @BindView(R.id.layout_auth_result)
    LinearLayout layoutAuthResult;
    @BindView(R.id.view_auth_base)
    View viewAuthBase;
    @BindView(R.id.view_auth_license_left)
    View viewAuthLicenseLeft;
    @BindView(R.id.view_auth_license_right)
    View viewAuthLicenseRight;
    @BindView(R.id.view_auth_result)
    View viewAuthResult;
    /**
     * 碎片管理
     */
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    /**
     * 基本信息
     */
    private AuthBaseFragment authBaseFragment;
    /**
     * 医生执照
     */
    private AuthLicenseFragment authLicenseFragment;
    /**
     * 认证结果
     */
    private AuthResultFragment authResultFragment;

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_auth;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        initTab();
    }

    /**
     * 初始化tabs
     */
    private void initTab() {
        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        tabAuthBaseView();
    }

    private void tabAuthBaseView() {
        transaction = fragmentManager.beginTransaction();
        hideAll(transaction);
        if (authBaseFragment == null) {
            authBaseFragment = new AuthBaseFragment();
            authBaseFragment.setOnAuthStepListener(this);
            transaction.add(R.id.layout_frame_root, authBaseFragment);
        }
        else {
            transaction.show(authBaseFragment);
            authBaseFragment.onResume();
        }
        transaction.commitAllowingStateLoss();
        selectTab(0);
    }

    private void tabAuthLicenseView() {
        transaction = fragmentManager.beginTransaction();
        hideAll(transaction);
        if (authLicenseFragment == null) {
            authLicenseFragment = new AuthLicenseFragment();
            authLicenseFragment.setOnAuthStepListener(this);
            transaction.add(R.id.layout_frame_root, authLicenseFragment);
        }
        else {
            transaction.show(authLicenseFragment);
            authLicenseFragment.onResume();
        }
        transaction.commitAllowingStateLoss();
        selectTab(1);
    }

    private void tabAuthResultView() {
        transaction = fragmentManager.beginTransaction();
        hideAll(transaction);
        if (authResultFragment == null) {
            authResultFragment = new AuthResultFragment();
            authResultFragment.setOnAuthStepListener(this);
            transaction.add(R.id.layout_frame_root, authResultFragment);
        }
        else {
            transaction.show(authResultFragment);
            authResultFragment.onResume();
        }
        transaction.commitAllowingStateLoss();
        selectTab(2);
    }

    /**
     * 隐藏所有碎片
     */
    private void hideAll(FragmentTransaction transaction) {
        if (authBaseFragment != null) {
            transaction.hide(authBaseFragment);
        }
        if (authLicenseFragment != null) {
            transaction.hide(authLicenseFragment);
        }
        if (authResultFragment != null) {
            transaction.hide(authResultFragment);
        }
    }

    /**
     * 选中tab
     */
    private void selectTab(int index) {
        switch (index) {
            case 0:
                ivAuthBase.setSelected(true);
                tvAuthBase.setSelected(true);
                break;
            case 1:
                //设置不为加粗
                tvAuthBase.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                layoutAuthBase.setSelected(true);
                ivAuthBase.setImageResource(R.mipmap.ic_step_finish);
                //认证
                viewAuthLicenseLeft.setSelected(true);
                ivAuthLicense.setSelected(true);
                tvAuthLicense.setSelected(true);
                tvAuthLicense.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                break;
            case 2:
                layoutAuthLicense.setSelected(true);
                tvAuthLicense.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                ivAuthLicense.setImageResource(R.mipmap.ic_step_finish);
                //结果
                layoutAuthResult.setSelected(true);
                break;
            default:
                ivAuthBase.setSelected(true);
                tvAuthBase.setSelected(true);
                break;
        }
    }

    @Override
    public void onStepOne() {
        tabAuthLicenseView();
    }

    @Override
    public void onStepTwo() {
    }

    @Override
    public void onStepThree() {
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        if (authBaseFragment != null) {
            authBaseFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onPermissionGranted(@NonNull String[] permissionName) {
        if (authBaseFragment != null) {
            authBaseFragment.onPermissionGranted(permissionName);
        }
    }

    @Override
    public void onPermissionDeclined(@NonNull String[] permissionName) {
        if (authBaseFragment != null) {
            authBaseFragment.onPermissionDeclined(permissionName);
        }
    }

    @Override
    public void onPermissionPreGranted(@NonNull String permissionsName) {
        if (authBaseFragment != null) {
            authBaseFragment.onPermissionPreGranted(permissionsName);
        }
    }

    @Override
    public void onPermissionNeedExplanation(@NonNull String permissionName) {
        if (authBaseFragment != null) {
            authBaseFragment.onPermissionNeedExplanation(permissionName);
        }
    }

    @Override
    public void onPermissionReallyDeclined(@NonNull String permissionName) {
        if (authBaseFragment != null) {
            authBaseFragment.onPermissionReallyDeclined(permissionName);
        }
    }

    @Override
    public void onNoPermissionNeeded(@NonNull Object permissionName) {
        if (authBaseFragment != null) {
            authBaseFragment.onNoPermissionNeeded(permissionName);
        }
    }
}
