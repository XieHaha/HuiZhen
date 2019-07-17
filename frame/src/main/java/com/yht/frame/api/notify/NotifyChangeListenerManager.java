package com.yht.frame.api.notify;

import android.support.annotation.NonNull;

import com.yht.frame.utils.HuiZhenLog;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author dundun
 */
public class NotifyChangeListenerManager {
    private static NotifyChangeListenerServer notifyChangeListenerServer;

    public synchronized static NotifyChangeListenerServer getInstance() {
        if (notifyChangeListenerServer == null) {
            notifyChangeListenerServer = new NotifyChangeListenerServer();
        }
        return notifyChangeListenerServer;
    }

    public static class NotifyChangeListenerServer implements INotifyChangeListenerServer {
        private final String TAG = "ZYC->notify";
        /**
         * 患者列表添加状态
         */
        private List<IChange<String>> mPatientListChangeListeners = new CopyOnWriteArrayList<>();
        /**
         * 医生添加状态
         */
        private List<IChange<String>> mDoctorStatusChangeListeners = new CopyOnWriteArrayList<>();
        /**
         * 医生认证
         */
        private List<IChange<Integer>> mDoctorAuthStatusChangeListeners = new CopyOnWriteArrayList<>();
        /**
         * 转诊申请
         */
        private List<IChange<String>> mDoctorTransferPatientListeners = new CopyOnWriteArrayList<>();
        /**
         * 消息
         */
        private List<IChange<String>> mRegisterMessageChangeListener = new CopyOnWriteArrayList<>();
        /**
         * 服务包订单
         */
        private List<IChange<String>> mOrderStatusChangeListener = new CopyOnWriteArrayList<>();

        @Override
        public void registerPatientListChangeListener(@NonNull IChange<String> listener,
                @NonNull RegisterType registerType) {
            if (listener == null) {
                return;
            }
            if (RegisterType.REGISTER == registerType) {
                mPatientListChangeListeners.add(listener);
            }
            else {
                mPatientListChangeListeners.remove(listener);
            }
        }

        @Override
        public void registerDoctorStatusChangeListener(@NonNull IChange<String> listener,
                @NonNull RegisterType registerType) {
            if (listener == null) {
                return;
            }
            if (RegisterType.REGISTER == registerType) {
                mDoctorStatusChangeListeners.add(listener);
            }
            else {
                mDoctorStatusChangeListeners.remove(listener);
            }
        }

        @Override
        public void registerDoctorTransferPatientListener(@NonNull IChange<String> listener,
                @NonNull RegisterType registerType) {
            if (listener == null) {
                return;
            }
            if (RegisterType.REGISTER == registerType) {
                mDoctorTransferPatientListeners.add(listener);
            }
            else {
                mDoctorTransferPatientListeners.remove(listener);
            }
        }

        @Override
        public void registerDoctorAuthStatusChangeListener(@NonNull IChange<Integer> listener,
                @NonNull RegisterType registerType) {
            if (listener == null) {
                return;
            }
            if (RegisterType.REGISTER == registerType) {
                mDoctorAuthStatusChangeListeners.add(listener);
            }
            else {
                mDoctorAuthStatusChangeListeners.remove(listener);
            }
        }

        @Override
        public void registerMessageStatusChangeListener(@NonNull IChange<String> listener,
                @NonNull RegisterType registerType) {
            if (listener == null) {
                return;
            }
            if (RegisterType.REGISTER == registerType) {
                mRegisterMessageChangeListener.add(listener);
            }
            else {
                mRegisterMessageChangeListener.remove(listener);
            }
        }

        @Override
        public void registerOrderStatusChangeListener(@NonNull IChange<String> listener,
                @NonNull RegisterType registerType) {
            if (listener == null) {
                return;
            }
            if (RegisterType.REGISTER == registerType) {
                mOrderStatusChangeListener.add(listener);
            }
            else {
                mOrderStatusChangeListener.remove(listener);
            }
        }

        /**
         * 患者添加
         *
         * @param data
         */
        public void notifyPatientStatusChange(final String data) {
            synchronized (mPatientListChangeListeners) {
                for (int i = 0, size = mPatientListChangeListeners.size(); i < size; i++) {
                    try {
                        final IChange<String> change = mPatientListChangeListeners.get(i);
                        if (null != change) {
                            change.onChange(data);
                        }
                    }
                    catch (Exception e) {
                        HuiZhenLog.w(TAG, "notifyStatusChange error", e);
                    }
                }
            }
        }

        /**
         * 医生添加
         *
         * @param data
         */
        public void notifyDoctorStatusChange(final String data) {
            synchronized (mDoctorStatusChangeListeners) {
                for (int i = 0, size = mDoctorStatusChangeListeners.size(); i < size; i++) {
                    try {
                        final IChange<String> change = mDoctorStatusChangeListeners.get(i);
                        if (null != change) {
                            change.onChange(data);
                        }
                    }
                    catch (Exception e) {
                        HuiZhenLog.w(TAG, "notifyMessageChange error", e);
                    }
                }
            }
        }

        /**
         * 转诊申请
         *
         * @param data
         */
        public void notifyDoctorTransferPatient(final String data) {
            synchronized (mDoctorTransferPatientListeners) {
                for (int i = 0, size = mDoctorTransferPatientListeners.size(); i < size; i++) {
                    try {
                        final IChange<String> change = mDoctorTransferPatientListeners.get(i);
                        if (null != change) {
                            change.onChange(data);
                        }
                    }
                    catch (Exception e) {
                        HuiZhenLog.w(TAG, "notifyMessageChange error", e);
                    }
                }
            }
        }

        /**
         * 医生认证状态
         *
         * @param data
         */
        public void notifyDoctorAuthStatus(final Integer data) {
            synchronized (mDoctorAuthStatusChangeListeners) {
                for (int i = 0, size = mDoctorAuthStatusChangeListeners.size(); i < size; i++) {
                    try {
                        final IChange<Integer> change = mDoctorAuthStatusChangeListeners.get(i);
                        if (null != change) {
                            change.onChange(data);
                        }
                    }
                    catch (Exception e) {
                        HuiZhenLog.w(TAG, "notifyMessageChange error", e);
                    }
                }
            }
        }

        /**
         * 消息
         *
         * @param data
         */
        public void notifyMessageStatusChange(final String data) {
            synchronized (mRegisterMessageChangeListener) {
                for (int i = 0, size = mRegisterMessageChangeListener.size(); i < size; i++) {
                    try {
                        final IChange<String> change = mRegisterMessageChangeListener.get(i);
                        if (null != change) {
                            change.onChange(data);
                        }
                    }
                    catch (Exception e) {
                        HuiZhenLog.w(TAG, "notifyMessageChange error", e);
                    }
                }
            }
        }

        /**
         * 服务包订单
         *
         * @param data
         */
        public void notifyOrderStatusChange(final String data) {
            synchronized (mOrderStatusChangeListener) {
                for (int i = 0, size = mOrderStatusChangeListener.size(); i < size; i++) {
                    try {
                        final IChange<String> change = mOrderStatusChangeListener.get(i);
                        if (null != change) {
                            change.onChange(data);
                        }
                    }
                    catch (Exception e) {
                        HuiZhenLog.w(TAG, "notifyMessageChange error", e);
                    }
                }
            }
        }
    }
}
