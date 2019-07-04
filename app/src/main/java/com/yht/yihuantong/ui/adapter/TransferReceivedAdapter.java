package com.yht.yihuantong.ui.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yht.frame.data.bean.PatientBean;
import com.yht.frame.utils.BaseUtils;
import com.yht.yihuantong.R;

import java.util.List;

/**
 * @author 顿顿
 * @date 19/6/5 14:25
 * @des 已接收转诊
 */
public class TransferReceivedAdapter extends BaseQuickAdapter<PatientBean, BaseViewHolder> {
    public TransferReceivedAdapter(int layoutResId, @Nullable List<PatientBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PatientBean item) {
        helper.setText(R.id.tv_reserve_visit_time,
                       BaseUtils.formatDate(System.currentTimeMillis(), BaseUtils.YYYY_MM_DD_HH_MM_SS))
              .setText(R.id.tv_receiving_doctor_name, item.getName())
              .setText(R.id.tv_receiving_doctor_sex, "男")
              .setText(R.id.tv_receiving_doctor_age, "20");
    }
}
