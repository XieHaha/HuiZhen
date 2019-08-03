package com.yht.yihuantong.ui.reservation;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.yht.frame.data.CommonData;
import com.yht.frame.ui.BaseActivity;
import com.yht.yihuantong.R;

import butterknife.BindView;

/**
 * @author 顿顿
 * @date 19/7/12 14:15
 * @des 预约禁用
 */
public class ReservationDisableActivity extends BaseActivity {
    @BindView(R.id.public_title_bar_title)
    TextView publicTitleBarTitle;
    @BindView(R.id.tv_disable_hint)
    TextView tvDisableHint;
    /**
     * 是否为转诊
     */
    private boolean isTransfer;

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_reservation_disable;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        if (getIntent() != null) {
            isTransfer = getIntent().getBooleanExtra(CommonData.KEY_CHECK_OR_TRANSFER, false);
        }
        if (isTransfer) {
            publicTitleBarTitle.setText(R.string.txt_reserve_transfer);
            tvDisableHint.setText(R.string.txt_none_hospital_by_reserve_transfer);
        }
        else {
            publicTitleBarTitle.setText(R.string.txt_reserve_check);
            tvDisableHint.setText(R.string.txt_none_hospital_by_reserve);
        }
    }
}
