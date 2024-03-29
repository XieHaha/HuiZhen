package com.yht.yihuantong.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yht.frame.data.BaseData;
import com.yht.frame.data.bean.CheckBean;
import com.yht.frame.data.bean.CheckTypeBean;
import com.yht.frame.data.type.ServiceOrderStatus;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.utils.glide.GlideHelper;
import com.yht.yihuantong.R;
import com.yht.yihuantong.utils.FileUrlUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 顿顿
 * @date 19/6/5 14:25
 * @des 预约检查记录
 */
public class ServiceHistoryAdapter extends BaseQuickAdapter<CheckBean, BaseViewHolder> implements ServiceOrderStatus {
    /**
     * 最大展示条数
     */
    private int MAX_SHOW_NUN = 3;

    public ServiceHistoryAdapter(int layoutResId, @Nullable List<CheckBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CheckBean item) {
        Glide.with(mContext)
                .load(FileUrlUtil.addTokenToUrl(item.getPatientPhoto()))
                .apply(GlideHelper.getOptions(BaseUtils.dp2px(mContext, 4)))
                .into((ImageView) helper.getView(R.id.iv_check_img));
        helper.setText(R.id.tv_check_name, item.getPatientName())
                .setText(R.id.tv_check_hospital, item.getTargetHospitalName())
                .addOnClickListener(R.id.iv_check_img);
        int status = item.getStatus();
        switch (status) {
            case CHECK_ORDER_STATUS_INCOMPLETE:
                helper.setImageResource(R.id.iv_check_status_in, R.mipmap.ic_tag_check_incomplete);
                break;
            case CHECK_ORDER_STATUS_COMPLETE:
                helper.setImageResource(R.id.iv_check_status_in, R.mipmap.ic_tag_complete);
                break;
            case CHECK_ORDER_STATUS_CANCEL:
                helper.setImageResource(R.id.iv_check_status_in, R.mipmap.ic_tag_cancel);
                break;
            default:
                break;
        }
        LinearLayout layout = helper.getView(R.id.layout_check_type);
        layout.removeAllViews();
        addView(helper, layout, item);
    }

    private void addView(BaseViewHolder helper, LinearLayout layout, CheckBean item) {
        ArrayList<CheckTypeBean> checkTypeList = item.getCheckList();
        if (checkTypeList == null) {
            return;
        }
        //最多展示三条数据 超过三天显示查看更多
        if (checkTypeList.size() > MAX_SHOW_NUN) {
            helper.setVisible(R.id.tv_look_more, true).setGone(R.id.tv_space, false);
        } else {
            helper.setGone(R.id.tv_look_more, false).setVisible(R.id.tv_space, true);
        }
        for (int i = 0; i < checkTypeList.size(); i++) {
            if (i == MAX_SHOW_NUN) {
                break;
            }
            CheckTypeBean bean = checkTypeList.get(i);
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_check_type, null);
            TextView serviceName = view.findViewById(R.id.tv_check_type_name);
            if (bean.getItemType() == BaseData.BASE_ONE) {
                serviceName.setText(bean.getName());
            } else {
                serviceName.setText(bean.getPackName());
            }
            layout.addView(view);
        }
    }
}
