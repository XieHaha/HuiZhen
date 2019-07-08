package com.yht.frame.data;

/**
 * @author dundun
 * @date 19/2/19
 * 订单状态
 */
public interface OrderStatus {
    /**
     * 0-未完成
     */
    int ORDER_STATUS_INCOMPLETE = 0;
    /**
     * 1-已完成
     */
    int ORDER_STATUS_COMPLETE = 1;
    /**
     * 3-已取消
     */
    int ORDER_STATUS_CANCEL = 4;
}
