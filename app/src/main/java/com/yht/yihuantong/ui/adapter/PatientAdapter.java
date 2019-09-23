package com.yht.yihuantong.ui.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yht.frame.data.bean.PatientBean;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.utils.glide.GlideHelper;
import com.yht.yihuantong.R;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @author 顿顿
 * @date 19/6/5 14:25
 * @des 居民适配器
 */
public class PatientAdapter extends BaseQuickAdapter<PatientBean, BaseViewHolder> {
    public PatientAdapter(int layoutResId, @Nullable List<PatientBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PatientBean item) {
        String label = StringUtils.join(item.getTagList(), "，");
        helper.setText(R.id.tv_patient_name, item.getName())
              .setText(R.id.tv_label, label)
              .addOnClickListener(R.id.iv_patient_call);
        Glide.with(mContext)
             .load(item.getPhoto())
             .apply(GlideHelper.getOptions(BaseUtils.dp2px(mContext, 4)))
             .into((ImageView)helper.getView(R.id.iv_patient_img));
    }
}
