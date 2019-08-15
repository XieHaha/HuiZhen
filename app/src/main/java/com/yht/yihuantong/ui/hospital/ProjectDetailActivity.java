package com.yht.yihuantong.ui.hospital;

import android.widget.TextView;

import com.yht.frame.ui.BaseActivity;
import com.yht.frame.widgets.textview.JustifiedTextView;
import com.yht.yihuantong.R;

import butterknife.BindView;

/**
 * @author 顿顿
 * @date 19/8/15 14:05
 * @des 医院服务详情
 */
public class ProjectDetailActivity extends BaseActivity {
    @BindView(R.id.public_title_bar_title)
    TextView publicTitleBarTitle;
    @BindView(R.id.tv_project_introduction)
    JustifiedTextView tvProjectIntroduction;
    @BindView(R.id.tv_project_notice)
    JustifiedTextView tvProjectNotice;

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_project_detail;
    }
}
