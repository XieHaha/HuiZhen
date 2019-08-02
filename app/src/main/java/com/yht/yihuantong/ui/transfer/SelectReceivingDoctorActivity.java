package com.yht.yihuantong.ui.transfer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yht.frame.data.BaseData;
import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.CommonData;
import com.yht.frame.data.Tasks;
import com.yht.frame.data.bean.DoctorInfoBean;
import com.yht.frame.data.bean.HospitalBean;
import com.yht.frame.data.bean.HospitalDepartBean;
import com.yht.frame.data.bean.HospitalDepartChildBean;
import com.yht.frame.http.retrofit.RequestUtils;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.widgets.dialog.HintDialog;
import com.yht.frame.widgets.edittext.AbstractTextWatcher;
import com.yht.frame.widgets.edittext.SuperEditText;
import com.yht.frame.widgets.recyclerview.loadview.CustomLoadMoreView;
import com.yht.frame.widgets.view.ExpandableLayout;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ui.adapter.DoctorAdapter;
import com.yht.yihuantong.ui.adapter.ReserveTransferSelectDoctorAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/6/26 15:41
 * @des 选择接诊医生
 */
public class SelectReceivingDoctorActivity extends BaseActivity
        implements BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.RequestLoadMoreListener,
                   BaseQuickAdapter.OnItemChildClickListener {
    @BindView(R.id.tv_select)
    TextView tvSelect;
    @BindView(R.id.layout_expand)
    ExpandableLayout layoutExpand;
    @BindView(R.id.search_recycler_view)
    RecyclerView searchRecyclerView;
    @BindView(R.id.recycler_view_select)
    RecyclerView selectRecyclerView;
    @BindView(R.id.layout_bg)
    RelativeLayout layoutBg;
    @BindView(R.id.tv_one)
    TextView tvHospitalTitle;
    @BindView(R.id.tv_two)
    TextView tvDepartOneTitle;
    @BindView(R.id.tv_three)
    TextView tvDepartTwoTitle;
    @BindView(R.id.et_search_check_type)
    SuperEditText etSearchCheckType;
    @BindView(R.id.view1)
    View viewHospitalBar;
    @BindView(R.id.view2)
    View viewDepartOneBar;
    @BindView(R.id.view3)
    View viewDepartTwoBar;
    @BindView(R.id.layout_none_doctor)
    LinearLayout layoutNoneDoctor;
    /**
     * 医院、科室适配器
     */
    private ReserveTransferSelectDoctorAdapter adapter;
    /**
     * 医生适配器
     */
    private DoctorAdapter doctorAdapter;
    /**
     * 医院列表
     */
    private List<HospitalBean> hospitals;
    private List<HospitalDepartBean> departOne;
    private List<HospitalDepartChildBean> departTwo;
    private List<DoctorInfoBean> doctors = new ArrayList<>();
    private List<String> data;
    /**
     * 当前选中的医院
     */
    private HospitalBean curHospital;
    /**
     * 当前选中的一级科室
     */
    private HospitalDepartBean curHospitalDepartBean;
    /**
     * 当前选中的二级科室
     */
    private HospitalDepartChildBean curHospitalDepartChildBean;
    /**
     * 一级科室选中
     */
    private int curDepartOnePosition = -1;
    /**
     * 二级科室选中
     */
    private int curDepartTwoPosition = -1;
    /**
     * 当前选择列表  0为医院 1为大科室  2为小科室
     */
    private int curType;
    private String orderNo;
    /**
     * 查询医生参数
     */
    private Map<String, Object> params = new HashMap<>();
    /**
     * 页码
     */
    private int page = 1;
    /**
     * 是否为转给他人
     */
    private boolean isTransferOther;
    /**
     * 只有第一次请求才判断是否显示无医生提示
     */
    boolean showNone = false;

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_select_receiving_doctor;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        if (getIntent() != null) {
            isTransferOther = getIntent().getBooleanExtra(CommonData.KEY_IS_TRANSFER_OTHER, false);
            orderNo = getIntent().getStringExtra(CommonData.KEY_ORDER_ID);
        }
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        //科室医院列表
        selectRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ReserveTransferSelectDoctorAdapter(R.layout.item_depart, data);
        adapter.setOnItemClickListener(this);
        selectRecyclerView.setAdapter(adapter);
        //医生列表
        searchRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        doctorAdapter = new DoctorAdapter(R.layout.item_doctor, doctors);
        doctorAdapter.setLoadMoreView(new CustomLoadMoreView());
        doctorAdapter.setOnLoadMoreListener(this, searchRecyclerView);
        doctorAdapter.setOnItemClickListener(this);
        doctorAdapter.setOnItemChildClickListener(this);
        searchRecyclerView.setAdapter(doctorAdapter);
        getHospitalListByReverse();
        showNone = true;
        getDoctorListByReverse(new HashMap<>(16));
    }

    @Override
    public void initListener() {
        super.initListener();
        selectRecyclerView.setOnFlingListener(new RecyclerView.OnFlingListener() {
            @Override
            public boolean onFling(int velocityX, int velocityY) {
                hideSoftInputFromWindow();
                return false;
            }
        });
        searchRecyclerView.setOnFlingListener(new RecyclerView.OnFlingListener() {
            @Override
            public boolean onFling(int velocityX, int velocityY) {
                hideSoftInputFromWindow();
                return false;
            }
        });
        etSearchCheckType.addTextChangedListener(new AbstractTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    page = 1;
                    doctors.clear();
                    searchDoctor(s.toString());
                    selectEnd();
                }
                else {
                    getDoctorListByReverse(new HashMap<>(16));
                    tvSelect.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    /**
     * 获取医院列表
     */
    private void getHospitalListByReverse() {
        if (isTransferOther) {
            //转给他人
            RequestUtils.getHospitalListByDoctor(this, loginBean.getToken(), orderNo, this);
        }
        else {
            // (创建预约服务订单)
            RequestUtils.getHospitalListByReverse(this, loginBean.getToken(), this);
        }
    }

    /**
     * 获取一级科室列表
     */
    private void getDepartOneListByReverse() {
        RequestUtils.getDepartOneListByReverse(this, loginBean.getToken(), curHospital.getHospitalCode(), this);
    }

    /**
     * 获取二级科室列表
     */
    private void getDepartTwoListByReverse() {
        RequestUtils.getDepartTwoListByReverse(this, loginBean.getToken(), curHospital.getHospitalCode(),
                                               curHospitalDepartBean.getDepartmentId(), this);
    }

    /**
     * 获取医生列表
     *
     * @param params
     */
    private void getDoctorListByReverse(Map<String, Object> params) {
        if (isTransferOther) {
            //重新转诊的接诊医生
            params.put("orderNo", orderNo);
            RequestUtils.getReceivingDoctorList(this, loginBean.getToken(), params, BaseData.BASE_PAGE_DATA_NUM, page,
                                                this);
        }
        else {
            //预约转诊的接诊医生
            RequestUtils.getDoctorListByReverse(this, loginBean.getToken(), params, BaseData.BASE_PAGE_DATA_NUM, page,
                                                this);
        }
    }

    /**
     * 选择结束后操作
     */
    private void selectEnd() {
        if (tvSelect.isSelected()) {
            layoutExpand.collapse();
            tvSelect.setSelected(false);
            layoutBg.setVisibility(View.GONE);
        }
    }

    /**
     * 医院数据处理
     */
    private void initHospital() {
        tvHospitalTitle.setSelected(true);
        viewHospitalBar.setVisibility(View.VISIBLE);
        //
        tvDepartOneTitle.setVisibility(View.INVISIBLE);
        viewDepartOneBar.setVisibility(View.GONE);
        tvDepartTwoTitle.setVisibility(View.INVISIBLE);
        viewDepartTwoBar.setVisibility(View.GONE);
    }

    /**
     * 一级科室数据处理
     */
    private void initDepartOne() {
        curType = 1;
        tvHospitalTitle.setText(curHospital.getHospitalName());
        tvHospitalTitle.setSelected(false);
        viewHospitalBar.setVisibility(View.GONE);
        viewDepartOneBar.setVisibility(View.VISIBLE);
        tvDepartOneTitle.setVisibility(View.VISIBLE);
        tvDepartOneTitle.setSelected(true);
    }

    /**
     * 二级科室数据处理
     */
    private void initDepartTwo() {
        curType = 2;
        tvDepartOneTitle.setText(curHospitalDepartBean.getDepartmentName());
        tvDepartOneTitle.setSelected(false);
        viewDepartOneBar.setVisibility(View.GONE);
        tvDepartTwoTitle.setVisibility(View.VISIBLE);
        viewDepartTwoBar.setVisibility(View.VISIBLE);
        tvDepartTwoTitle.setSelected(true);
    }

    /**
     * 医生搜索
     */
    private void searchDoctor(String key) {
        tvSelect.setVisibility(View.GONE);
        params = new HashMap<>(16);
        params.put("doctorName", key);
        getDoctorListByReverse(params);
    }

    @OnClick({ R.id.tv_select, R.id.tv_reset })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_select:
                hideSoftInputFromWindow();
                if (tvSelect.isSelected()) {
                    layoutBg.postDelayed(() -> layoutBg.setVisibility(View.GONE), 200);
                    tvSelect.setSelected(false);
                    layoutExpand.collapse();
                }
                else {
                    layoutBg.setVisibility(View.VISIBLE);
                    tvSelect.setSelected(true);
                    layoutExpand.expand();
                }
                break;
            case R.id.tv_reset:
                page = 1;
                curType = 0;
                tvSelect.setText(R.string.txt_select_hospital_depart);
                tvHospitalTitle.setText(R.string.txt_select_hint);
                tvHospitalTitle.setSelected(false);
                data = new ArrayList<>();
                if (hospitals != null) {
                    for (HospitalBean bean : hospitals) {
                        data.add(bean.getHospitalName());
                    }
                }
                adapter.setNewData(data);
                getDoctorListByReverse(new HashMap<>(16));
                initHospital();
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (adapter.getItem(position) instanceof DoctorInfoBean) {
            hideSoftInputFromWindow();
            Intent intent = new Intent();
            intent.putExtra(CommonData.KEY_DOCTOR_BEAN, doctors.get(position));
            setResult(RESULT_OK, intent);
            finish();
        }
        else {
            switch (curType) {
                case 0:
                    curHospital = hospitals.get(position);
                    tvSelect.setText(curHospital.getHospitalName());
                    getDepartOneListByReverse();
                    break;
                case 1:
                    curDepartOnePosition = position;
                    this.adapter.setCurPosition(curDepartOnePosition);
                    if (position != 0) {
                        curHospitalDepartBean = departOne.get(position - 1);
                        tvSelect.setText(
                                curHospital.getHospitalName() + "-" + curHospitalDepartBean.getDepartmentName());
                        getDepartTwoListByReverse();
                    }
                    else {
                        //选择的全部一级科室
                        selectEnd();
                        tvSelect.setText(curHospital.getHospitalName() + "-全部科室");
                        params = new HashMap<>(16);
                        params.put("hospitalCode", curHospital.getHospitalCode());
                        page = 1;
                        doctors.clear();
                        getDoctorListByReverse(params);
                    }
                    break;
                case 2:
                    curDepartTwoPosition = position;
                    this.adapter.setCurPosition(curDepartTwoPosition);
                    selectEnd();
                    params = new HashMap<>(16);
                    params.put("hospitalCode", curHospital.getHospitalCode());
                    params.put("pid", curHospitalDepartBean.getDepartmentId());
                    if (position != 0) {
                        curHospitalDepartChildBean = departTwo.get(position - 1);
                        params.put("id", curHospitalDepartChildBean.getDepartmentId());
                        tvSelect.setText(
                                curHospital.getHospitalName() + "-" + curHospitalDepartBean.getDepartmentName() + "-" +
                                curHospitalDepartChildBean.getDepartmentName());
                    }
                    else {
                        //选择全部的二级科室
                        tvSelect.setText(
                                curHospital.getHospitalName() + "-" + curHospitalDepartBean.getDepartmentName() +
                                "-全部子科室");
                    }
                    page = 1;
                    doctors.clear();
                    getDoctorListByReverse(params);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        DoctorInfoBean bean = doctors.get(position);
        new HintDialog(this).setPhone(getString(R.string.txt_contact_doctor_phone), bean.getDoctorPhone())
                            .setOnEnterClickListener(() -> callPhone(bean.getDoctorPhone()))
                            .show();
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        switch (task) {
            case GET_HOSPITAL_LIST_BY_DOCTOR:
            case GET_HOSPITAL_LIST_BY_RESERVE:
                hospitals = (List<HospitalBean>)response.getData();
                data = new ArrayList<>();
                for (HospitalBean bean : hospitals) {
                    data.add(bean.getHospitalName());
                }
                adapter.setNewData(data);
                initHospital();
                break;
            case GET_DEPART_ONE_LIST_BY_REVERSE:
                departOne = (List<HospitalDepartBean>)response.getData();
                data = new ArrayList<>();
                data.add("全部");
                for (HospitalDepartBean bean : departOne) {
                    data.add(bean.getDepartmentName());
                }
                adapter.setNewData(data);
                initDepartOne();
                break;
            case GET_DEPART_TWO_LIST_BY_REVERSE:
                departTwo = (List<HospitalDepartChildBean>)response.getData();
                data = new ArrayList<>();
                data.add("全部");
                for (HospitalDepartChildBean bean : departTwo) {
                    data.add(bean.getDepartmentName());
                }
                adapter.setNewData(data);
                initDepartTwo();
                break;
            case GET_RECEIVING_DOCTOR_LIST:
            case GET_DOCTOR_LIST_BY_REVERSE:
                List<DoctorInfoBean> list = (List<DoctorInfoBean>)response.getData();
                if (list == null) {
                    list = new ArrayList<>();
                }
                if (page == BaseData.BASE_ONE) {
                    doctors.clear();
                }
                doctors.addAll(list);
                if (doctors.size() > 0) {
                    searchRecyclerView.setVisibility(View.VISIBLE);
                }
                if (showNone) {
                    showNone = false;
                    if (doctors.size() == 0) {
                        layoutNoneDoctor.setVisibility(View.VISIBLE);
                    }
                    else {
                        layoutNoneDoctor.setVisibility(View.GONE);
                    }
                }
                else {
                    layoutNoneDoctor.setVisibility(View.GONE);
                }
                doctorAdapter.setNewData(doctors);
                if (list.size() >= BaseData.BASE_PAGE_DATA_NUM) {
                    doctorAdapter.loadMoreComplete();
                }
                else {
                    doctorAdapter.loadMoreEnd();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onLoadMoreRequested() {
        page++;
        getDoctorListByReverse(params);
    }
}
