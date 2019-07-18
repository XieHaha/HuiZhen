package com.yht.yihuantong.ui.main.fragment;

import android.app.Service;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.yht.frame.api.notify.NotifyChangeListenerManager;
import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.CommonData;
import com.yht.frame.data.Tasks;
import com.yht.frame.data.base.MessageTotalBean;
import com.yht.frame.http.retrofit.RequestUtils;
import com.yht.frame.ui.BaseFragment;
import com.yht.frame.utils.HuiZhenLog;
import com.yht.frame.utils.ToastUtil;
import com.yht.frame.widgets.dialog.HintDialog;
import com.yht.frame.widgets.view.AbstractOnPageChangeListener;
import com.yht.yihuantong.R;
import com.yht.yihuantong.chat.EaseConversationListFragment;
import com.yht.yihuantong.chat.listener.AbstractEMContactListener;
import com.yht.yihuantong.chat.listener.AbstractEMMessageListener;
import com.yht.yihuantong.ui.adapter.ViewPagerAdapter;
import com.yht.yihuantong.ui.patient.PatientPersonalActivity;

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
public class MessageFragment extends BaseFragment
        implements EaseConversationListFragment.EaseConversationListItemClickListener,
                   EaseConversationListFragment.EaseConversationListItemLongClickListener,
                   NotifyMessageFragment.OnMessageUpdateListener {
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
    @BindView(R.id.iv_message_dot)
    ImageView ivMessageDot;
    @BindView(R.id.iv_notify_dot)
    ImageView ivNotifyDot;
    /**
     * 震动
     */
    private Vibrator vibrator;
    /**
     * message 操作弹框view
     */
    private View messagePop;
    private TextView tvDelete;
    /**
     * message 操作弹框
     */
    private PopupWindow popupWindow;
    /**
     * 弹窗具体坐标
     */
    private int[] location = new int[2];
    /**
     * 屏幕高度
     */
    private int screenHeight;
    /**
     * popup
     */
    private int popupHeight;
    /**
     * 未读消息总数
     */
    private int msgUnReadCount = 0;
    /**
     * 当前选中会话
     */
    private EMConversation curConversation;
    /**
     * 聊天消息
     */
    private EaseConversationListFragment easeConversationListFragment;
    /**
     * 环信账号连接
     */
    private EaseConnectionListener connectionListener;
    /**
     * 消息监听
     */
    private AbstractEMMessageListener msgListener;
    /**
     * 联系人变化监听
     */
    private AbstractEMContactListener contactListener;

    @Override
    public int getLayoutID() {
        return R.layout.fragment_message;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUnReadCount();
    }

    @Override
    public void initView(View view, @NonNull Bundle savedInstanceState) {
        super.initView(view, savedInstanceState);
        statusBarFix.setLayoutParams(
                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStateBarHeight(getActivity())));
        vibrator = (Vibrator)getActivity().getSystemService(Service.VIBRATOR_SERVICE);
        messagePop = LayoutInflater.from(getContext()).inflate(R.layout.message_pop_menu, null);
        tvDelete = messagePop.findViewById(R.id.message_pop_menu_play);
        initFragment();
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        getAppUnReadMessageTotal();
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
        initEaseListener();
    }

    /**
     * 获取未读消息总数
     */
    private void getAppUnReadMessageTotal() {
        RequestUtils.getAppUnReadMessageTotal(getContext(), loginBean.getToken(), this);
    }

    /**
     * 消息全部已读
     */
    private void updateAppUnReadMessageAll() {
        RequestUtils.updateAppUnReadMessageAll(getContext(), loginBean.getToken(), this);
    }

    /**
     * 环信监听
     */
    private void initEaseListener() {
        tvDelete.setOnClickListener(v -> {
            popupWindow.dismiss();
            HintDialog hintDialog = new HintDialog(getContext());
            hintDialog.setContentString(getString(R.string.txt_delete_chat_item_hint));
            hintDialog.setOnEnterClickListener(() -> {
                if (curConversation != null) {
                    //删除和某个user会话，如果需要保留聊天记录，传false
                    EMClient.getInstance().chatManager().deleteConversation(curConversation.conversationId(), false);
                    refresh();
                }
            });
            hintDialog.show();
        });
    }

    public void refresh() {
        //收到消息
        if (easeConversationListFragment != null) {
            easeConversationListFragment.refresh();
        }
    }

    /**
     * 未读消息
     */
    public void updateUnReadCount() {
        msgUnReadCount = EMClient.getInstance().chatManager().getUnreadMessageCount();
        if (msgUnReadCount > 0) {
            ivMessageDot.setVisibility(View.VISIBLE);
        }
        else {
            ivMessageDot.setVisibility(View.INVISIBLE);
        }
        sharePreferenceUtil.putInt(CommonData.KEY_EASE_MESSAGE_UNREAD_STATUS, msgUnReadCount);
        NotifyChangeListenerManager.getInstance().notifyMessageStatusChange("");
    }

    /**
     * 会话列表点击
     *
     * @param conversation -- clicked item
     */
    @Override
    public void onListItemClicked(EMConversation conversation) {
        Intent intent = new Intent(getContext(), PatientPersonalActivity.class);
        intent.putExtra(CommonData.KEY_PATIENT_CODE, conversation.conversationId().toUpperCase());
        intent.putExtra(CommonData.KEY_PATIENT_CHAT, true);
        startActivity(intent);
    }

    /**
     * 会话列表长按
     *
     * @param view
     * @param conversation
     */
    @Override
    public void onListItemLongClick(View view, EMConversation conversation) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(30, VibrationEffect.DEFAULT_AMPLITUDE));
        }
        else {
            vibrator.vibrate(30);
        }
        curConversation = conversation;
        initPopwindow(view, popupLocation(view));
    }

    @Override
    public void onMessageUpdate() {
        getAppUnReadMessageTotal();
    }

    /**
     * 抢登录 或者删除账号
     */
    public class EaseConnectionListener implements EMConnectionListener {
        @Override
        public void onConnected() {
        }

        @Override
        public void onDisconnected(final int error) {
            getActivity().runOnUiThread(() -> {
                if (error == EMError.USER_REMOVED) {
                    HuiZhenLog.e(TAG, "账号被删除");
                }
                else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                    ToastUtil.toast(getContext(), "账号在其他设备登录");
                }
            });
        }
    }

    /**
     * @param contentTv 弹框依赖view
     * @param location  弹框坐标
     */
    public void initPopwindow(View contentTv, int[] location) {
        if (popupWindow == null) {
            popupWindow = new PopupWindow(LinearLayout.LayoutParams.WRAP_CONTENT,
                                          LinearLayout.LayoutParams.WRAP_CONTENT);
        }
        popupWindow.setFocusable(true);
        popupWindow.setContentView(messagePop);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        popupWindow.setOutsideTouchable(true);
        popOutShadow(popupWindow);
        popupWindow.showAtLocation(contentTv, Gravity.NO_GRAVITY, location[0], location[1] + contentTv.getHeight());
    }

    /**
     * popup位置计算
     *
     * @param view
     * @return
     */
    private int[] popupLocation(View view) {
        view.getLocationOnScreen(location);
        int viewHeight = view.getHeight();
        int viewWidth = view.getWidth();
        if (screenHeight - location[1] > (popupHeight + popupHeight / 2)) {
            location[0] = location[0] + viewWidth / 2;
            location[1] = location[1] - viewHeight / 2;
        }
        else {
            location[0] = location[0] + viewWidth / 2;
            location[1] = location[1] - popupHeight - viewHeight / 2;
        }
        return location;
    }

    /**
     * 让popupwindow以外区域阴影显示
     *
     * @param popupWindow
     */
    private void popOutShadow(PopupWindow popupWindow) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        //设置阴影透明度
        lp.alpha = 0.8f;
        getActivity().getWindow().setAttributes(lp);
        popupWindow.setOnDismissListener(() -> {
            WindowManager.LayoutParams lp1 = getActivity().getWindow().getAttributes();
            lp1.alpha = 1f;
            getActivity().getWindow().setAttributes(lp1);
        });
    }

    /**
     * 碎片初始化
     */
    private void initFragment() {
        //聊天消息
        easeConversationListFragment = new EaseConversationListFragment();
        easeConversationListFragment.setConversationListItemClickListener(this);
        easeConversationListFragment.setConversationListItemLongClickListener(this);
        //通知
        NotifyMessageFragment notifyMessageFragment = new NotifyMessageFragment();
        notifyMessageFragment.setOnMessageUpdateListener(this);
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(easeConversationListFragment);
        fragmentList.add(notifyMessageFragment);
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
                if (tvReadMessage.isSelected()) {
                    updateAppUnReadMessageAll();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        switch (task) {
            case GET_APP_UNREAD_MESSAGE_TOTAL:
                MessageTotalBean messageTotalBean = (MessageTotalBean)response.getData();
                if (messageTotalBean.getTotal() > 0) {
                    tvReadMessage.setSelected(true);
                    ivNotifyDot.setVisibility(View.VISIBLE);
                }
                else {
                    tvReadMessage.setSelected(false);
                    ivNotifyDot.setVisibility(View.INVISIBLE);
                }
                //小红点处理
                sharePreferenceUtil.putInt(CommonData.KEY_SYSTEM_MESSAGE_UNREAD_STATUS, messageTotalBean.getTotal());
                NotifyChangeListenerManager.getInstance().notifyMessageStatusChange("");
                break;
            case UPDATE_APP_UNREAD_MESSAGE_ALL:
                tvReadMessage.setSelected(false);
                ivNotifyDot.setVisibility(View.INVISIBLE);
                //小红点处理
                sharePreferenceUtil.putInt(CommonData.KEY_SYSTEM_MESSAGE_UNREAD_STATUS, 0);
                NotifyChangeListenerManager.getInstance().notifyMessageStatusChange("");
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
