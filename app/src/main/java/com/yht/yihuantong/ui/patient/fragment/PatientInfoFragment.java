package com.yht.yihuantong.ui.patient.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yht.frame.data.BaseData;
import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.CommonData;
import com.yht.frame.data.Tasks;
import com.yht.frame.data.bean.PatientBean;
import com.yht.frame.data.bean.PatientOrderBean;
import com.yht.frame.data.bean.ReservationValidateBean;
import com.yht.frame.http.retrofit.RequestUtils;
import com.yht.frame.ui.BaseFragment;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.utils.ToastUtil;
import com.yht.frame.utils.glide.GlideHelper;
import com.yht.frame.widgets.recyclerview.decoration.TimeItemDecoration;
import com.yht.frame.widgets.recyclerview.loadview.CustomLoadMoreView;
import com.yht.frame.widgets.textview.JustifiedTextView;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ui.adapter.PatientOrderAdapter;
import com.yht.yihuantong.ui.check.ServiceDetailActivity;
import com.yht.yihuantong.ui.patient.EditLabelActivity;
import com.yht.yihuantong.ui.patient.TransferDetailActivity;
import com.yht.yihuantong.ui.remote.RemoteDetailActivity;
import com.yht.yihuantong.ui.reservation.ReservationDisableActivity;
import com.yht.yihuantong.ui.reservation.service.ReservationServiceActivity;
import com.yht.yihuantong.ui.reservation.transfer.ReservationTransferActivity;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

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
        implements BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemClickListener,
                   BaseQuickAdapter.OnItemChildClickListener {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private TextView tvName, tvAge, tvSex, tvNoLabel, tvAddTime;
    private JustifiedTextView tvPastMedical, familyMedical, tvAllergies;
    private LinearLayout layoutEditLabel;
    private TagFlowLayout flowLayout;
    private ImageView ivHeadImg;
    private View headerView, footerView;
    private PatientOrderAdapter patientOrderAdapter;
    /**
     * 时间分隔
     */
    private TimeItemDecoration timeItemDecoration;
    private PatientBean patientBean;
    /**
     * 当前患者code
     */
    private String patientCode;
    /**
     * 诊疗记录（检查、转诊、远程）
     */
    private List<PatientOrderBean> patientOrderBeans = new ArrayList<>();
    /**
     * 是否能发起转诊
     */
    private boolean applyTransferAble = false;
    /**
     * 是否能发起服务
     */
    private boolean applyServiceAble = false;
    /**
     * 页码
     */
    private int page = 1;
    /**
     * 标签刷新
     */
    public static final int REQUEST_CODE_EDIT_LABEL = 100;

    @Override
    public int getLayoutID() {
        return R.layout.fragment_patient_info;
    }

    @Override
    public void onResume() {
        super.onResume();
        getPatientDetail();
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        initHeaderView();
        initFooterView();
        initAdapter();
    }

    @Override
    public void fillNetWorkData() {
        super.fillNetWorkData();
        getPatientOrderList(true);
        getValidateHospitalList();
    }

    public void setPatientCode(String patientCode) {
        this.patientCode = patientCode;
    }

    /**
     * 获取患者详情
     */
    private void getPatientDetail() {
        RequestUtils.getPatientDetailByPatientCode(getContext(), patientCode, loginBean.getToken(), this);
    }

    /**
     * 获取患者订单记录
     */
    private void getPatientOrderList(boolean showLoadView) {
        RequestUtils.getPatientOrderListByPatientCode(getContext(), patientCode, loginBean.getToken(),
                                                      BaseData.BASE_PAGE_DATA_NUM, page, showLoadView, this);
    }

    /**
     * 校验医生是否有预约检查和预约转诊的合作医院
     */
    private void getValidateHospitalList() {
        RequestUtils.getValidateHospitalList(getContext(), loginBean.getToken(), this);
    }

    /**
     * 查询患者是否存在未完成的转诊单
     */
    private void getPatientExistTransfer() {
        RequestUtils.getPatientExistTransfer(getContext(), loginBean.getToken(), patientCode, this);
    }

    /**
     * header
     */
    private void initHeaderView() {
        headerView = LayoutInflater.from(getContext()).inflate(R.layout.view_patient_info_header, null);
        layoutEditLabel = headerView.findViewById(R.id.layout_edit_label);
        flowLayout = headerView.findViewById(R.id.layout_flow);
        ivHeadImg = headerView.findViewById(R.id.iv_patient_img);
        tvName = headerView.findViewById(R.id.tv_patient_name);
        tvAge = headerView.findViewById(R.id.tv_patient_age);
        tvSex = headerView.findViewById(R.id.tv_patient_sex);
        tvPastMedical = headerView.findViewById(R.id.tv_past_medical);
        familyMedical = headerView.findViewById(R.id.tv_family_medical);
        tvAllergies = headerView.findViewById(R.id.tv_allergies);
        tvNoLabel = headerView.findViewById(R.id.tv_no_label);
        tvAddTime = headerView.findViewById(R.id.tv_add_time);
        layoutEditLabel.setOnClickListener(this);
    }

    /**
     * footer
     */
    private void initFooterView() {
        footerView = LayoutInflater.from(getContext()).inflate(R.layout.view_patient_info_footer, null);
    }

    private void initAdapter() {
        timeItemDecoration = new TimeItemDecoration(getContext(), true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(timeItemDecoration);
        patientOrderAdapter = new PatientOrderAdapter(patientOrderBeans);
        patientOrderAdapter.setOnItemClickListener(this);
        patientOrderAdapter.setOnItemChildClickListener(this);
        patientOrderAdapter.setLoadMoreView(new CustomLoadMoreView());
        patientOrderAdapter.setOnLoadMoreListener(this, recyclerView);
        patientOrderAdapter.addHeaderView(headerView);
        recyclerView.setAdapter(patientOrderAdapter);
    }

    /**
     * 患者基础数据
     */
    private void initPatientBaseInfo() {
        tvName.setText(patientBean.getName());
        tvAge.setText(String.valueOf(patientBean.getAge()));
        tvSex.setText(patientBean.getSex() == BaseData.BASE_MALE
                      ? getString(R.string.txt_sex_male)
                      : getString(R.string.txt_sex_female));
        tvAddTime.setText(patientBean.getAddTime());
        tvPastMedical.setText(patientBean.getPast());
        familyMedical.setText(patientBean.getFamily());
        tvAllergies.setText(patientBean.getAllergy());
        Glide.with(this)
             .load(patientBean.getPhoto())
             .apply(GlideHelper.getOptions(BaseUtils.dp2px(getContext(), 4)))
             .into(ivHeadImg);
        if (patientBean.getTagList() != null && patientBean.getTagList().size() > 0) {
            //初始化适配器
            TagAdapter<String> tagAdapter = new TagAdapter<String>(patientBean.getTagList()) {
                @Override
                public View getView(FlowLayout parent, int position, String s) {
                    return createNewLabel(s, flowLayout);
                }
            };
            flowLayout.setAdapter(tagAdapter);
            tvNoLabel.setVisibility(View.GONE);
            flowLayout.setVisibility(View.VISIBLE);
        }
        else {
            tvNoLabel.setVisibility(View.VISIBLE);
            flowLayout.setVisibility(View.GONE);
        }
    }

    /**
     * 创建一个正常状态的标签
     */
    private TextView createNewLabel(String label, ViewGroup parent) {
        TextView textView = (TextView)getLayoutInflater().inflate(R.layout.item_text_label, parent, false);
        textView.setBackgroundResource(R.drawable.corner28_stroke1_c5c8cc);
        textView.setTextColor(ContextCompat.getColor(getContext(), R.color.color_6a6f80));
        //设置边界
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                                                         ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(BaseUtils.dp2px(getContext(), 6), BaseUtils.dp2px(getContext(), 10),
                          BaseUtils.dp2px(getContext(), 6), 0);
        textView.setLayoutParams(params);
        if (parent != null) {
            textView.setCompoundDrawables(null, null, null, null);
        }
        textView.setText(label);
        return textView;
    }

    /**
     * 订单分组排序
     */
    private void sortOrderList() {
        List<String> titleBars = new ArrayList<>();
        for (PatientOrderBean bean : patientOrderBeans) {
            String time = BaseUtils.formatDate(
                    BaseUtils.date2TimeStamp(bean.getCreateAt(), BaseUtils.YYYY_MM_DD_HH_MM_SS), BaseUtils.YYYY_MM_DD);
            titleBars.add(time);
        }
        //返回一个包含所有Tag字母在内的字符串并赋值给tagsStr
        String tagsStr = BaseUtils.getTimeTags(titleBars);
        timeItemDecoration.setTitleBar(titleBars, tagsStr);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Intent intent;
        PatientOrderBean bean = patientOrderBeans.get(position);
        switch (bean.getItemType()) {
            case PatientOrderBean.CHECK:
                intent = new Intent(getContext(), ServiceDetailActivity.class);
                break;
            case PatientOrderBean.TRANSFER:
                intent = new Intent(getContext(), TransferDetailActivity.class);
                break;
            case PatientOrderBean.REMOTE:
                intent = new Intent(getContext(), RemoteDetailActivity.class);
                break;
            default:
                intent = new Intent(getContext(), ServiceDetailActivity.class);
                break;
        }
        intent.putExtra(CommonData.KEY_ORDER_ID, bean.getOrderNo());
        startActivity(intent);
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        Intent intent = new Intent(getContext(), ServiceDetailActivity.class);
        intent.putExtra(CommonData.KEY_ORDER_ID, patientOrderBeans.get(position).getOrderNo());
        intent.putExtra(CommonData.KEY_INTENT_BOOLEAN, true);
        startActivity(intent);
    }

    @OnClick({ R.id.tv_reserve_check, R.id.tv_reserve_transfer })
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.tv_reserve_check:
                if (applyServiceAble) {
                    intent = new Intent(getContext(), ReservationServiceActivity.class);
                    intent.putExtra(CommonData.KEY_PATIENT_BEAN, patientBean);
                    startActivity(intent);
                }
                else {
                    intent = new Intent(getContext(), ReservationDisableActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.tv_reserve_transfer:
                getPatientExistTransfer();
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == layoutEditLabel.getId()) {
            Intent intent = new Intent(getContext(), EditLabelActivity.class);
            intent.putExtra(CommonData.KEY_PATIENT_CODE, patientCode);
            startActivityForResult(intent, REQUEST_CODE_EDIT_LABEL);
        }
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        switch (task) {
            case GET_PATIENT_DETAIL_BY_PATIENT_CODE:
                patientBean = (PatientBean)response.getData();
                initPatientBaseInfo();
                break;
            case GET_PATIENT_ORDER_LIST_BY_PATIENT_CODE:
                List<PatientOrderBean> list = (List<PatientOrderBean>)response.getData();
                if (page == BASE_ONE) {
                    patientOrderBeans.clear();
                }
                patientOrderBeans.addAll(list);
                sortOrderList();
                patientOrderAdapter.setNewData(patientOrderBeans);
                if (list != null && list.size() < BASE_PAGE_DATA_NUM) {
                    if (patientOrderBeans.size() > BASE_PAGE_DATA_NUM) {
                        patientOrderAdapter.loadMoreEnd();
                    }
                    else {
                        patientOrderAdapter.setEnableLoadMore(false);
                    }
                }
                else {
                    patientOrderAdapter.loadMoreComplete();
                }
                if (patientOrderBeans != null && patientOrderBeans.size() > 0) {
                    patientOrderAdapter.removeAllFooterView();
                }
                else {
                    patientOrderAdapter.addFooterView(footerView);
                }
                break;
            case GET_PATIENT_EXIST_TRANSFER:
                //预约转诊前判断是否已有转诊未处理
                boolean exist = (boolean)response.getData();
                if (exist) {
                    ToastUtil.toast(getContext(), R.string.txt_patient_exist_transfer);
                }
                else {
                    Intent intent;
                    if (applyTransferAble) {
                        intent = new Intent(getContext(), ReservationTransferActivity.class);
                        intent.putExtra(CommonData.KEY_PATIENT_BEAN, patientBean);
                        startActivity(intent);
                    }
                    else {
                        intent = new Intent(getContext(), ReservationDisableActivity.class);
                        intent.putExtra(CommonData.KEY_CHECK_OR_TRANSFER, true);
                        startActivity(intent);
                    }
                }
                break;
            case GET_VALIDATE_HOSPITAL_LIST:
                ReservationValidateBean bean = (ReservationValidateBean)response.getData();
                if (bean != null) {
                    applyTransferAble = bean.isZz();
                    applyServiceAble = bean.isJc();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_EDIT_LABEL) {
            getPatientDetail();
        }
    }

    @Override
    public void onLoadMoreRequested() {
        page++;
        getPatientOrderList(false);
    }
}
