package com.zyc.doctor.ui.transfer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yht.frame.data.CommonData;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.widgets.dialog.HintDialog;
import com.yht.frame.widgets.dialog.InputDialog;
import com.yht.frame.widgets.edittext.SuperEditText;
import com.zyc.doctor.R;
import com.zyc.doctor.utils.glide.GlideHelper;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/6/14 10:56
 * @des 预约转诊详情 其他医生转诊给我的 (有四种状态  已取消、待接诊、被拒绝、已接诊)
 */
public class TransferFromDetailActivity extends BaseActivity {
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
    @BindView(R.id.tv_ic_card)
    SuperEditText tvIcCard;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_past_medical)
    TextView tvPastMedical;
    @BindView(R.id.tv_family_medical)
    TextView tvFamilyMedical;
    @BindView(R.id.tv_allergies)
    TextView tvAllergies;
    @BindView(R.id.tv_transfer_time)
    TextView tvTransferTime;
    @BindView(R.id.tv_receiving_doctor)
    TextView tvReceivingDoctor;
    @BindView(R.id.tv_transfer_depart)
    TextView tvTransferDepart;
    @BindView(R.id.tv_transfer_hospital)
    TextView tvTransferHospital;
    @BindView(R.id.tv_doctor_phone)
    TextView tvDoctorPhone;
    @BindView(R.id.tv_transfer_type)
    TextView tvTransferType;
    @BindView(R.id.tv_transfer_purpose)
    TextView tvTransferPurpose;
    @BindView(R.id.tv_payment)
    TextView tvPayment;
    @BindView(R.id.tv_initiate_diagnosis)
    TextView tvInitiateDiagnosis;
    @BindView(R.id.tv_receiving_status)
    TextView tvReceivingStatus;
    @BindView(R.id.tv_receiving_depart)
    TextView tvReceivingDepart;
    @BindView(R.id.tv_receiving_hospital)
    TextView tvReceivingHospital;
    @BindView(R.id.tv_reserve_time)
    TextView tvReserveTime;
    @BindView(R.id.tv_transfer_notice)
    TextView tvTransferNotice;
    @BindView(R.id.layout_call)
    LinearLayout layoutCall;
    @BindView(R.id.layout_doctor_phone)
    RelativeLayout layoutDoctorPhone;
    @BindView(R.id.layout_receiving_depart)
    RelativeLayout layoutReceivingDepart;
    @BindView(R.id.layout_receiving_hospital)
    RelativeLayout layoutReceivingHospital;
    @BindView(R.id.layout_reserve_time)
    RelativeLayout layoutReserveTime;
    @BindView(R.id.layout_notice)
    RelativeLayout layoutNotice;
    @BindView(R.id.layout_edit_transfer)
    RelativeLayout layoutEditTransfer;
    @BindView(R.id.layout_received)
    LinearLayout layoutReceived;
    @BindView(R.id.layout_contact)
    LinearLayout layoutContact;
    /**
     * 是否已接诊
     */
    private boolean isReceive;

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_transfer_from_detail;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        if (getIntent() != null) {
            isReceive = getIntent().getBooleanExtra(CommonData.KEY_IS_RECEIVE_TRANSFER, false);
        }
        initPage();
    }

    /**
     * 界面逻辑处理（已接诊、待处理）
     */
    private void initPage() {
        if (isReceive) {
            layoutDoctorPhone.setVisibility(View.VISIBLE);
            layoutReceivingDepart.setVisibility(View.VISIBLE);
            layoutReceivingHospital.setVisibility(View.VISIBLE);
            layoutReserveTime.setVisibility(View.VISIBLE);
            layoutNotice.setVisibility(View.VISIBLE);
            layoutEditTransfer.setVisibility(View.VISIBLE);
            layoutContact.setVisibility(View.VISIBLE);
            layoutCall.setVisibility(View.GONE);
            layoutReceived.setVisibility(View.GONE);
        }
    }

    private void init() {
        tvDoctorPhone.setText("");
        tvReceivingDepart.setText("");
        tvReceivingHospital.setText("");
        tvReserveTime.setText("");
        tvTransferNotice.setText("");
        Glide.with(this).load("").apply(GlideHelper.getOptionsPic(BaseUtils.dp2px(this, 4))).into(ivPatientImg);
        ivCheckStatus.setImageResource(R.mipmap.ic_received_transfer);
        tvPatientName.setText("");
        tvPhone.setText("");
        tvPatientSex.setText("");
        tvPatientAge.setText("");
        tvIcCard.setText("");
        tvPastMedical.setText("");
        tvFamilyMedical.setText("");
        tvAllergies.setText("");
        tvTransferTime.setText("");
        tvReceivingDoctor.setText("");
        tvTransferDepart.setText("");
        tvTransferHospital.setText("");
        tvTransferType.setText("");
        tvTransferPurpose.setText("");
        tvPayment.setText("");
        tvInitiateDiagnosis.setText("");
        tvReceivingStatus.setText("");
    }

    @OnClick({
            R.id.layout_call, R.id.layout_edit_transfer, R.id.tv_transfer_other, R.id.tv_refuse, R.id.tv_received,
            R.id.tv_contact_doctor, R.id.tv_contact_patient })
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.layout_call:
                break;
            case R.id.layout_edit_transfer:
                intent = new Intent(this, TransferEditActivity.class);
                intent.putExtra(CommonData.KEY_RECEIVE_OR_EDIT_VISIT, true);
                intent.putExtra(CommonData.KEY_RECEIVE_HOSPITAL, "接诊医院");
                intent.putExtra(CommonData.KEY_RESERVE_TIME, "预约就诊时间");
                startActivity(intent);
                break;
            case R.id.tv_transfer_other:
                intent = new Intent(this, TransferAgainActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_refuse:
                new InputDialog(this).Builder().setCancleBtnTxt("拒绝").setEnterBtnTxt("再想想").setEnterSelect(true).show();
                break;
            case R.id.tv_received:
                intent = new Intent(this, TransferEditActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_contact_patient:
                new HintDialog(this).setPhone("").setOnEnterClickListener(() -> callPhone("")).show();
                break;
            case R.id.tv_contact_doctor:
                new HintDialog(this).setPhone("").setOnEnterClickListener(() -> callPhone("")).show();
                break;
            default:
                break;
        }
    }
}
