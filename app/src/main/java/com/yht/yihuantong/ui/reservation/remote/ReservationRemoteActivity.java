package com.yht.yihuantong.ui.reservation.remote;

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
import com.yht.frame.data.bean.DepartInfoBean;
import com.yht.frame.data.bean.FileBean;
import com.yht.frame.data.bean.PatientBean;
import com.yht.frame.data.bean.RemoteDetailBean;
import com.yht.frame.data.bean.RemoteInvitedBean;
import com.yht.frame.data.bean.ReserveRemoteBean;
import com.yht.frame.http.retrofit.RequestUtils;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.widgets.dialog.HintDialog;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ui.remote.listener.OnRemoteListener;
import com.yht.yihuantong.ui.reservation.ReservationSuccessActivity;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * @author 顿顿
 * @date 19/6/14 14:05
 * @des 新增 远程会诊
 */
public class ReservationRemoteActivity extends BaseActivity implements OnRemoteListener {
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
    private RemoteIdentifyFragment identifyFragment;
    /**
     * 完善资料
     */
    private RemoteMaterialFragment materialFragment;
    /**
     * 确认提交(远程会诊)
     */
    private RemoteSubmitFragment submitCheckFragment;
    /**
     * 居民回填数据
     */
    private PatientBean patientBean;
    /**
     * 当前远程会诊数据
     */
    private ReserveRemoteBean reserveRemoteBean;
    /**
     * 详情数据 (重新发起)
     */
    private RemoteDetailBean remoteDetailBean;
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
            //会诊详情页面回传数据(重新转诊)
            remoteDetailBean = (RemoteDetailBean)getIntent().getSerializableExtra(CommonData.KEY_REMOTE_ORDER_BEAN);
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
     * 新增远程会诊订单
     */
    private void addReserveRemoteOrder() {
        RequestUtils.addReserveRemoteOrder(this, loginBean.getToken(), reserveRemoteBean, this);
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
            return true;
        }
        else if (remoteDetailBean != null) {
            initRemoteOrderData();
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
        //远程会诊
        reserveRemoteBean = new ReserveRemoteBean();
        reserveRemoteBean.setPatientName(patientBean.getName());
        reserveRemoteBean.setPatientIdCard(patientBean.getIdCard());
        reserveRemoteBean.setPatientCode(patientBean.getCode());
        reserveRemoteBean.setPatientSex(patientBean.getSex());
        reserveRemoteBean.setPatientAge(patientBean.getAge());
        reserveRemoteBean.setPatientMobile(patientBean.getMobile());
        reserveRemoteBean.setPast(patientBean.getPast());
        reserveRemoteBean.setFamily(patientBean.getFamily());
        reserveRemoteBean.setAllergy(patientBean.getAllergy());
    }

    /**
     * 会诊订单数据回填（重新发起）
     */
    private void initRemoteOrderData() {
        //远程会诊
        reserveRemoteBean = new ReserveRemoteBean();
        reserveRemoteBean.setPatientName(remoteDetailBean.getPatientName());
        reserveRemoteBean.setPatientIdCard(remoteDetailBean.getPatientIdCardNo());
        reserveRemoteBean.setPatientCode(remoteDetailBean.getPatientCode());
        reserveRemoteBean.setPatientSex(remoteDetailBean.getSex());
        reserveRemoteBean.setPatientAge(remoteDetailBean.getPatientAge());
        reserveRemoteBean.setPatientMobile(remoteDetailBean.getPatientMobile());
        reserveRemoteBean.setPast(remoteDetailBean.getPastHistory());
        reserveRemoteBean.setFamily(remoteDetailBean.getFamilyHistory());
        reserveRemoteBean.setAllergy(remoteDetailBean.getAllergyHistory());
        reserveRemoteBean.setDescIll(remoteDetailBean.getDescIll());
        reserveRemoteBean.setDestination(remoteDetailBean.getDestination());
        reserveRemoteBean.setInitResult(remoteDetailBean.getInitResult());
        reserveRemoteBean.setStartAt(BaseUtils.formatDate(remoteDetailBean.getStartAt(), BaseUtils.YYYY_MM_DD_HH_MM));
        reserveRemoteBean.setEndAt(BaseUtils.formatDate(remoteDetailBean.getEndAt(), BaseUtils.YYYY_MM_DD_HH_MM));
        //受邀方信息
        ArrayList<DepartInfoBean> list = new ArrayList<>();
        ArrayList<RemoteInvitedBean> beans = remoteDetailBean.getInvitationList();
        for (RemoteInvitedBean bean : beans) {
            DepartInfoBean infoBean = new DepartInfoBean();
            infoBean.setHospitalDepartmentId(bean.getHospitalDepartmentId());
            infoBean.setHospitalDepartmentName(bean.getHospitalDepartmentName());
            infoBean.setHospitalName(bean.getHospitalName());
            infoBean.setHospitalCode(bean.getHospitalCode());
            list.add(infoBean);
        }
        reserveRemoteBean.setHosDeptInfo(list);
        //附件
        StringBuilder builder = new StringBuilder();
        ArrayList<FileBean> fileBeans = remoteDetailBean.getPatientResourceList();
        for (int i = 0; i < fileBeans.size(); i++) {
            FileBean file = fileBeans.get(i);
            builder.append(file.getFileUrl());
            if (fileBeans.size() - 1 != i) {
                builder.append(",");
            }
        }
        reserveRemoteBean.setPatientResource(builder.toString());
    }

    private void tabReservationBaseView() {
        fragmentTransaction = fragmentManager.beginTransaction();
        hideAll(fragmentTransaction);
        if (identifyFragment == null) {
            identifyFragment = new RemoteIdentifyFragment();
            identifyFragment.setOnRemoteListener(this);
            identifyFragment.setReserveRemoteBean(reserveRemoteBean);
            fragmentTransaction.add(R.id.layout_frame_root, identifyFragment);
        }
        else {
            fragmentTransaction.show(identifyFragment);
            identifyFragment.setReserveRemoteBean(reserveRemoteBean);
            identifyFragment.onResume();
        }
        fragmentTransaction.commitAllowingStateLoss();
        selectTab(BASE_ZERO);
    }

    private void tabReservationLicenseView() {
        fragmentTransaction = fragmentManager.beginTransaction();
        hideAll(fragmentTransaction);
        if (materialFragment == null) {
            materialFragment = new RemoteMaterialFragment();
            materialFragment.setOnRemoteListener(this);
            materialFragment.setReserveRemoteBean(reserveRemoteBean);
            fragmentTransaction.add(R.id.layout_frame_root, materialFragment);
        }
        else {
            fragmentTransaction.show(materialFragment);
            materialFragment.setReserveRemoteBean(reserveRemoteBean);
            materialFragment.onResume();
        }
        fragmentTransaction.commitAllowingStateLoss();
        selectTab(BASE_ONE);
    }

    /**
     * 远程会诊提交碎片
     */
    private void tabCheckResultView() {
        fragmentTransaction = fragmentManager.beginTransaction();
        hideAll(fragmentTransaction);
        if (submitCheckFragment == null) {
            submitCheckFragment = new RemoteSubmitFragment();
            submitCheckFragment.setOnRemoteListener(this);
            submitCheckFragment.setReserveRemoteBean(reserveRemoteBean);
            fragmentTransaction.add(R.id.layout_frame_root, submitCheckFragment);
        }
        else {
            fragmentTransaction.show(submitCheckFragment);
            submitCheckFragment.setReserveRemoteBean(reserveRemoteBean);
            submitCheckFragment.onResume();
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
    public void onRemoteStepOne(ReserveRemoteBean bean) {
        reserveRemoteBean = bean;
        tabReservationLicenseView();
    }

    @Override
    public void onRemoteStepTwo(ReserveRemoteBean bean) {
        reserveRemoteBean = bean;
        tabCheckResultView();
    }

    @Override
    public void onRemoteStepThree(ReserveRemoteBean bean) {
        reserveRemoteBean = bean;
        addReserveRemoteOrder();
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        Intent intent;
        String orderNo;
        if (task == Tasks.ADD_RESERVE_REMOTE_ORDER) {
            orderNo = (String)response.getData();
            intent = new Intent(this, ReservationSuccessActivity.class);
            intent.putExtra(CommonData.KEY_RESERVATION_TYPE, BASE_TWO);
            intent.putExtra(CommonData.KEY_ORDER_ID, orderNo);
            startActivity(intent);
            finish();
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
            tabReservationLicenseView();
            return false;
        }
        else if (curPage == 1) {
            if (patientBean != null || remoteDetailBean != null) {
                return true;
            }
            if (materialFragment != null) {
                reserveRemoteBean = materialFragment.getReserveRemoteBean();
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
            case BASE_ONE:
                if (materialFragment != null) {
                    materialFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
                }
                break;
            case BASE_TWO:
                if (submitCheckFragment != null) {
                    submitCheckFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
            case BASE_ONE:
                if (materialFragment != null) {
                    materialFragment.onPermissionNeedExplanation(permissionName);
                }
                break;
            case BASE_TWO:
                if (submitCheckFragment != null) {
                    submitCheckFragment.onPermissionNeedExplanation(permissionName);
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
            case BASE_ONE:
                if (materialFragment != null) {
                    materialFragment.onNoPermissionNeeded(permissionName);
                }
                break;
            case BASE_TWO:
                if (submitCheckFragment != null) {
                    submitCheckFragment.onNoPermissionNeeded(permissionName);
                }
                break;
            default:
                break;
        }
    }
}
