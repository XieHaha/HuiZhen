package com.yht.frame.data.bean;

import java.io.Serializable;

/**
 * @author 顿顿
 * @date 19/8/15 17:14
 * @des 合作医院下的服务项目
 */
public class HospitalProductBean implements Serializable {
    private static final long serialVersionUID = 4167023736919812368L;
    private int id;
    private int hospitalDepartmentId;
    private int productTypeId;
    private int productTypePid;
    private int isUp;
    private int platformStatus;
    private int saleType;
    private int payType;
    private int allowUpdate;
    private int grantEntityType;
    private int suggestionType;
    private long suggestPrice;
    private long createAt;
    private long updateAt;
    private String hospitalCode;
    private String name;
    private String code;
    private String description;
    private String isEntity;
    private String hisId;
    private String notice;
    private String creator;
    private String alias;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHospitalDepartmentId() {
        return hospitalDepartmentId;
    }

    public void setHospitalDepartmentId(int hospitalDepartmentId) {
        this.hospitalDepartmentId = hospitalDepartmentId;
    }

    public int getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(int productTypeId) {
        this.productTypeId = productTypeId;
    }

    public int getProductTypePid() {
        return productTypePid;
    }

    public void setProductTypePid(int productTypePid) {
        this.productTypePid = productTypePid;
    }

    public int getIsUp() {
        return isUp;
    }

    public void setIsUp(int isUp) {
        this.isUp = isUp;
    }

    public int getPlatformStatus() {
        return platformStatus;
    }

    public void setPlatformStatus(int platformStatus) {
        this.platformStatus = platformStatus;
    }

    public long getSuggestPrice() {
        return suggestPrice;
    }

    public void setSuggestPrice(long suggestPrice) {
        this.suggestPrice = suggestPrice;
    }

    public long getCreateAt() {
        return createAt;
    }

    public void setCreateAt(long createAt) {
        this.createAt = createAt;
    }

    public long getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(long updateAt) {
        this.updateAt = updateAt;
    }

    public String getHospitalCode() {
        return hospitalCode;
    }

    public void setHospitalCode(String hospitalCode) {
        this.hospitalCode = hospitalCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIsEntity() {
        return isEntity;
    }

    public void setIsEntity(String isEntity) {
        this.isEntity = isEntity;
    }

    public String getHisId() {
        return hisId;
    }

    public void setHisId(String hisId) {
        this.hisId = hisId;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public int getSaleType() {
        return saleType;
    }

    public void setSaleType(int saleType) {
        this.saleType = saleType;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public int getAllowUpdate() {
        return allowUpdate;
    }

    public void setAllowUpdate(int allowUpdate) {
        this.allowUpdate = allowUpdate;
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

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}
