package com.yht.yihuantong.ui.transfer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yht.frame.data.CommonData;
import com.yht.frame.data.Tasks;
import com.yht.frame.data.base.PatientBean;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.widgets.recyclerview.decoration.TimeItemDecoration;
import com.yht.frame.widgets.recyclerview.loadview.CustomLoadMoreView;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ui.adapter.TransferHistoryAdapter;
import com.yht.yihuantong.ui.reservation.ReservationCheckOrTransferActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/6/13 15:26
 * @des 转诊列表
 */
public class TransferHistoryActivity extends BaseActivity
        implements BaseQuickAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener,
                   BaseQuickAdapter.RequestLoadMoreListener {
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.layout_refresh)
    SwipeRefreshLayout layoutRefresh;
    /**
     * 时间分隔
     */
    private TimeItemDecoration timeItemDecoration;
    private TransferHistoryAdapter transferHistoryAdapter;
    private List<PatientBean> transferList;
    private List<String> titleBars;

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
        initData();
        initAdapter();
    }

    /**
     * 适配器处理
     */
    private void initAdapter() {
        transferHistoryAdapter = new TransferHistoryAdapter(R.layout.item_transfer_history, transferList);
        transferHistoryAdapter.setLoadMoreView(new CustomLoadMoreView());
        transferHistoryAdapter.setOnLoadMoreListener(this, recyclerview);
        transferHistoryAdapter.setOnItemClickListener(this);
        transferHistoryAdapter.loadMoreEnd();
        recyclerview.setAdapter(transferHistoryAdapter);
    }

    @Override
    public void initListener() {
        super.initListener();
        layoutRefresh.setOnRefreshListener(this);
    }

    /**
     * 数据处理
     */
    private void initData() {
        transferList = new ArrayList<>();
        titleBars = new ArrayList<>();
        String[] names = {
                "孙尚香", "安其拉", "白起", "不知火舞" };
        for (int i = 0; i < 4; i++) {
            PatientBean bean = new PatientBean();
            bean.setName(names[i]);
            if (i > 1) {
                titleBars.add("2019-06");
                bean.setIndexTag("2019-06");
            }
            else {
                titleBars.add("2019-07");
                bean.setIndexTag("2019-07");
            }
            transferList.add(bean);
        }
        //返回一个包含所有Tag字符串并赋值给tagsStr
        String tagsStr = BaseUtils.getTags(transferList);
        timeItemDecoration.setTitleBar(titleBars, tagsStr);
    }

    @OnClick(R.id.tv_check_next)
    public void onViewClicked() {
        Intent intent = new Intent(this, ReservationCheckOrTransferActivity.class);
        intent.putExtra(CommonData.KEY_CHECK_OR_TRANSFER, true);
        startActivity(intent);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        startActivity(new Intent(this, TransferToDetailActivity.class));
    }

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
    }

    /**
     * 加载更多
     */
    @Override
    public void onLoadMoreRequested() {
    }

    @Override
    public void onResponseEnd(Tasks task) {
        super.onResponseEnd(task);
        layoutRefresh.setRefreshing(false);
    }
}
