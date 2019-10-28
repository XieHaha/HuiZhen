package com.yht.frame.data.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.ArrayList;

/**
 * @author 顿顿
 * @date 19/6/29 10:57
 * @des 居民订单 检查 转诊 远程
 */
public class PatientOrderBean implements MultiItemEntity {
    /**
     * 检查
     */
    public static final int CHECK = 1;
    /**
     * 转诊
     */
    public static final int TRANSFER = 2;
    /**
     * 远程
     */
    public static final int REMOTE = 3;
    /**
     * type
     */
    private int type;
    private int status;
    private long endAt;
    private String createAt;
    private String time;
    private String orderNo;
    private String sourceHospitalName;
    private String sourceHospitalDepartmentName;
    private String sourceDoctorName;
    private String targetHospitalName;
    private String targetHospitalDepartmentName;
    private String targetDoctorName;
    /**
     * 病情描述
     */
    private String descIll;
    /**
     * 预约服务检查项
     */
    private ArrayList<CheckTypeBean> trans;
    /**
     * 会诊参加者
     */
    private ArrayList<RemoteInvitedBean> invitationList;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
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

    public String getSourceDoctorName() {
        return sourceDoctorName;
    }

    public void setSourceDoctorName(String sourceDoctorName) {
        this.sourceDoctorName = sourceDoctorName;
    }

    public String getTargetHospitalName() {
        return targetHospitalName;
    }

    public void setTargetHospitalName(String targetHospitalName) {
        this.targetHospitalName = targetHospitalName;
    }

    public String getTargetHospitalDepartmentName() {
        return targetHospitalDepartmentName;
    }

    public void setTargetHospitalDepartmentName(String targetHospitalDepartmentName) {
        this.targetHospitalDepartmentName = targetHospitalDepartmentName;
    }

    public String getTargetDoctorName() {
        return targetDoctorName;
    }

    public void setTargetDoctorName(String targetDoctorName) {
        this.targetDoctorName = targetDoctorName;
    }

    public ArrayList<CheckTypeBean> getTrans() {
        return trans;
    }

    public void setTrans(ArrayList<CheckTypeBean> trans) {
        this.trans = trans;
    }

    public long getEndAt() {
        return endAt;
    }

    public void setEndAt(long endAt) {
        this.endAt = endAt;
    }

    public String getDescIll() {
        return descIll;
    }

    public void setDescIll(String descIll) {
        this.descIll = descIll;
    }

    public ArrayList<RemoteInvitedBean> getInvitationList() {
        return invitationList;
    }

    public void setInvitationList(ArrayList<RemoteInvitedBean> invitationList) {
        this.invitationList = invitationList;
    }

    @Override
    public int getItemType() {
        return type;
    }
}
