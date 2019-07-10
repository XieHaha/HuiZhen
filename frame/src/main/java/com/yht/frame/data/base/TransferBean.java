package com.yht.frame.data.base;

import java.io.Serializable;

/**
 * @author 顿顿
 * @date 19/7/10 13:44
 * @des 转诊数据
 */
public class TransferBean implements Serializable {
    private static final long serialVersionUID = -4842399083649318736L;
    private int receiveStatus;
    private int patientAge;
    private int patientSex;
    private String orderNo;
    private String wxPhoto;
    /**
     * 转诊时间
     */
    private String transferDate;
    private String confirmPhoto;
    private String patientName;
    private String transferTarget;
    /**
     * 接诊医生
     */
    private String targetDoctorName;
    private String targetHospitalDepartmentName;
    private String targetHospitalName;
    /**
     * 预约就诊时间
     */
    private String appointAt;
    private String patientMobile;
    /**
     * 转诊医生
     */
    private String sourceDoctorName;
    private String sourceHospitalDepartmentName;
    private String sourceHospitalName;
    /**
     * 详情数据
     */
    private int sex;
    private int transferType;
    private int payType;
    private String patientIdCardNo;
    private String pastHistory;
    private String familyHistory;
    private String allergyHistory;
    private String sourceDoctorMobile;
    private String initResult;
    private String targetDoctorMobile;
    private String note;
    private String rejectReason;
    private String cancelReason;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getWxPhoto() {
        return wxPhoto;
    }

    public void setWxPhoto(String wxPhoto) {
        this.wxPhoto = wxPhoto;
    }

    public int getReceiveStatus() {
        return receiveStatus;
    }

    public void setReceiveStatus(int receiveStatus) {
        this.receiveStatus = receiveStatus;
    }

    public int getPatientAge() {
        return patientAge;
    }

    public void setPatientAge(int patientAge) {
        this.patientAge = patientAge;
    }

    public int getPatientSex() {
        return patientSex;
    }

    public void setPatientSex(int patientSex) {
        this.patientSex = patientSex;
    }

    public String getTransferDate() {
        return transferDate;
    }

    public void setTransferDate(String transferDate) {
        this.transferDate = transferDate;
    }

    public String getConfirmPhoto() {
        return confirmPhoto;
    }

    public void setConfirmPhoto(String confirmPhoto) {
        this.confirmPhoto = confirmPhoto;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getTransferTarget() {
        return transferTarget;
    }

    public void setTransferTarget(String transferTarget) {
        this.transferTarget = transferTarget;
    }

    public String getTargetDoctorName() {
        return targetDoctorName;
    }

    public void setTargetDoctorName(String targetDoctorName) {
        this.targetDoctorName = targetDoctorName;
    }

    public String getTargetHospitalDepartmentName() {
        return targetHospitalDepartmentName;
    }

    public void setTargetHospitalDepartmentName(String targetHospitalDepartmentName) {
        this.targetHospitalDepartmentName = targetHospitalDepartmentName;
    }

    public String getTargetHospitalName() {
        return targetHospitalName;
    }

    public void setTargetHospitalName(String targetHospitalName) {
        this.targetHospitalName = targetHospitalName;
    }

    public String getAppointAt() {
        return appointAt;
    }

    public void setAppointAt(String appointAt) {
        this.appointAt = appointAt;
    }

    public String getPatientMobile() {
        return patientMobile;
    }

    public void setPatientMobile(String patientMobile) {
        this.patientMobile = patientMobile;
    }

    public String getSourceDoctorName() {
        return sourceDoctorName;
    }

    public void setSourceDoctorName(String sourceDoctorName) {
        this.sourceDoctorName = sourceDoctorName;
    }

    public String getSourceHospitalDepartmentName() {
        return sourceHospitalDepartmentName;
    }

    public void setSourceHospitalDepartmentName(String sourceHospitalDepartmentName) {
        this.sourceHospitalDepartmentName = sourceHospitalDepartmentName;
    }

    public String getSourceHospitalName() {
        return sourceHospitalName;
    }

    public void setSourceHospitalName(String sourceHospitalName) {
        this.sourceHospitalName = sourceHospitalName;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getTransferType() {
        return transferType;
    }

    public void setTransferType(int transferType) {
        this.transferType = transferType;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public String getPatientIdCardNo() {
        return patientIdCardNo;
    }

    public void setPatientIdCardNo(String patientIdCardNo) {
        this.patientIdCardNo = patientIdCardNo;
    }

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

    public String getTargetDoctorMobile() {
        return targetDoctorMobile;
    }

    public void setTargetDoctorMobile(String targetDoctorMobile) {
        this.targetDoctorMobile = targetDoctorMobile;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }
}
