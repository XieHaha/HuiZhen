package com.yht.yihuantong.ui.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yht.frame.data.bean.HospitalBean;
import com.yht.frame.data.type.CurrencyDetailType;
import com.yht.yihuantong.R;

import java.util.List;

/**
 * @author 顿顿
 * @date 19/6/10 12:18
 * @des 合作医院列表
 */
public class CooperateHospitalAdapter extends BaseQuickAdapter<HospitalBean, BaseViewHolder>
        implements CurrencyDetailType {
    public CooperateHospitalAdapter(int layoutResId, @Nullable List<HospitalBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HospitalBean item) {
        helper.setText(R.id.tv_hospital, item.getHospitalName());
        if (item.getSettleStatus() == 0) {
            helper.setImageResource(R.id.iv_status, R.mipmap.ic_cooperation);
        }
        else {
            helper.setImageResource(R.id.iv_status, R.mipmap.ic_no_cooperation);
        }
        if (mData.size() - 1 == helper.getAdapterPosition()) {
            helper.setGone(R.id.view, false);
        }
        else {
            helper.setGone(R.id.view, true);
        }
    }
}
