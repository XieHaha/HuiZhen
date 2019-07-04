package com.yht.frame.data.base;

import java.io.Serializable;

/**
 * 登录成功
 *
 * @author DUNDUN
 */
public class LoginSuccessBean implements Serializable {
    private static final long serialVersionUID = 1574016439324324068L;
    private String token;
    /**
     * 医生认证状态
     */
    private int approvalStatus;
    private int sex;
    private String doctorCode;
    private String mobile;
    private String doctorName;
    private String jobTitle;
    private String photo;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(int approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
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

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Override
    public String toString() {
        return "[LoginSuccessBean]->doctorCode:" + doctorCode + ",token:" + token + ",mobile:" + mobile +
               ",doctorName:" + doctorName + ",jobTitle:" + jobTitle + ",photo:" + photo
                //                + ",hospitalId:" + hospitalId
                //                + ",communityRequired:" + communityRequired
                //                + ",communityId:" + communityId
                //                + ",visited:" + visited
                //                + ",weight:" + weight
                //                + ",departmentId:" + departmentId
                //                + ",title:" + title
                //                + ",portraitUrl:" + portraitUrl
                //                + ",hosptialRequired:" + hosptialRequired
                //                + ",hospital:" + hospital
                //                + ",department:" + department
                //                + ",status:" + status
                //                + ",checked:" + checked
                ;
    }
}
