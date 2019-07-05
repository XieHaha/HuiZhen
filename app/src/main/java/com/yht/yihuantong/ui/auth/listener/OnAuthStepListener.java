package com.yht.yihuantong.ui.auth.listener;

import com.yht.frame.data.base.DoctorAuthBean;

/**
 * @author 顿顿
 * @date 19/6/4 10:22
 * @des 步骤回调
 */
public interface OnAuthStepListener {
    /**
     * 基础信息
     */
    void onAuthOne(DoctorAuthBean doctorAuthBean);

    /**
     * 执照
     *
     * @param type 操作类型
     */
    void onAuthTwo(int type);

    /**
     * 结果
     */
    void onAuthThree();
}
