package com.yht.yihuantong.ui.remote;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.yht.frame.data.BaseData;
import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.CommonData;
import com.yht.frame.data.Tasks;
import com.yht.frame.http.retrofit.RequestUtils;
import com.yht.frame.ui.BaseActivity;
import com.yht.yihuantong.R;

import org.apache.commons.lang3.StringUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author DUNDUN
 * 远程会诊登录
 */
public class RemoteLoginActivity extends BaseActivity {
    @BindView(R.id.act_remote_hint)
    TextView actRemoteHint;
    @BindView(R.id.act_remote_sure)
    TextView actRemoteSure;
    @BindView(R.id.act_remote_cancel)
    TextView actRemoteCancel;
    /**
     * 二维码结果
     */
    private String content;

    @Override
    public int getLayoutID() {
        return R.layout.act_remote_login;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        if (getIntent() != null) {
            content = getIntent().getStringExtra(CommonData.KEY_PUBLIC_STRING);
        }
    }

    /**
     * 远程会诊确认
     */
    private void applyRemote() {
        RequestUtils.applyRemote(this, loginBean.getToken(), StringUtils.substringAfter(content, BaseData.BASE_REMOTE),
                                 this);
    }

    /**
     * 远程会诊意见确认
     */
    private void applyRemoteAdvice() {
        RequestUtils.applyRemoteAdvice(this, loginBean.getToken(),
                                       StringUtils.substringAfter(content, BaseData.BASE_REMOTE_ADVICE), this);
    }

    @OnClick({ R.id.act_remote_sure, R.id.act_remote_cancel })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.act_remote_sure:
                if (content.startsWith(BaseData.BASE_REMOTE)) {
                    applyRemote();
                }
                else {
                    applyRemoteAdvice();
                }
                break;
            case R.id.act_remote_cancel:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        switch (task) {
            case APPLY_REMOTE:
                finish();
                break;
            case APPLY_REMOTE_ADVICE:
                finish();
                break;
            default:
                break;
        }
    }
}
