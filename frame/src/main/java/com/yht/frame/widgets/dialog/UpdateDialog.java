package com.yht.frame.widgets.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.TextView;

import com.yht.frame.R;
import com.yht.frame.widgets.dialog.listener.OnCancelClickListener;
import com.yht.frame.widgets.dialog.listener.OnEnterClickListener;

/**
 * @author dundun
 * 版本更新
 */
public class UpdateDialog extends Dialog implements OnClickListener {
    private TextView enter, cancel;
    private TextView title, content;
    private String titleString = "提示";
    private String contentString = "确认合格？";
    private String enterString = "确定";
    private int enterColor = 0;
    private String cancelString = "取消";
    private boolean isShowCancelBtn = true;
    private boolean isShow = false;

    public UpdateDialog(Context context) {
        super(context, R.style.normal_dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //状态栏透明
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.dialog_hint);
        initWidget();
        init();
    }

    private void initWidget() {
        enter = findViewById(R.id.dialog_simple_hint_enter);
        cancel = findViewById(R.id.dialog_simple_hint_cancel);
        title = findViewById(R.id.dialog_simple_hint_title);
        content = findViewById(R.id.dialog_simple_hint_content);
        cancel.setOnClickListener(this);
        enter.setOnClickListener(this);
        setCanceledOnTouchOutside(true);
    }

    private void init() {
        setCancelable(true);
    }

    @Override
    public void onClick(View v) {
        if (v == enter) {
            if (onEnterClickListener != null) {
                onEnterClickListener.onEnter();
            }
            dismiss();
        }
        else if (v == cancel) {
            if (onCancelClickListener != null) {
                onCancelClickListener.onCancel();
            }
            dismiss();
        }
    }

    /**
     * @param titleString 标题
     */
    public UpdateDialog setTitleString(String titleString) {
        this.titleString = titleString;
        return this;
    }

    /**
     * 设置提示语的文本
     *
     * @param contentString 内容
     */
    public UpdateDialog setContentString(String contentString) {
        this.contentString = contentString;
        return this;
    }

    /**
     * 设置确定按钮的文本
     */
    public UpdateDialog setEnterBtnTxt(String str) {
        this.enterString = str;
        return this;
    }

    /**
     * 设置确定按钮的颜色
     */
    public UpdateDialog setEnterTxtColor(int color) {
        this.enterColor = color;
        return this;
    }

    /**
     * 设置取消按钮的文本
     */
    public UpdateDialog setCancleBtnTxt(String str) {
        this.cancelString = str;
        return this;
    }

    /**
     * 设置取消Btn显示/隐藏
     *
     * @param isShowCancelBtn
     */
    public void isShowCancelBtn(boolean isShowCancelBtn) {
        this.isShowCancelBtn = isShowCancelBtn;
    }

    @Override
    public void show() {
        if (!isShow) {
            super.show();
            content.setText(contentString);
            enter.setText(enterString);
            if (enterColor != 0) { enter.setTextColor(content.getResources().getColor(enterColor)); }
            if (!isShowCancelBtn) {
                cancel.setVisibility(View.GONE);
            }
            else {
                cancel.setText(cancelString);
            }
            isShow = true;
        }
    }

    private OnEnterClickListener onEnterClickListener = null;
    private OnCancelClickListener onCancelClickListener = null;

    public UpdateDialog setOnEnterClickListener(OnEnterClickListener onEnterClickListener) {
        this.onEnterClickListener = onEnterClickListener;
        return this;
    }

    public UpdateDialog setOnCancelClickListener(OnCancelClickListener onCancelClickListener) {
        this.onCancelClickListener = onCancelClickListener;
        return this;
    }
}
