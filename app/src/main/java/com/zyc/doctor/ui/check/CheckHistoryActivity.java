package com.zyc.doctor.ui.check;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yht.frame.data.bean.PatientBean;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.widgets.recyclerview.TimeItemDecoration;
import com.zyc.doctor.R;
import com.zyc.doctor.ui.adapter.CheckHistoryAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/6/13 15:26
 * @des
 */
public class CheckHistoryActivity extends BaseActivity implements BaseQuickAdapter.OnItemClickListener {
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    /**
     * 时间分隔
     */
    private TimeItemDecoration timeItemDecoration;
    private CheckHistoryAdapter checkHistoryAdapter;
    private List<PatientBean> checkedList;

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_check_history;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        timeItemDecoration = new TimeItemDecoration(this);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.addItemDecoration(timeItemDecoration);
        initDatas();
        checkHistoryAdapter = new CheckHistoryAdapter(R.layout.item_check_history, checkedList);
        checkHistoryAdapter.setOnItemClickListener(this);
        recyclerview.setAdapter(checkHistoryAdapter);
    }

    private void initDatas() {
        checkedList = new ArrayList<>();
        String[] names = {
                "孙尚香", "安其拉", "白起", "不知火舞" };
        for (String name : names) {
            PatientBean bean = new PatientBean();
            bean.setName(name);
            checkedList.add(bean);
        }
        //对数据源进行排序
        BaseUtils.sortData(checkedList);
        //返回一个包含所有Tag字母在内的字符串并赋值给tagsStr
        String tagsStr = BaseUtils.getTags(checkedList);
        timeItemDecoration.setDatas(checkedList, tagsStr);
    }

    @OnClick(R.id.tv_check_next)
    public void onViewClicked() {
        startActivity(new Intent(this, ReservationCheckActivity.class));
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        startActivity(new Intent(this, CheckDetailActivity.class));
    }
}
