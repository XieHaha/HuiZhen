package com.zyc.doctor.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.yht.frame.data.bean.CooperateDocBean;
import com.yht.frame.data.bean.PatientBean;
import com.yht.frame.ui.AppManager;
import com.yht.frame.utils.SharePreferenceUtil;
import com.zyc.doctor.ui.login.LoginActivity;

import org.litepal.crud.DataSupport;

import java.util.Map;


/**
 * @author 顿顿
 * @date 19/4/28 11:41
 * @des
 */
public class LoginOutBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //清除本地数据
        SharePreferenceUtil.clear(context);
        //清除数据库数据
        DataSupport.deleteAll(PatientBean.class);
        DataSupport.deleteAll(CooperateDocBean.class);
        //极光推送
//        JPushInterface.deleteAlias(context, 100);
        //删除环信会话列表
        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        //删除和某个user会话，如果需要保留聊天记录，传false
        for (EMConversation conversation : conversations.values()) {
            EMClient.getInstance().chatManager().deleteConversation(conversation.conversationId(), true);
        }
        //退出环信
        EMClient.getInstance().logout(true);
        AppManager.getInstance().finishAllActivity();
        Intent intent1 = new Intent(context, LoginActivity.class);
        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent1);
    }
}
