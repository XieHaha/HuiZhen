package com.yht.frame.widgets.scrollview;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.widget.ScrollView;

import com.yht.frame.R;

/**
 * @author 顿顿
 * @date 19/8/23 18:40
 * @des 自定义高度
 */
public class CustomHeightScrollView extends ScrollView {
    private Context context;
    /**
     * 自定义高度
     */
    private int customHeight;

    public CustomHeightScrollView(Context context) {
        this(context, null);
    }

    public CustomHeightScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomHeightScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CustomHeightScrollView);
        customHeight = (int)array.getDimension(R.styleable.CustomHeightScrollView_customHeight, 200);
        array.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        try {
            //设置控件高度
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(customHeight, MeasureSpec.AT_MOST);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        //重新计算控件高、宽
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
