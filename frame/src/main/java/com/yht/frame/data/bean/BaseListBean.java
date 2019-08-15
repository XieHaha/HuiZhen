package com.yht.frame.data.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author 顿顿
 * @date 19/8/15 16:02
 * @des 所有列表基础bean
 */
public class BaseListBean<T> implements Serializable {
    private static final long serialVersionUID = 1302120641383357878L;
    private int currentPage;
    private int pageTotal;
    private int recordTotal;
    private List<T> records;

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageTotal() {
        return pageTotal;
    }

    public void setPageTotal(int pageTotal) {
        this.pageTotal = pageTotal;
    }

    public int getRecordTotal() {
        return recordTotal;
    }

    public void setRecordTotal(int recordTotal) {
        this.recordTotal = recordTotal;
    }

    public List<T> getRecords() {
        return records;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }
}
