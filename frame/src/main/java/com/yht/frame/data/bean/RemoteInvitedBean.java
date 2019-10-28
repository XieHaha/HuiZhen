package com.yht.frame.data.bean;

import java.io.Serializable;

/**
 * @author 顿顿
 * @date 19/8/26 14:18
 * @des 远程会诊受邀
 */
public class RemoteInvitedBean implements Serializable {
    private static final long serialVersionUID = 5775065159299464466L;
    private int hospitalDepartmentId;
    private int status;
    private boolean resultStatus;
    private String hospitalName;
    private String hospitalCode;
    private String hospitalDepartmentName;
    private String doctorCode;
    private String doctorName;
    private String doctorPhoto;
    private String doctorJobTitle;
    private String result;
    private String rejectReason;
    private String updateAt;

    public int getHospitalDepartmentId() {
        return hospitalDepartmentId;
    }

    public void setHospitalDepartmentId(int hospitalDepartmentId) {
        this.hospitalDepartmentId = hospitalDepartmentId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(boolean resultStatus) {
        this.resultStatus = resultStatus;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getHospitalCode() {
        return hospitalCode;
    }

    public void setHospitalCode(String hospitalCode) {
        this.hospitalCode = hospitalCode;
    }

    public String getHospitalDepartmentName() {
        return hospitalDepartmentName;
    }

    public void setHospitalDepartmentName(String hospitalDepartmentName) {
        this.hospitalDepartmentName = hospitalDepartmentName;
    }

    public String getDoctorCode() {
        return doctorCode;
    }

    public void setDoctorCode(String doctorCode) {
        this.doctorCode = doctorCode;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDoctorPhoto() {
        return doctorPhoto;
    }

    public void setDoctorPhoto(String doctorPhoto) {
        this.doctorPhoto = doctorPhoto;
    }

    public String getDoctorJobTitle() {
        return doctorJobTitle;
    }

    public void setDoctorJobTitle(String doctorJobTitle) {
        this.doctorJobTitle = doctorJobTitle;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
    }
}
