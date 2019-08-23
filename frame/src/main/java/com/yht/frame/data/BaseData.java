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
     * 男
     */
    int BASE_MALE = 1;
    /**
     * 女
     */
    int BASE_FEMALE = 2;
    /**
     * 列表单页请求数据
     */
    int BASE_PAGE_DATA_NUM = 20;
    /**
     * 昵称的最大长度
     */
    int BASE_NICK_NAME_LENGTH = 30;
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
    int BASE_MEAASGE_DISPLAY_NUM = 9999;
    /**
     * 消息最大显示数字
     */
    int BASE_MEAASGE_DISPLAY_MIDDLE_NUM = 99;
    /**
     * 屏幕适配  默认宽度
     */
    int BASE_DEVICE_DEFAULT_WIDTH = 667;
    /**
     * 单个患者拥有的最大标签数
     */
    int BASE_PATIENT_LABEL_NUM = 10;
    /**
     * 单个医生拥有的最大标签数
     */
    int BASE_DOCTOR_LABEL_NUM = 50;
    /**
     * 搜索图标 标志位
     */
    String BASE_SEARCH_TAG = "#";
    /**
     * 医生 标志位
     */
    String BASE_DOCTOR_TAG = "1";
    /**
     * 患者 标志位
     */
    String BASE_PATIENT_TAG = "2";
    /**
     * 医生code 第一个字母
     */
    String BASE_DOCTOR_CODE = "D";
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
     * 登出广播
     */
    String BASE_SIGN_OUT_ACTION = "zyc.doctor.logout.action";
    /**
     * token失效
     */
    String BASE_TOKEN_ERROR_ACTION = "zyc.doctor.token.action";
    /**
     * 环信消息通知点击action
     */
    String EASE_MSG_ANDROID_INTENT_CLICK = "ease.msg.android.intent.CLICK";
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
    /**
     * requestCode
     */
    int BASE_PENDING_COUNT = 10;
    /**
     * 极光推送渠道
     */
    String BASE_PUSH_CHANNEL = "d_base_push_channel";
    /**
     * 聊天
     */
    String BASE_CHAT_CHANNEL = "d_base_chat_channel";
    /**
     * 发起远程会诊
     */
    String BASE_REMOTE = "remoteStart";
    /**
     * 发起远程会诊意见
     */
    String BASE_REMOTE_ADVICE = "remoteResult";
}
