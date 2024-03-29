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
     * 个人简介
     */
    UPDATE_INTRODUCE,
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
     * 单条消息已读 通知栏
     */
    UPDATE_APP_UNREAD_MESSAGE_BY_NOTIFY,
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
    GET_HOSPITAL_LIST_BY_TRANSFER_OTHER,
    /**
     * 医生接诊时选择接诊医院或者改变接诊信息时选择医院 。
     */
    GET_HOSPITAL_LIST_BY_RECEIVE,
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
     * 接诊医生
     */
    GET_RECEIVING_DOCTOR_LIST,
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
     * 根据医生编码获取居民列表信息
     */
    GET_PATIENT_LIST_BY_DOCTOR_CODE,
    /**
     * 获取医生好友列表信息
     */
    GET_DOCTOR_LIST,
    /**
     * 根据居民编码获取居民信息
     */
    GET_PATIENT_DETAIL_BY_PATIENT_CODE,
    /**
     * 根据居民编码获取居民订单列表（远程、检查、转诊）
     */
    GET_PATIENT_ORDER_LIST_BY_PATIENT_CODE,
    /**
     * 居民身份校验
     */
    VERIFY_PATIENT,
    /**
     * 查询居民是否存在未完成的转诊单
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
    /**
     * 获取当前医生的合作医院
     */
    GET_COOPERATE_HOSPITAL_LIST,
    /**
     * 获取当前医生的合作医院详情
     */
    GET_COOPERATE_HOSPITAL_DETAIL,
    /**
     * 获取当前医生的合作医院下服务项
     */
    GET_COOPERATE_HOSPITAL_PROJECT_LIST,
    /**
     * 获取当前医生的合作医院下服务包
     */
    GET_COOPERATE_HOSPITAL_PACKAGE_LIST,
    /**
     * 获取当前医生的合作医院下服务项详情
     */
    GET_COOPERATE_HOSPITAL_PROJECT_DETAIL,
    /**
     * 获取医生二维码（医生端扫描）
     */
    GET_DOCTOR_QR_CODE,
    /**
     * 获取标签
     */
    GET_LABEL,
    /**
     * 保存居民标签
     */
    SAVE_PATIENT_LABEL,
    /**
     * 获取居民标签
     */
    GET_PATIENT_LABEL,
    /**
     * 删除居民标签
     */
    DELETE_PATIENT_LABEL,
    /**
     * 获取已存在居民标签
     */
    GET_EXIST_LABEL,
    /**
     * 根据标签获取居民
     */
    GET_PATIENT_BY_LABEL,
    /**
     * 获取最近添加的居民
     */
    GET_RECENT_ADD_PATIENT,
    /**
     * 远程会诊
     */
    APPLY_REMOTE,
    /**
     * 远程会诊发布意见
     */
    APPLY_REMOTE_ADVICE,
    /**
     * 远程会诊详情
     */
    GET_REMOTE_DETAIL,
    /**
     * 扫码获取居民信息
     */
    GET_PATIENT_BY_QR_ID,
    /**
     * 扫码获取医生信息
     */
    GET_DOCTOR_BY_QR_ID,
    /**
     * 添加好友
     */
    ADD_DOCTOR_FRIEND,
    /**
     * 在当天日期查询已经有的预约时间信息
     */
    GET_REMOTE_TIME,
    /**
     * 获取远程科室列表
     */
    GET_REMOTE_DEPARTMENT_INFO,
    /**
     * 新增远程会诊
     */
    ADD_RESERVE_REMOTE_ORDER,
    /**
     * 获取会诊订单列表
     */
    GET_RESERVE_REMOTE_ORDER_LIST,
    /**
     * 健康管理列表
     */
    QUERY_PACKAGE_LIST,
    /**
     * 健康管理详情
     */
    QUERY_PACKAGE_DETAIL,
    /**
     * 医生回执报告
     */
    DOCTOR_REPORT,
    /**
     * 获取预约转诊订单超时取消时间
     */
    GET_TRANSFER_TIMEOUT,
}

