package com.yht.yihuantong.ui.patient;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.TextView;

import com.yht.frame.ui.BaseActivity;
import com.yht.yihuantong.R;

import butterknife.BindView;

/**
 * @author 顿顿
 * @date 19/6/14 10:56
 * @des 预约检查详情
 */
public class TransferDetailActivity extends BaseActivity {
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
    }
}
