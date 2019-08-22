package com.yht.frame.api.notify;

import android.support.annotation.NonNull;

/**
 * @author dundun
 * @date 18/4/27
 */
public interface INotifyChangeListenerServer {
    /**
     * 患者状态监听
     *
     * @param listener     消息状态监听器
     * @param registerType 注册类型
     */
    void registerPatientListChangeListener(@NonNull IChange<String> listener, @NonNull RegisterType registerType);

    /**
     * 医生状态监听
     *
     * @param listener     消息状态监听器
     * @param registerType 注册类型
     */
    void registerDoctorListChangeListener(@NonNull IChange<String> listener, @NonNull RegisterType registerType);

    /**
     * 服务协议更新
     *
     * @param listener     接收消息监听器
     * @param registerType 注册类型
     */
    void registerProtocolChangeListener(@NonNull IChange<String> listener, @NonNull RegisterType registerType);

    /**
     * 医生转诊申请监听
     *
     * @param listener     接收消息监听器
     * @param registerType 注册类型
     */
    void registerDoctorTransferPatientListener(@NonNull IChange<String> listener, @NonNull RegisterType registerType);

    /**
     * 医生认证状态监听
     *
     * @param listener     接收消息监听器
     * @param registerType 注册类型
     */
    void registerDoctorAuthStatusChangeListener(@NonNull IChange<Integer> listener, @NonNull RegisterType registerType);

    /**
     * 消息红点
     *
     * @param listener     接收消息监听器
     * @param registerType 注册类型
     */
    void registerMessageStatusChangeListener(@NonNull IChange<String> listener, @NonNull RegisterType registerType);

    /**
     * 消息红点 （通知栏点击）
     *
     * @param listener     接收消息监听器
     * @param registerType 注册类型
     */
    void registerSingleMessageStatusChangeListener(@NonNull IChange<String> listener,
            @NonNull RegisterType registerType);

    /**
     * 服务包订单状态
     *
     * @param listener     接收消息监听器
     * @param registerType 注册类型
     */
    void registerOrderStatusChangeListener(@NonNull IChange<String> listener, @NonNull RegisterType registerType);
}
