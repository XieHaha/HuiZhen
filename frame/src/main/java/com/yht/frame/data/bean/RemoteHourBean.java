package com.yht.frame.data.bean;

import java.io.Serializable;

/**
 * @author 顿顿
 * @date 19/10/28 19:19
 * @description 预约时间
 */
public class RemoteHourBean implements Serializable {
    private static final long serialVersionUID = 5166243445558408463L;
    private long startAt;
    private long endAt;

    public long getStartAt() {
        return startAt;
    }

    public void setStartAt(long startAt) {
        this.startAt = startAt;
    }

    public long getEndAt() {
        return endAt;
    }

    public void setEndAt(long endAt) {
        this.endAt = endAt;
    }
}
