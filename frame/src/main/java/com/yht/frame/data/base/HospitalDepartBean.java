package com.yht.frame.data.base;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author 顿顿
 * @date 19/7/5 11:02
 * @des
 */
public class HospitalDepartBean implements Serializable {
    private static final long serialVersionUID = -3908180941956371366L;
    private int departmentId;
    private int departmentOrder;
    private String departmentName;
    private String createAt;
    private ArrayList<HospitalDepartChildBean> childList;
    //2019年7月9日19:32:02 提交预约转诊添加字段
    private String hospitalCode;
    private String hospitalName;
    private int pid;

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public int getDepartmentOrder() {
        return departmentOrder;
    }

    public void setDepartmentOrder(int departmentOrder) {
        this.departmentOrder = departmentOrder;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public ArrayList<HospitalDepartChildBean> getChildList() {
        return childList;
    }

    public void setChildList(ArrayList<HospitalDepartChildBean> childList) {
        this.childList = childList;
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

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }
}
