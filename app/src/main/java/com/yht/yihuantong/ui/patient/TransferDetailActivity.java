package com.yht.yihuantong.ui.patient;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.ImageView;
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
import com.yht.yihuantong.R;
import com.yht.yihuantong.utils.ImageUrlUtil;

import butterknife.BindView;

/**
 * @author 顿顿
 * @date 19/6/14 10:56
 * @des 预约转诊详情
 */
public class TransferDetailActivity extends BaseActivity implements TransferOrderStatus {
    @BindView(R.id.iv_patient_img)
    ImageView ivPatientImg;
    @BindView(R.id.tv_patient_name)
    TextView tvPatientName;
    @BindView(R.id.tv_patient_age)
    TextView tvPatientAge;
    @BindView(R.id.tv_patient_sex)
    TextView tvPatientSex;
    @BindView(R.id.iv_check_status)
    ImageView ivCheckStatus;
    @BindView(R.id.tv_transfer_time)
    TextView tvTransferTime;
    @BindView(R.id.tv_transfer_doctor)
    TextView tvTransferDoctor;
    @BindView(R.id.tv_transfer_depart)
    TextView tvTransferDepart;
    @BindView(R.id.tv_transfer_hospital)
    TextView tvTransferHospital;
    @BindView(R.id.tv_transfer_purpose)
    TextView tvTransferPurpose;
    @BindView(R.id.tv_check_diagnosis_top)
    TextView tvCheckDiagnosisTop;
    @BindView(R.id.tv_receiving_doctor)
    TextView tvReceivingDoctor;
    @BindView(R.id.tv_receiving_depart)
    TextView tvReceivingDepart;
    @BindView(R.id.tv_receiving_hospital)
    TextView tvReceivingHospital;
    @BindView(R.id.tv_receiving_status)
    TextView tvReceivingStatus;
    /**
     * 订单
     */
    private TransferBean transferBean;
    /**
     * 订单编号
     */
    private String orderNo;

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_transfer_detail;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        if (getIntent() != null) {
            orderNo = getIntent().getStringExtra(CommonData.KEY_ORDER_ID);
        }
        getTransferOrderDetail();
    }

    /**
     * 获取详情
     */
    private void getTransferOrderDetail() {
        RequestUtils.getTransferOrderDetail(this, loginBean.getToken(), orderNo, this);
    }

    /**
     * 详情数据
     */
    private void initDetailData() {
        if (transferBean == null) {
            return;
        }
        Glide.with(this)
             .load(ImageUrlUtil.addTokenToUrl(transferBean.getWxPhoto()))
             .apply(GlideHelper.getOptions(BaseUtils.dp2px(this, 4)))
             .into(ivPatientImg);
        tvPatientName.setText(transferBean.getPatientName());
        tvPatientSex.setText(transferBean.getSex() == BaseData.BASE_ONE
                             ? getString(R.string.txt_sex_male)
                             : getString(R.string.txt_sex_female));
        tvPatientAge.setText(String.valueOf(transferBean.getPatientAge()));
        tvTransferTime.setText(transferBean.getTransferDate());
        //        tvInitiateDiagnosis.setText(transferBean.getInitResult());
        //        tvTransferType.setText(transferBean.getTransferType() == BaseData.BASE_ZERO
        //                               ? getString(R.string.txt_transfer_up)
        //                               : getString(R.string.txt_transfer_down));
        tvTransferPurpose.setText(transferBean.getTransferTarget());
        //        int payType = transferBean.getPayType();
        //        if (payType == BaseData.BASE_ZERO) {
        //            tvPayment.setText(getString(R.string.txt_self_pay));
        //        }
        //        else if (payType == BaseData.BASE_ONE) {
        //            tvPayment.setText(getString(R.string.txt_medicare));
        //        }
        //        else {
        //            tvPayment.setText(getString(R.string.txt_self_medicare));
        //        }
        tvReceivingDoctor.setText(transferBean.getTargetDoctorName());
        int status = transferBean.getReceiveStatus();
        switch (status) {
            case TRANSFER_STATUS_WAIT:
                //                layoutBottom.setVisibility(View.VISIBLE);
                //                layoutBottomOne.setVisibility(View.GONE);
                ivCheckStatus.setImageResource(R.mipmap.ic_wait_transfer);
                //                tvTransferAgain.setText(R.string.txt_transfer_cancel);
                tvReceivingStatus.setText(getString(R.string.txt_status_wait));
                break;
            case TRANSFER_STATUS_RECEIVED:
                //                layoutBottom.setVisibility(View.GONE);
                //                layoutBottomOne.setVisibility(View.VISIBLE);
                //                layoutReceiveDepart.setVisibility(View.VISIBLE);
                //                layoutReceiveHospital.setVisibility(View.VISIBLE);
                //                layoutReceiveTime.setVisibility(View.VISIBLE);
                //                layoutReceiveNotice.setVisibility(View.VISIBLE);
                ivCheckStatus.setImageResource(R.mipmap.ic_status_received);
                tvReceivingStatus.setText(getString(R.string.txt_status_received));
                tvReceivingDepart.setText(transferBean.getTargetHospitalDepartmentName());
                tvReceivingHospital.setText(transferBean.getTargetHospitalName());
                //                tvReserveTime.setText(transferBean.getAppointAt());
                //                tvTransferNotice.setText(transferBean.getNote());
                break;
            case TRANSFER_STATUS_CANCEL:
                //                layoutBottom.setVisibility(View.VISIBLE);
                //                layoutBottomOne.setVisibility(View.GONE);
                //                layoutCancelResult.setVisibility(View.VISIBLE);
                ivCheckStatus.setImageResource(R.mipmap.ic_status_cancel);
                tvReceivingStatus.setText(getString(R.string.txt_status_cancel));
                //                tvTransferAgain.setText(R.string.txt_transfer_again);
                //                tvTransferCancel.setText(transferBean.getCancelReason());
                break;
            case TRANSFER_STATUS_REFUSE:
                //                layoutBottom.setVisibility(View.VISIBLE);
                //                layoutBottomOne.setVisibility(View.GONE);
                ivCheckStatus.setImageResource(R.mipmap.ic_status_reject);
                tvReceivingStatus.setText(getString(R.string.txt_status_reject));
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
            default:
                break;
        }
    }
}
