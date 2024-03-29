package com.yht.yihuantong.ui.transfer.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yht.frame.data.BaseData;
import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.CommonData;
import com.yht.frame.data.Tasks;
import com.yht.frame.data.bean.TransferDetailBean;
import com.yht.frame.data.type.TransferOrderStatus;
import com.yht.frame.http.retrofit.RequestUtils;
import com.yht.frame.ui.BaseFragment;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.widgets.LoadViewHelper;
import com.yht.frame.widgets.dialog.HintDialog;
import com.yht.frame.widgets.recyclerview.loadview.CustomLoadMoreView;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ui.adapter.TransferReceivedAdapter;
import com.yht.yihuantong.ui.transfer.TransferReceiveDetailActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author 顿顿
 * @date 19/5/17 14:55
 * @des 转诊已接收
 */
public class TransferReceivedFragment extends BaseFragment
        implements BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener,
                   BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemChildClickListener,
                   LoadViewHelper.OnNextClickListener {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.layout_refresh)
    SwipeRefreshLayout layoutRefresh;
    @BindView(R.id.layout_none_record)
    RelativeLayout layoutNoneRecord;
    private TransferReceivedAdapter transferReceivedAdapter;
    /**
     * 已接收转诊居民
     */
    private List<TransferDetailBean> transferBeans = new ArrayList<>();
    /**
     * 页码
     */
    private int page = 1;
    /**
     * 订单更新
     */
    private static final int REQUEST_CODE_UPDATE = 100;

    @Override
    public int getLayoutID() {
        return R.layout.fragment_transfer_received;
    }

    @Override
    public void initView(View view, @NonNull Bundle savedInstanceState) {
        super.initView(view, savedInstanceState);
        loadViewHelper = new LoadViewHelper(view);
        loadViewHelper.setOnNextClickListener(this);
        layoutRefresh.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light,
                                              android.R.color.holo_orange_light, android.R.color.holo_green_light);
        layoutRefresh.setOnRefreshListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        transferReceivedAdapter = new TransferReceivedAdapter(R.layout.item_received_patient, transferBeans);
        transferReceivedAdapter.setOnItemClickListener(this);
        transferReceivedAdapter.setOnItemChildClickListener(this);
        transferReceivedAdapter.setLoadMoreView(new CustomLoadMoreView());
        transferReceivedAdapter.setOnLoadMoreListener(this, recyclerView);
        recyclerView.setAdapter(transferReceivedAdapter);
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        if (BaseUtils.isNetworkAvailable(getContext())) {
            getTransferStatusOrderList();
        }
        else {
            layoutNoneRecord.setVisibility(View.VISIBLE);
            loadViewHelper.load(LoadViewHelper.NONE_NETWORK);
        }
    }

    /**
     * 查询已接收转诊记录
     */
    private void getTransferStatusOrderList() {
        RequestUtils.getTransferStatusOrderList(getContext(), loginBean.getToken(),
                                                TransferOrderStatus.TRANSFER_STATUS_RECEIVED,
                                                BaseData.BASE_PAGE_DATA_NUM, page, this);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Intent intent = new Intent(getContext(), TransferReceiveDetailActivity.class);
        intent.putExtra(CommonData.KEY_ORDER_ID, transferBeans.get(position).getOrderNo());
        startActivityForResult(intent, REQUEST_CODE_UPDATE);
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        TransferDetailBean bean = transferBeans.get(position);
        new HintDialog(getContext()).setPhone(getString(R.string.txt_contact_patient_phone), bean.getPatientMobile())
                                    .setOnEnterClickListener(() -> callPhone(bean.getPatientMobile()))
                                    .show();
    }

    @Override
    public void onNextClick() {
        getTransferStatusOrderList();
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        if (task == Tasks.GET_TRANSFER_STATUS_ORDER_LIST) {
            List<TransferDetailBean> list = (List<TransferDetailBean>)response.getData();
            if (page == BaseData.BASE_ONE) {
                transferBeans.clear();
            }
            transferBeans.addAll(list);
            transferReceivedAdapter.setNewData(transferBeans);
            if (list != null && list.size() >= BaseData.BASE_PAGE_DATA_NUM) {
                transferReceivedAdapter.loadMoreComplete();
            }
            else {
                if (transferBeans.size() > BaseData.BASE_PAGE_DATA_NUM) {
                    transferReceivedAdapter.loadMoreEnd();
                }
                else {
                    transferReceivedAdapter.setEnableLoadMore(false);
                }
            }
            if (transferBeans.size() == 0) {
                layoutNoneRecord.setVisibility(View.VISIBLE);
                loadViewHelper.load(LoadViewHelper.NONE_RECORDING);
            }
            else {
                layoutNoneRecord.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onResponseEnd(Tasks task) {
        super.onResponseEnd(task);
        layoutRefresh.setRefreshing(false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_UPDATE) {
            getTransferStatusOrderList();
        }
    }

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        page = 1;
        getTransferStatusOrderList();
    }

    /**
     * 加载更多
     */
    @Override
    public void onLoadMoreRequested() {
        page++;
        getTransferStatusOrderList();
    }
}
