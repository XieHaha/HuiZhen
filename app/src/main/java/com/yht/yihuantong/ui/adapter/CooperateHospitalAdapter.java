package com.yht.yihuantong.ui.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yht.frame.data.bean.CooperateHospitalBean;
import com.yht.yihuantong.R;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @author 顿顿
 * @date 19/6/10 12:18
 * @des 合作医院列表
 */
public class CooperateHospitalAdapter extends BaseQuickAdapter<CooperateHospitalBean, BaseViewHolder> {
    public CooperateHospitalAdapter(int layoutResId, @Nullable List<CooperateHospitalBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CooperateHospitalBean item) {
        helper.setText(R.id.tv_hospital, item.getHospitalName());
        if (TextUtils.equals(item.getCooperateStatus(), "1")) {
            helper.setImageResource(R.id.iv_status, R.mipmap.ic_cooperation);
            helper.setText(R.id.tv_reserve, R.string.txt_business_support)
                  .setText(R.id.tv_reserve_content, StringUtils.join(item.getServiceList(), ","));
        }
        else {
            helper.setImageResource(R.id.iv_status, R.mipmap.ic_no_cooperation);
            helper.setText(R.id.tv_reserve, R.string.txt_business_support_not).setText(R.id.tv_reserve_content, "");
        }
        if (mData.size() - 1 == helper.getAdapterPosition()) {
            helper.setGone(R.id.view, false);
        }
        else {
            helper.setGone(R.id.view, true);
        }
    }
}
