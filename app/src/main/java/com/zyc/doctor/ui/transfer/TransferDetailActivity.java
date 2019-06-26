package com.zyc.doctor.ui.transfer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yht.frame.ui.BaseActivity;
import com.yht.frame.widgets.dialog.HintDialog;
import com.yht.frame.widgets.dialog.InputDialog;
import com.zyc.doctor.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/6/14 10:56
 * @des 预约转诊详情
 */
public class TransferDetailActivity extends BaseActivity {
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

    @OnClick({ R.id.tv_transfer_again, R.id.tv_contact_patient, R.id.tv_contact_doctor })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_transfer_again:
                new InputDialog(this).Builder()
                                     .setCancleBtnTxt("确认取消")
                                     .setEnterBtnTxt("再想想")
                                     .setEnterSelect(true)
                                     .show();
                break;
            case R.id.tv_contact_patient:
                new HintDialog(this).setTitleString(getString(R.string.txt_hint))
                                    .setContentString(getString(R.string.txt_contact_hotline))
                                    .setEnterBtnTxt(getString(R.string.txt_call))
                                    .setEnterSelect(true)
                                    .setOnEnterClickListener(() -> callPhone(""))
                                    .show();
                break;
            case R.id.tv_contact_doctor:
                new HintDialog(this).setTitleString(getString(R.string.txt_hint))
                                    .setContentString(getString(R.string.txt_contact_hotline))
                                    .setEnterBtnTxt(getString(R.string.txt_call))
                                    .setEnterSelect(true)
                                    .setOnEnterClickListener(() -> callPhone(""))
                                    .show();
                break;
            default:
                break;
        }
    }
}
