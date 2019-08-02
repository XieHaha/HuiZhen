package com.yht.yihuantong.chat.receive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.yht.frame.data.CommonData;
import com.yht.yihuantong.ZycApplication;
import com.yht.yihuantong.ui.main.MainActivity;
import com.yht.yihuantong.ui.patient.PatientPersonalActivity;

/**
 * @author dundun
 * @date 19/2/23
 */
public class EaseMsgClickBroadCastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if ("ease.msg.android.intent.CLICK".equals(intent.getAction())) {
            if (ZycApplication.getInstance().getLoginBean() == null) {
                return;
            }
            String chatId = intent.getStringExtra(CommonData.KEY_CHAT_ID);
            Intent mainIntent, baseIntent;
            Intent[] intents;
            mainIntent = new Intent(context, MainActivity.class);
            mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            baseIntent = new Intent(context, PatientPersonalActivity.class);
            baseIntent.putExtra(CommonData.KEY_PATIENT_CODE, chatId);
            baseIntent.putExtra(CommonData.KEY_PATIENT_CHAT, true);
            intents = new Intent[] { mainIntent, baseIntent };
            context.startActivities(intents);
        }
    }
}
