package com.yht.frame.data.base;

import java.io.Serializable;

/**
 * @author 顿顿
 * @date 19/7/9 17:29
 * @des 提交预约检查时选择的检查项
 */
public class ReserveCheckTypeBean implements Serializable {
    private static final long serialVersionUID = -7335917258420482547L;
    private int price;
    private String hospitalCode;
    private String productCode;

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getHospitalCode() {
        return hospitalCode;
    }

    public void setHospitalCode(String hospitalCode) {
        this.hospitalCode = hospitalCode;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }
}
