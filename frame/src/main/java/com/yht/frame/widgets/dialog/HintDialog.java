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
 */
public class HintDialog extends Dialog implements OnClickListener {
    private TextView enter, cancel;
    private TextView title, content;
    private String titleString = "提示";
    private String contentString = "确认合格？";
    private String enterString = "确定";
    private int enterColor = 0;
    private String cancelString = "取消";
    private boolean isShow = false;

    public HintDialog(Context context) {
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
    public HintDialog setTitleString(String titleString) {
        this.titleString = titleString;
        return this;
    }

    /**
     * 设置提示语的文本
     *
     * @param contentString 内容
     */
    public HintDialog setContentString(String contentString) {
        this.contentString = contentString;
        return this;
    }

    /**
     * 设置确定按钮的文本
     */
    public HintDialog setEnterBtnTxt(String str) {
        this.enterString = str;
        return this;
    }

    /**
     * 设置确定按钮的颜色
     */
    public HintDialog setEnterTxtColor(int color) {
        this.enterColor = color;
        return this;
    }

    /**
     * 设置取消按钮的文本
     */
    public HintDialog setCancleBtnTxt(String str) {
        this.cancelString = str;
        return this;
    }

    @Override
    public void show() {
        if (!isShow) {
            super.show();
            title.setText(titleString);
            content.setText(contentString);
            enter.setText(enterString);
            cancel.setText(cancelString);
            isShow = true;
        }
    }

    private OnEnterClickListener onEnterClickListener = null;
    private OnCancelClickListener onCancelClickListener = null;

    public HintDialog setOnEnterClickListener(OnEnterClickListener onEnterClickListener) {
        this.onEnterClickListener = onEnterClickListener;
        return this;
    }

    public HintDialog setOnCancelClickListener(OnCancelClickListener onCancelClickListener) {
        this.onCancelClickListener = onCancelClickListener;
        return this;
    }
}
