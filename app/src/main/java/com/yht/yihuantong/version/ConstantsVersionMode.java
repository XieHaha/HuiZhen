package com.yht.yihuantong.version;

/**
 * @author dundun
 * @date 16/4/7
 */
public interface ConstantsVersionMode {
    /**
     * 不更新
     */
    int UPDATE_NONE = 0;
    /**
     * 环境切换（提测使用）
     */
    int UPDATE_OTHER = 1;
    /**
     * 选择更新
     */
    int UPDATE_CHOICE = 100;
    /**
     * 强制更新
     */
    int UPDATE_MUST = 200;
}
