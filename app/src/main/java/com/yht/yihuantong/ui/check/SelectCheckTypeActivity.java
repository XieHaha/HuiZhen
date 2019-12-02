package com.yht.yihuantong.ui.check;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.widgets.edittext.AbstractTextWatcher;
import com.yht.frame.widgets.edittext.SuperEditText;
import com.yht.frame.widgets.view.ExpandableLayout;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ZycApplication;
import com.yht.yihuantong.ui.adapter.SelectCheckTypeAdapter;
import com.yht.yihuantong.ui.adapter.SelectCheckTypeFilterAdapter;
import com.yht.yihuantong.ui.adapter.SelectCheckTypeRvAdapter;
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
public class SelectCheckTypeActivity extends BaseActivity implements BaseQuickAdapter.OnItemClickListener, AdapterView.OnItemClickListener,
        SwipeRefreshLayout.OnRefreshListener, ShopCheckTypeAdapter.OnServiceDeleteListener,
        BaseQuickAdapter.OnItemChildClickListener {
    @BindView(R.id.et_search_check_type)
    SuperEditText etSearchCheckType;
    @BindView(R.id.tv_hospital_btn)
    TextView tvHospitalBtn;
    @BindView(R.id.tv_service_btn)
    TextView tvServiceBtn;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.search_recycler_view)
    ListView searchListView;
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
    @BindView(R.id.layout_none)
    RelativeLayout layoutNone;
    @BindView(R.id.layout_search_none)
    RelativeLayout layoutSearchNone;
    @BindView(R.id.layout_shop_bg)
    RelativeLayout layoutShopBg;
    @BindView(R.id.tv_arrow)
    TextView tvArrow;
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
    @BindView(R.id.iv_path)
    ImageView ivPath;
    /**
     * 服务项
     */
    private SelectCheckTypeRvAdapter parentAdapter;
    private LinearLayoutManager layoutManager;
    /**
     * 筛选条件列表
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
    private List<SelectCheckTypeBean> parentNewBeans = new ArrayList<>();
    /**
     * 医院 服务项、服务包数据集合  筛选结果
     */
    private List<SelectCheckTypeBean> filterBeans = new ArrayList<>();
    /**
     * 购物车
     */
    private ArrayList<SelectCheckTypeBean> shopBeans = new ArrayList<>();
    /**
     * 搜索结果
     */
    private List<SelectCheckTypeBean> searchBeans = new ArrayList<>();
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
            shopBeans = (ArrayList<SelectCheckTypeBean>) getIntent().getSerializableExtra(
                    CommonData.KEY_RESERVE_CHECK_TYPE_LIST);
            //重新选择的强制更新
            forcedUpdate = getIntent().getBooleanExtra(CommonData.KEY_INTENT_BOOLEAN, false);
        }
        if (!forcedUpdate) {
            //忽略价格的强制更新
            forcedUpdate = sharePreferenceUtil.getBoolean(CommonData.KEY_RESERVE_CHECK_UPDATE);
        }
        layoutRefresh.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light, android.R.color.holo_green_light);
        layoutRefresh.setOnRefreshListener(this);
        //默认选中全部医院、全部服务
        curHospital = getString(R.string.txt_all_hospitals);
        curServiceType = getString(R.string.txt_all_services);
        //服务项列表
        recyclerView.setLayoutManager(layoutManager = new LinearLayoutManager(this));
        parentAdapter = new SelectCheckTypeRvAdapter(R.layout.item_check_select_new,
                parentNewBeans);
        parentAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(parentAdapter);
        //医院列表数据
        filterRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        filterAdapter = new SelectCheckTypeFilterAdapter(R.layout.item_select_filter,
                new ArrayList<>());
        filterAdapter.setHospitalName(loginBean.getHospitalName());
        filterAdapter.setOnItemClickListener(this);
        filterRecyclerView.setAdapter(filterAdapter);
        //搜索列表
        searchAdapter = new SelectCheckTypeAdapter(this);
        searchListView.setOnItemClickListener(this);
        searchListView.setAdapter(searchAdapter);
        //购物车
        shopRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        shopAdapter = new SelectCheckTypeShopAdapter(R.layout.item_check_shop, shopBeans);
        shopAdapter.setOnItemChildClickListener(this);
        shopRecyclerView.setAdapter(shopAdapter);
        selectedCodes = ZycApplication.getInstance().getSelectCodes();
        if (selectedCodes.size() > 0) {
            tvNext.setSelected(true);
        } else {
            tvNext.setSelected(false);
        }
        tvSelected.setText(String.valueOf(selectedCodes.size()));
        tvArrow.setSelected(true);
        //隐藏输入框光标
        etSearchCheckType.setCursorVisible(false);
        //是否显示刷新引导
        boolean status = sharePreferenceUtil.getAlwaysBoolean(CommonData.KEY_SHOW_REFRESH_STATUS);
        if (!status) {
            layoutRefreshRoot.setVisibility(View.VISIBLE);
            startAnim();
        }
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        if (forcedUpdate) {
            getCheckTypeList(true);
        } else {
            //获取最近使用
            getRecentlyUsedListByLocal();
            //获取本地服务项数据
            getCheckTypeListByLocal();
            //绑定数据
            bindServiceListData();
            //本地没有数据
            if (parentNewBeans.size() == 0) {
                getCheckTypeList(true);
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
                searchCheckTypeListByLocal(s.toString());
            }
        });
        //重新显示光标
        etSearchCheckType.setOnClickListener(v -> etSearchCheckType.setCursorVisible(true));
        searchListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case SCROLL_STATE_IDLE:
                        //滑动停止时调用
                        break;
                    case SCROLL_STATE_TOUCH_SCROLL:
                        hideSoftInputFromWindow();
                        //正在滚动时调用
                        break;
                    case SCROLL_STATE_FLING:
                        //手指快速滑动时,在离开ListView由于惯性滑动
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                                 int totalItemCount) {
            }
        });
    }

    /**
     * 获取检查项 全部
     */
    private void getCheckTypeList(boolean show) {
        RequestUtils.getCheckTypeList(this, loginBean.getToken(), show, this);
    }

    /**
     * 获取本地数据
     */
    private void getCheckTypeListByLocal() {
        parentNewBeans = LitePal.findAll(SelectCheckTypeBean.class, true);
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
    private void searchCheckTypeListByLocal(String searchKey) {
        if (!TextUtils.isEmpty(searchKey)) {
            searchBeans.clear();
            //重新拉取本地数据
            getCheckTypeListByLocal();
            for (SelectCheckTypeBean bean : parentNewBeans) {
                //服务名称、服务别名
                boolean contains = bean.getProjectName().contains(searchKey) ||
                        (!TextUtils.isEmpty(bean.getProjectAlias()) &&
                                bean.getProjectAlias().contains(searchKey));
                if (contains) {
                    searchBeans.add(bean);
                } else {
                    //服务包下的服务项
                    List<SelectCheckTypeChildBean> childBeans =
                            bean.getChildServiceTypes(bean.getProjectCode());
                    for (SelectCheckTypeChildBean childBean : childBeans) {
                        if (childBean.getProductName().contains(searchKey)) {
                            searchBeans.add(bean);
                            //服务包下的服务项轮询到结果及跳出
                            break;
                        }
                    }
                }
            }
            if (searchBeans.size() > 0) {
                searchListView.setVisibility(View.VISIBLE);
                searchAdapter.setList(searchBeans);
                layoutSearchNone.setVisibility(View.GONE);
            } else {
                searchListView.setVisibility(View.GONE);
                layoutSearchNone.setVisibility(View.VISIBLE);
            }
        } else {
            //取消搜索
            searchListView.setVisibility(View.GONE);
            layoutSearchNone.setVisibility(View.GONE);
        }
    }

    /**
     * 服务项数据集合
     */
    private void bindServiceListData() {
        parentAdapter.setSelectedCodes(selectedCodes);
        parentAdapter.setNewData(parentNewBeans);
        if (parentNewBeans.size() > 0) {
            layoutNone.setVisibility(View.GONE);
        } else {
            layoutNone.setVisibility(View.VISIBLE);
        }
        //筛选条件
        bindFilterListData();
    }

    /**
     * 服务项筛选数据集合
     */
    private void bindFilterServiceListData() {
        //重新拉取本地数据
        getCheckTypeListByLocal();
        filterBeans.clear();
        if (TextUtils.equals(curHospital, getString(R.string.txt_all_hospitals)) && TextUtils.equals(curServiceType, getString(R.string.txt_all_services))) {
            //当前筛选条件为 全部医院、全部服务
            filterBeans.addAll(parentNewBeans);
        } else {
            if (TextUtils.equals(curHospital, getString(R.string.txt_all_hospitals))) {
                //医院条件为全部医院  最近使用
                for (SelectCheckTypeBean bean : parentNewBeans) {
                    //筛选出最近使用
                    if (recentlyUsedServiceData.contains(bean.getProjectCode())) {
                        filterBeans.add(bean);
                    }
                }
            } else {
                for (SelectCheckTypeBean parentBean : parentNewBeans) {
                    //单个医院
                    if (TextUtils.equals(curHospital, parentBean.getHospitalName())) {
                        if (TextUtils.equals(curServiceType,
                                getString(R.string.txt_all_services))) {
                            //全部服务
                            filterBeans.add(parentBean);

                        } else if (recentlyUsedServiceData.contains(parentBean.getProjectCode())) {
                            //最近使用
                            filterBeans.add(parentBean);
                        }
                    }
                }
            }
        }
        parentAdapter.setSelectedCodes(selectedCodes);
        parentNewBeans.clear();
        parentNewBeans.addAll(filterBeans);
        parentAdapter.setNewData(parentNewBeans);
        if (filterBeans.size() == 0) {
            layoutNone.setVisibility(View.VISIBLE);
            if (TextUtils.equals(curServiceType, getString(R.string.txt_all_services))) {
                tvNone.setText(R.string.txt_none_service);
            } else {
                tvNone.setText(R.string.txt_none_recently_used_service);
            }
        } else {
            layoutNone.setVisibility(View.GONE);
        }
        layoutManager.scrollToPositionWithOffset(0, 0);
    }

    /**
     * 筛选条件数据集合
     */
    private void bindFilterListData() {
        //医院数据
        filterHospitalData = new ArrayList<>();
        filterHospitalData.add(getString(R.string.txt_all_hospitals));
        for (SelectCheckTypeBean bean : parentNewBeans) {
            String hospitalName = bean.getHospitalName();
            if (!filterHospitalData.contains(hospitalName)) {
                filterHospitalData.add(hospitalName);
            }
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
        } else {
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
        layoutExpandable.setOnExpansionUpdateListener((expansionFraction, state) -> {
            if (state == 0) {
                layoutFilterContentRoot.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 刷新数据
     */
    private void refreshData() {
        //默认选中全部医院、全部服务
        curHospital = getString(R.string.txt_all_hospitals);
        curServiceType = getString(R.string.txt_all_services);
        tvHospitalBtn.setText(curHospital);
        tvServiceBtn.setText(curServiceType);
        //清除搜索已选状态
        filterType = BASE_ONE;
        //重新从服务器拉取数据
        getCheckTypeList(false);
    }

    /**
     * 刷新数据后需要清除掉已失效的数据
     */
    private void refreshDataInit() {
        if (shopBeans.size() == 0) {
            return;
        }
        ArrayList<SelectCheckTypeBean> newShop = new ArrayList<>();
        ArrayList<String> newSelected = new ArrayList<>();
        for (int i = 0; i < parentNewBeans.size(); i++) {
            SelectCheckTypeBean bean = parentNewBeans.get(i);
            if (selectedCodes.contains(bean.getProjectCode())) {
                newSelected.add(bean.getProjectCode());
                newShop.add(bean);
            }
        }

        shopBeans.clear();
        shopBeans.addAll(newShop);
        selectedCodes.clear();
        selectedCodes.addAll(newSelected);
        ZycApplication.getInstance().setSelectCodes(selectedCodes);
        tvSelected.setText(String.valueOf(selectedCodes.size()));
        if (selectedCodes.size() > 0) {
            tvNext.setSelected(true);
        } else {
            tvNext.setSelected(false);
        }
    }

    /**
     * 删除后 更新购物车
     */
    private void updateShopCart() {
        //购物车列表刷新
        shopAdapter.setNewData(shopBeans);
        //主数据列表刷新
        parentAdapter.setSelectedCodes(selectedCodes);
        parentAdapter.notifyDataSetChanged();
        //搜索列表刷新
        searchAdapter.notifyDataSetChanged();
        tvSelected.setText(String.valueOf(selectedCodes.size()));
        if (selectedCodes.size() > 0) {
            tvNext.setSelected(true);
            layoutNoneShop.setVisibility(View.GONE);
        } else {
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
        tvArrow.setSelected(false);
        Animation toUp = AnimationUtils.loadAnimation(this, R.anim.anim_down_in);
        Animation alpha = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        if (shopBeans.size() > 0) {
            layoutShop.setVisibility(View.VISIBLE);
            layoutShop.startAnimation(toUp);
        } else {
            layoutNoneShop.setVisibility(View.VISIBLE);
            layoutNoneShop.startAnimation(toUp);
        }
        layoutShopBg.startAnimation(alpha);
    }

    /**
     * 隐藏购物车
     */
    private void hideShopLayout() {
        tvArrow.setSelected(true);
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
        } else {
            layoutNoneShop.startAnimation(toUp);
        }
        layoutShopBg.startAnimation(alpha);
    }

    /**
     * 已选code更新
     */
    private void updateSelectedCodes(SelectCheckTypeBean bean) {
        if (selectedCodes.contains(bean.getProjectCode())) {
            selectedCodes.remove(bean.getProjectCode());
        } else {
            selectedCodes.add(bean.getProjectCode());
        }
        if (selectedCodes.size() > 0) {
            tvNext.setSelected(true);
        } else {
            tvNext.setSelected(false);
        }
        tvSelected.setText(String.valueOf(selectedCodes.size()));
        //通知主列表
        parentAdapter.setSelectedCodes(selectedCodes);
        parentAdapter.notifyDataSetChanged();
        //更新购物车
        if (shopBeans.contains(bean)) {
            shopBeans.remove(bean);
        } else {
            shopBeans.add(bean);
        }
        shopAdapter.setNewData(shopBeans);
        //更新本地数据
        ZycApplication.getInstance().setSelectCodes(selectedCodes);
    }

    /**
     * 手指下拉动画
     */
    private void startAnim() {
        float y = ivPath.getY();
        Animation animation = new TranslateAnimation(ivPath.getX(), ivPath.getX(), y - 120,
                y + 140);
        animation.setRepeatMode(Animation.RESTART);
        animation.setRepeatCount(Animation.INFINITE);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.setDuration(1250);
        ivPath.startAnimation(animation);
    }

    private void endAnim() {
        ivPath.clearAnimation();
    }

    @OnClick({R.id.tv_cancel, R.id.layout_all_hospital, R.id.layout_all_service,
            R.id.layout_calc_selected, R.id.tv_next, R.id.layout_bg, R.id.tv_none_refresh,
            R.id.layout_shop_bg, R.id.tv_clear_shop, R.id.tv_know})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                finish();
                break;
            case R.id.layout_all_hospital:
                if (layoutFilterContentRoot.getVisibility() == View.GONE) {
                    filterType = BASE_ONE;
                    showFilterLayout(filterHospitalData);
                } else {
                    if (filterType == BASE_ONE) {
                        hideFilterLayout();
                    } else {
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
                } else {
                    if (filterType == BASE_TWO) {
                        hideFilterLayout();
                    } else {
                        filterType = BASE_TWO;
                        filterAdapter.setCurSelected(curServiceType);
                        filterAdapter.setNewData(filterServiceData);
                    }
                }
                break;
            case R.id.layout_calc_selected:
                if (tvArrow.isSelected()) {
                    showShopLayout();
                } else {
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
                endAnim();
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
        updateSelectedCodes(searchBeans.get(position));
        //搜索列表
        searchAdapter.notifyDataSetChanged();
    }

    /**
     * 筛选条件列表
     */
    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (adapter instanceof SelectCheckTypeFilterAdapter) {
            if (filterType == BASE_ONE) {
                curHospital = filterHospitalData.get(position);
                tvHospitalBtn.setText(curHospital);
            } else {
                curServiceType = filterServiceData.get(position);
                tvServiceBtn.setText(curServiceType);
            }
            bindFilterServiceListData();
            hideFilterLayout();
        } else if (adapter instanceof SelectCheckTypeRvAdapter) {
            updateSelectedCodes(parentNewBeans.get(position));
        }
    }

    /**
     * 购物车删除按钮监听
     */
    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        SelectCheckTypeBean bean = shopBeans.get(position);
        selectedCodes.remove(bean.getProjectCode());
        for (int i = 0; i < shopBeans.size(); i++) {
            if (shopBeans.contains(bean)) {
                shopBeans.remove(bean);
            }
        }
        ZycApplication.getInstance().setSelectCodes(selectedCodes);
        updateShopCart();
    }

    /**
     * 购物车数据删除
     */
    @Override
    public void onServiceDelete(SelectCheckTypeBean bean) {
        selectedCodes.remove(bean.getProjectCode());
        shopBeans.remove(bean);
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
            List<SelectCheckTypeParentBean> list =
                    (List<SelectCheckTypeParentBean>) response.getData();
            //排序
            sortHospitalData(list);
            //购物车、数据过滤
            refreshDataInit();
            //绑定列表服务项数据
            bindServiceListData();
            //存储
            ThreadPoolHelper.getInstance().execInSingle(() -> saveLocal());
        }
    }

    @Override
    public void onResponseEnd(Tasks task) {
        super.onResponseEnd(task);
        layoutRefresh.setRefreshing(false);
    }

    /**
     * 根据医院排序、本院置顶，其他医院按首字母排序
     */
    private void sortHospitalData(List<SelectCheckTypeParentBean> list) {
        //过滤掉没有服务项、服务包的医院
        ArrayList<SelectCheckTypeParentBean> nullData = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            SelectCheckTypeParentBean parentBean = list.get(i);
            if (parentBean.getProductPackageList() == null || parentBean.getProductPackageList().size() == 0) {
                nullData.add(parentBean);
            }
        }
        list.removeAll(nullData);
        //根据医院排序、本院置顶，其他医院按首字母排序
        List<SelectCheckTypeParentBean> parentBeans = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            SelectCheckTypeParentBean bean = list.get(i);
            if (TextUtils.equals(bean.getHospitalCode(), loginBean.getHospitalCode())) {
                parentBeans.add(bean);
                list.remove(bean);
                break;
            }
        }
        //医院名字首字母排序
        BaseUtils.sortHospitalData(list);
        parentBeans.addAll(list);
        //过滤
        filterHospitalLevel(parentBeans);
    }

    /**
     * 过滤掉医院层级
     */
    private void filterHospitalLevel(List<SelectCheckTypeParentBean> parentBeans) {
        parentNewBeans.clear();
        for (SelectCheckTypeParentBean parentBean : parentBeans) {
            ArrayList<SelectCheckTypeBean> list = parentBean.getProductPackageList();
            if (list != null && list.size() > 0) {
                for (SelectCheckTypeBean bean :
                        list) {
                    bean.setHospitalName(parentBean.getHospitalName());
                    bean.setHospitalCode(parentBean.getHospitalCode());
                    parentNewBeans.add(bean);
                }
            }
        }
    }

    /**
     * 保存到数据库
     */
    private void saveLocal() {
        //保存数据
        LitePal.deleteAll(SelectCheckTypeBean.class);
        LitePal.deleteAll(SelectCheckTypeChildBean.class);
        for (int i = 0; i < parentNewBeans.size(); i++) {
            SelectCheckTypeBean oneBean = parentNewBeans.get(i);
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
        LitePal.saveAll(parentNewBeans);
        //变更更新状态为已更新
        sharePreferenceUtil.putBoolean(CommonData.KEY_RESERVE_CHECK_UPDATE, false);
    }
}