package com.zyc.doctor.ui.main.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yht.frame.ui.BaseFragment;
import com.zyc.doctor.R;

import butterknife.BindView;

/**
 * @author 顿顿
 * @date 19/5/17 14:55
 * @des 患者列表
 */
public class PatientFragment extends BaseFragment {
    @BindView(R.id.status_bar_fix)
    View statusBarFix;
    @BindView(R.id.public_main_title)
    TextView publicMainTitle;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    @Override
    public int getLayoutID() {
        return R.layout.fragment_patient;
    }

    @Override
    public void initView(View view, @NonNull Bundle savedInstanceState) {
        super.initView(view, savedInstanceState);
        publicMainTitle.setText(R.string.title_patient);
        statusBarFix.setLayoutParams(
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStateBarHeight(getActivity())));
    }
}
