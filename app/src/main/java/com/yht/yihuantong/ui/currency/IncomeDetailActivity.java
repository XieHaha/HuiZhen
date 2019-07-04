package com.yht.yihuantong.ui.currency;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yht.frame.ui.BaseActivity;
import com.yht.frame.widgets.recyclerview.FullListView;
import com.yht.yihuantong.R;

import butterknife.BindView;

/**
 * @author 顿顿
 * @date 19/6/11 10:40
 * @des 收入详情
 */
public class IncomeDetailActivity extends BaseActivity {
    @BindView(R.id.tv_income_price)
    TextView tvIncomePrice;
    @BindView(R.id.tv_income_estimate)
    TextView tvIncomeEstimate;
    @BindView(R.id.tv_service_type)
    TextView tvServiceType;
    @BindView(R.id.tv_patient_name)
    TextView tvPatientName;
    @BindView(R.id.tv_service_hospital)
    TextView tvServiceHospital;
    @BindView(R.id.tv_receive_doctor)
    TextView tvReceiveDoctor;
    @BindView(R.id.layout_doctor)
    RelativeLayout layoutDoctor;
    @BindView(R.id.lv_checked)
    FullListView lvChecked;
    @BindView(R.id.layout_project_checked)
    LinearLayout layoutProjectChecked;
    @BindView(R.id.tv_diagnosis_time)
    TextView tvDiagnosisTime;

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_income_detail;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
    }
}
