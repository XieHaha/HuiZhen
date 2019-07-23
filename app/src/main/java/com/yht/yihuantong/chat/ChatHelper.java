//package com.yht.yihuantong.chat;
//
//import android.view.View;
//
//import com.hyphenate.chat.EMMessage;
//import com.hyphenate.easeui.widget.chatrow.EaseCustomChatRowProvider;
//import com.yht.frame.data.bean.LoginBean;
//
///**
// * Created by dundun on 18/4/20.
// */
//public class ChatHelper implements EaseChatFragment.EaseChatFragmentHelper {
//    @Override
//    public void onSetMessageAttributes(EMMessage message) {
//        LoginBean bean = YihtApplication.getInstance().getLoginBean();
//        if (null == bean) {
//            return;
//        }
//        //设置自己的头像和昵称到消息扩展中
//        message.setAttribute(HxHelper.MSG_EXT_NICKNAME, bean.getName());
//        message.setAttribute(HxHelper.MSG_EXT_AVATAR, bean.getPortraitUrl());
//    }
//
//    @Override
//    public void onEnterToChatDetails() {
//    }
//
//    @Override
//    public void onAvatarClick(String username) {
//    }
//
//    @Override
//    public void onAvatarLongClick(String username) {
//    }
//
//    @Override
//    public boolean onMessageBubbleClick(EMMessage message) {
//        return false;
//    }
//
//    @Override
//    public void onMessageBubbleLongClick(EMMessage message) {
//    }
//
//    @Override
//    public boolean onExtendMenuItemClick(int itemId, View view) {
//        return false;
//    }
//
//    @Override
//    public EaseCustomChatRowProvider onSetCustomChatRowProvider() {
//        return null;
//    }
//}
