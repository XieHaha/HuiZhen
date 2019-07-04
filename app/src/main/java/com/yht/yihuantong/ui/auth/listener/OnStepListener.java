package com.yht.yihuantong.ui.auth.listener;

/**
 * @author 顿顿
 * @date 19/6/4 10:22
 * @des 步骤回调
 */
public interface OnStepListener {
    /**
     * 基础信息
     */
    void onStepOne();

    /**
     * 执照
     *
     * @param type 操作类型
     */
    void onStepTwo(int type);

    /**
     * 结果
     */
    void onStepThree();
}
