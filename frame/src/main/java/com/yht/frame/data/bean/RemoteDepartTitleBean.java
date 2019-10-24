package com.yht.frame.data.bean;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;

/**
 * @author 顿顿
 * @date 19/8/20 14:46
 * @description 远程会诊   科室所属医院
 */
public class RemoteDepartTitleBean extends AbstractExpandableItem<RemoteDepartBean>
        implements Serializable, MultiItemEntity {
    private static final long serialVersionUID = 4260244264940911763L;
    private String hospitalName;
    private int selectedNum;

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public int getSelectedNum() {
        return selectedNum;
    }

    public void setSelectedNum(int selectedNum) {
        this.selectedNum = selectedNum;
    }

    @Override
    public int getLevel() {
        return 0;
    }

    @Override
    public int getItemType() {
        return 0;
    }
}
