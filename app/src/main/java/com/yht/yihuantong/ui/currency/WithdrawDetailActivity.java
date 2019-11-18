package com.yht.yihuantong.ui.currency;

import android.os.Bundle;
import androidx.annotation.NonNull;
import android.widget.TextView;

import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.CommonData;
import com.yht.frame.data.Tasks;
import com.yht.frame.data.bean.WithDrawDetailBean;
import com.yht.frame.http.retrofit.RequestUtils;
import com.yht.frame.ui.BaseActivity;
import com.yht.yihuantong.R;

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
    /**
     * 提现详情
     */
    private WithDrawDetailBean withDrawDetailBean;
    /**
     * 订单id
     */
    private int doctorOrderTranId;

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
        if (getIntent() != null) {
            doctorOrderTranId = getIntent().getIntExtra(CommonData.KEY_DOCTOR_CURRENCY_ID, 0);
        }
        getDoctorWithdraw();
    }

    private void initPage() {
        if (withDrawDetailBean != null) {
            tvWithdrawPrice.setText(withDrawDetailBean.getMedCoin());
            tvWithdrawTime.setText(withDrawDetailBean.getCreateAt());
            tvWithdrawWay.setText(withDrawDetailBean.getTransferTypeShow());
            tvWithdrawStatus.setText(withDrawDetailBean.getStatus());
            tvWithdrawRemark.setText(withDrawDetailBean.getRemark());
        }
    }

    /**
     * 提现详情
     */
    private void getDoctorWithdraw() {
        RequestUtils.getDoctorWithdraw(this, doctorOrderTranId, loginBean.getToken(), this);
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        if (task == Tasks.GET_DOCTOR_WITHDRAW) {
            withDrawDetailBean = (WithDrawDetailBean)response.getData();
            initPage();
        }
    }
}
