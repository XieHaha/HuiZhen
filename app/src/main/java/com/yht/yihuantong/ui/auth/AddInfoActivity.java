package com.yht.yihuantong.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.CommonData;
import com.yht.frame.data.Tasks;
import com.yht.frame.data.bean.HospitalBean;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.widgets.edittext.AbstractTextWatcher;
import com.yht.frame.widgets.edittext.EditTextLayout;
import com.yht.yihuantong.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/6/5 17:19
 * @des 添加医院 or 添加个人简介
 */
public class AddInfoActivity extends BaseActivity {
    @BindView(R.id.edit_layout)
    EditTextLayout etHospital;
    @BindView(R.id.public_title_bar_more)
    TextView publicTitleBarMore;
    @BindView(R.id.public_title_bar_title)
    TextView publicTitleBarTitle;
    @BindView(R.id.tv_calc_num)
    TextView tvCalcNum;
    private String name;
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
            name = getIntent().getStringExtra(CommonData.KEY_PUBLIC_STRING);
        }
        publicTitleBarMore.setVisibility(View.VISIBLE);
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        if (mode) {
            publicTitleBarTitle.setText(R.string.txt_introduction);
            publicTitleBarMore.setText(R.string.txt_save);
            etHospital.getEditText().setHint(R.string.txt_introduction_hint);
            tvCalcNum.setVisibility(View.VISIBLE);
        }
        else {
            publicTitleBarMore.setText(R.string.txt_add);
            publicTitleBarTitle.setText(R.string.title_add_hospital);
            etHospital.getEditText().setHint(R.string.txt_search_hospital);
            tvCalcNum.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(name)) {
            etHospital.getEditText().setText(name);
            etHospital.getEditText().setSelection(name.length());
        }
    }

    @Override
    public void initListener() {
        super.initListener();
        etHospital.getEditText().addTextChangedListener(new AbstractTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                if (TextUtils.isEmpty(s.toString())) {
                    if (mode) {
                        //个人简介可以为空
                        publicTitleBarMore.setSelected(true);
                    }
                    else {
                        publicTitleBarMore.setSelected(false);
                    }
                }
                else {
                    publicTitleBarMore.setSelected(true);
                }
            }
        });
    }

    /**
     * 更新个人简介
     */
    private void updateIntroduction() {
    }

    @OnClick(R.id.public_title_bar_more)
    public void onViewClicked() {
        if (!publicTitleBarMore.isSelected()) {
            return;
        }
        hideSoftInputFromWindow();
        String content = etHospital.getEditText().getText().toString().trim();
        if (mode) {
            updateIntroduction();
        }
        else {
            HospitalBean bean = new HospitalBean();
            bean.setHospitalName(content);
            Intent intent = new Intent();
            intent.putExtra(CommonData.KEY_HOSPITAL_BEAN, bean);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
    }
}
