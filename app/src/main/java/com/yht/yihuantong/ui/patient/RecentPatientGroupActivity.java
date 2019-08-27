package com.yht.yihuantong.ui.patient;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.Tasks;
import com.yht.frame.data.bean.PatientBean;
import com.yht.frame.data.bean.RecentPatientBean;
import com.yht.frame.data.bean.RecentPatientTitleBean;
import com.yht.frame.http.retrofit.RequestUtils;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.widgets.LoadViewHelper;
import com.yht.frame.widgets.recyclerview.loadview.CustomLoadMoreView;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ui.adapter.RecentPatientGroupAdapter;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * @author 顿顿
 * @date 19/6/13 15:26
 * @description 最近添加的患者
 */
public class RecentPatientGroupActivity extends BaseActivity implements LoadViewHelper.OnNextClickListener {
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    private RecentPatientGroupAdapter recentPatientGroupAdapter;
    /**
     * 患者
     */
    private RecentPatientBean recentPatientBean;
    /**
     * 分组title
     */
    private ArrayList<MultiItemEntity> recentPatientTitleBeans = new ArrayList<>();

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_recent_patient;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        loadViewHelper = new LoadViewHelper(this);
        loadViewHelper.setOnNextClickListener(this);
        initAdapter();
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        if (BaseUtils.isNetworkAvailable(this)) {
            getRecentAddPatient();
        }
        else {
            loadViewHelper.load(LoadViewHelper.NONE_NETWORK);
        }
    }

    /**
     * 适配器处理
     */
    private void initAdapter() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recentPatientGroupAdapter = new RecentPatientGroupAdapter(recentPatientTitleBeans);
        recentPatientGroupAdapter.setLoadMoreView(new CustomLoadMoreView());
        recentPatientGroupAdapter.loadMoreEnd();
        recyclerView.setAdapter(recentPatientGroupAdapter);
    }

    /**
     * 获取标签分组
     */
    private void getRecentAddPatient() {
        RequestUtils.getRecentAddPatient(this, loginBean.getToken(), this);
    }

    /**
     * 重新加载
     */
    @Override
    public void onNextClick() {
        getRecentAddPatient();
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        if (task == Tasks.GET_RECENT_ADD_PATIENT) {
            recentPatientBean = (RecentPatientBean)response.getData();
            initGroupData();
            recentPatientGroupAdapter.setNewData(recentPatientTitleBeans);
            if (recentPatientTitleBeans != null && recentPatientTitleBeans.size() > 0) {
                recyclerView.setVisibility(View.VISIBLE);
            }
            else {
                recyclerView.setVisibility(View.GONE);
                loadViewHelper.load(LoadViewHelper.NONE_RECORDING);
            }
        }
    }

    private void initGroupData() {
        for (int i = 0; i < 6; i++) {
            RecentPatientTitleBean bean = new RecentPatientTitleBean();
            ArrayList<PatientBean> list;
            if (i == 0) {
                list = recentPatientBean.getToday();
                bean.setTitle(String.format(getString(R.string.txt_recent_today), list.size()));
            }
            else if (i == 1) {
                list = recentPatientBean.getYesterday();
                bean.setTitle(String.format(getString(R.string.txt_recent_yesterday), list.size()));
            }
            else if (i == 2) {
                list = recentPatientBean.getWeek();
                bean.setTitle(String.format(getString(R.string.txt_recent_week), list.size()));
            }
            else if (i == 3) {
                list = recentPatientBean.getMonth();
                bean.setTitle(String.format(getString(R.string.txt_recent_one_month), list.size()));
            }
            else if (i == 4) {
                list = recentPatientBean.getThreeMonth();
                bean.setTitle(String.format(getString(R.string.txt_recent_three_month), list.size()));
            }
            else {
                list = recentPatientBean.getHalfYear();
                bean.setTitle(String.format(getString(R.string.txt_recent_six_month), list.size()));
            }
            for (PatientBean patientBean : list) {
                bean.addSubItem(patientBean);
            }
            recentPatientTitleBeans.add(bean);
        }
    }
}
