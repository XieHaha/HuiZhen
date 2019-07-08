package com.yht.yihuantong.ui.currency;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.CommonData;
import com.yht.frame.data.CurrencyDetailType;
import com.yht.frame.data.Tasks;
import com.yht.frame.data.base.CheckTypeBean;
import com.yht.frame.data.base.IncomeDetailBean;
import com.yht.frame.http.retrofit.RequestUtils;
import com.yht.frame.ui.BaseActivity;
import com.yht.yihuantong.R;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * @author 顿顿
 * @date 19/6/11 10:40
 * @des 收入详情
 */
public class IncomeDetailActivity extends BaseActivity implements CurrencyDetailType {
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
    @BindView(R.id.layout_project_checked)
    LinearLayout layoutProjectChecked;
    @BindView(R.id.tv_diagnosis_time)
    TextView tvDiagnosisTime;
    @BindView(R.id.tv_hospital_title)
    TextView tvHospitalTitle;
    @BindView(R.id.tv_doctor_title)
    TextView tvDoctorTitle;
    private IncomeDetailBean incomeDetailBean;
    /**
     * 订单id
     */
    private int doctorOrderTranId;
    /**
     * 临时token
     *
     * @return
     */
    final String token = "P1wDQpcrTx45XddRgbg6Kt+fSTJ6DDAce3H85a1p04lUcZRXC9MkRKGiC+Hk5cd8HvIintOVLGeRlt\\/DePjJ3DyMDcxmbdfurLDWNb4lXPFrWwhBoTdjSEntlFn5YPDcRCVzZezbHiOJkOBR8pnxYiYTP3DifKa+psssJ4Nruxg=";

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
        if (getIntent() != null) {
            doctorOrderTranId = getIntent().getIntExtra(CommonData.KEY_DOCTOR_CURRENCY_ID, 0);
        }
        getDoctorIncomeDetail();
    }

    private void initPage() {
        if (incomeDetailBean != null) {
            tvIncomePrice.setText(String.valueOf(incomeDetailBean.getArrived()));
            tvIncomeEstimate.setText(
                    String.format(getString(R.string.txt_personal_estimate_income), incomeDetailBean.getTotal()));
            tvPatientName.setText(incomeDetailBean.getPatientName());
            tvServiceHospital.setText(incomeDetailBean.getHospitalNamePrefix());
            tvReceiveDoctor.setText(incomeDetailBean.getDoctorNamePrefix());
            tvDiagnosisTime.setText(incomeDetailBean.getCreateAt());
            int type = incomeDetailBean.getServiceFlag();
            switch (type) {
                case CURRENCY_DETAIL_TYPE_CHECK:
                    tvHospitalTitle.setText(R.string.txt_check_hospital);
                    layoutDoctor.setVisibility(View.GONE);
                    tvServiceType.setText(R.string.txt_reserve_check);
                    addCheckType();
                    break;
                case CURRENCY_DETAIL_TYPE_TRANSFER:
                    tvHospitalTitle.setText(R.string.txt_receiving_hospital);
                    tvDoctorTitle.setText(R.string.txt_receiving_doctor);
                    tvServiceType.setText(R.string.txt_reserve_transfer);
                    layoutDoctor.setVisibility(View.VISIBLE);
                    layoutProjectChecked.setVisibility(View.GONE);
                    break;
                case CURRENCY_DETAIL_TYPE_REMOTE:
                    tvHospitalTitle.setText(R.string.txt_initiate_hospital);
                    tvDoctorTitle.setText(R.string.txt_initiate_doctor);
                    tvServiceType.setText(R.string.txt_remote_consultation);
                    layoutDoctor.setVisibility(View.VISIBLE);
                    layoutProjectChecked.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 检查项
     */
    private void addCheckType() {
        ArrayList<CheckTypeBean> list = incomeDetailBean.getExamList();
        if (list != null && list.size() > 0) {
            layoutProjectChecked.setVisibility(View.VISIBLE);
            for (int i = 0; i < list.size(); i++) {
                View view = getLayoutInflater().inflate(R.layout.item_check_type, null);
                TextView textView = view.findViewById(R.id.tv_check_type_name);
                textView.setText(list.get(i).getExamName());
                view.setTag(i);
                view.setOnClickListener(this);
                layoutProjectChecked.addView(view);
            }
        }
        else {
            layoutProjectChecked.setVisibility(View.GONE);
        }
    }

    /**
     * 提现详情
     */
    private void getDoctorIncomeDetail() {
        RequestUtils.getDoctorIncomeDetail(this, doctorOrderTranId, token, this);
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        if (task == Tasks.GET_DOCTOR_INCOME_DETAIL) {
            incomeDetailBean = (IncomeDetailBean)response.getData();
            initPage();
        }
    }
}
