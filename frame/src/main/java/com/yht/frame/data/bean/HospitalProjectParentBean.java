package com.yht.frame.data.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author 顿顿
 * @date 19/10/29 14:20
 * @description
 */
public class HospitalProjectParentBean implements Serializable {
    private static final long serialVersionUID = 628582912375489299L;
    private int count;
    private ArrayList<HospitalProjectBean> packageProductInfoList;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ArrayList<HospitalProjectBean> getPackageProductInfoList() {
        return packageProductInfoList;
    }

    public void setPackageProductInfoList(ArrayList<HospitalProjectBean> packageProductInfoList) {
        this.packageProductInfoList = packageProductInfoList;
    }
}
