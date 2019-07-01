package com.yht.frame.data.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * @author 顿顿
 * @date 19/6/29 10:57
 * @des 通知消息
 */
public class NotifyMessageBean implements MultiItemEntity {
    /**
     * 报告
     */
    public static final int REPORT = 1;
    /**
     * 会珍币
     */
    public static final int CURRENCY = 2;
    /**
     * type
     */
    private int itemType;
    private String title;
    private String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
