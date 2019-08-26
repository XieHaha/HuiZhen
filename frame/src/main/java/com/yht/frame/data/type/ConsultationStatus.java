package com.yht.frame.data.type;

/**
 * @author 顿顿
 * @date 19/8/26 15:12
 * @des 会诊状态
 * 会诊状态:0:未开始;1:会诊中;2:中断;3:全员拒绝;4:已完成;5:已关闭;6:超时关闭;7:中断关闭;8:全员拒绝关闭
 */
public interface ConsultationStatus {
    /**
     * 初始状态 未开始
     */
    int CONSULTATION_NONE = 0;
    /**
     * 会诊中
     */
    int CONSULTATION_ING = 1;
    /**
     * 中断
     */
    int CONSULTATION_INTERRUPT = 2;
    /**
     * 全员拒绝
     */
    int CONSULTATION_ALL_REJECT = 3;
    /**
     * 已完成
     */
    int CONSULTATION_COMPLETE = 4;
    /**
     * 已关闭
     */
    int CONSULTATION_CLOSE = 5;
    /**
     * 超时关闭
     */
    int CONSULTATION_CLOSE_BY_TIMEOUT = 6;
    /**
     * 中断关闭
     */
    int CONSULTATION_CLOSE_BY_INTERRUPT = 7;
    /**
     * 全员拒绝关闭
     */
    int CONSULTATION_CLOSE_ALL_REJECT = 8;
}
