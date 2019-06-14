package com.zyc.doctor.ui.check;

import com.yht.frame.ui.BaseActivity;
import com.zyc.doctor.R;

/**
 * @author 顿顿
 * @date 19/6/14 10:56
 * @des 预约检查详情
 */
public class ReservationCheckDetailActivity extends BaseActivity {
    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_reservation_check_detail;
    }
}
