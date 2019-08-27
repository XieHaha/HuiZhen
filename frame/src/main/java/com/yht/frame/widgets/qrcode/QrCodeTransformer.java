package com.yht.frame.widgets.qrcode;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * @author 顿顿
 * @date 19/8/14 11:05
 * @description
 */
public class QrCodeTransformer implements ViewPager.PageTransformer {
    private Context context;

    public QrCodeTransformer(Context context) {
        this.context = context;
    }

    @Override
    public void transformPage(@NonNull View page, float position) {
    }
}
