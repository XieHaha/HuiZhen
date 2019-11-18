package com.yht.frame.widgets.textview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.Gravity;

import com.yht.frame.R;
import com.yht.frame.utils.BaseUtils;

/**
 * @author 顿顿
 * @date 19/10/17 15:36
 * @description 垂直
 */
public class VerticalTextView extends AppCompatTextView {
    private Paint paint;
    private String text = "请居民或家属签名";
    private float width, height;

    public VerticalTextView(Context context) {
        super(context);
        init();
    }

    public VerticalTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public VerticalTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(BaseUtils.sp2px(getContext(), 16));
        paint.setColor(ContextCompat.getColor(getContext(), R.color.color_373d4d));
        setGravity(Gravity.CENTER);
        setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.corner28_dfe2e6_alpha_bg));
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //旋转90度
        canvas.rotate(90);
        canvas.drawText(text, BaseUtils.dp2px(getContext(), 20), -BaseUtils.dp2px(getContext(), 16), paint);
        canvas.save();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        width = BaseUtils.dp2px(getContext(), 44);
        height = paint.measureText(text) + BaseUtils.dp2px(getContext(), 40);
        setMeasuredDimension((int)width, (int)height);
    }
}
