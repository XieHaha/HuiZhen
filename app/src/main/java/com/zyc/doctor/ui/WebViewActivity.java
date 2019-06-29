package com.zyc.doctor.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tencent.smtt.sdk.WebView;
import com.yht.frame.data.BaseNetConfig;
import com.yht.frame.ui.BaseActivity;
import com.zyc.doctor.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/6/29 14:57
 * @des web
 */
public class WebViewActivity extends BaseActivity {
    @BindView(R.id.web_view)
    WebView webView;
    @BindView(R.id.tv_disagree)
    TextView tvDisagree;
    @BindView(R.id.tv_agree)
    TextView tvAgree;
    @BindView(R.id.layout_bottom)
    LinearLayout layoutBottom;

    @Override
    public int getLayoutID() {
        return R.layout.act_webview;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        webView.loadUrl(BaseNetConfig.BASE_BASIC_USER_PROTOCOL_URL);
    }

    @OnClick({ R.id.tv_disagree, R.id.tv_agree })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_disagree:
                break;
            case R.id.tv_agree:
                break;
            default:
                break;
        }
    }
}
