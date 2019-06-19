package com.yht.frame.widgets.recyclerview.loadview;

import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.yht.frame.R;

/**
 * @author BlingBling
 * @date 2016/10/15
 */
public final class CustomLoadMoreView extends LoadMoreView {
    @Override
    public int getLayoutId() {
        return R.layout.view_load_more;
    }

    @Override
    protected int getLoadingViewId() {
        return R.id.load_more_loading_view;
    }

    @Override
    protected int getLoadFailViewId() {
        return R.id.load_more_load_fail_view;
    }

    @Override
    protected int getLoadEndViewId() {
        return R.id.load_more_load_end_view;
    }
}
