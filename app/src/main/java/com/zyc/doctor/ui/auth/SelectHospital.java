package com.zyc.doctor.ui.auth;

import android.support.v7.widget.RecyclerView;

import com.yht.frame.ui.BaseActivity;
import com.yht.frame.widgets.edittext.SuperEditText;
import com.zyc.doctor.R;

import butterknife.BindView;

/**
 * @author 顿顿
 * @date 19/6/4 17:53
 * @des 医院选择
 */
public class SelectHospital extends BaseActivity {
    @BindView(R.id.et_search_hospital)
    SuperEditText etSearchHospital;
    @BindView(R.id.rv_hospital)
    RecyclerView rvHospital;

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_select_hospital;
    }
}
