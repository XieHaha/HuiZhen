package com.yht.yihuantong.ui.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @author 顿顿
 * @date 19/6/10 12:18
 * @des 收入记录
 */
public class CurrencyIncomeAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public CurrencyIncomeAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
    }
}
