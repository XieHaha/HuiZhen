package com.yht.frame.data.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author 顿顿
 * @date 19/8/15 16:13
 * @des 我的合作医院
 */
public class CooperateHospitalBean implements Serializable {
    private static final long serialVersionUID = -8879532310717138149L;
    private String hospitalName;
    private String hospitalCode;
    private String cooperateStatus;
    private String address;
    private String introduce;
    private List<String> serviceList;
    private HospitalProjectParentBean productList;

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getHospitalCode() {
        return hospitalCode;
    }

    public void setHospitalCode(String hospitalCode) {
        this.hospitalCode = hospitalCode;
    }

    public String getCooperateStatus() {
        return cooperateStatus;
    }

    public void setCooperateStatus(String cooperateStatus) {
        this.cooperateStatus = cooperateStatus;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public List<String> getServiceList() {
        return serviceList;
    }

    public void setServiceList(List<String> serviceList) {
        this.serviceList = serviceList;
    }

    public HospitalProjectParentBean getProductList() {
        return productList;
    }

    public void setProductList(HospitalProjectParentBean productList) {
        this.productList = productList;
    }
}
