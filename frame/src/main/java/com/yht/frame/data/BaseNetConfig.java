package com.yht.frame.data;

/**
 * @author DUNDUN
 * @date 2016/11/29
 */
public interface BaseNetConfig {
    /**
     * 请求成功编码
     */
    int REQUEST_SUCCESS = 2000;
    /**
     * 其他错误
     */
    int REQUEST_OTHER_ERROR = 4000;
    /**
     * token错误或者失效
     */
    int REQUEST_TOKEN_ERROR = 4001;
    /**
     * 账号禁用
     */
    int REQUEST_ACCOUNT_ERROR = 4005;
    /**
     * 服务器
     */
    int REQUEST_SERVER_ERROR = 4010;
    /**
     * 微信未绑定
     */
    int REQUEST_WE_CHAT_BIND_ERROR = 4004;
    /**
     * 取消订单的时候，如果当前订单状态如果不是待接诊和已转诊，返回6001
     * 拒绝订单的时候，如果当前订单状态如果不是待接诊和已转诊，返回6001
     * 接诊的时候，如果当前订单状态如果不是待接诊和已转诊，返回6001
     * 转给他人的时候，如果当前订单状态如果不是待接诊和已转诊，返回6001
     */
    int REQUEST_ORDER_ERROR = 6001;
    /**
     * 预约服务提交时 服务项状态发生改变
     */
    int REQUEST_SUBMIT_SERVICE_STATUS_ERROR = 7001;
    /**
     * 预约服务提交时 服务项价格发生改变
     */
    int REQUEST_SUBMIT_SERVICE_PRICE_ERROR = 7002;
    /**
     * 预约会诊提交时 时间错误
     */
    int REQUEST_SUBMIT_REMOTE_TIME_ERROR = 7003;
    /**
     * 预约会诊提交时 科室错误
     */
    int REQUEST_SUBMIT_REMOTE_DEPART_ERROR = 7004;
    /**
     * 设置默认超时时间
     */
    int DEFAULT_TIME = 30;
    /**
     * 用户使用协议
     */
    String BASE_BASIC_USER_PROTOCOL_URL = "client/sys/protocol_by_doctor";
    /**
     * 隐私协议
     */
    String BASE_BASIC_PRIVATE_PROTOCOL_URL = "client/sys/protocol_by_privacy";
    /**
     * 关于远程会诊
     */
    String BASE_BASIC_REMOTE_URL = "https://hsp.med-value.com/remoteintroduce/";
    /**
     * banner详情
     */
    String BASE_BASIC_BANNER_URL = "client/banner/banner_desc?bannerId=";
    /**
     * 图片基础链接
     */
    String BASE_URL_FILE = "client/file/download_file?filePath=";
    /**
     * 灰度环境
     */
    String BASE_URL_PRE = "https://doctor-pre.med-value.com/";
}
