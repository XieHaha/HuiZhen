package com.yht.yihuantong.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.yht.frame.data.CommonData;
import com.yht.frame.ui.BaseActivity;
import com.yht.yihuantong.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author dundun
 * @date 15/12/31
 */
public class HintLoginActivity extends BaseActivity {
    @BindView(R.id.dialog_simple_hint_content)
    TextView dialogSimpleHintContent;
    /**
     * 是否为环信抢登录
     */
    private boolean isEase;

    @Override
    public int getLayoutID() {
        return R.layout.act_hint;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        if (getIntent() != null) {
            isEase = getIntent().getBooleanExtra(CommonData.KEY_PUBLIC, false);
        }
        if (isEase) {
            dialogSimpleHintContent.setText(R.string.txt_ease_login_expired);
        }
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
