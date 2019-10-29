package com.yht.frame.data.type;

/**
 * @author 顿顿
 * @date 19/10/29 15:55
 * @description 诊断意见录入方式
 */
public interface SuggestionTypeStatus {
    /**
     * 执行科室录入
     */
    int SUGGESTION_TYPE_DEPART = 1;
    /**
     * 医生录入
     */
    int SUGGESTION_TYPE_DOCTOR = 2;
    /**
     * 无
     */
    int SUGGESTION_TYPE_NONE = 3;
}
