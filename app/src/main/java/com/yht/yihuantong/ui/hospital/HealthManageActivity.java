package com.yht.yihuantong.ui.hospital;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yht.frame.data.BaseData;
import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.CommonData;
import com.yht.frame.data.Tasks;
import com.yht.frame.data.bean.BaseListBean;
import com.yht.frame.data.bean.HealthPackageBean;
import com.yht.frame.http.retrofit.RequestUtils;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.widgets.LoadViewHelper;
import com.yht.frame.widgets.recyclerview.loadview.CustomLoadMoreView;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ui.adapter.HealthManageAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author 顿顿
 * @date 19/9/26 10:30
 * @description 健康管理
 */
public class HealthManageActivity extends BaseActivity
        implements BaseQuickAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener,
                   BaseQuickAdapter.RequestLoadMoreListener, LoadViewHelper.OnNextClickListener {
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.layout_refresh)
    SwipeRefreshLayout layoutRefresh;
    @BindView(R.id.layout_none)
    LinearLayout layoutNone;
    private HealthManageAdapter healthManageAdapter;
    private List<HealthPackageBean> healthPackageBeans = new ArrayList<>();
    /**
     * 页码
     */
    private int page = 1;

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_health_manage;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        loadViewHelper = new LoadViewHelper(this);
        loadViewHelper.setOnNextClickListener(this);
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        layoutRefresh.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light,
                                              android.R.color.holo_orange_light, android.R.color.holo_green_light);
        layoutRefresh.setOnRefreshListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        initAdapter();
        if (BaseUtils.isNetworkAvailable(this)) {
            queryPackageList(true);
        }
        else {
            layoutNone.setVisibility(View.VISIBLE);
            loadViewHelper.load(LoadViewHelper.NONE_NETWORK);
        }
    }

    /**
     * 获取健康管理列表
     */
    private void queryPackageList(boolean showLoading) {
        RequestUtils.queryPackageList(this, loginBean.getToken(), BaseData.BASE_PAGE_DATA_NUM, page, showLoading, this);
    }

    /**
     * 适配器处理
     */
    private void initAdapter() {
        healthManageAdapter = new HealthManageAdapter(R.layout.item_health_manage, healthPackageBeans);
        healthManageAdapter.setLoadMoreView(new CustomLoadMoreView());
        healthManageAdapter.setOnLoadMoreListener(this, recyclerView);
        healthManageAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(healthManageAdapter);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Intent intent = new Intent(this, ServicePackageDetailActivity.class);
        intent.putExtra(CommonData.KEY_ORDER_ID, healthPackageBeans.get(position).getPackageCode());
        startActivity(intent);
    }

    @Override
    public void onNextClick() {
        queryPackageList(true);
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        if (task == Tasks.QUERY_PACKAGE_LIST) {
            layoutNone.setVisibility(View.GONE);
            BaseListBean<HealthPackageBean> baseListBean = (BaseListBean<HealthPackageBean>)response.getData();
            List<HealthPackageBean> list = baseListBean.getRecords();
            if (page == BaseData.BASE_ONE) {
                healthPackageBeans.clear();
            }
            healthPackageBeans.addAll(list);
            healthManageAdapter.setNewData(healthPackageBeans);
            if (list != null && list.size() >= BaseData.BASE_PAGE_DATA_NUM) {
                healthManageAdapter.loadMoreComplete();
            }
            else {
                if (healthPackageBeans.size() > BaseData.BASE_PAGE_DATA_NUM) {
                    healthManageAdapter.loadMoreEnd();
                }
                else {
                    healthManageAdapter.setEnableLoadMore(false);
                }
            }
            if (healthPackageBeans != null && healthPackageBeans.size() > 0) {
                recyclerView.setVisibility(View.VISIBLE);
                layoutNone.setVisibility(View.GONE);
            }
            else {
                recyclerView.setVisibility(View.GONE);
                layoutNone.setVisibility(View.VISIBLE);
                loadViewHelper.load(LoadViewHelper.NONE_RECORDING);
            }
        }
    }

    @Override
    public void onResponseEnd(Tasks task) {
        super.onResponseEnd(task);
        layoutRefresh.setRefreshing(false);
    }

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        page = 1;
        queryPackageList(false);
    }

    /**
     * 加载更多
     */
    @Override
    public void onLoadMoreRequested() {
        page++;
        queryPackageList(false);
    }
}
