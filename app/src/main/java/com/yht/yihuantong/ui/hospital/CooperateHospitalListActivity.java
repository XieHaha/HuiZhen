package com.yht.yihuantong.ui.hospital;

import com.yht.frame.ui.BaseActivity;
import com.yht.yihuantong.R;

/**
 * @author 顿顿
 * @date 19/8/12 15:34
 * @des 合作医院列表
 */
public class CooperateHospitalListActivity extends BaseActivity {
    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_cooperate_hospital;
    }
}
