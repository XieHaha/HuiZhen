package com.yht.frame.data.base;

import java.io.Serializable;

/**
 * @author 顿顿
 * @date 19/7/11 18:12
 * @des 工作室banner
 */
public class BannerBean implements Serializable {
    private static final long serialVersionUID = -6912693721076342355L;
    private int intervalTime;
    private String bannerRemark;
    private String bannerUrl;

    public int getIntervalTime() {
        return intervalTime;
    }

    public void setIntervalTime(int intervalTime) {
        this.intervalTime = intervalTime;
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
}
