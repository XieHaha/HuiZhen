package com.yht.frame.data.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author 顿顿
 * @date 19/7/4 21:11
 * @des 接诊医生详细信息
 */
public class ReceiverDoctorBean implements Serializable {
    private static final long serialVersionUID = 5575806448053952083L;
    /**
     * 1为男性 2为女性
     */
    private int doctorSex;
    private int directDepartmentId;
    private int approvalStatus;
    private int enable;
    private String certBack;
    private String certFront;
    private String doctorName;
    private String doctorPhone;
    private String doctorPhoto;
    private String jobTitle;
    private String doctorCode;
    private String approvalUser;
    private String passAt;
    private String createAt;
    private String directHospitalName;
    private String directHospitalCode;
    private String directDepartmentName;
    private List<ReceiverDoctorHospitalBean> cooperateDepartmentList;
    //2019年7月9日20:12:00
    private String departmentName;
    private String hospitalName;
    private String hospitalCode;
    private String photo;

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

    public String getDirectHospitalName() {
        return directHospitalName;
    }

    public void setDirectHospitalName(String directHospitalName) {
        this.directHospitalName = directHospitalName;
    }

    public String getDirectHospitalCode() {
        return directHospitalCode;
    }

    public void setDirectHospitalCode(String directHospitalCode) {
        this.directHospitalCode = directHospitalCode;
    }

    public String getDirectDepartmentName() {
        return directDepartmentName;
    }

    public void setDirectDepartmentName(String directDepartmentName) {
        this.directDepartmentName = directDepartmentName;
    }

    public int getDirectDepartmentId() {
        return directDepartmentId;
    }

    public void setDirectDepartmentId(int directDepartmentId) {
        this.directDepartmentId = directDepartmentId;
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

    public List<ReceiverDoctorHospitalBean> getCooperateDepartmentList() {
        return cooperateDepartmentList;
    }

    public void setCooperateDepartmentList(List<ReceiverDoctorHospitalBean> cooperateDepartmentList) {
        this.cooperateDepartmentList = cooperateDepartmentList;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
