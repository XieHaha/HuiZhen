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
import com.yht.yihuantong.utils.ImageUrlUtil;

import java.util.List;

/**
 * @author 顿顿
 * @date 19/6/5 14:25
 * @des 已接收转诊
 */
public class TransferReceivedAdapter extends BaseQuickAdapter<TransferBean, BaseViewHolder> {
    public TransferReceivedAdapter(int layoutResId, @Nullable List<TransferBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TransferBean item) {
        Glide.with(mContext)
             .load(ImageUrlUtil.append(item.getConfirmPhoto()))
             .apply(GlideHelper.getOptionsPic(BaseUtils.dp2px(mContext, 4)))
             .into((ImageView)helper.getView(R.id.iv_receiving));
        helper.setText(R.id.tv_reserve_visit_time, item.getAppointAt())
              .setText(R.id.tv_receiving_name, item.getPatientName())
              .setText(R.id.tv_receiving_sex, item.getPatientSex())
              .setText(R.id.tv_receiving_age, String.valueOf(item.getPatientAge()))
              .addOnClickListener(R.id.iv_receiving_call);
    }
}
