package com.zyc.doctor.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yht.frame.data.bean.CheckBean;
import com.zyc.doctor.R;

import java.util.List;

/**
 * @author 顿顿
 * @date 19/6/5 14:25
 * @des 预约检查记录
 */
public class PatientInfoAdapter extends BaseMultiItemQuickAdapter<CheckBean, BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public PatientInfoAdapter(List<CheckBean> data) {
        super(data);
        addItemType(CheckBean.CHECK, R.layout.item_check_history);
        addItemType(CheckBean.TRANSFER, R.layout.item_transfer_history);
        addItemType(CheckBean.REMOTE, R.layout.item_remote_history);
    }

    @Override
    protected void convert(BaseViewHolder helper, CheckBean item) {
        switch (helper.getItemViewType()) {
            case CheckBean.CHECK:
                helper.setText(R.id.tv_check_name, R.string.txt_reserve_check);
                helper.setImageResource(R.id.iv_check_img, R.mipmap.ic_check);
                helper.setVisible(R.id.iv_check_status_out, true);
                helper.setVisible(R.id.layout_check_report_root, true);
                LinearLayout layout = helper.getView(R.id.layout_check_type);
                addCheckView(layout, item);
                LinearLayout layoutReport = helper.getView(R.id.layout_check_report);
                addReportView(layoutReport, item);
                break;
            case CheckBean.TRANSFER:
                helper.setText(R.id.tv_transfer_name, R.string.txt_reserve_transfer);
                helper.setImageResource(R.id.iv_transfer_img, R.mipmap.ic_transfer);
                helper.setVisible(R.id.layout_transfer_root, true);
                helper.setGone(R.id.layout_transfer_purpose, false);
                if (helper.getAdapterPosition() == 2) {
                    helper.setVisible(R.id.iv_transfer_status_in, true);
                }
                else {
                    helper.setVisible(R.id.iv_transfer_status_in, false);
                }
                if (helper.getAdapterPosition() == 3) {
                    helper.setImageResource(R.id.iv_transfer_status_out, R.mipmap.ic_check_cancel);
                    helper.setVisible(R.id.iv_transfer_status_out, true);
                }
                else {
                    helper.setVisible(R.id.iv_transfer_status_out, false);
                }
                break;
            case CheckBean.REMOTE:
                helper.setText(R.id.tv_remote_name, R.string.txt_remote_consultation);
                helper.setImageResource(R.id.iv_remote_img, R.mipmap.ic_remote);
                if (helper.getAdapterPosition() == 4) {
                    helper.setVisible(R.id.iv_status_in, true);
                }
                else {
                    helper.setVisible(R.id.iv_status_in, false);
                }
                if (helper.getAdapterPosition() == 5) {
                    helper.setImageResource(R.id.iv_status_out, R.mipmap.ic_check_cancel);
                    helper.setVisible(R.id.iv_status_out, true);
                }
                else {
                    helper.setVisible(R.id.iv_status_out, false);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 添加检查项
     *
     * @param layout
     * @param item
     */
    private void addCheckView(LinearLayout layout, CheckBean item) {
        //已经初始化后不在处理
        if (layout.getChildCount() > 0) {
            return;
        }
        for (int i = 0; i < 3; i++) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_check_type, null);
            TextView textView = view.findViewById(R.id.tv_check_type_name);
            ImageView imageDot = view.findViewById(R.id.iv_check_type_dot);
            ImageView imageView = view.findViewById(R.id.iv_check_type_status);
            textView.setText(item.getTitle());
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

    /**
     * 添加报告
     *
     * @param layout
     * @param item
     */
    private void addReportView(LinearLayout layout, CheckBean item) {
        //已经初始化后不在处理
        if (layout.getChildCount() > 0) {
            return;
        }
        for (int i = 0; i < 3; i++) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_check_report, null);
            TextView textView = view.findViewById(R.id.tv_check_report_name);
            textView.setText("检查报告");
            view.setTag(i);
            layout.addView(view);
        }
    }
}
