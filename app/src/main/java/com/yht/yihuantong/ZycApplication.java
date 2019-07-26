package com.yht.yihuantong;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.domain.EaseAvatarOptions;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.smtt.sdk.QbSdk;
import com.yanzhenjie.nohttp.NoHttp;
import com.yht.frame.api.ApiManager;
import com.yht.frame.api.CrashHandler;
import com.yht.frame.data.CommonData;
import com.yht.frame.data.bean.LoginBean;
import com.yht.frame.http.retrofit.RetrofitManager;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.utils.HuiZhenLog;
import com.yht.frame.utils.SharePreferenceUtil;
import com.yht.frame.widgets.imagePreview.ImageLoadUtil;
import com.yht.yihuantong.chat.HxHelper;

import org.litepal.LitePal;
import org.litepal.LitePalApplication;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import cn.jpush.android.api.JPushInterface;

/**
 * 2018年4月4日16:40:23
 *
 * @author DUNDUN
 */
public class ZycApplication extends LitePalApplication {
    private static ZycApplication instance;
    private LoginBean loginBean;
    /**
     * 微信api
     */
    public static IWXAPI iwxapi;
    /**
     * 当前正在聊天的id
     */
    private String chatId;
    /**
     * 服务、转诊权限
     */
    private boolean transferAble, serviceAble;
    /**
     * 调试模式
     */
    private final boolean debugMode = true;

    @Override
    public void onCreate() {
        MultiDex.install(this);
        super.onCreate();
        instance = this;
        registerActivityLifecycleCallbacks(new LifecycleHandler());
        //app 帮助类
        ApiManager.getInstance().init(this, debugMode);
        //网络
        RetrofitManager.getInstance().init(BuildConfig.BASE_BASIC_URL);
        //日志捕捉
        CrashHandler.init(this);
        //启动预加载的服务
        initX5();
        //腾讯buggly
        initBuggly();
        //环信
        initEase();
        //极光
        initJPush();
        //图片预览
        initImageLoader();
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

    /**
     * ImageLoader 参数化配置
     */
    private void initImageLoader() {
        ImageLoadUtil.getInstance().initImageLoader(getApplicationContext());
    }

    private void initX5() {
        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
            @Override
            public void onViewInitFinished(boolean arg0) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                HuiZhenLog.i("ZYC", "qbsdk:" + arg0);
            }

            @Override
            public void onCoreInitFinished() {
            }
        };
        //x5内核初始化接口 预加载
        QbSdk.initX5Environment(getApplicationContext(), cb);
    }

    private void initBuggly() {
        Context context = getApplicationContext();
        // 获取当前包名
        String packageName = context.getPackageName();
        // 获取当前进程名
        String processName = getProcessName(android.os.Process.myPid());
        // 设置是否为上报进程
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
        strategy.setUploadProcess(processName == null || processName.equals(packageName));
        // 初始化Bugly
        CrashReport.initCrashReport(context, "43524227c9", debugMode, strategy);
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

    /**
     * 清楚登录数据
     */
    public void clearLoginSuccessBean() {
        SharePreferenceUtil.remove(this, CommonData.KEY_LOGIN_BEAN);
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
    //    /**
    //     * 界面适配
    //     */
    //    private void initAndroidAutoSize() {
    //        //当 App 中出现多进程, 并且您需要适配所有的进程, 就需要在 App 初始化时调用 initCompatMultiProcess()
    //        //在 Demo 中跳转的三方库中的 DefaultErrorActivity 就是在另外一个进程中, 所以要想适配这个 Activity 就需要调用
    //        // initCompatMultiProcess()
    //        AutoSize.initCompatMultiProcess(this);
    //        AutoSizeConfig.getInstance().getUnitsManager().setSupportSP(false).setSupportSubunits(Subunits.MM);
    //        AutoSizeConfig.getInstance()
    //                      //是否让框架支持自定义 Fragment 的适配参数, 由于这个需求是比较少见的, 所以须要使用者手动开启
    //                      //如果没有这个需求建议不开启
    //                      .setCustomFragment(true)
    //                      //是否屏蔽系统字体大小对 AndroidAutoSize 的影响, 如果为 true, App 内的字体的大小将不会跟随系统设置中字体大小的改变
    //                      //如果为 false, 则会跟随系统设置中字体大小的改变, 默认为 false
    //                      .setExcludeFontScale(true);
    //    }

    public static ZycApplication getInstance() {
        return instance;
    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        }
        catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            }
            catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
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
