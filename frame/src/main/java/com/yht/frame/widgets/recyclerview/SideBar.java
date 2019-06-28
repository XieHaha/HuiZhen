package com.yht.frame.widgets.recyclerview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.yht.frame.R;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.utils.LogUtils;

/**
 * @author MQ
 * @date 2017/5/17
 */
public class SideBar extends View {
    private Context mContext;
    private Paint mPaint;
    private int mWidth, mHeight;
    private IndexChangeListener listener;
    /**
     * 单个字符高度
     */
    private int singleHeight;
    /**
     * dp值
     */
    private final int TOTAL_MARGIN = 80, TOP_MARGIN = 20;
    /**
     * px值
     */
    private int marginTop, marginTotal;
    /**
     * value
     */
    private String indexStr = "";

    public SideBar(Context context) {
        this(context, null);
    }

    public SideBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SideBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    private void init() {
        marginTop = BaseUtils.dp2px(mContext, TOP_MARGIN);
        marginTotal = BaseUtils.dp2px(mContext, TOTAL_MARGIN);
        mPaint = new Paint();
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.GRAY);
        mPaint.setTextSize(35);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //导航栏居中显示，上下各有80dp的边距
        mHeight = (h - marginTotal);
        mWidth = w;
        singleHeight = marginTop;
        mHeight = singleHeight * indexStr.length();
        LogUtils.i("test", "mWidth:" + mWidth + "  mHeight:" + mHeight + "  singleHeight:" + singleHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (TextUtils.isEmpty(indexStr)) {
            canvas.drawText("", 0, 0, mPaint);
        }
        for (int i = 0; i < indexStr.length(); i++) {
            String textTag = indexStr.substring(i, i + 1);
            float xPos = (mWidth - mPaint.measureText(textTag)) / 2;
            if (i == curPosition) {
                mPaint.setColor(ContextCompat.getColor(getContext(), R.color.color_1491fc));
            }
            else {
                mPaint.setColor(ContextCompat.getColor(getContext(), R.color.color_373d4d));
            }
            canvas.drawText(textTag, xPos, singleHeight * (i + 1) + marginTop, mPaint);
        }
    }

    public void setIndexStr(String indexStr) {
        this.indexStr = indexStr;
    }

    private int curPosition = -1;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                invalidate();
            case MotionEvent.ACTION_MOVE:
                //滑动 event.getY()得到在父View中的Y坐标，通过和总高度的比例再乘以字符个数总长度得到按下的位置
                float y = event.getY() - getTop() - marginTop + 200;
                if (y > 0) {
                    curPosition = (int)(y / mHeight * indexStr.toCharArray().length);
                    if (curPosition >= 0 && curPosition < indexStr.length()) {
                        /**
                         * 绘制浮窗圆
                         */
                        //                        ((IndexBar)getParent()).setDrawData(event.getY(),
                        //                                                            String.valueOf(indexStr.toCharArray()[curPosition]),
                        //                                                            curPosition);
                        if (listener != null) {
                            listener.indexChanged(indexStr.substring(curPosition, curPosition + 1));
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                //抬起
                ((IndexBar)getParent()).setTagStatus(false);
                mPaint.setColor(ContextCompat.getColor(getContext(), R.color.color_373d4d));
                curPosition = -1;
                invalidate();
                break;
            default:
                break;
        }
        return true;
    }

    public interface IndexChangeListener {
        void indexChanged(String tag);
    }

    public void setIndexChangeListener(IndexChangeListener listener) {
        this.listener = listener;
    }
}
