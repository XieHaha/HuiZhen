package com.yht.frame.data.bean;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

/**
 * @author dundun
 * @date 18/8/15 (医生认证选择医院)
 */
public class HospitalBean extends LitePalSupport implements Serializable {
    private static final long serialVersionUID = 6102212747958144450L;
    private int settleStatus;
    private String hospitalName;
    private String hospitalAddress;
    private String hospitalCode;
    private String hospitalProvince;
    private String hospitalCity;
    private String hospitalArea;
    private String hospitalLevel;
    private String createAt;
    private String hospitalIntroduce;
    private String hospitalDepartmentList;
    private String serviceArray;
    private String menuList;
    private String accountName;
    private String departmentTotal;
    private String directDoctorTotal;
    private String cooperateDoctorTotal;

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getHospitalAddress() {
        return hospitalAddress;
    }

    public void setHospitalAddress(String hospitalAddress) {
        this.hospitalAddress = hospitalAddress;
    }

    public String getHospitalCode() {
        return hospitalCode;
    }

    public void setHospitalCode(String hospitalCode) {
        this.hospitalCode = hospitalCode;
    }

    public int getSettleStatus() {
        return settleStatus;
    }

    public void setSettleStatus(int settleStatus) {
        this.settleStatus = settleStatus;
    }

    public String getHospitalProvince() {
        return hospitalProvince;
    }

    public void setHospitalProvince(String hospitalProvince) {
        this.hospitalProvince = hospitalProvince;
    }

    public String getHospitalCity() {
        return hospitalCity;
    }

    public void setHospitalCity(String hospitalCity) {
        this.hospitalCity = hospitalCity;
    }

    public String getHospitalArea() {
        return hospitalArea;
    }

    public void setHospitalArea(String hospitalArea) {
        this.hospitalArea = hospitalArea;
    }

    public String getHospitalLevel() {
        return hospitalLevel;
    }

    public void setHospitalLevel(String hospitalLevel) {
        this.hospitalLevel = hospitalLevel;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getHospitalIntroduce() {
        return hospitalIntroduce;
    }

    public void setHospitalIntroduce(String hospitalIntroduce) {
        this.hospitalIntroduce = hospitalIntroduce;
    }

    public String getHospitalDepartmentList() {
        return hospitalDepartmentList;
    }

    public void setHospitalDepartmentList(String hospitalDepartmentList) {
        this.hospitalDepartmentList = hospitalDepartmentList;
    }

    public String getServiceArray() {
        return serviceArray;
    }

    public void setServiceArray(String serviceArray) {
        this.serviceArray = serviceArray;
    }

    public String getMenuList() {
        return menuList;
    }

    public void setMenuList(String menuList) {
        this.menuList = menuList;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getDepartmentTotal() {
        return departmentTotal;
    }

    public void setDepartmentTotal(String departmentTotal) {
        this.departmentTotal = departmentTotal;
    }

    public String getDirectDoctorTotal() {
        return directDoctorTotal;
    }

    public void setDirectDoctorTotal(String directDoctorTotal) {
        this.directDoctorTotal = directDoctorTotal;
    }

    public String getCooperateDoctorTotal() {
        return cooperateDoctorTotal;
    }

    public void setCooperateDoctorTotal(String cooperateDoctorTotal) {
        this.cooperateDoctorTotal = cooperateDoctorTotal;
    }
}

