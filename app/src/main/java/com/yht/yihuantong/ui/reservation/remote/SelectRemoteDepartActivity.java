package com.yht.yihuantong.ui.reservation.remote;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.CommonData;
import com.yht.frame.data.Tasks;
import com.yht.frame.data.bean.RemoteDepartBean;
import com.yht.frame.data.bean.RemoteDepartTitleBean;
import com.yht.frame.http.retrofit.RequestUtils;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.widgets.LoadViewHelper;
import com.yht.frame.widgets.recyclerview.loadview.CustomLoadMoreView;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ui.adapter.RemoteDepartAdapter;
import com.yht.yihuantong.ui.reservation.time.ConsultationTimeActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/10/24 10:30
 * @description 远程会诊选择医院-科室
 */
public class SelectRemoteDepartActivity extends BaseActivity
        implements LoadViewHelper.OnNextClickListener, RemoteDepartAdapter.OnRemoteDepartSelectListener {
    @BindView(R.id.public_title_bar_more)
    TextView publicTitleBarMore;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.layout_hint)
    LinearLayout layoutHint;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private RemoteDepartAdapter remoteDepartAdapter;
    /**
     * 远程会诊时间范围
     */
    private String startTime, endTime;
    /**
     * 分组title
     */
    private ArrayList<MultiItemEntity> remoteDepartGroup = new ArrayList<>();
    /**
     * 远程会诊  科室数据
     */
    private ArrayList<RemoteDepartBean> remoteDepartBeans;
    /**
     * 选择的科室
     */
    private ArrayList<RemoteDepartBean> selectedRemoteDepartBeans;
    /**
     * 选择时间
     */
    public static final int REQUEST_CODE_REMOTE_TIME = 100;

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_select_remote_depart;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        publicTitleBarMore.setVisibility(View.VISIBLE);
        publicTitleBarMore.setText(R.string.txt_sure);
        loadViewHelper = new LoadViewHelper(this);
        loadViewHelper.setOnNextClickListener(this);
        initAdapter();
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        if (BaseUtils.isNetworkAvailable(this)) {
            getRemoteDepartmentInfo();
        }
        else {
            layoutHint.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            loadViewHelper.load(LoadViewHelper.NONE_NETWORK);
        }
    }

    /**
     * 获取远程  科室列表
     */
    private void getRemoteDepartmentInfo() {
        RequestUtils.getRemoteDepartmentInfo(this, loginBean.getToken(), startTime, endTime, this);
    }

    /**
     * 适配器处理
     */
    private void initAdapter() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        remoteDepartAdapter = new RemoteDepartAdapter(remoteDepartGroup);
        remoteDepartAdapter.setLoadMoreView(new CustomLoadMoreView());
        remoteDepartAdapter.setOnRemoteDepartSelectListener(this);
        remoteDepartAdapter.loadMoreEnd();
        recyclerView.setAdapter(remoteDepartAdapter);
    }

    /**
     * 根据医院字段重新分组
     */
    private void initGroupData() {
        RemoteDepartTitleBean titleBean;
        Map<String, List<RemoteDepartBean>> resultMap = new HashMap<>(16);
        RemoteDepartBean remoteDepartBean;
        for (int i = 0; i < remoteDepartBeans.size(); i++) {
            remoteDepartBean = remoteDepartBeans.get(i);
            if (resultMap.containsKey(remoteDepartBean.getHospitalName())) {
                resultMap.get(remoteDepartBean.getHospitalName()).add(remoteDepartBean);
            }
            else {
                List<RemoteDepartBean> list = new ArrayList<>();
                list.add(remoteDepartBean);
                resultMap.put(remoteDepartBean.getHospitalName(), list);
            }
        }
        for (List<RemoteDepartBean> value : resultMap.values()) {
            titleBean = new RemoteDepartTitleBean();
            for (RemoteDepartBean bean : value) {
                titleBean.setHospitalName(bean.getHospitalName());
                titleBean.addSubItem(bean);
            }
            remoteDepartGroup.add(titleBean);
        }
        remoteDepartAdapter.setNewData(remoteDepartGroup);
        //默认展开全部
        remoteDepartAdapter.expandAll();
    }

    @Override
    public void onNextClick() {
        getRemoteDepartmentInfo();
    }

    @OnClick({ R.id.public_title_bar_more, R.id.layout_time })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.public_title_bar_more:
                if (publicTitleBarMore.isSelected()) {
                }
                break;
            case R.id.layout_time:
                Intent intent = new Intent(this, ConsultationTimeActivity.class);
                startActivityForResult(intent, REQUEST_CODE_REMOTE_TIME);
                break;
            default:
                break;
        }
    }

    @Override
    public void onRemoteDepartSelect(RemoteDepartBean remoteDepartBean) {
        if (selectedRemoteDepartBeans == null) {
            selectedRemoteDepartBeans = new ArrayList<>();
        }
        if (selectedRemoteDepartBeans.contains(remoteDepartBean)) {
            selectedRemoteDepartBeans.remove(remoteDepartBean);
        }
        else {
            selectedRemoteDepartBeans.add(remoteDepartBean);
        }
        if (selectedRemoteDepartBeans.size() > 0) {
            publicTitleBarMore.setSelected(true);
        }
        else {
            publicTitleBarMore.setSelected(false);
        }
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        if (task == Tasks.GET_REMOTE_DEPARTMENT_INFO) {
            remoteDepartBeans = (ArrayList<RemoteDepartBean>)response.getData();
            if (remoteDepartBeans != null && remoteDepartBeans.size() > 0) {
                initGroupData();
            }
            else {
                recyclerView.setVisibility(View.GONE);
                layoutHint.setVisibility(View.VISIBLE);
                loadViewHelper.load(LoadViewHelper.NONE_RECORDING);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_REMOTE_TIME) {
            startTime = data.getStringExtra(CommonData.KEY_PUBLIC_STRING);
            endTime = data.getStringExtra(CommonData.KEY_PUBLIC_STRING);
            //初始化数据
            selectedRemoteDepartBeans.clear();
            getRemoteDepartmentInfo();
        }
    }
}
