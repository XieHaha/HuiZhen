package com.zyc.doctor.ui.login;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.yht.frame.data.BaseData;
import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.Tasks;
import com.yht.frame.http.retrofit.RequestUtils;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.widgets.edittext.SuperEditText;
import com.zyc.doctor.R;
import com.zyc.doctor.ui.AbstractTextWatcher;

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
    @BindView(R.id.tv_login_account)
    SuperEditText tvLoginAccount;
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
    private String phone, verifyCode;
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
        mode = getIntent().getBooleanExtra(BaseData.BASE_HONOR_NAME, false);
        if (mode) {
            tvLoginTitle.setText(R.string.txt_login_bind_phone);
            tvLoginTitleHint.setText(R.string.txt_login_bind_phone_hint);
            tvLoginNext.setText(R.string.txt_sure);
        }
    }

    @Override
    public void initListener() {
        super.initListener();
        tvLoginAccount.addTextChangedListener(new AbstractTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s) && s.length() == BaseData.BASE_PHONE_DEFAULT_LENGTH) {
                    tvLoginObtainCode.setSelected(true);
                }
                else {
                    tvLoginObtainCode.setSelected(false);
                }
            }
        });
        etLoginCode.addTextChangedListener(new AbstractTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(phone) && !TextUtils.isEmpty(s) &&
                    s.length() == BaseData.BASE_VERIFY_CODE_DEFAULT_LENGTH) {
                    tvLoginNext.setSelected(true);
                }
                else {
                    tvLoginNext.setSelected(false);
                }
            }
        });
    }

    /**
     * 获取验证码
     */
    private void getVerifyCode() {
        RequestUtils.getVerifyCode(this, phone, this);
    }

    @OnClick({ R.id.tv_login_obtain_code, R.id.tv_login_next })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_login_obtain_code:
                phone = tvLoginAccount.getText().toString().trim();
                if (!BaseUtils.isMobileNumber(phone)) {
                    return;
                }
                //                getVerifyCode();
                onResponseSuccess(Tasks.GET_VERIFY_CODE, null);
                break;
            case R.id.tv_login_next:
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
                isSendVerifyCode = true;
                time = BaseData.BASE_MAX_RESEND_TIME;
                final ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1,
                                                                                                 new BasicThreadFactory.Builder()
                                                                                                         .namingPattern(
                                                                                                                 "yht-thread-pool-%d")
                                                                                                         .daemon(true)
                                                                                                         .build());
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
                break;
            case LOGIN_AND_REGISTER:
                break;
            default:
                break;
        }
    }
}
