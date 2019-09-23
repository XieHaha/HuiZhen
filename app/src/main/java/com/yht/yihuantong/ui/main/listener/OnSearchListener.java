package com.yht.yihuantong.ui.main.listener;

/**
 * @author 顿顿
 * @date 19/8/16 15:32
 * @des 搜索回调
 */
public interface OnSearchListener {
    /**
     * @param mode 1 为居民，2为医生
     * @param num  总数
     */
    void onSearch(int mode, int num);
}
