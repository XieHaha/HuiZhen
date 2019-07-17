package com.yht.yihuantong.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.TaskStackBuilder;

import com.yht.frame.ui.BaseActivity;
import com.yht.yihuantong.R;
import com.yht.yihuantong.scheme.JumpUtils;
import com.yht.yihuantong.scheme.ViewUtils;
import com.yht.yihuantong.ui.main.MainActivity;

/**
 * @author 顿顿
 * @date 19/7/17 19:40
 * @des
 */
public class IntentParseActivity extends BaseActivity {
    @Override
    public int getLayoutID() {
        return R.layout.act_intent_parse;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        if (loginBean == null) {
            exit();
        }
        Intent intent = getIntent();
        String action = intent.getAction();
        Uri uri = intent.getData();
        try {
            if (Intent.ACTION_VIEW.equals(action) && uri != null) {
                Intent resultIntent = JumpUtils.parseIntent(this, uri.toString(), null);
                if (resultIntent == null) {
                    finish();
                    return;
                }
                resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if (ViewUtils.isLaunchedActivity(this, MainActivity.class)) {
                    startActivity(resultIntent);
                    //                    Intent upIntent = NavUtils.getParentActivityIntent(this);
                    //                    if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    //                        TaskStackBuilder.create(this).addNextIntentWithParentStack(upIntent).startActivities();
                    //                    }
                    //                    else {
                    //                        upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    //                        NavUtils.navigateUpTo(this, upIntent);
                    //                    }
                }
                else {
                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(this)
                                                                    .addParentStack(resultIntent.getComponent())
                                                                    .addNextIntent(resultIntent);
                    stackBuilder.startActivities();
                }
                finish();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            finish();
        }
    }
}
