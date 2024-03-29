package com.yht.yihuantong.ui.hint;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.app.TaskStackBuilder;
import androidx.appcompat.app.AppCompatActivity;

import com.yht.yihuantong.BuildConfig;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ZycApplication;
import com.yht.yihuantong.scheme.SmsJumpUtils;
import com.yht.yihuantong.scheme.ViewUtils;
import com.yht.yihuantong.ui.login.LoginOptionsActivity;
import com.yht.yihuantong.ui.main.MainActivity;

/**
 * @author 顿顿
 * @date 19/7/17 19:40
 * @description
 */
public class IntentParseActivity extends AppCompatActivity {
    public static final String PACKAGE_NAME = BuildConfig.APPLICATION_ID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_intent_parse);
        Intent intent = getIntent();
        String action = intent.getAction();
        Uri uri = intent.getData();
        try {
            if (Intent.ACTION_VIEW.equals(action) && uri != null) {
                Intent resultIntent = SmsJumpUtils.parseIntent(this, uri.toString(), null,
                                                               ZycApplication.getInstance().getLoginBean());
                if (resultIntent == null) {
                    finish();
                    return;
                }
                //                Activity curActivity = AppManager.getInstance().getCurrentActivity();
                if (ViewUtils.isLaunchedActivity(this, MainActivity.class)) {
                    //                        if (curActivity != null) {
                    //                            curActivity.finish();
                    //                            resultIntent.setComponent(curActivity.getComponentName());
                    //                        }
                    startActivity(resultIntent);
                }
                else if (ViewUtils.isLaunchedActivity(this, LoginOptionsActivity.class)) {
                    //                        if (curActivity != null) {
                    //                            curActivity.finish();
                    //                            resultIntent.setComponent(curActivity.getComponentName());
                    //                        }
                    startActivity(resultIntent);
                }
                else {
                    resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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
