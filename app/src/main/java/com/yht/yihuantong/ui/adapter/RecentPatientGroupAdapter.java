package com.yht.yihuantong.ui.adapter;

import android.content.Intent;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.yht.frame.data.BaseData;
import com.yht.frame.data.CommonData;
import com.yht.frame.data.bean.PatientBean;
import com.yht.frame.data.bean.RecentPatientTitleBean;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.utils.TimeUtil;
import com.yht.frame.utils.glide.GlideHelper;
import com.yht.frame.widgets.dialog.HintDialog;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ui.patient.ChatContainerActivity;

import java.util.List;

/**
 * @author 顿顿
 * @date 19/6/5 14:25
 * @description 最近添加的居民
 */
public class RecentPatientGroupAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder>
        implements BaseData {
    public RecentPatientGroupAdapter(List<MultiItemEntity> data) {
        super(data);
        addItemType(BASE_ZERO, R.layout.item_recent_patient_group);
        addItemType(BASE_ONE, R.layout.item_recent_patient);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultiItemEntity item) {
        switch (helper.getItemViewType()) {
            case BASE_ZERO:
                RecentPatientTitleBean bean = (RecentPatientTitleBean)item;
                helper.setText(R.id.tv_title, bean.getTitle())
                      .setImageResource(R.id.iv_tab, bean.isExpanded() ? R.mipmap.ic_solid_up : R.mipmap.ic_solid_down);
                helper.itemView.setOnClickListener(v -> {
                    int pos = helper.getAdapterPosition();
                    if (bean.isExpanded()) {
                        collapse(pos);
                    }
                    else {
                        expand(pos);
                    }
                });
                break;
            case BASE_ONE:
                PatientBean patientBean = (PatientBean)item;
                helper.setText(R.id.tv_patient_name, patientBean.getName())
                      .setText(R.id.tv_time, TimeUtil.getTimeString(
                              BaseUtils.date2TimeStamp(patientBean.getAddTime(), BaseUtils.YYYY_MM_DD_HH_MM)));
                Glide.with(mContext)
                     .load(patientBean.getPhoto())
                     .apply(GlideHelper.getOptions(BaseUtils.dp2px(mContext, 4)))
                     .into((ImageView)helper.getView(R.id.iv_patient_img));
                helper.itemView.setOnClickListener(v -> {
                    Intent intent = new Intent(mContext, ChatContainerActivity.class);
                    intent.putExtra(CommonData.KEY_CHAT_ID, patientBean.getCode());
                    intent.putExtra(CommonData.KEY_CHAT_NAME, patientBean.getName());
                    mContext.startActivity(intent);
                });
                ImageView imageView = helper.getView(R.id.iv_patient_call);
                imageView.setOnClickListener(
                        v -> new HintDialog(mContext).setPhone(mContext.getString(R.string.txt_contact_patient_phone),
                                                               patientBean.getMobile())
                                                     .setOnEnterClickListener(() -> callPhone(patientBean.getMobile()))
                                                     .show());
                break;
            default:
                break;
        }
    }

    /**
     * 拨打电话（跳转到拨号界面，用户手动点击拨打）
     *
     * @param phoneNum 电话号码
     */
    public void callPhone(String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        mContext.startActivity(intent);
    }
}
