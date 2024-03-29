package com.yht.frame.data.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author 顿顿
 * @date 19/7/11 15:26
 * @des 预约服务详情
 */
public class CheckDetailBean implements Serializable {
    private static final long serialVersionUID = 3945624210973553821L;
    private int sex;
    private int patientAge;
    private int isPregnancy;
    private int payType;
    private int status;
    private int actualPay;
    private int shouldPay;
    private String patientCode;
    private String patientPhoto;
    private String patientWxPhoto;
    private String patientName;
    private String createAt;
    private String sourceDoctorMobile;
    private String initResult;
    private String targetHospitalName;
    private String targetHospitalAddress;
    private String sourceDoctorJobTitle;
    private String sourceHospitalName;
    private String sourceHospitalDepartmentName;
    private String patientMobile;
    private String patientIdCardNo;
    private String finishAt;
    private String notes;
    private String doctorName;
    private String doctorCode;
    private String doctorAvatar;
    private String pastHistory;
    private String familyHistory;
    private String allergyHistory;
    private ArrayList<CheckTypeByDetailBean> trans;
    private ArrayList<CheckTypeByDetailBean> noticeList;


    public String getPastHistory() {
        return pastHistory;
    }

    public void setPastHistory(String pastHistory) {
        this.pastHistory = pastHistory;
    }

    public String getFamilyHistory() {
        return familyHistory;
    }

    public void setFamilyHistory(String familyHistory) {
        this.familyHistory = familyHistory;
    }

    public String getAllergyHistory() {
        return allergyHistory;
    }

    public void setAllergyHistory(String allergyHistory) {
        this.allergyHistory = allergyHistory;
    }

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

    public int getShouldPay() {
        return shouldPay;
    }

    public void setShouldPay(int shouldPay) {
        this.shouldPay = shouldPay;
    }

    public String getTargetHospitalAddress() {
        return targetHospitalAddress;
    }

    public void setTargetHospitalAddress(String targetHospitalAddress) {
        this.targetHospitalAddress = targetHospitalAddress;
    }

    public String getDoctorCode() {
        return doctorCode;
    }

    public void setDoctorCode(String doctorCode) {
        this.doctorCode = doctorCode;
    }

    public String getDoctorAvatar() {
        return doctorAvatar;
    }

    public void setDoctorAvatar(String doctorAvatar) {
        this.doctorAvatar = doctorAvatar;
    }

    public ArrayList<CheckTypeByDetailBean> getTrans() {
        return trans;
    }

    public void setTrans(ArrayList<CheckTypeByDetailBean> trans) {
        this.trans = trans;
    }

    public ArrayList<CheckTypeByDetailBean> getNoticeList() {
        return noticeList;
    }

    public void setNoticeList(ArrayList<CheckTypeByDetailBean> noticeList) {
        this.noticeList = noticeList;
    }
}
