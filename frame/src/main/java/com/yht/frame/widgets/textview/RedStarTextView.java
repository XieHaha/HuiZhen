package com.yht.frame.widgets.textview;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import androidx.appcompat.widget.AppCompatTextView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.Gravity;

import com.yht.frame.R;

/**
 * @创建者 顿顿
 * @创建时间 ${DATA} 16:56
 * @描述 自带星号
 */
public class RedStarTextView extends AppCompatTextView {
    /**
     * 特殊符号默认为*号
     */
    private String starType = "* ";
    /**
     * 默认颜色
     */
    private int starColor = Color.RED;

    public RedStarTextView(Context context) {
        super(context);
        init(context, null);
    }

    public RedStarTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RedStarTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RedStarTextView);
        starType = ta.getString(R.styleable.RedStarTextView_starType);
        if (TextUtils.isEmpty(starType)) {
            starType = "* ";
        }
        starColor = ta.getInteger(R.styleable.RedStarTextView_starColor, Color.RED);
        String text = ta.getString(R.styleable.RedStarTextView_android_text);
        ta.recycle();
        setGravity(Gravity.CENTER_VERTICAL);
        setText(text);
    }

    public void setText(String text) {
        Spannable span = new SpannableString(starType + text);
        span.setSpan(new ForegroundColorSpan(starColor), 0, starType.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        setText(span);

    }
}
