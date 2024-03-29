package com.yht.yihuantong.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
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
import com.umeng.analytics.AnalyticsConfig;
import com.yht.frame.data.BaseData;
import com.yht.frame.data.BaseNetConfig;
import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.CommonData;
import com.yht.frame.data.Tasks;
import com.yht.frame.data.bean.LoginBean;
import com.yht.frame.data.bean.ProtocolBean;
import com.yht.frame.data.bean.VersionBean;
import com.yht.frame.data.type.DocAuthStatus;
import com.yht.frame.http.retrofit.RequestUtils;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.utils.HuiZhenLog;
import com.yht.frame.utils.ToastUtil;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ZycApplication;
import com.yht.yihuantong.ui.WebViewActivity;
import com.yht.yihuantong.ui.auth.AuthDoctorActivity;
import com.yht.yihuantong.ui.dialog.UpdateDialog;
import com.yht.yihuantong.ui.hint.UrlManagerActivity;
import com.yht.yihuantong.ui.main.MainActivity;
import com.yht.yihuantong.version.ConstantsVersionMode;
import com.yht.yihuantong.version.presenter.VersionPresenter;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/5/24 14:02
 * @description
 */
public class LoginOptionsActivity extends BaseActivity
        implements VersionPresenter.VersionViewListener, UpdateDialog.OnEnterClickListener {
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
     * 登录协议
     */
    private ProtocolBean protocolBean;
    /**
     * 版本检测
     */
    private VersionPresenter mVersionPresenter;
    /**
     * 版本弹窗
     */
    private UpdateDialog updateDialog;
    /**
     * 服务协议更新时间戳
     */
    private long protocolUpdateDate;
    /**
     * 是否显示新的协议
     */
    private boolean isShowNewProtocol = true;
    /**
     * 是否同意协议
     */
    private boolean isAgree;
    /**
     * 账号登录状态
     */
    private static final int REQUEST_CODE_LOGIN_STATUS = 100;
    /**
     * 绑定手机号
     */
    private static final int REQUEST_CODE_BIND_STATUS = 200;
    /**
     * 协议结果
     */
    private static final int REQUEST_CODE_PROTOCOL = 300;

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
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        protocolUpdateDate = BaseUtils.date2TimeStamp(
                sharePreferenceUtil.getAlwaysString(CommonData.KEY_IS_PROTOCOL_UPDATE_DATE),
                BaseUtils.YYYY_MM_DD_HH_MM_SS);
        ZycApplication.getInstance().setLoginStatus(false);
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        //检查更新
        mVersionPresenter = new VersionPresenter(this, "");
        mVersionPresenter.setVersionViewListener(this);
        mVersionPresenter.init();
        registerToWeChat();
        spannableString(getString(R.string.txt_login_protocol));
        HuiZhenLog.i(TAG, "channel:" + AnalyticsConfig.getChannel(this));
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
     * 查询用户协议最后更新时间
     */
    private void getProtocolUpdateDate() {
        RequestUtils.getProtocolUpdateDate(this, this);
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
            ZycApplication.getInstance().setLoginBean(loginBean);
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
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(CommonData.KEY_HIDE_VERSION_UPDATE, true);
        startActivity(intent);
        finish();
    }

    /**
     * 微信登录 注册
     */
    private void registerToWeChat() {
        String weChatId;
        if (ZycApplication.getInstance().debugMode) {
            weChatId = BaseData.WE_CHAT_ID_TEST;
        }
        else {
            weChatId = BaseData.WE_CHAT_ID;
        }
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(this, weChatId, true);
        // 将应用的appId注册到微信
        api.registerApp(weChatId);
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

    @OnClick({ R.id.tv_login_wechat, R.id.tv_login_phone, R.id.url })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_login_wechat:
                //判断手机是否安装微信
                if (api.isWXAppInstalled()) {
                    sendReq();
                }
                else {
                    ToastUtil.toast(this, R.string.txt_wechat_install_error);
                }
                break;
            case R.id.tv_login_phone:
                Intent intent = new Intent(this, LoginActivity.class);
                intent.putExtra(CommonData.KEY_IS_PROTOCOL_UPDATE_DATE, isShowNewProtocol);
                if (protocolBean != null) {
                    intent.putExtra(CommonData.KEY_PUBLIC_STRING, protocolBean.getUpdateAt());
                }
                startActivityForResult(intent, REQUEST_CODE_LOGIN_STATUS);
                break;
            case R.id.url:
                if (ZycApplication.getInstance().debugMode) {
                    num++;
                    if (num == 5) {
                        startActivity(new Intent(this, UrlManagerActivity.class));
                        num = 0;
                    }
                }
                break;
            default:
                break;
        }
    }

    int num;

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        switch (task) {
            case WE_CHAT_LOGIN:
                //微信登录成功
                loginBean = (LoginBean)response.getData();
                jump();
                break;
            case GET_PROTOCOL_UPDATE_DATE:
                protocolBean = (ProtocolBean)response.getData();
                long lastTime = BaseUtils.date2TimeStamp(protocolBean.getUpdateAt(), BaseUtils.YYYY_MM_DD_HH_MM_SS);
                if (lastTime > protocolUpdateDate) {
                    isShowNewProtocol = true;
                }
                else {
                    isShowNewProtocol = false;
                }
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
            case REQUEST_CODE_LOGIN_STATUS:
            case REQUEST_CODE_BIND_STATUS:
                jumpMain();
                break;
            case REQUEST_CODE_PROTOCOL:
                if (protocolBean != null) {
                    sharePreferenceUtil.putAlwaysString(CommonData.KEY_IS_PROTOCOL_UPDATE_DATE,
                                                        protocolBean.getUpdateAt());
                }
                isAgree = true;
                sendReq();
                break;
            default:
                break;
        }
    }

    /*********************版本更新回调*************************/
    @Override
    public void updateVersion(VersionBean version, int mode, boolean isDownNewApk) {
        if (mode == ConstantsVersionMode.UPDATE_OTHER) {
            if (!ZycApplication.getInstance().debugMode) {
                //如果线上版本比当前版本更低，就切换到灰度环境
                ZycApplication.getInstance().updateBaseUrl(BaseNetConfig.BASE_URL_PRE);
            }
            return;
        }
        else if (mode == ConstantsVersionMode.UPDATE_NONE) {
            return;
        }
        updateDialog = new UpdateDialog(this);
        updateDialog.setCancelable(false);
        updateDialog.setUpdateMode(mode)
                    .setIsDownNewAPK(isDownNewApk)
                    .setTitle(version.getTitle())
                    .setData(version.getNotes());
        updateDialog.setOnEnterClickListener(this);
        updateDialog.show();
    }

    @Override
    public void updateLoading(long total, long current) {
        if (updateDialog != null && updateDialog.isShowing()) {
            updateDialog.setProgressValue(total, current);
        }
    }

    /**
     * 当无可用网络时回调
     */
    @Override
    public void updateNetWorkError() {
    }

    @Override
    public void onEnter(boolean isMustUpdate) {
        mVersionPresenter.getNewAPK(isMustUpdate);
        ToastUtil.toast(this, R.string.txt_download_hint);
    }

    /**
     * 字符串处理
     */
    private void spannableString(String s) {
        SpannableString style = new SpannableString(s);
        //颜色
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(ContextCompat.getColor(this, R.color.color_1491fc));
        style.setSpan(colorSpan, 7, 13, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        style.setSpan(colorSpan, 14, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //点击
        ClickableSpan clickSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                //                isAgree = true;
                Intent intent = new Intent(LoginOptionsActivity.this, WebViewActivity.class);
                intent.putExtra(CommonData.KEY_PUBLIC,
                                ZycApplication.getInstance().getBaseUrl() + BaseNetConfig.BASE_BASIC_USER_PROTOCOL_URL);
                intent.putExtra(CommonData.KEY_TITLE, getString(R.string.txt_about_protocol));
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
        //点击
        ClickableSpan privacyClickSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                //                isAgree = true;
                Intent intent = new Intent(LoginOptionsActivity.this, WebViewActivity.class);
                intent.putExtra(CommonData.KEY_PUBLIC, ZycApplication.getInstance().getBaseUrl() +
                                                       BaseNetConfig.BASE_BASIC_PRIVATE_PROTOCOL_URL);
                intent.putExtra(CommonData.KEY_TITLE, getString(R.string.txt_about_privacy));
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
        style.setSpan(clickSpan, 7, 13, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        style.setSpan(privacyClickSpan, 14, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
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
