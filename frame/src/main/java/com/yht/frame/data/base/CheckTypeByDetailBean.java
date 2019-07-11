package com.yht.frame.data.base;

import java.io.Serializable;

/**
 * @author 顿顿
 * @date 19/7/11 15:30
 * @des 检查详情中的检查项
 */
public class CheckTypeByDetailBean implements Serializable {
    private static final long serialVersionUID = 900363421370287560L;
    private int id;
    private int status;
    private int hospitalDepartmentId;
    private String name;
    private String orderAt;
    private String departmentName;
    private String type;
    private String cancelReason;
    private String report;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getHospitalDepartmentId() {
        return hospitalDepartmentId;
    }

    public void setHospitalDepartmentId(int hospitalDepartmentId) {
        this.hospitalDepartmentId = hospitalDepartmentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOrderAt() {
        return orderAt;
    }

    public void setOrderAt(String orderAt) {
        this.orderAt = orderAt;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }
}
