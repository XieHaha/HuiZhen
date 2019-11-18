package com.yht.yihuantong.ui.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yht.frame.data.type.CurrencyDetailType;
import com.yht.frame.data.bean.DoctorCurrencyDetailBean;
import com.yht.yihuantong.R;

import java.util.List;

/**
 * @author 顿顿
 * @date 19/6/10 12:18
 * @description 收入记录
 */
public class CurrencyDetailAdapter extends BaseQuickAdapter<DoctorCurrencyDetailBean, BaseViewHolder>
        implements CurrencyDetailType {
    public CurrencyDetailAdapter(int layoutResId, @Nullable List<DoctorCurrencyDetailBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DoctorCurrencyDetailBean item) {
        int type = item.getServiceFlag();
        switch (type) {
            case CURRENCY_DETAIL_TYPE_WITHDRAW:
                helper.setGone(R.id.tv_currency_total, false);
                helper.setText(R.id.tv_currency_arrived, item.getTotal());
                helper.setImageResource(R.id.iv_currency_type, R.mipmap.ic_withdraw);
                break;
            case CURRENCY_DETAIL_TYPE_CHECK:
                helper.setVisible(R.id.tv_currency_total, true);
                helper.setText(R.id.tv_currency_arrived, item.getArrived());
                helper.setImageResource(R.id.iv_currency_type, R.mipmap.ic_check);
                break;
            case CURRENCY_DETAIL_TYPE_TRANSFER:
                helper.setVisible(R.id.tv_currency_total, true);
                helper.setText(R.id.tv_currency_arrived, item.getArrived());
                helper.setImageResource(R.id.iv_currency_type, R.mipmap.ic_transfer);
                break;
            case CURRENCY_DETAIL_TYPE_REMOTE:
                helper.setVisible(R.id.tv_currency_total, true);
                helper.setText(R.id.tv_currency_arrived, item.getArrived());
                helper.setImageResource(R.id.iv_currency_type, R.mipmap.ic_remote);
                break;
            default:
                break;
        }
        helper.setText(R.id.tv_currency_name, item.getServiceFlagName())
              .setText(R.id.tv_currency_time, item.getOrderAt())
              .setText(R.id.tv_currency_total, "/" + item.getTotal());
    }
}
