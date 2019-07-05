package com.yht.yihuantong.ui.transfer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yht.frame.data.base.HospitalBean;
import com.yht.frame.data.base.HospitalDepartBean;
import com.yht.frame.data.base.HospitalDepartChildBean;
import com.yht.frame.data.bean.PatientBean;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.widgets.edittext.AbstractTextWatcher;
import com.yht.frame.widgets.edittext.SuperEditText;
import com.yht.frame.widgets.view.ExpandableLayout;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ui.adapter.DepartOneAdapter;
import com.yht.yihuantong.ui.adapter.DoctorAdapter;

import java.util.ArrayList;
import java.util.List;

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
    private DepartOneAdapter departOneAdapter;
    private DoctorAdapter doctorAdapter;
    private List<HospitalBean> hospitals;
    private List<HospitalDepartBean> departOne;
    private List<HospitalDepartChildBean> departTwo;
    private List<PatientBean> doctors;
    /**
     * 当前选择列表  0为医院 1为大科室  2为小科室
     */
    private int curType;

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
        //        //科室医院列表
        //        selectRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //        departOneAdapter = new DepartOneAdapter(R.layout.item_depart, departOne);
        //        departOneAdapter.setOnItemClickListener(this);
        //        selectRecyclerView.setAdapter(departOneAdapter);
        //        //医生列表
        //        searchRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //        doctorAdapter = new DoctorAdapter(R.layout.item_doctor, doctors);
        //        doctorAdapter.setLoadMoreView(new CustomLoadMoreView());
        //        doctorAdapter.setOnLoadMoreListener(this, searchRecyclerView);
        //        doctorAdapter.setOnItemClickListener(this);
        //        doctorAdapter.setOnItemChildClickListener(this);
        //        searchRecyclerView.setAdapter(doctorAdapter);
    }

    @Override
    public void initListener() {
        super.initListener();
        etSearchCheckType.addTextChangedListener(new AbstractTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    searchDoctor();
                }
                else {
                    init();
                }
            }
        });
    }

    /**
     * 恢复到最初状态
     */
    private void init() {
        //恢复最初状态
        curType = 0;
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
        //        hospitals = new ArrayList<>();
        //        for (int i = 0; i < 10; i++) {
        //            hospitals.add("医院--" + (i + 1));
        //        }
        //        departOneAdapter.setNewData(hospitals);
        //        tvOne.setSelected(true);
        //        view1.setVisibility(View.VISIBLE);
        //        //
        //        tvTwo.setVisibility(View.INVISIBLE);
        //        view2.setVisibility(View.GONE);
        //        tvThree.setVisibility(View.INVISIBLE);
        //        view3.setVisibility(View.GONE);
    }

    /**
     * 一级科室数据处理
     */
    private void initDepartOne(HospitalDepartBean hospital) {
        //        departOne = new ArrayList<>();
        //        for (int i = 0; i < 10; i++) {
        //            departOne.add("大科室--" + (i + 1));
        //        }
        //        departOneAdapter.setNewData(departOne);
        //        tvOne.setText(hospital);
        //        tvOne.setSelected(false);
        //        view1.setVisibility(View.GONE);
        //        view2.setVisibility(View.VISIBLE);
        //        tvTwo.setVisibility(View.VISIBLE);
        //        tvTwo.setSelected(true);
    }

    /**
     * 二级科室数据处理
     */
    private void initDepartTwo(HospitalDepartBean departOne) {
        //        departTwo = new ArrayList<>();
        //        for (int i = 0; i < 10; i++) {
        //            departTwo.add("小科室--" + (i + 1));
        //        }
        //        departOneAdapter.setNewData(departTwo);
        //        tvTwo.setText(departOne.getDepartmentName());
        //        tvTwo.setSelected(false);
        //        view2.setVisibility(View.GONE);
        //        tvThree.setVisibility(View.VISIBLE);
        //        view3.setVisibility(View.VISIBLE);
        //        tvThree.setSelected(true);
    }

    /**
     * 二级科室下医生数据处理
     */
    private void initDoctors(String departTwo) {
        searchRecyclerView.setVisibility(View.VISIBLE);
        doctors = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            PatientBean patientBean = new PatientBean();
            patientBean.setName("医生--" + (i + 1));
            doctors.add(patientBean);
        }
        doctorAdapter.setNewData(doctors);
        doctorAdapter.loadMoreEnd();
    }

    /**
     * 医生搜索数据处理
     */
    private void initSearchDoctors(String departTwo) {
        tvSelect.setVisibility(View.GONE);
        searchRecyclerView.setVisibility(View.VISIBLE);
        doctors = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            PatientBean patientBean = new PatientBean();
            patientBean.setName("医生搜索--" + (i + 1));
            doctors.add(patientBean);
        }
        doctorAdapter.setNewData(doctors);
        doctorAdapter.loadMoreEnd();
    }

    /**
     * 医生搜索
     */
    private void searchDoctor() {
        initSearchDoctors("");
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
                init();
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (adapter.getItem(position) instanceof PatientBean) {
            hideSoftInputFromWindow(etSearchCheckType);
            setResult(RESULT_OK);
            finish();
        }
        else {
            switch (curType) {
                case 0:
                    curType = 1;
                    //                    initDepartOne(hospitals.get(position));
                    break;
                case 1:
                    curType = 2;
                    initDepartTwo(departOne.get(position));
                    break;
                case 2:
                    init();
                    initDoctors("");
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
    public void onLoadMoreRequested() {
    }
}
