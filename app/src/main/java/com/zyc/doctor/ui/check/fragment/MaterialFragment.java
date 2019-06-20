package com.zyc.doctor.ui.check.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.yht.frame.ui.BaseFragment;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.widgets.edittext.SuperEditText;
import com.zyc.doctor.R;
import com.zyc.doctor.ui.check.listener.OnCheckListener;
import com.zyc.doctor.utils.text.BankCardTextWatcher;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/6/14 14:23
 * @des 完善资料
 */
public class MaterialFragment extends BaseFragment {
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
    @BindView(R.id.tv_identify_next)
    TextView tvIdentifyNext;
    @BindView(R.id.layout_past_medical_his)
    LinearLayout layoutPastMedicalHis;
    @BindView(R.id.layout_family_medical_his)
    LinearLayout layoutFamilyMedicalHis;
    @BindView(R.id.layout_allergies)
    LinearLayout layoutAllergies;
    /**
     * 姓名 身份证
     */
    private String name, idCard, age, sex, phone;
    private String pastMedicalHis = "", familyMedicalHis = "", allergiesHis = "", dianisisHis = "";

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
        //既往病史
        initPastMedicalHis();
        //家族病史
        initFamilyMedicalHis();
        //过敏史
        initAllergies();
    }

    private void initEdit() {
    }

    /**
     * 既往病史
     */
    private void initPastMedicalHis() {
        if (true) {
            etPastMedicalHis.setVisibility(View.VISIBLE);
            tvPastMedicalHisClear.setVisibility(View.VISIBLE);
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
    private void initFamilyMedicalHis() {
        if (true) {
            etFamilyMedicalHis.setVisibility(View.VISIBLE);
            tvFamilyMedicalHisClear.setVisibility(View.VISIBLE);
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
    private void initAllergies() {
        if (true) {
            etAllergies.setVisibility(View.VISIBLE);
            tvAllergiesClear.setVisibility(View.VISIBLE);
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

    @OnClick({
            R.id.tv_past_medical_his_clear, R.id.tv_family_medical_his_clear, R.id.tv_allergies_clear,
            R.id.tv_diagnosis_clear, R.id.tv_identify_next, R.id.iv_past_medical_his, R.id.iv_family_medical_his,
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
                initPastMedicalHis();
                break;
            case R.id.iv_family_medical_his:
                initFamilyMedicalHis();
                break;
            case R.id.iv_allergies:
                initAllergies();
                break;
            case R.id.tv_identify_next:
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
