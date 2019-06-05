package com.zyc.doctor.ui.auth;

import com.yht.frame.ui.BaseActivity;
import com.zyc.doctor.R;

/**
 * @author 顿顿
 * @date 19/6/5 17:19
 * @des 添加医院
 */
public class AddHospitalActivity extends BaseActivity {
    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_add_hospital;
    }
}
