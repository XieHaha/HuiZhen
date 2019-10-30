package com.yht.frame.data.bean;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

/**
 * @author 顿顿
 * @date 19/7/10 20:56
 * @description 服务包下的服务项
 */
public class SelectCheckTypeChildBean extends LitePalSupport implements Serializable {
    private static final long serialVersionUID = 3966513514891037983L;
    private String parentId;
    private String productCode;
    private String productName;
    private String productCount;
    private String actualPrice;

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCount() {
        return productCount;
    }

    public void setProductCount(String productCount) {
        this.productCount = productCount;
    }

    public String getActualPrice() {
        return actualPrice;
    }

    public void setActualPrice(String actualPrice) {
        this.actualPrice = actualPrice;
    }
}
