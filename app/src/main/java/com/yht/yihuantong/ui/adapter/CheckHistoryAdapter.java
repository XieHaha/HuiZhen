package com.yht.yihuantong.ui.adapter;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yht.frame.data.base.CheckBean;
import com.yht.yihuantong.R;

import java.util.List;

/**
 * @author 顿顿
 * @date 19/6/5 14:25
 * @des 预约检查记录
 */
public class CheckHistoryAdapter extends BaseQuickAdapter<CheckBean, BaseViewHolder> {
    public CheckHistoryAdapter(int layoutResId, @Nullable List<CheckBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CheckBean item) {
        helper.setText(R.id.tv_check_name, item.getOrderNo());
        if (helper.getAdapterPosition() == 0) {
            helper.setVisible(R.id.iv_check_status_in, true);
        }
        else {
            helper.setVisible(R.id.iv_check_status_in, false);
        }
        if (helper.getAdapterPosition() == 1) {
            helper.setVisible(R.id.iv_check_status_out, true);
        }
        else {
            helper.setVisible(R.id.iv_check_status_out, false);
        }
        if (helper.getAdapterPosition() == 2) {
            helper.setImageResource(R.id.iv_check_status_out, R.mipmap.ic_status_cancel);
            helper.setVisible(R.id.iv_check_status_out, true);
        }
        else {
            helper.setVisible(R.id.iv_check_status_out, false);
        }
        LinearLayout layout = helper.getView(R.id.layout_check_type);
        addView(layout, item);
    }

    private void addView(LinearLayout layout, CheckBean item) {
        for (int i = 0; i < 3; i++) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_check_type, null);
            TextView textView = view.findViewById(R.id.tv_check_type_name);
            ImageView imageDot = view.findViewById(R.id.iv_check_type_dot);
            ImageView imageView = view.findViewById(R.id.iv_check_type_status);
            textView.setText(item.getOrderNo());
            if (i == 1) {
                imageView.setVisibility(View.VISIBLE);
                textView.setSelected(true);
                imageDot.setSelected(true);
            }
            else {
                imageView.setVisibility(View.GONE);
                textView.setSelected(false);
                imageDot.setSelected(false);
            }
            layout.addView(view);
        }
    }
}
