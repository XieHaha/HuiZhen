package com.yht.frame.data.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author 顿顿
 * @date 19/7/11 15:26
 * @des
 */
public class CheckDetailBean implements Serializable {
    private static final long serialVersionUID = 3945624210973553821L;
    private int sex;
    private int patientAge;
    private int isPregnancy;
    private int payType;
    private int status;
    private int actualPay;
    private String patientCode;
    private String patientPhoto;
    private String patientWxPhoto;
    private String patientName;
    private String createAt;
    private String sourceDoctorMobile;
    private String initResult;
    private String targetHospitalName;
    private String sourceDoctorJobTitle;
    private String sourceHospitalName;
    private String sourceHospitalDepartmentName;
    private String patientMobile;
    private String patientIdCardNo;
    private String finishAt;
    private String notes;
    private String doctorName;
    private ArrayList<CheckTypeByDetailBean> tranList;

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getPatientAge() {
        return patientAge;
    }

    public void setPatientAge(int patientAge) {
        this.patientAge = patientAge;
    }

    public int getIsPregnancy() {
        return isPregnancy;
    }

    public void setIsPregnancy(int isPregnancy) {
        this.isPregnancy = isPregnancy;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getActualPay() {
        return actualPay;
    }

    public void setActualPay(int actualPay) {
        this.actualPay = actualPay;
    }

    public String getPatientCode() {
        return patientCode;
    }

    public void setPatientCode(String patientCode) {
        this.patientCode = patientCode;
    }

    public String getPatientPhoto() {
        return patientPhoto;
    }

    public String getPatientWxPhoto() {
        return patientWxPhoto;
    }

    public void setPatientWxPhoto(String patientWxPhoto) {
        this.patientWxPhoto = patientWxPhoto;
    }

    public void setPatientPhoto(String patientPhoto) {
        this.patientPhoto = patientPhoto;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getSourceDoctorMobile() {
        return sourceDoctorMobile;
    }

    public void setSourceDoctorMobile(String sourceDoctorMobile) {
        this.sourceDoctorMobile = sourceDoctorMobile;
    }

    public String getInitResult() {
        return initResult;
    }

    public void setInitResult(String initResult) {
        this.initResult = initResult;
    }

    public String getTargetHospitalName() {
        return targetHospitalName;
    }

    public void setTargetHospitalName(String targetHospitalName) {
        this.targetHospitalName = targetHospitalName;
    }

    public String getSourceDoctorJobTitle() {
        return sourceDoctorJobTitle;
    }

    public void setSourceDoctorJobTitle(String sourceDoctorJobTitle) {
        this.sourceDoctorJobTitle = sourceDoctorJobTitle;
    }

    public String getSourceHospitalName() {
        return sourceHospitalName;
    }

    public void setSourceHospitalName(String sourceHospitalName) {
        this.sourceHospitalName = sourceHospitalName;
    }

    public String getSourceHospitalDepartmentName() {
        return sourceHospitalDepartmentName;
    }

    public void setSourceHospitalDepartmentName(String sourceHospitalDepartmentName) {
        this.sourceHospitalDepartmentName = sourceHospitalDepartmentName;
    }

    public String getPatientMobile() {
        return patientMobile;
    }

    public void setPatientMobile(String patientMobile) {
        this.patientMobile = patientMobile;
    }

    public String getPatientIdCardNo() {
        return patientIdCardNo;
    }

    public void setPatientIdCardNo(String patientIdCardNo) {
        this.patientIdCardNo = patientIdCardNo;
    }

    public String getFinishAt() {
        return finishAt;
    }

    public void setFinishAt(String finishAt) {
        this.finishAt = finishAt;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public ArrayList<CheckTypeByDetailBean> getTranList() {
        return tranList;
    }

    public void setTranList(ArrayList<CheckTypeByDetailBean> tranList) {
        this.tranList = tranList;
    }
}
