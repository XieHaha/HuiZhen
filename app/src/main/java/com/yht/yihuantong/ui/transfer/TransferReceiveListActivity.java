package com.yht.yihuantong.ui.transfer;

import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yht.frame.data.BaseData;
import com.yht.frame.data.CommonData;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.utils.ScreenUtils;
import com.yht.frame.widgets.view.AbstractOnPageChangeListener;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ui.adapter.ViewPagerAdapter;
import com.yht.yihuantong.ui.transfer.fragment.TransferReceivedFragment;
import com.yht.yihuantong.ui.transfer.fragment.TransferWaitFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/6/27 14:17
 * @description 我收到的转诊
 */
public class TransferReceiveListActivity extends BaseActivity
        implements TransferWaitFragment.OnPendingTransferOrderListener {
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
    @BindView(R.id.tv_receiving_transfer_num)
    TextView tvReceivingTransferNum;
    @BindView(R.id.layout_receiving_transfer_num)
    RelativeLayout layoutReceivingTransferNum;
    /**
     * true为 1  false 为 0
     */
    private boolean index;

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
                    titleBar(false);
                }
                else {
                    titleBar(true);
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
        if (index) {
            move(1, calcViewBarOffset(), 0);
        }
        else {
            viewBar.setTranslationX(calcViewBarOffset());
        }
        //已接收
        TransferReceivedFragment transferReceivedFragment = new TransferReceivedFragment();
        //待处理
        TransferWaitFragment transferWaitFragment = new TransferWaitFragment();
        transferWaitFragment.setOnPendingTransferOrderListener(this);
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(transferReceivedFragment);
        fragmentList.add(transferWaitFragment);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(viewPagerAdapter);
        titleBar(index);
    }

    @Override
    public void onPendingTransferOrder(int num) {
        if (num > 0) {
            layoutReceivingTransferNum.setVisibility(View.VISIBLE);
            tvReceivingTransferNum.setText(num > BaseData.BASE_MEAASGE_DISPLAY_MIDDLE_NUM
                                           ? getString(R.string.txt_middle_num)
                                           : String.valueOf(num));
        }
        else {
            layoutReceivingTransferNum.setVisibility(View.GONE);
        }
    }

    @OnClick({ R.id.layout_left, R.id.layout_right })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_left:
                titleBar(false);
                break;
            case R.id.layout_right:
                titleBar(true);
                break;
            default:
                break;
        }
    }

    /**
     * titlebar处理
     *
     * @param index true
     */
    private void titleBar(boolean index) {
        if (!index) {
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
        int width = ScreenUtils.getScreenWidth(this);
        return (width - viewBar.getWidth() * 2) / 4;
    }

    private void move(int position, int offset, float positionOffset) {
        viewBar.setTranslationX((position * viewBar.getWidth() + offset + position * offset * 2) +
                                (positionOffset * (offset * 2 + viewBar.getWidth())));
    }
}
