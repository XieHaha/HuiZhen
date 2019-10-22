package com.yht.frame.data.bean;

import java.io.Serializable;

/**
 * @author 顿顿
 * @date 19/10/22 17:34
 * @description 时间条
 */
public class TimeBarBean implements Serializable {
    private static final long serialVersionUID = 4343140642098734701L;
    /**
     * 小时点 （以半个小时计算）
     */
    private int hour;
    private int position;
    private String hourString;

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public String getHourString() {
        return hourString;
    }

    public void setHourString(String hourString) {
        this.hourString = hourString;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
