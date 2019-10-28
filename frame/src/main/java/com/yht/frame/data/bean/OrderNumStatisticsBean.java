package com.yht.frame.data.bean;

import java.io.Serializable;

/**
 * @author 顿顿
 * @date 19/7/11 14:13
 * @description
 */
public class OrderNumStatisticsBean implements Serializable {
    private static final long serialVersionUID = -9097089869382102602L;
    /**
     * 发起检查
     */
    private int initiateOrderCheck;
    /**
     * 发起转诊
     */
    private int initiateOrderTransfer;
    /**
     * 待处理转诊
     */
    private int pendingOrderTransfer;
    /**
     * 已接收转诊
     */
    private int receiveOrderTransfer;
    /**
     * 远程会诊
     */
    private int initiateRemoteCheck;

    public int getInitiateOrderCheck() {
        return initiateOrderCheck;
    }

    public void setInitiateOrderCheck(int initiateOrderCheck) {
        this.initiateOrderCheck = initiateOrderCheck;
    }

    public int getInitiateOrderTransfer() {
        return initiateOrderTransfer;
    }

    public void setInitiateOrderTransfer(int initiateOrderTransfer) {
        this.initiateOrderTransfer = initiateOrderTransfer;
    }

    public int getPendingOrderTransfer() {
        return pendingOrderTransfer;
    }

    public void setPendingOrderTransfer(int pendingOrderTransfer) {
        this.pendingOrderTransfer = pendingOrderTransfer;
    }

    public int getReceiveOrderTransfer() {
        return receiveOrderTransfer;
    }

    public void setReceiveOrderTransfer(int receiveOrderTransfer) {
        this.receiveOrderTransfer = receiveOrderTransfer;
    }

    public int getInitiateRemoteCheck() {
        return initiateRemoteCheck;
    }

    public void setInitiateRemoteCheck(int initiateRemoteCheck) {
        this.initiateRemoteCheck = initiateRemoteCheck;
    }
}