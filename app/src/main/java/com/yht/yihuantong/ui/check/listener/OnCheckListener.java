package com.yht.yihuantong.ui.check.listener;

import com.yht.frame.data.base.ReserveTransferBean;

/**
 * @author 顿顿
 * @date 19/6/4 10:22
 * @des 步骤回调
 */
public interface OnCheckListener {
    /**
     * @param bean 基础信息
     */
    void onStepOne(ReserveTransferBean bean);

    /**
     * @param bean 包含病例等信息
     */
    void onStepTwo(ReserveTransferBean bean);

    /**
     * 结果
     */
    void onStepThree();
}
