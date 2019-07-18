package com.yht.yihuantong.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tencent.smtt.sdk.WebView;
import com.yht.frame.data.CommonData;
import com.yht.frame.ui.BaseActivity;
import com.yht.yihuantong.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/6/29 14:57
 * @des web
 */
public class WebViewActivity extends BaseActivity {
    @BindView(R.id.layout_title_root)
    RelativeLayout layoutTitleRoot;
    @BindView(R.id.web_view)
    WebView webView;
    @BindView(R.id.tv_disagree)
    TextView tvDisagree;
    @BindView(R.id.tv_agree)
    TextView tvAgree;
    @BindView(R.id.layout_bottom)
    LinearLayout layoutBottom;
    @BindView(R.id.public_title_bar_title)
    TextView publicTitleBarTitle;
    /**
     * title
     */
    private String title;
    /**
     * 源
     */
    private String url;
    /**
     * 是否是登录协议
     */
    private boolean isProtocol;

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_webview;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        if (getIntent() != null) {
            title = getIntent().getStringExtra(CommonData.KEY_TITLE);
            url = getIntent().getStringExtra(CommonData.KEY_PUBLIC);
            isProtocol = getIntent().getBooleanExtra(CommonData.KEY_IS_PROTOCOL, false);
        }
        if (isProtocol) {
            layoutBottom.setVisibility(View.VISIBLE);
            layoutTitleRoot.setVisibility(View.GONE);
        }
        else {
            publicTitleBarTitle.setText(title);
        }
        webView.loadUrl(url);
    }

    @OnClick({ R.id.tv_disagree, R.id.tv_agree })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_disagree:
                finish();
                break;
            case R.id.tv_agree:
                setResult(RESULT_OK);
                finish();
                break;
            default:
                break;
        }
    }
}
