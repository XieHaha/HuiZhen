package com.yht.frame.http.listener;

import com.yht.frame.data.Tasks;

/**
 * @author luozi
 * @date 2015/12/29
 * 响应监听，泛型
 */
public interface ResponseListener<T> {
    /**
     * 响应成功
     *
     * @param task     tag
     * @param response 返回结果
     */
    void onResponseSuccess(Tasks task, T response);

    /**
     * 请求错误
     *
     * @param task tag
     * @param e    错误信息
     */
    void onResponseError(Tasks task, Exception e);

    /**
     * 请求错误
     *
     * @param task     tag
     * @param response 返回结果
     */
    void onResponseCode(Tasks task, T response);

    /**
     * 请求开始
     *
     * @param task tag
     */
    @Deprecated
    void onResponseStart(Tasks task);

    /**
     * 请求结束，无论请求是否成功都会回调
     *
     * @param task tag
     */
    void onResponseEnd(Tasks task);

    /**
     * 取消请求
     * 根据框架而定，暂时弃用
     *
     * @param task tag
     */
    @Deprecated
    void onResponseCancel(Tasks task);
}
