package com.yht.yihuantong.ui.hospital;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yht.frame.data.BaseData;
import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.CommonData;
import com.yht.frame.data.Tasks;
import com.yht.frame.data.bean.BaseListBean;
import com.yht.frame.data.bean.CooperateHospitalBean;
import com.yht.frame.http.retrofit.RequestUtils;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.widgets.LoadViewHelper;
import com.yht.frame.widgets.recyclerview.loadview.CustomLoadMoreView;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ui.adapter.CooperateHospitalAdapter;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * @author 顿顿
 * @date 19/8/12 15:34
 * @description 合作医院列表
 */
public class CooperateHospitalListActivity extends BaseActivity
        implements BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemClickListener,
                   SwipeRefreshLayout.OnRefreshListener, LoadViewHelper.OnNextClickListener {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    private CooperateHospitalAdapter cooperateHospitalAdapter;
    private ArrayList<CooperateHospitalBean> hospitalBeans = new ArrayList<>();
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
        loadViewHelper = new LoadViewHelper(this);
        loadViewHelper.setOnNextClickListener(this);
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
        if (BaseUtils.isNetworkAvailable(this)) {
            getCooperateHospital(true);
        }
        else {
            refreshLayout.setVisibility(View.GONE);
            loadViewHelper.load(LoadViewHelper.NONE_NETWORK);
        }
    }

    /**
     * 获取合作医院
     */
    private void getCooperateHospital(boolean show) {
        RequestUtils.getCooperateHospitalList(this, loginBean.getToken(), BaseData.BASE_PAGE_DATA_NUM, page, show,
                                              this);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Intent intent = new Intent(this, HospitalDetailActivity.class);
        intent.putExtra(CommonData.KEY_HOSPITAL_BEAN, hospitalBeans.get(position).getHospitalCode());
        startActivity(intent);
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        if (task == Tasks.GET_COOPERATE_HOSPITAL_LIST) {
            BaseListBean<CooperateHospitalBean> baseListBean = (BaseListBean<CooperateHospitalBean>)response.getData();
            if (page == BaseData.BASE_ONE) {
                hospitalBeans.clear();
            }
            hospitalBeans.addAll(baseListBean.getRecords());
            cooperateHospitalAdapter.setNewData(hospitalBeans);
            if (hospitalBeans.size() > 0) {
                refreshLayout.setVisibility(View.VISIBLE);
                if (baseListBean.getRecords() != null &&
                    baseListBean.getRecords().size() >= BaseData.BASE_PAGE_DATA_NUM) {
                    cooperateHospitalAdapter.loadMoreComplete();
                }
                else {
                    if (hospitalBeans.size() > BaseData.BASE_PAGE_DATA_NUM) {
                        cooperateHospitalAdapter.loadMoreEnd();
                    }
                    else {
                        cooperateHospitalAdapter.setEnableLoadMore(false);
                    }
                }
            }
            else {
                refreshLayout.setVisibility(View.GONE);
                loadViewHelper.load(LoadViewHelper.NONE_RECORDING);
            }
        }
    }

    @Override
    public void onResponseEnd(Tasks task) {
        super.onResponseEnd(task);
        refreshLayout.setRefreshing(false);
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

    @Override
    public void onNextClick() {
        page = 1;
        getCooperateHospital(true);
    }
}
