package com.yht.frame.data.bean;

import java.io.Serializable;

/**
 * @author 顿顿
 * @date 19/10/24 13:48
 * @description 创建远程会诊时上传的科室信息
 */
public class DepartInfoBean implements Serializable {
    private static final long serialVersionUID = 5392860940936671708L;
    private int hospitalDepartmentId;
    private String hospitalDepartmentName;
    private String hospitalCode;
    private String hospitalName;

    public int getHospitalDepartmentId() {
        return hospitalDepartmentId;
    }

    public void setHospitalDepartmentId(int hospitalDepartmentId) {
        this.hospitalDepartmentId = hospitalDepartmentId;
    }

    public String getHospitalDepartmentName() {
        return hospitalDepartmentName;
    }

    public void setHospitalDepartmentName(String hospitalDepartmentName) {
        this.hospitalDepartmentName = hospitalDepartmentName;
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
}
