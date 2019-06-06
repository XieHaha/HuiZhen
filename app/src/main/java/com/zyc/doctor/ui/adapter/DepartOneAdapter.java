package com.zyc.doctor.ui.adapter;

import android.support.annotation.Nullable;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zyc.doctor.R;

import java.util.List;

/**
 * @author 顿顿
 * @date 19/6/5 14:25
 * @des
 */
public class DepartOneAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    private int curPosition = -1;

    public DepartOneAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        RelativeLayout relativeLayout = helper.getView(R.id.layout_depart);
        if (curPosition == helper.getAdapterPosition()) {
            relativeLayout.setSelected(true);
        }
        else {
            relativeLayout.setSelected(false);
        }
        helper.setText(R.id.tv_depart, item);
    }

    public void setCurPosition(int curPosition) {
        this.curPosition = curPosition;
        notifyDataSetChanged();
    }
}
