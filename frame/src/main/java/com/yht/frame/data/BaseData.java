package com.yht.frame.data;

/**
 * @author 顿顿
 * @date 19/4/11 16:45
 * @des 魔法值存储
 */
public interface BaseData {
    /**
     * 基础type 0
     */
    int BASE_ZERO = 0;
    /**
     * 基础type 1
     */
    int BASE_ONE = 1;
    /**
     * 基础type 2
     */
    int BASE_TWO = 2;
    /**
     * 昵称的最大长度
     */
    int BASE_NICK_NAME_LENGTH = 20;
    /**
     * 身份证号最大长度
     */
    int BASE_ID_CARD_LENGTH = 18;
    /**
     * 手机号默认长度
     */
    int BASE_PHONE_DEFAULT_LENGTH = 11;
    /**
     * 验证码默认长度
     */
    int BASE_VERIFY_CODE_DEFAULT_LENGTH = 6;
    /**
     * 广告页最长等待时间
     */
    int MAX_WAIT_TIME = 5;
    /**
     * 验证码二次获取默认时间
     */
    int BASE_MAX_RESEND_TIME = 60;
    /**
     * 聊天倒计时  一天
     */
    int BASE_MAX_CHAT_TIME = 60 * 60 * 24;
    /**
     * 消息最大显示数字
     */
    int BASE_MEAASGE_DISPLAY_NUM = 99;
    /**
     * 环信默认登录密码
     */
    String BASE_EASE_DEFAULT_PWD = "111111";
    /**
     * 计时器  开始
     */
    String BASE_START_TIMER_ACTION = "zyc.doctor.start.timer";
    /**
     * 适配华为  裁剪
     */
    String BASE_HONOR_NAME = "HONOR";
    /**
     * 适配华为  裁剪
     */
    String BASE_HUAWEI_NAME = "HUAWEI";
    /**
     * 图片类型
     */
    String BASE_IMAGE_TYPE = "image/bmp&&image/gif&&image/jpeg&&image/png";
    /**
     * 微信登录APP_ID
     */
    String WE_CHAT_ID = "wx06ffa935bd113f48";
    /**
     * 微信登录APP_SECRET
     */
    String WE_CHAT_SECRET = "c7299dcce25a5ff0487289370ba65dc0";
    /**
     * SCOPE
     */
    String WE_CHAT_SCOPE = "snsapi_userinfo";
    /**
     * state
     */
    String WE_CHAT_STATE = "huizhen_wechat_login";
    /**
     * 接口固定参数
     */
    String ADMIN = "ANDROID";
}
