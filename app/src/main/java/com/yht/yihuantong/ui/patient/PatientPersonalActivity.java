package com.yht.yihuantong.ui.patient;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyphenate.easeui.EaseConstant;
import com.yht.frame.data.BaseData;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.widgets.view.AbstractOnPageChangeListener;
import com.yht.frame.widgets.view.ViewPrepared;
import com.yht.yihuantong.R;
import com.yht.yihuantong.chat.EaseChatFragment;
import com.yht.yihuantong.ui.adapter.ViewPagerAdapter;
import com.yht.yihuantong.ui.patient.fragment.PatientInfoFragment;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.autosize.utils.ScreenUtils;

/**
 * @author 顿顿
 * @date 19/6/27 14:17
 * @des 患者页面（基础信息、聊天）
 */
public class PatientPersonalActivity extends BaseActivity implements EaseChatFragment.OnTimeLayoutClickListener {
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
    @BindView(R.id.public_title_bar_title)
    TextView publicTitleBarTitle;
    /**
     * 在线聊天
     */
    private EaseChatFragment easeChatFragment;
    /**
     * 开启聊天倒计时广播
     */
    private TimerReceiver timerReceiver;
    private ScheduledExecutorService executorService;
    /**
     * 倒计时
     */
    private int time = 0;
    /**
     * 开启计时器
     */
    public static final int START = 10;
    /**
     * 更新计时器
     */
    public static final int UPDATE = 20;
    /**
     * 结束计时器
     */
    public static final int END = 30;
    /**
     * 等待时间
     */
    final long awaitTime = 3 * 1000;

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_patient_personal;
    }

    private Handler handler = new Handler(message -> {
        switch (message.what) {
            case START:
                startChatTimer(24 * 60 * 60);
                if (easeChatFragment != null) {
                    easeChatFragment.startTimer();
                }
                break;
            case UPDATE:
                if (easeChatFragment != null) {
                    easeChatFragment.setTimeValue(BaseUtils.getTimeMode(time));
                }
                break;
            case END:
                if (easeChatFragment != null) {
                    easeChatFragment.endTimer();
                }
                try {
                    // 发送结束通知
                    executorService.shutdown();
                    // (所有的任务都结束的时候，返回TRUE)
                    if (!executorService.awaitTermination(awaitTime, TimeUnit.MILLISECONDS)) {
                        // 超时的时候向线程池中所有的线程发出中断(interrupted)。
                        executorService.shutdownNow();
                    }
                }
                catch (InterruptedException e) {
                    // awaitTermination方法被中断的时候也中止线程池中全部的线程的执行。
                    executorService.shutdownNow();
                }
                break;
            default:
                break;
        }
        return true;
    });

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        new ViewPrepared().asyncPrepare(tvLeft, (w, h) -> {
            ViewGroup.LayoutParams params = viewBar.getLayoutParams();
            params.width = w + BaseUtils.dp2px(this, 12);
            viewBar.setLayoutParams(params);
            viewBar.setTranslationX(calcViewBarOffset());
        });
        publicTitleBarTitle.setText("患者姓名");
        initFragment();
        initReceiver();
    }

    /**
     * 动态广播注册
     */
    private void initReceiver() {
        timerReceiver = new TimerReceiver();
        IntentFilter filter = new IntentFilter(BaseData.BASE_START_TIMER_ACTION);
        registerReceiver(timerReceiver, filter);
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
        //患者信息
        PatientInfoFragment patientInfoFragment = new PatientInfoFragment();
        //在线聊天
        easeChatFragment = new EaseChatFragment();
        easeChatFragment.setOnTimeLayoutClickListener(this);
        //传入参数
        Bundle args = new Bundle();
        args.putInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
        args.putString(EaseConstant.EXTRA_USER_ID, "18582317119_p");
        easeChatFragment.hideTitleBar();
        easeChatFragment.setArguments(args);
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(patientInfoFragment);
        fragmentList.add(easeChatFragment);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(viewPagerAdapter);
        titleBar(true);
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

    /**
     * 开启计时器
     *
     * @param remainingTime 剩余时间 (重新计时则忽略)
     */
    private void startChatTimer(int remainingTime) {
        time = remainingTime;
        executorService = new ScheduledThreadPoolExecutor(1, new BasicThreadFactory.Builder().namingPattern(
                "yht-thread-pool-%d").daemon(true).build());
        executorService.scheduleAtFixedRate(() -> {
            time--;
            if (time < 0) {
                time = 0;
                handler.sendEmptyMessage(END);
            }
            else {
                handler.sendEmptyMessage(UPDATE);
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    /**
     * 手动结束
     */
    @Override
    public void onTimeLayoutClick() {
        handler.sendEmptyMessage(END);
    }

    /**
     * 广播处理
     */
    public class TimerReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            handler.sendEmptyMessage(START);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timerReceiver != null) {
            unregisterReceiver(timerReceiver);
        }
        if (executorService != null) {
            executorService.shutdownNow();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        if (easeChatFragment != null) {
            easeChatFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onPermissionNeedExplanation(@NonNull String permissionName) {
        if (easeChatFragment != null) {
            easeChatFragment.onPermissionNeedExplanation(permissionName);
        }
    }

    @Override
    public void onNoPermissionNeeded(@NonNull Object permissionName) {
        super.onNoPermissionNeeded(permissionName);
        if (easeChatFragment != null) {
            easeChatFragment.onNoPermissionNeeded(permissionName);
        }
    }
}