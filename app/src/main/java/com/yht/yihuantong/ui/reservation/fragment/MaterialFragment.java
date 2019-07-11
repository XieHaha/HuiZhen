package com.yht.yihuantong.ui.reservation.fragment;

import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.yht.frame.data.BaseData;
import com.yht.frame.data.base.ReserveCheckBean;
import com.yht.frame.data.base.ReserveTransferBean;
import com.yht.frame.ui.BaseFragment;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.utils.ToastUtil;
import com.yht.frame.widgets.edittext.AbstractTextWatcher;
import com.yht.frame.widgets.edittext.MultiLineEditText;
import com.yht.frame.widgets.edittext.SuperEditText;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ui.check.listener.OnCheckListener;
import com.yht.yihuantong.utils.text.BankCardTextWatcher;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/6/14 14:23
 * @des 完善资料
 */
public class MaterialFragment extends BaseFragment implements View.OnFocusChangeListener {
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
    @BindView(R.id.tv_past_medical_his_not)
    TextView tvPastMedicalHisNot;
    @BindView(R.id.et_past_medical_his)
    MultiLineEditText etPastMedicalHis;
    @BindView(R.id.tv_past_medical_his_num)
    TextView tvPastMedicalHisNum;
    @BindView(R.id.tv_family_medical_his_not)
    TextView tvFamilyMedicalHisNot;
    @BindView(R.id.et_family_medical_his)
    MultiLineEditText etFamilyMedicalHis;
    @BindView(R.id.tv_family_medical_his_num)
    TextView tvFamilyMedicalHisNum;
    @BindView(R.id.tv_allergies_not)
    TextView tvAllergiesNot;
    @BindView(R.id.et_allergies)
    MultiLineEditText etAllergies;
    @BindView(R.id.tv_allergies_num)
    TextView tvAllergiesNum;
    @BindView(R.id.et_diagnosis)
    MultiLineEditText etDiagnosis;
    @BindView(R.id.tv_diagnosis_num)
    TextView tvDiagnosisNum;
    @BindView(R.id.tv_material_next)
    TextView tvMaterialNext;
    @BindView(R.id.layout_past_medical_his)
    LinearLayout layoutPastMedicalHis;
    @BindView(R.id.layout_family_medical_his)
    LinearLayout layoutFamilyMedicalHis;
    @BindView(R.id.layout_allergies)
    LinearLayout layoutAllergies;
    /**
     * 当前预约转诊数据
     */
    private ReserveTransferBean reverseTransferBean;
    /**
     * 当前预约检查数据
     */
    private ReserveCheckBean reserveCheckBean;
    /**
     * 基础信息
     */
    private String name, idCard, age, phone;
    private int sex;
    private String pastMedicalHis = "", familyMedicalHis = "", allergiesHis = "", diagnosisHis = "";
    private boolean isTransfer;
    /**
     * 二次编辑 是否清空所有已填数据
     */
    private boolean clearAll;

    @Override
    public int getLayoutID() {
        return R.layout.fragment_material;
    }

    @Override
    public void onResume() {
        super.onResume();
        initPatientBaseData();
    }

    public void setTransfer(boolean transfer) {
        this.isTransfer = transfer;
    }

    public void setReverseTransferBean(ReserveTransferBean bean) {
        clearAllTransferData(bean);
        this.reverseTransferBean = bean;
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
        if (isTransfer) {
            initTransferData();
        }
        else {
            initCheckData();
        }
    }

    private void initTransferData() {
        if (reverseTransferBean != null) {
            BankCardTextWatcher.bind(tvIdCard, this);
            if (!TextUtils.isEmpty(reverseTransferBean.getPatientCode())) {
                //老用户
                editStatus(false);
                age = String.valueOf(reverseTransferBean.getPatientAge());
                sex = reverseTransferBean.getSex();
                phone = reverseTransferBean.getPatientMobile();
                etPhone.setText(phone);
                pastMedicalHis = reverseTransferBean.getPastHistory();
                familyMedicalHis = reverseTransferBean.getFamilyHistory();
                allergiesHis = reverseTransferBean.getAllergyHistory();
            }
            else {
                //新用户
                editStatus(true);
                age = BaseUtils.getAgeByCard(reverseTransferBean.getPatientIdCardNo());
                sex = BaseUtils.getSexByCard(reverseTransferBean.getPatientIdCardNo());
            }
            tvName.setText(reverseTransferBean.getPatientName());
            tvIdCard.setText(reverseTransferBean.getPatientIdCardNo());
            etAge.setText(age);
            if (sex == BaseData.BASE_ONE) {
                rbMale.setChecked(true);
            }
            else {
                rbFemale.setChecked(true);
            }
            //既往病史
            initPastMedicalHis(!TextUtils.isEmpty(pastMedicalHis) &&
                               (!getString(R.string.txt_past_medical_his_not).equals(pastMedicalHis)));
            //家族病史
            initFamilyMedicalHis(!TextUtils.isEmpty(familyMedicalHis) &&
                                 (!getString(R.string.txt_family_medical_his_not).equals(familyMedicalHis)));
            //过敏史
            initAllergies(
                    !TextUtils.isEmpty(allergiesHis) && (!getString(R.string.txt_allergies_not).equals(allergiesHis)));
        }
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
                pastMedicalHis = reserveCheckBean.getPastHistory();
                familyMedicalHis = reserveCheckBean.getFamilyHistory();
                allergiesHis = reserveCheckBean.getAllergyHistory();
            }
            else {
                //新用户
                editStatus(true);
                age = BaseUtils.getAgeByCard(reserveCheckBean.getIdCardNo());
                sex = BaseUtils.getSexByCard(reserveCheckBean.getIdCardNo());
            }
            tvName.setText(reserveCheckBean.getPatientName());
            tvIdCard.setText(reserveCheckBean.getIdCardNo());
            etAge.setText(age);
            if (sex == BaseData.BASE_ONE) {
                rbMale.setChecked(true);
            }
            else {
                rbFemale.setChecked(true);
            }
            //既往病史
            initPastMedicalHis(!TextUtils.isEmpty(pastMedicalHis) &&
                               (!getString(R.string.txt_past_medical_his_not).equals(pastMedicalHis)));
            //家族病史
            initFamilyMedicalHis(!TextUtils.isEmpty(familyMedicalHis) &&
                                 (!getString(R.string.txt_family_medical_his_not).equals(familyMedicalHis)));
            //过敏史
            initAllergies(
                    !TextUtils.isEmpty(allergiesHis) && (!getString(R.string.txt_allergies_not).equals(allergiesHis)));
        }
    }

    /**
     * 涉及到数据回填逻辑，如果更改了患者，需要清空原有已填写数据
     */
    private void clearAllTransferData(ReserveTransferBean bean) {
        if (reverseTransferBean == null || bean == null) {
            clearAll = false;
        }
        else {
            if (reverseTransferBean.getPatientName().equals(bean.getPatientName()) &&
                reverseTransferBean.getPatientIdCardNo().equals(bean.getPatientIdCardNo())) {
                //都相等 说明未改变用户
                clearAll = false;
            }
            else {
                //有不相等的 说明患者已经更改，需要清除原有已填写数据
                clearAll = true;
            }
        }
    }

    /**
     * 涉及到数据回填逻辑，如果更改了患者，需要清空原有已填写数据
     */
    private void clearAllCheckData(ReserveCheckBean bean) {
        if (reserveCheckBean == null || bean == null) {
            clearAll = false;
        }
        else {
            if (reserveCheckBean.getPatientName().equals(bean.getPatientName()) &&
                reserveCheckBean.getIdCardNo().equals(bean.getIdCardNo())) {
                //都相等 说明未改变用户
                clearAll = false;
            }
            else {
                //有不相等的 说明患者已经更改，需要清除原有已填写数据
                clearAll = true;
            }
        }
    }

    /**
     * @param mode 是否可以编辑基本信息
     */
    private void editStatus(boolean mode) {
        etPhone.setFocusable(mode);
        etPhone.setFocusableInTouchMode(mode);
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
                age = s.toString();
                initNextButton();
            }
        });
        etPhone.setOnFocusChangeListener(this);
        etPhone.addTextChangedListener(new AbstractTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                phone = s.toString();
                //判断手机号和诊断史
                initNextButton();
            }
        });
        etPastMedicalHis.addTextChangedListener(new AbstractTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                pastMedicalHis = s.toString();
                tvPastMedicalHisNum.setText(String.format(getString(R.string.txt_calc_num), pastMedicalHis.length()));
                initNextButton();
            }
        });
        etFamilyMedicalHis.addTextChangedListener(new AbstractTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                familyMedicalHis = s.toString();
                tvFamilyMedicalHisNum.setText(
                        String.format(getString(R.string.txt_calc_num), familyMedicalHis.length()));
                initNextButton();
            }
        });
        etAllergies.addTextChangedListener(new AbstractTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                allergiesHis = s.toString();
                tvAllergiesNum.setText(String.format(getString(R.string.txt_calc_num), allergiesHis.length()));
                initNextButton();
            }
        });
        etDiagnosis.addTextChangedListener(new AbstractTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                diagnosisHis = s.toString();
                initNextButton();
                initDiagnosis();
            }
        });
    }

    /**
     * 既往病史
     */
    private void initPastMedicalHis(boolean status) {
        if (status) {
            etPastMedicalHis.setVisibility(View.VISIBLE);
            layoutPastMedicalHis.setVisibility(View.GONE);
            etPastMedicalHis.setText(pastMedicalHis);
            tvPastMedicalHisNum.setText(String.format(getString(R.string.txt_calc_num), pastMedicalHis.length()));
        }
        else {
            etPastMedicalHis.setVisibility(View.INVISIBLE);
            layoutPastMedicalHis.setVisibility(View.VISIBLE);
            tvPastMedicalHisNum.setText(
                    String.format(getString(R.string.txt_calc_num), tvPastMedicalHisNot.getText().toString().length()));
        }
    }

    /**
     * 家族病史
     */
    private void initFamilyMedicalHis(boolean status) {
        if (status) {
            etFamilyMedicalHis.setVisibility(View.VISIBLE);
            layoutFamilyMedicalHis.setVisibility(View.GONE);
            etFamilyMedicalHis.setText(familyMedicalHis);
            tvFamilyMedicalHisNum.setText(String.format(getString(R.string.txt_calc_num), familyMedicalHis.length()));
        }
        else {
            etFamilyMedicalHis.setVisibility(View.INVISIBLE);
            layoutFamilyMedicalHis.setVisibility(View.VISIBLE);
            tvFamilyMedicalHisNum.setText(String.format(getString(R.string.txt_calc_num),
                                                        tvFamilyMedicalHisNot.getText().toString().length()));
        }
    }

    /**
     * 过敏史
     */
    private void initAllergies(boolean status) {
        if (status) {
            etAllergies.setVisibility(View.VISIBLE);
            layoutAllergies.setVisibility(View.GONE);
            etAllergies.setText(allergiesHis);
            tvAllergiesNum.setText(String.format(getString(R.string.txt_calc_num), allergiesHis.length()));
        }
        else {
            etAllergies.setVisibility(View.INVISIBLE);
            layoutAllergies.setVisibility(View.VISIBLE);
            tvAllergiesNum.setText(
                    String.format(getString(R.string.txt_calc_num), tvAllergiesNot.getText().toString().length()));
        }
    }

    /**
     * 诊断内容
     */
    private void initDiagnosis() {
        tvDiagnosisNum.setText(String.format(getString(R.string.txt_calc_num), diagnosisHis.length()));
    }

    /**
     * next
     */
    private void initNextButton() {
        boolean past = etPastMedicalHis.getVisibility() != View.VISIBLE || !TextUtils.isEmpty(pastMedicalHis);
        boolean family = etFamilyMedicalHis.getVisibility() != View.VISIBLE || !TextUtils.isEmpty(familyMedicalHis);
        boolean allergies = etAllergies.getVisibility() != View.VISIBLE || !TextUtils.isEmpty(allergiesHis);
        //判断手机号和诊断史
        if (BaseUtils.isMobileNumber(phone) && !TextUtils.isEmpty(diagnosisHis) && !TextUtils.isEmpty(age) && past &&
            family && allergies) {
            tvMaterialNext.setSelected(true);
        }
        else {
            tvMaterialNext.setSelected(false);
        }
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
            R.id.tv_material_next, R.id.iv_past_medical_his, R.id.iv_family_medical_his, R.id.iv_allergies })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_past_medical_his:
                initPastMedicalHis(true);
                break;
            case R.id.iv_family_medical_his:
                initFamilyMedicalHis(true);
                break;
            case R.id.iv_allergies:
                initAllergies(true);
                break;
            case R.id.tv_material_next:
                if (tvMaterialNext.isSelected() && checkListener != null) {
                    if (isTransfer) {
                        //数据回填
                        reverseTransferBean.setSex(sex);
                        reverseTransferBean.setPatientAge(Integer.valueOf(age));
                        reverseTransferBean.setPatientMobile(phone);
                        if (TextUtils.isEmpty(pastMedicalHis)) {
                            reverseTransferBean.setPastHistory(getString(R.string.txt_past_medical_his_not));
                        }
                        else {
                            reverseTransferBean.setPastHistory(pastMedicalHis);
                        }
                        if (TextUtils.isEmpty(familyMedicalHis)) {
                            reverseTransferBean.setFamilyHistory(getString(R.string.txt_family_medical_his_not));
                        }
                        else {
                            reverseTransferBean.setFamilyHistory(familyMedicalHis);
                        }
                        if (TextUtils.isEmpty(allergiesHis)) {
                            reverseTransferBean.setAllergyHistory(getString(R.string.txt_allergies_not));
                        }
                        else {
                            reverseTransferBean.setAllergyHistory(allergiesHis);
                        }
                        reverseTransferBean.setInitResult(diagnosisHis);
                        checkListener.onTransferStepTwo(reverseTransferBean);
                    }
                    else {
                        //数据回填
                        reserveCheckBean.setSex(sex);
                        reserveCheckBean.setAge(Integer.valueOf(age));
                        reserveCheckBean.setPhone(phone);
                        if (TextUtils.isEmpty(pastMedicalHis)) {
                            reserveCheckBean.setPastHistory(getString(R.string.txt_past_medical_his_not));
                        }
                        else {
                            reserveCheckBean.setPastHistory(pastMedicalHis);
                        }
                        if (TextUtils.isEmpty(familyMedicalHis)) {
                            reserveCheckBean.setFamilyHistory(getString(R.string.txt_family_medical_his_not));
                        }
                        else {
                            reserveCheckBean.setFamilyHistory(familyMedicalHis);
                        }
                        if (TextUtils.isEmpty(allergiesHis)) {
                            reserveCheckBean.setAllergyHistory(getString(R.string.txt_allergies_not));
                        }
                        else {
                            reserveCheckBean.setAllergyHistory(allergiesHis);
                        }
                        reserveCheckBean.setInitResult(diagnosisHis);
                        checkListener.onCheckStepTwo(reserveCheckBean);
                    }
                }
                break;
            default:
                break;
        }
    }

    private OnCheckListener checkListener;

    public void setOnCheckListener(OnCheckListener onCheckListener) {
        this.checkListener = onCheckListener;
    }
}
