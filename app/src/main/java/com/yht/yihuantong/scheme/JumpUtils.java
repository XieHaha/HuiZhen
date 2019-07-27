package com.yht.yihuantong.scheme;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.yht.frame.data.CommonData;
import com.yht.yihuantong.ui.SplashActivity;
import com.yht.yihuantong.ui.auth.AuthDoctorActivity;
import com.yht.yihuantong.ui.check.ServiceDetailActivity;
import com.yht.yihuantong.ui.personal.SettingActivity;
import com.yht.yihuantong.ui.transfer.TransferInitiateDetailActivity;
import com.yht.yihuantong.ui.transfer.TransferReceiveDetailActivity;

/**
 * @author joe
 * @date 2017/12/28
 * Email: lovejjfg@gmail.com
 */
@SuppressWarnings({ "WeakerAccess", "unused" })
public class JumpUtils {
    private static final String TAG = JumpUtils.class.getSimpleName();

    public static boolean isKnownSchemes(String url) {
        return !TextUtils.isEmpty(url) && (isYiEr(url) || isHttp(url));
    }

    private static boolean isYiEr(String url) {
        return url.startsWith(Constants.KNOWN_SCHEME);
    }

    private static boolean isHttp(String url) {
        return url.startsWith(Constants.HTTP_SCHEME) || url.startsWith(Constants.HTTPS_SCHEME);
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
            String host = data.getHost();
            String msgType = data.getQueryParameter("msgType");
            String orderNo = data.getQueryParameter("orderNo");
            return parseSchemes(context, msgType, orderNo);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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
    private static Intent parseSchemes(Context context, String msgType, String orderNo) {
        Intent intent;
        switch (msgType) {
            case "0":
                intent = new Intent(context, TransferReceiveDetailActivity.class);
                intent.putExtra(CommonData.KEY_ORDER_ID, orderNo);
                return intent;
            case "1":
                intent = new Intent(context, TransferInitiateDetailActivity.class);
                intent.putExtra(CommonData.KEY_ORDER_ID, orderNo);
                return intent;
            case "2":
                intent = new Intent(context, ServiceDetailActivity.class);
                intent.putExtra(CommonData.KEY_ORDER_ID, orderNo);
                return intent;
            case "3":
                intent = new Intent(context, AuthDoctorActivity.class);
                return intent;
            default:
                break;
        }
        intent = new Intent(context, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }
}
