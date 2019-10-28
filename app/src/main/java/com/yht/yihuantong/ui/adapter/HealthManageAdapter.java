package com.yht.yihuantong.ui.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yht.frame.data.bean.HealthPackageBean;
import com.yht.frame.data.type.CheckOrderStatus;
import com.yht.yihuantong.R;

import java.util.List;

/**
 * @author 顿顿
 * @date 19/6/5 14:25
 * @des 健康管理
 */
public class HealthManageAdapter extends BaseQuickAdapter<HealthPackageBean, BaseViewHolder>
        implements CheckOrderStatus {
    public HealthManageAdapter(int layoutResId, @Nullable List<HealthPackageBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HealthPackageBean item) {
        helper.setText(R.id.tv_title, item.getPackageName()).setText(R.id.tv_hospital, item.getHospitalName());
    }
}
