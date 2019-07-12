package com.yht.yihuantong.utils;

import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.yht.frame.data.BaseNetConfig;
import com.yht.yihuantong.BuildConfig;
import com.yht.yihuantong.ZycApplication;

/**
 * @author 顿顿
 * @date 19/7/5 16:52
 * @des 文件下载链接拼接
 */
public class FileUrlUtil {
    /**
     * @param url 图片全链接
     * @return
     */
    public static String append(String url) {
        StringBuilder builder = new StringBuilder();
        builder.append(BuildConfig.BASE_BASIC_URL);
        builder.append(BaseNetConfig.BASE_URL_FILE);
        builder.append(url);
        return builder.toString();
    }

    /**
     * 添加header token
     *
     * @param url
     * @return
     */
    public static GlideUrl addTokenToUrl(String url) {
        GlideUrl glideUrl = new GlideUrl(append(url), new LazyHeaders.Builder().addHeader("token",
                                                                                          ZycApplication.getInstance()
                                                                                                        .getLoginSuccessBean()
                                                                                                        .getToken())
                                                                               .build());
        return glideUrl;
    }
}
