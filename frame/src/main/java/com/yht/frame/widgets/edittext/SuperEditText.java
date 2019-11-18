package com.yht.frame.widgets.edittext;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.yht.frame.R;
import com.yht.frame.utils.BaseUtils;

/**
 * @author Carson_Ho
 * @date 17/8/4
 */
public class SuperEditText extends AppCompatEditText {
    /**
     * 删除图标
     */
    private Drawable icDelete;
    /**
     * 删除键宽高
     */
    private int deleteWidth = 18, deleteHeight = 18;

    public SuperEditText(Context context) {
        super(context);
    }

    public SuperEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SuperEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * 步骤1：初始化属性
     */
    private void init(Context context) {
        //===================删除图标================
        // 1. 根据资源ID获取图标资源（转化成Drawable对象）
        icDelete = ContextCompat.getDrawable(context, R.mipmap.ic_delete);
        // 2. 设置图标大小  起点(x，y)、宽= left_width、高 = left_height
        icDelete.setBounds(0, 0, BaseUtils.dp2px(context,deleteWidth), BaseUtils.dp2px(context,deleteHeight));
    }

    /**
     * 复写EditText本身的方法：onTextChanged（）
     * 调用时刻：当输入框内容变化时
     */
    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        setDeleteIconVisible(hasFocus() && text.length() > 0);
    }

    /**
     * 复写EditText本身的方法：onFocusChanged（）
     * 调用时刻：焦点发生变化时
     */
    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if (focused) {
            setSelection(getText().toString().length());
        }
        setDeleteIconVisible(focused && length() > 0);
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
                        ondeleteClickListener.onDeleteClick();
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
     * 作用：判断是否显示删除图标
     */
    private void setDeleteIconVisible(boolean deleteVisible) {
        setCompoundDrawables(null, null, deleteVisible ? icDelete : null, null);
        //设置图片和text之间的间距
        setCompoundDrawablePadding(BaseUtils.dp2px(getContext(), 10));
        invalidate();
    }

    private OnDeleteClickListener ondeleteClickListener;

    public void setOnDeleteClickListener(OnDeleteClickListener ondeleteClickListener) {
        this.ondeleteClickListener = ondeleteClickListener;
    }

    public interface OnDeleteClickListener {
        void onDeleteClick();
    }
}

