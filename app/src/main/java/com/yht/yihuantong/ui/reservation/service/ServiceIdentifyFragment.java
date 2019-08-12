package com.yht.yihuantong.ui.reservation.service;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Selection;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.yht.frame.data.BaseData;
import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.Tasks;
import com.yht.frame.data.bean.PatientDetailBean;
import com.yht.frame.data.bean.ReserveCheckBean;
import com.yht.frame.http.retrofit.RequestUtils;
import com.yht.frame.ui.BaseFragment;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.utils.ToastUtil;
import com.yht.frame.widgets.edittext.AbstractTextWatcher;
import com.yht.frame.widgets.edittext.SuperEditText;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ui.check.listener.OnCheckListener;
import com.yht.yihuantong.utils.text.BankCardTextWatcher;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        etPatientName.setFilters(new InputFilter[] { emojiFilter });
        etPatientName.addTextChangedListener(new AbstractTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                name = s.toString().trim().trim();
                if (!TextUtils.isEmpty(idCard) && !TextUtils.isEmpty(s)) {
                    tvIdentifyNext.setSelected(true);
                }
                else {
                    tvIdentifyNext.setSelected(false);
                }
                int mTextMaxlenght = 0;
                Editable editable = etPatientName.getText();
                //得到最初字段的长度大小,用于光标位置的判断
                int selEndIndex = Selection.getSelectionEnd(editable);
                // 取出每个字符进行判断,如果是字母数字和标点符号则为一个字符加1,
                //如果是汉字则为两个字符
                for (int i = 0; i < name.length(); i++) {
                    char charAt = name.charAt(i);
                    //32-122包含了空格,大小写字母,数字和一些常用的符号,
                    //如果在这个范围内则算一个字符,
                    //如果不在这个范围比如是汉字的话就是两个字符
                    if (charAt >= 32 && charAt <= 122) {
                        mTextMaxlenght++;
                    }
                    else {
                        mTextMaxlenght += 2;
                    }
                    // 当最大字符大于10时,进行字段的截取,并进行提示字段的大小
                    if (mTextMaxlenght > BaseData.BASE_NICK_NAME_LENGTH) {
                        // 截取最大的字段
                        String newStr = name.substring(0, i);
                        etPatientName.setText(newStr);
                        // 得到新字段的长度值
                        editable = etPatientName.getText();
                        int newLen = editable.length();
                        if (selEndIndex > newLen) {
                            selEndIndex = editable.length();
                        }
                        // 设置新光标所在的位置
                        Selection.setSelection(editable, selEndIndex);
                    }
                }
                initNextButton();
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
            initNextButton();
        }
    }

    /**
     * 身份证输入框监听
     */
    public void onCardTextChanged(CharSequence s, int start, int before, int count) {
        idCard = s.toString().replace(" ", "");
        initNextButton();
    }

    private void initNextButton() {
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
        reserveCheckBean.setIsBind(patientDetailBean.getIsBind());
        reserveCheckBean.setPatientCode(patientDetailBean.getCode());
        reserveCheckBean.setPhone(patientDetailBean.getMobile());
        reserveCheckBean.setAge(patientDetailBean.getAge());
        reserveCheckBean.setSex(patientDetailBean.getSex());
        reserveCheckBean.setConfirmPhoto(patientDetailBean.getPhoto());
        reserveCheckBean.setPastHistory(patientDetailBean.getPast());
        reserveCheckBean.setFamilyHistory(patientDetailBean.getFamily());
        reserveCheckBean.setAllergyHistory(patientDetailBean.getAllergy());
        checkListener.onCheckStepOne(reserveCheckBean);
    }

    InputFilter emojiFilter = new InputFilter() {
        private String filterImoji = "[^a-zA-Z ·.\u4E00-\u9FA5]";
        Pattern emoji = Pattern.compile(filterImoji, Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            Matcher emojiMatcher = emoji.matcher(source);
            if (emojiMatcher.find()) {
                ToastUtil.toast(getContext(), "不支持输入");
                return "";
            }
            return null;
        }
    };
    private OnCheckListener checkListener;

    public void setOnCheckListener(OnCheckListener onCheckListener) {
        this.checkListener = onCheckListener;
    }
}
