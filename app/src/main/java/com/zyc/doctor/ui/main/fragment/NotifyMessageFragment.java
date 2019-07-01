package com.zyc.doctor.ui.main.fragment;

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
import com.yht.frame.widgets.recyclerview.decoration.TimeItemDecoration;
import com.yht.frame.widgets.recyclerview.loadview.CustomLoadMoreView;
import com.zyc.doctor.R;
import com.zyc.doctor.ui.adapter.NotifyMessageAdapter;
import com.zyc.doctor.ui.transfer.TransferToDetailActivity;

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
    /**
     * 时间分隔
     */
    private TimeItemDecoration timeItemDecoration;
    private NotifyMessageAdapter notifyMessageAdapter;
    private List<NotifyMessageBean> messageList;
    private List<String> titleBars;

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
        timeItemDecoration = new TimeItemDecoration(getContext(), false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(timeItemDecoration);
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
        notifyMessageAdapter.loadMoreEnd();
        recyclerView.setAdapter(notifyMessageAdapter);
    }

    /**
     * 数据处理
     */
    private void initData() {
        messageList = new ArrayList<>();
        titleBars = new ArrayList<>();
        String[] names = {
                "孙尚香", "安其拉", "白起", "不知火舞" };
        for (int i = 0; i < 4; i++) {
            NotifyMessageBean bean = new NotifyMessageBean();
            bean.setTitle(names[i]);
            if (i > 1) {
                titleBars.add("2019-06");
                bean.setTime("2019-06");
                bean.setItemType(NotifyMessageBean.REPORT);
            }
            else {
                titleBars.add("2019-07");
                bean.setTime("2019-07");
                bean.setItemType(NotifyMessageBean.CURRENCY);
            }
            messageList.add(bean);
        }
        //返回一个包含所有Tag字符串并赋值给tagsStr
        //        String tagsStr = BaseUtils.getTags(messageList);
        //        timeItemDecoration.setTitleBar(titleBars, tagsStr);
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
