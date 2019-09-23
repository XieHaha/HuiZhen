package com.yht.frame.data.bean;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;

/**
 * @author 顿顿
 * @date 19/8/20 14:46
 * @description 最近添加的居民
 */
public class RecentPatientTitleBean extends AbstractExpandableItem<PatientBean>
        implements Serializable, MultiItemEntity {
    private static final long serialVersionUID = -6284535838400084307L;
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
