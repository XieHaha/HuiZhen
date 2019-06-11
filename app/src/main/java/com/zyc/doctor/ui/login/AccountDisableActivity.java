package com.zyc.doctor.ui.login;

import android.content.Intent;
import android.net.Uri;

import com.yht.frame.ui.BaseActivity;
import com.yht.frame.widgets.dialog.HintDialog;
import com.zyc.doctor.R;

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
        new HintDialog(this).setTitleString(getString(R.string.txt_hint))
                            .setContentString(getString(R.string.txt_contact_hotline))
                            .setEnterBtnTxt(getString(R.string.txt_call))
                            .setEnterSelect(true)
                            .setOnEnterClickListener(() -> callPhone(""))
                            .show();
    }

    /**
     * 拨打电话（跳转到拨号界面，用户手动点击拨打）
     *
     * @param phoneNum 电话号码
     */
    public void callPhone(String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        startActivity(intent);
    }
}
