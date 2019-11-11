package com.yht.yihuantong.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.NotificationManagerCompat;

import static android.app.Notification.EXTRA_CHANNEL_ID;
import static android.provider.Settings.EXTRA_APP_PACKAGE;

/**
 * @author 顿顿
 * @date 19/7/25 17:07
 * @description 跳转设置界面
 */
public class NotifySettingUtils {
    /**
     * 检测通知权限
     */
    public static boolean hasNotify(Activity activity) {
        NotificationManagerCompat manager = NotificationManagerCompat.from(activity.getApplicationContext());
        return manager.areNotificationsEnabled();
    }

    /**
     * 设置界面
     */
    public static void openNotifySetting(Activity activity) {
        try {
            // 根据isOpened结果，判断是否需要提醒用户跳转AppInfo页面，去打开App通知权限
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            //这种方案适用于 API 26, 即8.0（含8.0）以上可以用
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                intent.putExtra(EXTRA_CHANNEL_ID, activity.getApplicationInfo().uid);
                intent.putExtra(EXTRA_APP_PACKAGE, activity.getPackageName());
            }
            else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //这种方案适用于 API21——25，即 5.0——7.1 之间的版本可以使用
                intent.putExtra("app_package", activity.getPackageName());
                intent.putExtra("app_uid", activity.getApplicationInfo().uid);
            }
            activity.startActivity(intent);
        }
        catch (Exception e) {
            e.printStackTrace();
            // 出现异常则跳转到应用设置界面：锤子坚果3——OC105 API25
            Intent intent = new Intent();
            //下面这种方案是直接跳转到当前应用的设置界面。
            //https://blog.csdn.net/ysy950803/article/details/71910806
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
            intent.setData(uri);
            activity.startActivity(intent);
        }
    }
}
