package com.zyc.doctor.ui.main.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yht.frame.ui.BaseFragment;
import com.yht.frame.widgets.view.AbstractOnPageChangeListener;
import com.zyc.doctor.R;
import com.zyc.doctor.chat.EaseConversationListFragment;
import com.zyc.doctor.ui.adapter.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/5/17 14:55
 * @des 消息列表
 */
public class MessageFragment extends BaseFragment {
    @BindView(R.id.status_bar_fix)
    View statusBarFix;
    @BindView(R.id.layout_title)
    LinearLayout layoutTitle;
    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.layout_left)
    RelativeLayout layoutLeft;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.layout_right)
    RelativeLayout layoutRight;
    @BindView(R.id.view_bar)
    View viewBar;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.tv_read_message)
    TextView tvReadMessage;

    @Override
    public int getLayoutID() {
        return R.layout.fragment_message;
    }

    @Override
    public void initView(View view, @NonNull Bundle savedInstanceState) {
        super.initView(view, savedInstanceState);
        statusBarFix.setLayoutParams(
                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStateBarHeight(getActivity())));
        initFragment();
    }

    @Override
    public void initListener() {
        super.initListener();
        viewPager.addOnPageChangeListener(new AbstractOnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                int offset = calcViewBarOffset();
                viewBar.setTranslationX((position * viewBar.getWidth() + offset + position * offset * 2) +
                                        (positionOffset * (offset * 2 + viewBar.getWidth())));
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position == 0) {
                    titleBar(true);
                }
                else {
                    titleBar(false);
                }
            }
        });
    }

    /**
     * 碎片初始化
     */
    private void initFragment() {
        //聊天消息
        EaseConversationListFragment easeConversationListFragment = new EaseConversationListFragment();
        //通知
        NotifyMessageFragment transferWaitFragment = new NotifyMessageFragment();
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(easeConversationListFragment);
        fragmentList.add(transferWaitFragment);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(
                Objects.requireNonNull(getActivity()).getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(viewPagerAdapter);
        titleBar(true);
    }

    /**
     * titlebar处理
     *
     * @param one true 为默认
     */
    private void titleBar(boolean one) {
        if (one) {
            viewPager.setCurrentItem(0);
            tvReadMessage.setVisibility(View.GONE);
            tvLeft.setSelected(true);
            tvLeft.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            tvRight.setSelected(false);
            tvRight.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        }
        else {
            viewPager.setCurrentItem(1);
            tvReadMessage.setVisibility(View.VISIBLE);
            tvLeft.setSelected(false);
            tvLeft.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            tvRight.setSelected(true);
            tvRight.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        }
    }

    @OnClick({ R.id.layout_left, R.id.layout_right, R.id.tv_read_message })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_left:
                break;
            case R.id.layout_right:
                break;
            case R.id.tv_read_message:
                break;
            default:
                break;
        }
    }

    /**
     * 计算游标位移量
     */
    private int calcViewBarOffset() {
        //获取控件宽度
        int width = layoutTitle.getMeasuredWidth();
        return (width - viewBar.getWidth() * 2) / 4;
    }
}
