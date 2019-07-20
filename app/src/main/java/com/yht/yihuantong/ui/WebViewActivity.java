package com.yht.yihuantong.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tencent.smtt.export.external.interfaces.WebResourceError;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.yht.frame.data.CommonData;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.widgets.LoadViewHelper;
import com.yht.yihuantong.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/6/29 14:57
 * @des web
 */
public class WebViewActivity extends BaseActivity implements LoadViewHelper.OnNextClickListener {
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
    @BindView(R.id.layout_public_hint)
    LinearLayout layoutPublicHint;
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
    private boolean isError;

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_webview;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        loadViewHelper = new LoadViewHelper(this);
        loadViewHelper.setOnNextClickListener(this);
        loadViewClient();
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
        if (BaseUtils.isNetworkAvailable(this)) {
            showLoadingView();
            webView.loadUrl(url);
        }
        else {
            loadStatus(true, LoadViewHelper.NONE_NETWORK);
        }
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

    private void loadViewClient() {
        WebViewClient webViewClient = new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                closeLoadingView();
                if (!isError) {
                    webView.setVisibility(View.VISIBLE);
                    layoutPublicHint.setVisibility(View.GONE);
                }
            }

            // 新版本，只会在Android6及以上调用
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                isError = true;
                loadStatus(true, LoadViewHelper.NONE_NETWORK);
            }

            /**
             * 这里进行无网络或错误处理，具体可以根据errorCode的值进行判断，做跟详细的处理。
             *
             * @param view
             */
            // 旧版本，会在新版本中也可能被调用，所以加上一个判断，防止重复显示
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                isError = true;
                loadStatus(true, LoadViewHelper.NONE_NETWORK);
            }
        };
        webView.setWebViewClient(webViewClient);
    }

    @Override
    public void onNextClick() {
        isError = false;
        showLoadingView();
        webView.loadUrl(url);
    }

    /**
     * 加载状态
     */
    private void loadStatus(boolean isError, int type) {
        if (isError) {
            webView.setVisibility(View.GONE);
            layoutPublicHint.setVisibility(View.VISIBLE);
            loadViewHelper.load(type);
        }
        else {
            webView.setVisibility(View.VISIBLE);
            layoutPublicHint.setVisibility(View.GONE);
        }
    }
}
