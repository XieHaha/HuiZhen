package com.zyc.doctor.ui.auth.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yht.frame.ui.BaseFragment;
import com.yht.frame.widgets.dialog.HintDialog;
import com.zyc.doctor.R;
import com.zyc.doctor.ui.auth.listener.OnStepListener;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/5/17 14:55
 * @des 认证结果信息
 */
public class AuthResultFragment extends BaseFragment {
    @BindView(R.id.tv_auth_result_status)
    TextView tvAuthResultStatus;
    @BindView(R.id.tv_auth_result_txt)
    TextView tvAuthResultTxt;
    @BindView(R.id.tv_auth_result_image)
    ImageView tvAuthResultImage;
    @BindView(R.id.tv_auth_result_contact)
    TextView tvAuthResultContact;
    @BindView(R.id.tv_auth_result_submit)
    TextView tvAuthResultSubmit;

    @Override
    public int getLayoutID() {
        return R.layout.fragment_auth_result;
    }

    @Override
    public void initView(View view, @NonNull Bundle savedInstanceState) {
        super.initView(view, savedInstanceState);
        tvAuthResultContact.setSelected(true);
        tvAuthResultSubmit.setVisibility(View.GONE);
    }

    @OnClick({ R.id.tv_auth_result_contact, R.id.tv_auth_result_submit })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_auth_result_contact:
                new HintDialog(getContext()).setPhone("028-88888888")
                                            .setOnEnterClickListener(() -> callPhone(""))
                                            .show();
                break;
            case R.id.tv_auth_result_submit:
                break;
            default:
                break;
        }
    }

    /**
     * 拨打电话（跳转到拨号界面，用户手动点击拨打）
     *
     * @param phoneNum 电话号码
     */
    public void callPhone(String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        startActivity(intent);
    }

    private OnStepListener onAuthStepListener;

    public void setOnAuthStepListener(OnStepListener onAuthStepListener) {
        this.onAuthStepListener = onAuthStepListener;
    }
}
