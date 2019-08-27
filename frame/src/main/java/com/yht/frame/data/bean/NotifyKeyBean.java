package com.yht.frame.data.bean;

import java.io.Serializable;

/**
 * @author 顿顿
 * @date 19/7/24 14:06
 * @description
 */
public class NotifyKeyBean implements Serializable {
    private static final long serialVersionUID = 6822439434282194327L;
    private String msgType;
    private String msgId;
    private String phone;
    private String orderNo;

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    @Override
    public String toString() {
        return "NotifyKeyBean:" + " msgType:" + msgType + ",msgId:" + msgId + ",phone:" + phone + ",orderNo:" + orderNo;
    }
}
