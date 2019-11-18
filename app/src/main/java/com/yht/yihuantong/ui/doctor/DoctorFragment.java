package com.yht.yihuantong.ui.doctor;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lijiankun24.shadowlayout.ShadowLayout;
import com.yht.frame.api.ApiManager;
import com.yht.frame.api.LitePalHelper;
import com.yht.frame.api.notify.IChange;
import com.yht.frame.api.notify.RegisterType;
import com.yht.frame.data.BaseData;
import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.CommonData;
import com.yht.frame.data.Tasks;
import com.yht.frame.data.bean.DoctorBean;
import com.yht.frame.http.retrofit.RequestUtils;
import com.yht.frame.ui.BaseFragment;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.widgets.recyclerview.SideBar;
import com.yht.frame.widgets.recyclerview.decoration.SideBarDoctorDecoration;
import com.yht.frame.widgets.recyclerview.loadview.CustomLoadMoreView;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ui.adapter.DoctorAdapter;
import com.yht.yihuantong.ui.main.listener.OnSearchListener;
import com.yht.yihuantong.ui.patient.ChatContainerActivity;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author 顿顿
 * @date 19/5/17 14:55
 * @description 医生列表
 */
public class DoctorFragment extends BaseFragment
        implements BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.RequestLoadMoreListener,
                   SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.tv_none_patient)
    TextView tvNonePatient;
    @BindView(R.id.layout_refresh)
    SwipeRefreshLayout layoutRefresh;
    @BindView(R.id.side_bar)
    SideBar sideBar;
    @BindView(R.id.tv_index)
    TextView tvIndex;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.layout_index)
    ShadowLayout layoutIndex;
    private View headerView, spaceView;
    private TextView searchText;
    private RelativeLayout layoutHeader;
    /**
     * 适配器
     */
    private DoctorAdapter doctorAdapter;
    /**
     * recycler
     */
    private LinearLayoutManager layoutManager;
    /**
     * 分隔线
     */
    private SideBarDoctorDecoration decoration;
    /**
     * 搜索回调
     */
    private OnSearchListener onSearchListener;
    /**
     * 所有医生数据
     */
    private List<DoctorBean> doctorBeans = new ArrayList<>();
    /**
     * 医生数据主动更新
     */
    private IChange<String> doctorDataUpdate = data -> getDoctorsByNetWork();

    @Override
    public int getLayoutID() {
        return R.layout.fragment_doctor;
    }

    @Override
    public void initView(View view, @NonNull Bundle savedInstanceState) {
        super.initView(view, savedInstanceState);
        layoutRefresh.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light,
                                              android.R.color.holo_orange_light, android.R.color.holo_green_light);
        layoutRefresh.setOnRefreshListener(this);
        recyclerview.setLayoutManager(layoutManager = new LinearLayoutManager(getContext()));
        recyclerview.addItemDecoration(decoration = new SideBarDoctorDecoration(getContext()));
        recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                //第一个可见的位置
                int position = layoutManager.findFirstVisibleItemPosition();
                if (position < 0) { return; }
                if (position == 0) {
                    sideBar.setCurPosition(BASE_SEARCH_TAG);
                }
                else {
                    sideBar.setCurPosition(doctorBeans.get(position - 1).getIndexTag());
                }
            }
        });
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        iNotifyChangeListenerServer = ApiManager.getInstance().getServer();
        initEvents();
        initAdapter();
        initDoctorData();
    }

    /**
     * 医生数据处理
     */
    private void initDoctorData() {
        //是否有缓存
        boolean cache = sharePreferenceUtil.getBoolean(CommonData.KEY_UPDATE_DOCTOR_DATA);
        if (cache) {
            getDoctorsByLocal();
        }
        else {
            getDoctorsByNetWork();
        }
    }

    @Override
    public void initListener() {
        super.initListener();
        //注册医生状态监听
        iNotifyChangeListenerServer.registerDoctorListChangeListener(doctorDataUpdate, RegisterType.REGISTER);
    }

    /**
     * 适配器
     */
    private void initAdapter() {
        //医生列表
        doctorAdapter = new DoctorAdapter(R.layout.item_doctor, doctorBeans);
        doctorAdapter.setOnItemClickListener(this);
        headerView = LayoutInflater.from(getContext()).inflate(R.layout.view_doctor_header, null);
        layoutHeader = headerView.findViewById(R.id.layout_header);
        spaceView = headerView.findViewById(R.id.view_space);
        //头部搜索按钮
        searchText = headerView.findViewById(R.id.tv_search_patient);
        searchText.setOnClickListener(this);
        doctorAdapter.addHeaderView(headerView);
        doctorAdapter.setLoadMoreView(new CustomLoadMoreView());
        doctorAdapter.setOnLoadMoreListener(this, recyclerview);
        recyclerview.setAdapter(doctorAdapter);
    }

    /**
     * 获取最新数据
     */
    private void getDoctorsByNetWork() {
        RequestUtils.getDoctorListByDoctorCode(getContext(), loginBean.getToken(), this);
    }

    public void setOnSearchListener(OnSearchListener onSearchListener) {
        this.onSearchListener = onSearchListener;
    }

    /**
     * 本地取缓存数据
     */
    public void getDoctorsByLocal() {
        //先从本地取
        doctorBeans = LitePal.findAll(DoctorBean.class);
        if (doctorBeans != null) {
            sortData();
            doctorAdapter.setNewData(doctorBeans);
            if (doctorBeans.size() > BaseData.BASE_PAGE_DATA_NUM) {
                doctorAdapter.loadMoreEnd();
            }
            else {
                doctorAdapter.setEnableLoadMore(false);
            }
            searchText.setHint(String.format(getString(R.string.txt_doctor_search_hint), doctorBeans.size()));
        }
    }

    /**
     * 对数据进行排序
     */
    private void sortData() {
        if (doctorBeans != null && doctorBeans.size() > 0) {
            recyclerview.setVisibility(View.VISIBLE);
            tvNonePatient.setVisibility(View.GONE);
        }
        else {
            recyclerview.setVisibility(View.GONE);
            tvNonePatient.setVisibility(View.VISIBLE);
            tvNonePatient.setText(R.string.txt_none_doctor_friend);
        }
        //对数据源进行排序
        BaseUtils.sortDoctorData(doctorBeans);
        //返回一个包含所有Tag字母在内的字符串并赋值给tagsStr
        String tagsStr;
        if (doctorBeans != null && doctorBeans.size() > 0) {
            tagsStr = BASE_SEARCH_TAG + BaseUtils.getDoctorTags(doctorBeans);
        }
        else {
            tagsStr = "";
        }
        sideBar.setIndexStr(tagsStr, BaseUtils.dp2px(getContext(), 72));
        decoration.setDatas(doctorBeans, tagsStr);
    }

    public void sortSearchData(String tag) {
        doctorBeans = LitePalHelper.findDoctors(tag);
        if (doctorBeans != null && doctorBeans.size() > 0) {
            recyclerview.setVisibility(View.VISIBLE);
            tvNonePatient.setVisibility(View.GONE);
        }
        else {
            recyclerview.setVisibility(View.GONE);
            tvNonePatient.setVisibility(View.VISIBLE);
            tvNonePatient.setText(R.string.txt_search_none_doctor);
        }
        //对数据源进行排序
        BaseUtils.sortDoctorData(doctorBeans);
        //返回一个包含所有Tag字母在内的字符串并赋值给tagsStr
        String tagsStr = BaseUtils.getDoctorTags(doctorBeans);
        sideBar.setIndexStr(tagsStr, BaseUtils.dp2px(getContext(), 72));
        decoration.setDatas(doctorBeans, tagsStr);
        doctorAdapter.setNewData(doctorBeans);
        if (doctorBeans.size() > BaseData.BASE_PAGE_DATA_NUM) {
            doctorAdapter.loadMoreEnd();
        }
        else {
            doctorAdapter.setEnableLoadMore(false);
        }
    }

    /**
     * 开启搜索
     */
    private void openSearch() {
        if (onSearchListener != null) {
            onSearchListener.onSearch(BASE_TWO, doctorBeans.size());
        }
        //搜索时 关闭下拉刷新
        layoutRefresh.setEnabled(false);
        layoutHeader.setVisibility(View.GONE);
        spaceView.setVisibility(View.VISIBLE);
    }

    /**
     * 关闭搜索
     */
    public void closeSearch() {
        layoutRefresh.setEnabled(true);
        layoutHeader.setVisibility(View.VISIBLE);
        spaceView.setVisibility(View.GONE);
    }

    /**
     * 列表侧边栏附表滚动
     */
    public void initEvents() {
        sideBar.setIndexChangeListener(new SideBar.IndexChangeListener() {
            @Override
            public void indexChanged(String tag) {
                if (TextUtils.isEmpty(tag) || doctorBeans.size() <= 0) { return; }
                if (tag.equals(BASE_SEARCH_TAG)) {
                    layoutManager.scrollToPositionWithOffset(0, 0);
                }
                else {
                    for (int i = 0; i < doctorBeans.size(); i++) {
                        if (tag.equals(doctorBeans.get(i).getIndexTag())) {
                            layoutManager.scrollToPositionWithOffset(i + 1, 0);
                            return;
                        }
                    }
                }
            }

            @Override
            public void indexShow(float y, String tag, int position) {
                indexBarVisible(tag, true);
            }

            @Override
            public void indexHide() {
                if (mDelay != null) {
                    layoutIndex.removeCallbacks(mDelay);
                }
                layoutIndex.postDelayed(mDelay = () -> indexBarVisible("", false), 1000);
            }
        });
    }

    private Runnable mDelay;

    private void indexBarVisible(String text, boolean show) {
        if (show) {
            if (BASE_SEARCH_TAG.equals(text)) {
                ivSearch.setVisibility(View.VISIBLE);
                tvIndex.setText("");
            }
            else {
                ivSearch.setVisibility(View.GONE);
                tvIndex.setVisibility(View.VISIBLE);
                tvIndex.setText(text);
            }
            layoutIndex.setVisibility(View.VISIBLE);
        }
        else {
            layoutIndex.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.tv_search_patient) {
            openSearch();
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Intent intent = new Intent(getContext(), ChatContainerActivity.class);
        intent.putExtra(CommonData.KEY_CHAT_ID, doctorBeans.get(position).getDoctorCode());
        intent.putExtra(CommonData.KEY_CHAT_NAME, doctorBeans.get(position).getDoctorName());
        intent.putExtra(CommonData.KEY_DOCTOR_CHAT, true);
        startActivity(intent);
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        if (task == Tasks.GET_DOCTOR_LIST) {
            doctorBeans = (List<DoctorBean>)response.getData();
            if (doctorBeans == null) {
                doctorBeans = new ArrayList<>();
            }
            //更新数据库
            new LitePalHelper<DoctorBean>().updateAll(doctorBeans, DoctorBean.class);
            sharePreferenceUtil.putBoolean(CommonData.KEY_UPDATE_DOCTOR_DATA, true);
            sortData();
            doctorAdapter.setNewData(doctorBeans);
            if (doctorBeans.size() > BaseData.BASE_PAGE_DATA_NUM) {
                doctorAdapter.loadMoreEnd();
            }
            else {
                doctorAdapter.setEnableLoadMore(false);
            }
            searchText.setHint(String.format(getString(R.string.txt_doctor_search_hint), doctorBeans.size()));
        }
    }

    @Override
    public void onResponseEnd(Tasks task) {
        super.onResponseEnd(task);
        layoutRefresh.setRefreshing(false);
    }

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        getDoctorsByNetWork();
    }

    @Override
    public void onLoadMoreRequested() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //注销医生状态监听
        iNotifyChangeListenerServer.registerDoctorListChangeListener(doctorDataUpdate, RegisterType.UNREGISTER);
    }
}
