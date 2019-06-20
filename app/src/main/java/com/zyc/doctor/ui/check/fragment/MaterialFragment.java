package com.zyc.doctor.ui.check.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.yht.frame.ui.BaseFragment;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.utils.ToastUtil;
import com.yht.frame.widgets.edittext.SuperEditText;
import com.zyc.doctor.R;
import com.zyc.doctor.ui.AbstractTextWatcher;
import com.zyc.doctor.ui.check.listener.OnCheckListener;
import com.zyc.doctor.utils.text.BankCardTextWatcher;

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
    @BindView(R.id.tv_past_medical_his_clear)
    TextView tvPastMedicalHisClear;
    @BindView(R.id.tv_past_medical_his_not)
    TextView tvPastMedicalHisNot;
    @BindView(R.id.et_past_medical_his)
    EditText etPastMedicalHis;
    @BindView(R.id.tv_past_medical_his_num)
    TextView tvPastMedicalHisNum;
    @BindView(R.id.tv_family_medical_his_clear)
    TextView tvFamilyMedicalHisClear;
    @BindView(R.id.tv_family_medical_his_not)
    TextView tvFamilyMedicalHisNot;
    @BindView(R.id.et_family_medical_his)
    EditText etFamilyMedicalHis;
    @BindView(R.id.tv_family_medical_his_num)
    TextView tvFamilyMedicalHisNum;
    @BindView(R.id.tv_allergies_clear)
    TextView tvAllergiesClear;
    @BindView(R.id.tv_allergies_not)
    TextView tvAllergiesNot;
    @BindView(R.id.et_allergies)
    EditText etAllergies;
    @BindView(R.id.tv_allergies_num)
    TextView tvAllergiesNum;
    @BindView(R.id.tv_diagnosis_clear)
    TextView tvDiagnosisClear;
    @BindView(R.id.et_diagnosis)
    EditText etDiagnosis;
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
     * 基础信息
     */
    private String name, idCard, age, sex, phone;
    private String pastMedicalHis = "", familyMedicalHis = "", allergiesHis = "", diagnosisHis = "";

    @Override
    public int getLayoutID() {
        return R.layout.fragment_material;
    }

    @Override
    public void onResume() {
        super.onResume();
        initNameAndCard();
        initAge();
        initSex();
    }

    public void setValue(String name, String idCard) {
        this.name = name;
        this.idCard = idCard;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        etFamilyMedicalHis.setHorizontallyScrolling(false);
        etFamilyMedicalHis.setMaxLines(Integer.MAX_VALUE);
        etPastMedicalHis.setHorizontallyScrolling(false);
        etPastMedicalHis.setMaxLines(Integer.MAX_VALUE);
        etAllergies.setHorizontallyScrolling(false);
        etAllergies.setMaxLines(Integer.MAX_VALUE);
        initPage();
    }

    @Override
    public void initListener() {
        super.initListener();
    }

    /**
     * 基础页面处理
     */
    private void initPage() {
        initEdit();
        //既往病史
        initPastMedicalHis(false);
        //家族病史
        initFamilyMedicalHis(false);
        //过敏史
        initAllergies(false);
    }

    private void initEdit() {
        etPhone.setOnFocusChangeListener(this);
        etPhone.addTextChangedListener(new AbstractTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                phone = s.toString();
                //判断手机号和诊断史
                if (BaseUtils.isMobileNumber(phone) && !TextUtils.isEmpty(diagnosisHis)) {
                    tvMaterialNext.setSelected(true);
                }
                else {
                    tvMaterialNext.setSelected(false);
                }
            }
        });
        etPastMedicalHis.addTextChangedListener(new AbstractTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                pastMedicalHis = s.toString();
                if (TextUtils.isEmpty(pastMedicalHis)) {
                    tvPastMedicalHisClear.setVisibility(View.GONE);
                }
                else {
                    tvPastMedicalHisClear.setVisibility(View.VISIBLE);
                }
                tvPastMedicalHisNum.setText(String.format(getString(R.string.txt_calc_num), pastMedicalHis.length()));
            }
        });
        etFamilyMedicalHis.addTextChangedListener(new AbstractTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                familyMedicalHis = s.toString();
                if (TextUtils.isEmpty(familyMedicalHis)) {
                    tvFamilyMedicalHisClear.setVisibility(View.GONE);
                }
                else {
                    tvFamilyMedicalHisClear.setVisibility(View.VISIBLE);
                }
                tvFamilyMedicalHisNum.setText(
                        String.format(getString(R.string.txt_calc_num), familyMedicalHis.length()));
            }
        });
        etAllergies.addTextChangedListener(new AbstractTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                allergiesHis = s.toString();
                if (TextUtils.isEmpty(allergiesHis)) {
                    tvAllergiesClear.setVisibility(View.GONE);
                }
                else {
                    tvAllergiesClear.setVisibility(View.VISIBLE);
                }
                tvAllergiesNum.setText(String.format(getString(R.string.txt_calc_num), allergiesHis.length()));
            }
        });
        etDiagnosis.addTextChangedListener(new AbstractTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                diagnosisHis = s.toString();
                //判断手机号和诊断史
                if (BaseUtils.isMobileNumber(phone) && !TextUtils.isEmpty(diagnosisHis)) {
                    tvMaterialNext.setSelected(true);
                }
                else {
                    tvMaterialNext.setSelected(false);
                }
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
            tvPastMedicalHisClear.setVisibility(View.GONE);
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
            tvFamilyMedicalHisClear.setVisibility(View.GONE);
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
            tvAllergiesClear.setVisibility(View.GONE);
            layoutAllergies.setVisibility(View.VISIBLE);
            tvAllergiesNum.setText(
                    String.format(getString(R.string.txt_calc_num), tvAllergiesNot.getText().toString().length()));
        }
    }

    /**
     * 过敏史
     */
    private void initDiagnosis() {
        if (TextUtils.isEmpty(diagnosisHis)) {
            tvDiagnosisClear.setVisibility(View.GONE);
        }
        else {
            tvDiagnosisClear.setVisibility(View.VISIBLE);
        }
        tvDiagnosisNum.setText(String.format(getString(R.string.txt_calc_num), diagnosisHis.length()));
    }

    /**
     * 姓名和身份证处理
     */
    private void initNameAndCard() {
        BankCardTextWatcher.bind(tvIdCard, this);
        if (tvName != null && tvIdCard != null) {
            tvName.setText(name);
            tvIdCard.setText(idCard);
        }
    }

    /**
     * 老用户获取，新用户根据身份证计算
     */
    private void initAge() {
        age = BaseUtils.getAgeByCard(idCard);
        etAge.setText(age);
    }

    /**
     * 老用户获取，新用户根据身份证计算
     */
    private void initSex() {
        sex = BaseUtils.getSexByCard(idCard);
        if (getString(R.string.txt_sex_female).equals(sex)) {
            rbFemale.setChecked(true);
        }
        else {
            rbMale.setChecked(true);
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
            R.id.tv_past_medical_his_clear, R.id.tv_family_medical_his_clear, R.id.tv_allergies_clear,
            R.id.tv_diagnosis_clear, R.id.tv_material_next, R.id.iv_past_medical_his, R.id.iv_family_medical_his,
            R.id.iv_allergies })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_past_medical_his_clear:
                etPastMedicalHis.setText("");
                break;
            case R.id.tv_family_medical_his_clear:
                etFamilyMedicalHis.setText("");
                break;
            case R.id.tv_allergies_clear:
                etAllergies.setText("");
                break;
            case R.id.tv_diagnosis_clear:
                etDiagnosis.setText("");
                break;
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
                if (checkListener != null) {
                    checkListener.onStepTwo();
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
