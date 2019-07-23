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
     * 微信未绑定
     */
    int REQUEST_WE_CHAT_BIND_ERROR = 4004;
    /**
     * 设置默认超时时间
     */
    int DEFAULT_TIME = 30;
    /**
     * 用户使用协议
     */
    String BASE_BASIC_USER_PROTOCOL_URL = "/client/sys/protocol_by_doctor";
    /**
     * 图片基础链接
     */
    String BASE_URL_FILE = "/client/file/download_file?filePath=";
}
