package com.yht.yihuantong.ui.transfer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yht.frame.data.BaseData;
import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.CommonData;
import com.yht.frame.data.Tasks;
import com.yht.frame.data.base.TransferBean;
import com.yht.frame.http.retrofit.RequestUtils;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.widgets.recyclerview.decoration.TimeItemDecoration;
import com.yht.frame.widgets.recyclerview.loadview.CustomLoadMoreView;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ui.adapter.TransferInitiateAdapter;
import com.yht.yihuantong.ui.reservation.ReservationCheckOrTransferActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/6/13 15:26
 * @des 我发起的转诊
 */
public class TransferInitiateListActivity extends BaseActivity
        implements BaseQuickAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener,
                   BaseQuickAdapter.RequestLoadMoreListener {
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.layout_refresh)
    SwipeRefreshLayout layoutRefresh;
    @BindView(R.id.public_title_bar_title)
    TextView publicTitleBarTitle;
    /**
     * 时间分隔
     */
    private TimeItemDecoration timeItemDecoration;
    private TransferInitiateAdapter transferInitiateAdapter;
    /**
     * 我发起的转诊列表
     */
    private List<TransferBean> transferList = new ArrayList<>();
    /**
     * 时间title
     */
    private List<String> titleBars = new ArrayList<>();
    /**
     * 页码
     */
    private int page = 1;
    /**
     * 订单是否更新
     */
    private static final int REQUEST_CODE_UPDATE = 100;

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_transfer_history;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        layoutRefresh.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light,
                                              android.R.color.holo_orange_light, android.R.color.holo_green_light);
        timeItemDecoration = new TimeItemDecoration(this, false);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.addItemDecoration(timeItemDecoration);
        initAdapter();
        getInitiateTransferOrderList();
    }

    /**
     * 适配器处理
     */
    private void initAdapter() {
        transferInitiateAdapter = new TransferInitiateAdapter(R.layout.item_transfer_history, transferList);
        transferInitiateAdapter.setLoadMoreView(new CustomLoadMoreView());
        transferInitiateAdapter.setOnLoadMoreListener(this, recyclerview);
        transferInitiateAdapter.setOnItemClickListener(this);
        transferInitiateAdapter.loadMoreEnd();
        recyclerview.setAdapter(transferInitiateAdapter);
    }

    @Override
    public void initListener() {
        super.initListener();
        layoutRefresh.setOnRefreshListener(this);
    }

    /**
     * 查询发起的转诊记录
     */
    private void getInitiateTransferOrderList() {
        RequestUtils.getInitiateTransferOrderList(this, loginBean.getToken(), BaseData.BASE_PAGE_DATA_NUM, page, this);
    }

    /**
     * 数据处理 排序
     */
    private void sortTransferData() {
        titleBars.clear();
        for (TransferBean bean : transferList) {
            titleBars.add(bean.getTransferDate());
        }
        //返回一个包含所有Tag字符串并赋值给tagsStr
        String tag = BaseUtils.getTransferTimeTags(transferList);
        timeItemDecoration.setTitleBar(titleBars, tag);
    }

    @OnClick(R.id.tv_check_next)
    public void onViewClicked() {
        Intent intent = new Intent(this, ReservationCheckOrTransferActivity.class);
        intent.putExtra(CommonData.KEY_CHECK_OR_TRANSFER, true);
        startActivity(intent);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Intent intent = new Intent(this, TransferInitiateDetailActivity.class);
        intent.putExtra(CommonData.KEY_TRANSFER_ORDER_BEAN, transferList.get(position));
        startActivityForResult(intent, REQUEST_CODE_UPDATE);
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        if (task == Tasks.GET_INITIATE_TRANSFER_ORDER_LIST) {
            List<TransferBean> list = (List<TransferBean>)response.getData();
            if (page == BaseData.BASE_ONE) {
                transferList.clear();
            }
            transferList.addAll(list);
            publicTitleBarTitle.setText(String.format(getString(R.string.title_add_transfer), transferList.size()));
            sortTransferData();
            transferInitiateAdapter.setNewData(transferList);
            if (list != null && list.size() == BaseData.BASE_PAGE_DATA_NUM) {
                transferInitiateAdapter.loadMoreComplete();
            }
            else {
                transferInitiateAdapter.loadMoreEnd();
            }
        }
    }

    @Override
    public void onResponseEnd(Tasks task) {
        super.onResponseEnd(task);
        layoutRefresh.setRefreshing(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_UPDATE) {
            getInitiateTransferOrderList();
        }
    }

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        page = 1;
        getInitiateTransferOrderList();
    }

    /**
     * 加载更多
     */
    @Override
    public void onLoadMoreRequested() {
        page++;
        getInitiateTransferOrderList();
    }
}
