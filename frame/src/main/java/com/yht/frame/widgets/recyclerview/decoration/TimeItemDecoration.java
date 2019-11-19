package com.yht.frame.widgets.recyclerview.decoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.yht.frame.R;
import com.yht.frame.utils.BaseUtils;

import java.util.List;

/**
 * @author dundun
 * @date 2019年6月13日
 */
public class TimeItemDecoration extends RecyclerView.ItemDecoration {
    private Paint mPaint;
    private List<String> titleBars;
    private static final int DIVIDER_HEIGHT = 40;
    private Context mContext;
    private final Rect mBounds = new Rect();
    private String titleBar;
    /**
     * 是否有头部
     * 默认没有
     */
    private boolean hasHeader;

    public void setTitleBar(List<String> titleBars, String tagsStr) {
        this.titleBars = titleBars;
        this.titleBar = tagsStr;
    }

    public TimeItemDecoration(Context mContext, boolean hasHeader) {
        this.mContext = mContext;
        this.hasHeader = hasHeader;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        if (parent.getLayoutManager() == null) {
            return;
        }
        canvas.save();
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)child.getLayoutParams();
            int position = params.getViewLayoutPosition();
            //第一条数据为头部，不需要titlebar，
            int realPosition;
            if (hasHeader) {
                realPosition = position - 1;
            }
            else {
                realPosition = position;
            }
            if (titleBars == null || titleBars.size() == 0 || titleBars.size() <= realPosition || realPosition < 0) {
                continue;
            }
            String bar = titleBars.get(realPosition);
            if (realPosition == 0) {
                drawTitleBar(canvas, parent, child, bar, titleBar.indexOf(bar));
            }
            else {
                //与上一条数据中的tag不同时，该显示bar了
                if (!bar.equals(titleBars.get(realPosition - 1))) {
                    drawTitleBar(canvas, parent, child, bar, titleBar.indexOf(bar));
                }
            }
        }
        canvas.restore();
    }

    /**
     * 绘制bar  Decoration
     *
     * @param canvas Canvas
     * @param parent RecyclerView
     * @param child  ItemView
     */
    private void drawTitleBar(Canvas canvas, RecyclerView parent, View child, String bar, int position) {
        //返回一个包含Decoration和Margin在内的Rect
        parent.getDecoratedBoundsWithMargins(child, mBounds);
        final int bottom = mBounds.top + Math.round(ViewCompat.getTranslationY(child)) + DIVIDER_HEIGHT;
        mPaint.setTextSize(BaseUtils.sp2px(mContext,13));
        mPaint.setColor(ContextCompat.getColor(mContext, R.color.color_6a6f80));
        Typeface font = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD);
        mPaint.setTypeface(font);
        canvas.drawText(bar, BaseUtils.dp2px(mContext, 48), bottom, mPaint);
    }

    /**
     * 插入 titlebar 空间
     *
     * @param outRect
     * @param view
     * @param parent
     * @param state
     */
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        //第一条数据为头部，不需要titlebar，
        int realPosition;
        if (hasHeader) {
            realPosition = position - 1;
        }
        else {
            realPosition = position;
        }
        if (titleBars == null || titleBars.size() == 0 || titleBars.size() <= realPosition || realPosition < 0) {
            super.getItemOffsets(outRect, view, parent, state);
            return;
        }
        if (realPosition == 0) {
            outRect.set(0, DIVIDER_HEIGHT, 0, 0);
        }
        else {
            //与上一条数据中的tag不同时，该显示bar了
            if (!titleBars.get(realPosition).equals(titleBars.get(realPosition - 1))) {
                outRect.set(0, DIVIDER_HEIGHT, 0, 0);
            }
        }
    }
}
