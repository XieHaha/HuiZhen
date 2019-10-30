package com.yht.frame.data.bean;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author 顿顿
 * @date 19/10/30 14:20
 * @description 包含医院、服务包、服务项集合
 */
public class SelectCheckTypeParentBean extends LitePalSupport implements Serializable {
    private static final long serialVersionUID = -8735324027625934833L;
    private int relation;
    private String hospitalCode;
    private String hospitalName;
    private ArrayList<SelectCheckTypeBean> productPackageList;

    public int getRelation() {
        return relation;
    }

    public void setRelation(int relation) {
        this.relation = relation;
    }

    public String getHospitalCode() {
        return hospitalCode;
    }

    public void setHospitalCode(String hospitalCode) {
        this.hospitalCode = hospitalCode;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public ArrayList<SelectCheckTypeBean> getProductPackageList() {
        return productPackageList;
    }

    public void setProductPackageList(ArrayList<SelectCheckTypeBean> productPackageList) {
        this.productPackageList = productPackageList;
    }
}