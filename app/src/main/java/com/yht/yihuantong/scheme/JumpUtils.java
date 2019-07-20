package com.yht.yihuantong.scheme;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.yht.frame.utils.HuiZhenLog;
import com.yht.yihuantong.ui.SplashActivity;
import com.yht.yihuantong.ui.check.ServiceDetailActivity;
import com.yht.yihuantong.ui.main.MainActivity;
import com.yht.yihuantong.ui.personal.SettingActivity;
import com.yht.yihuantong.ui.transfer.TransferInitiateDetailActivity;

/**
 *
 * @author joe
 * @date 2017/12/28
 * Email: lovejjfg@gmail.com
 */
@SuppressWarnings({ "WeakerAccess", "unused" })
public class JumpUtils {
    private static final String TAG = JumpUtils.class.getSimpleName();

    public static void jumpHome(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    public static void jumpWeb(Context context, String url) {
        Intent intent = new Intent(context, SettingActivity.class);
        intent.putExtra(Constants.URL, url);
        context.startActivity(intent);
    }

    public static void jumpDefaultWeb(Context context, String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(intent);
    }

    public static void jumpTest1(Context context) {
        Intent intent = new Intent(context, SettingActivity.class);
        intent.putExtra(Constants.TITLE, "首页点击跳转1！");
        context.startActivity(intent);
    }

    public static void jumpTest2(Context context) {
        Intent intent = new Intent(context, SettingActivity.class);
        intent.putExtra(Constants.TITLE, "首页点击跳转2！");
        context.startActivity(intent);
    }

    public static boolean isKnownSchemes(String url) {
        return !TextUtils.isEmpty(url) && (isYiEr(url) || isHttp(url));
    }

    private static boolean isYiEr(String url) {
        return url.startsWith(Constants.KNOWN_SCHEME);
    }

    @Nullable
    public static Intent parseIntent(Context context, String url, String title) {
        if (!isKnownSchemes(url)) {
            return null;
        }
        if (isHttp(url)) {
            return parseHttp(context, url, title);
        }
        try {
            Uri data = Uri.parse(url);
            String scheme = data.getScheme();
            String msgType = data.getQueryParameter("msgType");
            HuiZhenLog.i(TAG, "scheme: " + scheme);
            HuiZhenLog.i(TAG, "msgType: " + msgType);
            return parseSchemes(context, msgType);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static boolean isHttp(String url) {
        return url.startsWith(Constants.HTTP_SCHEME) || url.startsWith(Constants.HTTPS_SCHEME);
    }

    public static boolean parseIntentAndJump(Context context, String url, String title) {
        Intent intent = parseIntent(context, url, title);
        if (intent != null) {
            context.startActivity(intent);
            return true;
        }
        return false;
    }

    private static Intent parseHttp(Context context, String url, String title) {
        Intent intent = new Intent(context, SettingActivity.class);
        intent.putExtra(Constants.URL, url);
        intent.putExtra(Constants.TITLE, title);
        return intent;
    }

    @Nullable
    private static Intent parseSchemes(Context context, String msgType) {
        Intent intent;
        switch (msgType) {
            case "1":
                intent = new Intent(context, TransferInitiateDetailActivity.class);
                return intent;
            case "2":
                intent = new Intent(context, ServiceDetailActivity.class);
                return intent;
            default:
                break;
        }
        intent = new Intent(context, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }
}
