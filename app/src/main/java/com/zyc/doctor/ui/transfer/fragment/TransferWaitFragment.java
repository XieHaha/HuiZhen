package com.zyc.doctor.ui.transfer.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yht.frame.data.bean.PatientBean;
import com.yht.frame.ui.BaseFragment;
import com.yht.frame.widgets.recyclerview.loadview.CustomLoadMoreView;
import com.zyc.doctor.R;
import com.zyc.doctor.ui.adapter.TransferWaitAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author 顿顿
 * @date 19/5/17 14:55
 * @des 转诊待处理
 */
public class TransferWaitFragment extends BaseFragment
        implements BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.layout_refresh)
    SwipeRefreshLayout layoutRefresh;
    private TransferWaitAdapter transferWaitAdapter;
    /**
     * 已接收转诊患者
     */
    private List<PatientBean> data;

    @Override
    public int getLayoutID() {
        return R.layout.fragment_transfer_wait;
    }

    @Override
    public void initView(View view, @NonNull Bundle savedInstanceState) {
        super.initView(view, savedInstanceState);
        layoutRefresh.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light,
                                              android.R.color.holo_orange_light, android.R.color.holo_green_light);
        layoutRefresh.setOnRefreshListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        transferWaitAdapter = new TransferWaitAdapter(R.layout.item_transfer_wait, data);
        transferWaitAdapter.setLoadMoreView(new CustomLoadMoreView());
        transferWaitAdapter.setOnLoadMoreListener(this, recyclerView);
        recyclerView.setAdapter(transferWaitAdapter);
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
        transferWaitAdapter.setNewData(data);
        transferWaitAdapter.loadMoreEnd();
    }

    @Override
    public void onLoadMoreRequested() {
    }

    @Override
    public void onRefresh() {
    }
}
