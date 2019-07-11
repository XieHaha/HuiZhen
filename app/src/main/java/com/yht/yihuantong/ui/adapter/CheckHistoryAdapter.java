package com.yht.yihuantong.ui.adapter;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yht.frame.data.CheckTypeStatus;
import com.yht.frame.data.OrderStatus;
import com.yht.frame.data.base.CheckBean;
import com.yht.frame.data.base.CheckTypeBean;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.utils.glide.GlideHelper;
import com.yht.yihuantong.R;
import com.yht.yihuantong.utils.ImageUrlUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 顿顿
 * @date 19/6/5 14:25
 * @des 预约检查记录
 */
public class CheckHistoryAdapter extends BaseQuickAdapter<CheckBean, BaseViewHolder> implements OrderStatus {
    public CheckHistoryAdapter(int layoutResId, @Nullable List<CheckBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CheckBean item) {
        Glide.with(mContext)
             .load(ImageUrlUtil.append(item.getPatientPhoto()))
             .apply(GlideHelper.getOptions(BaseUtils.dp2px(mContext, 4)))
             .into((ImageView)helper.getView(R.id.iv_check_img));
        helper.setText(R.id.tv_check_name, item.getPatientName())
              .setText(R.id.tv_check_hospital, item.getTargetHospitalName());
        int status = item.getStatus();
        switch (status) {
            case ORDER_STATUS_INCOMPLETE:
                helper.setGone(R.id.iv_check_status_in, true);
                helper.setGone(R.id.iv_check_status_out, false);
                break;
            case ORDER_STATUS_COMPLETE:
                helper.setGone(R.id.iv_check_status_in, false);
                helper.setGone(R.id.iv_check_status_out, true);
                helper.setImageResource(R.id.iv_transfer_status_out, R.mipmap.ic_status_complete);
                break;
            case ORDER_STATUS_CANCEL:
                helper.setGone(R.id.iv_check_status_in, false);
                helper.setGone(R.id.iv_check_status_out, true);
                helper.setImageResource(R.id.iv_transfer_status_out, R.mipmap.ic_status_cancel);
                break;
            default:
                break;
        }
        LinearLayout layout = helper.getView(R.id.layout_check_type);
        addView(layout, item);
    }

    private void addView(LinearLayout layout, CheckBean item) {
        ArrayList<CheckTypeBean> checkTypeList = item.getCheckList();
        if (layout.getChildCount() > 0 || checkTypeList == null) {
            return;
        }
        for (int i = 0; i < checkTypeList.size(); i++) {
            CheckTypeBean bean = checkTypeList.get(i);
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_check_type, null);
            TextView textView = view.findViewById(R.id.tv_check_type_name);
            ImageView imageDot = view.findViewById(R.id.iv_check_type_dot);
            ImageView imageView = view.findViewById(R.id.iv_check_type_status);
            textView.setText(bean.getName());
            if (bean.getStatus() == CheckTypeStatus.CHECK_STATUS_CANCEL) {
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
