package com.yht.frame.api.notify;

/**
 * 数据变化接口
 *
 * @author dundun
 */
public interface IChange<T> {
    /**
     * 数据更新
     *
     * @param data
     */
    void onChange(T data);
}
