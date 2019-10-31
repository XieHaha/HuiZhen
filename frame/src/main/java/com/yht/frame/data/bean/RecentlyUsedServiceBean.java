package com.yht.frame.data.bean;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

/**
 * @author 顿顿
 * @date 19/10/31 15:43
 * @description 最近使用
 */
public class RecentlyUsedServiceBean extends LitePalSupport implements Serializable {
    private static final long serialVersionUID = -2203773659959500213L;
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
