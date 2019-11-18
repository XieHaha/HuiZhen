package com.yht.yihuantong.ui.adapter;

import androidx.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yht.frame.data.bean.DoctorBean;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.utils.glide.GlideHelper;
import com.yht.yihuantong.R;

import java.util.List;

/**
 * @author 顿顿
 * @date 19/6/5 14:25
 * @description 居民适配器
 */
public class DoctorAdapter extends BaseQuickAdapter<DoctorBean, BaseViewHolder> {
    public DoctorAdapter(int layoutResId, @Nullable List<DoctorBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DoctorBean item) {
        helper.setText(R.id.tv_doctor_name, item.getDoctorName())
              .setText(R.id.tv_title, item.getJobTitle())
              .setText(R.id.tv_depart, item.getDepartmentName());
        Glide.with(mContext)
             .load(item.getPhoto())
             .apply(GlideHelper.getOptions(BaseUtils.dp2px(mContext, 4)))
             .into((ImageView)helper.getView(R.id.iv_doctor_img));
    }
}
