package com.zyc.doctor.ui.check.fragment;

import com.yht.frame.ui.BaseFragment;
import com.yht.frame.widgets.edittext.SuperEditText;
import com.zyc.doctor.R;
import com.zyc.doctor.ui.auth.listener.OnStepListener;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/6/14 14:23
 * @des 完善资料
 */
public class MaterialFragment extends BaseFragment {
    @Override
    public int getLayoutID() {
        return R.layout.fragment_material;
    }

    private OnStepListener onStepListener;

    public void setOnStepListener(OnStepListener onStepListener) {
        this.onStepListener = onStepListener;
    }
}
