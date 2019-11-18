package com.yht.yihuantong.version.presenter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build;
import androidx.core.content.FileProvider;
import android.text.TextUtils;

import com.yanzhenjie.nohttp.Headers;
import com.yanzhenjie.nohttp.download.DownloadListener;
import com.yht.frame.api.DirHelper;
import com.yht.frame.data.bean.VersionBean;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.utils.HuiZhenLog;
import com.yht.frame.utils.ToastUtil;
import com.yht.yihuantong.ZycApplication;
import com.yht.yihuantong.version.ConstantsVersionMode;
import com.yht.yihuantong.version.model.VersionModel;
import com.yht.yihuantong.version.model.VersionModelListener;

import java.io.File;

/**
 * @author dundun
 */
public class VersionPresenter implements ConstantsVersionMode {
    private static final String TAG = "ZYC";
    private Context context;
    private VersionModel versionModel;
    private VersionBean nowVersion;
    private String url = null;
    private String token;
    /**
     * 文件大小
     */
    private long fileSize;

    public VersionPresenter(Context context, String token) {
        this.context = context;
        this.token = token;
    }

    public void init() {
        versionModel = new VersionModel(context, token);
        updateVersionByNetwork();
    }

    /**
     * 根据网络情况判断是否检查更新
     * 断网时不检查更新，启动网络监听广播
     */
    private void updateVersionByNetwork() {
        if (BaseUtils.isNetworkAvailable(context)) {
            versionModel.getNewestVersion(new VersionModelListener.NewestVersionCallBack() {
                @Override
                public void result(VersionBean version) {
                    if (version == null) { return; }
                    nowVersion = version;
                    url = nowVersion.getDownloadUrl();
                    checkVersion();
                }

                @Override
                public void error(String s) {
                    //                    if (!TextUtils.isEmpty(s)) { ToastUtil.toast(context, s); }
                }
            });
        }
        else {
            if (versionViewListener != null) {
                versionViewListener.updateNetWorkError();
            }
        }
    }

    /**
     * 下载apk
     *
     * @param isMustUpdate 判断是否是强制更新
     */
    public void getNewAPK(final boolean isMustUpdate) {
        if (versionModel != null) {
            versionModel.downloadAPK(url, new DownloadListener() {
                @Override
                public void onDownloadError(int what, Exception exception) {
                    ToastUtil.toast(context, exception.getMessage());
                }

                @Override
                public void onStart(int what, boolean isResume, long rangeSize, Headers responseHeaders,
                        long allCount) {
                    fileSize = allCount;
                }

                @Override
                public void onProgress(int what, int progress, long fileCount, long speed) {
                    if (isMustUpdate && versionViewListener != null) {
                        versionViewListener.updateLoading(fileSize, fileCount);
                    }
                }

                @Override
                public void onFinish(int what, String filePath) {
                    File file = new File(filePath);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    Uri uri;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        uri = FileProvider.getUriForFile(context, ZycApplication.getInstance().getPackageName() +
                                                                  ".fileprovider", file);
                    }
                    else {
                        uri = Uri.fromFile(file);
                    }
                    intent.setDataAndType(uri, "application/vnd.android.package-archive");
                    context.startActivity(intent);
                }

                @Override
                public void onCancel(int what) {
                }
            });
        }
    }

    /**
     * 版本检测更新
     */
    private void checkVersion() {
        //当前正在使用的版本
        String currentVersionName = getVersionName();
        //获取最新版本
        String newestVersionName = nowVersion.getVersion();
        //获取最低支持版本
        String lowestVersionName = nowVersion.getMinVersion();
        int mode = UPDATE_NONE;
        boolean isDownAPK = true;
        //小于0  表示获取的新版本号大于当前使用的版本,需要更新
        int updateTag = compareVersion(currentVersionName, newestVersionName);
        if (updateTag < 0) {
            //小于0 表示强制更新
            if (compareVersion(currentVersionName, lowestVersionName) < 0) {
                mode = UPDATE_MUST;
            }
            else {//大于等于0 用户选择更新
                mode = UPDATE_CHOICE;
            }
            //判断本地apk文件的版本号是否最新
            String apkVersionName = getApkVersion(DirHelper.getPathFile() + "/ZYC.apk");
            if (apkVersionName != null && compareVersion(newestVersionName, apkVersionName) == 0) {
                isDownAPK = false;
            }
        }
        else {
            mode = updateTag;
        }
        if (versionViewListener != null) {
            versionViewListener.updateVersion(nowVersion, mode, isDownAPK);
        }
    }

    private VersionViewListener versionViewListener;

    public interface VersionViewListener {
        /**
         * 版本是否更新回调
         *
         * @param version       新版本数据
         * @param mode          更新模式
         * @param isDownLoading 是否显示下载
         */
        void updateVersion(VersionBean version, int mode, boolean isDownLoading);

        /**
         * 更新进度
         *
         * @param total   总大小
         * @param current 当前
         */
        void updateLoading(long total, long current);

        /**
         * 网络错误
         */
        void updateNetWorkError();
    }

    public void setVersionViewListener(VersionViewListener versionViewListener) {
        this.versionViewListener = versionViewListener;
    }

    /**
     * 获取当前应用的版本号
     */
    private String getVersionName() {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName + "";
        }
        catch (NameNotFoundException e) {
            HuiZhenLog.w(TAG, "Exception error!", e);
        }
        return null;
    }

    /**
     * 获取apk包的信息：版本号
     *
     * @param absPath apk包的绝对路径
     */
    private String getApkVersion(String absPath) {
        if (new File(absPath).exists()) {
            PackageManager pm = context.getPackageManager();
            PackageInfo pkgInfo = pm.getPackageArchiveInfo(absPath, PackageManager.GET_ACTIVITIES);
            if (pkgInfo != null) {
                ApplicationInfo appInfo = pkgInfo.applicationInfo;
                /* 必须加这两句，不然下面icon获取是default icon而不是应用包的icon */
                appInfo.sourceDir = absPath;
                appInfo.publicSourceDir = absPath;
                // 得到版本信息
                return pkgInfo.versionName;
            }
        }
        return null;
    }

    /**
     * 版本号比较
     *
     * @return 0代表相等，1代表version1大于version2，-1代表version1小于version2
     */
    private int compareVersion(String version1, String version2) {
        //当前版本为空，直接提示更新
        if (TextUtils.isEmpty(version1)) {
            return -1;
        }
        if (TextUtils.isEmpty(version2) || version1.equals(version2)) {
            return 0;
        }
        String[] version1Array = version1.split("\\.");
        String[] version2Array = version2.split("\\.");
        int index = 0;
        // 获取最小长度值
        int minLen = Math.min(version1Array.length, version2Array.length);
        int diff = 0;
        // 循环判断每位的大小
        while (index < minLen &&
               (diff = Integer.parseInt(version1Array[index]) - Integer.parseInt(version2Array[index])) == 0) {
            index++;
        }
        if (diff == 0) {
            // 如果位数不一致，比较多余位数
            for (int i = index; i < version1Array.length; i++) {
                if (Integer.parseInt(version1Array[i]) > 0) {
                    return 1;
                }
            }
            for (int i = index; i < version2Array.length; i++) {
                if (Integer.parseInt(version2Array[i]) > 0) {
                    return -1;
                }
            }
            return 0;
        }
        else {
            return diff > 0 ? 1 : -1;
        }
    }
}
