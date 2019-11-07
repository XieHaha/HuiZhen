package com.yht.yihuantong.ui.adapter;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yht.frame.data.bean.InvitDepartBean;
import com.yht.frame.data.bean.RemoteBean;
import com.yht.frame.data.type.RemoteOrderStatus;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.utils.glide.GlideHelper;
import com.yht.yihuantong.R;
import com.yht.yihuantong.utils.FileUrlUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 顿顿
 * @date 19/6/5 14:25
 * @des 远程会诊记录
 */
public class RemoteHistoryAdapter extends BaseQuickAdapter<RemoteBean, BaseViewHolder> implements RemoteOrderStatus {
    public RemoteHistoryAdapter(int layoutResId, @Nullable List<RemoteBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, RemoteBean item) {
        Glide.with(mContext)
             .load(FileUrlUtil.addTokenToUrl(item.getPatientPhoto()))
             .apply(GlideHelper.getOptions(BaseUtils.dp2px(mContext, 4)))
             .into((ImageView)helper.getView(R.id.iv_head_img));
        helper.setText(R.id.tv_name, item.getPatientName())
              .setText(R.id.tv_remote_time, getRemoteTime(item.getStartAt(), item.getEndAt()))
              .addOnClickListener(R.id.iv_head_img);
        int status = item.getStatus();
        switch (status) {
            case REMOTE_ORDER_STATUS_NONE:
                helper.setImageResource(R.id.iv_status, R.mipmap.ic_tag_not_start);
                break;
            case REMOTE_ORDER_STATUS_IN_CONSULTATION:
            case REMOTE_ORDER_STATUS_INTERRUPT:
                helper.setImageResource(R.id.iv_status, R.mipmap.ic_tag_in_consultation);
                break;
            case REMOTE_ORDER_STATUS_ALL_REFUSE:
                helper.setImageResource(R.id.iv_status, R.mipmap.ic_tag_all_refuse);
                break;
            case REMOTE_ORDER_STATUS_COMPLETE:
                helper.setImageResource(R.id.iv_status, R.mipmap.ic_tag_complete);
                break;
            case REMOTE_ORDER_STATUS_CLOSED:
            case REMOTE_ORDER_STATUS_TIMEOUT_CLOSE:
            case REMOTE_ORDER_STATUS_INTERRRUPT_CLOSE:
            case REMOTE_ORDER_STATUS_ALL_REFUSE_CLOSE:
                helper.setImageResource(R.id.iv_status, R.mipmap.ic_tag_closed);
                break;
            case REMOTE_ORDER_STATUS_UNDER_REVIEW:
                helper.setImageResource(R.id.iv_status, R.mipmap.ic_tag_wait_review);
                break;
            case REMOTE_ORDER_STATUS_REVIEW_REFUSE:
                helper.setImageResource(R.id.iv_status, R.mipmap.ic_tag_pass_not);
                break;
            default:
                break;
        }
        LinearLayout layout = helper.getView(R.id.layout_remote_depart);
        layout.removeAllViews();
        addView(layout, item);
    }

    /**
     * 获取远程会诊时间
     */
    private String getRemoteTime(long startAt, long endAt) {
        String date = BaseUtils.formatDate(startAt, BaseUtils.YYYY_MM_DD);
        String startHour = BaseUtils.formatDate(startAt, BaseUtils.HH_MM);
        String endHour = BaseUtils.formatDate(endAt, BaseUtils.HH_MM);
        return date + " " + startHour + "-" + endHour;
    }

    private void addView(LinearLayout layout, RemoteBean item) {
        ArrayList<InvitDepartBean> invitationList = item.getInvitationList();
        if (invitationList == null) {
            return;
        }
        for (int i = 0; i < invitationList.size(); i++) {
            InvitDepartBean bean = invitationList.get(i);
            TextView textView = (TextView)LayoutInflater.from(mContext)
                                                        .inflate(R.layout.item_remote_depart_simple, null);
            textView.setText(String.format(mContext.getString(R.string.txt_joiner), bean.getHospitalDepartmentName(),
                                           bean.getHospitalName()));
            layout.addView(textView);
        }
    }
}
