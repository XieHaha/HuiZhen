package com.yht.frame.data.bean;

import java.io.Serializable;

/**
 * @author 顿顿
 * @date 19/8/19 18:07
 * @des 标签集合
 */
public class LabelBean implements Serializable {
    private static final long serialVersionUID = 3822264313226879908L;
    private String tagName;

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
}
