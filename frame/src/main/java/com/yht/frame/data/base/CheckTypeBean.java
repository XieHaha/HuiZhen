package com.yht.frame.data.base;

import java.io.Serializable;

/**
 * @author 顿顿
 * @date 19/7/8 17:17
 * @des 预约检查项
 */
public class CheckTypeBean implements Serializable {
    private static final long serialVersionUID = -3717248876501809123L;
    private int status;
    private String name;
    private String report;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }
}
