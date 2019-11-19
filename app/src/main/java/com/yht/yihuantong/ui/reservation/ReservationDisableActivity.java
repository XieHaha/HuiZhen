package com.yht.yihuantong.ui.reservation;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

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
    @BindView(R.id.iv_pic)
    ImageView ivPic;
    @BindView(R.id.tv_disable_hint)
    TextView tvDisableHint;

    /**
     * 预约类型  默认预约服务
     */
    private int reservationType;

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
            reservationType = getIntent().getIntExtra(CommonData.KEY_RESERVATION_TYPE, 0);
        }

        switch (reservationType) {
            case BASE_ONE:
                publicTitleBarTitle.setText(R.string.txt_reserve_transfer);
                tvDisableHint.setText(R.string.txt_none_hospital_by_reserve_transfer);
                break;
            case BASE_TWO:
                publicTitleBarTitle.setText(R.string.txt_remote_consultation);
                tvDisableHint.setText(R.string.txt_none_permission_by_reserve_remote);
                ivPic.setImageResource(R.mipmap.pic_account_disable);
                break;
            default:
                publicTitleBarTitle.setText(R.string.txt_reserve_check);
                tvDisableHint.setText(R.string.txt_none_hospital_by_reserve);
                break;
        }
    }
}
