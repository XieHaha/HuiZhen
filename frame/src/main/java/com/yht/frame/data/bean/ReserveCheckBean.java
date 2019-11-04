package com.yht.frame.data.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author 顿顿
 * @date 19/7/9 17:27
 * @des 预约检查提交数据
 */
public class ReserveCheckBean implements Serializable {
    private static final long serialVersionUID = -7797688942553183369L;
    private boolean notUpdate;
    private boolean createIgnorePriceChange;
    private int age;
    private int isPregnancy;
    private int sex;
    private String isBind;
    private String allergyHistory;
    private String confirmPhoto;
    private String familyHistory;
    private String idCardNo;
    private String initResult;
    private String pastHistory;
    private String patientCode;
    private String patientName;
    private String payType;
    private String phone;
    private String confirmType;
    private ArrayList<ReserveCheckTypeBean> checkTrans;

    public boolean isNotUpdate() {
        return notUpdate;
    }

    public void setNotUpdate(boolean notUpdate) {
        this.notUpdate = notUpdate;
    }

    public boolean isCreateIgnorePriceChange() {
        return createIgnorePriceChange;
    }

    public void setCreateIgnorePriceChange(boolean createIgnorePriceChange) {
        this.createIgnorePriceChange = createIgnorePriceChange;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getIsPregnancy() {
        return isPregnancy;
    }

    public void setIsPregnancy(int isPregnancy) {
        this.isPregnancy = isPregnancy;
    }

    public String getIsBind() {
        return isBind;
    }

    public void setIsBind(String isBind) {
        this.isBind = isBind;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
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

    public String getFamilyHistory() {
        return familyHistory;
    }

    public void setFamilyHistory(String familyHistory) {
        this.familyHistory = familyHistory;
    }

    public String getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
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

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getConfirmType() {
        return confirmType;
    }

    public void setConfirmType(String confirmType) {
        this.confirmType = confirmType;
    }

    public ArrayList<ReserveCheckTypeBean> getCheckTrans() {
        return checkTrans;
    }

    public void setCheckTrans(ArrayList<ReserveCheckTypeBean> checkTrans) {
        this.checkTrans = checkTrans;
    }
}
