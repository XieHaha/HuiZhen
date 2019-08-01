package com.yht.yihuantong.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yht.frame.data.bean.CheckTypeBean;
import com.yht.frame.data.bean.PatientOrderBean;
import com.yht.frame.data.type.CheckTypeStatus;
import com.yht.frame.data.type.PatientOrderStatus;
import com.yht.yihuantong.R;

import java.util.ArrayList;
import java.util.List;

import static com.yht.frame.data.type.CheckOrderStatus.CHECK_ORDER_STATUS_CANCEL;

/**
 * @author 顿顿
 * @date 19/6/5 14:25
 * @des 患者订单记录
 */
public class PatientOrderAdapter extends BaseMultiItemQuickAdapter<PatientOrderBean, BaseViewHolder>
        implements CheckTypeStatus, PatientOrderStatus {
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
            case PatientOrderBean.TRANSFER:
                initTransferData(helper, item);
                break;
            case PatientOrderBean.REMOTE:
                initRemoteData(helper, item);
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
            case PATIENT_ORDER_INCOMPLETE:
                helper.setVisible(R.id.iv_check_status_out, false);
                helper.setVisible(R.id.iv_check_status_in, true);
                break;
            case PATIENT_ORDER_COMPLETE:
                helper.setImageResource(R.id.iv_check_status_out, R.mipmap.ic_status_complete);
                helper.setVisible(R.id.iv_check_status_out, true);
                helper.setVisible(R.id.iv_check_status_in, false);
                break;
            case PATIENT_ORDER_CANCEL:
                helper.setVisible(R.id.iv_check_status_out, true);
                helper.setVisible(R.id.iv_check_status_in, false);
                helper.setImageResource(R.id.iv_check_status_out, R.mipmap.ic_status_cancel);
                break;
            default:
                break;
        }
        addCheckView(helper, item);
    }

    /**
     * 转诊数据
     *
     * @param helper
     * @param item
     */
    private void initTransferData(BaseViewHolder helper, PatientOrderBean item) {
        helper.setText(R.id.tv_transfer_name, R.string.txt_reserve_transfer)
              .setText(R.id.tv_transfer_doctor, item.getSourceDoctorName())
              .setText(R.id.tv_transfer_depart, item.getSourceHospitalDepartmentName())
              .setText(R.id.tv_transfer_hospital, item.getSourceHospitalName())
              .setText(R.id.tv_receiving_doctor, item.getTargetDoctorName())
              .setText(R.id.tv_receiving_depart, item.getTargetHospitalDepartmentName())
              .setText(R.id.tv_receiving_hospital, item.getTargetHospitalName())
              .setImageResource(R.id.iv_transfer_img, R.mipmap.ic_transfer)
              .setVisible(R.id.layout_transfer_root, true)
              .setGone(R.id.layout_transfer_purpose, false);
        //订单状态
        int status = item.getStatus();
        switch (status) {
            case PATIENT_ORDER_INCOMPLETE:
                helper.setVisible(R.id.iv_transfer_status_out, false);
                helper.setVisible(R.id.iv_transfer_status_in, true);
                break;
            case PATIENT_ORDER_COMPLETE:
                helper.setImageResource(R.id.iv_transfer_status_out, R.mipmap.ic_status_received);
                helper.setVisible(R.id.iv_transfer_status_out, true);
                helper.setVisible(R.id.iv_transfer_status_in, false);
                break;
            case PATIENT_ORDER_CANCEL:
                helper.setVisible(R.id.iv_transfer_status_out, true);
                helper.setVisible(R.id.iv_transfer_status_in, false);
                helper.setImageResource(R.id.iv_transfer_status_out, R.mipmap.ic_status_cancel);
                break;
            case PATIENT_ORDER_REJECT:
                helper.setVisible(R.id.iv_transfer_status_out, true);
                helper.setVisible(R.id.iv_transfer_status_in, false);
                helper.setImageResource(R.id.iv_transfer_status_out, R.mipmap.ic_status_be_rejected);
                break;
            default:
                break;
        }
    }

    /**
     * 远程数据
     *
     * @param helper
     * @param item
     */
    private void initRemoteData(BaseViewHolder helper, PatientOrderBean item) {
        helper.setText(R.id.tv_remote_name, R.string.txt_remote_consultation)
              .setImageResource(R.id.iv_remote_img, R.mipmap.ic_remote);
    }

    /**
     * 添加检查项
     *
     * @param helper
     * @param item
     */
    private void addCheckView(BaseViewHolder helper, PatientOrderBean item) {
        //检查项数据
        LinearLayout layout = helper.getView(R.id.layout_check_type);
        layout.removeAllViews();
        ArrayList<CheckTypeBean> list = item.getTrans();
        //已经上传了报告的检查项
        ArrayList<CheckTypeBean> reportList = new ArrayList<>();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                CheckTypeBean checkTypeBean = list.get(i);
                View view = LayoutInflater.from(mContext).inflate(R.layout.item_check_type, null);
                TextView textView = view.findViewById(R.id.tv_check_type_name);
                ImageView imageDot = view.findViewById(R.id.iv_check_type_dot);
                ImageView imageView = view.findViewById(R.id.iv_check_type_status);
                textView.setText(checkTypeBean.getName());
                int status = checkTypeBean.getStatus();
                //已完成（已上传报告）
                if (status == CHECK_TYPE_STATUS_COMPLETE) {
                    reportList.add(checkTypeBean);
                }
                //检查项已取消
                if (item.getStatus() != CHECK_ORDER_STATUS_CANCEL && status == CHECK_TYPE_STATUS_CANCEL) {
                    textView.setSelected(true);
                    imageDot.setSelected(true);
                    imageView.setVisibility(View.VISIBLE);
                }
                else {
                    textView.setSelected(false);
                    imageDot.setSelected(false);
                    imageView.setVisibility(View.GONE);
                }
                layout.addView(view);
            }
            if (reportList.size() > 0) {
                //                helper.setText(R.id.tv_check_report,
                //                               String.format(mContext.getString(R.string.txt_report_num), reportList.size(),
                //                                             list.size()));
                helper.setGone(R.id.layout_check_report_root, true);
                addReportView(helper, reportList);
            }
            else {
                helper.setGone(R.id.layout_check_report_root, false);
            }
        }
        else {
            helper.setGone(R.id.layout_check_report_root, false);
        }
    }

    /**
     * 添加报告
     *
     * @param helper
     * @param reportList
     */
    private void addReportView(BaseViewHolder helper, ArrayList<CheckTypeBean> reportList) {
        //检查报告
        LinearLayout layoutReport = helper.getView(R.id.layout_check_report);
        layoutReport.removeAllViews();
        for (int i = 0; i < reportList.size(); i++) {
            CheckTypeBean bean = reportList.get(i);
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_check_report, null);
            TextView textView = view.findViewById(R.id.tv_check_report_name);
            textView.setText(bean.getName());
            view.setTag(i);
            layoutReport.addView(view);
        }
    }
}
