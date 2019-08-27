package com.yht.frame.data.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author 顿顿
 * @date 19/8/19 18:50
 * @description 标签集合
 */
public class LabelSetBean implements Serializable {
    private static final long serialVersionUID = 4329017783451079173L;
    private ArrayList<LabelBean> patientTag;
    private ArrayList<LabelBean> doctorTag;

    public ArrayList<LabelBean> getPatientTag() {
        return patientTag;
    }

    public void setPatientTag(ArrayList<LabelBean> patientTag) {
        this.patientTag = patientTag;
    }

    public ArrayList<LabelBean> getDoctorTag() {
        return doctorTag;
    }

    public void setDoctorTag(ArrayList<LabelBean> doctorTag) {
        this.doctorTag = doctorTag;
    }
}
