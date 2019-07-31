package com.yht.yihuantong.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.yht.frame.data.CommonData;
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
 * @des 添加医院
 */
public class AddHospitalActivity extends BaseActivity {
    @BindView(R.id.edit_layout)
    EditTextLayout etHospital;
    @BindView(R.id.public_title_bar_more)
    TextView publicTitleBarMore;

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_add_hospital;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        publicTitleBarMore.setVisibility(View.VISIBLE);
        publicTitleBarMore.setText(R.string.txt_add);
    }

    @Override
    public void initListener() {
        super.initListener();
        etHospital.getEditText().addTextChangedListener(new AbstractTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                if (TextUtils.isEmpty(s.toString())) {
                    publicTitleBarMore.setSelected(false);
                }
                else {
                    publicTitleBarMore.setSelected(true);
                }
            }
        });
    }

    @OnClick(R.id.public_title_bar_more)
    public void onViewClicked() {
        if (!publicTitleBarMore.isSelected()) {
            return;
        }
        hideSoftInputFromWindow();
        String hospitalName = etHospital.getEditText().getText().toString().trim();
        HospitalBean bean = new HospitalBean();
        bean.setHospitalName(hospitalName);
        Intent intent = new Intent();
        intent.putExtra(CommonData.KEY_HOSPITAL_BEAN, bean);
        setResult(RESULT_OK, intent);
        finish();
    }
}
