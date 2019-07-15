package com.yht.yihuantong.ui.reservation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.yht.frame.api.notify.NotifyChangeListenerManager;
import com.yht.frame.data.CommonData;
import com.yht.frame.ui.BaseActivity;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ui.check.CheckDetailActivity;
import com.yht.yihuantong.ui.transfer.TransferInitiateDetailActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/6/17 15:06
 * @des 预约检查、转诊成功
 */
public class ReservationSuccessActivity extends BaseActivity {
    @BindView(R.id.public_title_bar_title)
    TextView publicTitleBarTitle;
    @BindView(R.id.tv_success_hint)
    TextView tvSuccessHint;
    @BindView(R.id.tv_detail)
    TextView tvDetail;
    @BindView(R.id.tv_again)
    TextView tvAgain;
    /**
     * 是否为预约转诊
     */
    private boolean isTransfer;
    /**
     * 订单编号
     */
    private String orderNo;

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_check_success;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        if (getIntent() != null) {
            isTransfer = getIntent().getBooleanExtra(CommonData.KEY_CHECK_OR_TRANSFER, false);
            orderNo = getIntent().getStringExtra(CommonData.KEY_ORDER_ID);
        }
        //预约订单成功  刷新患者列表数据
        NotifyChangeListenerManager.getInstance().notifyPatientStatusChange("");
        initPage();
    }

    /**
     * 页面展示处理
     */
    private void initPage() {
        if (isTransfer) {
            publicTitleBarTitle.setText(R.string.txt_reserve_transfer);
            tvSuccessHint.setText(R.string.txt_transfer_success_hint);
            tvDetail.setText(R.string.txt_look_transfer_detail);
            tvAgain.setText(R.string.txt_transfer_again_add);
        }
    }

    @OnClick({ R.id.tv_detail, R.id.tv_again })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_detail:
                if (isTransfer) {
                    Intent intent = new Intent(this, TransferInitiateDetailActivity.class);
                    intent.putExtra(CommonData.KEY_ORDER_ID, orderNo);
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(this, CheckDetailActivity.class);
                    intent.putExtra(CommonData.KEY_PUBLIC, true);
                    intent.putExtra(CommonData.KEY_ORDER_ID, orderNo);
                    startActivity(intent);
                }
                finish();
                break;
            case R.id.tv_again:
                if (!isTransfer) {
                    startActivity(new Intent(this, ReservationCheckOrTransferActivity.class));
                }
                else {
                    Intent intent = new Intent(this, ReservationCheckOrTransferActivity.class);
                    intent.putExtra(CommonData.KEY_CHECK_OR_TRANSFER, true);
                    startActivity(intent);
                }
                finish();
                break;
            default:
                break;
        }
    }
}
