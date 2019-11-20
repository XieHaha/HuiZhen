package com.yht.yihuantong.ui.adapter;

import android.text.TextUtils;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yht.yihuantong.R;

import java.util.List;

/**
 * @author 顿顿
 * @date 19/6/5 14:25
 * @des 医院筛选
 */
public class SelectCheckTypeFilterAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    /**
     * 本院
     */
    private String hospitalName;
    private String curSelected;

    public SelectCheckTypeFilterAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        TextView name = helper.getView(R.id.tv_hospital_name);
        name.setText(item);
        if (TextUtils.equals(hospitalName, item)) {
            name.append(mContext.getText(R.string.txt_our_hospital));
        }
        boolean show = TextUtils.equals(item, curSelected);
        name.setSelected(show);
        helper.setVisible(R.id.iv_hook, show);
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public void setCurSelected(String curSelected) {
        this.curSelected = curSelected;
    }
}
