package com.yht.yihuantong.ui.reservation.transfer;

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
import com.yht.frame.data.bean.ReserveTransferBean;
import com.yht.frame.ui.BaseFragment;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.utils.ToastUtil;
import com.yht.frame.widgets.edittext.AbstractTextWatcher;
import com.yht.frame.widgets.edittext.MultiLineEditText;
import com.yht.frame.widgets.edittext.SuperEditText;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ui.reservation.PastHistoryActivity;
import com.yht.yihuantong.ui.transfer.listener.OnTransferListener;
import com.yht.yihuantong.utils.text.BankCardTextWatcher;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/6/14 14:23
 * @description 完善资料
 */
public class TransferMaterialFragment extends BaseFragment implements View.OnFocusChangeListener {
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
    @BindView(R.id.tv_material_next)
    TextView tvMaterialNext;
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
     * 当前预约转诊数据
     */
    private ReserveTransferBean reverseTransferBean;
    /**
     * 基础信息
     */
    private String name, idCard, age, phone;
    private int sex;
    private String pastMedicalHis = "", familyMedicalHis = "", allergiesHis = "", diagnosisHis = "";
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

    public void setReverseTransferBean(ReserveTransferBean bean) {
        clearAllTransferData(bean);
        this.reverseTransferBean = bean;
    }

    public ReserveTransferBean getReverseTransferBean() {
        return reverseTransferBean;
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
        initTransferData();
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
                if (!TextUtils.isEmpty(age)) {
                    reverseTransferBean.setPatientAge(Integer.valueOf(age));
                }
                else {
                    reverseTransferBean.setPatientAge(0);
                }
                reverseTransferBean.setSex(sex);
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
            //初步诊断
            etDiagnosis.setText(diagnosisHis = reverseTransferBean.getInitResult());
            //既往史
            pastHistoryData = new ArrayList<>();
            pastHistoryData.add(pastMedicalHis);
            pastHistoryData.add(familyMedicalHis);
            pastHistoryData.add(allergiesHis);
            initPastHistory();
            //诊断史
            initDiagnosis();
            initNextButton();
        }
    }

    /**
     * 涉及到数据回填逻辑，如果更改了居民，需要清空原有已填写数据
     */
    private void clearAllTransferData(ReserveTransferBean bean) {
        if (reverseTransferBean == null || bean == null) {
            clearAll = false;
        }
        else {
            clearAll = !reverseTransferBean.getPatientName().equals(bean.getPatientName()) ||
                       !reverseTransferBean.getPatientIdCardNo().equals(bean.getPatientIdCardNo());
        }
    }

    /**
     * @param mode 是否可以编辑基本信息
     */
    private void editStatus(boolean mode) {
        //老用户未绑定手机号可以修改
        if (!mode && !BaseData.BASE_STRING_ONE_TAG.equals(reverseTransferBean.getIsBind())) {
            etPhone.setFocusable(true);
            etPhone.setFocusableInTouchMode(true);
        }
        else {
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
                initNextButton();
                if (reverseTransferBean != null) {
                    if (!TextUtils.isEmpty(age)) {
                        reverseTransferBean.setPatientAge(Integer.valueOf(age));
                    }
                    else {
                        reverseTransferBean.setPatientAge(0);
                    }
                }
            }
        });
        etPhone.setOnFocusChangeListener(this);
        etPhone.addTextChangedListener(new AbstractTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                phone = s.toString().trim();
                if (BaseUtils.isMobileNumber(phone) && reverseTransferBean != null) {
                    reverseTransferBean.setPatientMobile(phone);
                }
                //判断手机号和诊断史
                initNextButton();
            }
        });
        etDiagnosis.addTextChangedListener(new AbstractTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                diagnosisHis = s.toString().trim();
                initNextButton();
                initDiagnosis();
                if (reverseTransferBean != null) { reverseTransferBean.setInitResult(diagnosisHis); }
            }
        });
    }

    /**
     * 既往史
     */
    private void initPastHistory() {
        if (!TextUtils.isEmpty(pastHistoryData.get(0)) &&
            (!getString(R.string.txt_past_medical_his_not).equals(pastHistoryData.get(0)))) {
            tvPastMedical.setText(pastHistoryData.get(0));
        }
        else {
            tvPastMedical.setText(R.string.txt_past_medical_his_not);
        }
        if (!TextUtils.isEmpty(pastHistoryData.get(1)) &&
            (!getString(R.string.txt_past_medical_his_not).equals(pastHistoryData.get(1)))) {
            tvFamilyMedical.setText(pastHistoryData.get(1));
        }
        else {
            tvFamilyMedical.setText(R.string.txt_family_medical_his_not);
        }
        if (!TextUtils.isEmpty(pastHistoryData.get(2)) &&
            (!getString(R.string.txt_past_medical_his_not).equals(pastHistoryData.get(2)))) {
            tvAllergies.setText(pastHistoryData.get(2));
        }
        else {
            tvAllergies.setText(R.string.txt_allergies_not);
        }
    }

    /**
     * 诊断内容
     */
    private void initDiagnosis() {
        tvDiagnosisNum.setText(String.format(getString(R.string.txt_calc_num), diagnosisHis.length()));
        etDiagnosis.setSelection(diagnosisHis.length());
    }

    /**
     * next
     */
    private void initNextButton() {
        //判断手机号和诊断史
        if (BaseUtils.isMobileNumber(phone) && !TextUtils.isEmpty(diagnosisHis) && !TextUtils.isEmpty(age)) {
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
            R.id.tv_material_next, R.id.layout_past })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_material_next:
                if (tvMaterialNext.isSelected() && onTransferListener != null) {
                    onTransferListener.onTransferStepTwo(reverseTransferBean);
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
            reverseTransferBean.setPastHistory(pastHistoryData.get(0));
            reverseTransferBean.setFamilyHistory(pastHistoryData.get(1));
            reverseTransferBean.setAllergyHistory(pastHistoryData.get(2));
            initNextButton();
        }
    }

    private OnTransferListener onTransferListener;

    public void setOnTransferListener(OnTransferListener onTransferListener) {
        this.onTransferListener = onTransferListener;
    }
}
