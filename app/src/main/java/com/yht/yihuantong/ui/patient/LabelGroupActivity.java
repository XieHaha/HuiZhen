package com.yht.yihuantong.ui.patient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yht.frame.data.BaseData;
import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.CommonData;
import com.yht.frame.data.Tasks;
import com.yht.frame.data.bean.BaseListBean;
import com.yht.frame.data.bean.LabelBean;
import com.yht.frame.http.retrofit.RequestUtils;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.utils.ToastUtil;
import com.yht.frame.widgets.LoadViewHelper;
import com.yht.frame.widgets.dialog.HintDialog;
import com.yht.frame.widgets.recyclerview.loadview.CustomLoadMoreView;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ui.adapter.PatientLabelAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author 顿顿
 * @date 19/6/13 15:26
 * @des 患者标签
 */
public class LabelGroupActivity extends BaseActivity
        implements BaseQuickAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener,
                   BaseQuickAdapter.RequestLoadMoreListener, LoadViewHelper.OnNextClickListener,
                   BaseQuickAdapter.OnItemLongClickListener {
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.layout_refresh)
    SwipeRefreshLayout layoutRefresh;
    private PatientLabelAdapter patientLabelAdapter;
    /**
     * 标签
     */
    private List<LabelBean> labelBeans = new ArrayList<>();
    /**
     * 页码
     */
    private int page = 1;
    /**
     * 订单是否更新
     */
    private static final int REQUEST_CODE_UPDATE = 100;

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_label_group;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        loadViewHelper = new LoadViewHelper(this);
        loadViewHelper.setOnNextClickListener(this);
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        layoutRefresh.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light,
                                              android.R.color.holo_orange_light, android.R.color.holo_green_light);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        initAdapter();
        if (BaseUtils.isNetworkAvailable(this)) {
            getPatientLabel(true);
        }
        else {
            loadViewHelper.load(LoadViewHelper.NONE_NETWORK);
        }
    }

    /**
     * 适配器处理
     */
    private void initAdapter() {
        patientLabelAdapter = new PatientLabelAdapter(R.layout.item_patient_label, labelBeans);
        patientLabelAdapter.setLoadMoreView(new CustomLoadMoreView());
        patientLabelAdapter.setOnLoadMoreListener(this, recyclerView);
        patientLabelAdapter.setOnItemClickListener(this);
        patientLabelAdapter.setOnItemLongClickListener(this);
        patientLabelAdapter.loadMoreEnd();
        recyclerView.setAdapter(patientLabelAdapter);
    }

    @Override
    public void initListener() {
        super.initListener();
        layoutRefresh.setOnRefreshListener(this);
    }

    /**
     * 获取标签分组
     */
    private void getPatientLabel(boolean showLoading) {
        RequestUtils.getPatientLabel(this, loginBean.getToken(), BaseData.BASE_PAGE_DATA_NUM, page, showLoading, this);
    }

    /**
     * 查询发起的转诊记录
     */
    private void deletePatientLabel(long tagId) {
        RequestUtils.deleteLabel(this, loginBean.getToken(), tagId, this);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Intent intent = new Intent(this, LabelPatientActivity.class);
        intent.putExtra(CommonData.KEY_LABEL_BEAN, labelBeans.get(position));
        startActivityForResult(intent, REQUEST_CODE_UPDATE);
    }

    @Override
    public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
        new HintDialog(this).setTitleString(getString(R.string.txt_hint))
                            .setContentString(getString(R.string.txt_label_delete_hint))
                            .setEnterBtnTxt(getString(R.string.txt_sure_delete))
                            .setOnEnterClickListener(() -> deletePatientLabel(labelBeans.get(position).getId()))
                            .show();
        return true;
    }

    /**
     * 重新加载
     */
    @Override
    public void onNextClick() {
        getPatientLabel(true);
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        switch (task) {
            case GET_PATIENT_LABEL:
                BaseListBean<LabelBean> baseListBean = (BaseListBean<LabelBean>)response.getData();
                if (page == BASE_ONE) {
                    labelBeans.clear();
                }
                labelBeans.addAll(baseListBean.getRecords());
                patientLabelAdapter.setNewData(labelBeans);
                if (baseListBean.getRecords() != null &&
                    baseListBean.getRecords().size() >= BaseData.BASE_PAGE_DATA_NUM) {
                    patientLabelAdapter.loadMoreComplete();
                }
                else {
                    if (labelBeans.size() > BaseData.BASE_PAGE_DATA_NUM) {
                        patientLabelAdapter.loadMoreEnd();
                    }
                    else {
                        patientLabelAdapter.setEnableLoadMore(false);
                    }
                }
                if (labelBeans != null && labelBeans.size() > 0) {
                    recyclerView.setVisibility(View.VISIBLE);
                }
                else {
                    recyclerView.setVisibility(View.GONE);
                    loadViewHelper.load(LoadViewHelper.NONE_RECORDING);
                }
                break;
            case DELETE_PATIENT_LABEL:
                ToastUtil.toast(this, response.getMsg());
                getPatientLabel(false);
                break;
            default:
                break;
        }
    }

    @Override
    public void onResponseEnd(Tasks task) {
        super.onResponseEnd(task);
        layoutRefresh.setRefreshing(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_UPDATE) {
            getPatientLabel(true);
        }
    }

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        page = 1;
        getPatientLabel(false);
    }

    /**
     * 加载更多
     */
    @Override
    public void onLoadMoreRequested() {
        page++;
        getPatientLabel(false);
    }
}
