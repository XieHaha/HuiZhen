package com.yht.yihuantong.ui.auth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.widgets.edittext.SuperEditText;
import com.yht.frame.widgets.recyclerview.loadview.CustomLoadMoreView;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ui.adapter.HospitalSelectAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.yht.yihuantong.ui.auth.fragment.AuthBaseFragment.REQUEST_CODE_HOSPITAL;

/**
 * @author 顿顿
 * @date 19/6/4 17:53
 * @des 医院选择 认证
 */
public class SelectHospitalByAuthActivity extends BaseActivity
        implements BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.RequestLoadMoreListener {
    @BindView(R.id.et_search_hospital)
    SuperEditText etSearchHospital;
    @BindView(R.id.rv_hospital)
    RecyclerView rvHospital;
    @BindView(R.id.tv_add_hospital_next)
    TextView tvAddHospitalNext;
    @BindView(R.id.layout_none_hospital)
    LinearLayout layoutNoneHospital;
    private HospitalSelectAdapter hospitalAdapter;
    private List<String> hospitals;

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_select_hospital_by_auth;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        rvHospital.setLayoutManager(new LinearLayoutManager(this));
        hospitals = new ArrayList<>();
        hospitals.add("医院a");
        hospitals.add("医院b");
        hospitals.add("医院c");
        hospitals.add("医院d");
        hospitalAdapter = new HospitalSelectAdapter(R.layout.item_hospital, hospitals);
        hospitalAdapter.setLoadMoreView(new CustomLoadMoreView());
        hospitalAdapter.setOnLoadMoreListener(this, rvHospital);
        hospitalAdapter.loadMoreEnd();
        hospitalAdapter.setOnItemClickListener(this);
        rvHospital.setAdapter(hospitalAdapter);
    }


    @OnClick(R.id.tv_add_hospital_next)
    public void onViewClicked() {
        Intent intent = new Intent(this, AddHospitalActivity.class);
        startActivityForResult(intent, REQUEST_CODE_HOSPITAL);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
    }

    /**
     * 加载更多
     */
    @Override
    public void onLoadMoreRequested() {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQUEST_CODE_HOSPITAL:
                setResult(RESULT_OK, data);
                finish();
                break;
            default:
                break;
        }
    }
}
