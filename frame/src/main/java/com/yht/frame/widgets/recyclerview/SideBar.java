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
import com.yht.frame.utils.ScreenUtils;

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
     * 屏幕总高度
     */
    private int height;
    /**
     * 绘制的起始高度
     */
    private int startHeight;
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
        height = ScreenUtils.getScreenHeight(mContext);
        mPaint = new Paint();
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.GRAY);
        mPaint.setTextSize(BaseUtils.sp2px(mContext, 14));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //导航栏居中显示，上下各有80dp的边距
        //        mHeight = (h - marginTotal);
        mWidth = w;
        singleHeight = marginTop;
        mHeight = singleHeight * indexStr.length();
        //屏幕高度/2 - sidebar高度/2 + margintop的高度 - 标题栏高度
        startHeight = height / 2 - mHeight / 2 + marginTop - (marginTotal - marginTop);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!TextUtils.isEmpty(indexStr)) {
            for (int i = 0; i < indexStr.length(); i++) {
                String textTag = indexStr.substring(i, i + 1);
                float xPos = (mWidth - mPaint.measureText(textTag)) / 2;
                if (i == curPosition) {
                    mPaint.setColor(ContextCompat.getColor(getContext(), R.color.color_1491fc));
                }
                else {
                    mPaint.setColor(ContextCompat.getColor(getContext(), R.color.color_373d4d));
                }
                canvas.drawText(textTag, xPos, singleHeight * (i + 1) + startHeight, mPaint);
            }
        }
        else {
            //清空数据
            canvas.drawText("", 0, 0, mPaint);
        }
    }

    public void setIndexStr(String indexStr) {
        this.indexStr = indexStr;
        invalidate();
    }

    private int curPosition = -1;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                invalidate();
            case MotionEvent.ACTION_MOVE:
                //滑动 event.getY()得到在父View中的Y坐标，通过和总高度的比例再乘以字符个数总长度得到按下的位置
                float y = event.getY() - startHeight;
                if (y > 0) {
                    curPosition = (int)(y / mHeight * indexStr.toCharArray().length);
                    if (curPosition >= 0 && curPosition < indexStr.length()) {
                        if (listener != null) {
                            listener.indexChanged(indexStr.substring(curPosition, curPosition + 1));
                            listener.indexShow((height - mHeight) / 2f,
                                               String.valueOf(indexStr.toCharArray()[curPosition]), curPosition);
                        }
                    }
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                //抬起
                if (listener != null) {
                    listener.indexHide();
                }
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

        void indexShow(float y, String tag, int position);

        void indexHide();
    }

    public void setIndexChangeListener(IndexChangeListener listener) {
        this.listener = listener;
    }
}
