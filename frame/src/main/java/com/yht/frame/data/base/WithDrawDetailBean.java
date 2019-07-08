package com.yht.frame.data.base;

import java.io.Serializable;

/**
 * @author 顿顿
 * @date 19/7/8 20:17
 * @des 提现详情
 */
public class WithDrawDetailBean implements Serializable {
    private static final long serialVersionUID = 978498117984975933L;
    private int transferType;
    private String medCoin;
    private String doctorCode;
    private String createAt;
    private String status;
    private String remark;
    private String transferTypeShow;

    public String getMedCoin() {
        return medCoin;
    }

    public void setMedCoin(String medCoin) {
        this.medCoin = medCoin;
    }

    public int getTransferType() {
        return transferType;
    }

    public void setTransferType(int transferType) {
        this.transferType = transferType;
    }

    public String getDoctorCode() {
        return doctorCode;
    }

    public void setDoctorCode(String doctorCode) {
        this.doctorCode = doctorCode;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTransferTypeShow() {
        return transferTypeShow;
    }

    public void setTransferTypeShow(String transferTypeShow) {
        this.transferTypeShow = transferTypeShow;
    }
}
