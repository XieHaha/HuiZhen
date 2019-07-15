package com.yht.frame.data.type;

/**
 * @author dundun
 * @date 19/2/19
 * 医生认证状态
 */
public interface DocAuthStatus {
    /**
     * <0-未上传资料（可修改）
     */
    int AUTH_NONE = 0;
    /**
     * ；1-已上传资料等待审核（可修改）
     */
    int AUTH_WAITTING = 1;
    /**
     * 6-审核已通过（可修改）>
     */
    int AUTH_SUCCESS = 2;
    /**
     * ；2-审核未通过（可修改）；
     */
    int AUTH_FAILD = 3;
}
