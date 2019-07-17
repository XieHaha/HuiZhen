package com.yht.yihuantong.chat;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.widget.EaseConversationList;
import com.yht.yihuantong.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * conversation list fragment
 *
 * @author dundun
 */
public class EaseConversationListFragment extends EaseBaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    private final static int MSG_REFRESH = 2;
    private SwipeRefreshLayout layoutRefresh;
    protected EditText query;
    protected TextView tvHintTxt;
    protected ImageButton clearSearch;
    protected boolean hidden;
    protected List<EMConversation> conversationList = new ArrayList<EMConversation>();
    protected EaseConversationList conversationListView;
    protected FrameLayout errorItemContainer;
    private View footerView;
    protected boolean isConflict;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ease_fragment_conversation_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false)) { return; }
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void initView() {
        inputMethodManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        layoutRefresh = getView().findViewById(R.id.refresh_layout);
        layoutRefresh.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light,
                                              android.R.color.holo_orange_light, android.R.color.holo_green_light);
        layoutRefresh.setOnRefreshListener(this);
        footerView = LayoutInflater.from(getContext()).inflate(R.layout.view_load_end, null);
        conversationListView = getView().findViewById(R.id.list);
        query = getView().findViewById(R.id.query);
        tvHintTxt = getView().findViewById(R.id.tv_none_message);
        // button to clear content in search bar
        clearSearch = getView().findViewById(R.id.search_clear);
        errorItemContainer = getView().findViewById(R.id.fl_error_item);
    }

    @Override
    protected void setUpView() {
        conversationList.addAll(loadConversationList());
        conversationListView.init(conversationList);
        if (listItemClickListener != null) {
            conversationListView.setOnItemClickListener((parent, view, position, id) -> {
                EMConversation conversation = conversationListView.getItem(position);
                listItemClickListener.onListItemClicked(conversation);
            });
            if (listItemLongClickListener != null) {
                conversationListView.setOnItemLongClickListener((parent, view, position, id) -> {
                    EMConversation conversation = conversationListView.getItem(position);
                    listItemLongClickListener.onListItemLongClick(view, conversation);
                    return true;
                });
            }
        }
        EMClient.getInstance().addConnectionListener(connectionListener);
        conversationListView.addFooterView(footerView);
        conversationListView.setOnTouchListener((v, event) -> {
            hideSoftKeyboard();
            return false;
        });
    }

    protected EMConnectionListener connectionListener = new EMConnectionListener() {
        @Override
        public void onDisconnected(int error) {
            if (error == EMError.USER_REMOVED || error == EMError.USER_LOGIN_ANOTHER_DEVICE ||
                error == EMError.SERVER_SERVICE_RESTRICTED || error == EMError.USER_KICKED_BY_CHANGE_PASSWORD ||
                error == EMError.USER_KICKED_BY_OTHER_DEVICE) {
                isConflict = true;
            }
            else {
                handler.sendEmptyMessage(0);
            }
        }

        @Override
        public void onConnected() {
            handler.sendEmptyMessage(1);
        }
    };
    private EaseConversationListItemClickListener listItemClickListener;
    private EaseConversationListItemLongClickListener listItemLongClickListener;
    protected Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    onConnectionDisconnected();
                    break;
                case 1:
                    onConnectionConnected();
                    break;
                case MSG_REFRESH: {
                    conversationList.clear();
                    conversationList.addAll(loadConversationList());
                    conversationListView.refresh();
                    layoutRefresh.setRefreshing(false);
                    break;
                }
                default:
                    break;
            }
            return false;
        }
    });

    /**
     * connected to server
     */
    protected void onConnectionConnected() {
        errorItemContainer.setVisibility(View.GONE);
    }

    /**
     * disconnected with server
     */
    protected void onConnectionDisconnected() {
        errorItemContainer.setVisibility(View.VISIBLE);
    }

    /**
     * refresh ui
     */
    public void refresh() {
        if (!handler.hasMessages(MSG_REFRESH)) {
            handler.sendEmptyMessage(MSG_REFRESH);
        }
    }

    /**
     * load conversation list
     *
     * @return +
     */
    protected List<EMConversation> loadConversationList() {
        // get all conversations
        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        List<Pair<Long, EMConversation>> sortList = new ArrayList<>();
        if (conversations != null && conversations.size() > 0) {
            tvHintTxt.setVisibility(View.GONE);
            conversationListView.setVisibility(View.VISIBLE);
        }
        else {
            conversationListView.setVisibility(View.GONE);
            tvHintTxt.setVisibility(View.VISIBLE);
        }
        //lastMsgTime will change if there is new message during sorting so use synchronized to make sure timestamp of last message won't change.
        synchronized (conversations) {
            for (EMConversation conversation : conversations.values()) {
                if (conversation.getAllMessages().size() != 0) {
                    sortList.add(new Pair<>(conversation.getLastMessage().getMsgTime(), conversation));
                }
            }
        }
        try {
            // Internal is TimSort algorithm, has bug
            sortConversationByLastChatTime(sortList);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        List<EMConversation> list = new ArrayList<>();
        for (Pair<Long, EMConversation> sortItem : sortList) {
            list.add(sortItem.second);
        }
        return list;
    }

    /**
     * sort conversations according time stamp of last message
     *
     * @param conversationList
     */
    private void sortConversationByLastChatTime(List<Pair<Long, EMConversation>> conversationList) {
        Collections.sort(conversationList, (con1, con2) -> {
            if (con1.first.equals(con2.first)) {
                return 0;
            }
            else if (con2.first.longValue() > con1.first.longValue()) {
                return 1;
            }
            else {
                return -1;
            }
        });
    }

    @Override
    protected void hideSoftKeyboard() {
        if (getActivity().getWindow().getAttributes().softInputMode !=
            WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getActivity().getCurrentFocus() != null) {
                inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                                                           InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        this.hidden = hidden;
        if (!hidden && !isConflict) {
            refresh();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!hidden) {
            refresh();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().removeConnectionListener(connectionListener);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (isConflict) {
            outState.putBoolean("isConflict", true);
        }
    }

    @Override
    public void onRefresh() {
        if (!hidden) {
            refresh();
        }
    }

    public interface EaseConversationListItemClickListener {
        /**
         * click event for conversation list
         *
         * @param conversation -- clicked item
         */
        void onListItemClicked(EMConversation conversation);
    }

    public interface EaseConversationListItemLongClickListener {
        void onListItemLongClick(View view, EMConversation conversation);
    }

    /**
     * set conversation list item click listener
     *
     * @param listItemClickListener
     */
    public void setConversationListItemClickListener(EaseConversationListItemClickListener listItemClickListener) {
        this.listItemClickListener = listItemClickListener;
    }

    public void setConversationListItemLongClickListener(
            EaseConversationListItemLongClickListener listItemLongClickListener) {
        this.listItemLongClickListener = listItemLongClickListener;
    }
}
