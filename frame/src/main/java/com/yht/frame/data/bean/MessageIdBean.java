package com.yht.frame.data.bean;

import java.io.Serializable;

/**
 * @author 顿顿
 * @date 19/7/15 16:21
 * @des 系统消息 所有id集合
 */
public class MessageIdBean implements Serializable {
    private static final long serialVersionUID = -5735325720124017341L;
    private String orderNo;
    private String type;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
