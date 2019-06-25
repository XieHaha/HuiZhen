package com.zyc.doctor.ui.check.listener;

/**
 * @author 顿顿
 * @date 19/6/4 10:22
 * @des 步骤回调
 */
public interface OnCheckListener {
    /**
     * 基础信息
     *
     * @param name
     * @param idCard
     */
    void onStepOne(String name, String idCard);

    /**
     * 执照
     */
    void onStepTwo();

    /**
     * 结果
     */
    void onStepThree();
}
