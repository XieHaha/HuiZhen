package com.yht.frame.data.bean;

import java.io.Serializable;

/**
 * @author 顿顿
 * @date 19/8/14 10:22
 * @description
 */
public class QrCodeBean implements Serializable {
    private static final long serialVersionUID = 5071743849994487696L;
    private String title;
    private String mode;
    private String content;
    private String userQr;
    private QrWeChatCodeBean wxQr;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserQr() {
        return userQr;
    }

    public void setUserQr(String userQr) {
        this.userQr = userQr;
    }

    public QrWeChatCodeBean getWxQr() {
        return wxQr;
    }

    public void setWxQr(QrWeChatCodeBean wxQr) {
        this.wxQr = wxQr;
    }
}
