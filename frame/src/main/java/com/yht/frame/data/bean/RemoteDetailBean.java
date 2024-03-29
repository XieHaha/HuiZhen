package com.yht.frame.data.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author 顿顿
 * @date 19/8/26 14:28
 * @des 远程会诊详情
 */
public class RemoteDetailBean implements Serializable {
    private static final long serialVersionUID = 8366103546982326133L;
    private int status;
    private int sex;
    private int patientAge;
    private int approveStatus;
    private long beginAt;
    private long startAt;
    private long endAt;
    private String patientCode;
    private String patientName;
    private String patientPhoto;
    private String patientIdCardNo;
    private String patientMobile;
    private String sourceDoctorName;
    private String sourceDoctorCode;
    private String sourceDoctorPhoto;
    private String sourceHospitalName;
    private String sourceHospitalDepartmentName;
    private String descIll;
    private String initResult;
    private String destination;
    private String timeLength;
    private String rejectReason;
    private String pastHistory;
    private String familyHistory;
    private String allergyHistory;
    private ArrayList<RemoteInvitedBean> invitationList;
    private ArrayList<FileBean> patientResourceList;

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getBeginAt() {
        return beginAt;
    }

    public void setBeginAt(long beginAt) {
        this.beginAt = beginAt;
    }

    public long getStartAt() {
        return startAt;
    }

    public void setStartAt(long startAt) {
        this.startAt = startAt;
    }

    public long getEndAt() {
        return endAt;
    }

    public void setEndAt(long endAt) {
        this.endAt = endAt;
    }

    public String getPatientCode() {
        return patientCode;
    }

    public void setPatientCode(String patientCode) {
        this.patientCode = patientCode;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientPhoto() {
        return patientPhoto;
    }

    public void setPatientPhoto(String patientPhoto) {
        this.patientPhoto = patientPhoto;
    }

    public String getSourceDoctorName() {
        return sourceDoctorName;
    }

    public void setSourceDoctorName(String sourceDoctorName) {
        this.sourceDoctorName = sourceDoctorName;
    }

    public int getPatientAge() {
        return patientAge;
    }

    public void setPatientAge(int patientAge) {
        this.patientAge = patientAge;
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

    public String getDescIll() {
        return descIll;
    }

    public void setDescIll(String descIll) {
        this.descIll = descIll;
    }

    public String getInitResult() {
        return initResult;
    }

    public void setInitResult(String initResult) {
        this.initResult = initResult;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getTimeLength() {
        return timeLength;
    }

    public void setTimeLength(String timeLength) {
        this.timeLength = timeLength;
    }

    public ArrayList<RemoteInvitedBean> getInvitationList() {
        return invitationList;
    }

    public void setInvitationList(ArrayList<RemoteInvitedBean> invitationList) {
        this.invitationList = invitationList;
    }

    public int getApproveStatus() {
        return approveStatus;
    }

    public void setApproveStatus(int approveStatus) {
        this.approveStatus = approveStatus;
    }

    public String getSourceDoctorPhoto() {
        return sourceDoctorPhoto;
    }

    public void setSourceDoctorPhoto(String sourceDoctorPhoto) {
        this.sourceDoctorPhoto = sourceDoctorPhoto;
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

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }

    public ArrayList<FileBean> getPatientResourceList() {
        return patientResourceList;
    }

    public void setPatientResourceList(ArrayList<FileBean> patientResourceList) {
        this.patientResourceList = patientResourceList;
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

    public String getSourceDoctorCode() {
        return sourceDoctorCode;
    }

    public void setSourceDoctorCode(String sourceDoctorCode) {
        this.sourceDoctorCode = sourceDoctorCode;
    }
}
