package com.yht.yihuantong.ui.remote;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yht.frame.data.CommonData;
import com.yht.frame.ui.BaseActivity;
import com.yht.yihuantong.R;
import com.yzq.zxinglibrary.android.CaptureActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/8/21 17:50
 * @description 错误提示页面
 */
public class ErrorActivity extends BaseActivity {
    @BindView(R.id.public_title_bar_back)
    ImageView publicTitleBarBack;
    @BindView(R.id.tv_error_hint)
    TextView tvErrorHint;
    /**
     * 是否为预约
     */
    private boolean isReverse;
    /**
     * 扫码结果
     */
    private static final int REQUEST_CODE_SCAN = 100;

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_error;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        publicTitleBarBack.setImageResource(R.mipmap.ic_delete_black);
        if (getIntent() != null) {
            isReverse = getIntent().getBooleanExtra(CommonData.KEY_INTENT_BOOLEAN, false);
        }
        if (isReverse) {
            tvErrorHint.setText(R.string.txt_camera_patient_error);
        }
    }

    @OnClick({ R.id.public_title_bar_back, R.id.tv_contact })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.public_title_bar_back:
                finish();
                break;
            case R.id.tv_contact:
                openScan();
                break;
            default:
                break;
        }
    }

    /**
     * 开启扫一扫
     */
    private void openScan() {
        Intent intent = new Intent(this, CaptureActivity.class);
        startActivityForResult(intent, REQUEST_CODE_SCAN);
        overridePendingTransition(R.anim.keep, R.anim.keep);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (data == null) { finish(); }
        if (requestCode == REQUEST_CODE_SCAN && data != null) {
            setResult(RESULT_OK, data);
            finish();
        }
    }
}
