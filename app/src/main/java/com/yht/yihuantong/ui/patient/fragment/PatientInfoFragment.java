package com.yht.yihuantong.ui.patient.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.CommonData;
import com.yht.frame.data.Tasks;
import com.yht.frame.data.bean.CheckBean;
import com.yht.frame.http.retrofit.RequestUtils;
import com.yht.frame.ui.BaseFragment;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.widgets.recyclerview.decoration.TimeItemDecoration;
import com.yht.frame.widgets.recyclerview.loadview.CustomLoadMoreView;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ui.adapter.PatientInfoAdapter;
import com.yht.yihuantong.ui.check.CheckDetailActivity;
import com.yht.yihuantong.ui.patient.TransferDetailActivity;
import com.yht.yihuantong.ui.remote.RemoteDetailActivity;
import com.yht.yihuantong.ui.reservation.ReservationCheckOrTransferActivity;
import com.yht.yihuantong.utils.glide.GlideHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/5/17 14:55
 * @des 患者资料
 */
public class PatientInfoFragment extends BaseFragment
        implements BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemClickListener {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private TextView tvName, tvAge, tvSex, tvPastMedical, familyMedical, tvAllergies, tvNoneRecord;
    private ImageView ivHeadImg;
    private View headerView;
    private PatientInfoAdapter patientInfoAdapter;
    /**
     * 时间分隔
     */
    private TimeItemDecoration timeItemDecoration;
    /**
     * 当前患者code
     */
    private String patientCode;
    /**
     * 诊疗记录（检查、转诊、远程）
     */
    private List<CheckBean> data;
    private List<String> titleBars;

    @Override
    public int getLayoutID() {
        return R.layout.fragment_patient_info;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        initHeaderView();
        initAdapter();
        initPage();
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        getPatientDetail();
        data = new ArrayList<>();
        titleBars = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            if (i < 2) {
                CheckBean bean = new CheckBean();
                bean.setItemType(CheckBean.CHECK);
                bean.setTitle("全身检查");
                bean.setTime("2019-06-29");
                titleBars.add("2019-06-29");
                data.add(bean);
            }
            else if (i < 4) {
                CheckBean bean = new CheckBean();
                bean.setItemType(CheckBean.TRANSFER);
                bean.setTime("2019-06-30");
                titleBars.add("2019-06-30");
                data.add(bean);
            }
            else {
                CheckBean bean = new CheckBean();
                bean.setItemType(CheckBean.REMOTE);
                bean.setTime("2019-06-31");
                titleBars.add("2019-06-31");
                data.add(bean);
            }
        }
        patientInfoAdapter.setNewData(data);
        patientInfoAdapter.loadMoreEnd();
        //返回一个包含所有Tag字母在内的字符串并赋值给tagsStr
        String tagsStr = BaseUtils.getTimeTags(data);
        timeItemDecoration.setTitleBar(titleBars, tagsStr);
    }

    private void getPatientDetail() {
        RequestUtils.getPatientDetailByPatientCode(getContext(), patientCode, loginBean.getToken(), this);
    }

    public void setPatientCode(String patientCode) {
        this.patientCode = patientCode;
    }

    /**
     * header
     */
    private void initHeaderView() {
        headerView = LayoutInflater.from(getContext()).inflate(R.layout.view_patient_info_header, null);
        ivHeadImg = headerView.findViewById(R.id.iv_patient_img);
        tvName = headerView.findViewById(R.id.tv_patient_name);
        tvAge = headerView.findViewById(R.id.tv_patient_age);
        tvSex = headerView.findViewById(R.id.tv_patient_sex);
        tvPastMedical = headerView.findViewById(R.id.tv_past_medical);
        familyMedical = headerView.findViewById(R.id.tv_family_medical);
        tvAllergies = headerView.findViewById(R.id.tv_allergies);
        tvNoneRecord = headerView.findViewById(R.id.tv_none_medical_recording);
    }

    private void initAdapter() {
        timeItemDecoration = new TimeItemDecoration(getContext(), true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(timeItemDecoration);
        patientInfoAdapter = new PatientInfoAdapter(data);
        patientInfoAdapter.setOnItemClickListener(this);
        patientInfoAdapter.setLoadMoreView(new CustomLoadMoreView());
        patientInfoAdapter.setOnLoadMoreListener(this, recyclerView);
        patientInfoAdapter.addHeaderView(headerView);
        recyclerView.setAdapter(patientInfoAdapter);
    }

    /**
     * 页面数据
     */
    private void initPage() {
        tvName.setText("姓名");
        tvAge.setText("111");
        tvSex.setText("男");
        tvPastMedical.setText("既往病史既往病史既往病史既往病史既往病史既往病史既往病史既往病史既往病史既往病史");
        familyMedical.setText("无");
        tvAllergies.setText("无");
        Glide.with(this).load("").apply(GlideHelper.getOptionsP(BaseUtils.dp2px(getContext(), 4))).into(ivHeadImg);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Intent intent = null;
        CheckBean bean = data.get(position);
        switch (bean.getItemType()) {
            case CheckBean.CHECK:
                intent = new Intent(getContext(), CheckDetailActivity.class);
                break;
            case CheckBean.TRANSFER:
                intent = new Intent(getContext(), TransferDetailActivity.class);
                break;
            case CheckBean.REMOTE:
                intent = new Intent(getContext(), RemoteDetailActivity.class);
                break;
            default:
                break;
        }
        startActivity(intent);
    }

    @OnClick({ R.id.tv_reserve_check, R.id.tv_reserve_transfer })
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.tv_reserve_check:
                intent = new Intent(getContext(), ReservationCheckOrTransferActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_reserve_transfer:
                intent = new Intent(getContext(), ReservationCheckOrTransferActivity.class);
                intent.putExtra(CommonData.KEY_CHECK_OR_TRANSFER, true);
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
            case GET_PATIENT_DETAIL_BY_PATIENT_CODE:
                break;
            default:
                break;
        }
    }

    @Override
    public void onLoadMoreRequested() {
    }
}
