package com.zyc.doctor.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yht.frame.data.CommonData;
import com.yht.frame.ui.BaseActivity;
import com.zyc.doctor.R;
import com.zyc.doctor.ui.adapter.DepartOneAdapter;
import com.zyc.doctor.ui.adapter.DepartTwoAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author 顿顿
 * @date 19/6/5 17:38
 * @des
 */
public class SelectDepartActivity extends BaseActivity implements BaseQuickAdapter.OnItemClickListener {
    @BindView(R.id.rv_depart_one)
    RecyclerView rvDepartOne;
    @BindView(R.id.rv_depart_two)
    RecyclerView rvDepartTwo;
    private DepartOneAdapter departOneAdapter;
    private DepartTwoAdapter departTwoAdapter;
    private List<String> departOnes, departTwos;
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
        departOnes = new ArrayList<>();
        departOnes.add("aaa");
        departOnes.add("aaa");
        departOnes.add("aaa");
        departOnes.add("aaa");
        departOnes.add("aaa");
        departOnes.add("aaa");
        departOnes.add("aaa");
        departOnes.add("aaa");
        departTwos = new ArrayList<>();
        departTwos.add("aaa");
        departTwos.add("aaa");
        departTwos.add("aaa");
        departTwos.add("aaa");
        departTwos.add("aaa");
        departTwos.add("aaa");
        departTwos.add("aaa");
        departTwos.add("aaa");
        //一级科室
        rvDepartOne.setLayoutManager(new LinearLayoutManager(this));
        departOneAdapter = new DepartOneAdapter(R.layout.item_depart, departOnes);
        departOneAdapter.setCurPosition(positionOne);
        departOneAdapter.setOnItemClickListener(this);
        //默认第一个选中
        departOneAdapter.setCurPosition(0);
        rvDepartOne.setAdapter(departOneAdapter);
        //二级科室
        rvDepartTwo.setLayoutManager(new LinearLayoutManager(this));
        departTwoAdapter = new DepartTwoAdapter(R.layout.item_depart_two, departTwos);
        departTwoAdapter.setCurPosition(positionTwo);
        departTwoAdapter.setOnItemClickListener(this);
        rvDepartTwo.setAdapter(departTwoAdapter);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (adapter instanceof DepartOneAdapter) {
            positionOne = position;
            departOneAdapter.setCurPosition(position);
        }
        else {
            positionTwo = position;
            departTwoAdapter.setCurPosition(position);
            Intent intent = new Intent();
            intent.putExtra(CommonData.KEY_DEPART_NAME, "科室名字");
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
