package com.yht.frame.data.bean;

import java.io.Serializable;

/**
 * @author 顿顿
 * @date 19/7/11 18:12
 * @description 工作室banner
 */
public class BannerBean implements Serializable {
    private static final long serialVersionUID = -6912693721076342355L;
    private int intervalTime;
    private int bannerId;
    private String bannerRemark;

    public int getIntervalTime() {
        return intervalTime;
    }

    public void setIntervalTime(int intervalTime) {
        this.intervalTime = intervalTime;
    }

    public int getBannerId() {
        return bannerId;
    }

    public void setBannerId(int bannerId) {
        this.bannerId = bannerId;
    }

    public String getBannerRemark() {
        return bannerRemark;
    }

    public void setBannerRemark(String bannerRemark) {
        this.bannerRemark = bannerRemark;
    }
}
