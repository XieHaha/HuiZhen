package com.zyc.doctor.ui.auth;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
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
    /**
     * 当前碎片
     */
    private int curPage;

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
        findViewById(R.id.public_title_bar_back).setOnClickListener(this);
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
        selectTab(BASE_ZERO);
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
        selectTab(BASE_ONE);
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
        selectTab(BASE_TWO);
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
            case BASE_ZERO:
                curPage = BASE_ZERO;
                //0
                tvAuthBase.setSelected(true);
                viewAuthBase.setSelected(false);
                ivAuthBase.setImageResource(R.mipmap.ic_step_sel);
                tvAuthBase.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                //1
                tvAuthLicense.setSelected(false);
                viewAuthLicenseLeft.setSelected(false);
                ivAuthLicense.setImageResource(R.mipmap.ic_step_def);
                tvAuthLicense.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                break;
            case BASE_ONE:
                curPage = BASE_ONE;
                //0
                viewAuthBase.setSelected(true);
                tvAuthBase.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                ivAuthBase.setImageResource(R.mipmap.ic_step_finish);
                //1
                tvAuthLicense.setSelected(true);
                viewAuthLicenseLeft.setSelected(true);
                viewAuthLicenseRight.setSelected(false);
                ivAuthLicense.setImageResource(R.mipmap.ic_step_sel);
                tvAuthLicense.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                //2
                layoutAuthResult.setSelected(false);
                ivAuthResult.setImageResource(R.mipmap.ic_step_def);
                tvAuthResult.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                break;
            case BASE_TWO:
                curPage = BASE_TWO;
                //1
                viewAuthLicenseRight.setSelected(true);
                tvAuthLicense.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                ivAuthLicense.setImageResource(R.mipmap.ic_step_finish);
                //2
                layoutAuthResult.setSelected(true);
                ivAuthResult.setImageResource(R.mipmap.ic_step_sel);
                tvAuthResult.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                break;
            default:
                curPage = BASE_ZERO;
                //0
                tvAuthBase.setSelected(true);
                viewAuthBase.setSelected(false);
                ivAuthBase.setImageResource(R.mipmap.ic_step_sel);
                tvAuthBase.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                //1
                tvAuthLicense.setSelected(false);
                viewAuthLicenseLeft.setSelected(false);
                ivAuthLicense.setImageResource(R.mipmap.ic_step_def);
                tvAuthLicense.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                break;
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.public_title_bar_back:
                if (finishPage()) {
                    finish();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onStepOne() {
        tabAuthLicenseView();
    }

    @Override
    public void onStepTwo(int type) {
        switch (type) {
            case BASE_ONE:
                tabAuthBaseView();
                break;
            case BASE_TWO:
                tabAuthResultView();
                break;
            default:
                break;
        }
    }

    @Override
    public void onStepThree() {
    }

    /**
     * 页面逻辑处理
     *
     * @return
     */
    private boolean finishPage() {
        if (curPage == BASE_TWO) {
            curPage = 1;
            tabAuthLicenseView();
            return false;
        }
        else if (curPage == 1) {
            curPage = 0;
            tabAuthBaseView();
            return false;
        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (!finishPage()) {
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        switch (curPage) {
            case BASE_ZERO:
                if (authBaseFragment != null) {
                    authBaseFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
                }
                break;
            case BASE_ONE:
                if (authLicenseFragment != null) {
                    authLicenseFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onPermissionGranted(@NonNull String[] permissionName) {
        switch (curPage) {
            case BASE_ZERO:
                if (authBaseFragment != null) {
                    authBaseFragment.onPermissionGranted(permissionName);
                }
                break;
            case BASE_ONE:
                if (authLicenseFragment != null) {
                    authLicenseFragment.onPermissionGranted(permissionName);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onPermissionDeclined(@NonNull String[] permissionName) {
        switch (curPage) {
            case BASE_ZERO:
                if (authBaseFragment != null) {
                    authBaseFragment.onPermissionDeclined(permissionName);
                }
                break;
            case BASE_ONE:
                if (authLicenseFragment != null) {
                    authLicenseFragment.onPermissionDeclined(permissionName);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onPermissionPreGranted(@NonNull String permissionsName) {
        switch (curPage) {
            case BASE_ZERO:
                if (authBaseFragment != null) {
                    authBaseFragment.onPermissionPreGranted(permissionsName);
                }
                break;
            case BASE_ONE:
                if (authLicenseFragment != null) {
                    authLicenseFragment.onPermissionPreGranted(permissionsName);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onPermissionNeedExplanation(@NonNull String permissionName) {
        switch (curPage) {
            case BASE_ZERO:
                if (authBaseFragment != null) {
                    authBaseFragment.onPermissionNeedExplanation(permissionName);
                }
                break;
            case BASE_ONE:
                if (authLicenseFragment != null) {
                    authLicenseFragment.onPermissionNeedExplanation(permissionName);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onPermissionReallyDeclined(@NonNull String permissionName) {
        switch (curPage) {
            case BASE_ZERO:
                if (authBaseFragment != null) {
                    authBaseFragment.onPermissionReallyDeclined(permissionName);
                }
                break;
            case BASE_ONE:
                if (authLicenseFragment != null) {
                    authLicenseFragment.onPermissionReallyDeclined(permissionName);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onNoPermissionNeeded(@NonNull Object permissionName) {
        switch (curPage) {
            case BASE_ZERO:
                if (authBaseFragment != null) {
                    authBaseFragment.onNoPermissionNeeded(permissionName);
                }
                break;
            case BASE_ONE:
                if (authLicenseFragment != null) {
                    authLicenseFragment.onNoPermissionNeeded(permissionName);
                }
                break;
            default:
                break;
        }
    }
}
