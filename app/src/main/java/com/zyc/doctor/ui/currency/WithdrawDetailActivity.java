package com.zyc.doctor.ui.currency;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.yht.frame.ui.BaseActivity;
import com.zyc.doctor.R;

import butterknife.BindView;

/**
 * @author 顿顿
 * @date 19/6/11 10:40
 * @des 提现详情
 */
public class WithdrawDetailActivity extends BaseActivity {
    @BindView(R.id.tv_withdraw_price)
    TextView tvWithdrawPrice;
    @BindView(R.id.tv_withdraw_time)
    TextView tvWithdrawTime;
    @BindView(R.id.tv_withdraw_way)
    TextView tvWithdrawWay;
    @BindView(R.id.tv_withdraw_status)
    TextView tvWithdrawStatus;
    @BindView(R.id.tv_withdraw_remark)
    TextView tvWithdrawRemark;

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_withdraw_detail;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
    }
}
