package com.yht.yihuantong.ui.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yht.frame.data.base.PatientBean;
import com.yht.frame.utils.BaseUtils;
import com.yht.yihuantong.R;

import java.util.List;

/**
 * @author 顿顿
 * @date 19/6/5 14:25
 * @des 已接收转诊
 */
public class TransferWaitAdapter extends BaseQuickAdapter<PatientBean, BaseViewHolder> {
    public TransferWaitAdapter(int layoutResId, @Nullable List<PatientBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PatientBean item) {
        helper.setText(R.id.txt_transfer_time,
                       BaseUtils.formatDate(System.currentTimeMillis(), BaseUtils.YYYY_MM_DD_HH_MM_SS))
              .setText(R.id.tv_transfer_name, item.getName())
              .setText(R.id.tv_transfer_purpose, "目的")
              .setText(R.id.tv_transfer_doctor, "医生")
              .setText(R.id.tv_transfer_depart, "部门")
              .setText(R.id.tv_transfer_hospital, "医院");
    }
}
