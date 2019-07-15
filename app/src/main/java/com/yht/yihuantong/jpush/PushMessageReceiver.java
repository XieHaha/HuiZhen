package com.yht.yihuantong.jpush;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.yht.frame.utils.HuiZhenLog;
import com.yht.yihuantong.ui.personal.SettingActivity;

import cn.jpush.android.api.CmdMessage;
import cn.jpush.android.api.CustomMessage;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.JPushMessage;
import cn.jpush.android.api.NotificationMessage;
import cn.jpush.android.service.JPushMessageReceiver;

/**
 * @author dundun
 */
public class PushMessageReceiver extends JPushMessageReceiver {
    private static final String TAG = "ZYC";

    @Override
    public void onMessage(Context context, CustomMessage customMessage) {
        HuiZhenLog.i(TAG, "[onMessage] " + customMessage);
        processCustomMessage(context, customMessage);
    }

    @Override
    public void onNotifyMessageOpened(Context context, NotificationMessage message) {
        HuiZhenLog.i(TAG, "[onNotifyMessageOpened] " + message);
        try {
            //打开自定义的Activity
            Intent i = new Intent(context, SettingActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(JPushInterface.EXTRA_NOTIFICATION_TITLE, message.notificationTitle);
            bundle.putString(JPushInterface.EXTRA_ALERT, message.notificationContent);
            i.putExtras(bundle);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(i);
        }
        catch (Throwable throwable) {
        }
    }

    @Override
    public void onMultiActionClicked(Context context, Intent intent) {
        HuiZhenLog.i(TAG, "[onMultiActionClicked] 用户点击了通知栏按钮");
        String nActionExtra = intent.getExtras().getString(JPushInterface.EXTRA_NOTIFICATION_ACTION_EXTRA);
        //开发者根据不同 Action 携带的 extra 字段来分配不同的动作。
        if (nActionExtra == null) {
            HuiZhenLog.d(TAG, "ACTION_NOTIFICATION_CLICK_ACTION nActionExtra is null");
            return;
        }
        if (nActionExtra.equals("my_extra1")) {
            HuiZhenLog.i(TAG, "[onMultiActionClicked] 用户点击通知栏按钮一");
        }
        else if (nActionExtra.equals("my_extra2")) {
            HuiZhenLog.i(TAG, "[onMultiActionClicked] 用户点击通知栏按钮二");
        }
        else if (nActionExtra.equals("my_extra3")) {
            HuiZhenLog.i(TAG, "[onMultiActionClicked] 用户点击通知栏按钮三");
        }
        else {
            HuiZhenLog.i(TAG, "[onMultiActionClicked] 用户点击通知栏按钮未定义");
        }
    }

    @Override
    public void onNotifyMessageArrived(Context context, NotificationMessage message) {
        HuiZhenLog.i(TAG, "[onNotifyMessageArrived] " + message);
    }

    @Override
    public void onNotifyMessageDismiss(Context context, NotificationMessage message) {
        HuiZhenLog.i(TAG, "[onNotifyMessageDismiss] " + message);
    }

    @Override
    public void onRegister(Context context, String registrationId) {
        HuiZhenLog.i(TAG, "[onRegister] " + registrationId);
    }

    @Override
    public void onConnected(Context context, boolean isConnected) {
        HuiZhenLog.i(TAG, "[onConnected] " + isConnected);
    }

    @Override
    public void onCommandResult(Context context, CmdMessage cmdMessage) {
        HuiZhenLog.i(TAG, "[onCommandResult] " + cmdMessage);
    }

    @Override
    public void onTagOperatorResult(Context context, JPushMessage jPushMessage) {
        TagAliasOperatorHelper.getInstance().onTagOperatorResult(context, jPushMessage);
        super.onTagOperatorResult(context, jPushMessage);
    }

    @Override
    public void onCheckTagOperatorResult(Context context, JPushMessage jPushMessage) {
        TagAliasOperatorHelper.getInstance().onCheckTagOperatorResult(context, jPushMessage);
        super.onCheckTagOperatorResult(context, jPushMessage);
    }

    @Override
    public void onAliasOperatorResult(Context context, JPushMessage jPushMessage) {
        TagAliasOperatorHelper.getInstance().onAliasOperatorResult(context, jPushMessage);
        super.onAliasOperatorResult(context, jPushMessage);
    }

    @Override
    public void onMobileNumberOperatorResult(Context context, JPushMessage jPushMessage) {
        TagAliasOperatorHelper.getInstance().onMobileNumberOperatorResult(context, jPushMessage);
        super.onMobileNumberOperatorResult(context, jPushMessage);
    }

    //send msg to MainActivity
    private void processCustomMessage(Context context, CustomMessage customMessage) {
        String message = customMessage.message;
        String extras = customMessage.extra;
        //            LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
    }
}
