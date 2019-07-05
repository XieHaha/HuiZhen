package com.yht.frame.data.base;

import java.io.Serializable;

/**
 * @author 顿顿
 * @date 19/7/4 21:11
 * @des
 */
public class DoctorAuthBean implements Serializable {
    private static final long serialVersionUID = 5575806448053952083L;
    private String certBack;
    private String certFront;
    private String departmentId;
    private String doctorName;
    private String doctorPhone;
    private String doctorPhoto;
    private String doctorSex;
    private String hospitalName;
    private String jobTitle;

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

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
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

    public String getDoctorSex() {
        return doctorSex;
    }

    public void setDoctorSex(String doctorSex) {
        this.doctorSex = doctorSex;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }
}
