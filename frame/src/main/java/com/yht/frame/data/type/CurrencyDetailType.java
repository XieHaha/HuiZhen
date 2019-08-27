package com.yht.frame.data.type;

/**
 * @author 顿顿
 * @date 19/7/8 18:38
 * @description 资金明细type
 */
public interface CurrencyDetailType {
    /**
     * 医生提现
     */
    int CURRENCY_DETAIL_TYPE_WITHDRAW = 1;
    /**
     * 预约检查
     */
    int CURRENCY_DETAIL_TYPE_CHECK = 2;
    /**
     * 预约转诊
     */
    int CURRENCY_DETAIL_TYPE_TRANSFER = 3;
    /**
     * 远程会诊
     */
    int CURRENCY_DETAIL_TYPE_REMOTE = 4;
}
