package com.yht.yihuantong.version.model;

import android.content.Context;

import com.yanzhenjie.nohttp.download.DownloadListener;
import com.yht.frame.api.DirHelper;
import com.yht.frame.api.FileTransferServer;
import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.Tasks;
import com.yht.frame.data.bean.VersionBean;
import com.yht.frame.http.listener.AbstractResponseAdapter;
import com.yht.frame.http.retrofit.RequestUtils;
import com.yht.frame.utils.HuiZhenLog;

import java.io.File;

/**
 * @author dundun
 * @date 16/6/6
 */
public class VersionModel extends AbstractResponseAdapter<BaseResponse> implements VersionModelListener {
    private static final String TAG = "VersionModel";
    private Context context;
    private String token;
    private NewestVersionCallBack callBack;
    private DownloadAPKCallBack downloadAPKCallBack;

    public VersionModel(Context context, String token) {
        this.context = context;
        this.token = token;
    }

    @Override
    public void getNewestVersion(NewestVersionCallBack callBack) {
        this.callBack = callBack;
        //获取最新版本
        RequestUtils.getVersion(context, token, this);
    }

    @Override
    public void downloadAPK(String url, DownloadAPKCallBack downloadAPKCallBack) {
        this.downloadAPKCallBack = downloadAPKCallBack;
        File file = new File(DirHelper.getPathFile() + "/ZYC.apk");
        if (file.exists()) {
            if (!file.delete()) {
                HuiZhenLog.e(TAG, "delete error");
            }
        }
    }

    /**
     * 下载最新的apk
     */
    public void downloadAPK(String url, DownloadListener downloadListener) {
        File file = new File(DirHelper.getPathFile() + "/ZYC.apk");
        if (file.exists()) {
            if (!file.delete()) {
                HuiZhenLog.e(TAG, "delete error");
            }
        }
        FileTransferServer.getInstance(context)
                          .downloadFile("", url, DirHelper.getPathFile(), "ZYC" + ".apk", downloadListener);
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        if (callBack != null) {
            VersionBean bean = (VersionBean)response.getData();
            callBack.result(bean);
        }
    }

    @Override
    public void onResponseError(Tasks task, Exception e) {
        switch (task) {
            case UPDATE_VERSION:
                if (callBack != null) {
                    callBack.error(e.getMessage());
                }
                break;
            case DOWNLOAD_FILE:
                if (downloadAPKCallBack != null) {
                    downloadAPKCallBack.downloadError(e.getMessage());
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onResponseCode(Tasks task, BaseResponse response) {
        if (callBack != null) {
            callBack.error(response.getMsg());
        }
    }
}
