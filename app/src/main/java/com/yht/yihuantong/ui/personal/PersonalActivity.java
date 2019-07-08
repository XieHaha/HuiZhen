package com.yht.yihuantong.ui.personal;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.CommonData;
import com.yht.frame.data.Tasks;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.utils.glide.GlideHelper;
import com.yht.frame.widgets.recyclerview.loadview.CustomLoadMoreView;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ui.adapter.CurrencyIncomeAdapter;
import com.yht.yihuantong.ui.currency.CurrencyActivity;
import com.yht.yihuantong.utils.ImageUrlUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/6/10 11:01
 * @des 个人中心
 */
public class PersonalActivity extends BaseActivity implements BaseQuickAdapter.RequestLoadMoreListener {
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.public_title_bar_more)
    TextView publicTitleBarMore;
    @BindView(R.id.public_title_bar_title)
    TextView publicTitleBarTitle;
    /**
     * 头部控件
     */
    private TextView tvPersonalDepart, tvPersonalHospital, tvBalance, tvTotalIncome, tvTotal, tvMonthTotalIncome, tvMonthTotal;
    private ImageView ivPersonalImage, ivBalanceTab;
    private LinearLayout layoutTotalIncome, layoutMonthIncome;
    private RelativeLayout layoutPersonalBase;
    /**
     * 历史记录
     */
    private CurrencyIncomeAdapter currencyIncomeAdapter;
    private List<String> data;
    /**
     * 页码 默认第一页
     */
    private int page = 1;
    /**
     * 是否显示金额
     */
    private boolean show;

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_personal;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        publicTitleBarMore.setVisibility(View.VISIBLE);
        publicTitleBarMore.setText(R.string.title_setting);
        currencyIncomeAdapter = new CurrencyIncomeAdapter(R.layout.item_income, data);
        View view = getLayoutInflater().inflate(R.layout.view_personal_header, null);
        initHeaderView(view);
        currencyIncomeAdapter.addHeaderView(view);
        currencyIncomeAdapter.setLoadMoreView(new CustomLoadMoreView());
        currencyIncomeAdapter.setOnLoadMoreListener(this, recyclerview);
        currencyIncomeAdapter.loadMoreEnd();
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.setAdapter(currencyIncomeAdapter);
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        getDoctorBalanceInfo();
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
        tvBalance = view.findViewById(R.id.tv_balance);
        tvTotal = view.findViewById(R.id.tv_total);
        tvTotalIncome = view.findViewById(R.id.tv_total_income);
        tvMonthTotal = view.findViewById(R.id.tv_total_month);
        tvMonthTotalIncome = view.findViewById(R.id.tv_month_income);
        layoutTotalIncome = view.findViewById(R.id.layout_total_income);
        layoutMonthIncome = view.findViewById(R.id.layout_month_income);
        layoutPersonalBase = view.findViewById(R.id.layout_personal_base);
        ivBalanceTab.setOnClickListener(this);
        layoutTotalIncome.setOnClickListener(this);
        layoutMonthIncome.setOnClickListener(this);
        layoutPersonalBase.setOnClickListener(this);
        initBase();
        //获取本地
        show = sharePreferenceUtil.getBoolean(CommonData.KEY_SHOW_CURRENCY);
        initAmountDisplay();
    }

    /**
     * 基础信息展示
     */
    private void initBase() {
        publicTitleBarTitle.setText(loginBean.getDoctorName());
        tvPersonalDepart.setText(loginBean.getDepartmentName());
        tvPersonalHospital.setText(loginBean.getHospitalName());
        Glide.with(this)
             .load(ImageUrlUtil.append(loginBean.getPhoto()))
             .apply(GlideHelper.getOptions(BaseUtils.dp2px(this, 4)))
             .into(ivPersonalImage);
    }

    /**
     * 金额显示与隐藏处理
     */
    private void initAmountDisplay() {
        if (show) {
            ivBalanceTab.setSelected(true);
            tvBalance.setText("100");
            tvTotal.setText(String.format(getString(R.string.txt_personal_all_income), "1000"));
            tvTotalIncome.setText("100");
            tvMonthTotal.setText(String.format(getString(R.string.txt_personal_month_income), "1000"));
            tvMonthTotalIncome.setText("100");
        }
        else {
            ivBalanceTab.setSelected(false);
            tvBalance.setText(R.string.txt_star);
            tvTotal.setText(String.format(getString(R.string.txt_personal_all_income), getString(R.string.txt_star)));
            tvTotalIncome.setText(R.string.txt_star);
            tvMonthTotal.setText(
                    String.format(getString(R.string.txt_personal_month_income), getString(R.string.txt_star)));
            tvMonthTotalIncome.setText(R.string.txt_star);
        }
    }

    /**
     * 医生收入信息 预约检查+预约转诊+远程会珍
     */
    private void getDoctorBalanceInfo() {
        //        RequestUtils.getDoctorBalanceInfo(this, loginBean.getToken(), this);
        //        RequestUtils.getDoctorIncomeList(this, BaseData.BASE_PAGE_DATA_NUM, page, loginBean.getToken(), this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        Intent intent;
        switch (v.getId()) {
            case R.id.iv_balance_tab:
                sharePreferenceUtil.putBoolean(CommonData.KEY_SHOW_CURRENCY, !show);
                initAmountDisplay();
                break;
            case R.id.layout_total_income:
                intent = new Intent(this, CurrencyActivity.class);
                intent.putExtra(CommonData.KEY_PUBLIC, getString(R.string.title_total_income));
                startActivity(intent);
                break;
            case R.id.layout_month_income:
                intent = new Intent(this, CurrencyActivity.class);
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

    @OnClick({ R.id.public_title_bar_more })
    public void onViewClicked() {
        startActivity(new Intent(this, SettingActivity.class));
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        switch (task) {
            case GET_DOCTOR_BALANCE_INFO:
                break;
            default:
                break;
        }
    }

    /**
     * 加载更多
     */
    @Override
    public void onLoadMoreRequested() {
    }
}
