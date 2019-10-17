package com.yht.frame.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.yht.frame.widgets.textview.VerticalTextView;

/**
 * @author dundun
 */
public class ToastUtil {
    private static Toast toast = null;
    private static Toast verticalToast = null;

    private ToastUtil() {
    }

    public static void toast(Context context, String msg) {
        if (msg == null || msg.isEmpty()) {
            return;
        }
        if (toast == null) {
            toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
        }
        else {
            toast.setText(msg);
        }
        toast.show();
    }

    public static void toast(Context context, int resId) {
        if (toast == null) {
            toast = Toast.makeText(context, resId, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
        }
        else {
            toast.setText(resId);
        }
        toast.show();
    }

    public static void toast(Context context, int resId, int type) {
        if (verticalToast == null) {
            verticalToast = new Toast(context);
        }
        TextView textView = new VerticalTextView(context);
        textView.setText(resId);
        verticalToast.setView(textView);
        verticalToast.setGravity(Gravity.CENTER, 0, 0);
        verticalToast.show();
    }
}
