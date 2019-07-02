package com.zyc.doctor.ui.remote;

import android.view.View;
import android.widget.TextView;

import com.yht.frame.ui.BaseActivity;
import com.zyc.doctor.R;

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

    @Override
    public int getLayoutID() {
        return R.layout.act_remote_login;
    }

    @OnClick({ R.id.act_remote_sure, R.id.act_remote_cancel })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.act_remote_sure:
                break;
            case R.id.act_remote_cancel:
                break;
            default:
                break;
        }
    }
}
