package com.yht.yihuantong.ui.hospital;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.Tasks;
import com.yht.frame.data.bean.CheckTypeBean;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.widgets.recyclerview.loadview.CustomLoadMoreView;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ui.adapter.HospitalProjectAdapter;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * @author 顿顿
 * @date 19/8/12 15:34
 * @des 医院服务项
 */
public class HospitalProjectListActivity extends BaseActivity
        implements BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemClickListener,
                   SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    private HospitalProjectAdapter hospitalProjectAdapter;
    private ArrayList<CheckTypeBean> checkBeans = new ArrayList<>();
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
        return R.layout.act_hospital_project;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light,
                                              android.R.color.holo_orange_light, android.R.color.holo_green_light);
        hospitalProjectAdapter = new HospitalProjectAdapter(R.layout.item_hospital_project, checkBeans);
        hospitalProjectAdapter.setLoadMoreView(new CustomLoadMoreView());
        hospitalProjectAdapter.setOnLoadMoreListener(this, recyclerView);
        hospitalProjectAdapter.setOnItemClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(hospitalProjectAdapter);
    }

    @Override
    public void initListener() {
        super.initListener();
        refreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void fillNetWorkData() {
        super.fillNetWorkData();
        getHospitalProject(true);
        tempData();
    }

    private void tempData() {
        for (int i = 0; i < 5; i++) {
            CheckTypeBean bean = new CheckTypeBean();
            bean.setName("检查项->" + i);
            checkBeans.add(bean);
        }
        hospitalProjectAdapter.setNewData(checkBeans);
    }

    /**
     * 获取合作医院下服务项
     */
    private void getHospitalProject(boolean show) {
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Intent intent = new Intent(this, ProjectDetailActivity.class);
        startActivity(intent);
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
    }

    @Override
    public void onRefresh() {
        page = 1;
        getHospitalProject(false);
    }

    @Override
    public void onLoadMoreRequested() {
        page++;
        getHospitalProject(false);
    }
}
