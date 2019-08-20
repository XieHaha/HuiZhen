package com.yht.frame.data.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author 顿顿
 * @date 19/8/19 18:07
 * @des 标签集合
 */
public class LabelBean implements Serializable {
    private static final long serialVersionUID = 3822264313226879908L;
    private long id;
    private String tagName;
    private int patientNum;
    private int searchCount;
    private ArrayList<PatientBean> patientList;

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public int getPatientNum() {
        return patientNum;
    }

    public void setPatientNum(int patientNum) {
        this.patientNum = patientNum;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getSearchCount() {
        return searchCount;
    }

    public void setSearchCount(int searchCount) {
        this.searchCount = searchCount;
    }

    public ArrayList<PatientBean> getPatientList() {
        return patientList;
    }

    public void setPatientList(ArrayList<PatientBean> patientList) {
        this.patientList = patientList;
    }
}
