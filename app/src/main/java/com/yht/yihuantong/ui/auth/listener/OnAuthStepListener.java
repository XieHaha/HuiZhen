package com.yht.yihuantong.ui.auth.listener;

import com.yht.frame.data.bean.DoctorAuthBean;

/**
 * @author 顿顿
 * @date 19/6/4 10:22
 * @des 步骤回调
 */
public interface OnAuthStepListener {
    /**
     * 基础信息
     *
     * @param doctorAuthBean 回传数据
     */
    void onAuthOne(DoctorAuthBean doctorAuthBean);

    /**
     * 执照
     *
     * @param type           (意图  上一步or提交)
     * @param doctorAuthBean 回传数据
     */
    void onAuthTwo(int type, DoctorAuthBean doctorAuthBean);

    /**
     * 重新认证
     */
    void onAuthThree();
}
