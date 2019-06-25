package com.zyc.doctor.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.TextView;

import com.yht.frame.data.CommonData;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.utils.ToastUtil;
import com.yht.frame.widgets.edittext.SuperEditText;
import com.zyc.doctor.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/6/5 17:19
 * @des 添加医院
 */
public class AddHospitalActivity extends BaseActivity {
    @BindView(R.id.et_hospital)
    SuperEditText etHospital;
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
        publicTitleBarMore.setText(R.string.txt_add);
        publicTitleBarMore.setSelected(true);
    }

    @OnClick(R.id.public_title_bar_more)
    public void onViewClicked() {
        String hospitalName = etHospital.getText().toString().trim();
        if (TextUtils.isEmpty(hospitalName)) {
            ToastUtil.toast(this, "");
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(CommonData.KEY_HOSPITAL_NAME, hospitalName);
        setResult(RESULT_OK, intent);
        finish();
    }
}
