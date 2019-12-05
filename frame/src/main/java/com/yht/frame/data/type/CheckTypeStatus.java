package com.yht.frame.data.type;

/**
 * @author dundun
 * @date 19/2/19
 * 预约检查项状态
 */
public interface CheckTypeStatus {
    /**
     * 1-待确认
     */
    int CHECK_TYPE_STATUS_WAIT = 0;
    /**
     * 1-待付款
     */
    int CHECK_TYPE_STATUS_WAIT_PAY = 1;
    /**
     * 2-待上传报告
     */
    int CHECK_TYPE_STATUS_PAID = 2;
    /**
     * 3-已完成(报告已上传)
     */
    int CHECK_TYPE_STATUS_COMPLETE = 3;
    /**
     * 4-已取消
     */
    int CHECK_TYPE_STATUS_CANCEL = 4;
    /**
     * 5-待发放
     */
    int CHECK_TYPE_STATUS_PENDING = 5;
    /**
     * 6-退款中
     */
    int CHECK_TYPE_STATUS_REFUNDING = 6;
    /**
     * 7-退款失败
     */
    int CHECK_TYPE_STATUS_REFUND_FAILED = 7;
    /**
     * 8-退款成功
     */
    int CHECK_TYPE_STATUS_REFUND_SUCCESS = 8;
}
