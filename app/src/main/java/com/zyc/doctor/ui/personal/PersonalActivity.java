package com.zyc.doctor.ui.personal;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.yht.frame.ui.BaseActivity;
import com.zyc.doctor.R;
import com.zyc.doctor.ui.adapter.PersonalHistoryAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/6/10 11:01
 * @des 个人中心
 */
public class PersonalActivity extends BaseActivity {
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.swiperefreshlayout)
    SwipeRefreshLayout swiperefreshlayout;
    @BindView(R.id.public_title_bar_right_img)
    ImageView publicTitleBarRightImg;
    /**
     * 历史记录
     */
    private PersonalHistoryAdapter personalHistoryAdapter;
    private List<String> data;

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.public_recyclerview_layout1;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        publicTitleBarRightImg.setVisibility(View.VISIBLE);
        data = new ArrayList<String>() {
            {
                add("a");
                add("a");
                add("a");
                add("a");
                add("a");
                add("a");
                add("a");
                add("a");
                add("a");
                add("a");
                add("a");
                add("a");
                add("a");
                add("a");
                add("a");
            }
        };
        personalHistoryAdapter = new PersonalHistoryAdapter(R.layout.item_personal_history, data);
        View view = getLayoutInflater().inflate(R.layout.view_personal_header, null);
        personalHistoryAdapter.addHeaderView(view);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.setAdapter(personalHistoryAdapter);
    }

    @OnClick(R.id.public_title_bar_right_img)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.public_title_bar_right_img:
                startActivity(new Intent(this, SettingActivity.class));
                break;
            default:
                break;
        }
    }
}
