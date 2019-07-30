package com.yht.yihuantong.scheme;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.yht.frame.data.CommonData;
import com.yht.frame.data.bean.LoginBean;
import com.yht.frame.data.type.MessageType;
import com.yht.yihuantong.ui.IntentParseActivity;
import com.yht.yihuantong.ui.WebViewActivity;
import com.yht.yihuantong.ui.auth.AuthDoctorActivity;
import com.yht.yihuantong.ui.transfer.TransferInitiateDetailActivity;
import com.yht.yihuantong.ui.transfer.TransferReceiveDetailActivity;

/**
 * @author joe
 * @date 2017/12/28
 * Email: lovejjfg@gmail.com
 */
@SuppressWarnings({ "WeakerAccess", "unused" })
public class JumpUtils implements MessageType {
    private static final String TAG = "ZYC->OUTER";

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
    public static Intent parseIntent(Context context, String url, String title, LoginBean loginBean) {
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
            return parseSchemes(context, msgType, orderNo, loginBean);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Intent parseHttp(Context context, String url, String title) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(CommonData.KEY_PUBLIC, url);
        intent.putExtra(CommonData.KEY_TITLE, title);
        return intent;
    }

    @Nullable
    private static Intent parseSchemes(Context context, String msgType, String orderNo, LoginBean loginBean) {
        Intent intent;
        if (!TextUtils.isEmpty(msgType) && loginBean != null) {
            switch (msgType) {
                case MESSAGE_TRANSFER_APPLY:
                case MESSAGE_TRANSFER_CANCEL:
                case MESSAGE_TRANSFER_SYSTEM_CANCEL_R:
                    intent = new Intent(context, TransferReceiveDetailActivity.class);
                    intent.putExtra(CommonData.KEY_ORDER_ID, orderNo);
                    intent.putExtra(CommonData.KEY_IS_OUTER_CHAIN, true);
                    return intent;
                case MESSAGE_TRANSFER_UPDATE:
                case MESSAGE_TRANSFER_REJECT:
                case MESSAGE_TRANSFER_RECEIVED:
                case MESSAGE_TRANSFER_OTHER:
                case MESSAGE_TRANSFER_SYSTEM_CANCEL_T:
                    intent = new Intent(context, TransferInitiateDetailActivity.class);
                    intent.putExtra(CommonData.KEY_ORDER_ID, orderNo);
                    intent.putExtra(CommonData.KEY_IS_OUTER_CHAIN, true);
                    return intent;
                case MESSAGE_DOCTOR_AUTH_SUCCESS:
                case MESSAGE_DOCTOR_AUTH_FAILED:
                    intent = new Intent(context, AuthDoctorActivity.class);
                    return intent;
                default:
                    break;
            }
        }
        //默认启动APP
        intent = context.getPackageManager().getLaunchIntentForPackage(IntentParseActivity.PACKAGE_NAME);
        return intent;
    }
}
