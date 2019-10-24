package com.yht.frame.data.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;

/**
 * @author 顿顿
 * @date 19/10/24 13:48
 * @description
 */
public class RemoteDepartBean implements Serializable, MultiItemEntity {
    private static final long serialVersionUID = 588112764995473044L;
    private int departmentId;
    private boolean free;
    private String departmentName;
    private String hospitalCode;
    private String hospitalName;

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public boolean isFree() {
        return free;
    }

    public void setFree(boolean free) {
        this.free = free;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getHospitalCode() {
        return hospitalCode;
    }

    public void setHospitalCode(String hospitalCode) {
        this.hospitalCode = hospitalCode;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    @Override
    public int getItemType() {
        return 1;
    }
}
