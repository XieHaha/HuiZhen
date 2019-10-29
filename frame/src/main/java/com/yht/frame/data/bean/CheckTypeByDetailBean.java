package com.yht.frame.data.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author 顿顿
 * @date 19/7/11 15:30
 * @description 检查详情中的检查项
 */
public class CheckTypeByDetailBean implements Serializable {
    private static final long serialVersionUID = 900363421370287560L;
    private int id;
    private int status;
    private int hospitalDepartmentId;
    private int payType;
    private int grantEntityType;
    private int suggestionType;
    private int itemType;
    private long shouldPay;
    private String name;
    private String orderAt;
    private String payAt;
    private String finishAt;
    private String createAt;
    private String updateAt;
    private String operatorName;
    private String departmentName;
    private String type;
    private String cancelReason;
    private String report;
    private String notice;
    private String productCode;
    private String suggestionText;
    private String grantorName;
    private String packCode;
    private String hospitalCode;
    private String hospitalName;
    private String packName;
    private String mergeName;
    private ArrayList<CheckTypeByDetailBean> itemList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getHospitalDepartmentId() {
        return hospitalDepartmentId;
    }

    public void setHospitalDepartmentId(int hospitalDepartmentId) {
        this.hospitalDepartmentId = hospitalDepartmentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOrderAt() {
        return orderAt;
    }

    public void setOrderAt(String orderAt) {
        this.orderAt = orderAt;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public long getShouldPay() {
        return shouldPay;
    }

    public void setShouldPay(long shouldPay) {
        this.shouldPay = shouldPay;
    }

    public String getPayAt() {
        return payAt;
    }

    public void setPayAt(String payAt) {
        this.payAt = payAt;
    }

    public String getFinishAt() {
        return finishAt;
    }

    public void setFinishAt(String finishAt) {
        this.finishAt = finishAt;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public int getGrantEntityType() {
        return grantEntityType;
    }

    public void setGrantEntityType(int grantEntityType) {
        this.grantEntityType = grantEntityType;
    }

    public int getSuggestionType() {
        return suggestionType;
    }

    public void setSuggestionType(int suggestionType) {
        this.suggestionType = suggestionType;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getSuggestionText() {
        return suggestionText;
    }

    public void setSuggestionText(String suggestionText) {
        this.suggestionText = suggestionText;
    }

    public String getGrantorName() {
        return grantorName;
    }

    public void setGrantorName(String grantorName) {
        this.grantorName = grantorName;
    }

    public String getPackCode() {
        return packCode;
    }

    public void setPackCode(String packCode) {
        this.packCode = packCode;
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

    public String getPackName() {
        return packName;
    }

    public void setPackName(String packName) {
        this.packName = packName;
    }

    public String getMergeName() {
        return mergeName;
    }

    public void setMergeName(String mergeName) {
        this.mergeName = mergeName;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public ArrayList<CheckTypeByDetailBean> getItemList() {
        return itemList;
    }

    public void setItemList(ArrayList<CheckTypeByDetailBean> itemList) {
        this.itemList = itemList;
    }
}
