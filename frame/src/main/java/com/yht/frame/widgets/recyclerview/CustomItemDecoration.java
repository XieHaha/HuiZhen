package com.yht.frame.widgets.recyclerview;

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
import android.text.TextUtils;
import android.view.View;

import com.yht.frame.R;
import com.yht.frame.data.bean.PatientBean;
import com.yht.frame.utils.BaseUtils;

import java.util.List;

/**
 * @author MQ
 * @date 2017/5/8
 */
public class CustomItemDecoration extends RecyclerView.ItemDecoration {
    private Paint mPaint;
    private List<PatientBean> patientBeans;
    private static final int DIVIDER_HEIGHT = 80;
    private Context mContext;
    private final Rect mBounds = new Rect();
    private String tagsStr;

    public void setDatas(List<PatientBean> mBeans, String tagsStr) {
        this.patientBeans = mBeans;
        this.tagsStr = tagsStr;
    }

    public CustomItemDecoration(Context mContext) {
        this.mContext = mContext;
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
            if (patientBeans == null || patientBeans.size() == 0 || patientBeans.size() <= position || position < 0) {
                continue;
            }
            if (position == 0) {
                //第一条数据有bar
                drawTitleBar(canvas, parent, child, patientBeans.get(position),
                             tagsStr.indexOf(patientBeans.get(position).getIndexTag()));
            }
            else if (position > 0) {
                if (TextUtils.isEmpty(patientBeans.get(position).getIndexTag())) { continue; }
                //与上一条数据中的tag不同时，该显示bar了
                if (!patientBeans.get(position).getIndexTag().equals(patientBeans.get(position - 1).getIndexTag())) {
                    drawTitleBar(canvas, parent, child, patientBeans.get(position),
                                 tagsStr.indexOf(patientBeans.get(position).getIndexTag()));
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
        if (position == 0) {
            return;
        }
        final int left = 0;
        final int right = parent.getWidth();
        //返回一个包含Decoration和Margin在内的Rect
        parent.getDecoratedBoundsWithMargins(child, mBounds);
        final int top = mBounds.top;
        final int bottom = mBounds.top + Math.round(ViewCompat.getTranslationY(child)) + DIVIDER_HEIGHT;
        mPaint.setColor(Color.WHITE);
        canvas.drawRect(left, top, right, bottom, mPaint);
        mPaint.setTextSize(40);
        mPaint.setColor(ContextCompat.getColor(mContext, R.color.color_373d4d));
        Typeface font = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD);
        mPaint.setTypeface(font);
        canvas.drawText(bean.getIndexTag(), BaseUtils.dp2px(mContext, 30), bottom - DIVIDER_HEIGHT / 4, mPaint);
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
        if (position == 0) {
            return;
        }
        if (patientBeans == null || patientBeans.size() == 0 || patientBeans.size() <= position || position < 0) {
            return;
        }
        final int bottom = parent.getPaddingTop() + DIVIDER_HEIGHT;
        mPaint.setColor(Color.WHITE);
        canvas.drawRect(parent.getLeft(), parent.getPaddingTop(), parent.getRight() - parent.getPaddingRight(),
                        parent.getPaddingTop() + DIVIDER_HEIGHT, mPaint);
        mPaint.setTextSize(40);
        mPaint.setColor(ContextCompat.getColor(mContext, R.color.color_373d4d));
        Typeface font = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD);
        mPaint.setTypeface(font);
        canvas.drawText(patientBeans.get(position).getIndexTag(), BaseUtils.dp2px(mContext, 30),
                        bottom - DIVIDER_HEIGHT / 4, mPaint);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        if (patientBeans == null || patientBeans.size() == 0 || patientBeans.size() <= position || position < 0) {
            super.getItemOffsets(outRect, view, parent, state);
            return;
        }
        if (position == 0) {
            //第一条数据有bar
            //            outRect.set(0, DIVIDER_HEIGHT, 0, 0);
        }
        else if (position > 0) {
            if (TextUtils.isEmpty(patientBeans.get(position).getIndexTag())) { return; }
            //与上一条数据中的tag不同时，该显示bar了
            if (!patientBeans.get(position).getIndexTag().equals(patientBeans.get(position - 1).getIndexTag())) {
                outRect.set(0, DIVIDER_HEIGHT, 0, 0);
            }
        }
    }
}
