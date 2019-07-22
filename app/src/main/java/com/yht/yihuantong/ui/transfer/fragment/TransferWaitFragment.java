package com.yht.yihuantong.ui.transfer.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yht.frame.data.BaseData;
import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.CommonData;
import com.yht.frame.data.Tasks;
import com.yht.frame.data.type.TransferOrderStatus;
import com.yht.frame.data.base.TransferBean;
import com.yht.frame.http.retrofit.RequestUtils;
import com.yht.frame.ui.BaseFragment;
import com.yht.frame.widgets.recyclerview.loadview.CustomLoadMoreView;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ui.adapter.TransferWaitAdapter;
import com.yht.yihuantong.ui.patient.PatientPersonalActivity;
import com.yht.yihuantong.ui.transfer.TransferReceiveDetailActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author 顿顿
 * @date 19/5/17 14:55
 * @des 转诊待处理
 */
public class TransferWaitFragment extends BaseFragment
        implements BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener,
                   BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemChildClickListener {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.layout_refresh)
    SwipeRefreshLayout layoutRefresh;
    @BindView(R.id.layout_none_record)
    RelativeLayout layoutNoneRecord;
    private TransferWaitAdapter transferWaitAdapter;
    /**
     * 待处理转诊患者订单
     */
    private List<TransferBean> transferBeans = new ArrayList<>();
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
        return R.layout.fragment_transfer_wait;
    }

    @Override
    public void initView(View view, @NonNull Bundle savedInstanceState) {
        super.initView(view, savedInstanceState);
        layoutRefresh.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light,
                                              android.R.color.holo_orange_light, android.R.color.holo_green_light);
        layoutRefresh.setOnRefreshListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        transferWaitAdapter = new TransferWaitAdapter(R.layout.item_transfer_wait, transferBeans);
        transferWaitAdapter.setOnItemClickListener(this);
        transferWaitAdapter.setOnItemChildClickListener(this);
        transferWaitAdapter.setLoadMoreView(new CustomLoadMoreView());
        transferWaitAdapter.setOnLoadMoreListener(this, recyclerView);
        recyclerView.setAdapter(transferWaitAdapter);
        getTransferStatusOrderList();
    }

    /**
     * 查询待接收转诊记录
     */
    private void getTransferStatusOrderList() {
        RequestUtils.getTransferStatusOrderList(getContext(), loginBean.getToken(),
                                                TransferOrderStatus.TRANSFER_STATUS_WAIT, BaseData.BASE_PAGE_DATA_NUM,
                                                page, this);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Intent intent = new Intent(getContext(), TransferReceiveDetailActivity.class);
        intent.putExtra(CommonData.KEY_ORDER_ID, transferBeans.get(position).getOrderNo());
        startActivityForResult(intent, REQUEST_CODE_UPDATE);
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        Intent intent = new Intent(getContext(), PatientPersonalActivity.class);
        intent.putExtra(CommonData.KEY_PATIENT_CODE, transferBeans.get(position).getPatientCode());
        intent.putExtra(CommonData.KEY_PATIENT_NAME, transferBeans.get(position).getPatientName());
        startActivity(intent);
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        if (task == Tasks.GET_TRANSFER_STATUS_ORDER_LIST) {
            List<TransferBean> list = (List<TransferBean>)response.getData();
            if (page == BaseData.BASE_ONE) {
                transferBeans.clear();
            }
            transferBeans.addAll(list);
            transferWaitAdapter.setNewData(transferBeans);
            if (list != null && list.size() == BaseData.BASE_PAGE_DATA_NUM) {
                transferWaitAdapter.loadMoreComplete();
            }
            else {
                transferWaitAdapter.loadMoreEnd();
            }
            if (transferBeans.size() == 0) {
                layoutNoneRecord.setVisibility(View.VISIBLE);
                if (listener != null) {
                    listener.onPendingTransferOrder(false);
                }
            }
            else {
                layoutNoneRecord.setVisibility(View.GONE);
                if (listener != null) {
                    listener.onPendingTransferOrder(true);
                }
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

    private OnPendingTransferOrderListener listener;

    public void setOnPendingTransferOrderListener(OnPendingTransferOrderListener listener) {
        this.listener = listener;
    }

    public interface OnPendingTransferOrderListener {
        /**
         * @param visible 小红点
         */
        void onPendingTransferOrder(boolean visible);
    }
}
