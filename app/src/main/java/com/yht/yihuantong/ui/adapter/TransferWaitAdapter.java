package com.yht.yihuantong.ui.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yht.frame.data.base.TransferBean;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.utils.glide.GlideHelper;
import com.yht.yihuantong.R;
import com.yht.yihuantong.utils.FileUrlUtil;

import java.util.List;

/**
 * @author 顿顿
 * @date 19/6/5 14:25
 * @des 待处理转诊订单
 */
public class TransferWaitAdapter extends BaseQuickAdapter<TransferBean, BaseViewHolder> {
    public TransferWaitAdapter(int layoutResId, @Nullable List<TransferBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TransferBean item) {
        Glide.with(mContext)
             .load(FileUrlUtil.addTokenToUrl(item.getWxPhoto()))
             .apply(GlideHelper.getOptions(BaseUtils.dp2px(mContext, 4)))
             .into((ImageView)helper.getView(R.id.iv_transfer_img));
        helper.setText(R.id.txt_transfer_time, item.getTransferDate())
              .setText(R.id.tv_transfer_name, item.getPatientName())
              .setText(R.id.tv_transfer_purpose, item.getTransferTarget())
              .setText(R.id.tv_transfer_doctor, item.getSourceDoctorName())
              .setText(R.id.tv_transfer_depart, item.getSourceHospitalDepartmentName())
              .setText(R.id.tv_transfer_hospital, item.getSourceHospitalName())
              .addOnClickListener(R.id.iv_transfer_img);
    }
}
