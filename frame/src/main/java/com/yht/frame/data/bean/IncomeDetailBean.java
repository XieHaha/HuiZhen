package com.yht.frame.data.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author 顿顿
 * @date 19/7/8 20:27
 * @description 收入详情
 */
public class IncomeDetailBean implements Serializable {
    private static final long serialVersionUID = -762440704114133758L;
    private String total;
    private String arrived;
    private int serviceFlag;
    private String doctorCode;
    private String serviceFlagName;
    private String createAtPrefix;
    private String createAt;
    private String patientName;
    private String hospitalNamePrefix;
    private String hospitalName;
    private String doctorNamePrefix;
    private String doctorName;
    private String msg;
    private ArrayList<CheckTypeBean> examList;

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

    public int getServiceFlag() {
        return serviceFlag;
    }

    public void setServiceFlag(int serviceFlag) {
        this.serviceFlag = serviceFlag;
    }

    public String getDoctorCode() {
        return doctorCode;
    }

    public void setDoctorCode(String doctorCode) {
        this.doctorCode = doctorCode;
    }

    public String getServiceFlagName() {
        return serviceFlagName;
    }

    public void setServiceFlagName(String serviceFlagName) {
        this.serviceFlagName = serviceFlagName;
    }

    public String getCreateAtPrefix() {
        return createAtPrefix;
    }

    public void setCreateAtPrefix(String createAtPrefix) {
        this.createAtPrefix = createAtPrefix;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getHospitalNamePrefix() {
        return hospitalNamePrefix;
    }

    public void setHospitalNamePrefix(String hospitalNamePrefix) {
        this.hospitalNamePrefix = hospitalNamePrefix;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getDoctorNamePrefix() {
        return doctorNamePrefix;
    }

    public void setDoctorNamePrefix(String doctorNamePrefix) {
        this.doctorNamePrefix = doctorNamePrefix;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ArrayList<CheckTypeBean> getExamList() {
        return examList;
    }

    public void setExamList(ArrayList<CheckTypeBean> examList) {
        this.examList = examList;
    }
}
