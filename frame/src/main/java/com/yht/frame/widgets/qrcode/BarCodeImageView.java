package com.yht.frame.widgets.qrcode;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.yht.frame.api.ThreadPoolHelper;

/**
 * @author zhangrj
 * @date 2017/4/21
 */
public class BarCodeImageView extends AppCompatImageView {
    /**
     * 生成二维码的字符串
     */
    private String content;
    /**
     * 二维码中间添加的图案
     */
    private int resId;

    public BarCodeImageView(Context context) {
        super(context);
    }

    public BarCodeImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BarCodeImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed && !TextUtils.isEmpty(content)) {
            ThreadPoolHelper.getInstance().execInCached(() -> {
                Bitmap logoBm = null;
                if (resId > 0) {
                    logoBm = BitmapFactory.decodeResource(getResources(), resId);
                }
                final Bitmap bitmap = QrCodeHelper.createQRImage(content, getWidth(), getHeight(), logoBm);
                if (bitmap != null) {
                    post(() -> setImageBitmap(bitmap));
                }
            });
        }
    }
}
