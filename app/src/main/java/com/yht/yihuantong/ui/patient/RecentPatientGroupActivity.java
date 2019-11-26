package com.yht.yihuantong.ui.patient;

import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;

import androidx.annotation.NonNull;

import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.Tasks;
import com.yht.frame.data.bean.PatientBean;
import com.yht.frame.data.bean.RecentPatientBean;
import com.yht.frame.http.retrofit.RequestUtils;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.widgets.LoadViewHelper;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ui.adapter.RecentPatientGroupAdapter;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * @author 顿顿
 * @date 19/6/13 15:26
 * @description 最近添加的居民
 */
public class RecentPatientGroupActivity extends BaseActivity implements LoadViewHelper.OnNextClickListener {
    @BindView(R.id.expandable_list_view)
    ExpandableListView expandableListView;
    private RecentPatientGroupAdapter recentPatientGroupAdapter;
    /**
     * 居民
     */
    private RecentPatientBean recentPatientBean;
    private ArrayList<String> parentData = new ArrayList<>();
    private ArrayList<ArrayList<PatientBean>> childData = new ArrayList<>();

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
        //隐藏箭头
        expandableListView.setGroupIndicator(null);
        initAdapter();
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        if (BaseUtils.isNetworkAvailable(this)) {
            getRecentAddPatient();
        } else {
            loadViewHelper.load(LoadViewHelper.NONE_NETWORK);
        }
    }

    /**
     * 适配器处理
     */
    private void initAdapter() {
        recentPatientGroupAdapter = new RecentPatientGroupAdapter(this);
        expandableListView.setAdapter(recentPatientGroupAdapter);
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
            recentPatientBean = (RecentPatientBean) response.getData();
            initGroupData();
            recentPatientGroupAdapter.setParentData(parentData);
            recentPatientGroupAdapter.setChildData(childData);
            recentPatientGroupAdapter.notifyDataSetChanged();
            if (parentData != null && parentData.size() > 0) {
                expandableListView.setVisibility(View.VISIBLE);
            } else {
                expandableListView.setVisibility(View.GONE);
                loadViewHelper.load(LoadViewHelper.NONE_RECORDING);
            }
        }
    }

    private void initGroupData() {
        for (int i = 0; i < 6; i++) {
            ArrayList<PatientBean> list;
            if (i == 0) {
                list = recentPatientBean.getToday();
                parentData.add(String.format(getString(R.string.txt_recent_today), list.size()));
            } else if (i == 1) {
                list = recentPatientBean.getYesterday();
                parentData.add(String.format(getString(R.string.txt_recent_yesterday),
                        list.size()));
            } else if (i == 2) {
                list = recentPatientBean.getWeek();
                parentData.add(String.format(getString(R.string.txt_recent_week), list.size()));
            } else if (i == 3) {
                list = recentPatientBean.getMonth();
                parentData.add(String.format(getString(R.string.txt_recent_one_month),
                        list.size()));
            } else if (i == 4) {
                list = recentPatientBean.getThreeMonth();
                parentData.add(String.format(getString(R.string.txt_recent_three_month),
                        list.size()));
            } else {
                list = recentPatientBean.getHalfYear();
                parentData.add(String.format(getString(R.string.txt_recent_six_month),
                        list.size()));
            }
            childData.add(list);
        }
    }
}
