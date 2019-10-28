package com.yht.frame.data.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author 顿顿
 * @date 19/7/9 17:27
 * @des 预约远程会诊提交数据
 */
public class ReserveRemoteBean implements Serializable {
    private static final long serialVersionUID = -2120842044244575692L;
    private int patientAge;
    private int patientSex;
    private boolean isBind;
    private String allergy;
    private String confirmFile;
    private String confirmType;
    private String descIll;
    private String destination;
    private String endAt;
    private String startAt;
    private String family;
    private String initResult;
    private String past;
    private String patientCode;
    private String patientIdCard;
    private String patientMobile;
    private String patientName;
    private String patientResource;
    private ArrayList<DepartInfoBean> hosDeptInfo;

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

    public boolean getIsBind() {
        return isBind;
    }

    public void setBind(boolean bind) {
        isBind = bind;
    }

    public String getAllergy() {
        return allergy;
    }

    public void setAllergy(String allergy) {
        this.allergy = allergy;
    }

    public String getConfirmFile() {
        return confirmFile;
    }

    public void setConfirmFile(String confirmFile) {
        this.confirmFile = confirmFile;
    }

    public String getConfirmType() {
        return confirmType;
    }

    public void setConfirmType(String confirmType) {
        this.confirmType = confirmType;
    }

    public String getDescIll() {
        return descIll;
    }

    public void setDescIll(String descIll) {
        this.descIll = descIll;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getEndAt() {
        return endAt;
    }

    public void setEndAt(String endAt) {
        this.endAt = endAt;
    }

    public String getStartAt() {
        return startAt;
    }

    public void setStartAt(String startAt) {
        this.startAt = startAt;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getInitResult() {
        return initResult;
    }

    public void setInitResult(String initResult) {
        this.initResult = initResult;
    }

    public String getPast() {
        return past;
    }

    public void setPast(String past) {
        this.past = past;
    }

    public String getPatientCode() {
        return patientCode;
    }

    public void setPatientCode(String patientCode) {
        this.patientCode = patientCode;
    }

    public String getPatientIdCard() {
        return patientIdCard;
    }

    public void setPatientIdCard(String patientIdCard) {
        this.patientIdCard = patientIdCard;
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

    public String getPatientResource() {
        return patientResource;
    }

    public void setPatientResource(String patientResource) {
        this.patientResource = patientResource;
    }

    public ArrayList<DepartInfoBean> getHosDeptInfo() {
        return hosDeptInfo;
    }

    public void setHosDeptInfo(ArrayList<DepartInfoBean> hosDeptInfo) {
        this.hosDeptInfo = hosDeptInfo;
    }
}
