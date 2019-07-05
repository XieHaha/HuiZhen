package com.yht.frame.data.base;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * @author dundun
 * @date 18/8/15 (医生认证选择医院)
 */
public class HospitalBean extends DataSupport implements Serializable {
    private static final long serialVersionUID = 6102212747958144450L;
    private String hospitalName;
    private String hospitalAddress;
    private String hospitalCode;

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getHospitalAddress() {
        return hospitalAddress;
    }

    public void setHospitalAddress(String hospitalAddress) {
        this.hospitalAddress = hospitalAddress;
    }

    public String getHospitalCode() {
        return hospitalCode;
    }

    public void setHospitalCode(String hospitalCode) {
        this.hospitalCode = hospitalCode;
    }
}

