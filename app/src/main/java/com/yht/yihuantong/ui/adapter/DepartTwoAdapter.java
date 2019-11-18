package com.yht.yihuantong.ui.adapter;

import androidx.annotation.Nullable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yht.frame.data.bean.HospitalDepartChildBean;
import com.yht.yihuantong.R;

import java.util.List;

/**
 * @author 顿顿
 * @date 19/6/5 14:25
 * @des
 */
public class DepartTwoAdapter extends BaseQuickAdapter<HospitalDepartChildBean, BaseViewHolder> {
    private int curPosition = -1;

    public DepartTwoAdapter(int layoutResId, @Nullable List<HospitalDepartChildBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HospitalDepartChildBean item) {
        TextView textView = helper.getView(R.id.tv_depart);
        textView.setText(item.getDepartmentName());
        if (curPosition == helper.getAdapterPosition()) {
            textView.setSelected(true);
        }
        else {
            textView.setSelected(false);
        }
    }

    public void setCurPosition(int curPosition) {
        this.curPosition = curPosition;
        notifyDataSetChanged();
    }
}
