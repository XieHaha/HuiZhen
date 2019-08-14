package com.yht.yihuantong.ui.main.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yht.frame.api.ApiManager;
import com.yht.frame.api.LitePalHelper;
import com.yht.frame.api.notify.IChange;
import com.yht.frame.api.notify.RegisterType;
import com.yht.frame.data.BaseData;
import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.CommonData;
import com.yht.frame.data.Tasks;
import com.yht.frame.data.bean.PatientBean;
import com.yht.frame.http.retrofit.RequestUtils;
import com.yht.frame.ui.BaseFragment;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.widgets.dialog.HintDialog;
import com.yht.frame.widgets.edittext.AbstractTextWatcher;
import com.yht.frame.widgets.edittext.SuperEditText;
import com.yht.frame.widgets.recyclerview.IndexBar;
import com.yht.frame.widgets.recyclerview.SideBar;
import com.yht.frame.widgets.recyclerview.decoration.SideBarItemDecoration;
import com.yht.frame.widgets.recyclerview.loadview.CustomLoadMoreView;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ui.adapter.PatientAdapter;
import com.yht.yihuantong.ui.patient.PatientPersonalActivity;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/5/17 14:55
 * @des 医生列表
 */
public class DoctorFragment extends BaseFragment
        implements BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemChildClickListener,
                   BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.layout_bg)
    RelativeLayout layoutBg;
    @BindView(R.id.et_search_patient)
    SuperEditText etSearchPatient;
    @BindView(R.id.layout_search)
    RelativeLayout layoutSearch;
    @BindView(R.id.tv_none_patient)
    TextView tvNonePatient;
    @BindView(R.id.layout_refresh)
    SwipeRefreshLayout layoutRefresh;
    @BindView(R.id.side_bar)
    SideBar sideBar;
    @BindView(R.id.index_bar)
    IndexBar indexBar;
    private View headerView;
    /**
     * 适配器
     */
    private PatientAdapter patientAdapter;
    /**
     * recycler
     */
    private LinearLayoutManager layoutManager;
    /**
     * 分隔线
     */
    private SideBarItemDecoration decoration;
    /**
     * 所有患者数据
     */
    private List<PatientBean> patientBeans = new ArrayList<>();
    /**
     * 患者数据主动更新
     */
    private IChange<String> patientDataUpdate = data -> getPatients();

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
        recyclerview.addItemDecoration(decoration = new SideBarItemDecoration(getContext()));
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        iNotifyChangeListenerServer = ApiManager.getInstance().getServer();
        initEvents();
        initAdapter();
        initPatientData();
    }

    /**
     * 患者数据处理
     */
    private void initPatientData() {
        //是否有缓存
        boolean cache = sharePreferenceUtil.getBoolean(CommonData.KEY_UPDATE_PATIENT_DATA);
        if (cache) {
            getPatientsByLocal();
        }
        else {
            getPatients();
        }
    }

    @Override
    public void initListener() {
        super.initListener();
        etSearchPatient.addTextChangedListener(new AbstractTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s.toString())) {
                    layoutBg.setVisibility(View.VISIBLE);
                    getPatientsByLocal();
                }
                else {
                    sortSearchData(s.toString());
                    layoutBg.setVisibility(View.GONE);
                    patientAdapter.setNewData(patientBeans);
                    patientAdapter.loadMoreEnd();
                }
            }
        });
        //注册患者状态监听
        iNotifyChangeListenerServer.registerPatientListChangeListener(patientDataUpdate, RegisterType.REGISTER);
    }

    /**
     * 适配器
     */
    private void initAdapter() {
        //患者列表
        patientAdapter = new PatientAdapter(R.layout.item_patient, patientBeans);
        patientAdapter.setOnItemClickListener(this);
        patientAdapter.setOnItemChildClickListener(this);
        headerView = LayoutInflater.from(getContext()).inflate(R.layout.view_patient_header, null);
        //头部搜索按钮
        TextView searchText = headerView.findViewById(R.id.tv_search_patient);
        searchText.setOnClickListener(this);
        patientAdapter.addHeaderView(headerView);
        patientAdapter.setLoadMoreView(new CustomLoadMoreView());
        patientAdapter.setOnLoadMoreListener(this, recyclerview);
        recyclerview.setAdapter(patientAdapter);
    }

    /**
     * 获取最新数据
     */
    private void getPatients() {
        RequestUtils.getPatientListByDoctorCode(getContext(), loginBean.getDoctorCode(), loginBean.getToken(), this);
    }

    /**
     * 本地取缓存数据
     */
    private void getPatientsByLocal() {
        //先从本地取
        patientBeans = DataSupport.findAll(PatientBean.class);
        if (patientBeans != null) {
            sortData();
            patientAdapter.setNewData(patientBeans);
            if (patientBeans.size() > BaseData.BASE_PAGE_DATA_NUM) {
                patientAdapter.loadMoreEnd();
            }
            else {
                patientAdapter.setEnableLoadMore(false);
            }
            etSearchPatient.setHint(String.format(getString(R.string.title_patient), patientBeans.size()));
        }
    }

    /**
     * 对数据进行排序
     */
    private void sortData() {
        if (patientBeans != null && patientBeans.size() > 0) {
            recyclerview.setVisibility(View.VISIBLE);
            tvNonePatient.setVisibility(View.GONE);
        }
        else {
            recyclerview.setVisibility(View.GONE);
            tvNonePatient.setVisibility(View.VISIBLE);
            tvNonePatient.setText(R.string.txt_none_patient);
        }
        //对数据源进行排序
        BaseUtils.sortData(patientBeans);
        //返回一个包含所有Tag字母在内的字符串并赋值给tagsStr
        String tagsStr = BaseUtils.getTags(patientBeans);
        sideBar.setIndexStr(tagsStr);
        decoration.setDatas(patientBeans, tagsStr);
    }

    private void sortSearchData(String tag) {
        patientBeans = LitePalHelper.findPatients(tag);
        if (patientBeans != null && patientBeans.size() > 0) {
            recyclerview.setVisibility(View.VISIBLE);
            tvNonePatient.setVisibility(View.GONE);
            layoutBg.setVisibility(View.VISIBLE);
        }
        else {
            recyclerview.setVisibility(View.GONE);
            layoutBg.setVisibility(View.GONE);
            tvNonePatient.setVisibility(View.VISIBLE);
            tvNonePatient.setText(R.string.txt_search_none_patient);
        }
        //对数据源进行排序
        BaseUtils.sortData(patientBeans);
        //返回一个包含所有Tag字母在内的字符串并赋值给tagsStr
        String tagsStr = BaseUtils.getTags(patientBeans);
        sideBar.setIndexStr(tagsStr);
        decoration.setDatas(patientBeans, tagsStr);
    }

    /**
     * 开启搜索
     */
    private void openSearch() {
        //搜索时 关闭下拉刷新
        layoutRefresh.setEnabled(false);
        layoutSearch.setVisibility(View.VISIBLE);
        layoutBg.setVisibility(View.VISIBLE);
        etSearchPatient.requestFocus();
        showSoftInputFromWindow(getContext(), etSearchPatient);
        //显示输入框 隐藏原有输入框
        decoration.setHasHeader(false);
        patientAdapter.removeHeaderView(headerView);
        displaySearchLayout();
    }

    /**
     * 关闭搜索
     */
    private void closeSearch() {
        layoutRefresh.setEnabled(true);
        //隐藏搜索框时重新添加头部
        decoration.setHasHeader(true);
        if (patientAdapter.getHeaderLayoutCount() == 0) {
            patientAdapter.addHeaderView(headerView);
        }
        patientAdapter.notifyDataSetChanged();
        etSearchPatient.setText("");
        //隐藏软键盘
        hideSoftInputFromWindow(getContext(), etSearchPatient);
        //开启隐藏动画
        hideSearchLayout();
    }

    /**
     * 列表侧边栏附表滚动
     */
    public void initEvents() {
        sideBar.setIndexChangeListener(new SideBar.IndexChangeListener() {
            @Override
            public void indexChanged(String tag) {
                if (TextUtils.isEmpty(tag) || patientBeans.size() <= 0) { return; }
                for (int i = 0; i < patientBeans.size(); i++) {
                    if (tag.equals(patientBeans.get(i).getIndexTag())) {
                        layoutManager.scrollToPositionWithOffset(i + 1, 0);
                        return;
                    }
                }
            }

            @Override
            public void indexShow(float y, String tag, int position) {
                indexBar.setDrawData(y, tag, position);
            }

            @Override
            public void indexHide() {
                if (mDelay != null) {
                    indexBar.removeCallbacks(mDelay);
                }
                indexBar.postDelayed(mDelay = () -> indexBar.setTagStatus(false), 1000);
            }
        });
    }

    private Runnable mDelay;

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.tv_search_patient) {
            openSearch();
        }
    }

    @OnClick({ R.id.tv_search_cancel, R.id.layout_bg })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_search_cancel:
            case R.id.layout_bg:
                closeSearch();
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Intent intent = new Intent(getContext(), PatientPersonalActivity.class);
        intent.putExtra(CommonData.KEY_PATIENT_CODE, patientBeans.get(position).getCode());
        intent.putExtra(CommonData.KEY_PATIENT_NAME, patientBeans.get(position).getName());
        startActivity(intent);
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        new HintDialog(getContext()).setPhone(getString(R.string.txt_contact_patient_phone),
                                              patientBeans.get(position).getMobile())
                                    .setOnEnterClickListener(() -> callPhone(patientBeans.get(position).getMobile()))
                                    .show();
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        switch (task) {
            case GET_PATIENT_LIST_BY_DOCTOR_CODE:
                patientBeans = (List<PatientBean>)response.getData();
                if (patientBeans == null) {
                    patientBeans = new ArrayList<>();
                }
                //更新数据库
                new LitePalHelper().updateAll(patientBeans, PatientBean.class);
                sharePreferenceUtil.putBoolean(CommonData.KEY_UPDATE_PATIENT_DATA, true);
                sortData();
                patientAdapter.setNewData(patientBeans);
                if (patientBeans.size() > BaseData.BASE_PAGE_DATA_NUM) {
                    patientAdapter.loadMoreEnd();
                }
                else {
                    patientAdapter.setEnableLoadMore(false);
                }
                etSearchPatient.setHint(String.format(getString(R.string.title_patient), patientBeans.size()));
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

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        getPatients();
    }

    /**
     * 显示搜索框
     */
    private void displaySearchLayout() {
        Animation toUp = AnimationUtils.loadAnimation(getContext(), R.anim.anim_top_in);
        Animation alpha = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
        layoutBg.startAnimation(alpha);
        layoutSearch.startAnimation(toUp);
    }

    /**
     * 隐藏搜索框
     */
    private void hideSearchLayout() {
        Animation toUp = AnimationUtils.loadAnimation(getContext(), R.anim.anim_top_out);
        Animation alpha = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out);
        toUp.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                layoutSearch.setVisibility(View.GONE);
                layoutBg.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        layoutBg.startAnimation(alpha);
        layoutSearch.startAnimation(toUp);
    }

    @Override
    public void onLoadMoreRequested() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //注销患者状态监听
        iNotifyChangeListenerServer.registerPatientListChangeListener(patientDataUpdate, RegisterType.UNREGISTER);
    }
}