package com.yht.yihuantong.ui.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yht.frame.data.bean.HospitalPackageBean;
import com.yht.yihuantong.R;

import java.util.List;

/**
 * @author 顿顿
 * @date 19/6/10 12:18
 * @des 合作医院服务项列表
 */
public class HospitalPackageAdapter extends BaseQuickAdapter<HospitalPackageBean, BaseViewHolder> {
    public HospitalPackageAdapter(int layoutResId, @Nullable List<HospitalPackageBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HospitalPackageBean item) {
        helper.setText(R.id.tv_content, item.getPackageName());
        if (mData.size() - 1 == helper.getAdapterPosition()) {
            helper.setGone(R.id.view, false);
        }
        else {
            helper.setGone(R.id.view, true);
        }
    }
}
