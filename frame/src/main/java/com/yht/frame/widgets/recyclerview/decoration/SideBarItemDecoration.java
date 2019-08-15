package com.yht.frame.widgets.recyclerview.decoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yht.frame.R;
import com.yht.frame.data.bean.PatientBean;
import com.yht.frame.utils.BaseUtils;

import java.util.List;

/**
 * @author MQ
 * @date 2017/5/8
 */
public class SideBarItemDecoration extends RecyclerView.ItemDecoration {
    private Paint mPaint;
    private List<PatientBean> patientBeans;
    private static final int DIVIDER_HEIGHT = 80;
    private Context mContext;
    private final Rect mBounds = new Rect();
    private String titleBar;
    /**
     * 是否有头部
     * 默认有
     */
    private boolean HasHeader = true;

    public void setHasHeader(boolean hasHeader) {
        HasHeader = hasHeader;
    }

    public void setDatas(List<PatientBean> mBeans, String tagsStr) {
        this.patientBeans = mBeans;
        this.titleBar = tagsStr;
    }

    public SideBarItemDecoration(Context mContext) {
        this.mContext = mContext;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(ContextCompat.getColor(mContext, R.color.color_373d4d));
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
            if (HasHeader) {
                realPosition = position - 1;
            }
            else {
                realPosition = position;
            }
            if (patientBeans == null || patientBeans.size() == 0 || patientBeans.size() <= realPosition ||
                realPosition < 0) {
                continue;
            }
            PatientBean bean = patientBeans.get(realPosition);
            if (realPosition == 0) {
                drawTitleBar(canvas, parent, child, bean, titleBar.indexOf(bean.getIndexTag()));
            }
            else {
                //与上一条数据中的tag不同时，该显示bar了
                if (!bean.getIndexTag().equals(patientBeans.get(realPosition - 1).getIndexTag())) {
                    drawTitleBar(canvas, parent, child, bean, titleBar.indexOf(bean.getIndexTag()));
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
    private void drawTitleBar(Canvas canvas, RecyclerView parent, View child, PatientBean bean, int position) {
        final int left = 0;
        final int right = parent.getWidth();
        //返回一个包含Decoration和Margin在内的Rect
        parent.getDecoratedBoundsWithMargins(child, mBounds);
        final int top = mBounds.top;
        final int bottom = mBounds.top + Math.round(ViewCompat.getTranslationY(child)) + DIVIDER_HEIGHT;
        mPaint.setColor(Color.WHITE);
        canvas.drawRect(left, top, right, bottom, mPaint);
        mPaint.setTextSize(BaseUtils.sp2px(mContext, 14));
        mPaint.setColor(ContextCompat.getColor(mContext, R.color.color_373d4d));
        Typeface font = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD);
        mPaint.setTypeface(font);
        canvas.drawText(bean.getIndexTag(), BaseUtils.dp2px(mContext, 26), bottom - BaseUtils.dp2px(mContext, 5),
                        mPaint);
    }

    /**
     * 顶部悬浮窗
     *
     * @param canvas
     * @param parent
     * @param state
     */
    @Override
    public void onDrawOver(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        //用来绘制悬浮框
        int position = ((LinearLayoutManager)(parent.getLayoutManager())).findFirstVisibleItemPosition();
        // 第一条数据是头部 不需要bar, 直接从第二条数据开始
        int realPosition;
        if (HasHeader) {
            realPosition = position - 1;
        }
        else {
            realPosition = position;
        }
        if (patientBeans == null || patientBeans.size() == 0 || patientBeans.size() <= realPosition ||
            realPosition < 0) {
            return;
        }
        final int bottom = parent.getPaddingTop() + DIVIDER_HEIGHT;
        mPaint.setColor(Color.WHITE);
        canvas.drawRect(parent.getLeft(), parent.getPaddingTop(), parent.getRight() - parent.getPaddingRight(),
                        bottom / 2 * 3, mPaint);
        mPaint.setTextSize(BaseUtils.sp2px(mContext, 14));
        mPaint.setColor(ContextCompat.getColor(mContext, R.color.color_373d4d));
        Typeface font = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD);
        mPaint.setTypeface(font);
        //悬浮窗字母位置
        canvas.drawText(patientBeans.get(realPosition).getIndexTag(), BaseUtils.dp2px(mContext, 26),
                        bottom - BaseUtils.dp2px(mContext, 5), mPaint);
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
        // 第一条数据是头部 不需要bar, 直接从第二条数据开始
        int realPosition;
        if (HasHeader) {
            realPosition = position - 1;
        }
        else {
            realPosition = position;
        }
        if (patientBeans == null || patientBeans.size() == 0 || patientBeans.size() <= realPosition ||
            realPosition < 0) {
            super.getItemOffsets(outRect, view, parent, state);
            return;
        }
        if (realPosition == 0) {
            outRect.set(0, DIVIDER_HEIGHT, 0, 0);
        }
        else {
            //与上一条数据中的tag不同时，该显示bar了
            if (!patientBeans.get(realPosition)
                             .getIndexTag()
                             .equals(patientBeans.get(realPosition - 1).getIndexTag())) {
                outRect.set(0, DIVIDER_HEIGHT, 0, 0);
            }
        }
    }
}
