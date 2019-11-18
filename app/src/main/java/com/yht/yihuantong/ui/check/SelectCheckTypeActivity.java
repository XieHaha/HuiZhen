package com.yht.yihuantong.ui.check;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yht.frame.api.ThreadPoolHelper;
import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.CommonData;
import com.yht.frame.data.Tasks;
import com.yht.frame.data.bean.SelectCheckTypeBean;
import com.yht.frame.data.bean.SelectCheckTypeChildBean;
import com.yht.frame.data.bean.SelectCheckTypeParentBean;
import com.yht.frame.http.retrofit.RequestUtils;
import com.yht.frame.ui.AbstractAnimationListener;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.widgets.edittext.AbstractTextWatcher;
import com.yht.frame.widgets.edittext.SuperEditText;
import com.yht.frame.widgets.view.ExpandableLayout;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ZycApplication;
import com.yht.yihuantong.ui.adapter.SelectCheckTypeAdapter;
import com.yht.yihuantong.ui.adapter.SelectCheckTypeFilterAdapter;
import com.yht.yihuantong.ui.adapter.SelectCheckTypeParentAdapter;
import com.yht.yihuantong.ui.adapter.SelectCheckTypeShopAdapter;
import com.yht.yihuantong.ui.adapter.ShopCheckTypeAdapter;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/6/4 17:53
 * @des 医院选择
 */
public class SelectCheckTypeActivity extends BaseActivity
        implements SelectCheckTypeParentAdapter.OnSelectedCallback, BaseQuickAdapter.OnItemClickListener,
                   AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener,
                   ShopCheckTypeAdapter.OnServiceDeleteListener {
    @BindView(R.id.et_search_check_type)
    SuperEditText etSearchCheckType;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.tv_hospital_btn)
    TextView tvHospitalBtn;
    @BindView(R.id.layout_all_hospital)
    FrameLayout layoutAllHospital;
    @BindView(R.id.tv_service_btn)
    TextView tvServiceBtn;
    @BindView(R.id.layout_all_service)
    FrameLayout layoutAllService;
    @BindView(R.id.layout_filter)
    LinearLayout layoutFilter;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.hospital_recycler_view)
    RecyclerView hospitalRecyclerView;
    @BindView(R.id.search_recycler_view)
    ListView searchRecyclerView;
    @BindView(R.id.tv_selected)
    TextView tvSelected;
    @BindView(R.id.tv_next)
    TextView tvNext;
    @BindView(R.id.filter_recycler_view)
    RecyclerView filterRecyclerView;
    @BindView(R.id.layout_expandable)
    ExpandableLayout layoutExpandable;
    @BindView(R.id.layout_filter_content_root)
    RelativeLayout layoutFilterContentRoot;
    @BindView(R.id.tv_none)
    TextView tvNone;
    @BindView(R.id.tv_none_refresh)
    TextView tvNoneRefresh;
    @BindView(R.id.layout_none)
    RelativeLayout layoutNone;
    @BindView(R.id.layout_search_none)
    RelativeLayout layoutSearchNone;
    @BindView(R.id.layout_shop_bg)
    RelativeLayout layoutShopBg;
    @BindView(R.id.tv_clear_shop)
    TextView tvClearShop;
    @BindView(R.id.shop_recycler_view)
    RecyclerView shopRecyclerView;
    @BindView(R.id.layout_shop)
    RelativeLayout layoutShop;
    @BindView(R.id.layout_none_shop)
    RelativeLayout layoutNoneShop;
    @BindView(R.id.layout_refresh)
    SwipeRefreshLayout layoutRefresh;
    @BindView(R.id.layout_refresh_root)
    RelativeLayout layoutRefreshRoot;
    /**
     * 服务项
     */
    private SelectCheckTypeParentAdapter parentAdapter;
    /**
     * 筛选列表
     */
    private SelectCheckTypeFilterAdapter filterAdapter;
    /**
     * 搜索结果列表
     */
    private SelectCheckTypeAdapter searchAdapter;
    /**
     * 购物车
     */
    private SelectCheckTypeShopAdapter shopAdapter;
    /**
     * 医院 服务项、服务包数据集合
     */
    private List<SelectCheckTypeParentBean> parentBeans = new ArrayList<>();
    /**
     * 医院 服务项、服务包数据集合  筛选结果
     */
    private List<SelectCheckTypeParentBean> filterBeans = new ArrayList<>();
    /**
     * 购物车
     */
    private ArrayList<SelectCheckTypeParentBean> shopBeans = new ArrayList<>();
    /**
     * 搜索结果
     */
    private List<SelectCheckTypeBean> searchBeans = new ArrayList<>();
    /**
     * 搜索结果 包含医院数据
     */
    private List<SelectCheckTypeParentBean> searchParentBeans = new ArrayList<>();
    /**
     * 医院筛选条件
     */
    private ArrayList<String> filterHospitalData;
    /**
     * 服务范围筛选条件
     */
    private ArrayList<String> filterServiceData;
    /**
     * 已选中的服务项、服务包
     */
    private ArrayList<String> selectedCodes = new ArrayList<>();
    /**
     * 最近使用的服务项、服务包code
     */
    private List<String> recentlyUsedServiceData;
    /**
     * 当前选中的医院、选中的服务（全部服务or最近使用）
     */
    private String curHospital, curServiceType;
    /**
     * 筛选类型  1、医院   2、服务范围
     */
    private int filterType = BASE_ONE;
    /**
     * 强制更新
     */
    private boolean forcedUpdate = false;

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_select_check_type;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        if (getIntent() != null) {
            shopBeans = (ArrayList<SelectCheckTypeParentBean>)getIntent().getSerializableExtra(
                    CommonData.KEY_RESERVE_CHECK_TYPE_LIST);
            //重新选择的强制更新
            forcedUpdate = getIntent().getBooleanExtra(CommonData.KEY_INTENT_BOOLEAN, false);
        }
        if (!forcedUpdate) {
            //忽略价格的强制更新
            forcedUpdate = sharePreferenceUtil.getBoolean(CommonData.KEY_RESERVE_CHECK_UPDATE);
        }
        layoutRefresh.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light,
                                              android.R.color.holo_orange_light, android.R.color.holo_green_light);
        layoutRefresh.setOnRefreshListener(this);
        //默认选中全部医院、全部服务
        curHospital = getString(R.string.txt_all_hospitals);
        curServiceType = getString(R.string.txt_all_services);
        //服务项列表
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        parentAdapter = new SelectCheckTypeParentAdapter(R.layout.item_check_select_root, filterBeans);
        parentAdapter.setOnSelectedCallback(this);
        recyclerView.setAdapter(parentAdapter);
        //医院列表数据
        filterRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        filterAdapter = new SelectCheckTypeFilterAdapter(R.layout.item_select_filter, new ArrayList<>());
        filterAdapter.setOnItemClickListener(this);
        filterRecyclerView.setAdapter(filterAdapter);
        //搜索列表
        searchAdapter = new SelectCheckTypeAdapter(this);
        searchRecyclerView.setOnItemClickListener(this);
        searchRecyclerView.setAdapter(searchAdapter);
        //购物车
        shopRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        shopAdapter = new SelectCheckTypeShopAdapter(R.layout.item_check_shop_root, shopBeans);
        shopAdapter.setOnServiceDeleteListener(this);
        shopRecyclerView.setAdapter(shopAdapter);
        selectedCodes = ZycApplication.getInstance().getSelectCodes();
        if (selectedCodes.size() > 0) {
            tvNext.setSelected(true);
        }
        else {
            tvNext.setSelected(false);
        }
        tvSelected.setText(String.format(getString(R.string.txt_calc_selected_num), selectedCodes.size()));
        tvSelected.setSelected(true);
        //是否显示刷新引导
        boolean status = sharePreferenceUtil.getAlwaysBoolean(CommonData.KEY_SHOW_REFRESH_STATUS);
        if (!status) {
            layoutRefreshRoot.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        if (forcedUpdate) {
            getCheckTypeList();
        }
        else {
            //获取最近使用
            getRecentlyUsedListByLocal();
            //获取本地服务向数据
            getCheckTypeListByLocal();
            bindServiceListData();
            //本地没有数据
            if (parentBeans.size() == 0) {
                getCheckTypeList();
            }
        }
    }

    @Override
    public void initListener() {
        super.initListener();
        etSearchCheckType.addTextChangedListener(new AbstractTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                searchCheckTYpeListByLocal(s.toString());
            }
        });
    }

    /**
     * 获取检查项 全部
     */
    private void getCheckTypeList() {
        RequestUtils.getCheckTypeList(this, loginBean.getToken(), this);
    }

    /**
     * 获取本地数据
     */
    private void getCheckTypeListByLocal() {
        parentBeans = LitePal.findAll(SelectCheckTypeParentBean.class, true);
        //过滤
        filterNoneCheckHospital();
    }

    /**
     * 过滤掉没有服务项、服务包的医院
     */
    private void filterNoneCheckHospital() {
        for (int i = 0; i < parentBeans.size(); i++) {
            SelectCheckTypeParentBean parentBean = parentBeans.get(i);
            if (parentBean.getProductPackageList() == null || parentBean.getProductPackageList().size() == 0) {
                parentBeans.remove(parentBean);
            }
        }
    }

    /**
     * 获取最近使用数据
     */
    private void getRecentlyUsedListByLocal() {
        Set<String> recent = sharePreferenceUtil.getStringSet(CommonData.KEY_RECENTLY_USED_SERVICE);
        recentlyUsedServiceData = new ArrayList<>();
        if (recent != null) {
            recentlyUsedServiceData.addAll(recent);
        }
    }

    /**
     * 搜索数据集合
     */
    private void searchCheckTYpeListByLocal(String searchKey) {
        if (!TextUtils.isEmpty(searchKey)) {
            searchBeans.clear();
            searchParentBeans.clear();
            //重新拉取本地数据
            getCheckTypeListByLocal();
            for (SelectCheckTypeParentBean parentBean : parentBeans) {
                ArrayList<SelectCheckTypeBean> beans = parentBean.getProductPackageList();
                for (SelectCheckTypeBean bean : beans) {
                    //服务名称、服务别名
                    boolean contains = bean.getProjectName().contains(searchKey) ||
                                       (!TextUtils.isEmpty(bean.getProjectAlias()) &&
                                        bean.getProjectAlias().contains(searchKey));
                    if (contains) {
                        searchBeans.add(bean);
                        searchParentBeans.add(parentBean);
                    }
                    else {
                        //服务包下的服务项
                        List<SelectCheckTypeChildBean> childBeans = bean.getChildServiceTypes(bean.getProjectCode());
                        for (SelectCheckTypeChildBean childBean : childBeans) {
                            if (childBean.getProductName().contains(searchKey)) {
                                searchBeans.add(bean);
                                searchParentBeans.add(parentBean);
                                //服务包下的服务项轮询到结果及跳出
                                break;
                            }
                        }
                    }
                }
            }
            if (searchBeans.size() > 0) {
                searchRecyclerView.setVisibility(View.VISIBLE);
                searchAdapter.setList(searchBeans);
            }
            else {
                searchRecyclerView.setVisibility(View.GONE);
                layoutSearchNone.setVisibility(View.VISIBLE);
            }
        }
        else {
            //取消搜索
            searchRecyclerView.setVisibility(View.GONE);
            layoutSearchNone.setVisibility(View.GONE);
        }
    }

    /**
     * 服务项数据集合
     */
    private void bindServiceListData() {
        filterBeans.clear();
        filterBeans.addAll(parentBeans);
        parentAdapter.setNewData(filterBeans);
        bindFilterListData();
    }

    /**
     * 服务项筛选数据集合
     */
    private void bindFilterServiceListData() {
        //重新拉取本地数据
        getCheckTypeListByLocal();
        filterBeans.clear();
        if (TextUtils.equals(curHospital, getString(R.string.txt_all_hospitals)) &&
            TextUtils.equals(curServiceType, getString(R.string.txt_all_services))) {
            //当前筛选条件为 全部医院、全部服务
            filterBeans.addAll(parentBeans);
        }
        else {
            if (TextUtils.equals(curHospital, getString(R.string.txt_all_hospitals))) {
                //医院条件为全部医院  最近使用
                for (SelectCheckTypeParentBean parentBean : parentBeans) {
                    List<SelectCheckTypeBean> beans = parentBean.getProductPackageList();
                    ArrayList<SelectCheckTypeBean> filterBeans = new ArrayList<>();
                    for (SelectCheckTypeBean bean : beans) {
                        //筛选出最近使用
                        if (recentlyUsedServiceData.contains(bean.getProjectCode())) {
                            filterBeans.add(bean);
                        }
                    }
                    //当含有服务项数据才添加
                    if (filterBeans.size() > 0) {
                        parentBean.setProductPackageList(filterBeans);
                        this.filterBeans.add(parentBean);
                    }
                }
            }
            else {
                for (SelectCheckTypeParentBean parentBean : parentBeans) {
                    if (TextUtils.equals(curHospital, parentBean.getHospitalName())) {
                        if (!TextUtils.equals(curServiceType, getString(R.string.txt_all_services))) {
                            //筛选条件为单个医院 最近服务
                            List<SelectCheckTypeBean> beans = parentBean.getProductPackageList();
                            ArrayList<SelectCheckTypeBean> filterBeans = new ArrayList<>();
                            for (SelectCheckTypeBean bean : beans) {
                                //筛选出最近使用
                                if (recentlyUsedServiceData.contains(bean.getProjectCode())) {
                                    filterBeans.add(bean);
                                }
                            }
                            parentBean.setProductPackageList(filterBeans);
                        }
                        //当含有服务项数据才添加
                        if (parentBean.getProductPackageList().size() > 0) {
                            filterBeans.add(parentBean);
                        }
                        break;
                    }
                }
            }
        }
        parentAdapter.setNewData(filterBeans);
        if (filterBeans.size() == 0) {
            layoutNone.setVisibility(View.VISIBLE);
            if (TextUtils.equals(curServiceType, getString(R.string.txt_all_services))) {
                tvNone.setText(R.string.txt_none_service);
            }
            else {
                tvNone.setText(R.string.txt_none_recently_used_service);
            }
        }
        else {
            layoutNone.setVisibility(View.GONE);
        }
    }

    /**
     * 筛选条件数据集合
     */
    private void bindFilterListData() {
        //医院数据
        filterHospitalData = new ArrayList<>();
        filterHospitalData.add(getString(R.string.txt_all_hospitals));
        for (SelectCheckTypeParentBean bean : parentBeans) {
            filterHospitalData.add(bean.getHospitalName());
        }
        //服务范围数据
        filterServiceData = new ArrayList<>();
        filterServiceData.add(getString(R.string.txt_all_services));
        filterServiceData.add(getString(R.string.txt_recent_used));
    }

    /**
     * 显示筛选
     */
    private void showFilterLayout(ArrayList<String> data) {
        layoutFilterContentRoot.setVisibility(View.VISIBLE);
        if (filterType == BASE_ONE) {
            filterAdapter.setCurSelected(curHospital);
        }
        else {
            filterAdapter.setCurSelected(curServiceType);
        }
        filterAdapter.setNewData(data);
        layoutExpandable.expand();
    }

    /**
     * 收起筛选
     */
    private void hideFilterLayout() {
        layoutExpandable.collapse();
        layoutFilterContentRoot.setVisibility(View.GONE);
    }

    /**
     * 刷新数据
     */
    private void refreshData() {
        //默认选中全部医院、全部服务
        curHospital = getString(R.string.txt_all_hospitals);
        curServiceType = getString(R.string.txt_all_services);
        //清除搜索已选状态
        filterType = BASE_ONE;
        //清除购物车
        shopBeans.clear();
        selectedCodes.clear();
        ZycApplication.getInstance().clearSelectCodes();
        tvSelected.setText(String.format(getString(R.string.txt_calc_selected_num), selectedCodes.size()));
        tvNext.setSelected(false);
        //重新从服务器拉取数据
        getCheckTypeList();
    }

    /**
     * 添加服务后  更新购物车
     */
    private void addUpdateShopCart(SelectCheckTypeParentBean parentBean, SelectCheckTypeBean bean) {
        //购物车列表
        SelectCheckTypeParentBean newBean;
        if (shopBeans.contains(parentBean)) {
            for (SelectCheckTypeParentBean data : shopBeans) {
                if (data.equals(parentBean)) {
                    ArrayList<SelectCheckTypeBean> list = data.getProductPackageList();
                    if (list == null) {
                        list = new ArrayList<>();
                    }
                    else {
                        if (list.contains(bean)) {
                            list.remove(bean);
                        }
                        else {
                            list.add(bean);
                        }
                    }
                    if (list.size() == 0) {
                        shopBeans.remove(data);
                    }
                    else {
                        data.setProductPackageList(list);
                    }
                    break;
                }
            }
        }
        else {
            newBean = new SelectCheckTypeParentBean();
            newBean.setHospitalCode(parentBean.getHospitalCode());
            newBean.setHospitalName(parentBean.getHospitalName());
            ArrayList<SelectCheckTypeBean> list = new ArrayList<>();
            list.add(bean);
            newBean.setProductPackageList(list);
            shopBeans.add(newBean);
        }
        shopAdapter.setNewData(shopBeans);
    }

    /**
     * 删除后 更新购物车
     */
    private void updateShopCart() {
        //购物车列表刷新
        shopAdapter.setNewData(shopBeans);
        //主数据列表刷新
        parentAdapter.notifyDataSetChanged();
        //搜索列表刷新
        searchAdapter.notifyDataSetChanged();
        tvSelected.setText(String.format(getString(R.string.txt_calc_selected_num), selectedCodes.size()));
        if (selectedCodes.size() > 0) {
            tvNext.setSelected(true);
            layoutNoneShop.setVisibility(View.GONE);
        }
        else {
            tvNext.setSelected(false);
            layoutNoneShop.setVisibility(View.VISIBLE);
            layoutShop.setVisibility(View.GONE);
        }
    }

    /**
     * 显示购物车
     */
    private void showShopLayout() {
        layoutShopBg.setVisibility(View.VISIBLE);
        tvSelected.setSelected(false);
        Animation toUp = AnimationUtils.loadAnimation(this, R.anim.anim_down_in);
        Animation alpha = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        if (shopBeans.size() > 0) {
            layoutShop.setVisibility(View.VISIBLE);
            layoutShop.startAnimation(toUp);
        }
        else {
            layoutNoneShop.setVisibility(View.VISIBLE);
            layoutNoneShop.startAnimation(toUp);
        }
        layoutShopBg.startAnimation(alpha);
    }

    /**
     * 隐藏购物车
     */
    private void hideShopLayout() {
        tvSelected.setSelected(true);
        Animation toUp = AnimationUtils.loadAnimation(this, R.anim.anim_down_out);
        Animation alpha = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        toUp.setAnimationListener(new AbstractAnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                layoutShopBg.setVisibility(View.GONE);
                layoutShop.setVisibility(View.GONE);
                layoutNoneShop.setVisibility(View.GONE);
            }
        });
        if (shopBeans.size() > 0) {
            layoutShop.startAnimation(toUp);
        }
        else {
            layoutNoneShop.startAnimation(toUp);
        }
        layoutShopBg.startAnimation(alpha);
    }

    /**
     * 已选code更新
     */
    private void updateSelectedCodes(String projectCode) {
        if (selectedCodes.contains(projectCode)) {
            selectedCodes.remove(projectCode);
        }
        else {
            selectedCodes.add(projectCode);
        }
        if (selectedCodes.size() > 0) { tvNext.setSelected(true); }
        else { tvNext.setSelected(false); }
        tvSelected.setText(String.format(getString(R.string.txt_calc_selected_num), selectedCodes.size()));
        ZycApplication.getInstance().setSelectCodes(selectedCodes);
    }

    @OnClick({
            R.id.tv_cancel, R.id.layout_all_hospital, R.id.layout_all_service, R.id.tv_selected, R.id.tv_next,
            R.id.layout_bg, R.id.tv_none_refresh, R.id.layout_shop_bg, R.id.tv_clear_shop, R.id.tv_know })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                finish();
                break;
            case R.id.layout_all_hospital:
                if (layoutFilterContentRoot.getVisibility() == View.GONE) {
                    filterType = BASE_ONE;
                    showFilterLayout(filterHospitalData);
                }
                else {
                    if (filterType == BASE_ONE) {
                        hideFilterLayout();
                    }
                    else {
                        filterType = BASE_ONE;
                        filterAdapter.setCurSelected(curHospital);
                        filterAdapter.setNewData(filterHospitalData);
                    }
                }
                break;
            case R.id.layout_all_service:
                if (layoutFilterContentRoot.getVisibility() == View.GONE) {
                    filterType = BASE_TWO;
                    showFilterLayout(filterServiceData);
                }
                else {
                    if (filterType == BASE_TWO) {
                        hideFilterLayout();
                    }
                    else {
                        filterType = BASE_TWO;
                        filterAdapter.setCurSelected(curServiceType);
                        filterAdapter.setNewData(filterServiceData);
                    }
                }
                break;
            case R.id.tv_selected:
                if (tvSelected.isSelected()) {
                    showShopLayout();
                }
                else {
                    hideShopLayout();
                }
                break;
            case R.id.tv_next:
                if (tvNext.isSelected()) {
                    Intent intent = new Intent();
                    intent.putExtra(CommonData.KEY_RESERVE_CHECK_TYPE_LIST, shopBeans);
                    setResult(RESULT_OK, intent);
                    finish();
                }
                break;
            case R.id.layout_bg:
                hideFilterLayout();
                break;
            case R.id.tv_none_refresh:
                etSearchCheckType.setText("");
                layoutSearchNone.setVisibility(View.GONE);
                refreshData();
                break;
            case R.id.layout_shop_bg:
                hideShopLayout();
                break;
            case R.id.tv_clear_shop:
                //清除已选项
                selectedCodes.clear();
                ZycApplication.getInstance().clearSelectCodes();
                //购物车数据初始化
                shopBeans.clear();
                updateShopCart();
                break;
            case R.id.tv_know:
                layoutRefreshRoot.setVisibility(View.GONE);
                sharePreferenceUtil.putAlwaysBoolean(CommonData.KEY_SHOW_REFRESH_STATUS, true);
                break;
            default:
                break;
        }
    }

    /**
     * 搜索列表
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SelectCheckTypeParentBean parentBean = searchParentBeans.get(position);
        SelectCheckTypeBean bean = searchBeans.get(position);
        updateSelectedCodes(bean.getProjectCode());
        //搜索列表
        searchAdapter.notifyDataSetChanged();
        //更新主列表
        parentAdapter.notifyDataSetChanged();
        //更新购物车
        addUpdateShopCart(parentBean, bean);
    }

    /**
     * 筛选条件列表
     */
    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (filterType == BASE_ONE) {
            curHospital = filterHospitalData.get(position);
            tvHospitalBtn.setText(curHospital);
        }
        else {
            curServiceType = filterServiceData.get(position);
            tvServiceBtn.setText(curServiceType);
        }
        bindFilterServiceListData();
        hideFilterLayout();
    }

    /**
     * 购物车数据源
     */
    @Override
    public void onSelectedParent(SelectCheckTypeParentBean parentBean, SelectCheckTypeBean bean) {
        updateSelectedCodes(bean.getProjectCode());
        addUpdateShopCart(parentBean, bean);
    }

    /**
     * 购物车数据删除
     */
    @Override
    public void onServiceDelete(String code) {
        selectedCodes.remove(code);
        for (int i = 0; i < shopBeans.size(); i++) {
            SelectCheckTypeParentBean data = shopBeans.get(i);
            ArrayList<SelectCheckTypeBean> list = data.getProductPackageList();
            for (int j = 0; j < list.size(); j++) {
                SelectCheckTypeBean bean = list.get(j);
                if (TextUtils.equals(code, bean.getProjectCode())) {
                    list.remove(bean);
                }
            }
            if (list.size() == 0) {
                shopBeans.remove(data);
            }
        }
        ZycApplication.getInstance().setSelectCodes(selectedCodes);
        updateShopCart();
    }

    @Override
    public void onRefresh() {
        refreshData();
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        if (task == Tasks.GET_CHECK_TYPE) {
            parentBeans = (List<SelectCheckTypeParentBean>)response.getData();
            filterNoneCheckHospital();
            bindServiceListData();
            //存储
            ThreadPoolHelper.getInstance().execInSingle(() -> saveLocal(parentBeans));
            //重新拉取数据需要清除最近使用
            sharePreferenceUtil.putStringSet(CommonData.KEY_RECENTLY_USED_SERVICE, null);
        }
    }

    @Override
    public void onResponseEnd(Tasks task) {
        super.onResponseEnd(task);
        layoutRefresh.setRefreshing(false);
    }

    /**
     * 保存到数据库
     */
    private void saveLocal(List<SelectCheckTypeParentBean> list) {
        //保存数据
        ThreadPoolHelper.getInstance().execInSingle(() -> {
            LitePal.deleteAll(SelectCheckTypeParentBean.class);
            LitePal.deleteAll(SelectCheckTypeBean.class);
            LitePal.deleteAll(SelectCheckTypeChildBean.class);
            for (int i = 0; i < list.size(); i++) {
                SelectCheckTypeParentBean parentBean = list.get(i);
                ArrayList<SelectCheckTypeBean> oneBeans = parentBean.getProductPackageList();
                for (int j = 0; j < oneBeans.size(); j++) {
                    SelectCheckTypeBean oneBean = oneBeans.get(j);
                    List<SelectCheckTypeChildBean> twoBeans = oneBean.getProductInfoList();
                    if (twoBeans != null) {
                        List<SelectCheckTypeChildBean> newTwoBeans = new ArrayList<>();
                        for (int k = 0; k < twoBeans.size(); k++) {
                            SelectCheckTypeChildBean twoBean = twoBeans.get(k);
                            twoBean.setParentId(oneBean.getProjectCode());
                            newTwoBeans.add(twoBean);
                        }
                        LitePal.saveAll(newTwoBeans);
                    }
                }
                LitePal.saveAll(oneBeans);
            }
            LitePal.saveAll(list);
        });
        //变更更新状态为已更新
        sharePreferenceUtil.putBoolean(CommonData.KEY_RESERVE_CHECK_UPDATE, false);
    }
}