package com.yht.frame.data.bean;

import android.text.TextUtils;

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
    /**
     * 1、直属   2、合作
     */
    private int relation;
    private String hospitalCode;
    private String hospitalName;
    private ArrayList<SelectCheckTypeBean> productPackageList;
    /**
     * 排序
     */
    private String indexTag;

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

    public String getIndexTag() {
        return indexTag;
    }

    public void setIndexTag(String indexTag) {
        this.indexTag = indexTag;
    }

    @Override
    public boolean equals(Object obj) {
        SelectCheckTypeParentBean bean = (SelectCheckTypeParentBean)obj;
        return TextUtils.equals(hospitalCode, bean.getHospitalCode());
    }
}
