package com.yht.frame.widgets.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.yht.frame.R;
import com.yht.frame.widgets.dialog.listener.OnCancelClickListener;
import com.yht.frame.widgets.dialog.listener.OnEnterClickListener;

/**
 * @author 顿顿
 * @date 19/6/24 18:13
 * @des
 */
public class InputDialog implements View.OnClickListener {
    private Context context;
    private TextView tvEnter, tvCancel, tvTitle, tvContent;
    /**
     * 初始值
     */
    private String titleString = "提示", enterString = "确定", cancelString = "取消";
    private EditText etContent;
    /**
     * 确认按钮颜色控制
     */
    private boolean enterSelect = false;
    private Dialog dialog;

    public InputDialog(Context context) {
        this.context = context;
    }

    public InputDialog Builder() {
        // 定义Dialog布局和参数
        dialog = new Dialog(context, R.style.normal_dialog);
        dialog.setContentView(R.layout.dialog_input);
        tvTitle = dialog.findViewById(R.id.dialog_simple_hint_title);
        etContent = dialog.findViewById(R.id.dialog_simple_hint_content);
        tvCancel = dialog.findViewById(R.id.dialog_simple_hint_cancel);
        tvEnter = dialog.findViewById(R.id.dialog_simple_hint_enter);
        tvCancel.setOnClickListener(this);
        tvEnter.setOnClickListener(this);
        return this;
    }

    /**
     * @param titleString 标题
     */
    public InputDialog setTitleString(String titleString) {
        this.titleString = titleString;
        return this;
    }

    /**
     * 设置确定按钮的文本
     */
    public InputDialog setEnterBtnTxt(String str) {
        this.enterString = str;
        return this;
    }

    public InputDialog setEnterSelect(boolean enterSelect) {
        this.enterSelect = enterSelect;
        return this;
    }

    /**
     * 设置取消按钮的文本
     */
    public InputDialog setCancleBtnTxt(String str) {
        this.cancelString = str;
        return this;
    }

    /**
     * 获取密码输入内容
     *
     * @param watcher
     * @return
     */
    public InputDialog setResultListener(final ResultListener watcher) {
        return this;
    }

    public InputDialog setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }

    public InputDialog setCanceledOnTouchOutside(boolean cancel) {
        dialog.setCanceledOnTouchOutside(cancel);
        return this;
    }

    @Override
    public void onClick(View v) {
        if (v == tvEnter) {
            if (onEnterClickListener != null) {
                onEnterClickListener.onEnter();
            }
            dismiss();
        }
        else if (v == tvCancel) {
            if (onCancelClickListener != null) {
                onCancelClickListener.onCancel();
            }
            dismiss();
        }
    }

    public void show() {
        //显示键盘
        if (android.os.Build.VERSION.SDK_INT < 14) {
            InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            if (etContent != null) {
                imm.showSoftInput(etContent, InputMethodManager.SHOW_IMPLICIT);
            }
        }
        else {
            showIMEOtherWay(etContent);
        }
        tvTitle.setText(titleString);
        tvEnter.setText(enterString);
        tvEnter.setSelected(enterSelect);
        tvCancel.setText(cancelString);
        dialog.show();
    }

    /**
     * 其他方式显示键盘
     *
     * @param view
     */
    public static void showIMEOtherWay(final View view) {
        (new Handler()).postDelayed(() -> {
            view.dispatchTouchEvent(
                    MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN,
                                       0, 0, 0));
            view.dispatchTouchEvent(
                    MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, 0,
                                       0, 0));
        }, 200);
    }

    public void dismiss() {
        dialog.dismiss();
    }

    public interface ResultListener {
        void onResult(String result);
    }

    private OnEnterClickListener onEnterClickListener = null;
    private OnCancelClickListener onCancelClickListener = null;

    public InputDialog setOnEnterClickListener(OnEnterClickListener onEnterClickListener) {
        this.onEnterClickListener = onEnterClickListener;
        return this;
    }

    public InputDialog setOnCancelClickListener(OnCancelClickListener onCancelClickListener) {
        this.onCancelClickListener = onCancelClickListener;
        return this;
    }
}
