package com.yht.frame.data.bean;

import java.io.Serializable;

/**
 * @author 顿顿
 * @date 19/7/8 18:22
 * @des 医生资金
 */
public class DoctorCurrencyBean implements Serializable {
    private static final long serialVersionUID = 3378919882897560232L;
    private String doctorCode;
    private String doctorName;
    private String mobile;
    private String photo;
    private String sex;
    private String jobTitle;
    private String hospitalName;
    private String departmentName;
    private String total;
    private String arrived;
    private String curMonthTotal;
    private String curMonthArrived;
    private String balance;

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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
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

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getArrived() {
        return arrived;
    }

    public void setArrived(String arrived) {
        this.arrived = arrived;
    }

    public String getCurMonthTotal() {
        return curMonthTotal;
    }

    public void setCurMonthTotal(String curMonthTotal) {
        this.curMonthTotal = curMonthTotal;
    }

    public String getCurMonthArrived() {
        return curMonthArrived;
    }

    public void setCurMonthArrived(String curMonthArrived) {
        this.curMonthArrived = curMonthArrived;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }
}
