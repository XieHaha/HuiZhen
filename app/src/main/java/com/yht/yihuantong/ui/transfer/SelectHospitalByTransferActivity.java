package com.yht.yihuantong.ui.transfer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yht.frame.data.BaseData;
import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.CommonData;
import com.yht.frame.data.Tasks;
import com.yht.frame.data.bean.HospitalBean;
import com.yht.frame.http.retrofit.RequestUtils;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.widgets.recyclerview.loadview.CustomLoadMoreView;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ui.adapter.HospitalSelectAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author 顿顿
 * @date 19/6/4 17:53
 * @description 医院选择 接诊
 */
public class SelectHospitalByTransferActivity extends BaseActivity
        implements BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.RequestLoadMoreListener {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private HospitalSelectAdapter hospitalAdapter;
    private List<HospitalBean> hospitals;

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_select_hospital_by_transfer;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        hospitals = new ArrayList<>();
        hospitalAdapter = new HospitalSelectAdapter(R.layout.item_hospital, hospitals);
        hospitalAdapter.setOnItemClickListener(this);
        hospitalAdapter.setLoadMoreView(new CustomLoadMoreView());
        hospitalAdapter.setOnLoadMoreListener(this, recyclerView);
        recyclerView.setAdapter(hospitalAdapter);
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        getHospitalListByReceive();
    }

    /**
     * 获取当前医生可接受转诊的医院
     */
    private void getHospitalListByReceive() {
        RequestUtils.getHospitalListByReceive(this, loginBean.getToken(), this);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Intent intent = new Intent();
        intent.putExtra(CommonData.KEY_HOSPITAL_BEAN, hospitals.get(position));
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        switch (task) {
            case GET_HOSPITAL_LIST_BY_RECEIVE:
                hospitals = (List<HospitalBean>)response.getData();
                hospitalAdapter.setNewData(hospitals);
                if (hospitals.size() > BaseData.BASE_PAGE_DATA_NUM) {
                    hospitalAdapter.loadMoreEnd();
                }
                else {
                    hospitalAdapter.setEnableLoadMore(false);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onLoadMoreRequested() {
    }
}
