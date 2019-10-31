package com.yht.yihuantong.ui.check;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yht.frame.api.ThreadPoolHelper;
import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.Tasks;
import com.yht.frame.data.bean.SelectCheckTypeBean;
import com.yht.frame.data.bean.SelectCheckTypeChildBean;
import com.yht.frame.data.bean.SelectCheckTypeParentBean;
import com.yht.frame.http.retrofit.RequestUtils;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.widgets.edittext.AbstractTextWatcher;
import com.yht.frame.widgets.edittext.SuperEditText;
import com.yht.frame.widgets.view.ExpandableLayout;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ui.adapter.SelectCheckTypeFilterAdapter;
import com.yht.yihuantong.ui.adapter.SelectCheckTypeParentAdapter;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/6/4 17:53
 * @des 医院选择
 */
public class SelectCheckTypeActivity extends BaseActivity
        implements SelectCheckTypeParentAdapter.OnSelectedCallback, BaseQuickAdapter.OnItemClickListener {
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
    /**
     * 服务项
     */
    private SelectCheckTypeParentAdapter selectCheckTypeParentAdapter;
    /**
     * 筛选列表
     */
    private SelectCheckTypeFilterAdapter selectCheckTypeFilterAdapter;
    /**
     * 医院 服务项、服务包数据集合
     */
    private List<SelectCheckTypeParentBean> selectCheckTypeParentBeans = new ArrayList<>();
    /**
     * 医院 服务项、服务包数据集合  筛选结果
     */
    private List<SelectCheckTypeParentBean> filterCheckTypeParentBeans = new ArrayList<>();
    /**
     * 医院筛选条件
     */
    private ArrayList<String> filterHospitalData;
    /**
     * 服务范围筛选条件
     */
    private ArrayList<String> filterServiceData;
    /**
     * 当前选中的医院、选中的服务（全部服务or最近使用）
     */
    private String curHospital, curServiceType;
    /**
     * 筛选类型  1、医院   2、服务范围
     */
    private int filterType = BASE_ONE;
    /**
     * 已选中的服务项、服务包
     */
    private Map<String, ArrayList<String>> selectedServices;

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
        //默认选中全部医院、全部服务
        curHospital = getString(R.string.txt_all_hospitals);
        curServiceType = getString(R.string.txt_all_services);
        //服务项列表
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        selectCheckTypeParentAdapter = new SelectCheckTypeParentAdapter(R.layout.item_check_select_root,
                                                                        selectCheckTypeParentBeans);
        selectCheckTypeParentAdapter.setOnSelectedCallback(this);
        recyclerView.setAdapter(selectCheckTypeParentAdapter);
        //医院列表数据
        filterRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        selectCheckTypeFilterAdapter = new SelectCheckTypeFilterAdapter(R.layout.item_select_filter, new ArrayList<>());
        selectCheckTypeFilterAdapter.setOnItemClickListener(this);
        filterRecyclerView.setAdapter(selectCheckTypeFilterAdapter);
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        getCheckTypeListByLocal();
        //本地没有数据
        if (selectCheckTypeParentBeans.size() == 0) {
            getCheckTypeList();
        }
    }

    @Override
    public void initListener() {
        super.initListener();
        etSearchCheckType.addTextChangedListener(new AbstractTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
            }
        });
    }

    /**
     * 获取本地数据
     */
    private void getCheckTypeListByLocal() {
        selectCheckTypeParentBeans = LitePal.findAll(SelectCheckTypeParentBean.class, true);
        bindServiceListData();
    }

    /**
     * 获取检查项 全部
     */
    private void getCheckTypeList() {
        RequestUtils.getCheckTypeList(this, loginBean.getToken(), this);
    }

    /**
     * 服务项数据集合
     */
    private void bindServiceListData() {
        selectCheckTypeParentAdapter.setNewData(selectCheckTypeParentBeans);
        bindFilterListData();
    }

    /**
     * 服务项筛选数据集合
     */
    private void bindFilterServiceListData() {
        if (TextUtils.equals(curHospital, getString(R.string.txt_all_hospitals))) {
            selectCheckTypeParentAdapter.setNewData(selectCheckTypeParentBeans);
            return;
        }
        filterCheckTypeParentBeans.clear();
        for (SelectCheckTypeParentBean bean : selectCheckTypeParentBeans) {
            if (TextUtils.equals(bean.getHospitalName(), curHospital)) {
                filterCheckTypeParentBeans.add(bean);
                break;
            }
        }
        selectCheckTypeParentAdapter.setNewData(filterCheckTypeParentBeans);
    }

    /**
     * 筛选条件数据集合
     */
    private void bindFilterListData() {
        //医院数据
        filterHospitalData = new ArrayList<>();
        filterHospitalData.add(getString(R.string.txt_all_hospitals));
        for (SelectCheckTypeParentBean bean : selectCheckTypeParentBeans) {
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
            selectCheckTypeFilterAdapter.setCurSelected(curHospital);
        }
        else {
            selectCheckTypeFilterAdapter.setCurSelected(curServiceType);
        }
        selectCheckTypeFilterAdapter.setNewData(data);
        layoutExpandable.expand();
    }

    /**
     * 收起筛选
     */
    private void hideFilterLayout() {
        layoutExpandable.collapse();
        layoutFilterContentRoot.setVisibility(View.GONE);
    }

    @OnClick({
            R.id.tv_cancel, R.id.layout_all_hospital, R.id.layout_all_service, R.id.tv_selected, R.id.tv_next,
            R.id.layout_bg })
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
                        selectCheckTypeFilterAdapter.setCurSelected(curHospital);
                        selectCheckTypeFilterAdapter.setNewData(filterHospitalData);
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
                        selectCheckTypeFilterAdapter.setCurSelected(curServiceType);
                        selectCheckTypeFilterAdapter.setNewData(filterServiceData);
                    }
                }
                break;
            case R.id.tv_selected:
                break;
            case R.id.tv_next:
                break;
            case R.id.layout_bg:
                hideFilterLayout();
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (filterType == BASE_ONE) {
            curHospital = filterHospitalData.get(position);
            tvHospitalBtn.setText(curHospital);
            bindFilterServiceListData();
        }
        else {
            curServiceType = filterServiceData.get(position);
            tvServiceBtn.setText(curServiceType);
        }
        hideFilterLayout();
    }

    @Override
    public void onSelected(Map<String, ArrayList<String>> data) {
        selectedServices = data;
        if (selectedServices.size() > 0) {
            tvNext.setSelected(true);
        }
        else { tvNext.setSelected(false); }
        tvSelected.setText(String.format(getString(R.string.txt_calc_selected_num), selectedServices.size()));
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        if (task == Tasks.GET_CHECK_TYPE) {
            List<SelectCheckTypeParentBean> list = (List<SelectCheckTypeParentBean>)response.getData();
            saveLocal(list);
            bindServiceListData();
        }
    }

    /**
     * 已选择的服务包、服务项数据
     */
    private void selectedServiceData() {
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
    }
}