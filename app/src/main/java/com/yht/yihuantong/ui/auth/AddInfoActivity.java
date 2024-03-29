package com.yht.yihuantong.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.CommonData;
import com.yht.frame.data.Tasks;
import com.yht.frame.data.bean.HospitalBean;
import com.yht.frame.http.retrofit.RequestUtils;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.utils.ScreenUtils;
import com.yht.frame.utils.ToastUtil;
import com.yht.frame.widgets.dialog.HintDialog;
import com.yht.frame.widgets.edittext.AbstractTextWatcher;
import com.yht.frame.widgets.edittext.EditTextLayout;
import com.yht.frame.widgets.view.ViewPrepared;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ZycApplication;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/6/5 17:19
 * @description 添加医院 or 添加个人简介
 */
public class AddInfoActivity extends BaseActivity {
    @BindView(R.id.edit_layout)
    EditTextLayout etHospital;
    @BindView(R.id.public_title_bar_more_a1)
    TextView publicTitleBarMore;
    @BindView(R.id.public_title_bar_title)
    TextView publicTitleBarTitle;
    @BindView(R.id.public_title_bar_back)
    ImageView publicTitleBarBack;
    @BindView(R.id.tv_calc_num)
    TextView tvCalcNum;
    @BindView(R.id.layout_input)
    RelativeLayout layoutInput;
    @BindView(R.id.layout_root)
    RelativeLayout layoutRoot;
    private String inputValue;
    /**
     * true 添加个人简介
     */
    private boolean mode;

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_add_info;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        if (getIntent() != null) {
            mode = getIntent().getBooleanExtra(CommonData.KEY_INTENT_BOOLEAN, false);
            inputValue = getIntent().getStringExtra(CommonData.KEY_PUBLIC_STRING);
        }
        publicTitleBarMore.setVisibility(View.VISIBLE);
        publicTitleBarBack.setOnClickListener(this);
        new ViewPrepared().asyncPrepare(etHospital, (w, h) -> {
            if (mode) {
                ScreenUtils.controlKeyboardLayout(AddInfoActivity.this, layoutRoot, layoutInput);
            }
            showSoftInputFromWindow(etHospital);
        });


    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        if (mode) {
            publicTitleBarTitle.setText(R.string.txt_introduction);
            publicTitleBarMore.setText(R.string.txt_save);
            etHospital.getEditText().setHint(R.string.txt_introduction_hint);
            //最大输入长度
            etHospital.getEditText().setFilters(new InputFilter[] { emojiFilter, new InputFilter.LengthFilter(500) });
            tvCalcNum.setVisibility(View.VISIBLE);
        }
        else {
            publicTitleBarMore.setText(R.string.txt_add);
            publicTitleBarMore.setSelected(true);
            publicTitleBarTitle.setText(R.string.title_add_hospital);
            etHospital.getEditText().setHint(R.string.txt_search_hospital);
            //最大输入长度
            etHospital.getEditText().setFilters(new InputFilter[] { emojiFilter, new InputFilter.LengthFilter(20) });
            tvCalcNum.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(inputValue)) {
            etHospital.getEditText().setText(inputValue);
            etHospital.getEditText().setSelection(etHospital.getEditText().getText().toString().length());
            if (mode) {
                tvCalcNum.setText(String.format(getString(R.string.txt_calc_num), inputValue.length()));
            }
        }
    }

    @Override
    public void initListener() {
        super.initListener();
        etHospital.getEditText().addTextChangedListener(new AbstractTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                if (mode) {
                    //个人简介可以为空
                    if (TextUtils.equals(s.toString(), inputValue)) {
                        publicTitleBarMore.setSelected(false);
                    }
                    else {
                        publicTitleBarMore.setSelected(true);
                    }
                    tvCalcNum.setText(String.format(getString(R.string.txt_calc_num), s.toString().trim().length()));
                }
                else {
                    if (TextUtils.isEmpty(s.toString())) {
                        publicTitleBarMore.setSelected(false);
                    }
                    else {
                        publicTitleBarMore.setSelected(true);
                    }
                }
            }
        });
    }

    /**
     * 更新个人简介
     */
    private void updateIntroduction(String value) {
        RequestUtils.updateIntroduce(this, loginBean.getToken(), value, this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.public_title_bar_back) {
            onFinish();
        }
    }

    @OnClick({ R.id.public_title_bar_more_a1 })
    public void onViewClicked() {
        if (publicTitleBarMore.isSelected()) { save(); }
    }

    private void onFinish() {
        if (mode && publicTitleBarMore.isSelected()) {
            new HintDialog(this).setContentString(R.string.txt_save_edit)
                                .setCancleBtnTxt(R.string.txt_not_save)
                                .setOnCancelClickListener(this::finish)
                                .setEnterBtnTxt(R.string.txt_save)
                                .setEnterSelect(true)
                                .setDeleteVisible(View.VISIBLE)
                                .setOnEnterClickListener(this::save)
                                .show();
            return;
        }
        hideSoftInputFromWindow();
        finish();
    }

    /**
     * 保存
     */
    private void save() {
        hideSoftInputFromWindow();
        inputValue = etHospital.getEditText().getText().toString().trim();
        if (mode) {
            updateIntroduction(inputValue);
        }
        else {
            HospitalBean bean = new HospitalBean();
            bean.setHospitalName(inputValue);
            Intent intent = new Intent();
            intent.putExtra(CommonData.KEY_HOSPITAL_BEAN, bean);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        if (task == Tasks.UPDATE_INTRODUCE) {
            ToastUtil.toast(this, response.getMsg());
            loginBean.setIntroduce(inputValue);
            ZycApplication.getInstance().setLoginBean(loginBean);
            Intent intent = new Intent();
            intent.putExtra(CommonData.KEY_PUBLIC_STRING, inputValue);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        onFinish();
    }

    InputFilter emojiFilter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            Matcher emojiMatcher = emoji.matcher(source);
            if (emojiMatcher.find()) {
                ToastUtil.toast(AddInfoActivity.this, com.yht.frame.R.string.txt_not_support_input);
                return "";
            }
            return null;
        }

        Pattern emoji = Pattern.compile(BaseUtils.filterImoji, Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
    };
}
