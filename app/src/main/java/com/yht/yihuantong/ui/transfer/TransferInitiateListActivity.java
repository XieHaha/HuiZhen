package com.yht.yihuantong.ui.transfer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yht.frame.data.BaseData;
import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.CommonData;
import com.yht.frame.data.Tasks;
import com.yht.frame.data.bean.ReservationValidateBean;
import com.yht.frame.data.bean.TransferDetailBean;
import com.yht.frame.http.retrofit.RequestUtils;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.widgets.LoadViewHelper;
import com.yht.frame.widgets.recyclerview.decoration.TimeItemDecoration;
import com.yht.frame.widgets.recyclerview.loadview.CustomLoadMoreView;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ui.adapter.TransferInitiateAdapter;
import com.yht.yihuantong.ui.patient.ChatContainerActivity;
import com.yht.yihuantong.ui.reservation.ReservationDisableActivity;
import com.yht.yihuantong.ui.reservation.transfer.ReservationTransferActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/6/13 15:26
 * @description 我发起的转诊
 */
public class TransferInitiateListActivity extends BaseActivity
        implements BaseQuickAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener,
        BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemChildClickListener,
        LoadViewHelper.OnNextClickListener {
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.layout_refresh)
    SwipeRefreshLayout layoutRefresh;
    @BindView(R.id.public_title_bar_title)
    TextView publicTitleBarTitle;
    @BindView(R.id.layout_none)
    LinearLayout layoutNone;
    @BindView(R.id.layout_reserve_transfer)
    LinearLayout layoutReserveTransfer;
    /**
     * 时间分隔
     */
    private TimeItemDecoration timeItemDecoration;
    private TransferInitiateAdapter transferInitiateAdapter;
    /**
     * 我发起的转诊列表
     */
    private List<TransferDetailBean> transferList = new ArrayList<>();
    /**
     * 时间title
     */
    private List<String> titleBars = new ArrayList<>();
    /**
     * 是否能发起转诊
     */
    private boolean applyTransferAble = false;
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
        return R.layout.act_transfer_history;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        loadViewHelper = new LoadViewHelper(this);
        loadViewHelper.setOnNextClickListener(this);
        int num = 0;
        if (getIntent() != null) {
            num = getIntent().getIntExtra(CommonData.KEY_PUBLIC, 0);
        }
        if (num > 0) {
            publicTitleBarTitle.setText(String.format(getString(R.string.title_add_transfer), num));
        } else {
            publicTitleBarTitle.setText(R.string.txt_initiate_transfer);
        }
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        layoutRefresh.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light, android.R.color.holo_green_light);
        timeItemDecoration = new TimeItemDecoration(this, false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(timeItemDecoration);
        initAdapter();
        if (BaseUtils.isNetworkAvailable(this)) {
            getInitiateTransferOrderList(true);
            getValidateHospitalList();
        } else {
            layoutReserveTransfer.setVisibility(View.GONE);
            layoutNone.setVisibility(View.VISIBLE);
            loadViewHelper.load(LoadViewHelper.NONE_NETWORK);
        }
    }

    /**
     * 适配器处理
     */
    private void initAdapter() {
        transferInitiateAdapter = new TransferInitiateAdapter(R.layout.item_transfer_history,
                transferList);
        transferInitiateAdapter.setLoadMoreView(new CustomLoadMoreView());
        transferInitiateAdapter.setOnLoadMoreListener(this, recyclerView);
        transferInitiateAdapter.setOnItemClickListener(this);
        transferInitiateAdapter.setOnItemChildClickListener(this);
        transferInitiateAdapter.loadMoreEnd();
        recyclerView.setAdapter(transferInitiateAdapter);
    }

    @Override
    public void initListener() {
        super.initListener();
        layoutRefresh.setOnRefreshListener(this);
    }

    /**
     * 查询发起的转诊记录
     */
    private void getInitiateTransferOrderList(boolean showLoading) {
        RequestUtils.getInitiateTransferOrderList(this, loginBean.getToken(),
                BaseData.BASE_PAGE_DATA_NUM, page,
                showLoading, this);
    }

    /**
     * 校验医生是否有预约检查和预约转诊的合作医院
     */
    private void getValidateHospitalList() {
        RequestUtils.getValidateHospitalList(this, loginBean.getToken(), false,this);
    }

    /**
     * 数据处理 排序
     */
    private void sortTransferData() {
        titleBars.clear();
        for (TransferDetailBean bean : transferList) {
            long time = BaseUtils.date2TimeStamp(bean.getTransferDate(),
                    BaseUtils.YYYY_MM_DD_HH_MM);
            titleBars.add(BaseUtils.formatDate(time, BaseUtils.YYYY_MM_DD));
        }
        //返回一个包含所有Tag字符串并赋值给tagsStr
        String tag = BaseUtils.getTimeTags(titleBars);
        timeItemDecoration.setTitleBar(titleBars, tag);
    }

    @OnClick(R.id.tv_check_next)
    public void onViewClicked() {
        Intent intent;
        if (applyTransferAble) {
            intent = new Intent(this, ReservationTransferActivity.class);
            startActivity(intent);
        } else {
            intent = new Intent(this, ReservationDisableActivity.class);
            intent.putExtra(CommonData.KEY_RESERVATION_TYPE, BASE_ONE);
            startActivity(intent);
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Intent intent = new Intent(this, TransferInitiateDetailActivity.class);
        intent.putExtra(CommonData.KEY_TRANSFER_ORDER_BEAN, transferList.get(position));
        startActivityForResult(intent, REQUEST_CODE_UPDATE);
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        Intent intent = new Intent(this, ChatContainerActivity.class);
        intent.putExtra(CommonData.KEY_CHAT_ID, transferList.get(position).getPatientCode());
        intent.putExtra(CommonData.KEY_CHAT_NAME, transferList.get(position).getPatientName());
        startActivity(intent);
    }

    /**
     * 重新加载
     */
    @Override
    public void onNextClick() {
        getInitiateTransferOrderList(true);
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        switch (task) {
            case GET_INITIATE_TRANSFER_ORDER_LIST:
                layoutReserveTransfer.setVisibility(View.VISIBLE);
                layoutNone.setVisibility(View.GONE);
                List<TransferDetailBean> list = (List<TransferDetailBean>) response.getData();
                if (page == BaseData.BASE_ONE) {
                    transferList.clear();
                }
                transferList.addAll(list);
                sortTransferData();
                transferInitiateAdapter.setNewData(transferList);
                if (list != null && list.size() >= BaseData.BASE_PAGE_DATA_NUM) {
                    transferInitiateAdapter.loadMoreComplete();
                } else {
                    if (transferList.size() > BaseData.BASE_PAGE_DATA_NUM) {
                        transferInitiateAdapter.loadMoreEnd();
                    } else {
                        transferInitiateAdapter.setEnableLoadMore(false);
                    }
                }
                if (transferList != null && transferList.size() > 0) {
                    recyclerView.setVisibility(View.VISIBLE);
                    layoutNone.setVisibility(View.GONE);
                } else {
                    recyclerView.setVisibility(View.GONE);
                    layoutNone.setVisibility(View.VISIBLE);
                    loadViewHelper.load(LoadViewHelper.NONE_RECORDING);
                }
                break;
            case GET_VALIDATE_HOSPITAL_LIST:
                ReservationValidateBean bean = (ReservationValidateBean) response.getData();
                if (bean != null) {
                    applyTransferAble = bean.isZz();
                }
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
            getInitiateTransferOrderList(true);
        }
    }

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        page = 1;
        getInitiateTransferOrderList(false);
    }

    /**
     * 加载更多
     */
    @Override
    public void onLoadMoreRequested() {
        page++;
        getInitiateTransferOrderList(false);
    }
}
