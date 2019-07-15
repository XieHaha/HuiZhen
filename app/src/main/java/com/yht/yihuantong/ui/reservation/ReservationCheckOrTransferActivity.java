package com.yht.yihuantong.ui.reservation;

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

import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.CommonData;
import com.yht.frame.data.Tasks;
import com.yht.frame.data.base.PatientDetailBean;
import com.yht.frame.data.base.ReserveCheckBean;
import com.yht.frame.data.base.ReserveTransferBean;
import com.yht.frame.data.base.TransferBean;
import com.yht.frame.http.retrofit.RequestUtils;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.widgets.dialog.HintDialog;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ui.check.listener.OnCheckListener;
import com.yht.yihuantong.ui.reservation.fragment.IdentifyFragment;
import com.yht.yihuantong.ui.reservation.fragment.MaterialFragment;
import com.yht.yihuantong.ui.reservation.fragment.SubmitCheckFragment;
import com.yht.yihuantong.ui.reservation.fragment.SubmitTransferFragment;

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
    private FragmentTransaction fragmentTransaction;
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
     * 患者回填数据
     */
    private PatientDetailBean patientDetailBean;
    /**
     * 重新转诊 数据回填
     */
    private TransferBean transferBean;
    /**
     * 当前预约转诊数据
     */
    private ReserveTransferBean reverseTransferBean;
    /**
     * 当前预约检查数据
     */
    private ReserveCheckBean reserveCheckBean;
    /**
     * 当前碎片
     */
    private int curPage;
    /**
     * 是否为转诊
     */
    private boolean isTransfer;

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_reservation_check;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        fragmentManager = getSupportFragmentManager();
        if (getIntent() != null) {
            isTransfer = getIntent().getBooleanExtra(CommonData.KEY_CHECK_OR_TRANSFER, false);
            //患者详情页面回传数据
            patientDetailBean = (PatientDetailBean)getIntent().getSerializableExtra(CommonData.KEY_PATIENT_BEAN);
            //转诊详情页面回传数据(重新转诊)
            transferBean = (TransferBean)getIntent().getSerializableExtra(CommonData.KEY_TRANSFER_ORDER_BEAN);
        }
        initTitlePage();
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        if (hasHistoryData()) {
            //重新转诊直接进入到第二步
            tabReservationLicenseView();
        }
        else {
            tabReservationBaseView();
        }
    }

    /**
     * 新增预约转诊订单
     */
    private void addReserveTransferOrder() {
        RequestUtils.addReserveTransferOrder(this, loginBean.getToken(), reverseTransferBean, this);
    }

    /**
     * 新增预约检查订单
     */
    private void addReserveCheckOrder() {
        RequestUtils.addReserveCheckOrder(this, loginBean.getToken(), reserveCheckBean, this);
    }

    /**
     * title处理
     */
    private void initTitlePage() {
        //title
        if (isTransfer) {
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
     * 已有数据回填（包括患者基本数据或者订单数据）
     * 返回true  表示有数据需要回填
     */
    private boolean hasHistoryData() {
        if (patientDetailBean != null) {
            initPatientBaseData();
            return true;
        }
        else if (transferBean != null) {
            initTransferOrderData();
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * 患者基本数据回填
     */
    private void initPatientBaseData() {
        if (isTransfer) {
            //转诊 数据回填
            reverseTransferBean = new ReserveTransferBean();
            reverseTransferBean.setPatientName(patientDetailBean.getName());
            reverseTransferBean.setPatientIdCardNo(patientDetailBean.getIdCard());
            reverseTransferBean.setPatientCode(patientDetailBean.getCode());
            reverseTransferBean.setSex(patientDetailBean.getSex());
            reverseTransferBean.setPatientAge(patientDetailBean.getAge());
            reverseTransferBean.setPatientMobile(patientDetailBean.getMobile());
            reverseTransferBean.setPastHistory(patientDetailBean.getPast());
            reverseTransferBean.setFamilyHistory(patientDetailBean.getFamily());
            reverseTransferBean.setAllergyHistory(patientDetailBean.getAllergy());
            //给个默认值 防止回填数据冲突（区分转诊订单详情、患者的个人页面）
            reverseTransferBean.setTransferType(-1);
            reverseTransferBean.setPayType(-1);
        }
        else {
            //预约检查
            reserveCheckBean = new ReserveCheckBean();
            reserveCheckBean.setPatientName(patientDetailBean.getName());
            reserveCheckBean.setIdCardNo(patientDetailBean.getIdCard());
            reserveCheckBean.setPatientCode(patientDetailBean.getCode());
            reserveCheckBean.setSex(patientDetailBean.getSex());
            reserveCheckBean.setAge(patientDetailBean.getAge());
            reserveCheckBean.setPhone(patientDetailBean.getMobile());
            reserveCheckBean.setPastHistory(patientDetailBean.getPast());
            reserveCheckBean.setFamilyHistory(patientDetailBean.getFamily());
            reserveCheckBean.setAllergyHistory(patientDetailBean.getAllergy());
        }
    }

    /**
     * 订单数据回填
     */
    private void initTransferOrderData() {
        //数据回填 患者信息
        reverseTransferBean = new ReserveTransferBean();
        reverseTransferBean.setPatientName(transferBean.getPatientName());
        reverseTransferBean.setPatientIdCardNo(transferBean.getPatientIdCardNo());
        reverseTransferBean.setPatientCode(transferBean.getPatientCode());
        reverseTransferBean.setSex(transferBean.getSex());
        reverseTransferBean.setPatientAge(transferBean.getPatientAge());
        reverseTransferBean.setPatientMobile(transferBean.getPatientMobile());
        reverseTransferBean.setPastHistory(transferBean.getPastHistory());
        reverseTransferBean.setFamilyHistory(transferBean.getFamilyHistory());
        reverseTransferBean.setAllergyHistory(transferBean.getAllergyHistory());
        reverseTransferBean.setInitResult(transferBean.getInitResult());
        //接诊医生信息
        reverseTransferBean.setReceiveDoctorCode(transferBean.getTargetDoctorCode());
        reverseTransferBean.setReceiveDoctorName(transferBean.getTargetDoctorName());
        reverseTransferBean.setReceiveDoctorDepart(transferBean.getTargetHospitalDepartmentName());
        reverseTransferBean.setReceiveDoctorHospital(transferBean.getTargetHospitalName());
        reverseTransferBean.setReceiveDoctorPhoto(transferBean.getTargetDoctorPhoto());
        reverseTransferBean.setReceiveDoctorJobTitle(transferBean.getTargetDoctorJobTitle());
        //转诊类型
        reverseTransferBean.setTransferType(transferBean.getTransferType());
        //转诊目的
        reverseTransferBean.setTransferTarget(transferBean.getTransferTarget());
        //缴费类型
        reverseTransferBean.setPayType(transferBean.getPayType());
        //确认照片
        reverseTransferBean.setConfirmPhoto(transferBean.getConfirmPhoto());
    }

    private void tabReservationBaseView() {
        fragmentTransaction = fragmentManager.beginTransaction();
        hideAll(fragmentTransaction);
        if (identifyFragment == null) {
            identifyFragment = new IdentifyFragment();
            identifyFragment.setTransfer(isTransfer);
            identifyFragment.setOnCheckListener(this);
            fragmentTransaction.add(R.id.layout_frame_root, identifyFragment);
        }
        else {
            fragmentTransaction.show(identifyFragment);
            identifyFragment.onResume();
        }
        fragmentTransaction.commitAllowingStateLoss();
        selectTab(BASE_ZERO);
    }

    private void tabReservationLicenseView() {
        fragmentTransaction = fragmentManager.beginTransaction();
        hideAll(fragmentTransaction);
        if (materialFragment == null) {
            materialFragment = new MaterialFragment();
            materialFragment.setTransfer(isTransfer);
            materialFragment.setOnCheckListener(this);
            materialFragment.setReverseTransferBean(reverseTransferBean);
            materialFragment.setReserveCheckBean(reserveCheckBean);
            fragmentTransaction.add(R.id.layout_frame_root, materialFragment);
        }
        else {
            fragmentTransaction.show(materialFragment);
            materialFragment.setReverseTransferBean(reverseTransferBean);
            materialFragment.setReserveCheckBean(reserveCheckBean);
            materialFragment.onResume();
        }
        fragmentTransaction.commitAllowingStateLoss();
        selectTab(BASE_ONE);
    }

    /**
     * 预约检查提交碎片
     */
    private void tabCheckResultView() {
        fragmentTransaction = fragmentManager.beginTransaction();
        hideAll(fragmentTransaction);
        if (submitCheckFragment == null) {
            submitCheckFragment = new SubmitCheckFragment();
            submitCheckFragment.setOnCheckListener(this);
            submitCheckFragment.setReserveCheckBean(reserveCheckBean);
            fragmentTransaction.add(R.id.layout_frame_root, submitCheckFragment);
        }
        else {
            fragmentTransaction.show(submitCheckFragment);
            submitCheckFragment.setReserveCheckBean(reserveCheckBean);
            submitCheckFragment.onResume();
        }
        fragmentTransaction.commitAllowingStateLoss();
        selectTab(BASE_TWO);
    }

    /**
     * 预约转诊提交碎片
     */
    private void tabTransferResultView() {
        fragmentTransaction = fragmentManager.beginTransaction();
        hideAll(fragmentTransaction);
        if (submitTransferFragment == null) {
            submitTransferFragment = new SubmitTransferFragment();
            submitTransferFragment.setOnCheckListener(this);
            submitTransferFragment.setReverseTransferBean(reverseTransferBean);
            fragmentTransaction.add(R.id.layout_frame_root, submitTransferFragment);
        }
        else {
            fragmentTransaction.show(submitTransferFragment);
            submitTransferFragment.setReverseTransferBean(reverseTransferBean);
            submitTransferFragment.onResume();
        }
        fragmentTransaction.commitAllowingStateLoss();
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
                tvReservationBase.setSelected(true);
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
    public void onTransferStepOne(ReserveTransferBean bean) {
        reverseTransferBean = bean;
        tabReservationLicenseView();
    }

    @Override
    public void onTransferStepTwo(ReserveTransferBean bean) {
        reverseTransferBean = bean;
        tabTransferResultView();
    }

    @Override
    public void onTransferStepThree(ReserveTransferBean bean) {
        reverseTransferBean = bean;
        addReserveTransferOrder();
    }

    @Override
    public void onCheckStepOne(ReserveCheckBean bean) {
        reserveCheckBean = bean;
        tabReservationLicenseView();
    }

    @Override
    public void onCheckStepTwo(ReserveCheckBean bean) {
        reserveCheckBean = bean;
        tabCheckResultView();
    }

    @Override
    public void onCheckStepThree(ReserveCheckBean bean) {
        reserveCheckBean = bean;
        addReserveCheckOrder();
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        Intent intent;
        String orderNo;
        switch (task) {
            case ADD_RESERVE_TRANSFER_ORDER:
                orderNo = (String)response.getData();
                intent = new Intent(this, ReservationSuccessActivity.class);
                intent.putExtra(CommonData.KEY_CHECK_OR_TRANSFER, true);
                intent.putExtra(CommonData.KEY_ORDER_ID, orderNo);
                startActivity(intent);
                finish();
                break;
            case ADD_RESERVE_CHECK_ORDER:
                orderNo = (String)response.getData();
                intent = new Intent(this, ReservationSuccessActivity.class);
                intent.putExtra(CommonData.KEY_ORDER_ID, orderNo);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * 页面逻辑处理
     *
     * @return bool
     */
    private boolean finishPage() {
        if (curPage == BASE_TWO) {
            curPage = 1;
            if (submitTransferFragment != null) {
                reverseTransferBean = submitTransferFragment.getReverseTransferBean();
            }
            tabReservationLicenseView();
            return false;
        }
        else if (curPage == 1) {
            if (transferBean != null || patientDetailBean != null) {
                return true;
            }
            if (materialFragment != null) {
                if (isTransfer) {
                    reverseTransferBean = materialFragment.getReverseTransferBean();
                }
                else {
                    reserveCheckBean = materialFragment.getReserveCheckBean();
                }
            }
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
