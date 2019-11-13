package com.yht.yihuantong.ui.hint;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.yht.frame.api.LitePalHelper;
import com.yht.frame.data.CommonData;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.utils.SharePreferenceUtil;
import com.yht.yihuantong.R;

import org.litepal.LitePal;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

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
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);

        //清除本地数据
        SharePreferenceUtil.clear(this);
        //清除数据库数据
        LitePal.deleteDatabase(LitePalHelper.DATA_BASE_NAME);
        //极光推送
        JPushInterface.deleteAlias(this, BASE_ONE);
        //删除环信会话列表
        Map<String, EMConversation> conversations =
                EMClient.getInstance().chatManager().getAllConversations();
        //删除和某个user会话，如果需要保留聊天记录，传false
        for (EMConversation conversation : conversations.values()) {
            EMClient.getInstance().chatManager().deleteConversation(conversation.conversationId()
                    , true);
        }
        //退出环信
        EMClient.getInstance().logout(true);
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
