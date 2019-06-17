package com.zyc.doctor.ui.check.fragment;

import com.yht.frame.ui.BaseFragment;
import com.zyc.doctor.R;
import com.zyc.doctor.ui.check.listener.OnCheckListener;

/**
 * @author 顿顿
 * @date 19/6/14 14:23
 * @des 确认提交
 */
public class SubmitFragment extends BaseFragment {
    @Override
    public int getLayoutID() {
        return R.layout.fragment_submit;
    }

    private OnCheckListener checkListener;

    public void setOnCheckListener(OnCheckListener onCheckListener) {
        this.checkListener = onCheckListener;
    }
}
