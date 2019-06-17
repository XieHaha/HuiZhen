package com.zyc.doctor.ui.check;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.widgets.edittext.SuperEditText;
import com.zyc.doctor.R;
import com.zyc.doctor.ui.adapter.CheckTypeSelectAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.zyc.doctor.ui.auth.fragment.AuthBaseFragment.REQUEST_CODE_HOSPITAL;

/**
 * @author 顿顿
 * @date 19/6/4 17:53
 * @des 医院选择
 */
public class SelectCheckTypeActivity extends BaseActivity implements BaseQuickAdapter.OnItemClickListener {
    @BindView(R.id.et_search_check_type)
    SuperEditText etSearchCheckType;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    private CheckTypeSelectAdapter checkTypeSelectAdapter;
    private List<String> hospitals;

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_select_check_type;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        hospitals = new ArrayList<>();
        hospitals.add("a");
        hospitals.add("a");
        hospitals.add("a");
        hospitals.add("a");
        checkTypeSelectAdapter = new CheckTypeSelectAdapter(R.layout.item_check_select, hospitals);
        checkTypeSelectAdapter.setOnItemClickListener(this);
        recyclerview.setAdapter(checkTypeSelectAdapter);
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
        if (requestCode == REQUEST_CODE_HOSPITAL) {
            setResult(RESULT_OK, data);
            finish();
        }
    }
}
