package com.yht.yihuantong.ui.reservation.service;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yht.frame.data.BaseData;
import com.yht.frame.data.CommonData;
import com.yht.frame.data.bean.ReserveCheckBean;
import com.yht.frame.ui.BaseFragment;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.utils.ToastUtil;
import com.yht.frame.widgets.edittext.AbstractTextWatcher;
import com.yht.frame.widgets.edittext.MultiLineEditText;
import com.yht.frame.widgets.edittext.SuperEditText;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ui.check.listener.OnCheckListener;
import com.yht.yihuantong.ui.reservation.PastHistoryActivity;
import com.yht.yihuantong.ui.reservation.PastHistoryUtil;
import com.yht.yihuantong.utils.text.BankCardTextWatcher;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/6/14 14:23
 * @description 完善资料
 */
public class ServiceMaterialFragment extends BaseFragment implements View.OnFocusChangeListener {
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.rb_male)
    RadioButton rbMale;
    @BindView(R.id.rb_female)
    RadioButton rbFemale;
    @BindView(R.id.et_age)
    SuperEditText etAge;
    @BindView(R.id.tv_id_card)
    SuperEditText tvIdCard;
    @BindView(R.id.et_phone)
    SuperEditText etPhone;
    @BindView(R.id.et_diagnosis)
    MultiLineEditText etDiagnosis;
    @BindView(R.id.tv_diagnosis_num)
    TextView tvDiagnosisNum;
    @BindView(R.id.layout_past_medical_his)
    LinearLayout layoutPastMedicalHis;
    @BindView(R.id.layout_family_medical_his)
    LinearLayout layoutFamilyMedicalHis;
    @BindView(R.id.layout_allergies)
    LinearLayout layoutAllergies;
    @BindView(R.id.tv_past_medical)
    TextView tvPastMedical;
    @BindView(R.id.tv_family_medical)
    TextView tvFamilyMedical;
    @BindView(R.id.tv_allergies)
    TextView tvAllergies;
    @BindView(R.id.layout_past)
    RelativeLayout layoutPast;
    /**
     * 当前预约检查数据
     */
    private ReserveCheckBean reserveCheckBean;
    /**
     * 基础信息
     */
    private int sex;
    private String age, phone, pastMedicalHis = "", familyMedicalHis = "", allergiesHis = "",
            diagnosisHis = "";
    /**
     * 既往史数据
     */
    private ArrayList<String> pastHistoryData;
    /**
     * 二次编辑 是否清空所有已填数据
     */
    private boolean clearAll;
    /**
     * 编辑既往史
     */
    public static final int REQUEST_CODE_PAST_HISTORY = 100;

    @Override
    public int getLayoutID() {
        return R.layout.fragment_material;
    }

    @Override
    public void onResume() {
        super.onResume();
        initPatientBaseData();
    }

    public ReserveCheckBean getReserveCheckBean() {
        return reserveCheckBean;
    }

    public void setReserveCheckBean(ReserveCheckBean bean) {
        clearAllCheckData(bean);
        this.reserveCheckBean = bean;
    }

    @Override
    public void initListener() {
        super.initListener();
        initEditListener();
    }

    /**
     * 姓名和身份证处理  老用户获取，新用户根据身份证计算
     */
    private void initPatientBaseData() {
        if (clearAll) {
            etPhone.setText("");
            etDiagnosis.setText("");
            pastMedicalHis = "";
            familyMedicalHis = "";
            allergiesHis = "";
        }
        initCheckData();
    }

    private void initCheckData() {
        if (reserveCheckBean != null) {
            BankCardTextWatcher.bind(tvIdCard, this);
            if (!TextUtils.isEmpty(reserveCheckBean.getPatientCode())) {
                //老用户
                editStatus(false);
                age = String.valueOf(reserveCheckBean.getAge());
                sex = reserveCheckBean.getSex();
                phone = reserveCheckBean.getPhone();
                etPhone.setText(phone);
            } else {
                //新用户
                editStatus(true);
                age = BaseUtils.getAgeByCard(reserveCheckBean.getIdCardNo());
                sex = BaseUtils.getSexByCard(reserveCheckBean.getIdCardNo());
                reserveCheckBean.setSex(sex);
                if (!TextUtils.isEmpty(age)) {
                    reserveCheckBean.setAge(Integer.valueOf(age));
                } else {
                    reserveCheckBean.setAge(0);
                }
            }
            pastMedicalHis = reserveCheckBean.getPastHistory();
            familyMedicalHis = reserveCheckBean.getFamilyHistory();
            allergiesHis = reserveCheckBean.getAllergyHistory();
            tvName.setText(reserveCheckBean.getPatientName());
            tvIdCard.setText(reserveCheckBean.getIdCardNo());
            etAge.setText(age);
            if (sex == BaseData.BASE_ONE) {
                rbMale.setChecked(true);
            } else {
                rbFemale.setChecked(true);
            }
            //初步诊断
            etDiagnosis.setText(diagnosisHis = reserveCheckBean.getInitResult());
            //既往史
            pastHistoryData = new ArrayList<>();
            pastHistoryData.add(pastMedicalHis);
            pastHistoryData.add(familyMedicalHis);
            pastHistoryData.add(allergiesHis);
            initPastHistory();
            //诊断史
            initDiagnosis();
        }
    }

    /**
     * 涉及到数据回填逻辑，如果更改了居民，需要清空原有已填写数据
     */
    private void clearAllCheckData(ReserveCheckBean bean) {
        if (reserveCheckBean == null || bean == null) {
            clearAll = false;
        } else {
            clearAll = !reserveCheckBean.getPatientName().equals(bean.getPatientName()) ||
                    !reserveCheckBean.getIdCardNo().equals(bean.getIdCardNo());
        }
    }

    /**
     * @param mode 是否可以编辑基本信息
     */
    private void editStatus(boolean mode) {
        //老用户未绑定手机号可以修改
        if (!mode && !BaseData.BASE_STRING_ONE_TAG.equals(reserveCheckBean.getIsBind())) {
            etPhone.setFocusable(true);
            etPhone.setFocusableInTouchMode(true);
        } else {
            etPhone.setFocusable(mode);
            etPhone.setFocusableInTouchMode(mode);
        }
        etAge.setFocusable(mode);
        etAge.setFocusableInTouchMode(mode);
        rbFemale.setClickable(mode);
        rbMale.setClickable(mode);
    }

    private void initEditListener() {
        etAge.addTextChangedListener(new AbstractTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                age = s.toString().trim();
                if (!TextUtils.isEmpty(age)) {
                    reserveCheckBean.setAge(Integer.valueOf(age));
                } else {
                    reserveCheckBean.setAge(0);
                }
            }
        });
        etPhone.setOnFocusChangeListener(this);
        etPhone.addTextChangedListener(new AbstractTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                phone = s.toString().trim();
                if (BaseUtils.isMobileNumber(phone)) {
                    reserveCheckBean.setPhone(phone);
                }
            }
        });
        etDiagnosis.addTextChangedListener(new AbstractTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                diagnosisHis = s.toString().trim();
                initDiagnosis();
                reserveCheckBean.setInitResult(diagnosisHis);
            }
        });
    }

    /**
     * 既往史
     */
    private void initPastHistory() {
        tvPastMedical.setText(PastHistoryUtil.getPastMedical(getContext(), pastHistoryData.get(0)));
        tvFamilyMedical.setText(PastHistoryUtil.getFamilyMedical(getContext(),
                pastHistoryData.get(1)));
        tvAllergies.setText(PastHistoryUtil.getAllergies(getContext(), pastHistoryData.get(2)));
    }

    /**
     * 诊断内容
     */
    private void initDiagnosis() {
        tvDiagnosisNum.setText(String.format(getString(R.string.txt_calc_num),
                diagnosisHis.length()));
        etDiagnosis.setSelection(diagnosisHis.length());
    }

    /**
     * next
     */
    private boolean initNextButton() {
        if (TextUtils.isEmpty(age)) {
            ToastUtil.toast(getContext(), R.string.toast_input_age);
            return false;
        }
        if (!BaseUtils.isMobileNumber(phone)) {
            ToastUtil.toast(getContext(), R.string.toast_input_phone);
            return false;
        }
        if (TextUtils.isEmpty(diagnosisHis)) {
            ToastUtil.toast(getContext(), R.string.toast_input_diagnosisHis_description);
            return false;
        }
        return true;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus && etPhone != null) {
            if (!BaseUtils.isMobileNumber(phone)) {
                ToastUtil.toast(getContext(), R.string.toast_phone_error);
            }
        }
    }

    @OnClick({
            R.id.tv_material_next, R.id.layout_past})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_material_next:
                if (initNextButton() && checkListener != null) {
                    reserveCheckBean.setPastHistory(PastHistoryUtil.getPastMedical(getContext(),
                            pastHistoryData.get(0)));
                    reserveCheckBean.setFamilyHistory(PastHistoryUtil.getFamilyMedical(getContext(),
                            pastHistoryData.get(1)));
                    reserveCheckBean.setAllergyHistory(PastHistoryUtil.getAllergies(getContext(),
                            pastHistoryData.get(2)));
                    checkListener.onCheckStepTwo(reserveCheckBean);
                }
                break;
            case R.id.layout_past:
                Intent intent = new Intent(getContext(), PastHistoryActivity.class);
                intent.putExtra(CommonData.KEY_PUBLIC, pastHistoryData);
                startActivityForResult(intent, REQUEST_CODE_PAST_HISTORY);
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_PAST_HISTORY) {
            pastHistoryData = data.getStringArrayListExtra(CommonData.KEY_PUBLIC);
            reserveCheckBean.setPastHistory(PastHistoryUtil.getPastMedical(getContext(),
                    pastHistoryData.get(0)));
            reserveCheckBean.setFamilyHistory(PastHistoryUtil.getFamilyMedical(getContext(),
                    pastHistoryData.get(1)));
            reserveCheckBean.setAllergyHistory(PastHistoryUtil.getAllergies(getContext(),
                    pastHistoryData.get(2)));
        }
    }

    private OnCheckListener checkListener;

    public void setOnCheckListener(OnCheckListener onCheckListener) {
        this.checkListener = onCheckListener;
    }
}
