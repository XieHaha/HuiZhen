package com.yht.frame.data.base;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * 患者
 *
 * @author dundun
 */
public class PatientBean extends DataSupport implements Serializable {
    private static final long serialVersionUID = -8620697034094662215L;
    private String patientId;
    private String code;
    private String name;
    private String mobile;
    private String wxPhoto;
    /**
     * 排序
     */
    private String indexTag;

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getWxPhoto() {
        return wxPhoto;
    }

    public void setWxPhoto(String wxPhoto) {
        this.wxPhoto = wxPhoto;
    }

    public String getIndexTag() {
        return indexTag;
    }

    public void setIndexTag(String indexTag) {
        this.indexTag = indexTag;
    }
}
