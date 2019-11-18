package com.yht.yihuantong.ui.transfer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.NavUtils;
import androidx.core.app.TaskStackBuilder;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yht.frame.data.BaseData;
import com.yht.frame.data.BaseNetConfig;
import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.CommonData;
import com.yht.frame.data.Tasks;
import com.yht.frame.data.bean.TransferDetailBean;
import com.yht.frame.data.type.TransferOrderStatus;
import com.yht.frame.http.retrofit.RequestUtils;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.utils.ToastUtil;
import com.yht.frame.utils.glide.GlideHelper;
import com.yht.frame.widgets.LoadViewHelper;
import com.yht.frame.widgets.dialog.HintDialog;
import com.yht.frame.widgets.dialog.InputDialog;
import com.yht.frame.widgets.textview.JustifiedTextView;
import com.yht.yihuantong.R;
import com.yht.yihuantong.scheme.ViewUtils;
import com.yht.yihuantong.ui.main.MainActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/6/14 10:56
 * @des 预约转诊详情 其他医生转诊给我的 (有四种状态  已取消、待接诊、被拒绝、已接诊)
 */
public class TransferReceiveDetailActivity extends BaseActivity
        implements TransferOrderStatus, LoadViewHelper.OnNextClickListener {
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
    TextView tvIcCard;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_past_medical)
    TextView tvPastMedical;
    @BindView(R.id.tv_family_medical)
    TextView tvFamilyMedical;
    @BindView(R.id.tv_allergies)
    JustifiedTextView tvAllergies;
    @BindView(R.id.tv_transfer_time)
    TextView tvTransferTime;
    @BindView(R.id.tv_receiving_doctor)
    TextView tvSourceDoctorName;
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
    JustifiedTextView tvInitiateDiagnosis;
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
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.layout_cancel_result)
    RelativeLayout layoutCancelResult;
    @BindView(R.id.layout_hint)
    LinearLayout layoutHint;
    @BindView(R.id.tv_transfer_description)
    TextView tvTransferDescription;
    @BindView(R.id.layout_transfer_description)
    RelativeLayout layoutTransferDescription;
    private TransferDetailBean transferBean;
    private String orderNo;
    /**
     * 拒绝转诊原因
     */
    private String rejectReason;
    /**
     * 是否为外链打开
     */
    private boolean isOuterChain;
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
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        loadViewHelper = new LoadViewHelper(this);
        loadViewHelper.setOnNextClickListener(this);
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        if (getIntent() != null) {
            orderNo = getIntent().getStringExtra(CommonData.KEY_ORDER_ID);
            isOuterChain = getIntent().getBooleanExtra(CommonData.KEY_IS_OUTER_CHAIN, false);
        }
        if (BaseUtils.isNetworkAvailable(this)) {
            getTransferOrderDetail();
        }
        else {
            layoutHint.setVisibility(View.VISIBLE);
            loadViewHelper.load(LoadViewHelper.NONE_NETWORK);
        }
    }

    /**
     * 获取详情
     */
    private void getTransferOrderDetail() {
        RequestUtils.getTransferOrderDetail(this, loginBean.getToken(), orderNo, this);
    }

    /**
     * 拒绝转诊
     */
    private void rejectReserveTransferOrder() {
        RequestUtils.rejectReserveTransferOrder(this, loginBean.getToken(), rejectReason, orderNo, this);
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
            layoutCall.setVisibility(View.INVISIBLE);
            layoutContact.setVisibility(View.VISIBLE);
            layoutReceived.setVisibility(View.INVISIBLE);
            ivCheckStatus.setImageResource(R.mipmap.ic_status_received);
            publicTitleBarTitle.setText(R.string.title_received_transfer_detail);
        }
    }

    private void initDetailData() {
        Glide.with(this)
             .load(transferBean.getPhoto())
             .apply(GlideHelper.getOptions(BaseUtils.dp2px(this, 4)))
             .into(ivPatientImg);
        tvDoctorPhone.setText(transferBean.getSourceDoctorMobile());
        tvReceivingDepart.setText(transferBean.getTargetHospitalDepartmentName());
        tvReceivingHospital.setText(transferBean.getTargetHospitalName());
        tvReserveTime.setText(transferBean.getAppointAt());
        tvTransferNotice.setText(transferBean.getNote());
        tvPatientName.setText(transferBean.getPatientName());
        tvPhone.setText(BaseUtils.asteriskUserPhone(transferBean.getPatientMobile()));
        tvIcCard.setText(BaseUtils.asteriskUserCard(transferBean.getPatientIdCardNo(),12));
        tvPatientSex.setText(transferBean.getSex() == BaseData.BASE_ONE
                             ? getString(R.string.txt_sex_male)
                             : getString(R.string.txt_sex_female));
        tvPatientAge.setText(String.valueOf(transferBean.getPatientAge()));
        tvPastMedical.setText(transferBean.getPastHistory());
        tvFamilyMedical.setText(transferBean.getFamilyHistory());
        tvAllergies.setText(transferBean.getAllergyHistory());
        tvTransferTime.setText(transferBean.getTransferDate());
        tvSourceDoctorName.setText(transferBean.getSourceDoctorName());
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
                ivCheckStatus.setImageResource(R.mipmap.ic_tag_wait_transfer);
                layoutContact.setVisibility(View.GONE);
                layoutReceived.setVisibility(View.VISIBLE);
                break;
            case TRANSFER_STATUS_RECEIVED:
                tvReceivingStatus.setText(getString(R.string.txt_status_received));
                ivCheckStatus.setImageResource(R.mipmap.ic_status_received);
                //只有已接诊才显示全
                tvPhone.setText(transferBean.getPatientMobile());
                tvIcCard.setText(BaseUtils.spaceUserCard(transferBean.getPatientIdCardNo()));
                break;
            case TRANSFER_STATUS_CANCEL:
                tvReceivingStatus.setText(getString(R.string.txt_status_cancel));
                ivCheckStatus.setImageResource(R.mipmap.ic_status_cancel);
                layoutCancelResult.setVisibility(View.VISIBLE);
                tvCancel.setText(transferBean.getCancelReason());
                layoutContact.setVisibility(View.GONE);
                layoutReceived.setVisibility(View.GONE);
                break;
            case TRANSFER_STATUS_REFUSE:
                tvReceivingStatus.setText(getString(R.string.txt_status_reject));
                ivCheckStatus.setImageResource(R.mipmap.ic_status_rejected);
                layoutContact.setVisibility(View.GONE);
                layoutReceived.setVisibility(View.GONE);
                break;
            default:
                break;
        }
        if (!TextUtils.equals(transferBean.getTargetDoctorCode(), loginBean.getDoctorCode())) {
            tvReceivingStatus.setText(
                    String.format(getString(R.string.txt_transfer_other_doctor), transferBean.getTargetDoctorName()));
            tvTransferDescription.setText(transferBean.getTransferReason());
            layoutTransferDescription.setVisibility(View.VISIBLE);
            layoutContact.setVisibility(View.GONE);
            layoutReceived.setVisibility(View.GONE);
            ivCheckStatus.setVisibility(View.GONE);
            layoutCancelResult.setVisibility(View.GONE);
        }
    }

    @OnClick({
            R.id.public_title_bar_back, R.id.layout_call, R.id.layout_edit_transfer, R.id.tv_transfer_other,
            R.id.tv_refuse, R.id.tv_received, R.id.tv_contact_doctor, R.id.tv_contact_patient })
    public void onViewClicked(View view) {
        if (transferBean == null) { return; }
        Intent intent;
        switch (view.getId()) {
            case R.id.public_title_bar_back:
                onFinish();
                finish();
                break;
            case R.id.layout_call:
                new HintDialog(this).setPhone(getString(R.string.txt_contact_doctor_phone),
                                              transferBean.getSourceDoctorMobile())
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
                intent.putExtra(CommonData.KEY_ORDER_ID, transferBean.getOrderNo());
                startActivityForResult(intent, REQUEST_CODE_TRANSFER_AGAIN);
                break;
            case R.id.tv_refuse:
                new InputDialog(this).Builder()
                                     .setCancelable(false)
                                     .setCanceledOnTouchOutside(false)
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
                new HintDialog(this).setPhone(getString(R.string.txt_contact_patient_phone),
                                              transferBean.getPatientMobile())
                                    .setOnEnterClickListener(() -> callPhone(transferBean.getPatientMobile()))
                                    .show();
                break;
            case R.id.tv_contact_doctor:
                new HintDialog(this).setPhone(getString(R.string.txt_contact_doctor_phone),
                                              transferBean.getSourceDoctorMobile())
                                    .setOnEnterClickListener(() -> callPhone(transferBean.getSourceDoctorMobile()))
                                    .show();
                break;
            default:
                break;
        }
    }

    @Override
    public void onNextClick() {
        getTransferOrderDetail();
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        switch (task) {
            case GET_TRANSFER_ORDER_DETAIL:
                layoutHint.setVisibility(View.GONE);
                transferBean = (TransferDetailBean)response.getData();
                initPage();
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
    public void onResponseCode(Tasks task, BaseResponse response) {
        super.onResponseCode(task, response);
        if (response.getCode() == BaseNetConfig.REQUEST_ORDER_ERROR) {
            ToastUtil.toast(this, response.getMsg());
            getTransferOrderDetail();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_CANCELED &&
            (requestCode == REQUEST_CODE_TRANSFER_AGAIN || requestCode == REQUEST_CODE_RECEIVE_TRANSFER)) {
            setResult(RESULT_OK);
            getTransferOrderDetail();
        }
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

    private void onFinish() {
        hideSoftInputFromWindow();
        if (isOuterChain) {
            if (ViewUtils.isLaunchedActivity(this, MainActivity.class)) {
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    TaskStackBuilder.create(this).addNextIntentWithParentStack(upIntent).startActivities();
                }
                else {
                    upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    NavUtils.navigateUpTo(this, upIntent);
                }
            }
            else {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onBackPressed() {
        onFinish();
        super.onBackPressed();
    }
}
