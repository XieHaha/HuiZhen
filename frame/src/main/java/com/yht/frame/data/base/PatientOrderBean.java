package com.yht.frame.data.base;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.ArrayList;

/**
 * @author 顿顿
 * @date 19/6/29 10:57
 * @des 患者订单 检查 转诊 远程
 */
public class PatientOrderBean implements MultiItemEntity {
    /**
     * 检查
     */
    public static final int CHECK = 1;
    /**
     * 远程
     */
    public static final int REMOTE = 2;
    /**
     * 转诊
     */
    public static final int TRANSFER = 3;
    /**
     * type
     */
    private int type;
    private int status;
    private String time;
    private String orderNo;
    private String sourceHospitalName;
    private String sourceHospitalDepartmentName;
    private String sourceDoctorName;
    private String targetHospitalName;
    private ArrayList<CheckTypeBean> trans;

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

    public ArrayList<CheckTypeBean> getTrans() {
        return trans;
    }

    public void setTrans(ArrayList<CheckTypeBean> trans) {
        this.trans = trans;
    }

    @Override
    public int getItemType() {
        return type;
    }
}
