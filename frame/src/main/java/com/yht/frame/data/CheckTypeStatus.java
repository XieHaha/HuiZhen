package com.yht.frame.data;

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
     * 2-已付款
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
}
