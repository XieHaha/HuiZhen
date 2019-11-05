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
import com.yht.yihuantong.ui.check.ServiceHistoryActivity;
import com.yht.yihuantong.ui.remote.RemoteDetailActivity;
import com.yht.yihuantong.ui.reservation.service.ReservationServiceActivity;
import com.yht.yihuantong.ui.reservation.transfer.ReservationTransferActivity;
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
     * 预约类型  0 服务，1 转诊，2 会诊
     */
    private int reservationType;
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
            reservationType = getIntent().getIntExtra(CommonData.KEY_RESERVATION_TYPE, 0);
            orderNo = getIntent().getStringExtra(CommonData.KEY_ORDER_ID);
        }
        //预约订单成功  刷新居民列表数据
        NotifyChangeListenerManager.getInstance().notifyPatientListChanged("");
        initPage();
    }

    /**
     * 页面展示处理
     */
    private void initPage() {
        switch (reservationType) {
            case BASE_ZERO:
                break;
            case BASE_ONE:
                publicTitleBarTitle.setText(R.string.txt_reserve_transfer);
                tvSuccessHint.setText(R.string.txt_transfer_success_hint);
                tvDetail.setText(R.string.txt_look_transfer_detail);
                tvAgain.setText(R.string.txt_transfer_again_add);
                break;
            case BASE_TWO:
                publicTitleBarTitle.setText(R.string.txt_remote_consultation);
                tvSuccessHint.setText(R.string.txt_remote_success_hint);
                tvDetail.setText(R.string.txt_look_remote_detail);
                tvAgain.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    @OnClick({ R.id.tv_detail, R.id.tv_again })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_detail:
                Intent intent;
                switch (reservationType) {
                    case BASE_ZERO:
                        intent = new Intent(this, ServiceHistoryActivity.class);
                        startActivity(intent);
                        break;
                    case BASE_ONE:
                        intent = new Intent(this, TransferInitiateDetailActivity.class);
                        intent.putExtra(CommonData.KEY_ORDER_ID, orderNo);
                        startActivity(intent);
                        break;
                    case BASE_TWO:
                        intent = new Intent(this, RemoteDetailActivity.class);
                        intent.putExtra(CommonData.KEY_ORDER_ID, orderNo);
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
                finish();
                break;
            case R.id.tv_again:
                switch (reservationType) {
                    case BASE_ZERO:
                        startActivity(new Intent(this, ReservationServiceActivity.class));
                        break;
                    case BASE_ONE:
                        intent = new Intent(this, ReservationTransferActivity.class);
                        startActivity(intent);
                        break;
                    case BASE_TWO:
                        break;
                    default:
                        break;
                }
                finish();
                break;
            default:
                break;
        }
    }
}
