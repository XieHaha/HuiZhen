package com.zyc.doctor.ui.auth.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.yht.frame.ui.BaseFragment;
import com.zyc.doctor.R;
import com.zyc.doctor.ui.auth.listener.OnAuthStepListener;

/**
 * @author 顿顿
 * @date 19/5/17 14:55
 * @des 认证结果信息
 */
public class AuthResultFragment extends BaseFragment {
    @Override
    public int getLayoutID() {
        return R.layout.fragment_worker;
    }

    @Override
    public void initView(View view, @NonNull Bundle savedInstanceState) {
        super.initView(view, savedInstanceState);
    }



    private OnAuthStepListener onAuthStepListener;

    public void setOnAuthStepListener(OnAuthStepListener onAuthStepListener) {
        this.onAuthStepListener = onAuthStepListener;
    }
}
