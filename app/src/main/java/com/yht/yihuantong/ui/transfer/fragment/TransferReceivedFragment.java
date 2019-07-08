package com.yht.yihuantong.ui.transfer.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yht.frame.data.CommonData;
import com.yht.frame.data.base.PatientBean;
import com.yht.frame.ui.BaseFragment;
import com.yht.frame.widgets.recyclerview.loadview.CustomLoadMoreView;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ui.adapter.TransferReceivedAdapter;
import com.yht.yihuantong.ui.transfer.TransferFromDetailActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author 顿顿
 * @date 19/5/17 14:55
 * @des 转诊已接收
 */
public class TransferReceivedFragment extends BaseFragment
        implements BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener,
                   BaseQuickAdapter.OnItemClickListener {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.layout_refresh)
    SwipeRefreshLayout layoutRefresh;
    private TransferReceivedAdapter transferReceivedAdapter;
    /**
     * 已接收转诊患者
     */
    private List<PatientBean> data;

    @Override
    public int getLayoutID() {
        return R.layout.fragment_transfer_received;
    }

    @Override
    public void initView(View view, @NonNull Bundle savedInstanceState) {
        super.initView(view, savedInstanceState);
        layoutRefresh.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light,
                                              android.R.color.holo_orange_light, android.R.color.holo_green_light);
        layoutRefresh.setOnRefreshListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        transferReceivedAdapter = new TransferReceivedAdapter(R.layout.item_received_patient, data);
        transferReceivedAdapter.setOnItemClickListener(this);
        transferReceivedAdapter.setLoadMoreView(new CustomLoadMoreView());
        transferReceivedAdapter.setOnLoadMoreListener(this, recyclerView);
        recyclerView.setAdapter(transferReceivedAdapter);
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        data = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            PatientBean bean = new PatientBean();
            bean.setName("患者--" + i);
            data.add(bean);
        }
        transferReceivedAdapter.setNewData(data);
        transferReceivedAdapter.loadMoreEnd();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Intent intent = new Intent(getContext(), TransferFromDetailActivity.class);
        intent.putExtra(CommonData.KEY_IS_RECEIVE_TRANSFER, true);
        startActivity(intent);
    }

    @Override
    public void onLoadMoreRequested() {
    }

    @Override
    public void onRefresh() {
    }
}
