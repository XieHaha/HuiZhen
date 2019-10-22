package com.yht.yihuantong.ui.reservation.time;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.MonthView;
import com.yht.frame.utils.BaseUtils;
import com.yht.yihuantong.R;

/**
 * @author huanghaibin
 * @date 2017/11/15
 */
public class SimpleMonthView extends MonthView {
    /**
     * 选中状态
     */
    private int mRadius;
    /**
     * 标记当天的小红点
     */
    private int pointPadding, pointRadius;
    /**
     * 背景圆点
     */
    private Paint mPointPaint = new Paint();

    public SimpleMonthView(Context context) {
        super(context);
        //兼容硬件加速无效的代码
        setLayerType(View.LAYER_TYPE_SOFTWARE, mSelectedPaint);
        mPointPaint.setAntiAlias(true);
        mPointPaint.setStyle(Paint.Style.FILL);
        mPointPaint.setTextAlign(Paint.Align.CENTER);
        mPointPaint.setColor(ContextCompat.getColor(context, R.color.color_fb495e));
    }

    @Override
    protected void onPreviewHook() {
        mRadius = Math.min(mItemWidth, mItemHeight) / 2;
        //标记当天的小红点
        pointPadding = BaseUtils.dp2px(getContext(), 6);
        pointRadius = BaseUtils.dp2px(getContext(), 2);
        mSchemePaint.setStyle(Paint.Style.STROKE);
        mSchemePaint.setTextSize(BaseUtils.sp2px(getContext(), 16));
    }

    @Override
    protected void onLoopStart(int x, int y) {
    }

    @Override
    protected boolean onDrawSelected(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme) {
        int cx = x + mItemWidth / 2;
        int cy = y + mItemHeight / 2;
        canvas.drawCircle(cx, cy, mRadius, mSelectedPaint);
        return false;
    }

    @Override
    protected void onDrawScheme(Canvas canvas, Calendar calendar, int x, int y) {
        //        int cx = x + mItemWidth / 2;
        //        int cy = y + mItemHeight / 2;
        //        canvas.drawCircle(cx, cy, mRadius, mSchemePaint);
    }

    @Override
    protected void onDrawText(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme, boolean isSelected) {
        float baselineY = mTextBaseLine + y;
        int cx = x + mItemWidth / 2;
        if (calendar.isCurrentDay()) {
            canvas.drawCircle(x + mItemWidth - pointPadding, y + pointRadius / 2f, pointRadius, mPointPaint);
        }
        if (isSelected) {
            canvas.drawText(String.valueOf(calendar.getDay()), cx, baselineY, mSelectTextPaint);
        }
        else if (hasScheme) {
            canvas.drawText(String.valueOf(calendar.getDay()), cx, baselineY, mOtherMonthTextPaint);
        }
        else {
            canvas.drawText(String.valueOf(calendar.getDay()), cx, baselineY, calendar.isCurrentDay()
                                                                              ? mCurDayTextPaint
                                                                              : calendar.isCurrentMonth()
                                                                                ? mCurMonthTextPaint
                                                                                : mOtherMonthTextPaint);
        }
    }
}
