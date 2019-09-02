package com.yht.yihuantong.ui.login;

import android.view.View;
import android.widget.ImageView;

import com.yht.frame.ui.BaseActivity;
import com.yht.frame.widgets.dialog.HintDialog;
import com.yht.yihuantong.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/6/11 14:19
 * @description 账号禁用
 */
public class AccountDisableActivity extends BaseActivity {
    @BindView(R.id.public_title_bar_back)
    ImageView publicTitleBarBack;

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_account_disable;
    }

    @Override
    public void initListener() {
        super.initListener();
        publicTitleBarBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == publicTitleBarBack.getId()) {
            exit();
        }
    }

    @OnClick(R.id.tv_contact)
    public void onViewClicked() {
        new HintDialog(this).setPhone(getString(R.string.txt_contact_service),
                                      getString(R.string.txt_contact_service_phone), false)
                            .setOnEnterClickListener(() -> callPhone(getString(R.string.txt_contact_service_phone)))
                            .show();
    }

    @Override
    public void onBackPressed() {
        exit();
    }
}
