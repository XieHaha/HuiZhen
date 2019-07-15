package com.yht.frame.data;

/**
 * @author 顿顿
 * @date 19/7/15 13:39
 * @des 推送消息、系统消息管理
 */
public enum MessageType {/**
 * 医生认证成功
 */
MESSAGE_DOCTOR_AUTH_SUCCESS("A001", 1),
    /**
     * 医生认证不通过
     */
    MESSAGE_DOCTOR_AUTH_FAILED("A002", 2),
    /**
     * 医生订单有检查报告上传
     */
    MESSAGE_SERVICE_REPORT("C007", 3),
    /**
     * 收到转诊申请
     */
    MESSAGE_TRANSFER_APPLY("D002", 4),
    /**
     * 预约转诊被拒绝
     */
    MESSAGE_TRANSFER_REJECT("D004", 5),
    /**
     * 转诊申请通过
     */
    MESSAGE_TRANSFER_RECEIVED("D005", 6),
    /**
     * 转诊申请被转移给其他医生
     */
    MESSAGE_TRANSFER_OTHER("D008", 7),
    /**
     * 接诊医生变更接诊信息
     */
    MESSAGE_TRANSFER_UPDATE("D010", 8),
    /**
     * 转诊医生取消转诊
     */
    MESSAGE_TRANSFER_CANCEL("D012", 9),
    /**
     * 规定时间未处理转诊单，系统取消转诊（接诊医生收）
     */
    MESSAGE_TRANSFER_SYSTEM_CANCEL_R("D013", 10),
    /**
     * 规定时间未处理转诊单，系统取消转诊（转诊医生收）
     */
    MESSAGE_TRANSFER_SYSTEM_CANCEL_T("D014", 11),
    /**
     * 会诊即将开始提醒
     */
    MESSAGE_REMOTE_START("E002", 12),
    /**
     * 会诊确认提醒
     */
    MESSAGE_REMOTE_SURE("E003", 13),
    /**
     * 会诊拒绝提醒
     */
    MESSAGE_REMOTE_REJECT("E004", 14),
    /**
     * 会诊取消提醒
     */
    MESSAGE_REMOTE_CANCEL("E005", 15),
    /**
     * 发布会诊意见提醒
     */
    MESSAGE_REMOTE_ADVICE("E006", 16),
    /**
     * 会诊延期提醒
     */
    MESSAGE_REMOTE_DELAY("E007", 17),
    /**
     * 填写会诊意见提醒
     */
    MESSAGE_REMOTE_INPUT_ADVICE("E008", 18),
    /**
     * 新会诊邀请提醒
     */
    MESSAGE_REMOTE_INVITE("E009", 19),
    /**
     * 报告完成，会珍币到账
     */
    MESSAGE_CURRENCY_ARRIVED("F001", 20),
    /**
     * 总后台扣除会珍币
     */
    MESSAGE_CURRENCY_DEDUCTION("F002", 21),
    /**
     * 中台创建医生，推送给医生
     */
    MESSAGE_ACCOUNT_CREATE("G003", 22);
    private String msgType;
    private int msgCode;

    public String getMsgType() {
        return msgType;
    }

    public int getMsgCode() {
        return msgCode;
    }

    MessageType(String msgType, int msgCode) {
        this.msgType = msgType;
        this.msgCode = msgCode;
    }

    /**
     * 根据key获取枚举
     *
     * @param key
     * @return
     */
    public static int getValue(String key) {
        if (null == key) {
            return 0;
        }
        for (MessageType temp : MessageType.values()) {
            if (temp.getMsgType().equals(key)) {
                return temp.msgCode();
            }
        }
        return 0;
    }

    public Integer msgCode() {
        return this.msgCode;
    }

    public String msgType() {
        return this.msgType;
    }}
