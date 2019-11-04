package com.yht.frame.widgets.dialog.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yht.frame.R;
import com.yht.frame.data.BaseData;
import com.yht.frame.data.bean.ServiceSubmitErrorBean;
import com.yht.frame.data.type.CurrencyDetailType;
import com.yht.frame.utils.BaseUtils;

import java.util.List;

/**
 * @author 顿顿
 * @date 19/6/10 12:18
 * @description 收入记录
 */
public class DialogListAdapter extends BaseQuickAdapter<ServiceSubmitErrorBean, BaseViewHolder>
        implements CurrencyDetailType {
    /**
     * 1、服务项状态   2、服务项价格
     */
    private int type;

    public DialogListAdapter(int layoutResId, @Nullable List<ServiceSubmitErrorBean> data) {
        super(layoutResId, data);
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    protected void convert(BaseViewHolder helper, ServiceSubmitErrorBean item) {
        helper.setText(R.id.tv_content,
                       String.format(mContext.getString(R.string.txt_numbering), helper.getAdapterPosition() + 1,
                                     item.getName()));
        if (type == BaseData.BASE_TWO) {
            helper.setGone(R.id.layout_price, true)
                  .setText(R.id.tv_old_price, String.format(mContext.getString(R.string.txt_price),
                                                            BaseUtils.getPrice(item.getOldPrice())))
                  .setText(R.id.tv_new_price, String.format(mContext.getString(R.string.txt_price),
                                                            BaseUtils.getPrice(item.getNewPrice())));
        }
        else {
            helper.setGone(R.id.layout_price, false);
        }
    }
}
