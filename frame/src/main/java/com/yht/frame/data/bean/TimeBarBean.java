package com.yht.frame.data.bean;

import java.io.Serializable;

/**
 * @author 顿顿
 * @date 19/10/22 17:34
 * @description 时间条
 */
public class TimeBarBean implements Serializable {
    private static final long serialVersionUID = 4343140642098734701L;
    private int position;
    private String hourString;
    private String hourTxt;

    public String getHourString() {
        return hourString;
    }

    public void setHourString(String hourString) {
        this.hourString = hourString;
    }

    public String getHourTxt() {
        return hourTxt;
    }

    public void setHourTxt(String hourTxt) {
        this.hourTxt = hourTxt;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
