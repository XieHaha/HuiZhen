package com.yht.yihuantong.ui.transfer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yht.frame.data.BaseNetConfig;
import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.CommonData;
import com.yht.frame.data.Tasks;
import com.yht.frame.data.bean.ReceiverDoctorBean;
import com.yht.frame.http.retrofit.RequestUtils;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.utils.ToastUtil;
import com.yht.frame.utils.glide.GlideHelper;
import com.yht.frame.widgets.edittext.AbstractTextWatcher;
import com.yht.frame.widgets.edittext.MultiLineEditText;
import com.yht.yihuantong.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/6/27 19:41
 * @description 再次转诊
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
    @BindView(R.id.layout_selected_doctor)
    LinearLayout layoutReceivingDoctor;
    @BindView(R.id.et_notice)
    MultiLineEditText etNotice;
    @BindView(R.id.tv_notice_num)
    TextView tvNoticeNum;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    /**
     * 当前选中的医生
     */
    private ReceiverDoctorBean curReceiveDoctor;
    /**
     * 理由
     */
    private String noticeText;
    /**
     * 订单编号
     */
    private String orderNo;
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
            orderNo = getIntent().getStringExtra(CommonData.KEY_ORDER_ID);
        }
    }

    @Override
    public void initListener() {
        super.initListener();
        etNotice.addTextChangedListener(new AbstractTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                noticeText = s.toString().trim();
                tvNoticeNum.setText(String.format(getString(R.string.txt_calc_num), noticeText.length()));
                initNextButton();
            }
        });
    }

    /**
     * 再次转诊
     */
    private void transferAgainOtherDoctor() {
        RequestUtils.transferAgainOtherDoctor(this, loginBean.getToken(), orderNo, curReceiveDoctor.getDoctorCode(),
                                              noticeText, this);
    }

    /**
     * 接诊医生
     */
    private void initReceiveDoctor() {
        tvSelect.setVisibility(View.GONE);
        layoutReceivingDoctor.setVisibility(View.VISIBLE);
        if (curReceiveDoctor != null) {
            Glide.with(this)
                 .load(curReceiveDoctor.getPhoto())
                 .apply(GlideHelper.getOptions(BaseUtils.dp2px(this, 4)))
                 .into(ivReceivingDoctor);
            tvReceivingDoctorName.setText(curReceiveDoctor.getDoctorName());
            tvReceivingDoctorTitle.setText(curReceiveDoctor.getHospitalName());
            tvReceivingDoctorHospitalDepart.setText(
                    curReceiveDoctor.getJobTitle() + "  " + curReceiveDoctor.getDepartmentName());
            ivReceivingDoctorCall.setImageResource(R.mipmap.ic_delete_red);
        }
        initNextButton();
    }

    /**
     * next
     */
    private void initNextButton() {
        if (curReceiveDoctor != null && !TextUtils.isEmpty(noticeText)) {
            tvSubmit.setSelected(true);
        }
        else {
            tvSubmit.setSelected(false);
        }
    }

    @OnClick({ R.id.layout_doctor, R.id.tv_submit, R.id.iv_receiving_doctor_call })
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.layout_doctor:
                if (tvSelect.getVisibility() == View.VISIBLE) {
                    intent = new Intent(this, SelectReceivingDoctorActivity.class);
                    intent.putExtra(CommonData.KEY_ORDER_ID, orderNo);
                    intent.putExtra(CommonData.KEY_IS_TRANSFER_OTHER, true);
                    startActivityForResult(intent, REQUEST_CODE_SELECT_DOCTOR);
                }
                break;
            case R.id.iv_receiving_doctor_call:
                curReceiveDoctor = null;
                tvSelect.setVisibility(View.VISIBLE);
                layoutReceivingDoctor.setVisibility(View.GONE);
                initNextButton();
                break;
            case R.id.tv_submit:
                if (tvSubmit.isSelected()) {
                    transferAgainOtherDoctor();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        if (task == Tasks.TRANSFER_AGAIN_OTHER_DOCTOR) {
            ToastUtil.toast(this, response.getMsg());
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    public void onResponseCode(Tasks task, BaseResponse response) {
        super.onResponseCode(task, response);
        if (response.getCode() == BaseNetConfig.REQUEST_ORDER_ERROR) {
            ToastUtil.toast(this, response.getMsg());
            (new Handler()).postDelayed(() -> {
                setResult(RESULT_CANCELED);
                finish();
            }, 2000);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_SELECT_DOCTOR) {
            if (data != null) {
                curReceiveDoctor = (ReceiverDoctorBean)data.getSerializableExtra(CommonData.KEY_DOCTOR_BEAN);
            }
            initReceiveDoctor();
        }
    }
}
