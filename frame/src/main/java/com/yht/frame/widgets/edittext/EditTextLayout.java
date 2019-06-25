package com.yht.frame.widgets.edittext;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yht.frame.R;

/**
 * @author 顿顿
 * @date 19/6/25 11:17
 * @des
 */
public class EditTextLayout extends LinearLayout {
    private EditText mEdittext;
    private ImageView mImageView;
    private Context mContext;
    private float scaleSize;

    public EditTextLayout(Context context) {
        super(context);
        init(context);
    }

    public EditTextLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public EditTextLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        //设置方向
        setOrientation(HORIZONTAL);
        //获取当前设备的屏幕密度
        scaleSize = getScale(context);
        mEdittext = new EditText(mContext);
        mEdittext.setTextColor(ContextCompat.getColor(context, R.color.color_373d4d));
        mEdittext.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        mEdittext.setHint(R.string.txt_select_hospital);
        mEdittext.setHintTextColor(ContextCompat.getColor(context, R.color.color_a1a8b3));
        mEdittext.setBackground(null);
        //充满布局展示
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                                     ViewGroup.LayoutParams.MATCH_PARENT, 1);
        //添加到布局中去
        addView(mEdittext, layoutParams);
        mImageView = new ImageView(mContext);
        //设置资源文件  如果要投入使用最好不要写死，需要后续封装，通过自定义属性设置
        mImageView.setImageResource(R.mipmap.ic_delete);
        //设置ImageView的大小
        LayoutParams layoutParamsImage = new LayoutParams((int)(20 * scaleSize), (int)(20 * scaleSize));
        //设置内边距  如果要投入使用最好不要写死，需要后续封装，通过自定义属性设置
        mImageView.setPadding(5, 5, 5, 5);
        //设置不可见
        mImageView.setVisibility(INVISIBLE);
        //添加到布局中去
        addView(mImageView, layoutParamsImage);
        //设置事件监听
        mImageView.setOnClickListener(view -> mEdittext.getEditableText().clear());
        //添加内容变化监听
        mEdittext.addTextChangedListener(new AbstractTextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //内容不为空的时候显示清除按钮
                if (!TextUtils.isEmpty(charSequence)) {
                    mImageView.setVisibility(VISIBLE);
                }
                else {
                    mImageView.setVisibility(INVISIBLE);
                }
            }
        });
    }

    public EditText getmEdittext() {
        return mEdittext;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        MarginLayoutParams params = null;
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        measureChild(getChildAt(0), widthMeasureSpec - 60, heightMeasureSpec);
        View childOne = getChildAt(0);
        if (heightMode == MeasureSpec.AT_MOST) {
            //ViewGroup的高度为wrap_content,则宽度不需要管，高度为子View的高度和
            int childHeight = Math.max(childOne.getMeasuredHeight(), getSuggestedMinimumHeight());
            setMeasuredDimension(widthSize, childHeight);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        View child = getChildAt(1);
        child.layout(getWidth() - child.getWidth(), getHeight() - child.getHeight(), getWidth(), getHeight());
    }

    /**
     * 获取屏幕缩放比
     *
     * @param context
     * @return
     */
    private float getScale(Context context) {
        TextView tv = new TextView(context);
        tv.setTextSize(1);
        return tv.getTextSize();
    }
}
