package com.yht.frame.data.bean;

import java.io.Serializable;

/**
 * @author 顿顿
 * @date 19/10/29 14:18
 * @description 合作医院详情下 服务
 */
public class HospitalProjectBean implements Serializable {
    private static final long serialVersionUID = 1818919657536048057L;
    private String projectCode;
    private String projectName;
    private int type;

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
