package com.zyc.doctor.ui.personal;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yht.frame.data.CommonData;
import com.yht.frame.ui.BaseActivity;
import com.zyc.doctor.R;
import com.zyc.doctor.ui.adapter.CurrencyIncomeAdapter;
import com.zyc.doctor.ui.currency.IncomeActivity;

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
     * 头部控件
     */
    private TextView tvPersonalDepart, tvPersonalHospital, tvBalanceTotal, tvBalanceMonthTotal;
    private ImageView ivPersonalImage, ivBalanceTab;
    private LinearLayout layoutTotalIncome, layoutMonthIncome;
    private RelativeLayout layoutPersonalBase;
    /**
     * 历史记录
     */
    private CurrencyIncomeAdapter currencyIncomeAdapter;
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
        currencyIncomeAdapter = new CurrencyIncomeAdapter(R.layout.item_income, data);
        View view = getLayoutInflater().inflate(R.layout.view_personal_header, null);
        initHeaderView(view);
        currencyIncomeAdapter.addHeaderView(view);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.setAdapter(currencyIncomeAdapter);
    }

    /**
     * 头部控件初始化
     *
     * @param view
     */
    private void initHeaderView(View view) {
        tvPersonalDepart = view.findViewById(R.id.tv_personal_depart);
        tvPersonalHospital = view.findViewById(R.id.tv_personal_hospital);
        ivPersonalImage = view.findViewById(R.id.iv_personal_image);
        ivBalanceTab = view.findViewById(R.id.iv_balance_tab);
        tvBalanceTotal = view.findViewById(R.id.tv_balance_total);
        tvBalanceMonthTotal = view.findViewById(R.id.tv_balance_month_total);
        layoutTotalIncome = view.findViewById(R.id.layout_total_income);
        layoutMonthIncome = view.findViewById(R.id.layout_month_income);
        layoutPersonalBase = view.findViewById(R.id.layout_personal_base);
        layoutTotalIncome.setOnClickListener(this);
        layoutMonthIncome.setOnClickListener(this);
        layoutPersonalBase.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        Intent intent;
        switch (v.getId()) {
            case R.id.layout_total_income:
                intent = new Intent(this, IncomeActivity.class);
                intent.putExtra(CommonData.KEY_PUBLIC, getString(R.string.title_total_income));
                startActivity(intent);
                break;
            case R.id.layout_month_income:
                intent = new Intent(this, IncomeActivity.class);
                intent.putExtra(CommonData.KEY_PUBLIC, getString(R.string.title_month_income));
                startActivity(intent);
                break;
            case R.id.layout_personal_base:
                intent = new Intent(this, PersonalInfoActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @OnClick({ R.id.public_title_bar_right_img })
    public void onViewClicked() {
        startActivity(new Intent(this, SettingActivity.class));
    }
}
