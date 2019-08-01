package com.yht.yihuantong.ui.hint;

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
    private String errorHint;

    @Override
    public int getLayoutID() {
        return R.layout.act_hint;
    }

    @Override
    protected boolean isInitStatusBar() {
        return false;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        if (getIntent() != null) {
            errorHint = getIntent().getStringExtra(CommonData.KEY_PUBLIC_STRING);
        }
        dialogSimpleHintContent.setText(errorHint);
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
