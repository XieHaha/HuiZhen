package com.yht.frame.data.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author 顿顿
 * @date 19/10/28 11:22
 * @description 健康管理包详情
 */
public class HealthPackageDetailBean implements Serializable {
    private static final long serialVersionUID = 3431140510018349442L;
    private String packageCode;
    private String packageName;
    private String description;
    private ArrayList<ProductBean> productInfoList;

    public String getPackageCode() {
        return packageCode;
    }

    public void setPackageCode(String packageCode) {
        this.packageCode = packageCode;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<ProductBean> getProductInfoList() {
        return productInfoList;
    }

    public void setProductInfoList(ArrayList<ProductBean> productInfoList) {
        this.productInfoList = productInfoList;
    }
}
