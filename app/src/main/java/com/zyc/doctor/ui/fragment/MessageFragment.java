package com.zyc.doctor.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.yht.frame.ui.BaseFragment;
import com.zyc.doctor.R;

/**
 * @author 顿顿
 * @date 19/5/17 14:55
 * @des 消息列表
 */
public class MessageFragment extends BaseFragment {
    @Override
    public int getLayoutID() {
        return R.layout.public_recyclerview_layout;
    }

    @Override
    public void initView(View view, @NonNull Bundle savedInstanceState) {
        super.initView(view, savedInstanceState);
        //获取状态栏高度，填充
        View mStateBarFixer = view.findViewById(R.id.status_bar_fix);
        mStateBarFixer.setLayoutParams(
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStateBarHeight(getActivity())));
    }
}
