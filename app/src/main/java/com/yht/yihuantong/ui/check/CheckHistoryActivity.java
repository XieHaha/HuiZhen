package com.yht.yihuantong.ui.check;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yht.frame.data.BaseData;
import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.CommonData;
import com.yht.frame.data.Tasks;
import com.yht.frame.data.base.CheckBean;
import com.yht.frame.http.retrofit.RequestUtils;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.widgets.recyclerview.decoration.TimeItemDecoration;
import com.yht.frame.widgets.recyclerview.loadview.CustomLoadMoreView;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ui.adapter.CheckHistoryAdapter;
import com.yht.yihuantong.ui.reservation.ReservationCheckOrTransferActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/6/13 15:26
 * @des
 */
public class CheckHistoryActivity extends BaseActivity
        implements BaseQuickAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener,
                   BaseQuickAdapter.RequestLoadMoreListener {
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.layout_refresh)
    SwipeRefreshLayout layoutRefresh;
    private CheckHistoryAdapter checkHistoryAdapter;
    /**
     * 时间分隔
     */
    private TimeItemDecoration timeItemDecoration;
    private List<CheckBean> checkedList = new ArrayList<>();
    private List<String> titleBars = new ArrayList<>();
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
        return R.layout.act_check_history;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        layoutRefresh.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light,
                                              android.R.color.holo_orange_light, android.R.color.holo_green_light);
        layoutRefresh.setOnRefreshListener(this);
        timeItemDecoration = new TimeItemDecoration(this, false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(timeItemDecoration);
        initAdapter();
        getReserveCheckOrderList();
    }

    /**
     * 获取订单列表
     */
    private void getReserveCheckOrderList() {
        RequestUtils.getReserveCheckOrderList(this, loginBean.getToken(), BaseData.BASE_PAGE_DATA_NUM, page, this);
    }

    /**
     * 适配器处理
     */
    private void initAdapter() {
        checkHistoryAdapter = new CheckHistoryAdapter(R.layout.item_check_history, checkedList);
        checkHistoryAdapter.setLoadMoreView(new CustomLoadMoreView());
        checkHistoryAdapter.setOnLoadMoreListener(this, recyclerView);
        checkHistoryAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(checkHistoryAdapter);
    }

    /**
     * 数据处理 排序
     */
    private void sortTransferData() {
        titleBars.clear();
        for (CheckBean bean : checkedList) {
            String time = BaseUtils.formatDate(bean.getCreateAt(), BaseUtils.YYYY_MM_DD);
            titleBars.add(time);
        }
        //返回一个包含所有Tag字符串并赋值给tagsStr
        String tag = BaseUtils.getTimeTags(titleBars);
        timeItemDecoration.setTitleBar(titleBars, tag);
    }

    @OnClick(R.id.tv_check_next)
    public void onViewClicked() {
        startActivity(new Intent(this, ReservationCheckOrTransferActivity.class));
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Intent intent = new Intent(this, CheckDetailActivity.class);
        intent.putExtra(CommonData.KEY_ORDER_ID, checkedList.get(position).getOrderNo());
        startActivity(intent);
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        switch (task) {
            case GET_RESERVE_CHECK_ORDER_LIST:
                List<CheckBean> list = (List<CheckBean>)response.getData();
                if (page == BaseData.BASE_ONE) {
                    checkedList.clear();
                }
                checkedList.addAll(list);
                sortTransferData();
                checkHistoryAdapter.setNewData(checkedList);
                if (list != null && list.size() == BaseData.BASE_PAGE_DATA_NUM) {
                    checkHistoryAdapter.loadMoreComplete();
                }
                else {
                    checkHistoryAdapter.loadMoreEnd();
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
        getReserveCheckOrderList();
    }

    /**
     * 加载更多
     */
    @Override
    public void onLoadMoreRequested() {
        page++;
        getReserveCheckOrderList();
    }
}
