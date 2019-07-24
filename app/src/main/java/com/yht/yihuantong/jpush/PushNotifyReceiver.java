package com.yht.yihuantong.jpush;

import android.content.Context;
import android.content.Intent;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.yht.frame.api.notify.NotifyChangeListenerManager;
import com.yht.frame.data.CommonData;
import com.yht.frame.data.type.DocAuthStatus;
import com.yht.frame.data.type.MessageType;
import com.yht.frame.utils.HuiZhenLog;
import com.yht.yihuantong.ZycApplication;
import com.yht.yihuantong.ui.auth.AuthDoctorActivity;
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
    private static final String TAG = "ZYC";

    @Override
    public void onNotifyMessageArrived(Context context, NotificationMessage message) {
        JsonObject jsonObject = new JsonParser().parse(message.notificationExtras).getAsJsonObject();
        String type = jsonObject.get("msgType").getAsString();
        String msgId;
        if (MESSAGE_DOCTOR_AUTH_FAILED.equals(type) || MESSAGE_DOCTOR_AUTH_SUCCESS.equals(type)) {
            msgId = jsonObject.get("phone").getAsString();
        }
        else {
            msgId = jsonObject.get("orderNo").getAsString();
        }
        HuiZhenLog.i(TAG, "msgType:" + type + "  msgId:" + msgId);
        notifyStatusChange(type, msgId);
    }

    @Override
    public void onNotifyMessageOpened(Context context, NotificationMessage message) {
        JsonObject jsonObject = new JsonParser().parse(message.notificationExtras).getAsJsonObject();
        String type = jsonObject.get("msgType").getAsString();
        String orderNo, msgId = "";
        if (MESSAGE_DOCTOR_AUTH_FAILED.equals(type) || MESSAGE_DOCTOR_AUTH_SUCCESS.equals(type)) {
            orderNo = jsonObject.get("phone").getAsString();
        }
        else {
            orderNo = jsonObject.get("orderNo").getAsString();
            msgId = jsonObject.get("msgId").getAsString();
        }
        HuiZhenLog.i(TAG, "msgType:" + type + "  orderNo:" + orderNo + "  msgId:" + msgId);
        NotifyChangeListenerManager.getInstance().notifySingleMessageStatusChange(msgId);
        jumpPageByType(context, type, orderNo);
    }

    /**
     * 状态通知
     *
     * @param type
     */
    private void notifyStatusChange(String type, String msgId) {
        switch (type) {
            case MESSAGE_DOCTOR_AUTH_SUCCESS:
                NotifyChangeListenerManager.getInstance().notifyDoctorAuthStatus(DocAuthStatus.AUTH_SUCCESS);
                break;
            case MESSAGE_DOCTOR_AUTH_FAILED:
                NotifyChangeListenerManager.getInstance().notifyDoctorAuthStatus(DocAuthStatus.AUTH_FAILD);
                break;
            default:
                break;
        }
    }

    /**
     * 通知栏业务处理
     *
     * @param type
     */
    private void jumpPageByType(Context context, String type, String msgId) {
        if (ZycApplication.getInstance().getLoginBean() == null) {
            return;
        }
        Intent mainIntent, baseIntent;
        Intent[] intents;
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
                mainIntent.putExtra(CommonData.KEY_PUBLIC, 0);
                baseIntent = new Intent(context, AuthDoctorActivity.class);
                intents = new Intent[] { mainIntent, baseIntent };
                context.startActivities(intents);
                break;
            case MESSAGE_TRANSFER_UPDATE:
                break;
            case MESSAGE_TRANSFER_APPLY:
            case MESSAGE_TRANSFER_REJECT:
            case MESSAGE_TRANSFER_RECEIVED:
            case MESSAGE_TRANSFER_OTHER:
            case MESSAGE_TRANSFER_SYSTEM_CANCEL_T:
                mainIntent = new Intent(context, MainActivity.class);
                mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mainIntent.putExtra(CommonData.KEY_PUBLIC, 0);
                baseIntent = new Intent(context, TransferInitiateDetailActivity.class);
                baseIntent.putExtra(CommonData.KEY_ORDER_ID, msgId);
                intents = new Intent[] { mainIntent, baseIntent };
                context.startActivities(intents);
                break;
            case MESSAGE_TRANSFER_CANCEL:
            case MESSAGE_TRANSFER_SYSTEM_CANCEL_R:
                mainIntent = new Intent(context, MainActivity.class);
                mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mainIntent.putExtra(CommonData.KEY_PUBLIC, 0);
                baseIntent = new Intent(context, TransferReceiveDetailActivity.class);
                baseIntent.putExtra(CommonData.KEY_ORDER_ID, msgId);
                intents = new Intent[] { mainIntent, baseIntent };
                context.startActivities(intents);
                break;
            case MESSAGE_CURRENCY_ARRIVED:
            case MESSAGE_CURRENCY_DEDUCTION:
            case MESSAGE_ACCOUNT_CREATE:
            default:
                break;
        }
    }
}
