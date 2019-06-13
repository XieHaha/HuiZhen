package com.zyc.doctor;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.multidex.MultiDex;

import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.yanzhenjie.nohttp.NoHttp;
import com.yht.frame.api.ApiManager;
import com.yht.frame.api.CrashHandler;
import com.yht.frame.http.retrofit.RetrofitManager;

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
     * @return
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
