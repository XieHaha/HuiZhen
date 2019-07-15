package com.yht.frame.data.base;

import java.io.Serializable;

/**
 * @author 顿顿
 * @date 19/7/15 15:49
 * @des 未读消息总数
 */
public class MessageTotalBean implements Serializable {
    private static final long serialVersionUID = 5224775593865265465L;
    private int total;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
