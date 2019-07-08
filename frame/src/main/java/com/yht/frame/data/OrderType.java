package com.yht.frame.data;

/**
 * @author dundun
 * @date 19/2/19
 * 订单类型
 */
public interface OrderType {
    /**
     * 1-预约检查
     */
    int ORDER_TYPE_CHECK = 1;
    /**
     * 2-远程会诊
     */
    int ORDER_TYPE_REMOTE = 2;
    /**
     * 3-预约转诊
     */
    int ORDER_TYPE_TRANSFER = 3;
}
