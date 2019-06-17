package com.zyc.doctor.ui.check.fragment;

import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.yht.frame.ui.BaseFragment;
import com.yht.frame.widgets.edittext.SuperEditText;
import com.zyc.doctor.R;
import com.zyc.doctor.ui.check.listener.OnCheckListener;

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
    TextView tvIdCard;
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

    @Override
    public int getLayoutID() {
        return R.layout.fragment_material;
    }

    @OnClick({
            R.id.tv_past_medical_his_clear, R.id.tv_family_medical_his_clear, R.id.tv_allergies_clear,
            R.id.tv_diagnosis_clear, R.id.tv_identify_next })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_past_medical_his_clear:
                break;
            case R.id.tv_family_medical_his_clear:
                break;
            case R.id.tv_allergies_clear:
                break;
            case R.id.tv_diagnosis_clear:
                break;
            case R.id.tv_identify_next:
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
