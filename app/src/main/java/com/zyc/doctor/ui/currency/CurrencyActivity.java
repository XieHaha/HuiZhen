package com.zyc.doctor.ui.currency;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yht.frame.data.CommonData;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.widgets.recyclerview.loadview.CustomLoadMoreView;
import com.zyc.doctor.R;
import com.zyc.doctor.ui.adapter.CurrencyIncomeAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author 顿顿
 * @date 19/6/10 15:33
 * @des 会珍币管理
 */
public class CurrencyActivity extends BaseActivity implements BaseQuickAdapter.RequestLoadMoreListener {
    @BindView(R.id.public_title_bar_title)
    TextView publicTitleBarTitle;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    /**
     * 适配器
     */
    private CurrencyIncomeAdapter currencyIncomeAdapter;
    /**
     * 收入列表
     */
    private List<String> data;
    private String title;

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_currency;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        if (getIntent() != null) {
            title = getIntent().getStringExtra(CommonData.KEY_PUBLIC);
            publicTitleBarTitle.setText(title);
        }
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
            }
        };
        currencyIncomeAdapter = new CurrencyIncomeAdapter(R.layout.item_income, data);
        View view = getLayoutInflater().inflate(R.layout.view_space, null);
        currencyIncomeAdapter.addHeaderView(view);
        currencyIncomeAdapter.setLoadMoreView(new CustomLoadMoreView());
        currencyIncomeAdapter.setOnLoadMoreListener(this, recyclerview);
        currencyIncomeAdapter.loadMoreEnd();
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.setAdapter(currencyIncomeAdapter);
    }

    @Override
    public void onLoadMoreRequested() {
    }
}
