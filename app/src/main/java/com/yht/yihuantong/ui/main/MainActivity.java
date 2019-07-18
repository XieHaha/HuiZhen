package com.yht.yihuantong.ui.main;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.domain.EaseUser;
import com.yht.frame.api.ApiManager;
import com.yht.frame.api.notify.IChange;
import com.yht.frame.api.notify.RegisterType;
import com.yht.frame.data.BaseData;
import com.yht.frame.data.CommonData;
import com.yht.frame.data.base.LoginBean;
import com.yht.frame.data.bean.VersionBean;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.utils.HuiZhenLog;
import com.yht.frame.utils.ToastUtil;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ZycApplication;
import com.yht.yihuantong.chat.HxHelper;
import com.yht.yihuantong.chat.listener.AbstractEMContactListener;
import com.yht.yihuantong.chat.listener.AbstractEMMessageListener;
import com.yht.yihuantong.chat.receive.EaseMsgClickBroadCastReceiver;
import com.yht.yihuantong.jpush.TagAliasOperatorHelper;
import com.yht.yihuantong.ui.dialog.UpdateDialog;
import com.yht.yihuantong.ui.main.fragment.MessageFragment;
import com.yht.yihuantong.ui.main.fragment.PatientFragment;
import com.yht.yihuantong.ui.main.fragment.WorkerFragment;
import com.yht.yihuantong.version.presenter.VersionPresenter;
import com.zyc.shortcutbadge.ShortcutBadger;

import java.util.List;

import butterknife.BindView;

import static com.yht.yihuantong.jpush.TagAliasOperatorHelper.ACTION_SET;

/**
 * @author dundun
 */
public class MainActivity extends BaseActivity
        implements VersionPresenter.VersionViewListener, UpdateDialog.OnEnterClickListener {
    @BindView(R.id.act_main_tab1)
    RelativeLayout actMainTab1;
    @BindView(R.id.act_main_tab3)
    LinearLayout actMainTab3;
    @BindView(R.id.act_main_tab2)
    LinearLayout actMainTab2;
    @BindView(R.id.tv_message)
    TextView tvMessage;
    @BindView(R.id.tv_patient)
    TextView tvPatient;
    @BindView(R.id.tv_worker)
    TextView tvWorker;
    @BindView(R.id.iv_message_dot)
    ImageView ivMessageDot;
    /**
     * 碎片管理
     */
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    /**
     * 消息碎片
     */
    private MessageFragment messageFragment;
    /**
     * 工作室碎片
     */
    private WorkerFragment workerFragment;
    /**
     * 患者碎片
     */
    private PatientFragment patientFragment;
    private MyConnectionListener connectionListener;
    /**
     * 消息监听
     */
    private AbstractEMMessageListener msgListener;
    /**
     * 联系人变化监听
     */
    private AbstractEMContactListener contactListener;
    /**
     * 版本检测
     */
    private VersionPresenter mVersionPresenter;
    /**
     * 版本弹窗
     */
    private UpdateDialog updateDialog;
    private NotificationManager mNotificationManager;
    private Bitmap largeIcon = null;
    private int pendingCount = 1;
    /**
     * 消息红点
     */
    private IChange<String> messageUpdate = data -> getUnreadMessageStatus();

    @Override
    public int getLayoutID() {
        return R.layout.act_main;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        largeIcon = ((BitmapDrawable)getResources().getDrawable(R.mipmap.logo_icon)).getBitmap();
        //环信头像处理，
        HxHelper.getInstance().init(this);
        EaseUI.getInstance().setUserProfileProvider((username, callback) -> {
            LoginBean bean = getLoginBean();
            //如果是当前用户，就设置自己的昵称和头像
            if (null != bean && TextUtils.equals(bean.getDoctorCode(), username.toUpperCase())) {
                EaseUser eu = new EaseUser(username);
                eu.setNickname(bean.getDoctorName());
                eu.setAvatar(bean.getPhoto());
                callback.onSuccess(eu);
                return eu;
            }
            //否则交给HxHelper处理，从消息中获取昵称和头像
            return HxHelper.getInstance().getUser(username.toUpperCase(), callback);
        });
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        //状态栏透明
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        iNotifyChangeListenerServer = ApiManager.getInstance().getServer();
        initTab();
        //环信登录
        loginEaseChat();
        setJPushAlias(loginBean.getDoctorCode());
        //检查更新
        mVersionPresenter = new VersionPresenter(this, loginBean.getToken());
        mVersionPresenter.setVersionViewListener(this);
        mVersionPresenter.init();
    }

    /**
     * 极光alias推送设置
     *
     * @param alias
     */
    private void setJPushAlias(String alias) {
        TagAliasOperatorHelper.TagAliasBean tagAliasBean = new TagAliasOperatorHelper.TagAliasBean();
        tagAliasBean.action = ACTION_SET;
        tagAliasBean.alias = alias;
        tagAliasBean.isAliasAction = true;
        TagAliasOperatorHelper.getInstance().handleAction(getApplicationContext(), BASE_ONE, tagAliasBean);
    }

    @Override
    public void initListener() {
        super.initListener();
        actMainTab1.setOnClickListener(this);
        actMainTab2.setOnClickListener(this);
        actMainTab3.setOnClickListener(this);
        //注册患者状态监听
        iNotifyChangeListenerServer.registerMessageStatusChangeListener(messageUpdate, RegisterType.REGISTER);
        //注册一个监听连接状态的listener
        connectionListener = new MyConnectionListener();
        EMClient.getInstance().addConnectionListener(connectionListener);
        msgListener = new AbstractEMMessageListener() {
            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                //收到消息
                if (messageFragment != null) {
                    messageFragment.refresh();
                }
                initNotify();
                sendChatMsg(messages.get(0));
            }
        };
        EMClient.getInstance().chatManager().addMessageListener(msgListener);
        contactListener = new AbstractEMContactListener() {
            @Override
            public void onContactDeleted(String username) {
                //被删除时回调此方法
                //删除会话
                EMClient.getInstance().chatManager().deleteConversation(username, true);
                if (messageFragment != null) {
                    messageFragment.refresh();
                }
                //                //通知患者碎片刷新列表
                //                NotifyChangeListenerManager.getInstance().notifyPatientStatusChange("");
            }
        };
        EMClient.getInstance().contactManager().setContactListener(contactListener);
    }

    /**
     * 初始化tabs
     */
    private void initTab() {
        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        tabWorkerView();
    }

    /**
     * 登录环信聊天
     */
    private void loginEaseChat() {
        boolean isLogin = false;
        if (getIntent() != null) {
            isLogin = getIntent().getBooleanExtra(CommonData.KEY_EASE_LOGIN_STATUS, false);
        }
        //避免重复登录
        if (isLogin) {
            return;
        }
        EMClient.getInstance()
                .login(loginBean.getDoctorCode().toLowerCase(), BaseData.BASE_EASE_DEFAULT_PWD, new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        EMClient.getInstance().groupManager().loadAllGroups();
                        EMClient.getInstance().chatManager().loadAllConversations();
                        runOnUiThread(() -> HuiZhenLog.i(TAG, getString(R.string.txt_login_ease_success)));
                    }

                    @Override
                    public void onProgress(int progress, String status) {
                    }

                    @Override
                    public void onError(int code, String message) {
                        HuiZhenLog.i(TAG, getString(R.string.txt_login_ease_error));
                        ToastUtil.toast(MainActivity.this, R.string.txt_login_ease_error);
                    }
                });
    }

    private void initNotify() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = BaseData.BASE_CHAT_CHANNEL;
            String channelName = "聊天消息";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            createNotificationChannel(channelId, channelName, importance);
        }
        else {
            mNotificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        }
    }

    /**
     * 未读消息
     */
    public void updateUnReadCount() {
        int systemMessage = sharePreferenceUtil.getInt(CommonData.KEY_SYSTEM_MESSAGE_UNREAD_STATUS);
        int easeMessage = sharePreferenceUtil.getInt(CommonData.KEY_EASE_MESSAGE_UNREAD_STATUS);
        if (systemMessage > 0 || easeMessage > 0) {
            ivMessageDot.setVisibility(View.VISIBLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                //                sendSubscribeMsg(msgUnReadCount);
            }
            else {
                setShortcutBadge(systemMessage + easeMessage);
            }
        }
        else {
            ivMessageDot.setVisibility(View.INVISIBLE);
            removeShortcutBadge();
        }
    }

    /**
     * 设置角标
     */
    private void setShortcutBadge(int badgeCount) {
        ShortcutBadger.applyCount(this, badgeCount);
    }

    /**
     * 移除角标
     */
    private void removeShortcutBadge() {
        ShortcutBadger.removeCount(this);
    }

    public void sendChatMsg(EMMessage message) {
        //当前消息发送者与正在聊天界面对象一致时，不显示通知
        if (message.getFrom().equals(ZycApplication.getInstance().getChatId())) {
            return;
        }
        if (pendingCount > BaseData.BASE_PENDING_COUNT) {
            pendingCount = 1;
        }
        pendingCount++;
        Intent intent = new Intent(MainActivity.this, EaseMsgClickBroadCastReceiver.class);
        intent.putExtra(CommonData.KEY_CHAT_ID, message.getFrom());
        intent.setAction("ease.msg.android.intent.CLICK");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, pendingCount, intent,
                                                                 PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder = new NotificationCompat.Builder(this, BaseData.BASE_CHAT_CHANNEL);
            builder.setLargeIcon(largeIcon);
            builder.setChannelId(BaseData.BASE_CHAT_CHANNEL);
        }
        else {
            builder = new NotificationCompat.Builder(this, null);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setSmallIcon(R.mipmap.icon_alpha_logo);
        }
        else {
            builder.setSmallIcon(R.mipmap.logo_icon);
        }
        builder.setAutoCancel(true);
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        builder.setContentTitle("会珍");
        builder.setContentText("收到新的消息");
        builder.setContentIntent(pendingIntent);
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        builder.setWhen(System.currentTimeMillis());
        mNotificationManager.notify(message.getFrom(), pendingCount, builder.build());
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createNotificationChannel(String channelId, String channelName, int importance) {
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        mNotificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        channel.setLightColor(Color.GREEN);
        channel.enableVibration(true);
        mNotificationManager.createNotificationChannel(channel);
    }

    /**
     * 未读消息状态  小红点
     */
    private void getUnreadMessageStatus() {
        int systemMessage = sharePreferenceUtil.getInt(CommonData.KEY_SYSTEM_MESSAGE_UNREAD_STATUS);
        int easeMessage = sharePreferenceUtil.getInt(CommonData.KEY_EASE_MESSAGE_UNREAD_STATUS);
        if (systemMessage > 0 || easeMessage > 0) {
            ivMessageDot.setVisibility(View.VISIBLE);
        }
        else {
            ivMessageDot.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.act_main_tab1:
                tabMessageView();
                break;
            case R.id.act_main_tab2:
                tabWorkerView();
                break;
            case R.id.act_main_tab3:
                tabPatientView();
                break;
            default:
                tabMessageView();
                break;
        }
    }

    public class MyConnectionListener implements EMConnectionListener {
        @Override
        public void onConnected() {
        }

        @Override
        public void onDisconnected(final int error) {
            runOnUiThread(() -> {
                if (error == EMError.USER_REMOVED) {
                    HuiZhenLog.e("test", "账号被删除");
                }
                else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                    ToastUtil.toast(MainActivity.this, "账号在其他设备登录");
                }
            });
        }
    }

    private void tabMessageView() {
        transaction = fragmentManager.beginTransaction();
        hideAll(transaction);
        if (messageFragment == null) {
            messageFragment = new MessageFragment();
            transaction.add(R.id.act_main_tab_frameLayout, messageFragment);
        }
        else {
            transaction.show(messageFragment);
            messageFragment.onResume();
        }
        transaction.commitAllowingStateLoss();
        selectTab(0);
    }

    private void tabWorkerView() {
        transaction = fragmentManager.beginTransaction();
        hideAll(transaction);
        if (workerFragment == null) {
            workerFragment = new WorkerFragment();
            transaction.add(R.id.act_main_tab_frameLayout, workerFragment);
        }
        else {
            transaction.show(workerFragment);
            workerFragment.onResume();
        }
        transaction.commitAllowingStateLoss();
        selectTab(1);
    }

    private void tabPatientView() {
        transaction = fragmentManager.beginTransaction();
        hideAll(transaction);
        if (patientFragment == null) {
            patientFragment = new PatientFragment();
            transaction.add(R.id.act_main_tab_frameLayout, patientFragment);
        }
        else {
            transaction.show(patientFragment);
            patientFragment.onResume();
        }
        transaction.commitAllowingStateLoss();
        selectTab(2);
    }

    /**
     * 隐藏所有碎片
     */
    private void hideAll(FragmentTransaction transaction) {
        if (messageFragment != null) {
            transaction.hide(messageFragment);
        }
        if (workerFragment != null) {
            transaction.hide(workerFragment);
        }
        if (patientFragment != null) {
            transaction.hide(patientFragment);
        }
    }

    /**
     * 选中tab
     */
    private void selectTab(int index) {
        switch (index) {
            case 0:
                tvMessage.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                tvWorker.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                tvPatient.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                actMainTab1.setSelected(true);
                actMainTab2.setSelected(false);
                actMainTab3.setSelected(false);
                break;
            case 1:
                tvMessage.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                tvWorker.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                tvPatient.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                actMainTab1.setSelected(false);
                actMainTab2.setSelected(true);
                actMainTab3.setSelected(false);
                break;
            case 2:
                tvMessage.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                tvWorker.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                tvPatient.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                actMainTab1.setSelected(false);
                actMainTab2.setSelected(false);
                actMainTab3.setSelected(true);
                break;
            default:
                tvMessage.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                tvWorker.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                tvPatient.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                actMainTab1.setSelected(true);
                actMainTab2.setSelected(false);
                actMainTab3.setSelected(false);
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        if (workerFragment != null) {
            workerFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onPermissionNeedExplanation(@NonNull String permissionName) {
        if (workerFragment != null) {
            workerFragment.onPermissionNeedExplanation(permissionName);
        }
    }

    @Override
    public void onNoPermissionNeeded(@NonNull Object permissionName) {
        if (workerFragment != null) {
            workerFragment.onNoPermissionNeeded(permissionName);
        }
    }

    /*********************版本更新回调*************************/
    @Override
    public void updateVersion(VersionBean version, int mode, boolean isDownLoading) {
        if (mode == -1) {
            return;
        }
        updateDialog = new UpdateDialog(this);
        updateDialog.setCancelable(false);
        updateDialog.setUpdateMode(mode).setData(version.getNotes());
        updateDialog.setOnEnterClickListener(this);
        updateDialog.show();
    }

    @Override
    public void updateLoading(long total, long current) {
        if (updateDialog != null && updateDialog.isShowing()) {
            updateDialog.setProgressValue(total, current);
        }
    }

    /**
     * 当无可用网络时回调
     */
    @Override
    public void updateNetWorkError() {
    }

    @Override
    public void onEnter(boolean isMustUpdate) {
        mVersionPresenter.getNewAPK(isMustUpdate);
        ToastUtil.toast(this, R.string.txt_download_hint);
    }

    /**
     * 返回键 后台运行
     * 如果前一个activity未finish  会导致无法返回到后台
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        iNotifyChangeListenerServer.registerMessageStatusChangeListener(messageUpdate, RegisterType.UNREGISTER);
        //移除监听
        EMClient.getInstance().removeConnectionListener(connectionListener);
        EMClient.getInstance().chatManager().removeMessageListener(msgListener);
        EMClient.getInstance().contactManager().removeContactListener(contactListener);
    }
}
