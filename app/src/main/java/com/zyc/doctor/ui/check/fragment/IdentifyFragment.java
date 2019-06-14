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
 * @des 身份确认
 */
public class IdentifyFragment extends BaseFragment {
    @BindView(R.id.et_patient_name)
    SuperEditText etPatientName;
    @BindView(R.id.et_patient_id_card)
    SuperEditText etPatientIdCard;

    @Override
    public int getLayoutID() {
        return R.layout.fragment_identify;
    }

    @OnClick(R.id.tv_identify_next)
    public void onViewClicked() {}


    private OnStepListener onStepListener;

    public void setOnStepListener(OnStepListener onStepListener) {
        this.onStepListener = onStepListener;
    }
}
