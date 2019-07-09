package com.yht.yihuantong.ui.transfer;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yht.frame.data.CommonData;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.widgets.view.AbstractOnPageChangeListener;
import com.yht.frame.widgets.view.ViewPrepared;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ui.adapter.ViewPagerAdapter;
import com.yht.yihuantong.ui.transfer.fragment.TransferReceivedFragment;
import com.yht.yihuantong.ui.transfer.fragment.TransferWaitFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.autosize.utils.ScreenUtils;

/**
 * @author 顿顿
 * @date 19/6/27 14:17
 * @des
 */
public class TransferApplyActivity extends BaseActivity {
    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.layout_button)
    LinearLayout layoutButton;
    @BindView(R.id.view_bar)
    View viewBar;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_transfer_apply;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        new ViewPrepared().asyncPrepare(tvLeft, (w, h) -> {
            ViewGroup.LayoutParams params = viewBar.getLayoutParams();
            params.width = w + BaseUtils.dp2px(this, 12);
            viewBar.setLayoutParams(params);
            viewBar.setTranslationX(calcViewBarOffset());
        });
        boolean index = false;
        if (getIntent() != null) {
            index = getIntent().getBooleanExtra(CommonData.KEY_PUBLIC, false);
        }
        initFragment(index);
    }

    @Override
    public void initListener() {
        super.initListener();
        viewPager.addOnPageChangeListener(new AbstractOnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                int offset = calcViewBarOffset();
                move(position, offset, positionOffset);
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
     *
     * @param index
     */
    private void initFragment(boolean index) {
        //已接收
        TransferReceivedFragment transferReceivedFragment = new TransferReceivedFragment();
        //待处理
        TransferWaitFragment transferWaitFragment = new TransferWaitFragment();
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(transferReceivedFragment);
        fragmentList.add(transferWaitFragment);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(viewPagerAdapter);
        titleBar(index);
    }

    @OnClick({ R.id.layout_left, R.id.layout_right })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_left:
                titleBar(true);
                break;
            case R.id.layout_right:
                titleBar(false);
                break;
            default:
                break;
        }
    }

    /**
     * titlebar处理
     *
     * @param one true 为默认
     */
    private void titleBar(boolean one) {
        if (one) {
            viewPager.setCurrentItem(0);
            tvLeft.setSelected(true);
            tvLeft.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            tvRight.setSelected(false);
            tvRight.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        }
        else {
            viewPager.setCurrentItem(1);
            tvLeft.setSelected(false);
            tvLeft.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            tvRight.setSelected(true);
            tvRight.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        }
    }

    /**
     * 计算游标位移量
     */
    private int calcViewBarOffset() {
        //获取屏幕宽度
        int width = ScreenUtils.getScreenSize(this)[0];
        return (width - viewBar.getWidth() * 2) / 4;
    }

    private void move(int position, int offset, float positionOffset) {
        viewBar.setTranslationX((position * viewBar.getWidth() + offset + position * offset * 2) +
                                (positionOffset * (offset * 2 + viewBar.getWidth())));
    }
}
