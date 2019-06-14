package com.zyc.doctor.ui.check;

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
import com.zyc.doctor.ui.auth.listener.OnStepListener;
import com.zyc.doctor.ui.check.fragment.IdentifyFragment;
import com.zyc.doctor.ui.check.fragment.MaterialFragment;
import com.zyc.doctor.ui.check.fragment.SubmitFragment;

import butterknife.BindView;

/**
 * @author 顿顿
 * @date 19/6/14 14:05
 * @des 新增预约检查
 */
public class ReservationCheckActivity extends BaseActivity implements OnStepListener {
    @BindView(R.id.iv_base)
    ImageView ivReservationBase;
    @BindView(R.id.tv_base)
    TextView tvReservationBase;
    @BindView(R.id.layout_base)
    LinearLayout layoutReservationBase;
    @BindView(R.id.iv_two)
    ImageView ivReservationLicense;
    @BindView(R.id.tv_two)
    TextView tvReservationLicense;
    @BindView(R.id.layout_two)
    LinearLayout layoutReservationLicense;
    @BindView(R.id.iv_end)
    ImageView ivReservationResult;
    @BindView(R.id.tv_end)
    TextView tvReservationResult;
    @BindView(R.id.layout_end)
    LinearLayout layoutReservationResult;
    @BindView(R.id.view_base)
    View viewReservationBase;
    @BindView(R.id.view_two_left)
    View viewReservationLicenseLeft;
    @BindView(R.id.view_two_right)
    View viewReservationLicenseRight;
    @BindView(R.id.view_end)
    View viewReservationResult;
    /**
     * 碎片管理
     */
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    /**
     * 基本信息
     */
    private IdentifyFragment identifyFragment;
    /**
     * 完善资料
     */
    private MaterialFragment materialFragment;
    /**
     * 确认提交
     */
    private SubmitFragment submitFragment;
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
        return R.layout.act_reservation_check;
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
        tabReservationBaseView();
    }

    private void tabReservationBaseView() {
        transaction = fragmentManager.beginTransaction();
        hideAll(transaction);
        if (identifyFragment == null) {
            identifyFragment = new IdentifyFragment();
            identifyFragment.setOnStepListener(this);
            transaction.add(R.id.layout_frame_root, identifyFragment);
        }
        else {
            transaction.show(identifyFragment);
            identifyFragment.onResume();
        }
        transaction.commitAllowingStateLoss();
        selectTab(BASE_ZERO);
    }

    private void tabReservationLicenseView() {
        transaction = fragmentManager.beginTransaction();
        hideAll(transaction);
        if (materialFragment == null) {
            materialFragment = new MaterialFragment();
            materialFragment.setOnStepListener(this);
            transaction.add(R.id.layout_frame_root, materialFragment);
        }
        else {
            transaction.show(materialFragment);
            materialFragment.onResume();
        }
        transaction.commitAllowingStateLoss();
        selectTab(BASE_ONE);
    }

    private void tabReservationResultView() {
        transaction = fragmentManager.beginTransaction();
        hideAll(transaction);
        if (submitFragment == null) {
            submitFragment = new SubmitFragment();
            submitFragment.setOnStepListener(this);
            transaction.add(R.id.layout_frame_root, submitFragment);
        }
        else {
            transaction.show(submitFragment);
            submitFragment.onResume();
        }
        transaction.commitAllowingStateLoss();
        selectTab(BASE_TWO);
    }

    /**
     * 隐藏所有碎片
     */
    private void hideAll(FragmentTransaction transaction) {
        if (identifyFragment != null) {
            transaction.hide(identifyFragment);
        }
        if (materialFragment != null) {
            transaction.hide(materialFragment);
        }
        if (submitFragment != null) {
            transaction.hide(submitFragment);
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
                tvReservationBase.setSelected(true);
                viewReservationBase.setSelected(false);
                ivReservationBase.setImageResource(R.mipmap.ic_step_sel);
                tvReservationBase.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                //1
                tvReservationLicense.setSelected(false);
                viewReservationLicenseLeft.setSelected(false);
                ivReservationLicense.setImageResource(R.mipmap.ic_step_def);
                tvReservationLicense.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                break;
            case BASE_ONE:
                curPage = BASE_ONE;
                //0
                viewReservationBase.setSelected(true);
                tvReservationBase.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                ivReservationBase.setImageResource(R.mipmap.ic_step_finish);
                //1
                tvReservationLicense.setSelected(true);
                viewReservationLicenseLeft.setSelected(true);
                viewReservationLicenseRight.setSelected(false);
                ivReservationLicense.setImageResource(R.mipmap.ic_step_sel);
                tvReservationLicense.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                //2
                layoutReservationResult.setSelected(false);
                ivReservationResult.setImageResource(R.mipmap.ic_step_def);
                tvReservationResult.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                break;
            case BASE_TWO:
                curPage = BASE_TWO;
                //1
                viewReservationLicenseRight.setSelected(true);
                tvReservationLicense.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                ivReservationLicense.setImageResource(R.mipmap.ic_step_finish);
                //2
                layoutReservationResult.setSelected(true);
                ivReservationResult.setImageResource(R.mipmap.ic_step_sel);
                tvReservationResult.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                break;
            default:
                curPage = BASE_ZERO;
                //0
                tvReservationBase.setSelected(true);
                viewReservationBase.setSelected(false);
                ivReservationBase.setImageResource(R.mipmap.ic_step_sel);
                tvReservationBase.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                //1
                tvReservationLicense.setSelected(false);
                viewReservationLicenseLeft.setSelected(false);
                ivReservationLicense.setImageResource(R.mipmap.ic_step_def);
                tvReservationLicense.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
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
        tabReservationLicenseView();
    }

    @Override
    public void onStepTwo(int type) {
        switch (type) {
            case BASE_ONE:
                tabReservationBaseView();
                break;
            case BASE_TWO:
                tabReservationResultView();
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
     * @return bool
     */
    private boolean finishPage() {
        if (curPage == BASE_TWO) {
            curPage = 1;
            tabReservationLicenseView();
            return false;
        }
        else if (curPage == 1) {
            curPage = 0;
            tabReservationBaseView();
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
    }

    @Override
    public void onPermissionGranted(@NonNull String[] permissionName) {
    }

    @Override
    public void onPermissionDeclined(@NonNull String[] permissionName) {
    }

    @Override
    public void onPermissionPreGranted(@NonNull String permissionsName) {
    }

    @Override
    public void onPermissionNeedExplanation(@NonNull String permissionName) {
    }

    @Override
    public void onPermissionReallyDeclined(@NonNull String permissionName) {
    }

    @Override
    public void onNoPermissionNeeded(@NonNull Object permissionName) {
    }
}
