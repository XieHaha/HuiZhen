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
     * 上传文件
     */
    UPLOAD_FILE,
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
     * 获取环信appkey
     */
    GET_EASE_APPKEY,
    /**
     * 获取广告业
     */
    GET_SPLASH,
    /**
     * 上传基本信息
     */
    UPDATE_BASIC_INFO,
    /**
     * 上传职业信息
     */
    UPDATE_JOB_INFO,
    /**
     * 修改个人信息
     */
    UPDATE_USER_INFO,
    /**
     * 获取患者列表
     */
    GET_PATIENTS_LIST,
    /**
     * 获取转诊患者申请列表
     */
    GET_PATIENTS_TO_LIST,
    /**
     * 获取收到转诊患者列表
     */
    GET_PATIENTS_FROM_LIST,
    /**
     * 转诊记录（转入转出）
     */
    GET_TRANSFER_LIST,
    /**
     * 开单记录
     */
    GET_ORDER_LIST,
    /**
     * 医生转诊患者
     */
    ADD_PATIENT_BY_SCAN_OR_CHANGE_PATIENT,
    /**
     * 合作医生申请
     */
    APPLY_COOPERATE_DOC,
    /**
     * 合作医生备注设置
     */
    MODIFY_NICK_NAME,
    /**
     * 患者备注设置
     */
    MODIFY_NICK_NAME_BY_PATIENT,
    /**
     * 获取合作医生列表
     */
    GET_COOPERATE_DOC_LIST,
    /**
     * 获取申请合作医生列表
     */
    GET_APPLY_COOPERATE_DOC_LIST,
    /**
     * 获取患者申请列表
     */
    GET_APPLY_PATIENT_LIST,
    /**
     * 获取医生个人信息
     */
    GET_DOC_INFO,
    /**
     * 获取转诊详情
     */
    GET_TRANSFER_DETAIL_BY_ID,
    /**
     * 取消转诊
     */
    CANCEL_TRANSFER_PATIENT,
    /**
     * 拒绝转诊
     */
    REFUSE_TRANSFER_PATIENT,
    /**
     * 接受转诊
     */
    RECV_TRANSFER_PATIENT,
    /**
     * 医生转诊患者
     */
    ADD_TRANSFER_PATIENT,
    /**
     * 取消合作医生关系
     */
    CANCEL_COOPERATE_DOC,
    /**
     * 获取患者个人信息
     */
    GET_PATIENT_INFO,
    /**
     * 删除患者（取消关注）
     */
    DELETE_PATIENT,
    /**
     * 获取合作医院
     */
    GET_COOPERATE_HOSPITAL_LIST,
    /**
     * 获取病例详情
     */
    GET_CASE_DETAIL_BY_ID,
    /**
     * 拒绝患者申请
     */
    REFUSE_PATIENT_APPLY,
    /**
     * 同意患者申请
     */
    AGREE_PATIENT_APPLY,
    /**
     * 获取患者病例列表
     */
    GET_PATIENT_LIMIT_CASE_LIST,
    /**
     * 好友验证
     */
    FRIENDS_VERIFY,
    /**
     * 删除患者病例
     */
    DELETE_PATIENT_CASE,
    /**
     * 处理医生申请合作（proCode为字符1（表示同意）或字符3（表示拒绝））
     */
    DEAL_DOC_APPLY,
    /**
     * 版本更新
     */
    UPDATE_VERSION,
    /**
     * 文件下载
     */
    DOWNLOAD_FILE,
    /**
     * 医生资质认证
     */
    QUALIFIY_DOC,
    /**
     * 获取医院列表
     */
    GET_HOSPITAL_LIST,
    /**
     * 获取医院商品列表
     */
    GET_HOSPITAL_PRODUCT_LIST,
    /**
     * 根据医生id获取获取医院列表
     */
    GET_HOSPITAL_LIST_BY_DOCTORID,
    /**
     * 根据医生id获取获取合作医院
     */
    GET_COOPERATE_HOSPITAL_LIST_BY_DOCTORID,
    /**
     * 根据医生id获取获取合作医院下所有医生
     */
    GET_COOPERATE_HOSPITAL_DOCTOR_LIST,
    /**
     * 根据医院id获取商品类型和类型下的商品详情
     */
    GET_HOSPITAL_PRODUCT_LIST_BY_HOSPITALID,
    /**
     * 新增订单
     */
    ADD_PRODUCT_ORDER_NEW,
    /**
     * 获取患者综合病史
     */
    GET_PATIENT_COMBINE,
    /**
     * 新增患者病例
     */
    ADD_PATIENT_CASE,
    /**
     * 新增患者病例
     */
    UPDATE_PATIENT_CASE,
    /**
     * 我的转诊记录
     */
    GET_TRANSFER_PATIENT_HISTORY_LIST,
    /**
     * 患者转诊记录
     */
    GET_TRANSFER_BY_PATIENT,
    /**
     * 患者订单记录
     */
    GET_PATIENT_ALL_ORDER_LIST,
    /**
     * 当前医生给患者开的订单记录
     */
    GET_PATIENT_ORDER_LIST,}

