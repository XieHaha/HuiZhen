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
    private long tagId;
    private String tagName;
    private int patientNum;
    private ArrayList<PatientBean> patientList;

    public long getTagId() {
        return tagId;
    }

    public void setTagId(long tagId) {
        this.tagId = tagId;
    }

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

    public ArrayList<PatientBean> getPatientList() {
        return patientList;
    }

    public void setPatientList(ArrayList<PatientBean> patientList) {
        this.patientList = patientList;
    }
}
