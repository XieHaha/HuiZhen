package com.yht.frame.data.bean;

import java.io.Serializable;

/**
 * @author 顿顿
 * @date 19/8/14 10:22
 * @description
 */
public class QrWeChatCodeBean implements Serializable {
    private static final long serialVersionUID = -5991451326090655840L;
    private long expireTime;
    private String wxQrUrl;

    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }

    public String getWxQrUrl() {
        return wxQrUrl;
    }

    public void setWxQrUrl(String wxQrUrl) {
        this.wxQrUrl = wxQrUrl;
    }
}
