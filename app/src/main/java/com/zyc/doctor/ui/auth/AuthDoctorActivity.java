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

import com.hyphenate.chat.EMClient;
import com.yht.frame.data.DocAuthStatus;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.utils.SharePreferenceUtil;
import com.zyc.doctor.R;
import com.zyc.doctor.ui.auth.fragment.AuthBaseFragment;
import com.zyc.doctor.ui.auth.fragment.AuthLicenseFragment;
import com.zyc.doctor.ui.auth.fragment.AuthResultFragment;
import com.zyc.doctor.ui.auth.listener.OnStepListener;

import butterknife.BindView;

/**
 * @author 顿顿
 * @date 19/6/3 16:24
 * @des 医生认证
 */
public class AuthDoctorActivity extends BaseActivity implements OnStepListener {
    @BindView(R.id.iv_base)
    ImageView ivAuthBase;
    @BindView(R.id.tv_base)
    TextView tvAuthBase;
    @BindView(R.id.layout_base)
    LinearLayout layoutAuthBase;
    @BindView(R.id.iv_two)
    ImageView ivAuthLicense;
    @BindView(R.id.tv_two)
    TextView tvAuthLicense;
    @BindView(R.id.layout_two)
    LinearLayout layoutAuthLicense;
    @BindView(R.id.iv_end)
    ImageView ivAuthResult;
    @BindView(R.id.tv_end)
    TextView tvAuthResult;
    @BindView(R.id.layout_end)
    LinearLayout layoutAuthResult;
    @BindView(R.id.view_base)
    View viewAuthBase;
    @BindView(R.id.view_two_left)
    View viewAuthLicenseLeft;
    @BindView(R.id.view_two_right)
    View viewAuthLicenseRight;
    @BindView(R.id.view_end)
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
    /**
     * 当前审核状态
     */
    private int curAuthStatus = -1;

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
        curAuthStatus = loginBean.getApprovalStatus();
        initTab();
    }

    /**
     * 初始化tabs
     */
    private void initTab() {
        fragmentManager = getSupportFragmentManager();
        if (curAuthStatus == DocAuthStatus.AUTH_NONE) {
            tabAuthBaseView();
        }
        else {
            tabAuthResultView(curAuthStatus);
        }
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

    private void tabAuthResultView(int curAuthStatus) {
        transaction = fragmentManager.beginTransaction();
        hideAll(transaction);
        if (authResultFragment == null) {
            authResultFragment = new AuthResultFragment();
            authResultFragment.setCurAuthStatus(curAuthStatus);
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
                tabAuthResultView(DocAuthStatus.AUTH_WAITTING);
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
        if (curPage == 1) {
            curPage = 0;
            tabAuthBaseView();
            return false;
        }
        //返回登录页面 清除登录信息 退出环信等
        //清除本地数据
        SharePreferenceUtil.clear(this);
        //退出环信
        EMClient.getInstance().logout(true);
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
