package com.yht.yihuantong.jpush;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.yht.frame.api.notify.NotifyChangeListenerManager;
import com.yht.frame.data.CommonData;
import com.yht.frame.data.bean.NotifyKeyBean;
import com.yht.frame.data.type.DocAuthStatus;
import com.yht.frame.data.type.MessageType;
import com.yht.frame.utils.HuiZhenLog;
import com.yht.yihuantong.LifecycleHandler;
import com.yht.yihuantong.ZycApplication;
import com.yht.yihuantong.ui.auth.AuthDoctorActivity;
import com.yht.yihuantong.ui.check.ServiceDetailActivity;
import com.yht.yihuantong.ui.login.LoginOptionsActivity;
import com.yht.yihuantong.ui.main.MainActivity;
import com.yht.yihuantong.ui.transfer.TransferInitiateDetailActivity;
import com.yht.yihuantong.ui.transfer.TransferReceiveDetailActivity;

import cn.jpush.android.api.NotificationMessage;
import cn.jpush.android.service.JPushMessageReceiver;

/**
 * @author dundun
 * 通知消息
 */
public class PushNotifyReceiver extends JPushMessageReceiver implements MessageType {
    private static final String TAG = "ZYC->NOTIFY";

    @Override
    public void onNotifyMessageArrived(Context context, NotificationMessage message) {
        NotifyKeyBean notifyKeyBean = new Gson().fromJson(message.notificationExtras, NotifyKeyBean.class);
        String type = notifyKeyBean.getMsgType();
        HuiZhenLog.i(TAG, notifyKeyBean.toString());
        notifyStatusChange(type);
    }

    @Override
    public void onNotifyMessageOpened(Context context, NotificationMessage message) {
        NotifyKeyBean notifyKeyBean = new Gson().fromJson(message.notificationExtras, NotifyKeyBean.class);
        HuiZhenLog.i(TAG, notifyKeyBean.toString());
        //点击通知栏刷新单条消息已读状态
        NotifyChangeListenerManager.getInstance().notifySingleMessageStatusChange(notifyKeyBean.getMsgId());
        jumpPageByType(context, notifyKeyBean.getMsgType(), notifyKeyBean.getOrderNo());
    }

    /**
     * 状态通知
     *
     * @param type
     */
    private void notifyStatusChange(String type) {
        switch (type) {
            case MESSAGE_DOCTOR_AUTH_SUCCESS:
                NotifyChangeListenerManager.getInstance().notifyDoctorAuthStatus(DocAuthStatus.AUTH_SUCCESS);
                break;
            case MESSAGE_DOCTOR_AUTH_FAILED:
                NotifyChangeListenerManager.getInstance().notifyDoctorAuthStatus(DocAuthStatus.AUTH_FAILD);
                break;
            case MESSAGE_TRANSFER_APPLY:
            case MESSAGE_TRANSFER_CANCEL:
            case MESSAGE_TRANSFER_SYSTEM_CANCEL_R:
                NotifyChangeListenerManager.getInstance().notifyDoctorTransferPatient("");
                //系统消息列表
                NotifyChangeListenerManager.getInstance().notifyMessageStatusChange("");
                break;
            default:
                //系统消息列表
                NotifyChangeListenerManager.getInstance().notifyMessageStatusChange("");
                break;
        }
    }

    /**
     * 通知栏业务处理
     *
     * @param type
     */
    private void jumpPageByType(Context context, String type, String msgId) {
        Intent mainIntent, baseIntent;
        Intent[] intents;
        if (TextUtils.isEmpty(type) || !ZycApplication.getInstance().isLoginStatus() ||
            ZycApplication.getInstance().getLoginBean() == null) {
            mainIntent = new Intent(context, LoginOptionsActivity.class);
            mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(mainIntent);
            return;
        }
        switch (type) {
            case MESSAGE_DOCTOR_AUTH_SUCCESS:
            case MESSAGE_DOCTOR_AUTH_FAILED:
                mainIntent = new Intent(context, AuthDoctorActivity.class);
                mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(mainIntent);
                break;
            case MESSAGE_SERVICE_REPORT:
                mainIntent = new Intent(context, MainActivity.class);
                mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                baseIntent = new Intent(context, ServiceDetailActivity.class);
                baseIntent.putExtra(CommonData.KEY_ORDER_ID, msgId);
                if (!LifecycleHandler.isApplicationInForeground()) {
                    intents = new Intent[] { mainIntent, baseIntent };
                    context.startActivities(intents);
                }
                else {
                    context.startActivity(baseIntent);
                }
                break;
            case MESSAGE_TRANSFER_UPDATE:
            case MESSAGE_TRANSFER_REJECT:
            case MESSAGE_TRANSFER_RECEIVED:
            case MESSAGE_TRANSFER_OTHER:
            case MESSAGE_TRANSFER_SYSTEM_CANCEL_T:
                mainIntent = new Intent(context, MainActivity.class);
                mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                baseIntent = new Intent(context, TransferInitiateDetailActivity.class);
                baseIntent.putExtra(CommonData.KEY_ORDER_ID, msgId);
                if (!LifecycleHandler.isApplicationInForeground()) {
                    intents = new Intent[] { mainIntent, baseIntent };
                    context.startActivities(intents);
                }
                else {
                    context.startActivity(baseIntent);
                }
                break;
            case MESSAGE_TRANSFER_APPLY:
            case MESSAGE_TRANSFER_CANCEL:
            case MESSAGE_TRANSFER_SYSTEM_CANCEL_R:
                mainIntent = new Intent(context, MainActivity.class);
                mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                baseIntent = new Intent(context, TransferReceiveDetailActivity.class);
                baseIntent.putExtra(CommonData.KEY_ORDER_ID, msgId);
                if (!LifecycleHandler.isApplicationInForeground()) {
                    intents = new Intent[] { mainIntent, baseIntent };
                    context.startActivities(intents);
                }
                else {
                    context.startActivity(baseIntent);
                }
                break;
            case MESSAGE_CURRENCY_ARRIVED:
            case MESSAGE_CURRENCY_DEDUCTION:
            case MESSAGE_ACCOUNT_CREATE:
            default:
                break;
        }
    }
}
