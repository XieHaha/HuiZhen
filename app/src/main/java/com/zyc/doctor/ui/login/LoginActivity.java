package com.zyc.doctor.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.yht.frame.data.BaseData;
import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.CommonData;
import com.yht.frame.data.DocAuthStatus;
import com.yht.frame.data.Tasks;
import com.yht.frame.data.base.LoginBean;
import com.yht.frame.data.base.VerifyCodeBean;
import com.yht.frame.http.retrofit.RequestUtils;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.utils.LogUtils;
import com.yht.frame.utils.ToastUtil;
import com.yht.frame.widgets.edittext.AbstractTextWatcher;
import com.yht.frame.widgets.edittext.SuperEditText;
import com.zyc.doctor.R;
import com.zyc.doctor.ZycApplication;
import com.zyc.doctor.ui.auth.AuthDoctorActivity;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/5/24 15:20
 * @des
 */
public class LoginActivity extends BaseActivity {
    @BindView(R.id.et_login_account)
    SuperEditText etLoginAccount;
    @BindView(R.id.tv_login_obtain_code)
    TextView tvLoginObtainCode;
    @BindView(R.id.et_login_code)
    SuperEditText etLoginCode;
    @BindView(R.id.tv_login_next)
    TextView tvLoginNext;
    @BindView(R.id.tv_login_title)
    TextView tvLoginTitle;
    @BindView(R.id.tv_login_title_hint)
    TextView tvLoginTitleHint;
    private ScheduledExecutorService executorService;
    private String phone, verifyCode;
    /**
     * 获取验证码后得到的 校验值
     */
    private VerifyCodeBean verifyCodeBean;
    /**
     * 登录 or绑定
     */
    private boolean mode;
    /**
     * 验证码计时
     */
    private int time = 0;
    /**
     * 是否获取过验证码
     */
    private boolean isSendVerifyCode = false;
    /**
     * 医生认证状态回调
     */
    private static final int REQUEST_CODE_AUTH_STATUS = 100;

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_login;
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            if (time <= 0) {
                tvLoginObtainCode.setClickable(true);
                tvLoginObtainCode.setText(R.string.txt_login_obtain_code);
            }
            else {
                tvLoginObtainCode.setClickable(false);
                tvLoginObtainCode.setText(String.format(getString(R.string.txt_login_time), time));
            }
            return true;
        }
    });

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        mode = getIntent().getBooleanExtra(CommonData.KEY_PUBLIC, false);
        if (mode) {
            tvLoginTitle.setText(R.string.txt_login_bind_phone);
            tvLoginTitleHint.setText(R.string.txt_login_bind_phone_hint);
            tvLoginTitleHint.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            tvLoginNext.setText(R.string.txt_sure);
        }
    }

    @Override
    public void initListener() {
        super.initListener();
        etLoginAccount.addTextChangedListener(new AbstractTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                phone = s.toString();
                if (BaseUtils.isMobileNumber(phone)) {
                    tvLoginObtainCode.setSelected(true);
                }
                else {
                    tvLoginObtainCode.setSelected(false);
                }
                initNextButton();
            }
        });
        etLoginCode.addTextChangedListener(new AbstractTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                verifyCode = s.toString();
                initNextButton();
            }
        });
    }

    /**
     * 获取验证码
     */
    private void getVerifyCode() {
        RequestUtils.getVerifyCode(this, phone, BaseData.ADMIN, this);
    }

    /**
     * 登录
     */
    private void login() {
        RequestUtils.login(this, verifyCodeBean.getPrepare_id(), verifyCode, this);
    }

    /**
     * 微信绑定手机号
     */
    private void weChatBind() {
        RequestUtils.weChatBind(this, loginBean.getOpenid(), loginBean.getUnionid(), phone,
                                verifyCodeBean.getPrepare_id(), verifyCode, BaseData.ADMIN, this);
    }

    /**
     * 登录按钮
     */
    private void initNextButton() {
        if (BaseUtils.isMobileNumber(phone) && BaseUtils.isCorrectVerifyCode(verifyCode)) {
            tvLoginNext.setSelected(true);
        }
        else {
            tvLoginNext.setSelected(false);
        }
    }

    /**
     * 登录环信聊天
     */
    private void loginEaseChat() {
        showLoadingView();
        EMClient.getInstance().login("15828456584_d", BaseData.BASE_EASE_DEFAULT_PWD, new EMCallBack() {
            @Override
            public void onSuccess() {
                closeLoadingView();
                runOnUiThread(() -> {
                    EMClient.getInstance().chatManager().loadAllConversations();
                    LogUtils.i(TAG, getString(R.string.txt_login_ease_success));
                    if (loginBean.getApprovalStatus() == DocAuthStatus.AUTH_SUCCESS) {
                        jumpMain();
                    }
                    else {
                        jumpAuth();
                    }
                });
            }

            @Override
            public void onProgress(int progress, String status) {
            }

            @Override
            public void onError(int code, String message) {
                closeLoadingView();
                LogUtils.i(TAG, getString(R.string.txt_login_ease_error));
                ToastUtil.toast(LoginActivity.this, R.string.txt_login_ease_error);
            }
        });
    }

    /**
     * 医生认证
     */
    private void jumpAuth() {
        startActivityForResult(new Intent(this, AuthDoctorActivity.class), REQUEST_CODE_AUTH_STATUS);
    }

    /**
     * 主页
     */
    private void jumpMain() {
        setResult(RESULT_OK);
        finish();
    }

    /**
     * 验证码再次获取倒计时
     */
    private void startVerifyCodeTimer() {
        time = BaseData.BASE_MAX_RESEND_TIME;
        executorService = new ScheduledThreadPoolExecutor(1, new BasicThreadFactory.Builder().namingPattern(
                "yht-thread-pool-%d").daemon(true).build());
        executorService.scheduleAtFixedRate(() -> {
            time--;
            if (time < 0) {
                time = 0;
                executorService.shutdownNow();
            }
            else {
                handler.sendEmptyMessage(0);
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    @OnClick({ R.id.tv_login_obtain_code, R.id.tv_login_next })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_login_obtain_code:
                phone = etLoginAccount.getText().toString();
                if (!BaseUtils.isMobileNumber(phone)) {
                    return;
                }
                getVerifyCode();
                break;
            case R.id.tv_login_next:
                if (tvLoginNext.isSelected()) {
                    if (isSendVerifyCode) {
                        //绑定手机号
                        if (mode) {
                            weChatBind();
                        }
                        else {
                            login();
                        }
                    }
                    else {
                        ToastUtil.toast(this, R.string.txt_login_verify_code_error);
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        switch (task) {
            case GET_VERIFY_CODE:
                verifyCodeBean = (VerifyCodeBean)response.getData();
                //是否获取过验证码 标识符
                isSendVerifyCode = true;
                startVerifyCodeTimer();
                break;
            case LOGIN_AND_REGISTER:
                //保存登录数据
                loginBean = (LoginBean)response.getData();
                ZycApplication.getInstance().setLoginSuccessBean(loginBean);
                //登录成功后调用环信服务器
                loginEaseChat();
                break;
            case WE_CHAT_BIND:
                //绑定成功后需要更新token
                LoginBean bean = (LoginBean)response.getData();
                loginBean.setToken(bean.getToken());
                ZycApplication.getInstance().setLoginSuccessBean(loginBean);
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
        if (requestCode == REQUEST_CODE_AUTH_STATUS) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executorService != null) {
            executorService.shutdownNow();
        }
    }
}
