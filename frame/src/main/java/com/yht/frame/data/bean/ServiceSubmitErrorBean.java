package com.yht.frame.data.bean;

import java.io.Serializable;

/**
 * @author 顿顿
 * @date 19/11/4 14:57
 * @description 预约服务提交错误
 */
public class ServiceSubmitErrorBean implements Serializable {
    private static final long serialVersionUID = 7814627175781805432L;
    private long oldPrice;
    private long newPrice;
    private String name;
    private String code;

    public long getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(long oldPrice) {
        this.oldPrice = oldPrice;
    }

    public long getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(long newPrice) {
        this.newPrice = newPrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
