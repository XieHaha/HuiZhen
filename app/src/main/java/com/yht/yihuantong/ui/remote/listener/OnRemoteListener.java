package com.yht.yihuantong.ui.remote.listener;

import com.yht.frame.data.bean.ReserveRemoteBean;

/**
 * @author 顿顿
 * @date 19/6/4 10:22
 * @description 步骤回调
 */
public interface OnRemoteListener {
    /**
     * @param bean 基础信息
     */
    void onRemoteStepOne(ReserveRemoteBean bean);

    /**
     * @param bean 包含病例等信息
     */
    void onRemoteStepTwo(ReserveRemoteBean bean);

    /**
     * @param bean 结果
     */
    void onRemoteStepThree(ReserveRemoteBean bean);
}
