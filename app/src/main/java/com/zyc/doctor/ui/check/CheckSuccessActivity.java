package com.zyc.doctor.ui.check;

import android.content.Intent;
import android.view.View;

import com.yht.frame.ui.BaseActivity;
import com.zyc.doctor.R;

import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/6/17 15:06
 * @des 预约检查成功
 */
public class CheckSuccessActivity extends BaseActivity {
    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_check_success;
    }

    @OnClick({ R.id.tv_check_detail, R.id.tv_check_again })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_check_detail:
                startActivity(new Intent(this, CheckDetailActivity.class));
                finish();
                break;
            case R.id.tv_check_again:
                startActivity(new Intent(this, ReservationCheckActivity.class));
                finish();
                break;
            default:
                break;
        }
    }
}
