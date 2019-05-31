package com.zyc.doctor.chat.receive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * @author dundun
 * @date 19/2/23
 */
public class EaseMsgClickBroadCastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if ("ease.msg.android.intent.CLICK".equals(intent.getAction())) {
            //            if (ZycApplication.getInstance().getLoginSuccessBean() == null) {
            //                return;
            //            }
            //            String chatId = intent.getStringExtra(CommonData.KEY_CHAT_ID);
            //            Intent mainIntent, baseIntent;
            //            Intent[] intents;
            //            mainIntent = new Intent(context, MainActivity.class);
            //            mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //            mainIntent.putExtra(CommonData.KEY_PUBLIC, 1);
            //            baseIntent = new Intent(context, ChatActivity.class);
            //            baseIntent.putExtra(CommonData.KEY_CHAT_ID, chatId);
            //            intents = new Intent[] { mainIntent, baseIntent };
            //            context.startActivities(intents);
        }
    }
}
