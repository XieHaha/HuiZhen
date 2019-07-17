package com.yht.yihuantong.ui.reservation.service;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.Tasks;
import com.yht.frame.data.base.PatientDetailBean;
import com.yht.frame.data.base.ReserveCheckBean;
import com.yht.frame.http.retrofit.RequestUtils;
import com.yht.frame.ui.BaseFragment;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.utils.ToastUtil;
import com.yht.frame.widgets.edittext.AbstractTextWatcher;
import com.yht.frame.widgets.edittext.SuperEditText;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ui.check.listener.OnCheckListener;
import com.yht.yihuantong.utils.text.BankCardTextWatcher;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/6/14 14:23
 * @des 身份确认
 */
public class ServiceIdentifyFragment extends BaseFragment implements View.OnFocusChangeListener {
    @BindView(R.id.et_patient_name)
    SuperEditText etPatientName;
    @BindView(R.id.et_patient_id_card)
    SuperEditText etPatientIdCard;
    @BindView(R.id.tv_identify_next)
    TextView tvIdentifyNext;
    private String name, idCard;
    /**
     * 当前患者
     */
    private PatientDetailBean patientDetailBean;
    /**
     * 当前预约检查信息
     */
    private ReserveCheckBean reserveCheckBean;

    @Override
    public int getLayoutID() {
        return R.layout.fragment_identify;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        BankCardTextWatcher.bind(etPatientIdCard, this);
    }

    @Override
    public void initListener() {
        super.initListener();
        etPatientIdCard.setOnFocusChangeListener(this);
        etPatientName.addTextChangedListener(new AbstractTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                name = s.toString();
                if (!TextUtils.isEmpty(idCard) && !TextUtils.isEmpty(s)) {
                    tvIdentifyNext.setSelected(true);
                }
                else {
                    tvIdentifyNext.setSelected(false);
                }
            }
        });
    }

    public void setReserveCheckBean(ReserveCheckBean reserveCheckBean) {
        this.reserveCheckBean = reserveCheckBean;
    }

    /**
     * 患者验证
     */
    private void verifyPatient() {
        RequestUtils.verifyPatient(getContext(), loginBean.getToken(), idCard, this);
    }

    @OnClick(R.id.tv_identify_next)
    public void onViewClicked() {
        if (tvIdentifyNext.isSelected()) {
            //已经校验过  不在校验
            if (reserveCheckBean != null && idCard.equals(reserveCheckBean.getIdCardNo())) {
                if (checkListener != null) {
                    checkListener.onCheckStepOne(reserveCheckBean);
                }
            }
            else {
                verifyPatient();
            }
        }
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if (!hasFocus && etPatientIdCard != null) {
            idCard = etPatientIdCard.getText().toString().replace(" ", "");
            if (!TextUtils.isEmpty(idCard) && !BaseUtils.isCardNum(idCard)) {
                ToastUtil.toast(getContext(), R.string.toast_id_card_error);
            }
        }
    }

    /**
     * 身份证输入框监听
     */
    public void onCardTextChanged(CharSequence s, int start, int before, int count) {
        idCard = s.toString().replace(" ", "");
        if (!TextUtils.isEmpty(name) && BaseUtils.isCardNum(idCard)) {
            tvIdentifyNext.setSelected(true);
        }
        else {
            tvIdentifyNext.setSelected(false);
        }
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        if (task == Tasks.VERIFY_PATIENT) {
            patientDetailBean = (PatientDetailBean)response.getData();
            //新用户
            if (patientDetailBean == null) {
                if (checkListener != null) {
                    reserveCheckBean = new ReserveCheckBean();
                    reserveCheckBean.setPatientName(name);
                    reserveCheckBean.setIdCardNo(idCard);
                    checkListener.onCheckStepOne(reserveCheckBean);
                }
            }
            else {
                if (!name.equals(patientDetailBean.getName())) {
                    ToastUtil.toast(getContext(), R.string.txt_identity_information_error);
                }
                else {
                    if (checkListener != null) {
                        checkData();
                    }
                }
            }
        }
    }

    /**
     * 检查数据
     */
    private void checkData() {
        reserveCheckBean = new ReserveCheckBean();
        reserveCheckBean.setPatientName(name);
        reserveCheckBean.setIdCardNo(idCard);
        reserveCheckBean.setPatientCode(patientDetailBean.getCode());
        reserveCheckBean.setPhone(patientDetailBean.getMobile());
        reserveCheckBean.setAge(patientDetailBean.getAge());
        reserveCheckBean.setSex(patientDetailBean.getSex());
        reserveCheckBean.setConfirmPhoto(patientDetailBean.getWxPhoto());
        reserveCheckBean.setPastHistory(patientDetailBean.getPast());
        reserveCheckBean.setFamilyHistory(patientDetailBean.getFamily());
        reserveCheckBean.setAllergyHistory(patientDetailBean.getAllergy());
        checkListener.onCheckStepOne(reserveCheckBean);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private OnCheckListener checkListener;

    public void setOnCheckListener(OnCheckListener onCheckListener) {
        this.checkListener = onCheckListener;
    }
}
