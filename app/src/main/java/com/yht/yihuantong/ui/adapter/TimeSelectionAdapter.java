package com.yht.yihuantong.ui.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yht.frame.data.bean.TimeBarBean;
import com.yht.yihuantong.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 顿顿
 * @date 19/6/5 14:25
 * @des 时间选择
 */
public class TimeSelectionAdapter extends BaseQuickAdapter<TimeBarBean, BaseViewHolder> {
    private int startPosition;
    private ArrayList<Integer> selectPositions = new ArrayList<>();

    public TimeSelectionAdapter(int layoutResId, @Nullable List<TimeBarBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TimeBarBean item) {
        helper.setText(R.id.tv_hour, item.getHourTxt());
        helper.addOnClickListener(R.id.layout_hour);
        TextView textView = helper.getView(R.id.tv_hour_status);
        if (startPosition >= helper.getAdapterPosition()) {
            textView.setVisibility(View.VISIBLE);
        }
        else {
            textView.setVisibility(View.INVISIBLE);
        }
        int position = helper.getAdapterPosition();
        if (selectPositions.contains(position) && position > startPosition) {
            textView.setVisibility(View.VISIBLE);
            textView.setSelected(true);
        }
        else {
            textView.setSelected(false);
        }
    }

    public void setRange(int startHour) {
        this.startPosition = startHour;
    }

    public void setSelectPositions(ArrayList<Integer> selectPositions) {
        this.selectPositions = selectPositions;
    }
}
