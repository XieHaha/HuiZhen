package com.zyc.doctor.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yht.frame.data.BaseData;
import com.yht.frame.data.BaseNetConfig;
import com.yht.frame.data.CommonData;
import com.yht.frame.data.Tasks;
import com.yht.frame.http.retrofit.RequestUtils;
import com.yht.frame.ui.BaseActivity;
import com.zyc.doctor.R;
import com.zyc.doctor.ZycApplication;
import com.zyc.doctor.ui.WebViewActivity;
import com.zyc.doctor.ui.auth.AuthDoctorActivity;
import com.zyc.doctor.ui.main.MainActivity;

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
    /**
     * IWXAPI 是第三方app和微信通信的openApi接口
     */
    private IWXAPI api;
    /**
     * 医生认证状态回调
     */
    private static final int REQUEST_CODE_AUTH_STATUS = 100;
    /**
     * 账号登录状态
     */
    private static final int REQUEST_CODE_LOGIN_STATUS = 200;

    @Override
    public int getLayoutID() {
        return R.layout.act_login_options;
    }

    @Override
    protected void onResume() {
        super.onResume();
        weChatCallBack();
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        registerToWeChat();
        spannableString(getString(R.string.txt_login_protocol));
    }

    /**
     * 微信登录
     *
     * @param code
     */
    private void weChatLogin(String code) {
        RequestUtils.weChatLogin(this, code, BaseData.ADMIN, this);
    }

    /**
     * 微信登录回调  请求服务器
     */
    private void weChatCallBack() {
        if (getIntent() != null) {
            String code = getIntent().getStringExtra(CommonData.KEY_PUBLIC);
            if (!TextUtils.isEmpty(code)) {
                weChatLogin(code);
            }
        }
    }

    /**
     * 医生认证
     */
    private void jumpAuth() {
        startActivityForResult(new Intent(this, AuthDoctorActivity.class), REQUEST_CODE_AUTH_STATUS);
    }

    @OnClick({ R.id.tv_login_wechat, R.id.tv_login_phone })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_login_wechat:
                if (api.isWXAppInstalled()) {
                    sendReq();
                }
                else {
                    Intent intent = new Intent(LoginOptionsActivity.this, WebViewActivity.class);
                    intent.putExtra(CommonData.KEY_PUBLIC, BaseNetConfig.BASE_WECHAT_DOWNLOAD_URL);
                    startActivity(intent);
                }
                sendReq();
                break;
            case R.id.tv_login_phone:
                Intent intent = new Intent(this, LoginActivity.class);
                startActivityForResult(intent, REQUEST_CODE_LOGIN_STATUS);
                break;
            default:
                break;
        }
    }

    /**
     * 微信登录 注册
     */
    private void registerToWeChat() {
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(this, BaseData.WECHAT_ID, true);
        // 将应用的appId注册到微信
        api.registerApp(BaseData.WECHAT_ID);
        ZycApplication.iwxapi = api;
    }

    private void sendReq() {
        final SendAuth.Req req = new SendAuth.Req();
        //获取个人信息（作用域）
        req.scope = BaseData.WECHAT_SCOPE;
        req.state = BaseData.WECHAT_STATE;
        //调用api接口，发送数据到微信
        api.sendReq(req);
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
                Intent intent = new Intent(LoginOptionsActivity.this, WebViewActivity.class);
                intent.putExtra(CommonData.KEY_PUBLIC, BaseNetConfig.BASE_BASIC_USER_PROTOCOL_URL);
                startActivity(intent);
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

    @Override
    public void onResponseSuccess(Tasks task, Object response) {
        switch (task) {
            case WE_CHAT_LOGIN:
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQUEST_CODE_AUTH_STATUS:
                break;
            case REQUEST_CODE_LOGIN_STATUS:
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
            default:
                break;
        }
    }
}
