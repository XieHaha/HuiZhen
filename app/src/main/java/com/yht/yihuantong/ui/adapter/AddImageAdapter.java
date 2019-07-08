package com.yht.yihuantong.ui.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yht.frame.utils.BaseUtils;
import com.yht.yihuantong.R;
import com.yht.frame.utils.glide.GlideHelper;

import java.util.List;

/**
 * @author 顿顿
 * @date 19/6/5 14:25
 * @des 预约检查记录
 */
public class AddImageAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public AddImageAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        Glide.with(mContext)
             .load(item)
             .apply(GlideHelper.getOptionsPic(BaseUtils.dp2px(mContext, 4)))
             .into((ImageView)helper.getView(R.id.iv_upload));
        if (!TextUtils.isEmpty(item)) {
            helper.setVisible(R.id.iv_delete, true).addOnClickListener(R.id.iv_delete);
        }
        else {
            helper.setVisible(R.id.iv_delete, false);
        }
    }
}
