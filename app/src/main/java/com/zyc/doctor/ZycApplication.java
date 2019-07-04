package com.zyc.doctor;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.domain.EaseAvatarOptions;
import com.hyphenate.easeui.domain.EaseUser;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.smtt.sdk.QbSdk;
import com.yanzhenjie.nohttp.NoHttp;
import com.yht.frame.api.ApiManager;
import com.yht.frame.api.CrashHandler;
import com.yht.frame.data.CommonData;
import com.yht.frame.data.base.LoginBean;
import com.yht.frame.http.retrofit.RetrofitManager;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.utils.LogUtils;
import com.yht.frame.utils.SharePreferenceUtil;
import com.zyc.doctor.chat.HxHelper;

import org.litepal.LitePal;
import org.litepal.LitePalApplication;

import me.jessyan.autosize.AutoSize;
import me.jessyan.autosize.AutoSizeConfig;
import me.jessyan.autosize.unit.Subunits;

/**
 * 2018年4月4日16:40:23
 *
 * @author DUNDUN
 */
public class ZycApplication extends LitePalApplication {
    private static ZycApplication instance;
    private LoginBean loginSuccessBean;
    /**
     * 微信api
     */
    public static IWXAPI iwxapi;

    @Override
    public void onCreate() {
        MultiDex.install(this);
        super.onCreate();
        instance = this;
        // 界面适配
        initAndroidAutoSize();
        //app 帮助类
        ApiManager.getInstance().init(this, true);
        //网络
        RetrofitManager.getInstance().init(BuildConfig.BASE_BASIC_URL);
        //启动预加载的服务
        initX5();
        //环信
        initEase();
        //数据库
        LitePal.initialize(this);
        //日志捕捉
        CrashHandler.init(this);
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
        EaseUI.getInstance().init(this, options);
        //设置头像为圆形
        EaseAvatarOptions avatarOpts = new EaseAvatarOptions();
        //0：默认，1：圆形，2：矩形
        avatarOpts.setAvatarShape(2);
        avatarOpts.setAvatarRadius(BaseUtils.dp2px(this, 4));
        EaseUI.getInstance().setAvatarOptions(avatarOpts);
        //设置有关环信自定义的相关配置  titlebar、头像、名字处理
        HxHelper.Opts opts = new HxHelper.Opts();
        opts.setShowChatTitle(false);
        HxHelper.getInstance().init(this);
        EaseUI.getInstance().setUserProfileProvider((username, callback) -> {
            LoginBean bean = getLoginSuccessBean();
            //如果是当前用户，就设置自己的昵称和头像
            if (null != bean && TextUtils.equals(bean.getDoctorCode(), username)) {
                EaseUser eu = new EaseUser(username);
                eu.setNickname(bean.getDoctorName());
                eu.setAvatar(bean.getPhoto());
                callback.onSuccess(eu);
                return eu;
            }
            //否则交给HxHelper处理，从消息中获取昵称和头像
            return HxHelper.getInstance().getUser(username, callback);
        });
    }

    private void initX5() {
        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
            @Override
            public void onViewInitFinished(boolean arg0) {
                // TODO Auto-generated method stub
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                LogUtils.d("test", " onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
                // TODO Auto-generated method stub
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(), cb);
        //        HashMap<String, Object> map = new HashMap<>(16);
        //        map.put(TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER, true);
        //        QbSdk.initTbsSettings(map);
        //        Intent intent = new Intent(this, PreLoadX5Service.class);
        //        startService(intent);
    }

    public LoginBean getLoginSuccessBean() {
        String userStr = (String)SharePreferenceUtil.getObject(this, CommonData.KEY_LOGIN_SUCCESS_BEAN, "");
        if (!TextUtils.isEmpty(userStr)) {
            loginSuccessBean = new Gson().fromJson(userStr, LoginBean.class);
        }
        return loginSuccessBean;
    }

    public void setLoginSuccessBean(LoginBean loginSuccessBean) {
        this.loginSuccessBean = loginSuccessBean;
        SharePreferenceUtil.putObject(this, CommonData.KEY_LOGIN_SUCCESS_BEAN, loginSuccessBean);
    }

    /**
     * 清楚登录数据
     */
    public void clearLoginSuccessBean() {
        SharePreferenceUtil.remove(this, CommonData.KEY_LOGIN_SUCCESS_BEAN);
    }

    /**
     * 界面适配
     */
    private void initAndroidAutoSize() {
        //当 App 中出现多进程, 并且您需要适配所有的进程, 就需要在 App 初始化时调用 initCompatMultiProcess()
        //在 Demo 中跳转的三方库中的 DefaultErrorActivity 就是在另外一个进程中, 所以要想适配这个 Activity 就需要调用
        // initCompatMultiProcess()
        AutoSize.initCompatMultiProcess(this);
        AutoSizeConfig.getInstance().getUnitsManager().setSupportSP(false).setSupportSubunits(Subunits.MM);
        AutoSizeConfig.getInstance()
                      //是否让框架支持自定义 Fragment 的适配参数, 由于这个需求是比较少见的, 所以须要使用者手动开启
                      //如果没有这个需求建议不开启
                      .setCustomFragment(true)
                      //是否屏蔽系统字体大小对 AndroidAutoSize 的影响, 如果为 true, App 内的字体的大小将不会跟随系统设置中字体大小的改变
                      //如果为 false, 则会跟随系统设置中字体大小的改变, 默认为 false
                      .setExcludeFontScale(true);
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
