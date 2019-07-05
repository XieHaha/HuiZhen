package com.yht.yihuantong.ui.login;

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
import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.CommonData;
import com.yht.frame.data.DocAuthStatus;
import com.yht.frame.data.Tasks;
import com.yht.frame.data.base.LoginBean;
import com.yht.frame.http.retrofit.RequestUtils;
import com.yht.frame.ui.BaseActivity;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ZycApplication;
import com.yht.yihuantong.ui.WebViewActivity;
import com.yht.yihuantong.ui.auth.AuthDoctorActivity;
import com.yht.yihuantong.ui.main.MainActivity;

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
     * 账号登录状态
     */
    private static final int REQUEST_CODE_LOGIN_STATUS = 100;
    /**
     * 绑定手机号
     */
    private static final int REQUEST_CODE_BIND_STATUS = 200;

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

    private void jump() {
        //判断是否绑定手机号
        if (TextUtils.isEmpty(loginBean.getMobile())) {
            Intent intent = new Intent(this, BindPhoneActivity.class);
            intent.putExtra(CommonData.KEY_LOGIN_BEAN, loginBean);
            startActivityForResult(intent, REQUEST_CODE_BIND_STATUS);
        }
        else {
            // 已绑定手机，需要存储登录信息
            ZycApplication.getInstance().setLoginSuccessBean(loginBean);
            //判断认证状态
            switch (loginBean.getApprovalStatus()) {
                case DocAuthStatus.AUTH_NONE:
                case DocAuthStatus.AUTH_WAITTING:
                case DocAuthStatus.AUTH_FAILD:
                    jumpAuth();
                    break;
                case DocAuthStatus.AUTH_SUCCESS:
                    jumpMain();
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 医生认证
     */
    private void jumpAuth() {
        startActivity(new Intent(this, AuthDoctorActivity.class));
    }

    /**
     * 主页
     */
    private void jumpMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    /**
     * 微信登录 注册
     */
    private void registerToWeChat() {
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(this, BaseData.WE_CHAT_ID, true);
        // 将应用的appId注册到微信
        api.registerApp(BaseData.WE_CHAT_ID);
        ZycApplication.iwxapi = api;
    }

    /**
     * 调用微信登录授权
     */
    private void sendReq() {
        final SendAuth.Req req = new SendAuth.Req();
        //获取个人信息（作用域）
        req.scope = BaseData.WE_CHAT_SCOPE;
        req.state = BaseData.WE_CHAT_STATE;
        //调用api接口，发送数据到微信
        api.sendReq(req);
    }

    /**
     * 微信登录回调  请求服务器
     */
    private void weChatCallBack() {
        String code = sharePreferenceUtil.getString(CommonData.KEY_WECHAT_LOGIN_SUCCESS_BEAN);
        if (!TextUtils.isEmpty(code)) {
            weChatLogin(code);
            //获取后就清除本地数据，防止二次请求
            sharePreferenceUtil.clear(CommonData.KEY_WECHAT_LOGIN_SUCCESS_BEAN);
        }
    }

    @OnClick({ R.id.tv_login_wechat, R.id.tv_login_phone })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_login_wechat:
                //判断手机是否安装微信
                if (api.isWXAppInstalled()) {
                    sendReq();
                }
                else {
                    //显示微信下载页面
                    Intent intent = new Intent(this, WebViewActivity.class);
                    intent.putExtra(CommonData.KEY_PUBLIC, BaseNetConfig.BASE_WE_CHAT_DOWNLOAD_URL);
                    startActivity(intent);
                }
                break;
            case R.id.tv_login_phone:
                Intent intent = new Intent(this, LoginActivity.class);
                startActivityForResult(intent, REQUEST_CODE_LOGIN_STATUS);
                break;
            default:
                break;
        }
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        if (task == Tasks.WE_CHAT_LOGIN) {
            //微信登录成功
            loginBean = (LoginBean)response.getData();
            jump();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQUEST_CODE_LOGIN_STATUS:
            case REQUEST_CODE_BIND_STATUS:
                jumpMain();
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
}