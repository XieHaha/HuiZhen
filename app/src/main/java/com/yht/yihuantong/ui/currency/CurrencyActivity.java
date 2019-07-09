package com.yht.yihuantong.ui.currency;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yht.frame.data.BaseData;
import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.CommonData;
import com.yht.frame.data.CurrencyDetailType;
import com.yht.frame.data.Tasks;
import com.yht.frame.data.base.DoctorCurrencyBean;
import com.yht.frame.data.base.DoctorCurrencyDetailBean;
import com.yht.frame.http.retrofit.RequestUtils;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.widgets.recyclerview.loadview.CustomLoadMoreView;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ui.adapter.CurrencyDetailAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author 顿顿
 * @date 19/6/10 15:33
 * @des 会珍币管理
 */
public class CurrencyActivity extends BaseActivity
        implements BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemClickListener, CurrencyDetailType {
    @BindView(R.id.public_title_bar_title)
    TextView publicTitleBarTitle;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.tv_arrived)
    TextView tvArrived;
    @BindView(R.id.tv_total)
    TextView tvTotal;
    private DoctorCurrencyBean doctorCurrencyBean;
    /**
     * 适配器
     */
    private CurrencyDetailAdapter currencyIncomeAdapter;
    /**
     * 收入列表
     */
    private List<DoctorCurrencyDetailBean> doctorCurrencyDetailBeans = new ArrayList<>();
    /**
     * 查询所有
     */
    private boolean showAll;
    /**
     * 页码 默认第一页
     */
    private int page = 1;

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_currency;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        currencyIncomeAdapter = new CurrencyDetailAdapter(R.layout.item_income, doctorCurrencyDetailBeans);
        View view = getLayoutInflater().inflate(R.layout.view_space, null);
        currencyIncomeAdapter.setOnItemClickListener(this);
        currencyIncomeAdapter.addHeaderView(view);
        currencyIncomeAdapter.setLoadMoreView(new CustomLoadMoreView());
        currencyIncomeAdapter.setOnLoadMoreListener(this, recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.setAdapter(currencyIncomeAdapter);
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        if (getIntent() != null) {
            showAll = getIntent().getBooleanExtra(CommonData.KEY_SHOW_ALL, false);
            doctorCurrencyBean = (DoctorCurrencyBean)getIntent().getSerializableExtra(
                    CommonData.KEY_DOCTOR_CURRENCY_BEAN);
        }
        if (showAll) {
            publicTitleBarTitle.setText(R.string.title_total_income);
            tvArrived.setText(doctorCurrencyBean.getArrived());
            tvTotal.setText(String.format(getString(R.string.txt_expected_income), doctorCurrencyBean.getTotal()));
            getDoctorIncomeWithOutList();
        }
        else {
            publicTitleBarTitle.setText(R.string.title_month_income);
            tvArrived.setText(doctorCurrencyBean.getCurMonthArrived());
            tvTotal.setText(
                    String.format(getString(R.string.txt_expected_income), doctorCurrencyBean.getCurMonthTotal()));
            getDoctorIncomeByMonthList();
        }
    }

    /**
     * 医生所有收入明细列表
     */
    private void getDoctorIncomeWithOutList() {
        RequestUtils.getDoctorIncomeWithOutList(this, loginBean.getToken(), BaseData.BASE_PAGE_DATA_NUM, page, this);
    }

    /**
     * 医生当前月收入明细列表
     */
    private void getDoctorIncomeByMonthList() {
        RequestUtils.getDoctorIncomeByMonthList(this, loginBean.getToken(), 1, BaseData.BASE_PAGE_DATA_NUM, page, this);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Intent intent;
        DoctorCurrencyDetailBean bean = doctorCurrencyDetailBeans.get(position);
        int type = bean.getServiceFlag();
        switch (type) {
            case CURRENCY_DETAIL_TYPE_WITHDRAW:
                intent = new Intent(this, WithdrawDetailActivity.class);
                intent.putExtra(CommonData.KEY_DOCTOR_CURRENCY_ID, bean.getDoctorOrderTranId());
                startActivity(intent);
                break;
            case CURRENCY_DETAIL_TYPE_CHECK:
            case CURRENCY_DETAIL_TYPE_TRANSFER:
            case CURRENCY_DETAIL_TYPE_REMOTE:
                intent = new Intent(this, IncomeDetailActivity.class);
                intent.putExtra(CommonData.KEY_DOCTOR_CURRENCY_ID, bean.getDoctorOrderTranId());
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        switch (task) {
            case GET_DOCTOR_INCOME_WITHOUT_LIST:
            case GET_DOCTOR_INCOME_BY_MONTH_LIST:
                List<DoctorCurrencyDetailBean> list = (List<DoctorCurrencyDetailBean>)response.getData();
                if (page == BaseData.BASE_ONE) {
                    doctorCurrencyDetailBeans.clear();
                }
                doctorCurrencyDetailBeans.addAll(list);
                currencyIncomeAdapter.setNewData(doctorCurrencyDetailBeans);
                if (list.size() < BaseData.BASE_PAGE_DATA_NUM) {
                    currencyIncomeAdapter.loadMoreEnd();
                }
                else {
                    currencyIncomeAdapter.loadMoreComplete();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onLoadMoreRequested() {
        page++;
        if (showAll) {
            getDoctorIncomeWithOutList();
        }
        else {
            getDoctorIncomeByMonthList();
        }
    }
}
