package com.yht.frame.data.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * @author 顿顿
 * @date 19/6/29 10:57
 * @des 检查 转诊 远程
 */
public class CheckBean implements MultiItemEntity {
    /**
     * 检查
     */
    public static final int CHECK = 1;
    /**
     * 转诊
     */
    public static final int TRANSFER = 2;
    /**
     * 远程
     */
    public static final int REMOTE = 3;
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
