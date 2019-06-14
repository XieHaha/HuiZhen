package com.yht.frame.widgets.edittext;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.yht.frame.R;

/**
 * @author Carson_Ho
 * @date 17/8/4
 */
public class SuperEditText extends AppCompatEditText {
    /**
     * 定义属性变量
     * 画笔
     */
    private Paint mPaint;
    /**
     * 删除图标
     */
    private Drawable icDelete;
    /**
     * 左侧图标（点击 & 未点击）
     */
    private Drawable icLeftClick, icLeftNoClick;
    /**
     * 分割线变量
     * 点击时 & 未点击颜色
     */
    private int lineColorClick, lineColorNoClick;
    private int color;
    private int linePosition;

    public SuperEditText(Context context) {
        super(context);
    }

    public SuperEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SuperEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    /**
     * 步骤1：初始化属性
     */
    private void init(Context context, AttributeSet attrs) {
        // 获取控件资源
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SuperEditText);
        boolean hide = typedArray.getBoolean(R.styleable.SuperEditText_hide_left_icon, false);
        if (!hide) {
            // ==============a. 点击状态的左侧图标================
            // 1. 获取资源ID
            int icLeftClickResID = typedArray.getResourceId(R.styleable.SuperEditText_ic_left_click,
                                                            R.mipmap.ic_delete);
            // 2. 根据资源ID获取图标资源（转化成Drawable对象）
            icLeftClick = ContextCompat.getDrawable(context, icLeftClickResID);
            // 3. 设置图标大小
            // 起点(x，y)、宽= left_width、高 = left_height
            int leftX = typedArray.getInteger(R.styleable.SuperEditText_left_x, 0);
            int leftY = typedArray.getInteger(R.styleable.SuperEditText_left_y, 0);
            int leftWidth = typedArray.getInteger(R.styleable.SuperEditText_left_width, 60);
            int leftHeight = typedArray.getInteger(R.styleable.SuperEditText_left_height, 60);
            icLeftClick.setBounds(leftX, leftY, leftWidth, leftHeight);
            // Drawable.setBounds(x,y,width,height) = 设置Drawable的初始位置、宽和高等信息
            // x = 组件在容器X轴上的起点、y = 组件在容器Y轴上的起点、width=组件的长度、height = 组件的高度
            // ==================b. 未点击状态的左侧图标=================
            // 1. 获取资源ID
            int icLeftNoClickResID = typedArray.getResourceId(R.styleable.SuperEditText_ic_left_unclick,
                                                              R.mipmap.ic_delete);
            // 2. 根据资源ID获取图标资源（转化成Drawable对象）
            // 3. 设置图标大小（此处默认左侧图标点解 & 未点击状态的大小相同）
            icLeftNoClick = ContextCompat.getDrawable(context, icLeftNoClickResID);
            icLeftNoClick.setBounds(leftX, leftY, leftWidth, leftHeight);
        }
        //===================删除图标================
        // 1. 获取资源ID
        int icDeleteResID = typedArray.getResourceId(R.styleable.SuperEditText_ic_delete, R.mipmap.ic_delete);
        // 2. 根据资源ID获取图标资源（转化成Drawable对象）
        icDelete = ContextCompat.getDrawable(context, icDeleteResID);
        // 3. 设置图标大小
        // 起点(x，y)、宽= left_width、高 = left_height
        int deleteX = typedArray.getInteger(R.styleable.SuperEditText_delete_x, 0);
        int deleteY = typedArray.getInteger(R.styleable.SuperEditText_delete_y, 0);
        int deleteWidth = typedArray.getInteger(R.styleable.SuperEditText_delete_width, 60);
        int deleteHeight = typedArray.getInteger(R.styleable.SuperEditText_delete_height, 60);
        icDelete.setBounds(deleteX, deleteY, deleteWidth, deleteHeight);
        setCompoundDrawables(icLeftNoClick, null, null, null);
        // 1. 设置画笔
        mPaint = new Paint();
        // 分割线粗细
        mPaint.setStrokeWidth(1.0f);
        // 2. 设置分割线颜色（使用十六进制代码，如#333、#8e8e8e）
        int lineColorClickDefault = ContextCompat.getColor(context, R.color.color_d8d8d8);
        int lineColorNoClickDefault = ContextCompat.getColor(context, R.color.color_d8d8d8);
        lineColorClick = typedArray.getColor(R.styleable.SuperEditText_lineColor_click, lineColorClickDefault);
        lineColorNoClick = typedArray.getColor(R.styleable.SuperEditText_lineColor_unclick, lineColorNoClickDefault);
        color = lineColorNoClick;
        // 分割线默认颜色 = 灰色
        mPaint.setColor(lineColorNoClick);
        // 3. 分割线位置
        linePosition = typedArray.getInteger(R.styleable.SuperEditText_linePosition, 1);
        typedArray.recycle();
    }

    /**
     * 复写EditText本身的方法：onTextChanged（）
     * 调用时刻：当输入框内容变化时
     */
    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        setDeleteIconVisible(hasFocus() && text.length() > 0, hasFocus());
    }

    /**
     * 复写EditText本身的方法：onFocusChanged（）
     * 调用时刻：焦点发生变化时
     */
    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        setDeleteIconVisible(focused && length() > 0, focused);
    }

    /**
     * 作用：对删除图标区域设置为"点击 即 清空搜索框内容"
     * 原理：当手指抬起的位置在删除图标的区域，即视为点击了删除图标 = 清空搜索框内容
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 原理：当手指抬起的位置在删除图标的区域，即视为点击了删除图标 = 清空搜索框内容
        switch (event.getAction()) {
            // 判断动作 = 手指抬起时
            case MotionEvent.ACTION_UP:
                Drawable drawable = icDelete;
                if (hasFocus() && drawable != null && event.getX() <= (getWidth() - getPaddingRight()) &&
                    event.getX() >= (getWidth() - getPaddingRight() - drawable.getBounds().width())) {
                    setText("");
                    if (ondeleteClickListener != null) {
                        ondeleteClickListener.OnDeleteClick();
                    }
                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 关注1
     * 作用：判断是否显示删除图标 & 设置分割线颜色
     */
    private void setDeleteIconVisible(boolean deleteVisible, boolean leftVisible) {
        setCompoundDrawables(leftVisible ? icLeftClick : icLeftNoClick, null, deleteVisible ? icDelete : null, null);
        color = leftVisible ? lineColorClick : lineColorNoClick;
        invalidate();
    }

    /**
     * 作用：绘制分割线
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(color);
        // 绘制分割线
        // 需要考虑：当输入长度超过输入框时，所画的线需要跟随着延伸
        // 解决方案：线的长度 = 控件长度 + 延伸后的长度
        // 获取延伸后的长度
        int x = this.getScrollX();
        // 获取控件长度
        int w = this.getMeasuredWidth();
        // 传入参数时，线的长度 = 控件长度 + 延伸后的长度
        //        if (getBackground() == null) {
        //            canvas.drawLine(0, this.getMeasuredHeight() - linePosition, w + x, this.getMeasuredHeight() - linePosition,
        //                            mPaint);
        //        }
    }

    private OndeleteClickListener ondeleteClickListener;

    public void setOndeleteClickListener(OndeleteClickListener ondeleteClickListener) {
        this.ondeleteClickListener = ondeleteClickListener;
    }

    public interface OndeleteClickListener {
        void OnDeleteClick();
    }
}

