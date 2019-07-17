package com.yht.frame.data;

/**
 * 此类为任务队列编号，根据任务队列编号确定是否取消的任务
 *
 * @author dundun
 */
public enum Tasks {/**
 * 微信登录
 */
WE_CHAT_LOGIN,
    /**
     * 微信绑定
     */
    WE_CHAT_BIND,
    /**
     * 获取验证码
     */
    GET_VERIFY_CODE,
    /**
     * 登录 注册
     */
    LOGIN_AND_REGISTER,
    /**
     * 用户协议最后更新时间
     */
    GET_PROTOCOL_UPDATE_DATE,
    /**
     * 上传文件
     */
    UPLOAD_FILE,
    /**
     * 获取消息列表
     */
    GET_APP_MESSAGE_LIST,
    /**
     * 获取未读消息总数
     */
    GET_APP_UNREAD_MESSAGE_TOTAL,
    /**
     * 消息全部已读
     */
    UPDATE_APP_UNREAD_MESSAGE_ALL,
    /**
     * 单条消息已读
     */
    UPDATE_APP_UNREAD_MESSAGE_BY_ID,
    /**
     * 获取医院列表 (认证模块)
     */
    GET_HOSPITAL_LIST_BY_AUTH,
    /**
     * 获取当前医生有预约转诊权限的合作医院。
     */
    GET_HOSPITAL_LIST_BY_RESERVE,
    /**
     * 获取当前医生可进行转诊的医院列表。
     */
    GET_HOSPITAL_LIST_BY_DOCTOR,
    /**
     * 校验医生是否有预约检查和预约转诊的合作医院。
     */
    GET_VALIDATE_HOSPITAL_LIST,
    /**
     * 获取当前医生有预约转诊权限的合作医院下面的一级级科室。
     */
    GET_DEPART_ONE_LIST_BY_REVERSE,
    /**
     * 获取当前医生有预约转诊权限的合作医院下面的二级级科室。
     */
    GET_DEPART_TWO_LIST_BY_REVERSE,
    /**
     * 获取当前医生有预约转诊权限的合作医院下面的医生
     */
    GET_DOCTOR_LIST_BY_REVERSE,
    /**
     * 根据医院code获取科室树
     */
    GET_DEPART_LIST,
    /**
     * 提交医生认证资料
     */
    SUBMIT_DOCTOR_AUTH,
    /**
     * 获取医生认证资料
     */
    GET_DOCTOR_AUTH,
    /**
     * 获取医生个人详情
     */
    GET_DOCTOR_INFO,
    /**
     * 数据字典
     */
    DATA_JOB_TITLE,
    /**
     * 取医生基本信息+收入信息
     */
    GET_DOCTOR_INFO_AND_BALANCE_INFO,
    /**
     * 取医生收入详情
     */
    GET_DOCTOR_INCOME_DETAIL,
    /**
     * 医生提现信息
     */
    GET_DOCTOR_WITHDRAW,
    /**
     * 医生一个月提现信息
     */
    GET_DOCTOR_WITHDRAW_BY_MONTH,
    /**
     * 医生收入明细（列表 包含提现）
     */
    GET_DOCTOR_INCOME_LIST,
    /**
     * 医生收入明细（列表/不包含提现）
     */
    GET_DOCTOR_INCOME_WITHOUT_LIST,
    /**
     * 医生某月收入明细信息 (列表)
     */
    GET_DOCTOR_INCOME_BY_MONTH_LIST,
    /**
     * 根据医生编码获取患者列表信息
     */
    GET_PATIENT_LIST_BY_DOCTOR_CODE,
    /**
     * 根据患者编码获取患者信息
     */
    GET_PATIENT_DETAIL_BY_PATIENT_CODE,
    /**
     * 根据患者编码获取患者订单列表（远程、检查、转诊）
     */
    GET_PATIENT_ORDER_LIST_BY_PATIENT_CODE,
    /**
     * 患者身份校验
     */
    VERIFY_PATIENT,
    /**
     * 查询患者是否存在未完成的转诊单
     */
    GET_PATIENT_EXIST_TRANSFER,
    /**
     * 新增预约检查订单
     */
    ADD_RESERVE_CHECK_ORDER,
    /**
     * 获取检查订单列表
     */
    GET_RESERVE_CHECK_ORDER_LIST,
    /**
     * 获取检查订单详情
     */
    GET_RESERVE_CHECK_ORDER_DETAIL,
    /**
     * 获取检查项
     */
    GET_CHECK_TYPE,
    /**
     * 根据医院code获取检查项
     */
    GET_CHECK_TYPE_BY_HOSPITAL,
    /**
     * 新增预约转诊订单
     */
    ADD_RESERVE_TRANSFER_ORDER,
    /**
     * 取消预约转诊订单
     */
    CANCEL_RESERVE_TRANSFER_ORDER,
    /**
     * 接受预约转诊订单
     */
    RECEIVE_RESERVE_TRANSFER_ORDER,
    /**
     * 变更接诊信息
     */
    UPDATE_RESERVE_TRANSFER_ORDER,
    /**
     * 拒绝预约转诊订单
     */
    REJECT_RESERVE_TRANSFER_ORDER,
    /**
     * 拒绝预约转诊订单
     */
    TRANSFER_AGAIN_OTHER_DOCTOR,
    /**
     * 查询发起的转诊记录
     */
    GET_INITIATE_TRANSFER_ORDER_LIST,
    /**
     * 根据状态查询转诊记录
     */
    GET_TRANSFER_STATUS_ORDER_LIST,
    /**
     * 查询发起的转诊记录
     */
    GET_TRANSFER_ORDER_DETAIL,
    /**
     * 获取所有订单数量
     */
    GET_STUDIO_ORDER_STATISTICS,
    /**
     * banner
     */
    GET_BANNER,
    /**
     * 版本更新
     */
    GET_VERSION,
    /**
     * 开始聊天
     */
    START_CHAT,
    /**
     * 获取聊天剩余时间
     */
    GET_CHAT_LAST_TIME,
    /**
     * 结束聊天
     */
    END_CHAT,
   }


