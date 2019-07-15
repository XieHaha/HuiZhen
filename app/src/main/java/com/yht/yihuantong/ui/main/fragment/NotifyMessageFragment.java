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
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.yht.frame.data.BaseData;
import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.CommonData;
import com.yht.frame.data.Tasks;
import com.yht.frame.data.base.MessageIdBean;
import com.yht.frame.data.bean.NotifyMessageBean;
import com.yht.frame.http.retrofit.RequestUtils;
import com.yht.frame.ui.BaseFragment;
import com.yht.frame.widgets.recyclerview.loadview.CustomLoadMoreView;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ui.adapter.NotifyMessageAdapter;
import com.yht.yihuantong.ui.check.CheckDetailActivity;
import com.yht.yihuantong.ui.currency.IncomeDetailActivity;
import com.yht.yihuantong.ui.currency.WithdrawDetailActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.yht.frame.data.MessageType.MESSAGE_ACCOUNT_CREATE;
import static com.yht.frame.data.MessageType.MESSAGE_CURRENCY_ARRIVED;
import static com.yht.frame.data.MessageType.MESSAGE_CURRENCY_DEDUCTION;
import static com.yht.frame.data.MessageType.MESSAGE_REMOTE_ADVICE;
import static com.yht.frame.data.MessageType.MESSAGE_REMOTE_CANCEL;
import static com.yht.frame.data.MessageType.MESSAGE_REMOTE_DELAY;
import static com.yht.frame.data.MessageType.MESSAGE_REMOTE_INPUT_ADVICE;
import static com.yht.frame.data.MessageType.MESSAGE_REMOTE_INVITE;
import static com.yht.frame.data.MessageType.MESSAGE_REMOTE_REJECT;
import static com.yht.frame.data.MessageType.MESSAGE_REMOTE_START;
import static com.yht.frame.data.MessageType.MESSAGE_REMOTE_SURE;
import static com.yht.frame.data.MessageType.MESSAGE_SERVICE_REPORT;
import static com.yht.frame.data.MessageType.MESSAGE_TRANSFER_APPLY;
import static com.yht.frame.data.MessageType.MESSAGE_TRANSFER_CANCEL;
import static com.yht.frame.data.MessageType.MESSAGE_TRANSFER_OTHER;
import static com.yht.frame.data.MessageType.MESSAGE_TRANSFER_RECEIVED;
import static com.yht.frame.data.MessageType.MESSAGE_TRANSFER_REJECT;
import static com.yht.frame.data.MessageType.MESSAGE_TRANSFER_SYSTEM_CANCEL_R;
import static com.yht.frame.data.MessageType.MESSAGE_TRANSFER_SYSTEM_CANCEL_T;
import static com.yht.frame.data.MessageType.MESSAGE_TRANSFER_UPDATE;
import static com.yht.frame.data.Tasks.GET_APP_MESSAGE_LIST;

/**
 * @author 顿顿
 * @date 19/5/17 14:55
 * @des 消息列表
 */
public class NotifyMessageFragment extends BaseFragment
        implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener,
                   BaseQuickAdapter.OnItemChildClickListener {
    @BindView(R.id.layout_refresh)
    SwipeRefreshLayout layoutRefresh;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.tv_none_message)
    TextView tvNoneMessage;
    private NotifyMessageAdapter notifyMessageAdapter;
    /**
     * 通知消息列表
     */
    private List<NotifyMessageBean> messageList = new ArrayList<>();
    /**
     * 页码
     */
    private int page = 1;
    private String token = "P1wDQpcrTx45XddRgbg6Kt+fSTJ6DDAce3H85a1p04ls4M3tX3xBTPizfExE4cA7WT3jx9t8rXTXzxD+Xi23YYm8ealJOSAiIGoEXpyzcxe+ryCfBnijo8VXl9T8mn14YDNT+fgaVIsMQRmtVBuwr3fLS+9qUUTY5JR+ui4w1+2vP3mgP\\/keCEBSJlOJ1+Pxx1REKgCN6\\/5r1wf+wVJlyMBMzx\\/IaHRNy2jm3YjJlfM70jF1ZBKUY2q51rxQoeh\\/IPZ+HVIWdaj0yXvlb+AqEA==";

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
        initAdapter();
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        getAppMessageList();
    }

    /**
     * 获取消息列表
     */
    private void getAppMessageList() {
        RequestUtils.getAppMessageList(getContext(), token, BaseData.BASE_PAGE_DATA_NUM, page, this);
    }

    /**
     * 单条消息已读
     */
    private void updateAppUnReadMessageById(int id) {
        RequestUtils.updateAppUnReadMessageById(getContext(), token, id, this);
    }

    /**
     * 适配器处理
     */
    private void initAdapter() {
        notifyMessageAdapter = new NotifyMessageAdapter(R.layout.item_notify_message_currency, messageList);
        notifyMessageAdapter.setLoadMoreView(new CustomLoadMoreView());
        notifyMessageAdapter.setOnLoadMoreListener(this, recyclerView);
        notifyMessageAdapter.setOnItemChildClickListener(this);
        recyclerView.setAdapter(notifyMessageAdapter);
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        NotifyMessageBean bean = messageList.get(position);
        if (bean.getState() == BASE_ZERO) {
            updateAppUnReadMessageById(messageList.get(position).getId());
        }
        Intent intent;
        String type = bean.getMsgType();
        if (MESSAGE_SERVICE_REPORT.msgType().equals(type)) {
            intent = new Intent(getContext(), CheckDetailActivity.class);
            intent.putExtra(CommonData.KEY_ORDER_ID, getMessageTypeId(BASE_ZERO, bean.getExtraData()));
            intent.putExtra(CommonData.KEY_PUBLIC, true);
            startActivity(intent);
        }
        else if (MESSAGE_TRANSFER_APPLY.msgType().equals(type)) {
        }
        else if (MESSAGE_TRANSFER_REJECT.msgType().equals(type)) {
        }
        else if (MESSAGE_TRANSFER_RECEIVED.msgType().equals(type)) {
        }
        else if (MESSAGE_TRANSFER_OTHER.msgType().equals(type)) {
        }
        else if (MESSAGE_TRANSFER_UPDATE.msgType().equals(type)) {
        }
        else if (MESSAGE_TRANSFER_CANCEL.msgType().equals(type)) {
        }
        else if (MESSAGE_TRANSFER_SYSTEM_CANCEL_R.msgType().equals(type)) {
        }
        else if (MESSAGE_TRANSFER_SYSTEM_CANCEL_T.msgType().equals(type)) {
        }
        else if (MESSAGE_REMOTE_START.msgType().equals(type)) {
        }
        else if (MESSAGE_REMOTE_SURE.msgType().equals(type)) {
        }
        else if (MESSAGE_REMOTE_REJECT.msgType().equals(type)) {
        }
        else if (MESSAGE_REMOTE_CANCEL.msgType().equals(type)) {
        }
        else if (MESSAGE_REMOTE_ADVICE.msgType().equals(type)) {
        }
        else if (MESSAGE_REMOTE_DELAY.msgType().equals(type)) {
        }
        else if (MESSAGE_REMOTE_INPUT_ADVICE.msgType().equals(type)) {
        }
        else if (MESSAGE_REMOTE_INVITE.msgType().equals(type)) {
        }
        else if (MESSAGE_CURRENCY_ARRIVED.msgType().equals(type)) {
            intent = new Intent(getContext(), IncomeDetailActivity.class);
            intent.putExtra(CommonData.KEY_DOCTOR_CURRENCY_ID, getMessageTypeId(BASE_ONE, bean.getExtraData()));
            startActivity(intent);
        }
        else if (MESSAGE_CURRENCY_DEDUCTION.msgType().equals(type)) {
            intent = new Intent(getContext(), WithdrawDetailActivity.class);
            intent.putExtra(CommonData.KEY_DOCTOR_CURRENCY_ID, getMessageTypeId(BASE_ZERO, bean.getExtraData()));
            startActivity(intent);
        }
        else if (MESSAGE_ACCOUNT_CREATE.msgType().equals(type)) {
        }
        else {
        }
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        switch (task) {
            case GET_APP_MESSAGE_LIST:
                List<NotifyMessageBean> list = (List<NotifyMessageBean>)response.getData();
                if (page == BaseData.BASE_ONE) {
                    messageList.clear();
                }
                messageList.addAll(list);
                notifyMessageAdapter.setNewData(messageList);
                if (list.size() >= BaseData.BASE_PAGE_DATA_NUM) {
                    notifyMessageAdapter.loadMoreComplete();
                }
                else {
                    notifyMessageAdapter.loadMoreEnd();
                }
                break;
            case UPDATE_APP_UNREAD_MESSAGE_BY_ID:
                getAppMessageList();
                if (onMessageUpdateListener != null) {
                    onMessageUpdateListener.onMessageUpdate();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onResponseEnd(Tasks task) {
        super.onResponseEnd(task);
        if (task == GET_APP_MESSAGE_LIST) { layoutRefresh.setRefreshing(false); }
    }

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        page = 1;
        getAppMessageList();
    }

    /**
     * 加载更多
     */
    @Override
    public void onLoadMoreRequested() {
        page++;
        getAppMessageList();
    }

    /**
     * 获取id
     *
     * @param type 0 id,1 orderNo,2 doctorCode
     * @param data
     */
    private String getMessageTypeId(int type, String data) {
        try {
            switch (type) {
                case BASE_ZERO:
                    return new Gson().fromJson(data, MessageIdBean.class).getId();
                case BASE_ONE:
                    return new Gson().fromJson(data, MessageIdBean.class).getOrderNo();
                case BASE_TWO:
                    return new Gson().fromJson(data, MessageIdBean.class).getDoctorCode();
                default:
                    return "";
            }
        }
        catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return "";
    }

    private OnMessageUpdateListener onMessageUpdateListener;

    public void setOnMessageUpdateListener(OnMessageUpdateListener onMessageUpdateListener) {
        this.onMessageUpdateListener = onMessageUpdateListener;
    }

    public interface OnMessageUpdateListener {
        void onMessageUpdate();
    }
}
