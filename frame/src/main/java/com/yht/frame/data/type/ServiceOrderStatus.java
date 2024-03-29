package com.yht.frame.data.type;

/**
 * @author 顿顿
 * @date 19/7/10 14:01
 * @description 检查订单状态
 */
public interface ServiceOrderStatus {
    /**
     * -未完成
     */
    int CHECK_ORDER_STATUS_INCOMPLETE = 0;
    /**
     * -已完成
     */
    int CHECK_ORDER_STATUS_COMPLETE = 1;
    /**
     * -已取消
     */
    int CHECK_ORDER_STATUS_CANCEL = 2;
}
