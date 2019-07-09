package com.yht.yihuantong.ui.transfer;

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
import com.yht.frame.data.Tasks;
import com.yht.frame.data.base.DoctorInfoBean;
import com.yht.frame.data.base.HospitalBean;
import com.yht.frame.data.base.HospitalDepartBean;
import com.yht.frame.data.base.HospitalDepartChildBean;
import com.yht.frame.http.retrofit.RequestUtils;
import com.yht.frame.ui.BaseActivity;
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
    TextView tvOne;
    @BindView(R.id.tv_two)
    TextView tvTwo;
    @BindView(R.id.tv_three)
    TextView tvThree;
    @BindView(R.id.et_search_check_type)
    SuperEditText etSearchCheckType;
    @BindView(R.id.view1)
    View view1;
    @BindView(R.id.view2)
    View view2;
    @BindView(R.id.view3)
    View view3;
    @BindView(R.id.layout_none_doctor)
    LinearLayout layoutNoneDoctor;
    /**
     * 医院、科室适配器
     */
    private ReserveTransferSelectDoctorAdapter reserveTransferSelectDoctorAdapter;
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
    private List<DoctorInfoBean> doctors;
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
     * 当前选择列表  0为医院 1为大科室  2为小科室
     */
    private int curType;
    /**
     * 页码
     */
    private int page = 1;

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_select_receiving_doctor;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        //科室医院列表
        selectRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        reserveTransferSelectDoctorAdapter = new ReserveTransferSelectDoctorAdapter(R.layout.item_depart, data);
        reserveTransferSelectDoctorAdapter.setOnItemClickListener(this);
        selectRecyclerView.setAdapter(reserveTransferSelectDoctorAdapter);
        //医生列表
        searchRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        doctorAdapter = new DoctorAdapter(R.layout.item_doctor, doctors);
        doctorAdapter.setLoadMoreView(new CustomLoadMoreView());
        doctorAdapter.setOnLoadMoreListener(this, searchRecyclerView);
        doctorAdapter.setOnItemClickListener(this);
        doctorAdapter.setOnItemChildClickListener(this);
        searchRecyclerView.setAdapter(doctorAdapter);
        getHospitalListByReverse();
    }

    @Override
    public void initListener() {
        super.initListener();
        etSearchCheckType.addTextChangedListener(new AbstractTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    searchDoctor(s.toString());
                }
                else {
                    init();
                }
            }
        });
    }

    /**
     * 获取医院列表
     */
    private void getHospitalListByReverse() {
        RequestUtils.getHospitalListByReverse(this, loginBean.getToken(), this);
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
        RequestUtils.getDoctorListByReverse(this, loginBean.getToken(), params, BaseData.BASE_PAGE_DATA_NUM, page,
                                            this);
    }

    /**
     * 恢复到最初状态
     */
    private void init() {
        layoutExpand.collapse();
        tvSelect.setVisibility(View.VISIBLE);
        tvSelect.setSelected(false);
        layoutBg.setVisibility(View.GONE);
        searchRecyclerView.setVisibility(View.GONE);
    }

    /**
     * 医院数据处理
     */
    private void initHospital() {
        tvOne.setSelected(true);
        view1.setVisibility(View.VISIBLE);
        //
        tvTwo.setVisibility(View.INVISIBLE);
        view2.setVisibility(View.GONE);
        tvThree.setVisibility(View.INVISIBLE);
        view3.setVisibility(View.GONE);
    }

    /**
     * 一级科室数据处理
     */
    private void initDepartOne() {
        tvOne.setText(curHospital.getHospitalName());
        tvOne.setSelected(false);
        view1.setVisibility(View.GONE);
        view2.setVisibility(View.VISIBLE);
        tvTwo.setVisibility(View.VISIBLE);
        tvTwo.setSelected(true);
    }

    /**
     * 二级科室数据处理
     */
    private void initDepartTwo() {
        tvTwo.setText(curHospitalDepartBean.getDepartmentName());
        tvTwo.setSelected(false);
        view2.setVisibility(View.GONE);
        tvThree.setVisibility(View.VISIBLE);
        view3.setVisibility(View.VISIBLE);
        tvThree.setSelected(true);
    }

    /**
     * 医生搜索
     */
    private void searchDoctor(String key) {
        tvSelect.setVisibility(View.GONE);
        Map<String, Object> params = new HashMap<>(16);
        params.put("doctorName", key);
        getDoctorListByReverse(params);
    }

    @OnClick({ R.id.tv_select, R.id.tv_reset })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_select:
                if (tvSelect.isSelected()) {
                    layoutBg.postDelayed(() -> layoutBg.setVisibility(View.GONE), 200);
                    tvSelect.setSelected(false);
                    layoutExpand.collapse();
                }
                else {
                    initHospital();
                    layoutBg.setVisibility(View.VISIBLE);
                    tvSelect.setSelected(true);
                    layoutExpand.expand();
                }
                break;
            case R.id.tv_reset:
                tvSelect.setText(R.string.txt_select_hospital_depart);
                data = new ArrayList<>();
                for (HospitalBean bean : hospitals) {
                    data.add(bean.getHospitalName());
                }
                reserveTransferSelectDoctorAdapter.setNewData(data);
                initHospital();
                init();
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (adapter.getItem(position) instanceof DoctorInfoBean) {
            hideSoftInputFromWindow(etSearchCheckType);
            setResult(RESULT_OK);
            finish();
        }
        else {
            switch (curType) {
                case 0:
                    curType = 1;
                    curHospital = hospitals.get(position);
                    tvSelect.setText(curHospital.getHospitalName());
                    getDepartOneListByReverse();
                    break;
                case 1:
                    curType = 2;
                    if (position != 0) {
                        curHospitalDepartBean = departOne.get(position);
                        tvSelect.setText(
                                curHospital.getHospitalName() + "-" + curHospitalDepartBean.getDepartmentName());
                        getDepartTwoListByReverse();
                    }
                    else {
                        init();
                        tvSelect.setText(curHospital.getHospitalName() + "-全部科室");
                        Map<String, Object> params = new HashMap<>(16);
                        params.put("hospitalCode", curHospital.getHospitalCode());
                        getDoctorListByReverse(params);
                    }
                    break;
                case 2:
                    init();
                    Map<String, Object> params = new HashMap<>(16);
                    params.put("hospitalCode", curHospital.getHospitalCode());
                    if (position != 0) {
                        curHospitalDepartChildBean = departTwo.get(position);
                        params.put("pid", curHospitalDepartChildBean.getDepartmentId());
                        tvSelect.setText(
                                curHospital.getHospitalName() + "-" + curHospitalDepartBean.getDepartmentName() + "-" +
                                curHospitalDepartChildBean.getDepartmentName());
                    }
                    else {
                        params.put("pid", curHospitalDepartBean.getDepartmentId());
                        tvSelect.setText(
                                curHospital.getHospitalName() + "-" + curHospitalDepartBean.getDepartmentName() +
                                "-全部子科室");
                    }
                    getDoctorListByReverse(params);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        switch (task) {
            case GET_HOSPITAL_LIST_BY_RESERVE:
                hospitals = (List<HospitalBean>)response.getData();
                data = new ArrayList<>();
                for (HospitalBean bean : hospitals) {
                    data.add(bean.getHospitalName());
                }
                reserveTransferSelectDoctorAdapter.setNewData(data);
                initHospital();
                break;
            case GET_DEPART_ONE_LIST_BY_REVERSE:
                departOne = (List<HospitalDepartBean>)response.getData();
                data = new ArrayList<>();
                data.add("全部");
                for (HospitalDepartBean bean : departOne) {
                    data.add(bean.getDepartmentName());
                }
                reserveTransferSelectDoctorAdapter.setNewData(data);
                initDepartOne();
                break;
            case GET_DEPART_TWO_LIST_BY_REVERSE:
                departTwo = (List<HospitalDepartChildBean>)response.getData();
                data = new ArrayList<>();
                data.add("全部");
                for (HospitalDepartChildBean bean : departTwo) {
                    data.add(bean.getDepartmentName());
                }
                reserveTransferSelectDoctorAdapter.setNewData(data);
                initDepartTwo();
                break;
            case GET_DOCTOR_LIST_BY_REVERSE:
                doctors = (List<DoctorInfoBean>)response.getData();
                searchRecyclerView.setVisibility(View.VISIBLE);
                doctorAdapter.setNewData(doctors);
                if (doctors != null && doctors.size() >= BaseData.BASE_PAGE_DATA_NUM) {
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
    }
}
