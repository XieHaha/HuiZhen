package com.yht.frame.data.base;

import java.io.Serializable;

/**
 * @author 顿顿
 * @date 19/7/4 21:11
 * @des 医生认证信息
 */
public class DoctorAuthBean implements Serializable {
    private static final long serialVersionUID = 5575806448053952083L;
    /**
     * 1为男性 2为女性
     */
    private int doctorSex;
    private int lastApplyDepartmentId;
    private int approvalStatus;
    private int enable;
    private String certBack;
    private String certFront;
    private String doctorName;
    private String doctorCode;
    private String doctorPhone;
    private String doctorPhoto;
    private String jobTitle;
    private String approvalUser;
    private String approvalRejectReason;
    private String passAt;
    private String createAt;
    private String lastApplyHospitalName;
    private String lastApplyHospitalCode;
    private String lastApplyDepartmentName;

    public String getCertBack() {
        return certBack;
    }

    public void setCertBack(String certBack) {
        this.certBack = certBack;
    }

    public String getCertFront() {
        return certFront;
    }

    public void setCertFront(String certFront) {
        this.certFront = certFront;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDoctorPhone() {
        return doctorPhone;
    }

    public void setDoctorPhone(String doctorPhone) {
        this.doctorPhone = doctorPhone;
    }

    public String getDoctorPhoto() {
        return doctorPhoto;
    }

    public void setDoctorPhoto(String doctorPhoto) {
        this.doctorPhoto = doctorPhoto;
    }

    public int getDoctorSex() {
        return doctorSex;
    }

    public void setDoctorSex(int doctorSex) {
        this.doctorSex = doctorSex;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getDoctorCode() {
        return doctorCode;
    }

    public void setDoctorCode(String doctorCode) {
        this.doctorCode = doctorCode;
    }

    public String getApprovalUser() {
        return approvalUser;
    }

    public void setApprovalUser(String approvalUser) {
        this.approvalUser = approvalUser;
    }

    public String getPassAt() {
        return passAt;
    }

    public void setPassAt(String passAt) {
        this.passAt = passAt;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public int getLastApplyDepartmentId() {
        return lastApplyDepartmentId;
    }

    public void setLastApplyDepartmentId(int lastApplyDepartmentId) {
        this.lastApplyDepartmentId = lastApplyDepartmentId;
    }

    public int getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(int approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public int getEnable() {
        return enable;
    }

    public void setEnable(int enable) {
        this.enable = enable;
    }

    public String getApprovalRejectReason() {
        return approvalRejectReason;
    }

    public void setApprovalRejectReason(String approvalRejectReason) {
        this.approvalRejectReason = approvalRejectReason;
    }

    public String getLastApplyHospitalName() {
        return lastApplyHospitalName;
    }

    public void setLastApplyHospitalName(String lastApplyHospitalName) {
        this.lastApplyHospitalName = lastApplyHospitalName;
    }

    public String getLastApplyHospitalCode() {
        return lastApplyHospitalCode;
    }

    public void setLastApplyHospitalCode(String lastApplyHospitalCode) {
        this.lastApplyHospitalCode = lastApplyHospitalCode;
    }

    public String getLastApplyDepartmentName() {
        return lastApplyDepartmentName;
    }

    public void setLastApplyDepartmentName(String lastApplyDepartmentName) {
        this.lastApplyDepartmentName = lastApplyDepartmentName;
    }
}
