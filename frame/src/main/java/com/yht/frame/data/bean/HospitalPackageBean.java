package com.yht.frame.data.bean;

import java.io.Serializable;

/**
 * @author 顿顿
 * @date 19/10/29 14:46
 * @description 服务包
 */
public class HospitalPackageBean implements Serializable {
    private static final long serialVersionUID = 8238857906628418524L;
    private String code;
    private String packageName;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
