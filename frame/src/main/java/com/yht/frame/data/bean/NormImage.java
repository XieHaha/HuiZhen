package com.yht.frame.data.bean;

import java.io.Serializable;

/**
 * @author luozi
 * @date 2016/6/2
 */
public class NormImage implements Serializable {
    private static final long serialVersionUID = -6229093380433256099L;
    /**
     * url地址
     */
    String imageUrl;
    /**
     * 本地地址
     */
    String imagePath;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
