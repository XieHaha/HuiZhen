package com.yht.yihuantong.ui.reservation.remote;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.CommonData;
import com.yht.frame.data.Tasks;
import com.yht.frame.data.bean.RemoteDepartBean;
import com.yht.frame.data.bean.RemoteDepartTitleBean;
import com.yht.frame.http.retrofit.RequestUtils;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.utils.ToastUtil;
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
        implements LoadViewHelper.OnNextClickListener,
        RemoteDepartAdapter.OnRemoteDepartSelectListener {
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
    private String date, startHour, endHour;
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
    private ArrayList<RemoteDepartBean> selectedRemoteDepartBeans = new ArrayList<>();
    /**
     * 选择的科室 id
     */
    private ArrayList<Integer> selectedRemoteDepartId = new ArrayList<>();
    /**
     * 最多可选的科室
     */
    private final int MAX_DEPART = 5;
    /**
     * 是否更新过时间
     */
    private boolean isUpdateTime;
    /**
     * 选择时间
     */
    private static final int REQUEST_CODE_REMOTE_TIME = 100;

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
        if (getIntent() != null) {
            initRemoteHour(getIntent());
            selectedRemoteDepartId =
                    getIntent().getIntegerArrayListExtra(CommonData.KEY_REMOTE_DEPART_LIST_ID);
        }
        publicTitleBarMore.setVisibility(View.VISIBLE);
        publicTitleBarMore.setText(R.string.txt_sure);
        if (selectedRemoteDepartId != null && selectedRemoteDepartId.size() > 0) {
            publicTitleBarMore.setSelected(true);
        }
        loadViewHelper = new LoadViewHelper(this);
        loadViewHelper.setOnNextClickListener(this);
        initAdapter();
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        if (BaseUtils.isNetworkAvailable(this)) {
            getRemoteDepartmentInfo();
        } else {
            layoutHint.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            loadViewHelper.load(LoadViewHelper.NONE_NETWORK);
        }
    }

    /**
     * 获取远程  科室列表
     */
    private void getRemoteDepartmentInfo() {
        RequestUtils.getRemoteDepartmentInfo(this, loginBean.getToken(), date + " " + startHour,
                date + " " + endHour,
                this);
    }

    /**
     * 适配器处理
     */
    private void initAdapter() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        remoteDepartAdapter = new RemoteDepartAdapter(remoteDepartGroup);
        //赋值已选择的科室
        remoteDepartAdapter.setSelectedRemoteDepartIds(selectedRemoteDepartId);
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
        Map<String, List<RemoteDepartBean>> resultMap = new HashMap<>();
        RemoteDepartBean remoteDepartBean;
        for (int i = 0; i < remoteDepartBeans.size(); i++) {
            remoteDepartBean = remoteDepartBeans.get(i);
            if (resultMap.containsKey(remoteDepartBean.getHospitalName())) {
                resultMap.get(remoteDepartBean.getHospitalName()).add(remoteDepartBean);
            } else {
                List<RemoteDepartBean> list = new ArrayList<>();
                list.add(remoteDepartBean);
                resultMap.put(remoteDepartBean.getHospitalName(), list);
            }
        }
        remoteDepartGroup.clear();
        for (List<RemoteDepartBean> value : resultMap.values()) {
            titleBean = new RemoteDepartTitleBean();
            for (RemoteDepartBean bean : value) {
                titleBean.setHospitalName(bean.getHospitalName());
                titleBean.addSubItem(bean);
                if (selectedRemoteDepartId != null && selectedRemoteDepartId.size() > 0) {
                    if (selectedRemoteDepartId.contains(bean.getDepartmentId())) {
                        //计数
                        int num = titleBean.getSelectedNum();
                        if (num < 0) {
                            num = 0;
                        }
                        num++;
                        titleBean.setSelectedNum(num);
                    }
                }
            }
            remoteDepartGroup.add(titleBean);
        }
        if (remoteDepartGroup.size() > 0) {
            remoteDepartAdapter.setNewData(remoteDepartGroup);
            //默认展开全部
            remoteDepartAdapter.expandAll();
            recyclerView.setVisibility(View.VISIBLE);
            layoutHint.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.GONE);
            layoutHint.setVisibility(View.VISIBLE);
            loadViewHelper.load(LoadViewHelper.NONE_RESERVATION_DEPART);
        }
    }

    @Override
    public void onNextClick() {
        getRemoteDepartmentInfo();
    }

    @OnClick({R.id.public_title_bar_more, R.id.layout_time})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.public_title_bar_more:
                if (publicTitleBarMore.isSelected()) {
                    Intent intent = new Intent();
                    if (isUpdateTime) {
                        intent.putExtra(CommonData.KEY_REMOTE_DATE, date);
                        intent.putExtra(CommonData.KEY_REMOTE_START_HOUR, startHour);
                        intent.putExtra(CommonData.KEY_REMOTE_END_HOUR, endHour);
                    }
                    intent.putExtra(CommonData.KEY_REMOTE_DEPART_LIST, selectedRemoteDepartBeans);
                    intent.putExtra(CommonData.KEY_REMOTE_DEPART_LIST_ID, selectedRemoteDepartId);
                    setResult(RESULT_OK, intent);
                    finish();
                }
                break;
            case R.id.layout_time:
                Intent intent = new Intent(this, ConsultationTimeActivity.class);
                intent.putExtra(CommonData.KEY_REMOTE_DATE, date);
                intent.putExtra(CommonData.KEY_REMOTE_START_HOUR, startHour);
                intent.putExtra(CommonData.KEY_REMOTE_END_HOUR, endHour);
                startActivityForResult(intent, REQUEST_CODE_REMOTE_TIME);
                break;
            default:
                break;
        }
    }

    @Override
    public void onRemoteDepartSelect(RemoteDepartBean bean, RemoteDepartTitleBean parentBean) {
        if (selectedRemoteDepartBeans.contains(bean)) {
            selectedRemoteDepartBeans.remove(bean);
            selectedRemoteDepartId.remove(Integer.valueOf(bean.getDepartmentId()));
            //计数
            int num = parentBean.getSelectedNum();
            if (num > 0) {
                num--;
            } else {
                num = 0;
            }
            parentBean.setSelectedNum(num);
        } else {
            if (selectedRemoteDepartBeans.size() >= MAX_DEPART) {
                ToastUtil.toast(this, R.string.txt_add_depart_max);
                return;
            }
            selectedRemoteDepartBeans.add(bean);
            selectedRemoteDepartId.add(bean.getDepartmentId());
            //计数
            int num = parentBean.getSelectedNum();
            if (num < 0) {
                num = 0;
            }
            num++;
            parentBean.setSelectedNum(num);
        }
        if (selectedRemoteDepartBeans.size() > 0) {
            publicTitleBarMore.setSelected(true);
        } else {
            publicTitleBarMore.setSelected(false);
        }
        //刷新数据
        remoteDepartAdapter.setSelectedRemoteDepartIds(selectedRemoteDepartId);
        remoteDepartAdapter.notifyDataSetChanged();
    }

    @Override
    public void addRemoteDepartHistory(RemoteDepartBean bean) {
        if (!selectedRemoteDepartBeans.contains(bean)) {
            selectedRemoteDepartBeans.add(bean);
        }
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        if (task == Tasks.GET_REMOTE_DEPARTMENT_INFO) {
            remoteDepartBeans = (ArrayList<RemoteDepartBean>) response.getData();
            //清除已有数据
            remoteDepartGroup.clear();
            if (remoteDepartBeans != null && remoteDepartBeans.size() > 0) {
                initGroupData();
            } else {
                recyclerView.setVisibility(View.GONE);
                layoutHint.setVisibility(View.VISIBLE);
                loadViewHelper.load(LoadViewHelper.NONE_RESERVATION_DEPART);
            }
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_REMOTE_TIME) {
            //更新时间
            isUpdateTime = true;
            initRemoteHour(data);
            //初始化数据
            selectedRemoteDepartBeans.clear();
            selectedRemoteDepartId.clear();
            publicTitleBarMore.setSelected(false);
            //更改时间后需要重新拉取科室数据
            getRemoteDepartmentInfo();
        }
    }

    /**
     * 时间
     */
    private void initRemoteHour(Intent data) {
        date = data.getStringExtra(CommonData.KEY_REMOTE_DATE);
        startHour = data.getStringExtra(CommonData.KEY_REMOTE_START_HOUR);
        endHour = data.getStringExtra(CommonData.KEY_REMOTE_END_HOUR);
        //时间
        tvTime.setText(String.format(getString(R.string.txt_date_joiner), date, startHour,
                endHour));
    }
}
