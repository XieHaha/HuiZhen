package com.yht.yihuantong.ui.reservation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yht.frame.data.CommonData;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.widgets.dialog.HintDialog;
import com.yht.frame.widgets.edittext.AbstractTextWatcher;
import com.yht.frame.widgets.edittext.MultiLineEditText;
import com.yht.yihuantong.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/9/26 15:59
 * @description 既往史
 */
public class PastHistoryActivity extends BaseActivity {
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
    @BindView(R.id.layout_past_medical_his)
    LinearLayout layoutPastMedicalHis;
    @BindView(R.id.layout_family_medical_his)
    LinearLayout layoutFamilyMedicalHis;
    @BindView(R.id.layout_allergies)
    LinearLayout layoutAllergies;
    @BindView(R.id.public_title_bar_back)
    ImageView publicTitleBarBack;
    @BindView(R.id.public_title_bar_more)
    TextView publicTitleBarMore;
    private String pastMedicalHis = "", familyMedicalHis = "", allergiesHis = "";
    private ArrayList<String> pastHistoryData;

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_past_history;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        publicTitleBarMore.setVisibility(View.VISIBLE);
        publicTitleBarMore.setText(R.string.txt_save);
        if (getIntent() != null) {
            pastHistoryData = getIntent().getStringArrayListExtra(CommonData.KEY_PUBLIC);
        }
        pastMedicalHis = pastHistoryData.get(0);
        familyMedicalHis = pastHistoryData.get(1);
        allergiesHis = pastHistoryData.get(2);
        //既往病史
        initPastMedicalHis(PastHistoryUtil.verifyPastMedical(this, pastMedicalHis));
        //家族病史
        initFamilyMedicalHis(PastHistoryUtil.verifyFamilyMedical(this, familyMedicalHis));
        //过敏史
        initAllergies(PastHistoryUtil.verifyAllergies(this, allergiesHis));
    }

    @Override
    public void initListener() {
        super.initListener();
        publicTitleBarBack.setOnClickListener(this);
        etPastMedicalHis.addTextChangedListener(new AbstractTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                pastMedicalHis = s.toString().trim();
                tvPastMedicalHisNum.setText(String.format(getString(R.string.txt_calc_num), pastMedicalHis.length()));
                initNextButton();
            }
        });
        etFamilyMedicalHis.addTextChangedListener(new AbstractTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                familyMedicalHis = s.toString().trim();
                tvFamilyMedicalHisNum.setText(
                        String.format(getString(R.string.txt_calc_num), familyMedicalHis.length()));
                initNextButton();
            }
        });
        etAllergies.addTextChangedListener(new AbstractTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                allergiesHis = s.toString().trim();
                tvAllergiesNum.setText(String.format(getString(R.string.txt_calc_num), allergiesHis.length()));
                initNextButton();
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
            etPastMedicalHis.setSelection(pastMedicalHis.length());
            tvPastMedicalHisNum.setText(String.format(getString(R.string.txt_calc_num), pastMedicalHis.length()));
        }
        else {
            etPastMedicalHis.setText("");
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
            etFamilyMedicalHis.setSelection(familyMedicalHis.length());
            tvFamilyMedicalHisNum.setText(String.format(getString(R.string.txt_calc_num), familyMedicalHis.length()));
        }
        else {
            etFamilyMedicalHis.setText("");
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
            etAllergies.setSelection(allergiesHis.length());
            tvAllergiesNum.setText(String.format(getString(R.string.txt_calc_num), allergiesHis.length()));
        }
        else {
            etAllergies.setText("");
            etAllergies.setVisibility(View.INVISIBLE);
            layoutAllergies.setVisibility(View.VISIBLE);
            tvAllergiesNum.setText(
                    String.format(getString(R.string.txt_calc_num), tvAllergiesNot.getText().toString().length()));
        }
    }

    /**
     * 保存
     */
    private void initNextButton() {
        if (TextUtils.equals(pastMedicalHis, pastHistoryData.get(0)) &&
            TextUtils.equals(familyMedicalHis, pastHistoryData.get(1)) &&
            TextUtils.equals(allergiesHis, pastHistoryData.get(2))) {
            publicTitleBarMore.setSelected(false);
        }
        else {
            publicTitleBarMore.setSelected(true);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.public_title_bar_back) {
            onFinish();
        }
    }

    @OnClick({ R.id.public_title_bar_more, R.id.iv_past_medical_his, R.id.iv_family_medical_his, R.id.iv_allergies })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.public_title_bar_more:
                if (publicTitleBarMore.isSelected()) { save(); }
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
            default:
                break;
        }
    }

    /**
     * 保存
     */
    private void save() {
        hideSoftInputFromWindow();
        pastHistoryData.clear();
        pastHistoryData.add(pastMedicalHis);
        pastHistoryData.add(familyMedicalHis);
        pastHistoryData.add(allergiesHis);
        Intent intent = new Intent();
        intent.putExtra(CommonData.KEY_PUBLIC, pastHistoryData);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void onFinish() {
        if (publicTitleBarMore.isSelected()) {
            new HintDialog(this).setContentString(R.string.txt_save_edit)
                                .setCancleBtnTxt(R.string.txt_not_save)
                                .setOnCancelClickListener(this::finish)
                                .setEnterBtnTxt(R.string.txt_save)
                                .setEnterSelect(true)
                                .setDeleteVisible(View.VISIBLE)
                                .setOnEnterClickListener(this::save)
                                .show();
        }
        else {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        onFinish();
    }
}
