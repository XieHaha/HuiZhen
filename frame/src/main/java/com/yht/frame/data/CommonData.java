package com.yht.frame.data;

/**
 * @author dundun
 */
public interface CommonData {
    /**
     * 公用数据key
     */
    String KEY_PUBLIC = "key_public";
    /**
     * 公用数据key
     */
    String KEY_TITLE = "key_title";
    /**
     * 公用数据key
     */
    String KEY_PUBLIC_STRING = "key_public_string";
    /**
     * 是否执行某种意图
     */
    String KEY_INTENT_BOOLEAN = "key_intent_boolean";
    /**
     * 是否取消检查更新
     */
    String KEY_HIDE_VERSION_UPDATE = "key_hide_version_update";
    /**
     * 自定义url
     */
    String KEY_BASE_URL = "key_base_url";
    /**
     * 环信登录状态
     */
    String KEY_EASE_LOGIN_STATUS = "key_ease_login_status";
    /**
     * 登录账号
     */
    String KEY_LOGIN_ACCOUNT = "key_login_account";
    /**
     * 检查或者转诊  true为转诊  false为检查
     */
    String KEY_CHECK_OR_TRANSFER = "key_check_or_transfer";
    /**
     * 预约类型
     */
    String KEY_RESERVATION_TYPE = "key_reservation_type";
    /**
     * 是否为转给他人
     */
    String KEY_IS_TRANSFER_OTHER = "key_is_transfer_other";
    /**
     * 变更接诊信息  or  接诊
     */
    String KEY_RECEIVE_OR_EDIT_VISIT = "key_receive_or_edit_visit";
    /**
     * 变更接诊信息  or  接诊
     */
    String KEY_IS_RECEIVE_DOCTOR = "key_is_receive_doctor";
    /**
     * 是否显示的是登录协议（登录协议页面不显示title）
     */
    String KEY_IS_PROTOCOL = "key_is_protocol";
    /**
     * 协议更新时间
     */
    String KEY_IS_PROTOCOL_UPDATE_DATE = "key_is_protocol_update_date";
    /**
     * 登录成功返回数据
     */
    String KEY_LOGIN_BEAN = "key_login_success_bean";
    /**
     * 微信登录成功返回数据
     */
    String KEY_WECHAT_LOGIN_SUCCESS_BEAN = "key_wechat_login_success_bean";
    /**
     * 聊天id
     */
    String KEY_CHAT_ID = "key_chat_id";
    /**
     * 聊天 name
     */
    String KEY_CHAT_NAME = "key_chat_name";
    /**
     * 金额是否显示
     */
    String KEY_SHOW_CURRENCY = "key_show_currency";
    /**
     * 是否查询所有收入详情
     */
    String KEY_SHOW_ALL = "key_show_all";
    /**
     * 收入详情bean
     */
    String KEY_DOCTOR_CURRENCY_BEAN = "key_doctor_currency_bean";
    /**
     * 收入详情bean 单条id
     */
    String KEY_DOCTOR_CURRENCY_ID = "key_doctor_currency_id";
    /**
     * 转诊订单
     */
    String KEY_TRANSFER_ORDER_BEAN = "key_transfer_order_bean";
    /**
     * 订单id
     */
    String KEY_ORDER_ID = "key_order_id";
    /**
     * patient bean
     */
    String KEY_PATIENT_BEAN = "key_patient_bean";
    /**
     * patient code
     */
    String KEY_PATIENT_CODE = "key_patient_code";
    /**
     * patient 聊天
     */
    String KEY_PATIENT_CHAT = "key_patient_chat";
    /**
     * 医生 聊天
     */
    String KEY_DOCTOR_CHAT = "key_doctor_chat";
    /**
     * patient name
     */
    String KEY_PATIENT_NAME = "key_patient_name";
    /**
     * doctor bean
     */
    String KEY_DOCTOR_BEAN = "key_doctor_bean";
    /**
     * doctor
     */
    String KEY_DOCTOR_QR_CODE_BEAN = "key_doctor_qr_code_bean";
    /**
     * hospital bean
     */
    String KEY_HOSPITAL_BEAN = "key_hospital_bean";
    /**
     * hospital product bean
     */
    String KEY_HOSPITAL_PRODUCT_BEAN = "key_hospital_product_bean";
    /**
     * hospital CODE
     */
    String KEY_HOSPITAL_CODE = "key_hospital_code";
    /**
     * depart bean
     */
    String KEY_DEPART_BEAN = "key_depart_bean";
    /**
     * 一级科室 position
     */
    String KEY_DEPART_POSITION = "key_depart_position";
    /**
     * 二级科室 position
     */
    String KEY_DEPART_CHILD_POSITION = "key_depart_child_position";
    /**
     * 预约检查选择的检查项
     */
    String KEY_RESERVE_CHECK_TYPE_BEAN = "key_reserve_check_type_bean";
    /**
     * 预约检查选择的检查项列表
     */
    String KEY_RESERVE_CHECK_TYPE_LIST = "key_reserve_check_type_list";
    /**
     * 检查报告列表
     */
    String KEY_CHECK_REPORT_LIST = "key_check_report_list";
    /**
     * 检查项id
     */
    String KEY_CHECK_TYPE_ID = "key_check_type_id";
    /**
     * 广告页下载链接
     */
    String KEY_SPLASH_IMG_URL = "key_splash_img_url";
    /**
     * 居民列表数据更新
     */
    String KEY_UPDATE_PATIENT_DATA = "key_update_patient_data";
    /**
     * 医生列表数据更新
     */
    String KEY_UPDATE_DOCTOR_DATA = "key_update_doctor_data";
    /**
     * 通知开关
     */
    String KEY_NOTIFICATION_CONTROL = "key_notification_control";
    /**
     * 系统消息未读状态
     */
    String KEY_SYSTEM_MESSAGE_UNREAD_STATUS = "key_system_message_unread_status";
    /**
     * 环信消息未读状态
     */
    String KEY_EASE_MESSAGE_UNREAD_STATUS = "key_ease_message_unread_status";
    /**
     * 外链跳转页面销毁逻辑
     */
    String KEY_IS_OUTER_CHAIN = "key_is_outer_chain";
    /**
     * 标签分组
     */
    String KEY_LABEL_BEAN = "key_label_bean";
    /**
     * 选择的远程科室列表
     */
    String KEY_REMOTE_DEPART_LIST = "key_remote_depart_list";
    /**
     * 选择的远程科室列表 下标
     */
    String KEY_REMOTE_DEPART_LIST_POSITION = "key_remote_depart_list_position";
    /**
     * 远程会诊 日期
     */
    String KEY_REMOTE_DATE = "key_remote_date";
    /**
     * 远程会诊 开始时间
     */
    String KEY_REMOTE_START_HOUR = "key_remote_start_hour";
    /**
     * 远程会诊 结束时间
     */
    String KEY_REMOTE_END_HOUR = "key_remote_end_hour";
}
