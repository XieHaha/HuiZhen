package com.yht.frame.data.type;

/**
 * @author 顿顿
 * @date 19/7/15 13:39
 * @des 推送消息、系统消息管理
 */
public interface MessageType {
    /**
     * 医生认证成功
     */
    String MESSAGE_DOCTOR_AUTH_SUCCESS = "A001";
    /**
     * 医生认证不通过
     */
    String MESSAGE_DOCTOR_AUTH_FAILED = "A002";
    /**
     * 医生订单有检查报告上传
     */
    String MESSAGE_SERVICE_REPORT = "C007";
    /**
     * 收到转诊申请
     */
    String MESSAGE_TRANSFER_APPLY = "D002";
    /**
     * 预约转诊被拒绝
     */
    String MESSAGE_TRANSFER_REJECT = "D004";
    /**
     * 转诊申请通过
     */
    String MESSAGE_TRANSFER_RECEIVED = "D005";
    /**
     * 转诊申请被转移给其他医生
     */
    String MESSAGE_TRANSFER_OTHER = "D008";
    /**
     * 接诊医生变更接诊信息
     */
    String MESSAGE_TRANSFER_UPDATE = "D010";
    /**
     * 转诊医生取消转诊
     */
    String MESSAGE_TRANSFER_CANCEL = "D012";
    /**
     * 规定时间未处理转诊单，系统取消转诊（接诊医生收）
     */
    String MESSAGE_TRANSFER_SYSTEM_CANCEL_R = "D013";
    /**
     * 规定时间未处理转诊单，系统取消转诊（转诊医生收）
     */
    String MESSAGE_TRANSFER_SYSTEM_CANCEL_T = "D014";
    /**
     * 会诊即将开始提醒
     */
    String MESSAGE_REMOTE_START = "E002";
    /**
     * 会诊确认提醒
     */
    String MESSAGE_REMOTE_SURE = "E003";
    /**
     * 会诊拒绝提醒
     */
    String MESSAGE_REMOTE_REJECT = "E004";
    /**
     * 会诊取消提醒
     */
    String MESSAGE_REMOTE_CANCEL = "E005";
    /**
     * 发布会诊意见提醒
     */
    String MESSAGE_REMOTE_ADVICE = "E006";
    /**
     * 会诊延期提醒
     */
    String MESSAGE_REMOTE_DELAY = "E007";
    /**
     * 填写会诊意见提醒
     */
    String MESSAGE_REMOTE_INPUT_ADVICE = "E008";
    /**
     * 新会诊邀请提醒
     */
    String MESSAGE_REMOTE_INVITE = "E009";
    /**
     * 报告完成，会珍币到账
     */
    String MESSAGE_CURRENCY_ARRIVED = "F001";
    /**
     * 总后台扣除会珍币
     */
    String MESSAGE_CURRENCY_DEDUCTION = "F002";
    /**
     * 中台创建医生，推送给医生
     */
    String MESSAGE_ACCOUNT_CREATE = "G003";
}
