package com.zyc.doctor.ui.auth;

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
import com.zyc.doctor.R;
import com.zyc.doctor.ui.adapter.HospitalAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/6/4 17:53
 * @des 医院选择
 */
public class SelectHospitalActivity extends BaseActivity implements BaseQuickAdapter.OnItemClickListener {
    @BindView(R.id.et_search_hospital)
    SuperEditText etSearchHospital;
    @BindView(R.id.rv_hospital)
    RecyclerView rvHospital;
    @BindView(R.id.tv_add_hospital_next)
    TextView tvAddHospitalNext;
    @BindView(R.id.layout_none_hospital)
    LinearLayout layoutNoneHospital;
    private HospitalAdapter hospitalAdapter;
    private List<String> hospitals;
    /**
     * 添加医院
     */
    public static final int REQUEST_CODE_ADD_HOSPITAL = 100;

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_select_hospital;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        rvHospital.setLayoutManager(new LinearLayoutManager(this));
        hospitals = new ArrayList<>();
        hospitals.add("a");
        hospitals.add("a");
        hospitals.add("a");
        hospitals.add("a");
        hospitalAdapter = new HospitalAdapter(R.layout.item_hospital, hospitals);
        hospitalAdapter.setOnItemClickListener(this);
        rvHospital.setAdapter(hospitalAdapter);
    }

    @OnClick(R.id.tv_add_hospital_next)
    public void onViewClicked() {
        Intent intent = new Intent(this, AddHospitalActivity.class);
        startActivityForResult(intent, REQUEST_CODE_ADD_HOSPITAL);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
    }
}
