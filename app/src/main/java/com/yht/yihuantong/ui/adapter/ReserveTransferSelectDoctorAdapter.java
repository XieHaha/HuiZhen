package com.yht.yihuantong.ui.adapter;

import android.support.annotation.Nullable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yht.yihuantong.R;

import java.util.List;

/**
 * @author 顿顿
 * @date 19/6/5 14:25
 * @des 选择接诊医生
 */
public class ReserveTransferSelectDoctorAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    private int curPosition = -1;

    public ReserveTransferSelectDoctorAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        TextView textView = helper.getView(R.id.tv_depart);
        textView.setText(item);
    }

    public void setCurPosition(int curPosition) {
        this.curPosition = curPosition;
    }
}
