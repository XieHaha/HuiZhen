package com.yht.frame.data.type;

/**
 * @author 顿顿
 * @date 19/10/28 13:56
 * @description 远程会诊
 */
public interface RemoteOrderStatus {
    /**
     * 未开始
     */
    int REMOTE_ORDER_STATUS_NONE = 0;
    /**
     * 会诊中
     */
    int REMOTE_ORDER_STATUS_IN_CONSULTATION = 1;
    /**
     * 中断
     */
    int REMOTE_ORDER_STATUS_INTERRUPT = 2;
    /**
     * 全员拒绝
     */
    int REMOTE_ORDER_STATUS_ALL_REFUSE = 3;
    /**
     * 已完成
     */
    int REMOTE_ORDER_STATUS_COMPLETE = 4;
    /**
     * 已关闭
     */
    int REMOTE_ORDER_STATUS_CLOSED = 5;
    /**
     * 超时关闭
     */
    int REMOTE_ORDER_STATUS_TIMEOUT_CLOSE = 6;
    /**
     * 中断关闭
     */
    int REMOTE_ORDER_STATUS_INTERRUPT_CLOSE = 7;
    /**
     * 全员拒绝关闭
     */
    int REMOTE_ORDER_STATUS_ALL_REFUSE_CLOSE = 8;
    /**
     * 审核中
     */
    int REMOTE_ORDER_STATUS_UNDER_REVIEW = 9;
    /**
     * 审核拒绝
     */
    int REMOTE_ORDER_STATUS_REVIEW_REFUSE = 10;
}
