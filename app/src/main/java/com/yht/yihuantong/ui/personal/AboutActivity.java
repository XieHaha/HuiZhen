package com.yht.yihuantong.ui.personal;

import android.content.Intent;
import android.view.View;

import com.yht.frame.data.BaseNetConfig;
import com.yht.frame.data.CommonData;
import com.yht.frame.ui.BaseActivity;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ZycApplication;
import com.yht.yihuantong.ui.WebViewActivity;

import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/6/10 14:10
 * @description
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

    @OnClick({ R.id.layout_service_protocol, R.id.layout_privacy_protocol, R.id.layout_about_understand })
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.layout_service_protocol:
                intent = new Intent(this, WebViewActivity.class);
                intent.putExtra(CommonData.KEY_PUBLIC,
                                ZycApplication.getInstance().getBaseUrl() + BaseNetConfig.BASE_BASIC_USER_PROTOCOL_URL);
                intent.putExtra(CommonData.KEY_TITLE, getString(R.string.txt_about_protocol));
                startActivity(intent);
                break;
            case R.id.layout_privacy_protocol:
                intent = new Intent(this, WebViewActivity.class);
                intent.putExtra(CommonData.KEY_PUBLIC, ZycApplication.getInstance().getBaseUrl() +
                                                       BaseNetConfig.BASE_BASIC_PRIVATE_PROTOCOL_URL);
                intent.putExtra(CommonData.KEY_TITLE, getString(R.string.txt_about_privacy));
                startActivity(intent);
                break;
            case R.id.layout_about_understand:
                startActivity(new Intent(this, AboutIntroductionActivity.class));
                break;
            default:
                break;
        }
    }
}
