package com.yht.yihuantong.ui.remote;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.TextView;

import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.CommonData;
import com.yht.frame.data.Tasks;
import com.yht.frame.http.retrofit.RequestUtils;
import com.yht.frame.ui.BaseActivity;
import com.yht.yihuantong.R;

import butterknife.BindView;

/**
 * @author 顿顿
 * @date 19/6/14 10:56
 * @des 远程会诊详情
 */
public class RemoteDetailActivity extends BaseActivity {
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
    @BindView(R.id.tv_initiate_time)
    TextView tvInitiateTime;
    @BindView(R.id.tv_initiate_doctor)
    TextView tvInitiateDoctor;
    @BindView(R.id.tv_initiate_depart)
    TextView tvInitiateDepart;
    @BindView(R.id.tv_initiate_hospital)
    TextView tvInitiateHospital;
    @BindView(R.id.tv_condition_description)
    TextView tvConditionDescription;
    @BindView(R.id.tv_check_diagnosis_top)
    TextView tvCheckDiagnosisTop;
    @BindView(R.id.tv_consultation_purpose)
    TextView tvConsultationPurpose;
    @BindView(R.id.tv_consultation_status)
    TextView tvConsultationStatus;
    @BindView(R.id.tv_consultation_time)
    TextView tvConsultationTime;
    @BindView(R.id.tv_consultation_advice)
    TextView tvConsultationAdvice;
    /**
     * id
     */
    private String orderNo;

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_remote_detail;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        if (getIntent() != null) {
            orderNo = getIntent().getStringExtra(CommonData.KEY_ORDER_ID);
        }
    }

    @Override
    public void fillNetWorkData() {
        getRemoteDetail();
    }

    private void getRemoteDetail() {
        RequestUtils.getRemoteDetail(this, loginBean.getToken(), orderNo, this);
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        switch (task) {
            case GET_REMOTE_DETAIL:
                break;
            default:
                break;
        }
    }
}
