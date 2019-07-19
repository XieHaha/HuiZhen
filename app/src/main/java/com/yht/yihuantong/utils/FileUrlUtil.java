package com.yht.yihuantong.utils;

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
    public static String addTokenToUrl(String url) {
        return url;
        //        if (TextUtils.isEmpty(url)) {
        //            return null;
        //        }
        //        GlideUrl glideUrl = new GlideUrl(url, new LazyHeaders.Builder().addHeader("token", ZycApplication.getInstance()
        //                                                                                                         .getLoginBean()
        //                                                                                                         .getToken())
        //                                                                       .build());
        //        return glideUrl;
    }
}
