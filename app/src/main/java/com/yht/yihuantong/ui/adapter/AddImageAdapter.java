package com.yht.yihuantong.ui.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yht.frame.data.bean.NormImage;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.utils.glide.GlideHelper;
import com.yht.yihuantong.R;
import com.yht.yihuantong.utils.ImageUrlUtil;

import java.util.List;

/**
 * @author 顿顿
 * @date 19/6/5 14:25
 * @des 图片加载
 */
public class AddImageAdapter extends BaseQuickAdapter<NormImage, BaseViewHolder> {
    public AddImageAdapter(int layoutResId, @Nullable List<NormImage> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, NormImage item) {
        //两个都为空
        if (TextUtils.isEmpty(item.getImagePath()) && TextUtils.isEmpty(item.getImageUrl())) {
            helper.setVisible(R.id.iv_delete, false);
            Glide.with(mContext)
                 .load("")
                 .apply(GlideHelper.getOptionsPic(BaseUtils.dp2px(mContext, 4)))
                 .into((ImageView)helper.getView(R.id.iv_upload));
        }
        else {
            if (TextUtils.isEmpty(item.getImagePath())) {
                Glide.with(mContext)
                     .load(ImageUrlUtil.addTokenToUrl(item.getImageUrl()))
                     .apply(GlideHelper.getOptionsPic(BaseUtils.dp2px(mContext, 4)))
                     .into((ImageView)helper.getView(R.id.iv_upload));
            }
            else {
                Glide.with(mContext)
                     .load(item.getImagePath())
                     .apply(GlideHelper.getOptionsPic(BaseUtils.dp2px(mContext, 4)))
                     .into((ImageView)helper.getView(R.id.iv_upload));
            }
            helper.setVisible(R.id.iv_delete, true).addOnClickListener(R.id.iv_delete);
        }
    }
}
