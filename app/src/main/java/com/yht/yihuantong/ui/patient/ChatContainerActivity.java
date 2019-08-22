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
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.widget.chatrow.EaseChatRowVoicePlayer;
import com.yht.frame.data.BaseData;
import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.CommonData;
import com.yht.frame.data.Tasks;
import com.yht.frame.data.bean.ChatTimeBean;
import com.yht.frame.data.bean.DoctorBean;
import com.yht.frame.data.bean.PatientBean;
import com.yht.frame.http.retrofit.RequestUtils;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.utils.ScreenUtils;
import com.yht.frame.widgets.dialog.HintDialog;
import com.yht.frame.widgets.view.AbstractOnPageChangeListener;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ZycApplication;
import com.yht.yihuantong.chat.EaseChatFragment;
import com.yht.yihuantong.ui.adapter.ViewPagerAdapter;
import com.yht.yihuantong.ui.patient.fragment.PatientInfoFragment;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/6/27 14:17
 * @des 聊天容器界面（包含患者页面（基础信息、聊天）、医生聊天）
 */
public class ChatContainerActivity extends BaseActivity implements EaseChatFragment.OnTimeLayoutClickListener {
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
    @BindView(R.id.line)
    View line;
    @BindView(R.id.public_title_bar_right_img)
    ImageView publicTitleBarRightImg;
    /**
     * 在线聊天
     */
    private EaseChatFragment easeChatFragment;
    /**
     * 患者信息
     */
    private PatientInfoFragment patientInfoFragment;
    /**
     * 开启聊天倒计时广播
     */
    private TimerReceiver timerReceiver;
    private String chatCode, chatName, doctorPhone;
    private ScheduledExecutorService executorService;
    /**
     * 聊天
     */
    private boolean isChat;
    /**
     * 医生聊天
     */
    private boolean isDoctor;
    /**
     * 倒计时
     */
    private long time = 0;
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
    private Handler handler = new Handler(message -> {
        switch (message.what) {
            case START:
                startChatTimer();
                if (easeChatFragment != null && easeChatFragment.isAdded()) {
                    easeChatFragment.startTimer();
                }
                break;
            case UPDATE:
                if (easeChatFragment != null && easeChatFragment.isAdded()) {
                    easeChatFragment.setTimeValue(BaseUtils.getTimeMode(time));
                }
                break;
            case END:
                if (easeChatFragment != null && easeChatFragment.isAdded()) {
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
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_patient_personal;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        if (getIntent() != null) {
            chatCode = getIntent().getStringExtra(CommonData.KEY_CHAT_ID);
            chatName = getIntent().getStringExtra(CommonData.KEY_CHAT_NAME);
            isChat = getIntent().getBooleanExtra(CommonData.KEY_PATIENT_CHAT, false);
            isDoctor = getIntent().getBooleanExtra(CommonData.KEY_DOCTOR_CHAT, false);
        }
        //通知显示问题
        if (!TextUtils.isEmpty(chatCode)) {
            if (isDoctor) {
                List<DoctorBean> list = DataSupport.where("doctorCode = ?", chatCode.toUpperCase())
                                                   .find(DoctorBean.class);
                if (list != null && list.size() > 0) {
                    DoctorBean bean = list.get(0);
                    chatName = bean.getDoctorName();
                    doctorPhone = bean.getMobile();
                }
                publicTitleBarRightImg.setVisibility(View.VISIBLE);
            }
            else {
                List<PatientBean> list = DataSupport.where("code = ?", chatCode.toUpperCase()).find(PatientBean.class);
                if (list != null && list.size() > 0) {
                    PatientBean bean = list.get(0);
                    chatName = bean.getName();
                }
            }
            publicTitleBarTitle.setText(chatName);
            ZycApplication.getInstance().setChatId(chatCode.toLowerCase());
        }
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        initFragment();
        if (isDoctor) {
            layoutButton.setVisibility(View.GONE);
            viewBar.setVisibility(View.GONE);
            line.setVisibility(View.GONE);
            publicTitleBarTitle.post(() -> {
                if (easeChatFragment != null && easeChatFragment.isAdded()) {
                    easeChatFragment.hideStartButton();
                }
            });
        }
        else {
            viewBar.setTranslationX(calcViewBarOffset());
            initReceiver();
            getChatLastTime();
        }
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
                    titleBar(false);
                }
                else {
                    titleBar(true);
                }
            }
        });
    }


    /**
     * 开始聊天
     */
    private void startChat() {
        RequestUtils.startChat(this, loginBean.getToken(), loginBean.getDoctorCode(), chatCode, this);
    }

    /**
     * 获取聊天剩余时间
     */
    private void getChatLastTime() {
        RequestUtils.getChatLastTime(this, loginBean.getToken(), loginBean.getDoctorCode(), chatCode, this);
    }

    /**
     * 结束聊天
     */
    private void endChat() {
        RequestUtils.endChat(this, loginBean.getToken(), loginBean.getDoctorCode(), chatCode, this);
    }

    /**
     * 动态广播注册
     */
    private void initReceiver() {
        timerReceiver = new TimerReceiver();
        IntentFilter filter = new IntentFilter(BaseData.BASE_START_TIMER_ACTION);
        registerReceiver(timerReceiver, filter);
    }

    /**
     * 碎片初始化
     */
    private void initFragment() {
        //患者信息
        patientInfoFragment = new PatientInfoFragment();
        patientInfoFragment.setPatientCode(chatCode);
        //在线聊天
        easeChatFragment = new EaseChatFragment();
        easeChatFragment.setOnTimeLayoutClickListener(this);
        //传入参数
        Bundle args = new Bundle();
        args.putInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
        args.putString(EaseConstant.EXTRA_USER_ID, chatCode.toLowerCase());
        easeChatFragment.hideTitleBar();
        easeChatFragment.setArguments(args);
        List<Fragment> fragmentList = new ArrayList<>();
        if (!isDoctor) {
            fragmentList.add(patientInfoFragment);
        }
        fragmentList.add(easeChatFragment);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(viewPagerAdapter);
        titleBar(isChat);
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

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        switch (task) {
            case START_CHAT:
            case GET_CHAT_LAST_TIME:
                ChatTimeBean chatTimeBean = (ChatTimeBean)response.getData();
                if (chatTimeBean == null || chatTimeBean.getStopTime() == 0) {
                    return;
                }
                time = chatTimeBean.getStopTime() / 1000;
                handler.sendEmptyMessage(START);
                break;
            case END_CHAT:
                handler.sendEmptyMessage(END);
                break;
            default:
                break;
        }
    }

    /**
     * titlebar处理
     *
     * @param isChat 聊天
     */
    private void titleBar(boolean isChat) {
        if (!isChat) {
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

    /**
     * 开启计时器
     */
    private void startChatTimer() {
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
        endChat();
    }

    @OnClick(R.id.public_title_bar_right_img)
    public void onViewClicked() {
        new HintDialog(this).setPhone(getString(R.string.txt_contact_doctor_phone), doctorPhone)
                            .setOnEnterClickListener(() -> callPhone(doctorPhone))
                            .show();
    }

    /**
     * 广播处理
     */
    public class TimerReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //开启聊天
            runOnUiThread(() -> startChat());
        }
    }

    private void stopVoice() {
        EaseChatRowVoicePlayer voicePlayer = EaseChatRowVoicePlayer.getInstance(this);
        if (voicePlayer != null && voicePlayer.isPlaying()) {
            voicePlayer.stop();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopVoice();
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
        ZycApplication.getInstance().setChatId("");
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
