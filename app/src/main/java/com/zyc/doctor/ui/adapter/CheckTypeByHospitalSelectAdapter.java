package com.zyc.doctor.ui.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zyc.doctor.R;

import java.util.List;

/**
 * @author 顿顿
 * @date 19/6/5 14:25
 * @des 医院下检查项目 搜索适配器
 */
public class CheckTypeByHospitalSelectAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public CheckTypeByHospitalSelectAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_check_type_name, item);
    }
}
