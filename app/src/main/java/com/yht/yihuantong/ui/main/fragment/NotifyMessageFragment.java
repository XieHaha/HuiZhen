package com.yht.yihuantong.ui.main.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yht.frame.data.Tasks;
import com.yht.frame.data.bean.NotifyMessageBean;
import com.yht.frame.ui.BaseFragment;
import com.yht.frame.widgets.recyclerview.loadview.CustomLoadMoreView;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ui.adapter.NotifyMessageAdapter;
import com.yht.yihuantong.ui.transfer.TransferToDetailActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author 顿顿
 * @date 19/5/17 14:55
 * @des 消息列表
 */
public class NotifyMessageFragment extends BaseFragment
        implements BaseQuickAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener,
                   BaseQuickAdapter.RequestLoadMoreListener {
    @BindView(R.id.layout_refresh)
    SwipeRefreshLayout layoutRefresh;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.tv_none_message)
    TextView tvNoneMessage;
    private NotifyMessageAdapter notifyMessageAdapter;
    private List<NotifyMessageBean> messageList;

    @Override
    public int getLayoutID() {
        return R.layout.fragment_message_notify;
    }

    @Override
    public void initView(View view, @NonNull Bundle savedInstanceState) {
        super.initView(view, savedInstanceState);
        layoutRefresh.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light,
                                              android.R.color.holo_orange_light, android.R.color.holo_green_light);
        layoutRefresh.setOnRefreshListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        initData();
        initAdapter();
    }

    /**
     * 适配器处理
     */
    private void initAdapter() {
        notifyMessageAdapter = new NotifyMessageAdapter(messageList);
        notifyMessageAdapter.setLoadMoreView(new CustomLoadMoreView());
        notifyMessageAdapter.setOnLoadMoreListener(this, recyclerView);
        notifyMessageAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(notifyMessageAdapter);
        notifyMessageAdapter.loadMoreEnd();
    }

    /**
     * 数据处理
     */
    private void initData() {
        messageList = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            NotifyMessageBean bean = new NotifyMessageBean();
            if (i == 0) {
                bean.setTime("1561969120000");
                bean.setItemType(NotifyMessageBean.REPORT);
            }
            else if (i == 1) {
                bean.setTime("1561969080000");
                bean.setItemType(NotifyMessageBean.CURRENCY);
            }
            else if (i == 2) {
                bean.setTime("1561969000000");
                bean.setItemType(NotifyMessageBean.CURRENCY);
            }
            else if (i == 3) {
                bean.setTime("1561962000000");
                bean.setItemType(NotifyMessageBean.REPORT);
            }
            else if (i == 4) {
                bean.setTime("1561879120000");
                bean.setItemType(NotifyMessageBean.CURRENCY);
            }
            else if (i == 5) {
                bean.setTime("1561819120000");
                bean.setItemType(NotifyMessageBean.REPORT);
            }
            else if (i == 6) {
                bean.setTime("1560819120000");
                bean.setItemType(NotifyMessageBean.REPORT);
            }
            else {
                bean.setTime("1520819120000");
                bean.setItemType(NotifyMessageBean.REPORT);
            }
            messageList.add(bean);
        }
        if (messageList != null && messageList.size() > 0) {
            tvNoneMessage.setVisibility(View.GONE);
        }
        else {
            tvNoneMessage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        startActivity(new Intent(getContext(), TransferToDetailActivity.class));
    }

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
    }

    /**
     * 加载更多
     */
    @Override
    public void onLoadMoreRequested() {
    }

    @Override
    public void onResponseEnd(Tasks task) {
        super.onResponseEnd(task);
        layoutRefresh.setRefreshing(false);
    }
}
