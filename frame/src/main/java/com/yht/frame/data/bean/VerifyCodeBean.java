package com.yht.frame.data.bean;

import java.io.Serializable;

/**
 * @author 顿顿
 * @date 19/7/3 21:14
 * @description
 */
public class VerifyCodeBean implements Serializable {
    private static final long serialVersionUID = -6673728701675365482L;
    private String prepare_id;

    public String getPrepare_id() {
        return prepare_id;
    }

    public void setPrepare_id(String prepare_id) {
        this.prepare_id = prepare_id;
    }
}
