package com.yht.frame.widgets.recyclerview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.yht.frame.utils.BaseUtils;

/**
 * @author MQ
 * @date 2017/5/17
 */
public class SideBar extends View {
    private int mHeight;
    private int mWidth;
    private Paint mPaint;
    private int singleHeight;
    private Context mContext;
    private IndexChangeListener listener;
    private final int TOTAL_MARGIN = 80;
    private final int TOP_MARGIN = 20;

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
        mHeight = (h - BaseUtils.dp2px(mContext, TOTAL_MARGIN));
        mWidth = w;
        singleHeight = BaseUtils.dp2px(mContext, 20);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < indexStr.length(); i++) {
            String textTag = indexStr.substring(i, i + 1);
            float xPos = (mWidth - mPaint.measureText(textTag)) / 2;
            canvas.drawText(textTag, xPos, singleHeight * (i + 1) + BaseUtils.dp2px(mContext, TOP_MARGIN), mPaint);
        }
    }

    private String indexStr = "ABCDEFGHIJKLMNOPQRSTUVWXY#";

    public void setIndexStr(String indexStr) {
        this.indexStr = indexStr;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //按下
                mPaint.setColor(Color.BLACK);
                invalidate();
            case MotionEvent.ACTION_MOVE:
                //滑动 event.getY()得到在父View中的Y坐标，通过和总高度的比例再乘以字符个数总长度得到按下的位置  200为margintop距离
                int position = (int)((event.getY() - getTop() - BaseUtils.dp2px(mContext, 20) + 200) / mHeight *
                                     indexStr.toCharArray().length);
                if (position >= 0 && position < indexStr.length()) {
                    ((IndexBar)getParent()).setDrawData(event.getY(), String.valueOf(indexStr.toCharArray()[position]),
                                                        position);
                    if (listener != null) {
                        listener.indexChanged(indexStr.substring(position, position + 1));
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                //抬起
                ((IndexBar)getParent()).setTagStatus(false);
                mPaint.setColor(Color.GRAY);
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