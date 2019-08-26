package com.yht.yihuantong.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.yht.frame.data.CommonData;
import com.yht.frame.data.bean.PatientBean;
import com.yht.frame.ui.AppManager;
import com.yht.frame.utils.SharePreferenceUtil;
import com.yht.yihuantong.ui.hint.HintLoginActivity;
import com.yht.yihuantong.ui.login.AccountDisableActivity;
import com.yht.yihuantong.ui.login.LoginOptionsActivity;

import org.litepal.crud.DataSupport;

import java.util.Map;

import cn.jpush.android.api.JPushInterface;

import static com.yht.frame.data.BaseData.BASE_ACCOUNT_ERROR_ACTION;
import static com.yht.frame.data.BaseData.BASE_ONE;
import static com.yht.frame.data.BaseData.BASE_TOKEN_ERROR_ACTION;

/**
 * @author 顿顿
 * @date 19/4/28 11:41
 * @des 自定义广播
 */
public class LoginOutBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent data) {
        String action = data.getAction();
        String errorHint = data.getStringExtra(CommonData.KEY_PUBLIC_STRING);
        Intent intent;
        if (BASE_TOKEN_ERROR_ACTION.equals(action)) {
            intent = new Intent(context, HintLoginActivity.class);
            intent.putExtra(CommonData.KEY_PUBLIC_STRING, errorHint);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
        else if (BASE_ACCOUNT_ERROR_ACTION.equals(action)) {
            intent = new Intent(context, AccountDisableActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
        else {
            //清除本地数据
            SharePreferenceUtil.clear(context);
            //清除数据库数据
            DataSupport.deleteAll(PatientBean.class);
            //极光推送
            JPushInterface.deleteAlias(context, BASE_ONE);
            //删除环信会话列表
            Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
            //删除和某个user会话，如果需要保留聊天记录，传false
            for (EMConversation conversation : conversations.values()) {
                EMClient.getInstance().chatManager().deleteConversation(conversation.conversationId(), true);
            }
            //退出环信
            EMClient.getInstance().logout(true);
            AppManager.getInstance().finishAllActivity();
            intent = new Intent(context, LoginOptionsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }
}
