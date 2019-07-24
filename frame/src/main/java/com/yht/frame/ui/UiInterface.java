package com.yht.frame.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

/**
 * @author luozi
 * @date 2015/12/25
 */
public interface UiInterface {
    /**
     * 得到布局的id
     */
    int getLayoutID();

    /**
     * 得到布局的view
     */
    View getLayoutView();

    /**
     * 初始化类，调用顺序为2
     */
    void initView(@NonNull Bundle savedInstanceState);

    /**
     * 初始化数据，调用顺序为3
     */
    void initData(@NonNull Bundle savedInstanceState);

    /**
     * 得到布局的id，调用顺序为4
     */
    void initListener();

    /**
     * 数据请求
     */
    void fillNetWorkData();

    /**
     * setContentView调用前调用
     */
    void beforeCreateView(@NonNull Bundle savedInstanceState);
}
