package com.yht.frame.widgets.viewpager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @author 顿顿
 * @date 19/8/16 16:11
 * @des 控制是否可以滑动
 */
public class CustomScrollViewPager extends ViewPager {
    /**
     * 是否允许viewpager左右滑动 默认可以
     */
    private boolean scrollEnabled = true;

    public CustomScrollViewPager(@NonNull Context context) {
        super(context);
    }

    public CustomScrollViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setScrollEnabled(boolean scrollEnabled) {
        this.scrollEnabled = scrollEnabled;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.scrollEnabled) {
            return super.onTouchEvent(event);
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (this.scrollEnabled) {
            return super.onInterceptTouchEvent(event);
        }
        return false;
    }
}
