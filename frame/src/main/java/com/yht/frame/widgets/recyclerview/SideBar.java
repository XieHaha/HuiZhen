package com.yht.frame.widgets.recyclerview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.yht.frame.R;
import com.yht.frame.data.BaseData;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.utils.HuiZhenLog;
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
     * 标题栏高度
     */
    private int titleBarHeight;
    /**
     * value
     */
    private String indexStr = "";
    private Bitmap bitmap;

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
        height = ScreenUtils.getScreenHeight(mContext);
        mPaint = new Paint();
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);
        mPaint.setColor(ContextCompat.getColor(mContext, R.color.color_373d4d));
        mPaint.setTextSize(BaseUtils.sp2px(mContext, 14));
        //扫描线 bitmap
        Drawable drawable = ContextCompat.getDrawable(getContext(), R.mipmap.ic_search);
        bitmap = ((BitmapDrawable)drawable).getBitmap();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //导航栏居中显示，上下各有80dp的边距
        //        mHeight = (h - marginTotal);
        mWidth = w;
        singleHeight = BaseUtils.dp2px(mContext, 20);
        mHeight = singleHeight * indexStr.length();
        //屏幕高度/2 - sidebar高度/2 + margintop的高度
        startHeight = height / 2 - mHeight / 2 - titleBarHeight;
        HuiZhenLog.i("ZYC", " height:" + height + "  mHeight:" + mHeight + " startHeight:" + startHeight);
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
                if (BaseData.BASE_SEARCH_TAG.equals(textTag)) {
                    canvas.drawBitmap(bitmap, xPos - BaseUtils.dp2px(mContext, 3), startHeight, mPaint);
                }
                else {
                    canvas.drawText(textTag, xPos, singleHeight * (i + 1) + startHeight, mPaint);
                }
            }
        }
        else {
            //清空数据
            canvas.drawText("", 0, 0, mPaint);
        }
    }

    public void setIndexStr(String indexStr, int titleBarHeight) {
        this.titleBarHeight = titleBarHeight;
        this.indexStr = indexStr;
        if (mHeight == 0) {
            mHeight = singleHeight * indexStr.length();
            startHeight = height / 2 - mHeight / 2 - titleBarHeight;
            HuiZhenLog.i("ZYC-1", " height:" + height + "  mHeight:" + mHeight + " startHeight:" + startHeight);
        }
        invalidate();
    }

    public void setCurPosition(int curPosition) {
        this.curPosition = curPosition;
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
                break;
            default:
                break;
        }
        return true;
    }

    public interface IndexChangeListener {
        /**
         * 改变
         *
         * @param tag tag
         */
        void indexChanged(String tag);

        /**
         * 显示
         *
         * @param y        显示位置
         * @param tag      tag
         * @param position p
         */
        void indexShow(float y, String tag, int position);

        /**
         * 隐藏
         */
        void indexHide();
    }

    public void setIndexChangeListener(IndexChangeListener listener) {
        this.listener = listener;
    }
}
