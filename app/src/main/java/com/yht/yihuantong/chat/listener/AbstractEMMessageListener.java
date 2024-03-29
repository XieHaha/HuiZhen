package com.yht.yihuantong.chat.listener;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMMessage;

import java.util.List;

/**
 * @author dundun
 * @date ${DATA} 14:52
 * @description
 */
public abstract class AbstractEMMessageListener implements EMMessageListener {

    @Override
    public void onMessageReceived(List<EMMessage> list) {

    }

    @Override
    public void onCmdMessageReceived(List<EMMessage> messages) {
        //收到透传消息
    }

    @Override
    public void onMessageRead(List<EMMessage> messages) {
        //收到已读回执
    }

    @Override
    public void onMessageDelivered(List<EMMessage> message) {
        //收到已送达回执
    }

    @Override
    public void onMessageRecalled(List<EMMessage> messages) {
        //消息被撤回
    }

    @Override
    public void onMessageChanged(EMMessage message, Object change) {
        //消息状态变动
    }
}
