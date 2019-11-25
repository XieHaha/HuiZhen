package com.yht.yihuantong.ui.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

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
    /**
     * 开始时段
     */
    private int startPosition;
    /**
     * 不可点时段
     */
    private ArrayList<Integer> rangePosition = new ArrayList<>();
    /**
     * 已选时段
     */
    private ArrayList<Integer> selectPositions = new ArrayList<>();

    public TimeSelectionAdapter(int layoutResId, @Nullable List<TimeBarBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TimeBarBean item) {
        helper.setText(R.id.tv_hour, item.getHourTxt());
        helper.addOnClickListener(R.id.layout_hour);
        initRange(helper);
    }

    /**
     * 初始化可选时段范围
     */
    private void initRange(BaseViewHolder helper) {
        int position = helper.getAdapterPosition();
        TextView textView = helper.getView(R.id.tv_hour_status);
        //当前坐标小于开始坐标  或者  当前坐标已限制选择
        if (position < startPosition || rangePosition.contains(position)) {
            textView.setVisibility(View.VISIBLE);
            textView.setSelected(false);
        }
        else {
            if (selectPositions.contains(position)) {
                textView.setVisibility(View.VISIBLE);
                textView.setSelected(true);
            }
            else {
                textView.setVisibility(View.INVISIBLE);
            }
        }
    }

    public void setRange(int startPosition) {
        this.startPosition = startPosition;
    }

    public void setRangePosition(ArrayList<Integer> rangePosition) {
        this.rangePosition = rangePosition;
    }

    public void setSelectPositions(ArrayList<Integer> selectPositions) {
        this.selectPositions = selectPositions;
    }
}
