package com.zyc.doctor.ui.reservation;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yht.frame.data.CommonData;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.widgets.dialog.HintDialog;
import com.zyc.doctor.R;
import com.zyc.doctor.ui.check.CheckSuccessActivity;
import com.zyc.doctor.ui.check.listener.OnCheckListener;
import com.zyc.doctor.ui.reservation.fragment.IdentifyFragment;
import com.zyc.doctor.ui.reservation.fragment.MaterialFragment;
import com.zyc.doctor.ui.reservation.fragment.SubmitCheckFragment;
import com.zyc.doctor.ui.reservation.fragment.SubmitTransferFragment;

import butterknife.BindView;

/**
 * @author 顿顿
 * @date 19/6/14 14:05
 * @des 新增 预约检查 预约转诊
 */
public class ReservationCheckOrTransferActivity extends BaseActivity implements OnCheckListener {
    @BindView(R.id.public_title_bar_title)
    TextView publicTitleBarTitle;
    @BindView(R.id.iv_base)
    ImageView ivReservationBase;
    @BindView(R.id.tv_base)
    TextView tvReservationBase;
    @BindView(R.id.layout_base)
    LinearLayout layoutReservationBase;
    @BindView(R.id.iv_two)
    ImageView ivReservationMaterial;
    @BindView(R.id.tv_two)
    TextView tvReservationMaterial;
    @BindView(R.id.layout_two)
    LinearLayout layoutReservationMaterial;
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
    @BindView(R.id.public_title_bar_more)
    TextView publicTitleBarMore;
    @BindView(R.id.layout_frame_root)
    FrameLayout layoutFrameRoot;
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
     * 确认提交(预约检查)
     */
    private SubmitCheckFragment submitCheckFragment;
    /**
     * 确认提交(预约转诊)
     */
    private SubmitTransferFragment submitTransferFragment;
    /**
     * 当前碎片
     */
    private int curPage;
    /**
     * 姓名 身份证
     */
    private String name, idCard;
    /**
     * 是否为转诊
     */
    private boolean istransfer;

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
        if (getIntent() != null) {
            istransfer = getIntent().getBooleanExtra(CommonData.KEY_CHECK_OR_TRANSFER, false);
        }
        initTitlePage();
        initTab();
    }

    /**
     * title处理
     */
    private void initTitlePage() {
        //title
        if (istransfer) {
            publicTitleBarTitle.setText(R.string.txt_reserve_transfer);
        }
        //自定义返回键实践处理
        findViewById(R.id.public_title_bar_back).setOnClickListener(this);
        publicTitleBarMore.setVisibility(View.VISIBLE);
        publicTitleBarMore.setText(R.string.txt_contact);
        publicTitleBarMore.setOnClickListener(this);
        tvReservationBase.setText(R.string.txt_identify);
        tvReservationMaterial.setText(R.string.txt_material);
        tvReservationResult.setText(R.string.txt_sure_submit);
    }

    /**
     * 初始化tabs
     */
    private void initTab() {
        fragmentManager = getSupportFragmentManager();
        tabReservationBaseView();
    }

    private void tabReservationBaseView() {
        transaction = fragmentManager.beginTransaction();
        hideAll(transaction);
        if (identifyFragment == null) {
            identifyFragment = new IdentifyFragment();
            identifyFragment.setOnCheckListener(this);
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
            materialFragment.setOnCheckListener(this);
            materialFragment.setValue(name, idCard);
            transaction.add(R.id.layout_frame_root, materialFragment);
        }
        else {
            transaction.show(materialFragment);
            materialFragment.setValue(name, idCard);
            materialFragment.onResume();
        }
        transaction.commitAllowingStateLoss();
        selectTab(BASE_ONE);
    }

    /**
     * 预约检查提交碎片
     */
    private void tabCheckResultView() {
        transaction = fragmentManager.beginTransaction();
        hideAll(transaction);
        if (submitCheckFragment == null) {
            submitCheckFragment = new SubmitCheckFragment();
            submitCheckFragment.setOnCheckListener(this);
            transaction.add(R.id.layout_frame_root, submitCheckFragment);
        }
        else {
            transaction.show(submitCheckFragment);
            submitCheckFragment.onResume();
        }
        transaction.commitAllowingStateLoss();
        selectTab(BASE_TWO);
    }

    /**
     * 预约转诊提交碎片
     */
    private void tabTransferResultView() {
        transaction = fragmentManager.beginTransaction();
        hideAll(transaction);
        if (submitTransferFragment == null) {
            submitTransferFragment = new SubmitTransferFragment();
            submitTransferFragment.setOnCheckListener(this);
            transaction.add(R.id.layout_frame_root, submitTransferFragment);
        }
        else {
            transaction.show(submitTransferFragment);
            submitTransferFragment.onResume();
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
        if (submitCheckFragment != null) {
            transaction.hide(submitCheckFragment);
        }
        if (submitTransferFragment != null) {
            transaction.hide(submitTransferFragment);
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
                tvReservationMaterial.setSelected(false);
                viewReservationLicenseLeft.setSelected(false);
                ivReservationMaterial.setImageResource(R.mipmap.ic_step_def);
                tvReservationMaterial.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                break;
            case BASE_ONE:
                curPage = BASE_ONE;
                //0
                viewReservationBase.setSelected(true);
                tvReservationBase.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                ivReservationBase.setImageResource(R.mipmap.ic_step_finish);
                //1
                tvReservationMaterial.setSelected(true);
                viewReservationLicenseLeft.setSelected(true);
                viewReservationLicenseRight.setSelected(false);
                ivReservationMaterial.setImageResource(R.mipmap.ic_step_sel);
                tvReservationMaterial.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                //2
                layoutReservationResult.setSelected(false);
                ivReservationResult.setImageResource(R.mipmap.ic_step_def);
                tvReservationResult.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                break;
            case BASE_TWO:
                curPage = BASE_TWO;
                //1
                viewReservationLicenseRight.setSelected(true);
                tvReservationMaterial.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                ivReservationMaterial.setImageResource(R.mipmap.ic_step_finish);
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
                tvReservationMaterial.setSelected(false);
                viewReservationLicenseLeft.setSelected(false);
                ivReservationMaterial.setImageResource(R.mipmap.ic_step_def);
                tvReservationMaterial.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
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
            case R.id.public_title_bar_more:
                new HintDialog(this).setPhone("").setOnEnterClickListener(() -> callPhone("")).show();
                break;
            default:
                break;
        }
    }

    @Override
    public void onStepOne(String name, String idCard) {
        this.name = name;
        this.idCard = idCard;
        tabReservationLicenseView();
    }

    @Override
    public void onStepTwo() {
        if (istransfer) {
            tabTransferResultView();
        }
        else {
            tabCheckResultView();
        }
    }

    @Override
    public void onStepThree() {
        Intent intent = new Intent(this, CheckSuccessActivity.class);
        if (istransfer) {
            intent.putExtra(CommonData.KEY_CHECK_OR_TRANSFER, true);
        }
        startActivity(intent);
        finish();
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
        if (submitCheckFragment != null) {
            submitCheckFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        if (submitTransferFragment != null) {
            submitTransferFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onPermissionNeedExplanation(@NonNull String permissionName) {
        if (submitCheckFragment != null) {
            submitCheckFragment.onPermissionNeedExplanation(permissionName);
        }
        if (submitTransferFragment != null) {
            submitTransferFragment.onPermissionNeedExplanation(permissionName);
        }
    }

    @Override
    public void onNoPermissionNeeded(@NonNull Object permissionName) {
        super.onNoPermissionNeeded(permissionName);
        if (submitCheckFragment != null) {
            submitCheckFragment.onNoPermissionNeeded(permissionName);
        }
        if (submitTransferFragment != null) {
            submitTransferFragment.onNoPermissionNeeded(permissionName);
        }
    }
}
