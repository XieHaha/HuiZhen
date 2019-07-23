package com.yht.frame.data.bean;

import java.io.Serializable;

/**
 * 医生绑定的合作医院
 *
 * @author dundun
 */
public class CooperateHospitalBean implements Serializable {
    private static final long serialVersionUID = -5825956356939935298L;
    private String hospitalCode;
    private String hospitalName;
    private String departmentName;
    private int departmentId;
    private int pid;
    private int departmentOrder;

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

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getDepartmentOrder() {
        return departmentOrder;
    }

    public void setDepartmentOrder(int departmentOrder) {
        this.departmentOrder = departmentOrder;
    }
}
