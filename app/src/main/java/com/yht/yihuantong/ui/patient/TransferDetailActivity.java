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
import com.yht.frame.data.bean.TransferBean;
import com.yht.frame.data.type.TransferOrderStatus;
import com.yht.frame.http.retrofit.RequestUtils;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.utils.glide.GlideHelper;
import com.yht.frame.widgets.textview.JustifiedTextView;
import com.yht.yihuantong.R;
import com.yht.yihuantong.utils.FileUrlUtil;

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
    @BindView(R.id.tv_past_medical)
    JustifiedTextView tvPastMedical;
    @BindView(R.id.tv_family_medical)
    JustifiedTextView tvFamilyMedical;
    @BindView(R.id.tv_allergies)
    JustifiedTextView tvAllergies;
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
             .load(FileUrlUtil.addTokenToUrl(transferBean.getPhoto()))
             .apply(GlideHelper.getOptions(BaseUtils.dp2px(this, 4)))
             .into(ivPatientImg);
        tvPatientName.setText(transferBean.getPatientName());
        tvPatientSex.setText(transferBean.getSex() == BaseData.BASE_ONE
                             ? getString(R.string.txt_sex_male)
                             : getString(R.string.txt_sex_female));
        tvPatientAge.setText(String.valueOf(transferBean.getPatientAge()));
        tvTransferTime.setText(transferBean.getTransferDate());
        //病史
        tvPastMedical.setText(transferBean.getPastHistory());
        tvFamilyMedical.setText(transferBean.getFamilyHistory());
        tvAllergies.setText(transferBean.getAllergyHistory());
        //转诊医生信息
        tvTransferDoctor.setText(transferBean.getSourceDoctorName());
        tvTransferDepart.setText(transferBean.getSourceHospitalDepartmentName());
        tvTransferHospital.setText(transferBean.getSourceHospitalName());
        //转诊目的
        tvTransferPurpose.setText(transferBean.getTransferTarget());
        //初步诊断
        tvCheckDiagnosisTop.setText(transferBean.getInitResult());
        //接诊医生信息
        tvReceivingDoctor.setText(transferBean.getTargetDoctorName());
        tvReceivingDepart.setText(transferBean.getTargetHospitalDepartmentName());
        tvReceivingHospital.setText(transferBean.getTargetHospitalName());
        int status = transferBean.getReceiveStatus();
        switch (status) {
            case TRANSFER_STATUS_WAIT:
                ivCheckStatus.setImageResource(R.mipmap.ic_tag_status_wait_transfer);
                tvReceivingStatus.setText(getString(R.string.txt_status_wait));
                break;
            case TRANSFER_STATUS_RECEIVED:
                ivCheckStatus.setImageResource(R.mipmap.ic_status_received);
                tvReceivingStatus.setText(getString(R.string.txt_status_received));
                break;
            case TRANSFER_STATUS_CANCEL:
                ivCheckStatus.setImageResource(R.mipmap.ic_status_cancel);
                tvReceivingStatus.setText(getString(R.string.txt_status_cancel));
                break;
            case TRANSFER_STATUS_REFUSE:
                ivCheckStatus.setImageResource(R.mipmap.ic_status_be_rejected);
                tvReceivingStatus.setText(getString(R.string.txt_status_reject));
                break;
            default:
                break;
        }
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        if (task == Tasks.GET_TRANSFER_ORDER_DETAIL) {
            transferBean = (TransferBean)response.getData();
            initDetailData();
        }
    }
}
