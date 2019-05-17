package com.yht.frame.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * @author dundun
 */
public class ToastUtil {
    private static Toast toast = null;

    private ToastUtil() {
    }

    /**
     * @param context
     * @param msg
     */
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

    /**
     * @param context
     * @param resId
     */
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
}
