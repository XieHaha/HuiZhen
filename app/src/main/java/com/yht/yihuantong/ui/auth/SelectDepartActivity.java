package com.yht.yihuantong.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.CommonData;
import com.yht.frame.data.Tasks;
import com.yht.frame.data.bean.HospitalDepartBean;
import com.yht.frame.data.bean.HospitalDepartChildBean;
import com.yht.frame.http.retrofit.RequestUtils;
import com.yht.frame.ui.BaseActivity;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ui.adapter.DepartOneAdapter;
import com.yht.yihuantong.ui.adapter.DepartTwoAdapter;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * @author 顿顿
 * @date 19/6/5 17:38
 * @description
 */
public class SelectDepartActivity extends BaseActivity implements BaseQuickAdapter.OnItemClickListener {
    @BindView(R.id.rv_depart_one)
    RecyclerView rvDepartOne;
    @BindView(R.id.rv_depart_two)
    RecyclerView rvDepartTwo;
    private DepartOneAdapter departOneAdapter;
    private DepartTwoAdapter departTwoAdapter;
    private ArrayList<HospitalDepartBean> departs;
    private ArrayList<HospitalDepartChildBean> departChildren;
    /**
     * 当前选中医院code
     */
    private String hospitalCode;
    /**
     * 当前选中的科室position
     */
    private int positionOne = -1, positionTwo = -1;

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_selelct_depart;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        if (getIntent() != null) {
            hospitalCode = getIntent().getStringExtra(CommonData.KEY_HOSPITAL_CODE);
            positionOne = getIntent().getIntExtra(CommonData.KEY_DEPART_POSITION, -1);
            positionTwo = getIntent().getIntExtra(CommonData.KEY_DEPART_CHILD_POSITION, -1);
        }
        getDepartTree();
        //一级科室
        rvDepartOne.setLayoutManager(new LinearLayoutManager(this));
        departOneAdapter = new DepartOneAdapter(R.layout.item_depart, departs);
        if (positionOne == -1) {
            //默认选中第一个
            positionOne = 0;
        }
        departOneAdapter.setCurPosition(positionOne);
        departOneAdapter.setOnItemClickListener(this);
        rvDepartOne.setAdapter(departOneAdapter);
        //二级科室
        rvDepartTwo.setLayoutManager(new LinearLayoutManager(this));
        departTwoAdapter = new DepartTwoAdapter(R.layout.item_depart_two, departChildren);
        departTwoAdapter.setCurPosition(positionTwo);
        departTwoAdapter.setOnItemClickListener(this);
        rvDepartTwo.setAdapter(departTwoAdapter);
    }

    /**
     * 获取科室树
     */
    private void getDepartTree() {
        //如果是用户自己添加的医院，code可以为空
        RequestUtils.getDepartTree(this, hospitalCode, loginBean.getToken(), this);
    }

    /**
     * 子科室
     */
    private void setDepartChild() {
        departChildren = departs.get(positionOne).getChildList();
        departTwoAdapter.setCurPosition(positionTwo);
        departTwoAdapter.setNewData(departChildren);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (adapter instanceof DepartOneAdapter) {
            positionOne = position;
            //一级科室改变后重置二级科室选中状态
            positionTwo = -1;
            departOneAdapter.setCurPosition(position);
            setDepartChild();
        }
        else {
            positionTwo = position;
            departTwoAdapter.setCurPosition(position);
            Intent intent = new Intent();
            intent.putExtra(CommonData.KEY_DEPART_BEAN, departChildren.get(position));
            intent.putExtra(CommonData.KEY_DEPART_POSITION, positionOne);
            intent.putExtra(CommonData.KEY_DEPART_CHILD_POSITION, positionTwo);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        if (task == Tasks.GET_DEPART_LIST) {
            departs = (ArrayList<HospitalDepartBean>)response.getData();
            departOneAdapter.setNewData(departs);
            if (departs != null && departs.size() > 0) {
                setDepartChild();
            }
        }
    }
}
