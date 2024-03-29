package com.yht.frame.data.type;

/**
 * @author 顿顿
 * @date 19/7/13 13:50
 * @description 居民订单状态（检查、转诊、远程）
 */
public interface PatientOrderStatus {
    /**
     * 未完成
     */
    int PATIENT_ORDER_INCOMPLETE = 0;
    /**
     * 已完成
     */
    int PATIENT_ORDER_COMPLETE = 1;
    /**
     * 已取消
     */
    int PATIENT_ORDER_CANCEL = 2;
    /**
     * 已拒绝
     */
    int PATIENT_ORDER_REJECT = 3;
}
