package com.yht.yihuantong.ui.main.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.yht.frame.api.ApiManager;
import com.yht.frame.api.notify.IChange;
import com.yht.frame.api.notify.RegisterType;
import com.yht.frame.data.BaseData;
import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.CommonData;
import com.yht.frame.data.Tasks;
import com.yht.frame.data.bean.MessageIdBean;
import com.yht.frame.data.bean.NotifyMessageBean;
import com.yht.frame.data.type.MessageType;
import com.yht.frame.http.retrofit.RequestUtils;
import com.yht.frame.ui.BaseFragment;
import com.yht.frame.widgets.recyclerview.loadview.CustomLoadMoreView;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ui.adapter.NotifyMessageAdapter;
import com.yht.yihuantong.ui.check.ReservationServiceDetailActivity;
import com.yht.yihuantong.ui.currency.IncomeDetailActivity;
import com.yht.yihuantong.ui.currency.WithdrawDetailActivity;
import com.yht.yihuantong.ui.hospital.HospitalDetailActivity;
import com.yht.yihuantong.ui.main.listener.OnMessageUpdateListener;
import com.yht.yihuantong.ui.transfer.TransferInitiateDetailActivity;
import com.yht.yihuantong.ui.transfer.TransferReceiveDetailActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.yht.frame.data.Tasks.GET_APP_MESSAGE_LIST;

/**
 * @author 顿顿
 * @date 19/5/17 14:55
 * @des 消息列表
 */
public class NotifyMessageFragment extends BaseFragment
        implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener
        , MessageType,
        BaseQuickAdapter.OnItemClickListener {
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
     * 当前操作的消息 position
     */
    private int curPosition;
    /**
     * 页码
     */
    private int page = 1;
    /**
     * 消息红点
     */
    private IChange<String> messageUpdate = data -> {
        page = 1;
        //获取所有消息
        getAppMessageList();
    };
    //点击通知栏  单条消息已读
    /**
     * 单条消息  通知栏点击
     */
    private IChange<String> singleMessage = this::updateAppUnReadMessageByNotify;

    @Override
    public int getLayoutID() {
        return R.layout.fragment_message_notify;
    }

    @Override
    public void initView(View view, @NonNull Bundle savedInstanceState) {
        super.initView(view, savedInstanceState);
        layoutRefresh.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light, android.R.color.holo_green_light);
        layoutRefresh.setOnRefreshListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        initAdapter();
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        iNotifyChangeListenerServer = ApiManager.getInstance().getServer();
        getAppMessageList();
    }

    @Override
    public void initListener() {
        super.initListener();
        //注册消息状态监听
        iNotifyChangeListenerServer.registerSystemMessageStatusChangeListener(messageUpdate,
                RegisterType.REGISTER);
        iNotifyChangeListenerServer.registerSingleMessageStatusChangeListener(singleMessage,
                RegisterType.REGISTER);
    }

    /**
     * 获取消息列表
     */
    private void getAppMessageList() {
        RequestUtils.getAppMessageList(getContext(), loginBean.getToken(),
                BaseData.BASE_PAGE_DATA_NUM, page, this);
    }

    /**
     * 单条消息已读
     */
    private void updateAppUnReadMessageById(int id) {
        RequestUtils.updateAppUnReadMessageById(getContext(), loginBean.getToken(), id, this);
    }

    /**
     * 单条消息已读 notify
     */
    private void updateAppUnReadMessageByNotify(String id) {
        RequestUtils.updateAppUnReadMessageByNotify(getContext(), loginBean.getToken(), id, this);
    }

    /**
     * 适配器处理
     */
    private void initAdapter() {
        notifyMessageAdapter = new NotifyMessageAdapter(R.layout.item_notify_message_currency,
                messageList);
        notifyMessageAdapter.setLoadMoreView(new CustomLoadMoreView());
        notifyMessageAdapter.setOnLoadMoreListener(this, recyclerView);
        notifyMessageAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(notifyMessageAdapter);
    }

    /**
     * 全部已读  修改本地数据
     */
    public void updateAll() {
        notifyMessageAdapter.setUpdateAll(true);
        notifyMessageAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        NotifyMessageBean bean = messageList.get(position);
        if (bean.getState() == BASE_ZERO) {
            curPosition = position;
            //单条消息已读
            messageList.get(position).setState(1);
            updateAppUnReadMessageById(messageList.get(position).getId());
        }
        if (TextUtils.isEmpty(bean.getExtraData())) {
            //数据为空 不做跳转
            return;
        }
        Intent intent;
        String type = bean.getMsgType();
        String orderNo = getMessageTypeId(bean.getExtraData());
        switch (type) {
            case MESSAGE_SERVICE_REPORT:
                intent = new Intent(getContext(), ReservationServiceDetailActivity.class);
                intent.putExtra(CommonData.KEY_ORDER_ID, orderNo);
                intent.putExtra(CommonData.KEY_PUBLIC, true);
                startActivity(intent);
                break;
            case MESSAGE_SERVICE_ADVICE:
                intent = new Intent(getContext(), ReservationServiceDetailActivity.class);
                intent.putExtra(CommonData.KEY_ORDER_ID, orderNo);
                startActivity(intent);
                break;
            case MESSAGE_TRANSFER_REJECT:
            case MESSAGE_TRANSFER_RECEIVED:
            case MESSAGE_TRANSFER_OTHER:
            case MESSAGE_TRANSFER_UPDATE:
            case MESSAGE_TRANSFER_SYSTEM_CANCEL_T:
                intent = new Intent(getContext(), TransferInitiateDetailActivity.class);
                intent.putExtra(CommonData.KEY_ORDER_ID, orderNo);
                startActivity(intent);
                break;
            case MESSAGE_TRANSFER_APPLY:
            case MESSAGE_TRANSFER_CANCEL:
            case MESSAGE_TRANSFER_SYSTEM_CANCEL_R:
                intent = new Intent(getContext(), TransferReceiveDetailActivity.class);
                intent.putExtra(CommonData.KEY_ORDER_ID, orderNo);
                startActivity(intent);
                break;
            case MESSAGE_CURRENCY_ARRIVED:
                intent = new Intent(getContext(), IncomeDetailActivity.class);
                intent.putExtra(CommonData.KEY_DOCTOR_CURRENCY_ID, orderNo);
                startActivity(intent);
                break;
            case MESSAGE_CURRENCY_DEDUCTION:
                intent = new Intent(getContext(), WithdrawDetailActivity.class);
                intent.putExtra(CommonData.KEY_DOCTOR_CURRENCY_ID, orderNo);
                startActivity(intent);
                break;
            case MESSAGE_COOPERATE_ACCOUNT_CREATE:
                intent = new Intent(getContext(), HospitalDetailActivity.class);
                intent.putExtra(CommonData.KEY_HOSPITAL_CODE, orderNo);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        switch (task) {
            case GET_APP_MESSAGE_LIST:
                List<NotifyMessageBean> list = (List<NotifyMessageBean>) response.getData();
                if (page == BaseData.BASE_ONE) {
                    messageList.clear();
                }
                messageList.addAll(list);
                notifyMessageAdapter.setUpdateAll(false);
                notifyMessageAdapter.setNewData(messageList);
                if (list.size() >= BaseData.BASE_PAGE_DATA_NUM) {
                    notifyMessageAdapter.loadMoreComplete();
                } else {
                    if (messageList.size() > BaseData.BASE_PAGE_DATA_NUM) {
                        notifyMessageAdapter.loadMoreEnd();
                    } else {
                        notifyMessageAdapter.setEnableLoadMore(false);
                    }
                }
                if (messageList != null && messageList.size() > 0) {
                    tvNoneMessage.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                } else {
                    tvNoneMessage.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
                if (onMessageUpdateListener != null) {
                    onMessageUpdateListener.onMessageUpdate();
                }
                break;
            case UPDATE_APP_UNREAD_MESSAGE_BY_ID:
                notifyMessageAdapter.notifyItemChanged(curPosition);
                if (onMessageUpdateListener != null) {
                    onMessageUpdateListener.onMessageUpdate();
                }
                break;
            case UPDATE_APP_UNREAD_MESSAGE_BY_NOTIFY:
                getAppMessageList();
                break;
            default:
                break;
        }
    }

    @Override
    public void onResponseEnd(Tasks task) {
        super.onResponseEnd(task);
        if (task == GET_APP_MESSAGE_LIST) {
            layoutRefresh.setRefreshing(false);
        }
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
     */
    private String getMessageTypeId(String data) {
        if (!TextUtils.isEmpty(data)) {
            return new Gson().fromJson(data, MessageIdBean.class).getOrderNo();
        } else {
            return "";
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        iNotifyChangeListenerServer.registerSystemMessageStatusChangeListener(messageUpdate,
                RegisterType.UNREGISTER);
        iNotifyChangeListenerServer.registerSingleMessageStatusChangeListener(singleMessage,
                RegisterType.UNREGISTER);
    }

    private OnMessageUpdateListener onMessageUpdateListener;

    public void setOnMessageUpdateListener(OnMessageUpdateListener onMessageUpdateListener) {
        this.onMessageUpdateListener = onMessageUpdateListener;
    }
}
