package com.yht.yihuantong.ui.personal;

import android.view.View;

import com.yht.frame.ui.BaseActivity;
import com.yht.yihuantong.R;

import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/6/10 14:10
 * @des
 */
public class AboutActivity extends BaseActivity {
    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_about;
    }

    @OnClick({ R.id.layout_service_protocol, R.id.layout_about_understand })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_service_protocol:
                break;
            case R.id.layout_about_understand:
                break;
            default:
                break;
        }
    }
}
