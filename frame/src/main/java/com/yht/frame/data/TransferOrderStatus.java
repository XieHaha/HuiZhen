package com.yht.frame.data;

/**
 * @author 顿顿
 * @date 19/7/10 14:01
 * @des 转诊状态
 */
public interface TransferOrderStatus {
    /**
     * -待接诊
     */
    int TRANSFER_STATUS_WAIT = 0;
    /**
     * -已接诊
     */
    int TRANSFER_STATUS_RECEIVED = 1;
    /**
     * -已转诊
     */
    int TRANSFER_STATUS_TRANSFED = 2;
    /**
     * -已取消
     */
    int TRANSFER_STATUS_CANCEL = 3;
    /**
     * -已拒绝
     */
    int TRANSFER_STATUS_REFUSE = 4;
}
