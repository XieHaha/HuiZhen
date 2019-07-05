package com.yht.yihuantong.utils.glide;

import com.yht.frame.data.BaseNetConfig;
import com.yht.yihuantong.BuildConfig;

/**
 * @author 顿顿
 * @date 19/7/5 16:52
 * @des
 */
public class ImageUrlUtil {
    /**
     * @param url 图片全链接
     * @return
     */
    public static String append(String url) {
        StringBuilder builder = new StringBuilder();
        builder.append(BuildConfig.BASE_BASIC_URL);
        builder.append(BaseNetConfig.BASE_URL_IMAGE);
        builder.append(url);
        return builder.toString();
    }
}
