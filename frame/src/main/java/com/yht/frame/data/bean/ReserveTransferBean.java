package com.yht.frame.data.bean;

import java.io.Serializable;

/**
 * @author 顿顿
 * @date 19/7/9 13:55
 * @des 预约转诊
 */
public class ReserveTransferBean implements Serializable {
    private static final long serialVersionUID = -4694142274920855865L;
    private int sex;
    private int patientAge;
    private int payType;
    private int transferType;
    private String isBind;
    private String allergyHistory;
    private String confirmPhoto;
    private String confirmType;
    private String familyHistory;
    private String initResult;
    private String pastHistory;
    private String patientCode;
    private String patientIdCardNo;
    private String patientMobile;
    private String patientName;
    private String transferTarget;
    /**
     * 接诊医生信息
     */
    private String receiveDoctorName;
    private String receiveDoctorCode;
    private String receiveDoctorDepart;
    private String receiveDoctorHospital;
    private String receiveDoctorPhoto;
    private String receiveDoctorJobTitle;

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

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public String getIsBind() {
        return isBind;
    }

    public void setIsBind(String isBind) {
        this.isBind = isBind;
    }

    public int getTransferType() {
        return transferType;
    }

    public void setTransferType(int transferType) {
        this.transferType = transferType;
    }

    public String getAllergyHistory() {
        return allergyHistory;
    }

    public void setAllergyHistory(String allergyHistory) {
        this.allergyHistory = allergyHistory;
    }

    public String getConfirmPhoto() {
        return confirmPhoto;
    }

    public void setConfirmPhoto(String confirmPhoto) {
        this.confirmPhoto = confirmPhoto;
    }

    public String getConfirmType() {
        return confirmType;
    }

    public void setConfirmType(String confirmType) {
        this.confirmType = confirmType;
    }

    public String getFamilyHistory() {
        return familyHistory;
    }

    public void setFamilyHistory(String familyHistory) {
        this.familyHistory = familyHistory;
    }

    public String getInitResult() {
        return initResult;
    }

    public void setInitResult(String initResult) {
        this.initResult = initResult;
    }

    public String getPastHistory() {
        return pastHistory;
    }

    public void setPastHistory(String pastHistory) {
        this.pastHistory = pastHistory;
    }

    public String getPatientCode() {
        return patientCode;
    }

    public void setPatientCode(String patientCode) {
        this.patientCode = patientCode;
    }

    public String getPatientIdCardNo() {
        return patientIdCardNo;
    }

    public void setPatientIdCardNo(String patientIdCardNo) {
        this.patientIdCardNo = patientIdCardNo;
    }

    public String getPatientMobile() {
        return patientMobile;
    }

    public void setPatientMobile(String patientMobile) {
        this.patientMobile = patientMobile;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getReceiveDoctorCode() {
        return receiveDoctorCode;
    }

    public void setReceiveDoctorCode(String receiveDoctorCode) {
        this.receiveDoctorCode = receiveDoctorCode;
    }

    public String getReceiveDoctorName() {
        return receiveDoctorName;
    }

    public void setReceiveDoctorName(String receiveDoctorName) {
        this.receiveDoctorName = receiveDoctorName;
    }

    public String getReceiveDoctorDepart() {
        return receiveDoctorDepart;
    }

    public void setReceiveDoctorDepart(String receiveDoctorDepart) {
        this.receiveDoctorDepart = receiveDoctorDepart;
    }

    public String getReceiveDoctorHospital() {
        return receiveDoctorHospital;
    }

    public void setReceiveDoctorHospital(String receiveDoctorHospital) {
        this.receiveDoctorHospital = receiveDoctorHospital;
    }

    public String getReceiveDoctorPhoto() {
        return receiveDoctorPhoto;
    }

    public void setReceiveDoctorPhoto(String receiveDoctorPhoto) {
        this.receiveDoctorPhoto = receiveDoctorPhoto;
    }

    public String getTransferTarget() {
        return transferTarget;
    }

    public void setTransferTarget(String transferTarget) {
        this.transferTarget = transferTarget;
    }

    public String getReceiveDoctorJobTitle() {
        return receiveDoctorJobTitle;
    }

    public void setReceiveDoctorJobTitle(String receiveDoctorJobTitle) {
        this.receiveDoctorJobTitle = receiveDoctorJobTitle;
    }
}
