package com.yht.yihuantong.ui.remote;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yht.frame.data.BaseData;
import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.CommonData;
import com.yht.frame.data.Tasks;
import com.yht.frame.data.bean.CheckBean;
import com.yht.frame.data.bean.ReservationValidateBean;
import com.yht.frame.http.retrofit.RequestUtils;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.widgets.LoadViewHelper;
import com.yht.frame.widgets.recyclerview.loadview.CustomLoadMoreView;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ui.adapter.RemoteHistoryAdapter;
import com.yht.yihuantong.ui.reservation.ReservationDisableActivity;
import com.yht.yihuantong.ui.reservation.service.ReservationServiceActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/6/13 15:26
 * @des 发起会诊历史
 */
public class RemoteHistoryActivity extends BaseActivity
        implements BaseQuickAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener,
                   BaseQuickAdapter.RequestLoadMoreListener, LoadViewHelper.OnNextClickListener {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.layout_refresh)
    SwipeRefreshLayout layoutRefresh;
    @BindView(R.id.layout_none)
    LinearLayout layoutNone;
    @BindView(R.id.public_title_bar_title)
    TextView publicTitleBarTitle;
    @BindView(R.id.layout_reserve_service)
    LinearLayout layoutReserveService;
    private RemoteHistoryAdapter remoteHistoryAdapter;
    private List<CheckBean> checkedList = new ArrayList<>();
    /**
     * 是否能发起服务
     */
    private boolean applyServiceAble = false;
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
        return R.layout.act_remote_history;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        loadViewHelper = new LoadViewHelper(this);
        loadViewHelper.setOnNextClickListener(this);
        int num = 0;
        if (getIntent() != null) {
            num = getIntent().getIntExtra(CommonData.KEY_PUBLIC, 0);
        }
        if (num > 0) {
            publicTitleBarTitle.setText(String.format(getString(R.string.title_add_service), num));
        }
        else {
            publicTitleBarTitle.setText(R.string.txt_initiate_check);
        }
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
            getReserveCheckOrderList(true);
            getValidateHospitalList();
        }
        else {
            layoutReserveService.setVisibility(View.GONE);
            layoutNone.setVisibility(View.VISIBLE);
            loadViewHelper.load(LoadViewHelper.NONE_NETWORK);
        }
    }

    /**
     * 获取订单列表
     */
    private void getReserveCheckOrderList(boolean showLoading) {
        RequestUtils.getReserveCheckOrderList(this, loginBean.getToken(), BaseData.BASE_PAGE_DATA_NUM, page,
                                              showLoading, this);
    }

    /**
     * 校验医生是否有预约检查和预约转诊的合作医院
     */
    private void getValidateHospitalList() {
        RequestUtils.getValidateHospitalList(this, loginBean.getToken(), this);
    }

    /**
     * 适配器处理
     */
    private void initAdapter() {
        remoteHistoryAdapter = new RemoteHistoryAdapter(R.layout.item_check_history, checkedList);
        remoteHistoryAdapter.setLoadMoreView(new CustomLoadMoreView());
        remoteHistoryAdapter.setOnLoadMoreListener(this, recyclerView);
        remoteHistoryAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(remoteHistoryAdapter);
    }

    @OnClick(R.id.tv_remote_next)
    public void onViewClicked() {
        if (applyServiceAble) {
            startActivity(new Intent(this, ReservationServiceActivity.class));
        }
        else {
            startActivity(new Intent(this, ReservationDisableActivity.class));
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Intent intent = new Intent(this, RemoteDetailActivity.class);
        intent.putExtra(CommonData.KEY_ORDER_ID, checkedList.get(position).getOrderNo());
        startActivity(intent);
    }

    @Override
    public void onNextClick() {
        getReserveCheckOrderList(true);
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        switch (task) {
            case GET_RESERVE_CHECK_ORDER_LIST:
                layoutReserveService.setVisibility(View.VISIBLE);
                layoutNone.setVisibility(View.GONE);
                List<CheckBean> list = (List<CheckBean>)response.getData();
                if (page == BaseData.BASE_ONE) {
                    checkedList.clear();
                }
                checkedList.addAll(list);
                remoteHistoryAdapter.setNewData(checkedList);
                if (list != null && list.size() >= BaseData.BASE_PAGE_DATA_NUM) {
                    remoteHistoryAdapter.loadMoreComplete();
                }
                else {
                    if (checkedList.size() > BaseData.BASE_PAGE_DATA_NUM) {
                        remoteHistoryAdapter.loadMoreEnd();
                    }
                    else {
                        remoteHistoryAdapter.setEnableLoadMore(false);
                    }
                }
                if (checkedList != null && checkedList.size() > 0) {
                    recyclerView.setVisibility(View.VISIBLE);
                    layoutNone.setVisibility(View.GONE);
                }
                else {
                    recyclerView.setVisibility(View.GONE);
                    layoutNone.setVisibility(View.VISIBLE);
                    loadViewHelper.load(LoadViewHelper.NONE_RECORDING);
                }
                break;
            case GET_VALIDATE_HOSPITAL_LIST:
                ReservationValidateBean bean = (ReservationValidateBean)response.getData();
                if (bean != null) {
                    applyServiceAble = bean.isJc();
                }
                break;
            default:
                break;
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
        getReserveCheckOrderList(false);
    }

    /**
     * 加载更多
     */
    @Override
    public void onLoadMoreRequested() {
        page++;
        getReserveCheckOrderList(false);
    }
}
