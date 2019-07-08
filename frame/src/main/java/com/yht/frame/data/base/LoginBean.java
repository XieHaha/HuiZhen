package com.yht.frame.data.base;

import java.io.Serializable;

/**
 * 登录成功
 *
 * @author DUNDUN
 */
public class LoginBean implements Serializable {
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
    private String hospitalCode;
    private String hospitalName;
    private String departmentName;
    /**
     * 认证失败原因
     */
    private String rejectReason;
    /**
     * 微信登录
     */
    private String openid;
    private String unionid;

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

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    @Override
    public String toString() {
        return "[LoginBean]->doctorCode:" + doctorCode + ",token:" + token + ",mobile:" + mobile + ",doctorName:" +
               doctorName + ",jobTitle:" + jobTitle + ",photo:" + photo
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
