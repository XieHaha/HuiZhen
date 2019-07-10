package com.yht.yihuantong.ui.transfer;

import android.app.Activity;
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
import com.yht.frame.utils.ToastUtil;
import com.yht.frame.utils.glide.GlideHelper;
import com.yht.frame.widgets.dialog.HintDialog;
import com.yht.frame.widgets.dialog.InputDialog;
import com.yht.frame.widgets.edittext.SuperEditText;
import com.yht.yihuantong.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/6/14 10:56
 * @des 预约转诊详情 其他医生转诊给我的 (有四种状态  已取消、待接诊、被拒绝、已接诊)
 */
public class TransferReceiveDetailActivity extends BaseActivity implements TransferOrderStatus {
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
    @BindView(R.id.public_title_bar_title)
    TextView publicTitleBarTitle;
    private TransferBean transferBean;
    /**
     * 拒绝转诊原因
     */
    private String rejectReason;
    /**
     * 变更接诊信息
     */
    public static final int REQUEST_CODE_UPDATE_TRANSFER = 100;
    /**
     * 接诊
     */
    public static final int REQUEST_CODE_RECEIVE_TRANSFER = 200;
    /**
     * 再次转诊
     */
    public static final int REQUEST_CODE_TRANSFER_AGAIN = 300;

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
            transferBean = (TransferBean)getIntent().getSerializableExtra(CommonData.KEY_TRANSFER_ORDER_BEAN);
        }
        getTransferOrderDetail();
        initPage();
    }

    /**
     * 获取详情
     */
    private void getTransferOrderDetail() {
        RequestUtils.getTransferOrderDetail(this, loginBean.getToken(), transferBean.getOrderNo(), this);
    }

    /**
     * 拒绝转诊
     */
    private void rejectReserveTransferOrder() {
        RequestUtils.rejectReserveTransferOrder(this, loginBean.getToken(), rejectReason, transferBean.getOrderNo(),
                                                this);
    }

    /**
     * 界面逻辑处理（已接诊、待处理）
     */
    private void initPage() {
        if (transferBean.getReceiveStatus() == TRANSFER_STATUS_RECEIVED) {
            layoutDoctorPhone.setVisibility(View.VISIBLE);
            layoutReceivingDepart.setVisibility(View.VISIBLE);
            layoutReceivingHospital.setVisibility(View.VISIBLE);
            layoutReserveTime.setVisibility(View.VISIBLE);
            layoutNotice.setVisibility(View.VISIBLE);
            layoutEditTransfer.setVisibility(View.VISIBLE);
            layoutContact.setVisibility(View.VISIBLE);
            layoutCall.setVisibility(View.INVISIBLE);
            layoutReceived.setVisibility(View.GONE);
            ivCheckStatus.setImageResource(R.mipmap.ic_status_received);
            publicTitleBarTitle.setText(R.string.title_received_transfer_detail);
        }
    }

    private void initDetailData() {
        Glide.with(this)
             .load(transferBean.getWxPhoto())
             .apply(GlideHelper.getOptions(BaseUtils.dp2px(this, 4)))
             .into(ivPatientImg);
        tvDoctorPhone.setText(transferBean.getSourceDoctorMobile());
        tvReceivingDoctor.setText(transferBean.getTargetDoctorName());
        tvReceivingDepart.setText(transferBean.getTargetHospitalDepartmentName());
        tvReceivingHospital.setText(transferBean.getTargetHospitalName());
        tvReserveTime.setText(transferBean.getAppointAt());
        tvTransferNotice.setText(transferBean.getNote());
        tvPatientName.setText(transferBean.getPatientName());
        tvPhone.setText(transferBean.getPatientMobile());
        tvPatientSex.setText(transferBean.getSex() == BaseData.BASE_ONE
                             ? getString(R.string.txt_sex_male)
                             : getString(R.string.txt_sex_female));
        tvPatientAge.setText(String.valueOf(transferBean.getPatientAge()));
        tvIcCard.setText(transferBean.getPatientIdCardNo());
        tvPastMedical.setText(transferBean.getPastHistory());
        tvFamilyMedical.setText(transferBean.getFamilyHistory());
        tvAllergies.setText(transferBean.getAllergyHistory());
        tvTransferTime.setText(transferBean.getTransferDate());
        tvTransferDepart.setText(transferBean.getSourceHospitalDepartmentName());
        tvTransferHospital.setText(transferBean.getSourceHospitalName());
        tvTransferPurpose.setText(transferBean.getTransferTarget());
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
        tvInitiateDiagnosis.setText(transferBean.getInitResult());
        int status = transferBean.getReceiveStatus();
        switch (status) {
            case TRANSFER_STATUS_WAIT:
                tvReceivingStatus.setText(getString(R.string.txt_status_wait));
                break;
            case TRANSFER_STATUS_RECEIVED:
                tvReceivingStatus.setText(getString(R.string.txt_status_received));
                break;
            case TRANSFER_STATUS_CANCEL:
                tvReceivingStatus.setText(getString(R.string.txt_status_cancel));
                break;
            case TRANSFER_STATUS_REFUSE:
                tvReceivingStatus.setText(getString(R.string.txt_status_reject));
                break;
            default:
                break;
        }
    }

    @OnClick({
            R.id.layout_call, R.id.layout_edit_transfer, R.id.tv_transfer_other, R.id.tv_refuse, R.id.tv_received,
            R.id.tv_contact_doctor, R.id.tv_contact_patient })
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.layout_call:
                new HintDialog(this).setPhone(transferBean.getSourceDoctorMobile())
                                    .setOnEnterClickListener(() -> callPhone(transferBean.getSourceDoctorMobile()))
                                    .show();
                break;
            case R.id.layout_edit_transfer:
                intent = new Intent(this, TransferEditActivity.class);
                intent.putExtra(CommonData.KEY_RECEIVE_OR_EDIT_VISIT, true);
                intent.putExtra(CommonData.KEY_TRANSFER_ORDER_BEAN, transferBean);
                startActivityForResult(intent, REQUEST_CODE_UPDATE_TRANSFER);
                break;
            case R.id.tv_transfer_other:
                intent = new Intent(this, TransferAgainActivity.class);
                intent.putExtra(CommonData.KEY_TRANSFER_ORDER_ID, transferBean.getOrderNo());
                startActivityForResult(intent, REQUEST_CODE_TRANSFER_AGAIN);
                break;
            case R.id.tv_refuse:
                new InputDialog(this).Builder()
                                     .setEditHintText(getString(R.string.txt_reject_transfer_reason_hint))
                                     .setEnterBtnTxt(getString(R.string.txt_refuse))
                                     .setEnterSelect(true)
                                     .setOnEnterClickListener(() -> {
                                     })
                                     .setResultListener(result -> {
                                         rejectReason = result;
                                         rejectReserveTransferOrder();
                                     })
                                     .show();
                break;
            case R.id.tv_received:
                intent = new Intent(this, TransferEditActivity.class);
                intent.putExtra(CommonData.KEY_TRANSFER_ORDER_BEAN, transferBean);
                startActivityForResult(intent, REQUEST_CODE_RECEIVE_TRANSFER);
                break;
            case R.id.tv_contact_patient:
                new HintDialog(this).setPhone(transferBean.getPatientMobile())
                                    .setOnEnterClickListener(() -> callPhone(transferBean.getPatientMobile()))
                                    .show();
                break;
            case R.id.tv_contact_doctor:
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
            case RECEIVE_RESERVE_TRANSFER_ORDER:
            case REJECT_RESERVE_TRANSFER_ORDER:
                ToastUtil.toast(this, response.getMsg());
                //通知列表刷新
                setResult(RESULT_OK);
                finish();
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
        switch (requestCode) {
            case REQUEST_CODE_UPDATE_TRANSFER:
                setResult(RESULT_OK);
                getTransferOrderDetail();
                break;
            case REQUEST_CODE_RECEIVE_TRANSFER:
            case REQUEST_CODE_TRANSFER_AGAIN:
                setResult(RESULT_OK);
                finish();
                break;
            default:
                break;
        }
    }
}
