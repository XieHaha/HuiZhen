package com.yht.frame.data.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author 顿顿
 * @date 19/10/25 18:35
 * @description 会诊
 */
public class RemoteBean implements Serializable {
    private static final long serialVersionUID = 5775065159299464466L;
    private int status;
    private long startAt;
    private long endAt;
    private String orderNo;
    private String patientName;
    private String patientPhoto;
    private ArrayList<InvitDepartBean> invitationList;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
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

    public ArrayList<InvitDepartBean> getInvitationList() {
        return invitationList;
    }

    public void setInvitationList(ArrayList<InvitDepartBean> invitationList) {
        this.invitationList = invitationList;
    }
}
