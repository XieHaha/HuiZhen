package com.yht.frame.widgets.dialog.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yht.frame.R;
import com.yht.frame.data.bean.DepartInfoBean;

import java.util.List;

/**
 * @author 顿顿
 * @date 19/6/10 12:18
 * @description 收入记录
 */
public class ErrorDepartAdapter extends BaseQuickAdapter<DepartInfoBean, BaseViewHolder> {
    public ErrorDepartAdapter(int layoutResId, @Nullable List<DepartInfoBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DepartInfoBean item) {
        helper.setText(R.id.tv_content,
                       String.format(mContext.getString(R.string.txt_joiner), item.getHospitalDepartmentName(),
                                     item.getHospitalName()));
    }
}
