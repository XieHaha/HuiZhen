package com.yht.yihuantong.ui.hospital;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.Tasks;
import com.yht.frame.data.bean.HospitalBean;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.widgets.recyclerview.loadview.CustomLoadMoreView;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ui.adapter.CooperateHospitalAdapter;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * @author 顿顿
 * @date 19/8/12 15:34
 * @des 合作医院列表
 */
public class CooperateHospitalListActivity extends BaseActivity
        implements BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemClickListener,
                   SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    private CooperateHospitalAdapter cooperateHospitalAdapter;
    private ArrayList<HospitalBean> hospitalBeans = new ArrayList<>();
    /**
     * 默认第一页
     */
    private int page = 1;

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_cooperate_hospital;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light,
                                              android.R.color.holo_orange_light, android.R.color.holo_green_light);
        cooperateHospitalAdapter = new CooperateHospitalAdapter(R.layout.item_cooperate_hospital, hospitalBeans);
        cooperateHospitalAdapter.setLoadMoreView(new CustomLoadMoreView());
        cooperateHospitalAdapter.setOnLoadMoreListener(this, recyclerView);
        cooperateHospitalAdapter.setOnItemClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(cooperateHospitalAdapter);
    }

    @Override
    public void initListener() {
        super.initListener();
        refreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void fillNetWorkData() {
        super.fillNetWorkData();
        getCooperateHospital(true);
        tempData();
    }

    private void tempData() {
        for (int i = 0; i < 5; i++) {
            HospitalBean bean = new HospitalBean();
            bean.setHospitalName("医院->" + i);
            if (i < 3) {
                bean.setSettleStatus(0);
            }
            else {
                bean.setSettleStatus(1);
            }
            hospitalBeans.add(bean);
        }
        cooperateHospitalAdapter.setNewData(hospitalBeans);
    }

    /**
     * 获取合作医院
     */
    private void getCooperateHospital(boolean show) {
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
    }

    @Override
    public void onRefresh() {
        page = 1;
        getCooperateHospital(false);
    }

    @Override
    public void onLoadMoreRequested() {
        page++;
        getCooperateHospital(false);
    }
}
