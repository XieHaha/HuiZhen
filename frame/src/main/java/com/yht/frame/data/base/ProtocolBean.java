package com.yht.frame.data.base;

import java.io.Serializable;

/**
 * @author 顿顿
 * @date 19/7/16 16:49
 * @des
 */
public class ProtocolBean implements Serializable {
    private static final long serialVersionUID = -1519263388218189946L;
    private int appType;
    private String updateAt;

    public int getAppType() {
        return appType;
    }

    public void setAppType(int appType) {
        this.appType = appType;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
    }
}
