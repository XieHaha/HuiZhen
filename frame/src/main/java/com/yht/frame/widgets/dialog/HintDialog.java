package com.yht.frame.widgets.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.yht.frame.R;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.widgets.dialog.listener.OnCancelClickListener;
import com.yht.frame.widgets.dialog.listener.OnEnterClickListener;

/**
 * @author dundun
 */
public class HintDialog extends Dialog implements OnClickListener {
    private Context context;
    private TextView tvEnter, tvCancel, tvTitle, tvContent;
    private ImageView ivDelete;
    private View line;
    /**
     * 初始值
     */
    private String titleString = "提示", contentString = "确认合格？", enterString = "确定", cancelString = "取消";
    /**
     * 是否隐藏
     */
    private boolean cancelGone;
    /**
     * 确认按钮颜色控制
     */
    private boolean enterSelect = false;
    private boolean isShow = false;
    private boolean cancelAble = true;
    private int showDeleteIcon;

    public HintDialog(Context context) {
        super(context, R.style.normal_dialog);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //状态栏透明
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.dialog_hint);
        initWidget();
    }

    private void initWidget() {
        tvEnter = findViewById(R.id.dialog_simple_hint_enter);
        tvCancel = findViewById(R.id.dialog_simple_hint_cancel);
        tvTitle = findViewById(R.id.dialog_simple_hint_title);
        tvContent = findViewById(R.id.dialog_simple_hint_content);
        ivDelete = findViewById(R.id.dialog_simple_hint_delete);
        line = findViewById(R.id.view_line);
        tvContent.setMovementMethod(ScrollingMovementMethod.getInstance());
        tvCancel.setOnClickListener(this);
        tvEnter.setOnClickListener(this);
        ivDelete.setOnClickListener(this);
        setCanceledOnTouchOutside(cancelAble);
        setCancelable(cancelAble);
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
        else if (v == ivDelete) {
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
     * @param titleStringId 标题
     */
    public HintDialog setTitleString(int titleStringId) {
        titleString = context.getString(titleStringId);
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

    public HintDialog setContentString(int contentStringId) {
        contentString = context.getString(contentStringId);
        return this;
    }

    /**
     * 设置确定按钮的文本
     */
    public HintDialog setEnterBtnTxt(String str) {
        this.enterString = str;
        return this;
    }

    public HintDialog setEnterBtnTxt(int enterStringId) {
        enterString = context.getString(enterStringId);
        return this;
    }

    public HintDialog setEnterSelect(boolean enterSelect) {
        this.enterSelect = enterSelect;
        return this;
    }

    /**
     * 是否显示删除按钮
     */
    public HintDialog setDeleteVisible(int visible) {
        showDeleteIcon = visible;
        return this;
    }

    /**
     * 设置取消按钮的文本
     */
    public HintDialog setCancleBtnTxt(String str) {
        this.cancelString = str;
        return this;
    }

    public HintDialog setCancleBtnTxt(int cancelStringId) {
        cancelString = context.getString(cancelStringId);
        return this;
    }

    public HintDialog setCancelBtnGone(boolean gone) {
        this.cancelGone = gone;
        return this;
    }

    public HintDialog setCancelableAndTouch(boolean cancel) {
        cancelAble = cancel;
        return this;
    }

    @Override
    public void show() {
        if (!isShow) {
            super.show();
            tvTitle.setText(titleString);
            tvContent.setText(contentString);
            tvEnter.setText(enterString);
            tvEnter.setSelected(enterSelect);
            ivDelete.setVisibility(showDeleteIcon);
            if (cancelGone) {
                tvCancel.setVisibility(View.GONE);
                line.setVisibility(View.GONE);
            }
            else {
                tvCancel.setText(cancelString);
            }
            isShow = true;
        }
    }

    /**
     * 拨打电话封装
     */
    public HintDialog setPhone(String key, String phone) {
        setPhone(key, phone, true);
        return this;
    }

    /**
     * 拨打电话封装
     * asterisk 是否加密显示
     */
    public HintDialog setPhone(String key, String phone, boolean asterisk) {
        setTitleString(R.string.txt_hint);
        setContentString(String.format(context.getString(R.string.txt_contact_hotline), key,
                                       asterisk ? BaseUtils.asteriskUserPhone(phone) : phone));
        setEnterBtnTxt(R.string.txt_call);
        setEnterSelect(true);
        return this;
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
