package com.yht.frame.data.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author 顿顿
 * @date 19/8/20 14:46
 * @des 最近添加的患者
 */
public class RecentPatientBean implements Serializable {
    private static final long serialVersionUID = 9111708711871278260L;
    private ArrayList<PatientBean> today;
    private ArrayList<PatientBean> yesterday;
    private ArrayList<PatientBean> week;
    private ArrayList<PatientBean> month;
    private ArrayList<PatientBean> threeMonth;
    private ArrayList<PatientBean> halfYear;

    public ArrayList<PatientBean> getToday() {
        return today;
    }

    public void setToday(ArrayList<PatientBean> today) {
        this.today = today;
    }

    public ArrayList<PatientBean> getYesterday() {
        return yesterday;
    }

    public void setYesterday(ArrayList<PatientBean> yesterday) {
        this.yesterday = yesterday;
    }

    public ArrayList<PatientBean> getWeek() {
        return week;
    }

    public void setWeek(ArrayList<PatientBean> week) {
        this.week = week;
    }

    public ArrayList<PatientBean> getMonth() {
        return month;
    }

    public void setMonth(ArrayList<PatientBean> month) {
        this.month = month;
    }

    public ArrayList<PatientBean> getThreeMonth() {
        return threeMonth;
    }

    public void setThreeMonth(ArrayList<PatientBean> threeMonth) {
        this.threeMonth = threeMonth;
    }

    public ArrayList<PatientBean> getHalfYear() {
        return halfYear;
    }

    public void setHalfYear(ArrayList<PatientBean> halfYear) {
        this.halfYear = halfYear;
    }
}
