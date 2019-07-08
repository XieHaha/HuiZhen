package com.yht.yihuantong.ui.auth.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yht.frame.data.DocAuthStatus;
import com.yht.frame.ui.BaseFragment;
import com.yht.frame.widgets.dialog.HintDialog;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ui.auth.listener.OnAuthStepListener;

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
    @BindView(R.id.iv_auth_result_image)
    ImageView ivAuthResultImage;
    @BindView(R.id.tv_auth_result_contact)
    TextView tvAuthResultContact;
    @BindView(R.id.tv_auth_result_submit)
    TextView tvAuthResultSubmit;

    @Override
    public int getLayoutID() {
        return R.layout.fragment_auth_result;
    }

    @Override
    public void onResume() {
        super.onResume();
        loginBean = getLoginBean();
        initAuthStatus();
    }

    private void initAuthStatus() {
        int curAuthStatus = loginBean.getApprovalStatus();
        if (curAuthStatus == DocAuthStatus.AUTH_FAILD) {
            tvAuthResultStatus.setText(R.string.txt_review_failure);
            tvAuthResultTxt.setText(loginBean.getRejectReason());
            tvAuthResultSubmit.setVisibility(View.VISIBLE);
            tvAuthResultContact.setSelected(false);
            ivAuthResultImage.setImageResource(R.mipmap.pic_auth_fail);
        }
        else {
            tvAuthResultStatus.setText(R.string.txt_review);
            tvAuthResultTxt.setText(R.string.txt_review_hint);
            tvAuthResultContact.setSelected(true);
            tvAuthResultSubmit.setVisibility(View.GONE);
            ivAuthResultImage.setImageResource(R.mipmap.pic_auth_waiting);
        }
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
                if (onAuthStepListener != null) {
                    onAuthStepListener.onAuthThree();
                }
                break;
            default:
                break;
        }
    }

    private OnAuthStepListener onAuthStepListener;

    public void setOnAuthStepListener(OnAuthStepListener onAuthStepListener) {
        this.onAuthStepListener = onAuthStepListener;
    }
}
