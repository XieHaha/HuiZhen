package com.yht.frame.widgets.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yht.frame.R;
import com.yht.frame.api.DirHelper;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.utils.ScreenUtils;
import com.yht.frame.utils.ToastUtil;
import com.yht.frame.widgets.view.SignatureView;

import java.io.IOException;

/**
 * @author dundun
 * 签名
 */
public class SignatureDialog extends Dialog implements OnClickListener {
    private Context context;
    private TextView tvEnter, tvReset;
    private ImageView ivDelete;
    private SignatureView signatureView;
    private boolean cancelAble = true;

    public SignatureDialog(Context context) {
        super(context, R.style.normal_dialog);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //状态栏透明
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.dialog_signature);
        initWidget();
    }

    private void initWidget() {
        // 设置dialog的宽度
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = ScreenUtils.getScreenHeight(context) - BaseUtils.dp2px(context, 60);
        params.width = ScreenUtils.getScreenWidth(context) - BaseUtils.dp2px(context, 30);
        getWindow().setAttributes(params);
        //控件
        tvEnter = findViewById(R.id.tv_sure);
        tvReset = findViewById(R.id.tv_reset);
        ivDelete = findViewById(R.id.iv_delete);
        signatureView = findViewById(R.id.signature);
        tvReset.setOnClickListener(this);
        tvEnter.setOnClickListener(this);
        setCanceledOnTouchOutside(cancelAble);
        setCancelable(cancelAble);
        ivDelete.setOnClickListener(v -> dismiss());
    }

    @Override
    public void onClick(View v) {
        if (v == tvEnter) {
            if (signatureView.hasDraw() && onEnterClickListener != null) {
                onEnterClickListener.onEnter(signatureView.getBitMap());
                try {
                    signatureView.save(DirHelper.getPathImage() + "/test.jpg");
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                dismiss();
            }
            else {
                ToastUtil.toast(context, R.string.txt_signature_none_hint, LinearLayout.VERTICAL);
            }
        }
        else if (v == tvReset) {
            if (signatureView != null) {
                signatureView.clear();
            }
        }
    }

    public interface OnEnterClickListener {
        /**
         * bitmap 回调
         *
         * @param bitmap 资源
         */
        void onEnter(Bitmap bitmap);
    }

    private OnEnterClickListener onEnterClickListener = null;

    public SignatureDialog setOnEnterClickListener(OnEnterClickListener onEnterClickListener) {
        this.onEnterClickListener = onEnterClickListener;
        return this;
    }
}
