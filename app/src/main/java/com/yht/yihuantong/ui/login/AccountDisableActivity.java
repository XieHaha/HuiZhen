package com.yht.yihuantong.ui.login;

import com.yht.frame.ui.BaseActivity;
import com.yht.frame.widgets.dialog.HintDialog;
import com.yht.yihuantong.R;

import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/6/11 14:19
 * @des 账号禁用
 */
public class AccountDisableActivity extends BaseActivity {
    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_account_disable;
    }

    @OnClick(R.id.tv_contact)
    public void onViewClicked() {
        new HintDialog(this).setPhone(getString(R.string.txt_contact_service),
                                      getString(R.string.txt_contact_service_phone), false)
                            .setOnEnterClickListener(() -> callPhone(getString(R.string.txt_contact_service_phone)))
                            .show();
    }
}
