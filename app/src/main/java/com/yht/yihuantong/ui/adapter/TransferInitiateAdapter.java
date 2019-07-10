package com.yht.yihuantong.ui.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yht.frame.data.TransferOrderStatus;
import com.yht.frame.data.base.TransferBean;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.utils.glide.GlideHelper;
import com.yht.yihuantong.R;
import com.yht.yihuantong.utils.ImageUrlUtil;

import java.util.List;

/**
 * @author 顿顿
 * @date 19/6/5 14:25
 * @des 预约检查记录
 */
public class TransferInitiateAdapter extends BaseQuickAdapter<TransferBean, BaseViewHolder>
        implements TransferOrderStatus {
    public TransferInitiateAdapter(int layoutResId, @Nullable List<TransferBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TransferBean item) {
        Glide.with(mContext)
             .load(ImageUrlUtil.append(item.getWxPhoto()))
             .apply(GlideHelper.getOptions(BaseUtils.dp2px(mContext, 4)))
             .into((ImageView)helper.getView(R.id.iv_transfer_img));
        helper.setGone(R.id.layout_transfer_root, false);
        helper.setText(R.id.tv_transfer_name, item.getPatientName());
        helper.setText(R.id.tv_transfer_purpose, item.getTransferTarget());
        int receiveStatus = item.getReceiveStatus();
        switch (receiveStatus) {
            case TRANSFER_STATUS_WAIT:
                helper.setGone(R.id.iv_transfer_status_in, true);
                helper.setGone(R.id.iv_transfer_status_out, false);
                helper.setGone(R.id.layout_receiving_doctor, false);
                helper.setGone(R.id.layout_receiving_depart, false);
                helper.setGone(R.id.layout_receiving_hospital, false);
                break;
            case TRANSFER_STATUS_RECEIVED:
                helper.setGone(R.id.iv_transfer_status_in, false);
                helper.setGone(R.id.iv_transfer_status_out, true);
                helper.setGone(R.id.layout_receiving_doctor, true);
                helper.setGone(R.id.layout_receiving_depart, true);
                helper.setGone(R.id.layout_receiving_hospital, true);
                helper.setText(R.id.tv_receiving_doctor, item.getTargetDoctorName());
                helper.setText(R.id.tv_receiving_depart, item.getTargetHospitalDepartmentName());
                helper.setText(R.id.tv_receiving_hospital, item.getTargetHospitalName());
                helper.setImageResource(R.id.iv_transfer_status_out, R.mipmap.ic_status_received);
                break;
            case TRANSFER_STATUS_CANCEL:
                helper.setGone(R.id.iv_transfer_status_in, false);
                helper.setGone(R.id.iv_transfer_status_out, true);
                helper.setGone(R.id.layout_receiving_doctor, false);
                helper.setGone(R.id.layout_receiving_depart, false);
                helper.setGone(R.id.layout_receiving_hospital, false);
                helper.setImageResource(R.id.iv_transfer_status_out, R.mipmap.ic_status_cancel);
                break;
            case TRANSFER_STATUS_REFUSE:
                helper.setGone(R.id.iv_transfer_status_in, false);
                helper.setGone(R.id.layout_receiving_doctor, false);
                helper.setGone(R.id.layout_receiving_depart, false);
                helper.setGone(R.id.layout_receiving_hospital, false);
                helper.setGone(R.id.iv_transfer_status_out, true);
                helper.setImageResource(R.id.iv_transfer_status_out, R.mipmap.ic_status_reject);
                break;
            default:
                break;
        }
    }
}
