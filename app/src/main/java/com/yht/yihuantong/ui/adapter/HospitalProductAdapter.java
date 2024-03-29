package com.yht.yihuantong.ui.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yht.frame.data.bean.HospitalProductBean;
import com.yht.yihuantong.R;

import java.util.List;

/**
 * @author 顿顿
 * @date 19/6/10 12:18
 * @des 合作医院服务项列表
 */
public class HospitalProductAdapter extends BaseQuickAdapter<HospitalProductBean, BaseViewHolder> {
    public HospitalProductAdapter(int layoutResId, @Nullable List<HospitalProductBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HospitalProductBean item) {
        helper.setText(R.id.tv_content, item.getName());
        if (mData.size() - 1 == helper.getAdapterPosition()) {
            helper.setGone(R.id.view, false);
        }
        else {
            helper.setGone(R.id.view, true);
        }
    }
}
