package com.yht.frame.data.bean;

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
    /**
     * 龙建军返回数据
     *******/
    private String amount;
    private String examName;
    private String isArrived;

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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public String getIsArrived() {
        return isArrived;
    }

    public void setIsArrived(String isArrived) {
        this.isArrived = isArrived;
    }
}
