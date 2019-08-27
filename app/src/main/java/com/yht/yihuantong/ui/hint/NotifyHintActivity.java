package com.yht.yihuantong.ui.hint;

import android.view.View;

import com.yht.frame.data.BaseData;
import com.yht.frame.data.CommonData;
import com.yht.frame.ui.BaseActivity;
import com.yht.yihuantong.R;
import com.yht.yihuantong.utils.NotifySettingUtils;

import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/7/25 16:50
 * @description
 */
public class NotifyHintActivity extends BaseActivity {
    @Override
    public int getLayoutID() {
        return R.layout.act_nofity_hint;
    }

    @OnClick({ R.id.tv_open_notify_next, R.id.tv_notify_ignore })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_open_notify_next:
                //表示不再提示通知权限的提醒
                sharePreferenceUtil.putAlwaysInteger(CommonData.KEY_NOTIFICATION_CONTROL, BaseData.BASE_TWO);
                NotifySettingUtils.openNotifySetting(this);
                finish();
                break;
            case R.id.tv_notify_ignore:
                //表示不再提示通知权限的提醒
                sharePreferenceUtil.putAlwaysInteger(CommonData.KEY_NOTIFICATION_CONTROL, BaseData.BASE_TWO);
                finish();
                overridePendingTransition(R.anim.keep, R.anim.actionsheet_dialog_out);
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
    }
}
