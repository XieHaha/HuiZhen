package com.yht.frame.data.base;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author 顿顿
 * @date 19/7/11 14:58
 * @des 预约检查
 */
public class CheckBean implements Serializable {
    private static final long serialVersionUID = 6353080025533815362L;
    private int status;
    private long createAt;
    private String orderNo;
    private String targetHospitalName;
    private String patientCode;
    private String patientName;
    private String patientPhoto;
    private ArrayList<CheckTypeBean> checkList;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getCreateAt() {
        return createAt;
    }

    public void setCreateAt(long createAt) {
        this.createAt = createAt;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getTargetHospitalName() {
        return targetHospitalName;
    }

    public void setTargetHospitalName(String targetHospitalName) {
        this.targetHospitalName = targetHospitalName;
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

    public ArrayList<CheckTypeBean> getCheckList() {
        return checkList;
    }

    public void setCheckList(ArrayList<CheckTypeBean> checkList) {
        this.checkList = checkList;
    }
}
