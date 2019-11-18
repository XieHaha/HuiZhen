package com.yht.yihuantong.ui.hospital.fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yht.frame.data.BaseData;
import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.CommonData;
import com.yht.frame.data.Tasks;
import com.yht.frame.data.bean.BaseListBean;
import com.yht.frame.data.bean.CooperateHospitalBean;
import com.yht.frame.data.bean.HospitalProductBean;
import com.yht.frame.http.retrofit.RequestUtils;
import com.yht.frame.ui.BaseFragment;
import com.yht.frame.widgets.LoadViewHelper;
import com.yht.frame.widgets.recyclerview.loadview.CustomLoadMoreView;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ui.adapter.HospitalProductAdapter;
import com.yht.yihuantong.ui.hospital.ServiceDetailActivity;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * @author 顿顿
 * @date 19/8/12 15:34
 * @des 医院服务项
 */
public class HospitalServiceFragment extends BaseFragment
        implements BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemClickListener,
                   SwipeRefreshLayout.OnRefreshListener, LoadViewHelper.OnNextClickListener {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    private HospitalProductAdapter hospitalProjectAdapter;
    private ArrayList<HospitalProductBean> hospitalProductBeans = new ArrayList<>();
    /**
     * 当前医院
     */
    private CooperateHospitalBean curHospital;
    /**
     * 默认第一页
     */
    private int page = 1;

    @Override
    public int getLayoutID() {
        return R.layout.fragment_hospital_service;
    }

    @Override
    public void initView(View view, @NonNull Bundle savedInstanceState) {
        super.initView(view, savedInstanceState);
        loadViewHelper = new LoadViewHelper(view);
        loadViewHelper.setOnNextClickListener(this);
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light,
                                              android.R.color.holo_orange_light, android.R.color.holo_green_light);
        hospitalProjectAdapter = new HospitalProductAdapter(R.layout.item_hospital_project, hospitalProductBeans);
        hospitalProjectAdapter.setLoadMoreView(new CustomLoadMoreView());
        hospitalProjectAdapter.setOnLoadMoreListener(this, recyclerView);
        hospitalProjectAdapter.setOnItemClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(hospitalProjectAdapter);
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        getHospitalProduct();
    }

    @Override
    public void initListener() {
        super.initListener();
        refreshLayout.setOnRefreshListener(this);
    }

    public void setCurHospital(CooperateHospitalBean curHospital) {
        this.curHospital = curHospital;
    }

    /**
     * 获取合作医院下服务项
     */
    private void getHospitalProduct() {
        RequestUtils.getCooperateHospitalServiceList(getContext(), loginBean.getToken(), curHospital.getHospitalCode(),
                                                     BaseData.BASE_PAGE_DATA_NUM, page, this);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Intent intent = new Intent(getContext(), ServiceDetailActivity.class);
        intent.putExtra(CommonData.KEY_HOSPITAL_PRODUCT_BEAN, hospitalProductBeans.get(position));
        startActivity(intent);
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        if (task == Tasks.GET_COOPERATE_HOSPITAL_PROJECT_LIST) {
            BaseListBean<HospitalProductBean> baseListBean = (BaseListBean<HospitalProductBean>)response.getData();
            if (page == BaseData.BASE_ONE) {
                hospitalProductBeans.clear();
            }
            hospitalProductBeans.addAll(baseListBean.getRecords());
            hospitalProjectAdapter.setNewData(hospitalProductBeans);
            if (hospitalProductBeans.size() > 0) {
                refreshLayout.setVisibility(View.VISIBLE);
                if (baseListBean.getRecords() != null &&
                    baseListBean.getRecords().size() >= BaseData.BASE_PAGE_DATA_NUM) {
                    hospitalProjectAdapter.loadMoreComplete();
                }
                else {
                    if (hospitalProductBeans.size() > BaseData.BASE_PAGE_DATA_NUM) {
                        hospitalProjectAdapter.loadMoreEnd();
                    }
                    else {
                        hospitalProjectAdapter.setEnableLoadMore(false);
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
        getHospitalProduct();
    }

    @Override
    public void onLoadMoreRequested() {
        page++;
        getHospitalProduct();
    }

    @Override
    public void onNextClick() {
        page = 1;
        getHospitalProduct();
    }
}
