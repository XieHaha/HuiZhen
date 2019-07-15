package com.yht.yihuantong.utils;

import android.text.TextUtils;

import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.yht.yihuantong.ZycApplication;

/**
 * @author 顿顿
 * @date 19/7/5 16:52
 * @des 文件下载链接拼接
 */
public class FileUrlUtil {
    /**
     * 添加header token
     *
     * @param url
     * @return
     */
    public static GlideUrl addTokenToUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        GlideUrl glideUrl = new GlideUrl(url, new LazyHeaders.Builder().addHeader("token", ZycApplication.getInstance()
                                                                                                         .getLoginBean()
                                                                                                         .getToken())
                                                                       .build());
        return glideUrl;
    }
}
