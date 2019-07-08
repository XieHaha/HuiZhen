package com.yht.frame.data.base;

import java.io.Serializable;

/**
 * @author 顿顿
 * @date 19/7/8 18:22
 * @des 医生资金明细列表
 */
public class DoctorCurrencyDetailBean implements Serializable {
    private static final long serialVersionUID = -8093502912970407325L;
    private int doctorOrderTranId;
    private int serviceFlag;
    private String orderAt;
    private String serviceFlagName;
    private String total;
    private String arrived;

    public int getDoctorOrderTranId() {
        return doctorOrderTranId;
    }

    public void setDoctorOrderTranId(int doctorOrderTranId) {
        this.doctorOrderTranId = doctorOrderTranId;
    }

    public int getServiceFlag() {
        return serviceFlag;
    }

    public void setServiceFlag(int serviceFlag) {
        this.serviceFlag = serviceFlag;
    }

    public String getOrderAt() {
        return orderAt;
    }

    public void setOrderAt(String orderAt) {
        this.orderAt = orderAt;
    }

    public String getServiceFlagName() {
        return serviceFlagName;
    }

    public void setServiceFlagName(String serviceFlagName) {
        this.serviceFlagName = serviceFlagName;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getArrived() {
        return arrived;
    }

    public void setArrived(String arrived) {
        this.arrived = arrived;
    }
}
