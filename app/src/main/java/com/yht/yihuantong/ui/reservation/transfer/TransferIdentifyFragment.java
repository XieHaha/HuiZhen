package com.yht.yihuantong.ui.reservation.transfer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.Tasks;
import com.yht.frame.data.base.PatientDetailBean;
import com.yht.frame.data.base.ReserveTransferBean;
import com.yht.frame.http.retrofit.RequestUtils;
import com.yht.frame.ui.BaseFragment;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.utils.ToastUtil;
import com.yht.frame.widgets.edittext.AbstractTextWatcher;
import com.yht.frame.widgets.edittext.SuperEditText;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ui.transfer.listener.OnTransferListener;
import com.yht.yihuantong.utils.text.BankCardTextWatcher;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/6/14 14:23
 * @des 身份确认
 */
public class TransferIdentifyFragment extends BaseFragment implements View.OnFocusChangeListener {
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
     * 当前预约转诊信息
     */
    private ReserveTransferBean reverseTransferBean;

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

    /**
     * 患者验证
     */
    private void verifyPatient() {
        RequestUtils.verifyPatient(getContext(), loginBean.getToken(), idCard, this);
    }

    /**
     * 查询患者是否存在未完成的转诊单
     */
    private void getPatientExistTransfer() {
        RequestUtils.getPatientExistTransfer(getContext(), loginBean.getToken(), patientDetailBean.getCode(), this);
    }

    @OnClick(R.id.tv_identify_next)
    public void onViewClicked() {
        if (tvIdentifyNext.isSelected()) {
            verifyPatient();
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
        switch (task) {
            case VERIFY_PATIENT:
                patientDetailBean = (PatientDetailBean)response.getData();
                //新用户
                if (patientDetailBean == null) {
                    if (onTransferListener != null) {
                        reverseTransferBean = new ReserveTransferBean();
                        reverseTransferBean.setPatientName(name);
                        reverseTransferBean.setPatientIdCardNo(idCard);
                        onTransferListener.onTransferStepOne(reverseTransferBean);
                    }
                }
                else {
                    if (!name.equals(patientDetailBean.getName())) {
                        ToastUtil.toast(getContext(), R.string.txt_identity_information_error);
                    }
                    else {
                        //校验患者是否有存在的待处理转诊单
                        getPatientExistTransfer();
                    }
                }
                break;
            case GET_PATIENT_EXIST_TRANSFER:
                boolean exist = (boolean)response.getData();
                if (exist) {
                    ToastUtil.toast(getContext(), R.string.txt_patient_exist_transfer);
                }
                else {
                    if (onTransferListener != null) {
                        transferData();
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * 转诊数据
     */
    private void transferData() {
        reverseTransferBean = new ReserveTransferBean();
        reverseTransferBean.setPatientName(name);
        reverseTransferBean.setPatientIdCardNo(idCard);
        reverseTransferBean.setPatientCode(patientDetailBean.getCode());
        reverseTransferBean.setPatientMobile(patientDetailBean.getMobile());
        reverseTransferBean.setPatientAge(patientDetailBean.getAge());
        reverseTransferBean.setSex(patientDetailBean.getSex());
        reverseTransferBean.setConfirmPhoto(patientDetailBean.getWxPhoto());
        reverseTransferBean.setPastHistory(patientDetailBean.getPast());
        reverseTransferBean.setFamilyHistory(patientDetailBean.getFamily());
        reverseTransferBean.setAllergyHistory(patientDetailBean.getAllergy());
        onTransferListener.onTransferStepOne(reverseTransferBean);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private OnTransferListener onTransferListener;

    public void setOnTransferListener(OnTransferListener onTransferListener) {
        this.onTransferListener = onTransferListener;
    }
}
