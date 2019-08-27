package com.yht.yihuantong.jpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.yht.frame.api.notify.NotifyChangeListenerManager;
import com.yht.frame.data.CommonData;
import com.yht.frame.data.bean.NotifyKeyBean;
import com.yht.frame.data.type.MessageType;
import com.yht.frame.utils.HuiZhenLog;
import com.yht.frame.utils.SharePreferenceUtil;
import com.yht.yihuantong.ui.login.AccountDisableActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;

/**
 * 1) 接收自定义消息
 *
 * @author dundun
 */
public class PushMessageReceiver extends BroadcastReceiver implements MessageType {
    private static final String TAG = "ZYC-PUSH";

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Bundle bundle = intent.getExtras();
            HuiZhenLog.i(TAG, "onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));
            if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
                try {
                    NotifyKeyBean notifyKeyBean = new Gson().fromJson(bundle.getString(JPushInterface.EXTRA_EXTRA),
                                                                      NotifyKeyBean.class);
                    pushMessageArrived(context, notifyKeyBean.getMsgType());
                }
                catch (JsonSyntaxException e) {
                    e.printStackTrace();
                    HuiZhenLog.e(TAG, "Get message extra JSON error!");
                }
            }
        }
        catch (Exception e) {
            HuiZhenLog.e(TAG, "Exception error", e);
        }
    }

    private void pushMessageArrived(Context context, String type) {
        switch (type) {
            case MESSAGE_ACCOUNT_DISABLE:
                Intent mainIntent = new Intent(context, AccountDisableActivity.class);
                mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(mainIntent);
                break;
            case MESSAGE_PROTOCOL_UPDATE:
                //通知协议更新
                NotifyChangeListenerManager.getInstance().notifyProtocolChange("");
                new SharePreferenceUtil(context).putBoolean(CommonData.KEY_IS_PROTOCOL_UPDATE_DATE, true);
                break;
            default:
                break;
        }
    }

    /**
     * 打印所有的 intent extra 数据
     *
     * @param bundle a
     * @return a
     */
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            }
            else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            }
            else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
                    HuiZhenLog.i(TAG, "This message has no Extra data");
                    continue;
                }
                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();
                    while (it.hasNext()) {
                        String myKey = it.next();
                        sb.append("\nkey:" + key + ", value: [" + myKey + " - " + json.optString(myKey) + "]");
                    }
                }
                catch (JSONException e) {
                    HuiZhenLog.e(TAG, "Get message extra JSON error!");
                }
            }
            else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }
}
