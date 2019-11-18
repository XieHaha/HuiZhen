package com.yht.frame.widgets.recyclerview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.yht.frame.R;
import com.yht.frame.utils.BaseUtils;

/**
 * @author MQ
 * @date 2017/5/18
 */
public class IndexBar extends ViewGroup {
    private int mHeight, mWidth;
    private Context mContext;
    private Paint mPaint;
    private float centerY;
    private String tag = "";
    private boolean isShowTag;
    private final int circleRadius = 8;

    public IndexBar(Context context) {
        this(context, null);
    }

    public IndexBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndexBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        setWillNotDraw(false);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setDither(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeight = h;
        mWidth = w;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        //MeasureSpec封装了父View传给子View的布局要求
        int wMode = MeasureSpec.getMode(widthMeasureSpec);
        int wSize = MeasureSpec.getSize(widthMeasureSpec);
        int hMode = MeasureSpec.getMode(heightMeasureSpec);
        int hSize = MeasureSpec.getSize(heightMeasureSpec);
        switch (wMode) {
            case MeasureSpec.EXACTLY:
                mWidth = wSize;
                break;
            case MeasureSpec.AT_MOST:
                mWidth = wSize;
                break;
            case MeasureSpec.UNSPECIFIED:
                break;
            default:
                break;
        }
        switch (hMode) {
            case MeasureSpec.EXACTLY:
                mHeight = hSize;
                break;
            case MeasureSpec.AT_MOST:
                mHeight = hSize;
                break;
            case MeasureSpec.UNSPECIFIED:
                break;
            default:
                break;
        }
        setMeasuredDimension(mWidth, mHeight);
    }

    private int childWidth;

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childNum = getChildCount();
        if (childNum <= 0) { return; }
        //得到SideBar
        View childView = getChildAt(0);
        childWidth = childView.getMeasuredWidth();
        //把SideBar排列到最右侧
        childView.layout((mWidth - childWidth), 200, mWidth, mHeight);
    }

    /**
     * @param centerY 要绘制的圆的Y坐标
     * @param tag     要绘制的字母Tag
     */
    public void setDrawData(float centerY, String tag, int position) {
        this.centerY = centerY;
        this.tag = tag;
        isShowTag = true;
        invalidate();
    }

    /**
     * 通过标志位来控制是否来显示圆
     *
     * @param isShowTag 是否显示圆
     */
    public void setTagStatus(boolean isShowTag) {
        this.isShowTag = isShowTag;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isShowTag) {
            //绘制圆和文字
            canvas.drawCircle((mWidth - childWidth) / 2f, centerY, BaseUtils.dp2px(mContext, circleRadius), mPaint);
            mPaint.setColor(ContextCompat.getColor(mContext, R.color.color_373d4d));
            mPaint.setTextSize(BaseUtils.sp2px(mContext, 24));
            canvas.drawText(tag, (mWidth - childWidth - mPaint.measureText(tag)) / 2,
                            centerY - (mPaint.ascent() + mPaint.descent()) / 2, mPaint);
        }
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return super.generateDefaultLayoutParams();
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return super.generateLayoutParams(p);
    }
}
