package com.yht.frame.widgets.recyclerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.yht.frame.utils.ScreenUtils;

/**
 * @author 顿顿
 * @date 19/11/1 16:33
 * @description
 */
public class SquareRecyclerView extends RecyclerView {
    public SquareRecyclerView(Context context) {
        super(context);
    }

    public SquareRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int maxHeight = (int)(ScreenUtils.getScreenHeight(getContext()) * 0.6);
        setMeasuredDimension(getMeasuredWidth(), Math.min(maxHeight, getMeasuredHeight()));
    }
}
