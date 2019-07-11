package com.yht.yihuantong.ui.transfer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yht.frame.data.BaseData;
import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.CommonData;
import com.yht.frame.data.Tasks;
import com.yht.frame.data.TransferOrderStatus;
import com.yht.frame.data.base.TransferBean;
import com.yht.frame.http.retrofit.RequestUtils;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.utils.glide.GlideHelper;
import com.yht.frame.widgets.dialog.HintDialog;
import com.yht.frame.widgets.dialog.InputDialog;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ui.reservation.ReservationCheckOrTransferActivity;
import com.yht.yihuantong.utils.ImageUrlUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/6/14 10:56
 * @des 预约转诊详情  我转诊给其他医生 (有两种状态  待接诊、已接诊)
 */
public class TransferInitiateDetailActivity extends BaseActivity implements TransferOrderStatus {
    @BindView(R.id.iv_patient_img)
    ImageView ivPatientImg;
    @BindView(R.id.tv_patient_name)
    TextView tvPatientName;
    @BindView(R.id.tv_patient_sex)
    TextView tvPatientSex;
    @BindView(R.id.tv_patient_age)
    TextView tvPatientAge;
    @BindView(R.id.iv_check_status)
    ImageView ivCheckStatus;
    @BindView(R.id.tv_transfer_time)
    TextView tvTransferTime;
    @BindView(R.id.tv_initiate_diagnosis)
    TextView tvInitiateDiagnosis;
    @BindView(R.id.tv_transfer_type)
    TextView tvTransferType;
    @BindView(R.id.tv_transfer_purpose)
    TextView tvTransferPurpose;
    @BindView(R.id.tv_payment)
    TextView tvPayment;
    @BindView(R.id.tv_receiving_doctor)
    TextView tvReceivingDoctor;
    @BindView(R.id.tv_receiving_depart)
    TextView tvReceivingDepart;
    @BindView(R.id.tv_receiving_hospital)
    TextView tvReceivingHospital;
    @BindView(R.id.tv_receiving_status)
    TextView tvReceivingStatus;
    @BindView(R.id.tv_reserve_time)
    TextView tvReserveTime;
    @BindView(R.id.tv_transfer_cancel)
    TextView tvTransferCancel;
    @BindView(R.id.tv_transfer_notice)
    TextView tvTransferNotice;
    @BindView(R.id.tv_transfer_again)
    TextView tvTransferAgain;
    @BindView(R.id.tv_contact_patient)
    TextView tvContactPatient;
    @BindView(R.id.tv_contact_doctor)
    TextView tvContactDoctor;
    @BindView(R.id.layout_bottom)
    LinearLayout layoutBottom;
    @BindView(R.id.layout_bottom_one)
    LinearLayout layoutBottomOne;
    @BindView(R.id.layout_receive_depart)
    RelativeLayout layoutReceiveDepart;
    @BindView(R.id.layout_receive_hospital)
    RelativeLayout layoutReceiveHospital;
    @BindView(R.id.layout_receive_time)
    RelativeLayout layoutReceiveTime;
    @BindView(R.id.layout_cancel_result)
    RelativeLayout layoutCancelResult;
    @BindView(R.id.layout_receive_notice)
    RelativeLayout layoutReceiveNotice;
    /**
     * 订单号
     */
    private TransferBean transferBean;
    /**
     * 订单编号
     */
    private String orderNo;
    /**
     * 取消转诊原因
     */
    private String cancelreason;

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_transfer_to_detail;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        if (getIntent() != null) {
            transferBean = (TransferBean)getIntent().getSerializableExtra(CommonData.KEY_TRANSFER_ORDER_BEAN);
            orderNo = getIntent().getStringExtra(CommonData.KEY_ORDER_ID);
        }
        if (transferBean != null) {
            orderNo = transferBean.getOrderNo();
        }
        initDetailData();
        getTransferOrderDetail();
    }

    /**
     * 详情数据
     */
    private void initDetailData() {
        Glide.with(this)
             .load(ImageUrlUtil.append(transferBean.getWxPhoto()))
             .apply(GlideHelper.getOptions(BaseUtils.dp2px(this, 4)))
             .into(ivPatientImg);
        tvPatientName.setText(transferBean.getPatientName());
        tvPatientSex.setText(transferBean.getSex() == BaseData.BASE_ONE
                             ? getString(R.string.txt_sex_male)
                             : getString(R.string.txt_sex_female));
        tvPatientAge.setText(String.valueOf(transferBean.getPatientAge()));
        tvTransferTime.setText(transferBean.getTransferDate());
        tvInitiateDiagnosis.setText(transferBean.getInitResult());
        tvTransferType.setText(transferBean.getTransferType() == BaseData.BASE_ZERO
                               ? getString(R.string.txt_transfer_up)
                               : getString(R.string.txt_transfer_down));
        tvTransferPurpose.setText(transferBean.getTransferTarget());
        int payType = transferBean.getPayType();
        if (payType == BaseData.BASE_ZERO) {
            tvPayment.setText(getString(R.string.txt_self_pay));
        }
        else if (payType == BaseData.BASE_ONE) {
            tvPayment.setText(getString(R.string.txt_medicare));
        }
        else {
            tvPayment.setText(getString(R.string.txt_self_medicare));
        }
        tvReceivingDoctor.setText(transferBean.getTargetDoctorName());
        int status = transferBean.getReceiveStatus();
        switch (status) {
            case TRANSFER_STATUS_WAIT:
                layoutBottom.setVisibility(View.VISIBLE);
                layoutBottomOne.setVisibility(View.GONE);
                ivCheckStatus.setImageResource(R.mipmap.ic_wait_transfer);
                tvTransferAgain.setText(R.string.txt_transfer_cancel);
                tvReceivingStatus.setText(getString(R.string.txt_status_wait));
                break;
            case TRANSFER_STATUS_RECEIVED:
                layoutBottom.setVisibility(View.GONE);
                layoutBottomOne.setVisibility(View.VISIBLE);
                layoutReceiveDepart.setVisibility(View.VISIBLE);
                layoutReceiveHospital.setVisibility(View.VISIBLE);
                layoutReceiveTime.setVisibility(View.VISIBLE);
                layoutReceiveNotice.setVisibility(View.VISIBLE);
                ivCheckStatus.setImageResource(R.mipmap.ic_status_received);
                tvReceivingStatus.setText(getString(R.string.txt_status_received));
                tvReceivingDepart.setText(transferBean.getTargetHospitalDepartmentName());
                tvReceivingHospital.setText(transferBean.getTargetHospitalName());
                tvReserveTime.setText(transferBean.getAppointAt());
                tvTransferNotice.setText(transferBean.getNote());
                break;
            case TRANSFER_STATUS_CANCEL:
                layoutBottom.setVisibility(View.VISIBLE);
                layoutBottomOne.setVisibility(View.GONE);
                layoutCancelResult.setVisibility(View.VISIBLE);
                ivCheckStatus.setImageResource(R.mipmap.ic_status_cancel);
                tvReceivingStatus.setText(getString(R.string.txt_status_cancel));
                tvTransferAgain.setText(R.string.txt_transfer_again);
                tvTransferCancel.setText(transferBean.getCancelReason());
                break;
            case TRANSFER_STATUS_REFUSE:
                layoutBottom.setVisibility(View.VISIBLE);
                layoutBottomOne.setVisibility(View.GONE);
                ivCheckStatus.setImageResource(R.mipmap.ic_status_reject);
                tvReceivingStatus.setText(getString(R.string.txt_status_reject));
                break;
            default:
                break;
        }
    }

    /**
     * 获取详情
     */
    private void getTransferOrderDetail() {
        RequestUtils.getTransferOrderDetail(this, loginBean.getToken(), orderNo, this);
    }

    /**
     * 取消转诊
     */
    private void cancelReserveTransferOrder() {
        RequestUtils.cancelReserveTransferOrder(this, loginBean.getToken(), cancelreason, orderNo, this);
    }

    @OnClick({
            R.id.tv_transfer_again, R.id.tv_contact_patient, R.id.tv_contact_doctor, R.id.tv_contact_patient_one,
            R.id.tv_contact_doctor_one })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_transfer_again:
                if (TRANSFER_STATUS_WAIT == transferBean.getReceiveStatus()) {
                    new InputDialog(this).Builder()
                                         .setEditHintText(getString(R.string.txt_cancel_transfer_reason_hint))
                                         .setCancleBtnTxt(getString(R.string.txt_sure_cancel))
                                         .setEnterBtnTxt(getString(R.string.txt_think_again))
                                         .setEnterSelect(true)
                                         .setLeft(true)
                                         .setOnCancelClickListener(() -> {
                                         })
                                         .setResultListener(result -> {
                                             cancelreason = result;
                                             cancelReserveTransferOrder();
                                         })
                                         .show();
                }
                else {
                    //重新转诊
                    Intent intent = new Intent(this, ReservationCheckOrTransferActivity.class);
                    intent.putExtra(CommonData.KEY_CHECK_OR_TRANSFER, true);
                    startActivity(intent);
                }
                break;
            case R.id.tv_contact_patient:
            case R.id.tv_contact_patient_one:
                new HintDialog(this).setPhone(transferBean.getPatientMobile())
                                    .setOnEnterClickListener(() -> callPhone(transferBean.getPatientMobile()))
                                    .show();
                break;
            case R.id.tv_contact_doctor:
            case R.id.tv_contact_doctor_one:
                new HintDialog(this).setPhone(transferBean.getTargetDoctorMobile())
                                    .setOnEnterClickListener(() -> callPhone(transferBean.getTargetDoctorMobile()))
                                    .show();
                break;
            default:
                break;
        }
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        switch (task) {
            case GET_TRANSFER_ORDER_DETAIL:
                transferBean = (TransferBean)response.getData();
                initDetailData();
                break;
            case CANCEL_RESERVE_TRANSFER_ORDER:
                //通知列表刷新
                setResult(RESULT_OK);
                getTransferOrderDetail();
                break;
            default:
                break;
        }
    }
}
