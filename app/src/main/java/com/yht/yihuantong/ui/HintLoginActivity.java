package com.yht.yihuantong.ui;

import com.yht.frame.ui.BaseActivity;
import com.yht.yihuantong.R;

import butterknife.OnClick;

/**
 * @author dundun
 * @date 15/12/31
 *
 */
public class HintLoginActivity extends BaseActivity {
    @Override
    public int getLayoutID() {
        return R.layout.act_hint;
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.keep, R.anim.fade_out);
    }

    @OnClick(R.id.dialog_simple_hint_enter)
    public void onViewClicked() {
        exit();
    }
}
