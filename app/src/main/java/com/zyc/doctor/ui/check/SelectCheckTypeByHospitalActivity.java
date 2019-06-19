package com.zyc.doctor.ui.check;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
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
public class SelectCheckTypeByHospitalActivity extends BaseActivity implements BaseQuickAdapter.OnItemClickListener {
    @BindView(R.id.et_search_check_type)
    SuperEditText etSearchCheckType;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.public_title_bar_more)
    TextView publicTitleBarMore;
    @BindView(R.id.public_title_bar_title)
    TextView publicTitleBarTitle;
    private CheckTypeByHospitalSelectAdapter checkTypeByHospitalSelectAdapter;
    private List<String> hospitals;
    private ArrayList<Integer> selectPosition = new ArrayList<>();

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
        hospitals.add("检查项目1");
        hospitals.add("检查项目2");
        hospitals.add("检查项目3");
        hospitals.add("检查项目4");
        checkTypeByHospitalSelectAdapter = new CheckTypeByHospitalSelectAdapter(R.layout.item_check_by_hospital_select,
                                                                                hospitals);
        checkTypeByHospitalSelectAdapter.setOnItemClickListener(this);
        recyclerview.setAdapter(checkTypeByHospitalSelectAdapter);
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
    }

    @OnClick(R.id.public_title_bar_more)
    public void onViewClicked() {
        if (publicTitleBarMore.isSelected()) {
            Intent intent = new Intent();
            intent.putExtra("test", selectPosition);
            setResult(RESULT_OK, intent);
            finish();
        }
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

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Integer integer = position;
        if (selectPosition.contains(integer)) {
            selectPosition.remove(integer);
        }
        else {
            selectPosition.add(integer);
        }
        if (selectPosition.size() > 0) {
            publicTitleBarMore.setSelected(true);
        }
        else {
            publicTitleBarMore.setSelected(false);
        }
        checkTypeByHospitalSelectAdapter.setSelectPosition(selectPosition);
        checkTypeByHospitalSelectAdapter.notifyDataSetChanged();
    }
}
