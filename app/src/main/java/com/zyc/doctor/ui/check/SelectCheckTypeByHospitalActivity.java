package com.zyc.doctor.ui.check;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.yht.frame.data.CommonData;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.widgets.edittext.SuperEditText;
import com.zyc.doctor.R;
import com.zyc.doctor.ui.adapter.CheckTypeByHospitalSelectAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.zyc.doctor.ui.auth.fragment.AuthBaseFragment.REQUEST_CODE_HOSPITAL;

/**
 * @author 顿顿
 * @date 19/6/4 17:53
 * @des 选择医院下所有检查项目
 */
public class SelectCheckTypeByHospitalActivity extends BaseActivity {
    @BindView(R.id.et_search_check_type)
    SuperEditText etSearchCheckType;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.public_title_bar_more)
    TextView publicTitleBarMore;
    @BindView(R.id.public_title_bar_title)
    TextView publicTitleBarTitle;
    private CheckTypeByHospitalSelectAdapter checkTypeSelectAdapter;
    private List<String> hospitals;

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_select_check_type_by_hospital;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        initTitle();
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        hospitals = new ArrayList<>();
        hospitals.add("a");
        hospitals.add("a");
        hospitals.add("a");
        hospitals.add("a");
        checkTypeSelectAdapter = new CheckTypeByHospitalSelectAdapter(R.layout.item_check_select, hospitals);
        recyclerview.setAdapter(checkTypeSelectAdapter);
    }

    /**
     * title处理
     */
    private void initTitle() {
        if (getIntent() != null) {
            String title = getIntent().getStringExtra(CommonData.KEY_PUBLIC);
            publicTitleBarTitle.setText(title);
        }
        //右边按钮
        publicTitleBarMore.setVisibility(View.VISIBLE);
        publicTitleBarMore.setText(R.string.txt_add);
        publicTitleBarMore.setSelected(true);
    }

    @OnClick(R.id.public_title_bar_more)
    public void onViewClicked() {
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
