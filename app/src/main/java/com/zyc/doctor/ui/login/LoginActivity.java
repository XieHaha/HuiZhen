package com.zyc.doctor.ui.login;

import android.view.View;
import android.widget.TextView;

import com.yht.frame.http.retrofit.RequestUtils;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.widgets.edittext.SuperEditText;
import com.zyc.doctor.R;

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
    @BindView(R.id.tv_login_account_error)
    TextView tvLoginAccountError;
    private String phone, code;

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_login;
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
                    tvLoginAccountError.setVisibility(View.VISIBLE);
                    return;
                }
                getVerifyCode();
                break;
            case R.id.tv_login_next:
                break;
            default:
                break;
        }
    }
}
