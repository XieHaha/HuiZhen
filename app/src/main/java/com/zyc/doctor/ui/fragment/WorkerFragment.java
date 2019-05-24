package com.zyc.doctor.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yht.frame.ui.BaseFragment;
import com.yht.frame.widgets.edittext.SuperEditText;
import com.zyc.doctor.R;
import com.zyc.doctor.ui.AnimFinishAdapter;

import butterknife.BindView;

/**
 * @author 顿顿
 * @date 19/5/17 14:55
 * @des 工作室
 */
public class WorkerFragment extends BaseFragment {
    @BindView(R.id.view_search_bg)
    RelativeLayout viewSearchBg;
    @BindView(R.id.view_search_cancel)
    TextView viewSearchCancel;
    @BindView(R.id.view_search_edit)
    SuperEditText viewSearchEdit;
    @BindView(R.id.view_search_layout)
    RelativeLayout viewSearchLayout;
    @BindView(R.id.view_search_list)
    RecyclerView viewSearchList;
    @BindView(R.id.view_search_edit_layout)
    RelativeLayout viewSearchEditLayout;

    @Override
    public int getLayoutID() {
        return R.layout.fragment_worker;
    }

    @Override
    public void initView(View view, @NonNull Bundle savedInstanceState) {
        super.initView(view, savedInstanceState);
        viewSearchCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTopBarVisibility(View.GONE);
            }
        });
    }

    /**
     * 设置tabbar的可见性
     */
    public void setTopBarVisibility(int visibility) {
        if (visibility == View.VISIBLE) {
            Animation toDown = new TranslateAnimation(0f, 0f, -viewSearchLayout.getMeasuredHeight(), 0f);
            toDown.setDuration(300);
            viewSearchLayout.startAnimation(toDown);
            Animation alpha = new AlphaAnimation(0, 1);
            alpha.setDuration(300);
            viewSearchBg.startAnimation(alpha);
            viewSearchLayout.setVisibility(View.VISIBLE);
            showSoftInputFromWindow(getContext(), viewSearchEdit);
            toDown.setAnimationListener(new AnimFinishAdapter() {
                @Override
                public void end() {
                    viewSearchBg.setVisibility(View.VISIBLE);
                }
            });
        }
        else {
            Animation toUp = new TranslateAnimation(0f, 0f, 0f, -viewSearchLayout.getMeasuredHeight());
            toUp.setDuration(300);
            viewSearchLayout.startAnimation(toUp);
            Animation alpha = new AlphaAnimation(1, 0);
            alpha.setDuration(300);
            viewSearchBg.startAnimation(alpha);
            hideSoftInputFromWindow(getContext(), viewSearchEdit);
            toUp.setAnimationListener(new AnimFinishAdapter() {
                @Override
                public void end() {
                    viewSearchLayout.setVisibility(View.INVISIBLE);
                    viewSearchBg.setVisibility(View.GONE);
                }
            });
        }
    }
}
