package com.yht.yihuantong.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yht.frame.data.CheckStatus;
import com.yht.frame.data.OrderStatus;
import com.yht.frame.data.base.CheckTypeBean;
import com.yht.frame.data.base.PatientOrderBean;
import com.yht.yihuantong.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 顿顿
 * @date 19/6/5 14:25
 * @des 患者订单记录
 */
public class PatientOrderAdapter extends BaseMultiItemQuickAdapter<PatientOrderBean, BaseViewHolder>
        implements CheckStatus, OrderStatus {
    /**
     * @param data A new list is created out of this one to avoid mutable list
     */
    public PatientOrderAdapter(List<PatientOrderBean> data) {
        super(data);
        addItemType(PatientOrderBean.CHECK, R.layout.item_check_history);
        addItemType(PatientOrderBean.TRANSFER, R.layout.item_transfer_history);
        addItemType(PatientOrderBean.REMOTE, R.layout.item_remote_history);
    }

    @Override
    protected void convert(BaseViewHolder helper, PatientOrderBean item) {
        switch (helper.getItemViewType()) {
            case PatientOrderBean.CHECK:
                initCheckData(helper, item);
                break;
            case PatientOrderBean.REMOTE:
                initRemoteData(helper, item);
                break;
            case PatientOrderBean.TRANSFER:
                initTransferData(helper, item);
                break;
            default:
                break;
        }
    }

    /**
     * 检查数据
     *
     * @param helper
     * @param item
     */
    private void initCheckData(BaseViewHolder helper, PatientOrderBean item) {
        helper.setText(R.id.tv_check_name, R.string.txt_reserve_check);
        helper.setImageResource(R.id.iv_check_img, R.mipmap.ic_check);
        helper.setText(R.id.tv_check_hospital, item.getTargetHospitalName());
        //订单状态
        int status = item.getStatus();
        switch (status) {
            case ORDER_STATUS_INCOMPLETE:
                helper.setVisible(R.id.iv_check_status_out, false);
                helper.setVisible(R.id.iv_check_status_in, true);
                break;
            case ORDER_STATUS_COMPLETE:
                helper.setImageResource(R.id.iv_check_status_out, R.mipmap.ic_check_complete);
                helper.setVisible(R.id.iv_check_status_out, true);
                helper.setVisible(R.id.iv_check_status_in, false);
                break;
            case ORDER_STATUS_CANCEL:
                helper.setVisible(R.id.iv_check_status_out, true);
                helper.setVisible(R.id.iv_check_status_in, false);
                helper.setImageResource(R.id.iv_check_status_out, R.mipmap.ic_check_cancel);
                break;
            default:
                break;
        }
        //检查项数据
        LinearLayout layout = helper.getView(R.id.layout_check_type);
        addCheckView(layout, item);
        //检查报告
        helper.setVisible(R.id.layout_check_report_root, false);
        LinearLayout layoutReport = helper.getView(R.id.layout_check_report);
        addReportView(layoutReport, item);
    }

    /**
     * 远程数据
     *
     * @param helper
     * @param item
     */
    private void initRemoteData(BaseViewHolder helper, PatientOrderBean item) {
        helper.setText(R.id.tv_remote_name, R.string.txt_remote_consultation);
        helper.setImageResource(R.id.iv_remote_img, R.mipmap.ic_remote);
    }

    /**
     * 转诊数据
     *
     * @param helper
     * @param item
     */
    private void initTransferData(BaseViewHolder helper, PatientOrderBean item) {
        helper.setText(R.id.tv_transfer_name, R.string.txt_reserve_transfer);
        helper.setImageResource(R.id.iv_transfer_img, R.mipmap.ic_transfer);
        helper.setVisible(R.id.layout_transfer_root, true);
        helper.setVisible(R.id.layout_transfer_purpose, false);
    }

    /**
     * 添加检查项
     *
     * @param layout
     * @param item
     */
    private void addCheckView(LinearLayout layout, PatientOrderBean item) {
        //已经初始化后不在处理
        if (layout.getChildCount() > 0) {
            return;
        }
        ArrayList<CheckTypeBean> list = item.getTrans();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                View view = LayoutInflater.from(mContext).inflate(R.layout.item_check_type, null);
                TextView textView = view.findViewById(R.id.tv_check_type_name);
                ImageView imageDot = view.findViewById(R.id.iv_check_type_dot);
                ImageView imageView = view.findViewById(R.id.iv_check_type_status);
                textView.setText(list.get(i).getName());
                int status = list.get(i).getStatus();
                switch (status) {
                    case CHECK_STATUS_WAIT:
                        textView.setSelected(true);
                        imageDot.setSelected(true);
                        imageView.setVisibility(View.GONE);
                        break;
                    case CHECK_STATUS_WAIT_PAY:
                        break;
                    case CHECK_STATUS_PAID:
                        break;
                    case CHECK_STATUS_COMPLETE:
                        break;
                    case CHECK_STATUS_CANCEL:
                        textView.setSelected(false);
                        imageDot.setSelected(false);
                        imageView.setVisibility(View.VISIBLE);
                        break;
                    default:
                        break;
                }
                layout.addView(view);
            }
        }
    }

    /**
     * 添加报告
     *
     * @param layout
     * @param item
     */
    private void addReportView(LinearLayout layout, PatientOrderBean item) {
        //已经初始化后不在处理
        if (layout.getChildCount() > 0) {
            return;
        }
    }
}
