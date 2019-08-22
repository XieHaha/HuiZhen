package com.yht.frame.data.bean;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * @author 顿顿
 * @date 19/8/15 18:49
 * @des 医生扫码后信息
 */
public class DoctorQrCodeBean extends DataSupport implements Serializable {
    private static final long serialVersionUID = -3592347450050434576L;
    private String code;
    private String photo;
    private String jobTitle;
    private String departmentName;
    private String hospitalName;
    private String introduce;
    private String doctorName;
    private boolean isFriend;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
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

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public boolean isFriend() {
        return isFriend;
    }

    public void setFriend(boolean friend) {
        isFriend = friend;
    }
}
