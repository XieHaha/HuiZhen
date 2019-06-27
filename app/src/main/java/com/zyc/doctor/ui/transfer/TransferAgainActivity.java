package com.zyc.doctor.ui.transfer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yht.frame.ui.BaseActivity;
import com.zyc.doctor.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/6/27 19:41
 * @des 再次转诊
 */
public class TransferAgainActivity extends BaseActivity {
    @BindView(R.id.tv_select)
    TextView tvSelect;
    @BindView(R.id.iv_receiving_doctor)
    ImageView ivReceivingDoctor;
    @BindView(R.id.tv_receiving_doctor_name)
    TextView tvReceivingDoctorName;
    @BindView(R.id.tv_receiving_doctor_title)
    TextView tvReceivingDoctorTitle;
    @BindView(R.id.tv_receiving_doctor_hospital_depart)
    TextView tvReceivingDoctorHospitalDepart;
    @BindView(R.id.iv_receiving_doctor_call)
    ImageView ivReceivingDoctorCall;
    @BindView(R.id.layout_receiving_doctor)
    RelativeLayout layoutReceivingDoctor;
    @BindView(R.id.et_notice)
    EditText etNotice;
    @BindView(R.id.tv_notice_num)
    TextView tvNoticeNum;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    /**
     * 选择接诊医生
     */
    public static final int REQUEST_CODE_SELECT_DOCTOR = 100;

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_transfer_again;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        ivReceivingDoctorCall.setImageResource(R.mipmap.ic_delete);
        if (getIntent() != null) {
        }
    }

    @OnClick({ R.id.layout_doctor, R.id.tv_submit, R.id.iv_receiving_doctor_call })
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.layout_doctor:
                if (tvSelect.getVisibility() == View.VISIBLE) {
                    intent = new Intent(this, SelectReceivingDoctorActivity.class);
                    startActivityForResult(intent, REQUEST_CODE_SELECT_DOCTOR);
                }
                break;
            case R.id.iv_receiving_doctor_call:
                tvSelect.setVisibility(View.VISIBLE);
                layoutReceivingDoctor.setVisibility(View.GONE);
                break;
            case R.id.tv_submit:
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_SELECT_DOCTOR) {
            tvSelect.setVisibility(View.GONE);
            layoutReceivingDoctor.setVisibility(View.VISIBLE);
        }
    }
}
