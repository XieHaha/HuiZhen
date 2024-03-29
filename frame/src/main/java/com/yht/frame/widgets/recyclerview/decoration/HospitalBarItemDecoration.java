package com.yht.frame.widgets.recyclerview.decoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.yht.frame.R;
import com.yht.frame.data.bean.SelectCheckTypeBean;
import com.yht.frame.utils.BaseUtils;

import java.util.List;

/**
 * @author MQ
 * @date 2017/5/8
 */
public class HospitalBarItemDecoration extends RecyclerView.ItemDecoration {
    private Paint mPaint;
    private List<SelectCheckTypeBean> patientBeans;
    private static final int DIVIDER_HEIGHT = 80;
    private Context mContext;
    private final Rect mBounds = new Rect();
    private String titleBar;

    public void setDatas(List<SelectCheckTypeBean> mBeans, String tagsStr) {
        this.patientBeans = mBeans;
        this.titleBar = tagsStr;
    }

    public HospitalBarItemDecoration(Context mContext) {
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
            if (patientBeans == null || patientBeans.size() == 0 || patientBeans.size() <= position || position < 0) {
                continue;
            }
            SelectCheckTypeBean bean = patientBeans.get(position);
            if (position == 0) {
                drawTitleBar(canvas, parent, child, bean, titleBar.indexOf(bean.getHospitalCode()));
            }
            else {
                //与上一条数据中的tag不同时，该显示bar了
                if (!bean.getHospitalCode().equals(patientBeans.get(position - 1).getHospitalCode())) {
                    drawTitleBar(canvas, parent, child, bean, titleBar.indexOf(bean.getHospitalCode()));
                }
            }
        }
        canvas.restore();
    }

    /**
     * 绘制bar  Decoration
     */
    private void drawTitleBar(Canvas canvas, RecyclerView parent, View child, SelectCheckTypeBean bean, int position) {
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
        canvas.drawText(bean.getHospitalCode(), BaseUtils.dp2px(mContext, 26), bottom - BaseUtils.dp2px(mContext, 5),
                        mPaint);
    }

    /**
     * 插入 titlebar 空间
     */
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        // 第一条数据是头部 不需要bar, 直接从第二条数据开始
        if (patientBeans == null || patientBeans.size() == 0 || patientBeans.size() <= position || position < 0) {
            super.getItemOffsets(outRect, view, parent, state);
            return;
        }
        if (position == 0) {
            outRect.set(0, DIVIDER_HEIGHT, 0, 0);
        }
        else {
            //与上一条数据中的tag不同时，该显示bar了
            if (!patientBeans.get(position)
                             .getHospitalCode()
                             .equals(patientBeans.get(position - 1).getHospitalCode())) {
                outRect.set(0, DIVIDER_HEIGHT, 0, 0);
            }
        }
    }
}
