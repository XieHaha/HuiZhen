package com.zyc.doctor.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.yht.frame.ui.BaseActivity;
import com.yht.frame.utils.ToastUtil;
import com.zyc.doctor.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/5/24 14:02
 * @des
 */
public class LoginOptionsActivity extends BaseActivity {
    @BindView(R.id.tv_login_wechat)
    TextView tvLoginWechat;
    @BindView(R.id.tv_login_phone)
    TextView tvLoginPhone;
    @BindView(R.id.tv_login_protocol)
    TextView tvLoginProtocol;

    @Override
    public int getLayoutID() {
        return R.layout.act_login_options;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        spannableString(getString(R.string.txt_login_protocol));
    }

    @OnClick({ R.id.tv_login_wechat, R.id.tv_login_phone })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_login_wechat:
                ToastUtil.toast(this, "微信登录");
                break;
            case R.id.tv_login_phone:
                //                startActivity(new Intent(this, AuthDoctorActivity.class));
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * 字符串处理
     */
    private void spannableString(String s) {
        SpannableString style = new SpannableString(s);
        //颜色
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(ContextCompat.getColor(this, R.color.color_1491fc));
        style.setSpan(colorSpan, 7, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //点击
        ClickableSpan clickSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                ToastUtil.toast(LoginOptionsActivity.this, "跳转协议");
                clearBackgroundColor(widget);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
                ds.clearShadowLayer();
            }
        };
        style.setSpan(clickSpan, 7, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //配置给TextView
        tvLoginProtocol.setMovementMethod(LinkMovementMethod.getInstance());
        tvLoginProtocol.setText(style);
    }

    private void clearBackgroundColor(View view) {
        if (view instanceof TextView) {
            ((TextView)view).setHighlightColor(ContextCompat.getColor(this, android.R.color.transparent));
        }
    }
}
