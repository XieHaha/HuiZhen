package com.yht.frame.widgets.recyclerview;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * @author 顿顿
 * @date 19/6/20 17:02
 * @des
 */
public class ControlScrollLayoutManager extends LinearLayoutManager {
    private RecyclerView recyclerView;
    /**
     * 默认不可以自动滑动，可通过setCanAutoScroll(boolean)来设置
     */
    private boolean mCanAutoScroll;

    public ControlScrollLayoutManager(Context context, RecyclerView recyclerView) {
        this(context, VERTICAL, false, recyclerView);
    }

    public ControlScrollLayoutManager(Context context, int orientation, boolean reverseLayout,
            RecyclerView recyclerView) {
        super(context, orientation, reverseLayout);
        this.recyclerView = recyclerView;
    }

    public void setCanAutoScroll(boolean canAutoScroll) {
        mCanAutoScroll = canAutoScroll;
    }

    @Override
    public int scrollVerticallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int consumedY = 0;
        // Do not let auto scroll
        if (mCanAutoScroll || recyclerView == null ||
            recyclerView.getScrollState() != RecyclerView.SCROLL_STATE_SETTLING) {
            consumedY = super.scrollVerticallyBy(dx, recycler, state);
        }
        return consumedY;
    }
}
