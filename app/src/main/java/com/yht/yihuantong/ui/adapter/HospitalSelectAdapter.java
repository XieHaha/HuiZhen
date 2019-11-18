package com.yht.yihuantong.ui.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yht.frame.data.bean.HospitalBean;
import com.yht.yihuantong.R;

import java.util.List;

/**
 * @author 顿顿
 * @date 19/6/5 14:25
 * @des 医院搜索适配器
 */
public class HospitalSelectAdapter extends BaseQuickAdapter<HospitalBean, BaseViewHolder> {
    public HospitalSelectAdapter(int layoutResId, @Nullable List<HospitalBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HospitalBean item) {
        helper.setText(R.id.tv_hospital_name, item.getHospitalName())
              .setText(R.id.tv_hospital_address, item.getHospitalAddress());
    }
}
