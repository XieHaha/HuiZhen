package com.yht.yihuantong.ui.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yht.frame.data.base.PatientBean;
import com.yht.yihuantong.R;

import java.util.List;

/**
 * @author 顿顿
 * @date 19/6/5 14:25
 * @des 预约检查记录
 */
public class TransferHistoryAdapter extends BaseQuickAdapter<PatientBean, BaseViewHolder> {
    public TransferHistoryAdapter(int layoutResId, @Nullable List<PatientBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PatientBean item) {
        helper.setText(R.id.tv_transfer_name, item.getName());
        if (helper.getAdapterPosition() == 0) {
            helper.setVisible(R.id.iv_transfer_status_in, true);
        }
        else {
            helper.setVisible(R.id.iv_transfer_status_in, false);
        }
        if (helper.getAdapterPosition() == 1) {
            helper.setVisible(R.id.iv_transfer_status_out, true);
        }
        else {
            helper.setVisible(R.id.iv_transfer_status_out, false);
        }
        if (helper.getAdapterPosition() == 2) {
            helper.setImageResource(R.id.iv_transfer_status_out, R.mipmap.ic_check_cancel);
            helper.setVisible(R.id.iv_transfer_status_out, true);
        }
        else {
            helper.setVisible(R.id.iv_transfer_status_out, false);
        }
    }
}
