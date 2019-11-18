package com.yht.yihuantong.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.yht.frame.data.BaseData;
import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.CommonData;
import com.yht.frame.data.Tasks;
import com.yht.frame.data.bean.LoginBean;
import com.yht.frame.data.bean.VerifyCodeBean;
import com.yht.frame.data.type.DocAuthStatus;
import com.yht.frame.http.retrofit.RequestUtils;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.utils.ToastUtil;
import com.yht.frame.widgets.edittext.AbstractTextWatcher;
import com.yht.frame.widgets.edittext.SuperEditText;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ZycApplication;
import com.yht.yihuantong.ui.auth.AuthDoctorActivity;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/5/24 15:20
 * @description
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
     * 验证码计时
     */
    private int time = 0;
    /**
     * 协议更新时间
     */
    private String ptotocolUpdateTime;
    /**
     * 是否获取过验证码
     */
    private boolean isSendVerifyCode = false;
    /**
     * 是否显示登录协议
     */
    private boolean isShowNewProtocol;
    /**
     * 是否同意协议
     */
    private boolean isAgree;
    /**
     * 协议结果
     */
    private static final int REQUEST_CODE_PROTOCOL = 100;
    /**
     * 认证状态  （如果提交了认证信息，就返回到登陆选择界面）
     */
    private static final int REQUEST_CODE_AUTH_STATUS = 200;

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
        if (getIntent() != null) {
            isShowNewProtocol = getIntent().getBooleanExtra(CommonData.KEY_IS_PROTOCOL_UPDATE_DATE, false);
            ptotocolUpdateTime = getIntent().getStringExtra(CommonData.KEY_PUBLIC_STRING);
        }
        phone = sharePreferenceUtil.getAlwaysString(CommonData.KEY_LOGIN_ACCOUNT);
        etLoginAccount.setText(phone);
        if (BaseUtils.isMobileNumber(phone)) {
            tvLoginObtainCode.setSelected(true);
        }
    }

    @Override
    public void initListener() {
        super.initListener();
        etLoginAccount.addTextChangedListener(new AbstractTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                phone = s.toString().trim();
                if (phone.length() == BaseData.BASE_PHONE_DEFAULT_LENGTH) {
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
                verifyCode = s.toString().trim();
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
        RequestUtils.login(this, verifyCodeBean.getPrepare_id(), phone, verifyCode, BaseData.ADMIN, this);
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

    private void jump() {
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
                if (!BaseUtils.isMobileNumber(phone)) {
                    ToastUtil.toast(this, R.string.toast_phone_error);
                    return;
                }
                getVerifyCode();
                break;
            case R.id.tv_login_next:
                if (tvLoginNext.isSelected()) {
                    if (isSendVerifyCode) {
                        //                        if (isShowNewProtocol && !isAgree) {
                        //                            showProtocol();
                        //                        }
                        //                        else {
                        //                            login();
                        //                        }
                        login();
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
                sharePreferenceUtil.putAlwaysString(CommonData.KEY_LOGIN_ACCOUNT, phone);
                loginBean = (LoginBean)response.getData();
                //存储登录结果
                ZycApplication.getInstance().setLoginBean(loginBean);
                jump();
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
            case REQUEST_CODE_PROTOCOL:
                sharePreferenceUtil.putAlwaysString(CommonData.KEY_IS_PROTOCOL_UPDATE_DATE, ptotocolUpdateTime);
                isAgree = true;
                login();
                break;
            case REQUEST_CODE_AUTH_STATUS:
                finish();
                break;
            default:
                break;
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
