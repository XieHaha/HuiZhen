package com.yht.yihuantong;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.text.TextUtils;

import androidx.multidex.MultiDex;

import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.domain.EaseAvatarOptions;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.smtt.sdk.QbSdk;
import com.umeng.commonsdk.UMConfigure;
import com.yanzhenjie.nohttp.NoHttp;
import com.yht.frame.api.ApiManager;
import com.yht.frame.api.CrashHandler;
import com.yht.frame.data.CommonData;
import com.yht.frame.data.bean.LoginBean;
import com.yht.frame.http.retrofit.RetrofitManager;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.utils.SharePreferenceUtil;
import com.yht.yihuantong.chat.HxHelper;

import org.litepal.LitePal;
import org.litepal.LitePalApplication;

import java.util.ArrayList;

import cn.jpush.android.api.JPushInterface;

/**
 * 2018年4月4日16:40:23
 *
 * @author DUNDUN
 */
public class ZycApplication extends LitePalApplication {
    private static ZycApplication instance;
    private LoginBean loginBean;
    private boolean loginStatus = false;
    /**
     * 微信api
     */
    public static IWXAPI iwxapi;
    /**
     * 当前正在聊天的id
     */
    private String chatId;
    /**
     * 服务、转诊、远程权限
     */
    private boolean transferAble, serviceAble, remoteAble;
    /**
     * 预约服务已选服务项code
     */
    private ArrayList<String> selectCodes = new ArrayList<>();
    /**
     * 调试模式
     * 1、微信登录
     * 2、自定义URL
     * 3、自动切换环境（如果线上版本比当前版本更低，就切换到灰度环境）
     */
    public final boolean debugMode = true;
    /**
     * baseUrl
     */
    private String baseUrl;

    @Override
    public void onCreate() {
        MultiDex.install(this);
        super.onCreate();
        instance = this;
        setBaseUrl(BuildConfig.BASE_BASIC_URL);
        registerActivityLifecycleCallbacks(new LifecycleHandler());
        //app 帮助类
        ApiManager.getInstance().init(this, debugMode);
        //网络
        RetrofitManager.getInstance().init(BuildConfig.BASE_BASIC_URL);
        //日志捕捉
        CrashHandler.init(this);
        //启动预加载的服务
        initX5();
        //友盟
        initUMeng();
        //环信
        initEase();
        //极光
        initJPush();
        //数据库
        LitePal.initialize(this);
        NoHttp.initialize(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    /**
     * 环信初始化
     */
    private void initEase() {
        //环信初始化
        EMOptions options = new EMOptions();
        // 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);
        // 是否自动将消息附件上传到环信服务器，默认为True是使用环信服务器上传下载，如果设为 false，需要开发者自己处理附件消息的上传和下载
        options.setAutoTransferMessageAttachments(true);
        // 是否自动下载附件类消息的缩略图等，默认为 true 这里和上边这个参数相关联
        options.setAutoDownloadThumbnail(true);
        EaseUI.getInstance().init(this, options);
        //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(debugMode);
        //设置头像为圆形
        EaseAvatarOptions avatarOpts = new EaseAvatarOptions();
        //0：默认，1：圆形，2：矩形
        avatarOpts.setAvatarShape(2);
        avatarOpts.setAvatarRadius(BaseUtils.dp2px(this, 4));
        EaseUI.getInstance().setAvatarOptions(avatarOpts);
        //设置有关环信自定义的相关配置  titlebar、头像、名字处理
        HxHelper.Opts opts = new HxHelper.Opts();
        opts.setShowChatTitle(false);
    }

    /**
     * 极光推送 初始化
     */
    private void initJPush() {
        //极光推送
        JPushInterface.setDebugMode(debugMode);
        JPushInterface.init(this);
    }

    private void initX5() {
        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
            @Override
            public void onViewInitFinished(boolean arg0) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
            }

            @Override
            public void onCoreInitFinished() {
            }
        };
        //x5内核初始化接口 预加载
        QbSdk.initX5Environment(getApplicationContext(), cb);
    }

    /**
     * 初始化common库
     * 参数1:上下文，不能为空
     * 参数2:设备类型，UMConfigure.DEVICE_TYPE_PHONE为手机、UMConfigure.DEVICE_TYPE_BOX为盒子，默认为手机
     * 参数3:Push推送业务的secret
     */
    private void initUMeng() {
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, null);
    }

    public LoginBean getLoginBean() {
        String userStr = (String)SharePreferenceUtil.getObject(this, CommonData.KEY_LOGIN_BEAN, "");
        if (!TextUtils.isEmpty(userStr)) {
            loginBean = new Gson().fromJson(userStr, LoginBean.class);
        }
        return loginBean;
    }

    public void setLoginBean(LoginBean loginBean) {
        this.loginBean = loginBean;
        SharePreferenceUtil.putObject(this, CommonData.KEY_LOGIN_BEAN, loginBean);
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public boolean isTransferAble() {
        return transferAble;
    }

    public void setTransferAble(boolean transferAble) {
        this.transferAble = transferAble;
    }

    public boolean isServiceAble() {
        return serviceAble;
    }

    public void setServiceAble(boolean serviceAble) {
        this.serviceAble = serviceAble;
    }

    public boolean isRemoteAble() {
        return remoteAble;
    }

    public void setRemoteAble(boolean remoteAble) {
        this.remoteAble = remoteAble;
    }

    public boolean isLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(boolean loginStatus) {
        this.loginStatus = loginStatus;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    /**
     * 动态更新url
     */
    public void updateBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        RetrofitManager.getInstance().updateBaseUrl(baseUrl);
    }

    public ArrayList<String> getSelectCodes() {
        return selectCodes;
    }

    public void setSelectCodes(ArrayList<String> selectCodes) {
        this.selectCodes = selectCodes;
    }

    public void clearSelectCodes() {
        if (selectCodes != null) {
            selectCodes.clear();
        }
    }

    /**
     * 界面适配
     */
    private void initAndroidAutoSize() {
        //当 App 中出现多进程, 并且您需要适配所有的进程, 就需要在 App 初始化时调用 initCompatMultiProcess()
        //在 Demo 中跳转的三方库中的 DefaultErrorActivity 就是在另外一个进程中, 所以要想适配这个 Activity 就需要调用
        // initCompatMultiProcess()
        //        AutoSize.initCompatMultiProcess(this);
        //        AutoSizeConfig.getInstance().getUnitsManager().setSupportSP(false).setSupportSubunits(Subunits.MM);
        //        AutoSizeConfig.getInstance()
        //                      //是否让框架支持自定义 Fragment 的适配参数, 由于这个需求是比较少见的, 所以须要使用者手动开启
        //                      //如果没有这个需求建议不开启
        //                      .setCustomFragment(true)
        //                      //是否屏蔽系统字体大小对 AndroidAutoSize 的影响, 如果为 true, App 内的字体的大小将不会跟随系统设置中字体大小的改变
        //                      //如果为 false, 则会跟随系统设置中字体大小的改变, 默认为 false
        //                      .setExcludeFontScale(true);
    }

    public static ZycApplication getInstance() {
        return instance;
    }

    /**
     * app字体不随系统改变而改变
     *
     * @return 字体
     */
    @Override
    public Resources getResources() {
        Resources resources = super.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.fontScale = 1;
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        return resources;
    }
}
