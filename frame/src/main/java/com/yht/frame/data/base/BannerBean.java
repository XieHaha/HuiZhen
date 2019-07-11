package com.yht.frame.data.base;

import java.io.Serializable;

/**
 * @author 顿顿
 * @date 19/7/11 18:12
 * @des 工作室banner
 */
public class BannerBean implements Serializable {
    private static final long serialVersionUID = -6912693721076342355L;
    private int id;
    private int appType;
    private int intervalTime;
    private int bannerStatus;
    private long onlineDate;
    private long offlineDate;
    private long createAt;
    private long updateAt;
    private String bannerName;
    private String bannerRemark;
    private String bannerUrl;
    private String creator;
    private String updator;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAppType() {
        return appType;
    }

    public void setAppType(int appType) {
        this.appType = appType;
    }

    public int getIntervalTime() {
        return intervalTime;
    }

    public void setIntervalTime(int intervalTime) {
        this.intervalTime = intervalTime;
    }

    public int getBannerStatus() {
        return bannerStatus;
    }

    public void setBannerStatus(int bannerStatus) {
        this.bannerStatus = bannerStatus;
    }

    public long getOnlineDate() {
        return onlineDate;
    }

    public void setOnlineDate(long onlineDate) {
        this.onlineDate = onlineDate;
    }

    public long getOfflineDate() {
        return offlineDate;
    }

    public void setOfflineDate(long offlineDate) {
        this.offlineDate = offlineDate;
    }

    public long getCreateAt() {
        return createAt;
    }

    public void setCreateAt(long createAt) {
        this.createAt = createAt;
    }

    public long getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(long updateAt) {
        this.updateAt = updateAt;
    }

    public String getBannerName() {
        return bannerName;
    }

    public void setBannerName(String bannerName) {
        this.bannerName = bannerName;
    }

    public String getBannerRemark() {
        return bannerRemark;
    }

    public void setBannerRemark(String bannerRemark) {
        this.bannerRemark = bannerRemark;
    }

    public String getBannerUrl() {
        return bannerUrl;
    }

    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getUpdator() {
        return updator;
    }

    public void setUpdator(String updator) {
        this.updator = updator;
    }
}
