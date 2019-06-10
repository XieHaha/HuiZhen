package com.zyc.doctor.ui.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @author 顿顿
 * @date 19/6/10 12:18
 * @des 个人中心历史记录
 */
public class PersonalHistoryAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public PersonalHistoryAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
    }
}
