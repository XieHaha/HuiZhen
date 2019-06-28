package com.zyc.doctor.ui.transfer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yht.frame.data.CommonData;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.widgets.recyclerview.loadview.CustomLoadMoreView;
import com.zyc.doctor.R;
import com.zyc.doctor.ui.adapter.HospitalSelectAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author 顿顿
 * @date 19/6/4 17:53
 * @des 医院选择 接诊
 */
public class SelectHospitalByTransferActivity extends BaseActivity
        implements BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.RequestLoadMoreListener {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private HospitalSelectAdapter hospitalAdapter;
    private List<String> hospitals;

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_select_hospital_by_transfer;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        hospitals = new ArrayList<>();
        hospitals.add("医院a");
        hospitals.add("a");
        hospitals.add("a");
        hospitals.add("a");
        hospitalAdapter = new HospitalSelectAdapter(R.layout.item_hospital, hospitals);
        hospitalAdapter.setOnItemClickListener(this);
        hospitalAdapter.setLoadMoreView(new CustomLoadMoreView());
        hospitalAdapter.setOnLoadMoreListener(this, recyclerView);
        hospitalAdapter.loadMoreEnd();
        recyclerView.setAdapter(hospitalAdapter);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Intent intent = new Intent();
        intent.putExtra(CommonData.KEY_HOSPITAL_BEAN, "已选择的医院");
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onLoadMoreRequested() {
    }
}
