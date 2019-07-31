package com.yht.frame.data.bean;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * @author 顿顿
 * @date 19/7/10 20:56
 * @des
 */
public class SelectCheckTypeBean extends DataSupport implements Serializable {
    private static final long serialVersionUID = 2307028291664717958L;
    private long price;
    private String projectCode;
    private String projectName;
    private String hospitalName;
    private String hospitalCode;

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

    @Override
    public boolean equals(Object obj) {
        SelectCheckTypeBean bean = (SelectCheckTypeBean)obj;
        return projectCode.equals(bean.getProjectCode());
    }
}
