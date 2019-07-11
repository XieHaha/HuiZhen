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
     * 环信登录状态
     */
    String KEY_EASE_LOGIN_STATUS = "key_ease_login_status";
    /**
     * 检查或者转诊  true为转诊  false为检查
     */
    String KEY_CHECK_OR_TRANSFER = "key_check_or_transfer";
    /**
     * 变更接诊信息  or  接诊
     */
    String KEY_RECEIVE_OR_EDIT_VISIT = "key_receive_or_edit_visit";
    /**
     * 是否显示的是登录协议（登录协议页面不显示title）
     */
    String KEY_IS_PROTOCOL = "key_is_protocol";
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
     * doctor bean
     */
    String KEY_DOCTOR_BEAN = "key_doctor_bean";
    /**
     * hospital bean
     */
    String KEY_HOSPITAL_BEAN = "key_hospital_bean";
    /**
     * hospital CODE
     */
    String KEY_HOSPITAL_CODE = "key_hospital_code";
    /**
     * depart bean
     */
    String KEY_DEPART_BEAN = "key_depart_bean";
    /**
     * 预约检查选择的检查项
     */
    String KEY_RESERVE_CHECK_TYPE_BEAN = "key_reserve_check_type_bean";
    /**
     * 预约检查选择的检查项列表
     */
    String KEY_RESERVE_CHECK_TYPE_LIST = "key_reserve_check_type_list";
    /**
     * 广告页下载链接
     */
    String KEY_SPLASH_IMG_URL = "key_splash_img_url";
    /**
     * 极光-合作医生申请码
     */
    int JIGUANG_CODE_COLLEBORATE_DOCTOR_REQUEST = 101;
    /**
     * 极光-合作医生添加成功
     */
    int JIGUANG_CODE_COLLEBORATE_ADD_SUCCESS = 102;
    /**
     * 医生认证成功
     */
    int JIGUANG_CODE_DOCTOR_INFO_CHECK_SUCCESS = 103;
    /**
     * 医生认证失败
     */
    int JIGUANG_CODE_DOCTOR_INFO_CHECK_FAILED = 104;
    /**
     * 医患好友添加成功 (医生端)
     */
    int JIGUANG_CODE_DOCTOR_DP_ADD_SUCCESS = 106;
    /**
     * 申请添加医生 患者申请(医生端)
     */
    int JIGUANG_CODE_DOCTOR_DP_ADD_REQUEST = 108;
    /**
     * 收到转诊通知 合作医生接受了我的转诊
     */
    int JIGUANG_CODE_TRANS_PATIENT_SUCCESS = 112;
    /**
     * 收到转诊通知 合作医生转给我的
     */
    int JIGUANG_CODE_TRANS_PATIENT_APPLY = 133;
    /**
     * 极光-合作医生修改转诊单-拒绝接收该转诊（发送给医生）
     */
    int JIGUANG_CODE_DOCTOR_TRANS_REFUSE = 134;
    /**
     * 极光-医院取消转诊（发送给发起医生）
     */
    int JIGUANG_CODE_FROM_DOCTOR_TRANSFER_FINISHED = 136;
    /**
     * 极光-医院取消转诊（发送给接受医生）
     */
    int JIGUANG_CODE_TO_DOCTOR_TRANSFER_FINISHED = 137;
    /**
     * 极光-患者确认服务包订单（发送给医生）
     */
    int JIGUANG_CODE_DOCTOR_PRODUCT_ACCEPTED = 141;
    /**
     * 极光-患者拒绝服务包订单（发送给医生）
     */
    int JIGUANG_CODE_DOCTOR_PRODUCT_REFUSED = 147;
    /**
     * 极光-后台确认完成检查（发送给医生）
     */
    int JIGUANG_CODE_DOCTOR_PRODUCT_FINISH = 144;
    /**
     * 极光-后台确认发送报告（发送给医生）
     */
    int JIGUANG_CODE_DOCTOR_PRODUCT_REPORT = 146;
    /**
     * 极光-医院确认患者就诊（发送给发起医生）
     */
    int JIGUANG_CODE_FROM_DOCTOR_TRANSFER_FINISH_SUCCESS = 160;
    /**
     * 极光-医院确认患者就诊（发送给接受医生）
     */
    int JIGUANG_CODE_TO_DOCTOR_TRANSFER_FINISH_SUCCESS = 161;
}
