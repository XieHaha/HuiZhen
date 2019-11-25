package com.yht.yihuantong.ui.transfer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.NavUtils;
import androidx.core.app.TaskStackBuilder;

import com.bumptech.glide.Glide;
import com.yht.frame.data.BaseData;
import com.yht.frame.data.BaseNetConfig;
import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.CommonData;
import com.yht.frame.data.Tasks;
import com.yht.frame.data.bean.ReservationValidateBean;
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
import com.yht.yihuantong.ui.reservation.ReservationDisableActivity;
import com.yht.yihuantong.ui.reservation.transfer.ReservationTransferActivity;
import com.yht.yihuantong.utils.FileUrlUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/6/14 10:56
 * @description 预约转诊详情  我转诊给其他医生 (有两种状态  待接诊、已接诊)
 */
public class TransferInitiateDetailActivity extends BaseActivity
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
    @BindView(R.id.tv_transfer_time)
    TextView tvTransferTime;
    @BindView(R.id.tv_initiate_diagnosis)
    JustifiedTextView tvInitiateDiagnosis;
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
    @BindView(R.id.layout_bottom)
    LinearLayout layoutBottom;
    @BindView(R.id.layout_bottom_one)
    LinearLayout layoutBottomOne;
    @BindView(R.id.layout_receive_depart)
    RelativeLayout layoutReceiveDepart;
    @BindView(R.id.layout_receive_hospital)
    RelativeLayout layoutReceiveHospital;
    @BindView(R.id.layout_receive_time)
    RelativeLayout layoutReceiveTime;
    @BindView(R.id.layout_cancel_result)
    RelativeLayout layoutCancelResult;
    @BindView(R.id.layout_receive_notice)
    RelativeLayout layoutReceiveNotice;
    @BindView(R.id.tv_transfer_reject)
    TextView tvTransferReject;
    @BindView(R.id.layout_reject_result)
    RelativeLayout layoutRejectResult;
    @BindView(R.id.layout_hint)
    LinearLayout layoutHint;
    @BindView(R.id.tv_past_medical)
    JustifiedTextView tvPastMedical;
    @BindView(R.id.tv_family_medical)
    JustifiedTextView tvFamilyMedical;
    @BindView(R.id.tv_allergies)
    JustifiedTextView tvAllergies;
    /**
     * 订单 详情
     */
    private TransferDetailBean transferBean;
    /**
     * 订单编号
     */
    private String orderNo;
    /**
     * 取消转诊原因
     */
    private String cancelReason;
    /**
     * 是否为外链打开
     */
    private boolean isOuterChain;
    /**
     * 是否能发起转诊
     */
    private boolean applyTransferAble = false;

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_transfer_to_detail;
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
            transferBean =
                    (TransferDetailBean) getIntent().getSerializableExtra(CommonData.KEY_TRANSFER_ORDER_BEAN);
            orderNo = getIntent().getStringExtra(CommonData.KEY_ORDER_ID);
            isOuterChain = getIntent().getBooleanExtra(CommonData.KEY_IS_OUTER_CHAIN, false);
        }
        if (transferBean != null) {
            orderNo = transferBean.getOrderNo();
        }
        initDetailData();
        if (BaseUtils.isNetworkAvailable(this)) {
            getTransferOrderDetail();
            getValidateHospitalList();
        } else {
            layoutHint.setVisibility(View.VISIBLE);
            loadViewHelper.load(LoadViewHelper.NONE_NETWORK);
        }
    }

    /**
     * 详情数据
     */
    private void initDetailData() {
        if (transferBean == null) {
            return;
        }
        tvPastMedical.setText(transferBean.getPastHistory());
        tvFamilyMedical.setText(transferBean.getFamilyHistory());
        tvAllergies.setText(transferBean.getAllergyHistory());
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
        tvInitiateDiagnosis.setText(transferBean.getInitResult());
        tvTransferType.setText(transferBean.getTransferType() == BaseData.BASE_ZERO
                ? getString(R.string.txt_transfer_up)
                : getString(R.string.txt_transfer_down));
        tvTransferPurpose.setText(transferBean.getTransferTarget());
        int payType = transferBean.getPayType();
        if (payType == BaseData.BASE_ZERO) {
            tvPayment.setText(getString(R.string.txt_self_pay));
        } else if (payType == BaseData.BASE_ONE) {
            tvPayment.setText(getString(R.string.txt_medicare));
        } else {
            tvPayment.setText(getString(R.string.txt_self_medicare));
        }
        tvReceivingDoctor.setText(transferBean.getTargetDoctorName());
        int status = transferBean.getReceiveStatus();
        switch (status) {
            case TRANSFER_STATUS_WAIT:
                layoutBottom.setVisibility(View.VISIBLE);
                layoutBottomOne.setVisibility(View.GONE);
                layoutRejectResult.setVisibility(View.GONE);
                ivCheckStatus.setImageResource(R.mipmap.ic_status_wait);
                tvTransferAgain.setText(R.string.txt_transfer_cancel);
                tvReceivingStatus.setText(getString(R.string.txt_status_wait));
                break;
            case TRANSFER_STATUS_RECEIVED:
                layoutBottom.setVisibility(View.GONE);
                layoutBottomOne.setVisibility(View.VISIBLE);
                layoutReceiveDepart.setVisibility(View.VISIBLE);
                layoutReceiveHospital.setVisibility(View.VISIBLE);
                layoutReceiveTime.setVisibility(View.VISIBLE);
                layoutReceiveNotice.setVisibility(View.VISIBLE);
                layoutRejectResult.setVisibility(View.GONE);
                ivCheckStatus.setImageResource(R.mipmap.ic_status_received);
                tvReceivingStatus.setText(getString(R.string.txt_status_received));
                tvReceivingDepart.setText(transferBean.getTargetHospitalDepartmentName());
                tvReceivingHospital.setText(transferBean.getTargetHospitalName());
                tvReserveTime.setText(transferBean.getAppointAt());
                tvTransferNotice.setText(transferBean.getNote());
                break;
            case TRANSFER_STATUS_CANCEL:
                layoutBottom.setVisibility(View.VISIBLE);
                layoutBottomOne.setVisibility(View.GONE);
                layoutCancelResult.setVisibility(View.VISIBLE);
                layoutRejectResult.setVisibility(View.GONE);
                ivCheckStatus.setImageResource(R.mipmap.ic_status_cancel);
                tvReceivingStatus.setText(getString(R.string.txt_status_cancel));
                tvTransferAgain.setText(R.string.txt_transfer_again);
                tvTransferCancel.setText(transferBean.getCancelReason());
                break;
            case TRANSFER_STATUS_REFUSE:
                layoutBottom.setVisibility(View.VISIBLE);
                layoutBottomOne.setVisibility(View.GONE);
                ivCheckStatus.setImageResource(R.mipmap.ic_status_be_rejected);
                tvReceivingStatus.setText(getString(R.string.txt_status_rejected));
                layoutRejectResult.setVisibility(View.VISIBLE);
                tvTransferReject.setText(transferBean.getRejectReason());
                break;
            default:
                break;
        }
    }

    /**
     * 获取详情
     */
    private void getTransferOrderDetail() {
        RequestUtils.getTransferOrderDetail(this, loginBean.getToken(), orderNo, this);
    }

    /**
     * 取消转诊
     */
    private void cancelReserveTransferOrder() {
        RequestUtils.cancelReserveTransferOrder(this, loginBean.getToken(), cancelReason, orderNo
                , this);
    }

    /**
     * 校验医生是否有预约检查和预约转诊的合作医院
     */
    private void getValidateHospitalList() {
        RequestUtils.getValidateHospitalList(this, loginBean.getToken(), false,this);
    }

    /**
     * 查询居民是否存在未完成的转诊单
     */
    private void getPatientExistTransfer() {
        if (transferBean != null) {
            RequestUtils.getPatientExistTransfer(this, loginBean.getToken(),
                    transferBean.getPatientCode(), this);
        }
    }

    @OnClick({
            R.id.public_title_bar_back, R.id.tv_transfer_again, R.id.tv_contact_patient,
            R.id.tv_contact_doctor,
            R.id.tv_contact_patient_one, R.id.tv_contact_doctor_one})
    public void onViewClicked(View view) {
        if (transferBean == null) {
            return;
        }
        switch (view.getId()) {
            case R.id.public_title_bar_back:
                hideSoftInputFromWindow();
                onFinish();
                finish();
                break;
            case R.id.tv_transfer_again:
                if (TRANSFER_STATUS_WAIT == transferBean.getReceiveStatus()) {
                    new InputDialog(this).Builder()
                            .setCancelable(false)
                            .setCanceledOnTouchOutside(false)
                            .setEditHintText(getString(R.string.txt_cancel_transfer_reason_hint))
                            .setCancleBtnTxt(getString(R.string.txt_sure_cancel))
                            .setEnterBtnTxt(getString(R.string.txt_think_again))
                            .setEnterSelect(true)
                            .setLeft(true)
                            .setOnCancelClickListener(() -> {
                            })
                            .setResultListener(result -> {
                                cancelReason = result;
                                cancelReserveTransferOrder();
                            })
                            .show();
                } else {
                    if (applyTransferAble) {
                        getPatientExistTransfer();
                    } else {
                        Intent intent = new Intent(this, ReservationDisableActivity.class);
                        intent.putExtra(CommonData.KEY_RESERVATION_TYPE, BASE_ONE);
                        startActivity(intent);
                    }
                }
                break;
            case R.id.tv_contact_patient:
            case R.id.tv_contact_patient_one:
                new HintDialog(this).setPhone(getString(R.string.txt_contact_patient_phone),
                        transferBean.getPatientMobile())
                        .setOnEnterClickListener(() -> callPhone(transferBean.getPatientMobile()))
                        .show();
                break;
            case R.id.tv_contact_doctor:
            case R.id.tv_contact_doctor_one:
                new HintDialog(this).setPhone(getString(R.string.txt_contact_doctor_phone),
                        transferBean.getTargetDoctorMobile())
                        .setOnEnterClickListener(() -> callPhone(transferBean.getTargetDoctorMobile()))
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
                transferBean = (TransferDetailBean) response.getData();
                initDetailData();
                break;
            case CANCEL_RESERVE_TRANSFER_ORDER:
                //通知列表刷新
                setResult(RESULT_OK);
                getTransferOrderDetail();
                break;
            case GET_PATIENT_EXIST_TRANSFER:
                boolean exist = (boolean) response.getData();
                if (exist) {
                    ToastUtil.toast(this, R.string.txt_patient_exist_transfer);
                } else {
                    //重新转诊
                    Intent intent = new Intent(this, ReservationTransferActivity.class);
                    intent.putExtra(CommonData.KEY_TRANSFER_ORDER_BEAN, transferBean);
                    startActivity(intent);
                }
                break;
            case GET_VALIDATE_HOSPITAL_LIST:
                ReservationValidateBean bean = (ReservationValidateBean) response.getData();
                if (bean != null) {
                    applyTransferAble = bean.isZz();
                }
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
            setResult(RESULT_OK);
            getTransferOrderDetail();
        }
    }

    private void onFinish() {
        if (isOuterChain) {
            if (ViewUtils.isLaunchedActivity(this, MainActivity.class)) {
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    TaskStackBuilder.create(this).addNextIntentWithParentStack(upIntent).startActivities();
                } else {
                    upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    NavUtils.navigateUpTo(this, upIntent);
                }
            } else {
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
