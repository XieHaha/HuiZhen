package com.yht.yihuantong.ui.adapter;

import androidx.annotation.Nullable;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yht.frame.data.bean.HospitalDepartBean;
import com.yht.yihuantong.R;

import java.util.List;

/**
 * @author 顿顿
 * @date 19/6/5 14:25
 * @des
 */
public class DepartOneAdapter extends BaseQuickAdapter<HospitalDepartBean, BaseViewHolder> {
    private int curPosition = -1;

    public DepartOneAdapter(int layoutResId, @Nullable List<HospitalDepartBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HospitalDepartBean item) {
        RelativeLayout relativeLayout = helper.getView(R.id.layout_depart);
        if (curPosition == helper.getAdapterPosition()) {
            relativeLayout.setSelected(true);
        }
        else {
            relativeLayout.setSelected(false);
        }
        helper.setText(R.id.tv_depart, item.getDepartmentName());
    }

    public void setCurPosition(int curPosition) {
        this.curPosition = curPosition;
        notifyDataSetChanged();
    }
}
