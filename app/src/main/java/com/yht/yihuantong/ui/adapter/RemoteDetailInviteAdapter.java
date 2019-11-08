package com.yht.yihuantong.ui.adapter;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yht.frame.data.bean.RemoteInvitedBean;
import com.yht.frame.data.type.InvitedPartyStatus;
import com.yht.frame.data.type.RemoteOrderStatus;
import com.yht.frame.widgets.textview.JustifiedTextView;
import com.yht.frame.widgets.view.CenterImageSpan;
import com.yht.frame.widgets.view.ExpandableLayout;
import com.yht.yihuantong.R;

import java.util.List;

/**
 * @author 顿顿
 * @date 19/6/10 12:18
 * @description 会诊详情受邀方
 */
public class RemoteDetailInviteAdapter extends BaseQuickAdapter<RemoteInvitedBean, BaseViewHolder>
        implements RemoteOrderStatus, InvitedPartyStatus {
    private int orderStatus;
    /**
     * 受邀方状态
     */
    private Bitmap bitmapReceived, bitmapWait, bitmapRefused;

    public RemoteDetailInviteAdapter(int layoutResId, @Nullable List<RemoteInvitedBean> data) {
        super(layoutResId, data);
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void setBitmap(Bitmap bitmapWait, Bitmap bitmapReceived, Bitmap bitmapRefused) {
        this.bitmapReceived = bitmapReceived;
        this.bitmapWait = bitmapWait;
        this.bitmapRefused = bitmapRefused;
    }

    @Override
    protected void convert(BaseViewHolder helper, RemoteInvitedBean item) {
        helper.setText(R.id.tv_doctor_name, item.getDoctorName()).setText(R.id.tv_reason, item.getRejectReason());
        LinearLayout layoutRefuseReason = helper.getView(R.id.layout_refuse_reason);
        RelativeLayout layoutAdvice = helper.getView(R.id.layout_advice);
        ExpandableLayout expandableLayout = helper.getView(R.id.layout_other);
        JustifiedTextView tvResult = helper.getView(R.id.tv_result);
        TextView tvHospitalAndDepart = helper.getView(R.id.tv_depart_hospital);
        TextView tvAdviceBtn = helper.getView(R.id.tv_advice_btn);
        tvHospitalAndDepart.setText(
                String.format(mContext.getString(R.string.txt_joiner), item.getHospitalDepartmentName(),
                              item.getHospitalName() + " "));
        //订单状态为待审核、被拒绝 不显示受邀方状态
        if (orderStatus == REMOTE_ORDER_STATUS_REVIEW_REFUSE || orderStatus == REMOTE_ORDER_STATUS_UNDER_REVIEW) {
            helper.setGone(R.id.layout_refuse_reason, false).setGone(R.id.layout_advice, false);
        }
        else {
            tvHospitalAndDepart.append(appendImage(item.getStatus(),
                                                   String.format(mContext.getString(R.string.txt_joiner),
                                                                 item.getHospitalDepartmentName(),
                                                                 item.getHospitalName() + " ")));
            switch (item.getStatus()) {
                case INVITED_PARTY_STATUS_RECEIVED:
                case INVITED_PARTY_STATUS_WAIT:
                    layoutRefuseReason.setVisibility(View.GONE);
                    //显示会诊意见
                    if (item.isResultStatus()) {
                        layoutAdvice.setVisibility(View.VISIBLE);
                        if (tvAdviceBtn.isSelected()) {
                            expandableLayout.expand();
                        }
                        else {
                            expandableLayout.collapse();
                        }
                        if (TextUtils.isEmpty(item.getResult())) {
                            tvResult.setText(R.string.txt_consultation_advice_none);
                        }
                        else { tvResult.setText(item.getResult()); }
                        tvAdviceBtn.setOnClickListener(v -> {
                            tvAdviceBtn.setSelected(!tvAdviceBtn.isSelected());
                            notifyDataSetChanged();
                        });
                    }
                    else {
                        layoutAdvice.setVisibility(View.GONE);
                    }
                    break;
                case INVITED_PARTY_STATUS_REFUSED:
                    //显示拒绝原因
                    layoutRefuseReason.setVisibility(View.VISIBLE);
                    layoutAdvice.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 添加状态图
     */
    private SpannableString appendImage(int status, String showText) {
        CenterImageSpan imgSpan;
        switch (status) {
            case INVITED_PARTY_STATUS_RECEIVED:
                imgSpan = new CenterImageSpan(mContext, bitmapReceived);
                break;
            case INVITED_PARTY_STATUS_WAIT:
                imgSpan = new CenterImageSpan(mContext, bitmapWait);
                break;
            case INVITED_PARTY_STATUS_REFUSED:
                imgSpan = new CenterImageSpan(mContext, bitmapRefused);
                break;
            default:
                imgSpan = new CenterImageSpan(mContext, bitmapReceived);
                break;
        }
        SpannableString spanString = new SpannableString(showText);
        spanString.setSpan(imgSpan, 0, showText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanString;
    }
}
