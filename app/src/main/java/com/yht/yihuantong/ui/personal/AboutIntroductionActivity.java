package com.yht.yihuantong.ui.personal;

import com.yht.frame.ui.BaseActivity;
import com.yht.yihuantong.R;

/**
 * @author 顿顿
 * @date 19/6/10 14:10
 * @description 会珍简介
 */
public class AboutIntroductionActivity extends BaseActivity {
    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_about_introduction;
    }
}
