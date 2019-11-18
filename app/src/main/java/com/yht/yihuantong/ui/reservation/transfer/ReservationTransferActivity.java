package com.yht.yihuantong.ui.reservation.transfer;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.CommonData;
import com.yht.frame.data.Tasks;
import com.yht.frame.data.bean.PatientBean;
import com.yht.frame.data.bean.ReserveTransferBean;
import com.yht.frame.data.bean.TransferDetailBean;
import com.yht.frame.http.retrofit.RequestUtils;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.widgets.dialog.HintDialog;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ui.reservation.ReservationSuccessActivity;
import com.yht.yihuantong.ui.transfer.listener.OnTransferListener;

import butterknife.BindView;

/**
 * @author 顿顿
 * @date 19/6/14 14:05
 * @des 新增 预约检查 预约转诊
 */
public class ReservationTransferActivity extends BaseActivity implements OnTransferListener {
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
    private TransferIdentifyFragment identifyFragment;
    /**
     * 完善资料
     */
    private TransferMaterialFragment materialFragment;
    /**
     * 确认提交(预约转诊)
     */
    private TransferSubmitFragment submitTransferFragment;
    /**
     * 居民回填数据
     */
    private PatientBean patientBean;
    /**
     * 重新转诊 数据回填
     */
    private TransferDetailBean transferBean;
    /**
     * 当前预约转诊数据
     */
    private ReserveTransferBean reverseTransferBean;
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
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        fragmentManager = getSupportFragmentManager();
        if (getIntent() != null) {
            //居民详情页面回传数据
            patientBean = (PatientBean)getIntent().getSerializableExtra(CommonData.KEY_PATIENT_BEAN);
            //转诊详情页面回传数据(重新转诊)
            transferBean = (TransferDetailBean)getIntent().getSerializableExtra(CommonData.KEY_TRANSFER_ORDER_BEAN);
        }
        initTitlePage();
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        if (!hasHistoryData()) {
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
     * 居民验证
     * (重新转诊，需要校验用户)
     */
    private void verifyPatient() {
        RequestUtils.verifyPatient(this, loginBean.getToken(), transferBean.getPatientIdCardNo(), this);
    }

    /**
     * title处理
     */
    private void initTitlePage() {
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
     * 已有数据回填（包括居民基本数据或者订单数据）
     * 返回true  表示有数据需要回填
     */
    private boolean hasHistoryData() {
        if (patientBean != null) {
            initPatientBaseData();
            //直接进入到第二步 或者在居民页面预约转诊
            tabReservationLicenseView();
            return true;
        }
        else if (transferBean != null) {
            verifyPatient();
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * 居民基本数据回填
     */
    private void initPatientBaseData() {
        //转诊 数据回填
        reverseTransferBean = new ReserveTransferBean();
        reverseTransferBean.setPatientName(patientBean.getName());
        reverseTransferBean.setPatientIdCardNo(patientBean.getIdCard());
        reverseTransferBean.setPatientCode(patientBean.getCode());
        reverseTransferBean.setSex(patientBean.getSex());
        reverseTransferBean.setPatientAge(patientBean.getAge());
        reverseTransferBean.setPatientMobile(patientBean.getMobile());
        reverseTransferBean.setPastHistory(patientBean.getPast());
        reverseTransferBean.setFamilyHistory(patientBean.getFamily());
        reverseTransferBean.setAllergyHistory(patientBean.getAllergy());
        reverseTransferBean.setIsBind(patientBean.getIsBind());
        //给个默认值 防止回填数据冲突（区分转诊订单详情、居民的个人页面）
        reverseTransferBean.setTransferType(-1);
        reverseTransferBean.setPayType(-1);
    }

    /**
     * 订单数据回填
     */
    private void initTransferOrderData(PatientBean bean) {
        //数据回填 居民信息
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
        if (bean != null) {
            reverseTransferBean.setIsBind(bean.getIsBind());
        }
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
            identifyFragment = new TransferIdentifyFragment();
            identifyFragment.setReverseTransferBean(reverseTransferBean);
            identifyFragment.setOnTransferListener(this);
            fragmentTransaction.add(R.id.layout_frame_root, identifyFragment);
        }
        else {
            fragmentTransaction.show(identifyFragment);
            identifyFragment.setReverseTransferBean(reverseTransferBean);
            identifyFragment.onResume();
        }
        fragmentTransaction.commitAllowingStateLoss();
        selectTab(BASE_ZERO);
    }

    private void tabReservationLicenseView() {
        fragmentTransaction = fragmentManager.beginTransaction();
        hideAll(fragmentTransaction);
        if (materialFragment == null) {
            materialFragment = new TransferMaterialFragment();
            materialFragment.setOnTransferListener(this);
            materialFragment.setReverseTransferBean(reverseTransferBean);
            fragmentTransaction.add(R.id.layout_frame_root, materialFragment);
        }
        else {
            fragmentTransaction.show(materialFragment);
            materialFragment.setReverseTransferBean(reverseTransferBean);
            materialFragment.onResume();
        }
        fragmentTransaction.commitAllowingStateLoss();
        selectTab(BASE_ONE);
    }

    /**
     * 预约转诊提交碎片
     */
    private void tabTransferResultView() {
        fragmentTransaction = fragmentManager.beginTransaction();
        hideAll(fragmentTransaction);
        if (submitTransferFragment == null) {
            submitTransferFragment = new TransferSubmitFragment();
            submitTransferFragment.setOnTransferListener(this);
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
                    hideSoftInputFromWindow();
                    finish();
                }
                break;
            case R.id.public_title_bar_more:
                new HintDialog(this).setPhone(getString(R.string.txt_contact_service),
                                              getString(R.string.txt_contact_service_phone), false)
                                    .setOnEnterClickListener(
                                            () -> callPhone(getString(R.string.txt_contact_service_phone)))
                                    .show();
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
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        Intent intent;
        String orderNo;
        switch (task) {
            case ADD_RESERVE_TRANSFER_ORDER:
                orderNo = (String)response.getData();
                intent = new Intent(this, ReservationSuccessActivity.class);
                intent.putExtra(CommonData.KEY_RESERVATION_TYPE, BASE_ONE);
                intent.putExtra(CommonData.KEY_ORDER_ID, orderNo);
                startActivity(intent);
                finish();
                break;
            case VERIFY_PATIENT:
                PatientBean bean = (PatientBean)response.getData();
                initTransferOrderData(bean);
                tabReservationLicenseView();
                break;
            default:
                break;
        }
    }

    /**
     * 页面逻辑处理
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
            if (transferBean != null || patientBean != null) {
                return true;
            }
            if (materialFragment != null) {
                reverseTransferBean = materialFragment.getReverseTransferBean();
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
        switch (curPage) {
            case BASE_ZERO:
                if (identifyFragment != null) {
                    identifyFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
                }
                break;
            case BASE_TWO:
                if (submitTransferFragment != null) {
                    submitTransferFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
                if (identifyFragment != null) {
                    identifyFragment.onPermissionNeedExplanation(permissionName);
                }
                break;
            case BASE_TWO:
                if (submitTransferFragment != null) {
                    submitTransferFragment.onPermissionNeedExplanation(permissionName);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onNoPermissionNeeded(@NonNull Object permissionName) {
        super.onNoPermissionNeeded(permissionName);
        switch (curPage) {
            case BASE_ZERO:
                if (identifyFragment != null) {
                    identifyFragment.onNoPermissionNeeded(permissionName);
                }
                break;
            case BASE_TWO:
                if (submitTransferFragment != null) {
                    submitTransferFragment.onNoPermissionNeeded(permissionName);
                }
                break;
            default:
                break;
        }
    }
}
