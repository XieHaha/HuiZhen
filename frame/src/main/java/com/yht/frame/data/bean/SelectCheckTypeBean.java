package com.yht.frame.data.bean;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 顿顿
 * @date 19/7/10 20:56
 * @description 服务项or服务包
 */
public class SelectCheckTypeBean extends LitePalSupport implements Serializable {
    private static final long serialVersionUID = 2307028291664717958L;
    /**
     * 1 服务项，2 服务包
     */
    private int type;
    private int refundType;
    private int payType;
    private long price;
    private String projectCode;
    private String projectName;
    private String projectAlias;
    private String hospitalCode;
    private String hospitalName;
    private ArrayList<SelectCheckTypeChildBean> productInfoList;

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getRefundType() {
        return refundType;
    }

    public void setRefundType(int refundType) {
        this.refundType = refundType;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public String getProjectAlias() {
        return projectAlias;
    }

    public void setProjectAlias(String projectAlias) {
        this.projectAlias = projectAlias;
    }

    public ArrayList<SelectCheckTypeChildBean> getProductInfoList() {
        return productInfoList;
    }

    public void setProductInfoList(ArrayList<SelectCheckTypeChildBean> productInfoList) {
        this.productInfoList = productInfoList;
    }

    public List<SelectCheckTypeChildBean> getChildServiceTypes(String id) {
        return LitePal.where("parentId=?", id).find(SelectCheckTypeChildBean.class);
    }

    @Override
    public boolean equals(Object obj) {
        SelectCheckTypeBean bean = (SelectCheckTypeBean)obj;
        return projectCode.equals(bean.getProjectCode());
    }
}
