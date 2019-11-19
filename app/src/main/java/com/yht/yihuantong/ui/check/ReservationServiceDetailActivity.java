package com.yht.yihuantong.ui.check;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.yht.frame.data.BaseData;
import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.CommonData;
import com.yht.frame.data.Tasks;
import com.yht.frame.data.bean.CheckDetailBean;
import com.yht.frame.data.bean.CheckTypeByDetailBean;
import com.yht.frame.data.type.ServiceOrderStatus;
import com.yht.frame.data.type.CheckTypeStatus;
import com.yht.frame.data.type.SuggestionTypeStatus;
import com.yht.frame.http.retrofit.RequestUtils;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.utils.glide.GlideHelper;
import com.yht.frame.widgets.LoadViewHelper;
import com.yht.frame.widgets.dialog.HintDialog;
import com.yht.frame.widgets.textview.JustifiedTextView;
import com.yht.frame.widgets.view.CenterImageSpan;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ui.x5.FileDisplayActivity;
import com.yht.yihuantong.utils.FileUrlUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/6/14 10:56
 * @des 预约服务详情
 */
public class ReservationServiceDetailActivity extends BaseActivity
        implements ServiceOrderStatus, CheckTypeStatus, SuggestionTypeStatus,
        LoadViewHelper.OnNextClickListener {
    @BindView(R.id.iv_patient_img)
    ImageView ivPatientImg;
    @BindView(R.id.tv_patient_name)
    TextView tvPatientName;
    @BindView(R.id.tv_patient_age)
    TextView tvPatientAge;
    @BindView(R.id.tv_patient_sex)
    TextView tvPatientSex;
    @BindView(R.id.tv_check_doctor_top)
    TextView tvCheckDoctorTop;
    @BindView(R.id.tv_check_doctor_hospital_depart)
    TextView tvCheckDoctorHospitalDepart;
    @BindView(R.id.tv_check_doctor_hospital_depart_bottom)
    TextView tvCheckDoctorHospitalDepartBottom;
    @BindView(R.id.tv_check_time_top)
    TextView tvCheckTimeTop;
    @BindView(R.id.tv_check_diagnosis_top)
    TextView tvCheckDiagnosisTop;
    @BindView(R.id.layout_top)
    LinearLayout layoutTop;
    @BindView(R.id.layout_check_type_root)
    LinearLayout layoutCheckTypeRoot;
    @BindView(R.id.layout_check_type)
    LinearLayout layoutCheckType;
    @BindView(R.id.tv_check_hospital)
    TextView tvCheckHospital;
    @BindView(R.id.tv_check_payment)
    TextView tvCheckPayment;
    @BindView(R.id.tv_check_status)
    TextView tvCheckStatus;
    @BindView(R.id.tv_check_cancel)
    TextView tvCheckCancel;
    @BindView(R.id.tv_check_report)
    TextView tvCheckReport;
    @BindView(R.id.layout_check_report)
    LinearLayout layoutCheckReport;
    @BindView(R.id.tv_check_doctor_bottom)
    TextView tvCheckDoctorBottom;
    @BindView(R.id.tv_check_time_bottom)
    TextView tvCheckTimeBottom;
    @BindView(R.id.tv_check_diagnosis_bottom)
    TextView tvCheckDiagnosisBottom;
    @BindView(R.id.layout_bottom)
    LinearLayout layoutBottom;
    @BindView(R.id.layout_contact)
    LinearLayout layoutContact;
    @BindView(R.id.iv_check_status)
    ImageView ivCheckStatus;
    @BindView(R.id.layout_cancel_result)
    LinearLayout layoutCancelResult;
    @BindView(R.id.layout_check_report_root)
    RelativeLayout layoutCheckReportRoot;
    @BindView(R.id.layout_hint)
    LinearLayout layoutHint;
    @BindView(R.id.scroll_view)
    ScrollView scrollView;
    @BindView(R.id.tv_past_medical)
    JustifiedTextView tvPastMedical;
    @BindView(R.id.tv_family_medical)
    JustifiedTextView tvFamilyMedical;
    @BindView(R.id.tv_allergies)
    JustifiedTextView tvAllergies;
    /**
     * 检查详情
     */
    private CheckDetailBean checkDetailBean;
    /**
     * 检查项
     */
    private List<CheckTypeByDetailBean> checkTypeList;
    /**
     * 已上传报告的检查项 (每一项可能含有多个报告地址)
     */
    private List<CheckTypeByDetailBean> reportList;
    /**
     * 已拆分后的检查报告列表（每一项只包含一项报告）
     */
    private ArrayList<CheckTypeByDetailBean> newReportUrls;
    private Bitmap bitmapCancel, bitmapNoReach, bitmapReach;
    /**
     * 订单
     */
    private String orderNo;
    /**
     * 头部底部判断
     */
    private boolean isShowBottom;
    /**
     * 是否滚动到底部
     */
    private boolean isScrollBottom;
    /**
     * 填写诊断意见
     */
    public static final int REQUEST_CODE_EDIT_ADVICE = 100;

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_check_detail;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        loadViewHelper = new LoadViewHelper(this);
        loadViewHelper.setOnNextClickListener(this);
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        if (getIntent() != null) {
            isShowBottom = getIntent().getBooleanExtra(CommonData.KEY_PUBLIC, false);
            isScrollBottom = getIntent().getBooleanExtra(CommonData.KEY_INTENT_BOOLEAN, false);
            orderNo = getIntent().getStringExtra(CommonData.KEY_ORDER_ID);
        }
        initBitmap();
        initBasePage();
        if (BaseUtils.isNetworkAvailable(this)) {
            getReserveCheckOrderDetail();
        } else {
            layoutHint.setVisibility(View.VISIBLE);
            loadViewHelper.load(LoadViewHelper.NONE_NETWORK);
        }
    }

    /**
     * 检查项状态图
     */
    private void initBitmap() {
        bitmapCancel = BitmapFactory.decodeResource(getApplication().getResources(),
                R.mipmap.ic_label_cancel);
        bitmapNoReach = BitmapFactory.decodeResource(getApplication().getResources(),
                R.mipmap.ic_label_noreach);
        bitmapReach = BitmapFactory.decodeResource(getApplication().getResources(),
                R.mipmap.ic_label_reach);
    }

    /**
     * 获取订单详情
     */
    private void getReserveCheckOrderDetail() {
        RequestUtils.getReserveCheckOrderDetail(this, loginBean.getToken(), orderNo, this);
    }

    /**
     * 页面展示逻辑
     */
    private void initBasePage() {
        if (isShowBottom) {
            layoutBottom.setVisibility(View.VISIBLE);
            layoutTop.setVisibility(View.GONE);
        } else {
            layoutBottom.setVisibility(View.GONE);
            layoutContact.setVisibility(View.GONE);
            layoutTop.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 详情数据
     */
    private void initDetailData() {
        if (checkDetailBean == null) {
            return;
        }
        initCheckType();
        tvPastMedical.setText(checkDetailBean.getPastHistory());
        tvFamilyMedical.setText(checkDetailBean.getFamilyHistory());
        tvAllergies.setText(checkDetailBean.getAllergyHistory());
        Glide.with(this)
                .load(FileUrlUtil.addTokenToUrl(checkDetailBean.getPatientPhoto()))
                .apply(GlideHelper.getOptions(BaseUtils.dp2px(this, 4)))
                .into(ivPatientImg);
        tvPatientName.setText(checkDetailBean.getPatientName());
        tvPatientSex.setText(checkDetailBean.getSex() == BaseData.BASE_ONE
                ? getString(R.string.txt_sex_male)
                : getString(R.string.txt_sex_female));
        tvPatientAge.setText(String.valueOf(checkDetailBean.getPatientAge()));
        if (isShowBottom) {
            tvCheckDoctorBottom.setText(checkDetailBean.getDoctorName());
            tvCheckDoctorHospitalDepartBottom.setText(String.format(getString(R.string.txt_joiner),
                    checkDetailBean.getSourceHospitalDepartmentName(),
                    checkDetailBean.getSourceHospitalName()));
            tvCheckTimeBottom.setText(checkDetailBean.getCreateAt());
            tvCheckDiagnosisBottom.setText(checkDetailBean.getInitResult());
        } else {
            tvCheckDoctorTop.setText(checkDetailBean.getDoctorName());
            tvCheckDoctorHospitalDepart.setText(String.format(getString(R.string.txt_joiner),
                    checkDetailBean.getSourceHospitalDepartmentName(),
                    checkDetailBean.getSourceHospitalName()));
            tvCheckTimeTop.setText(checkDetailBean.getCreateAt());
            tvCheckDiagnosisTop.setText(checkDetailBean.getInitResult());
        }
        int status = checkDetailBean.getStatus();
        switch (status) {
            case CHECK_ORDER_STATUS_INCOMPLETE:
                ivCheckStatus.setImageResource(R.mipmap.ic_tag_check_incomplete);
                layoutCancelResult.setVisibility(View.GONE);
                tvCheckStatus.setText(getString(R.string.txt_status_incomplete));
                break;
            case CHECK_ORDER_STATUS_COMPLETE:
                ivCheckStatus.setImageResource(R.mipmap.ic_status_complete);
                layoutCancelResult.setVisibility(View.GONE);
                tvCheckStatus.setText(getString(R.string.txt_status_complete));
                break;
            default:
                ivCheckStatus.setImageResource(R.mipmap.ic_status_cancel);
                layoutCancelResult.setVisibility(View.GONE);
                tvCheckStatus.setText(getString(R.string.txt_status_cancel));
                break;
        }
        tvCheckHospital.setText(checkDetailBean.getTargetHospitalName());
        //缴费类型
        int payType = checkDetailBean.getPayType();
        if (payType == BaseData.BASE_ZERO) {
            tvCheckPayment.setText(getString(R.string.txt_self_pay));
        } else if (payType == BaseData.BASE_ONE) {
            tvCheckPayment.setText(getString(R.string.txt_medicare));
        } else {
            tvCheckPayment.setText(getString(R.string.txt_self_medicare));
        }
        if (isScrollBottom) {
            scrollView.post(() -> scrollView.fullScroll(ScrollView.FOCUS_DOWN));
        }
    }

    @OnClick(R.id.tv_check_next)
    public void onViewClicked() {
        if (checkDetailBean == null) {
            return;
        }
        new HintDialog(this).setPhone(getString(R.string.txt_contact_patient_phone),
                checkDetailBean.getPatientMobile())
                .setOnEnterClickListener(() -> callPhone(checkDetailBean.getPatientMobile()))
                .show();
    }

    @Override
    public void onNextClick() {
        getReserveCheckOrderDetail();
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        if (task == Tasks.GET_RESERVE_CHECK_ORDER_DETAIL) {
            layoutHint.setVisibility(View.GONE);
            checkDetailBean = (CheckDetailBean) response.getData();
            initDetailData();
        }
    }

    /**
     * 检查项目
     */
    private void initCheckType() {
        layoutCheckType.removeAllViews();
        checkTypeList = checkDetailBean.getTrans();
        reportList = new ArrayList<>();
        if (checkTypeList != null && checkTypeList.size() > 0) {
            for (int i = 0; i < checkTypeList.size(); i++) {
                CheckTypeByDetailBean bean = checkTypeList.get(i);
                layoutCheckType.addView(addCheckType(bean));
            }
            //判断是否有已上传报告
            if (reportList.size() > 0) {
                layoutCheckReportRoot.setVisibility(View.VISIBLE);
                //拆分报告
                splitReportUrl();
                //添加报告
                addCheckReport();
            } else {
                layoutCheckReportRoot.setVisibility(View.GONE);
            }
        } else {
            layoutCheckReportRoot.setVisibility(View.GONE);
        }
    }

    /**
     * 添加检查项
     */
    private View addCheckType(CheckTypeByDetailBean bean) {
        View view = getLayoutInflater().inflate(R.layout.item_check_by_detail, null);
        ImageView imageView = view.findViewById(R.id.iv_check_type_dot);
        imageView.setVisibility(View.VISIBLE);
        //服务名称
        TextView tvType = view.findViewById(R.id.tv_check_type);
        if (bean.getItemType() == BASE_ONE) {
            serviceType(bean, view, tvType, imageView);
        } else {
            //服务包
            tvType.setText(String.format(getString(R.string.txt_space), bean.getPackName()));
            //服务包下的服务项父布局
            LinearLayout layoutChildCheck = view.findViewById(R.id.layout_two_check_type);
            ArrayList<CheckTypeByDetailBean> childCheck = bean.getItemList();
            if (childCheck != null && childCheck.size() > 0) {
                for (int i = 0; i < childCheck.size(); i++) {
                    CheckTypeByDetailBean childBean = childCheck.get(i);
                    View childView =
                            getLayoutInflater().inflate(R.layout.item_child_check_by_detail, null);
                    layoutChildCheck.addView(servicePackage(childBean, childView));
                }
            }
        }
        return view;
    }

    /**
     * 服务项
     */
    private void serviceType(CheckTypeByDetailBean bean, View view, TextView tvType,
                             ImageView imageView) {
        //填写诊断意见
        TextView editAdvice = view.findViewById(R.id.tv_check_edit);
        //预约就诊时间
        TextView reserveTime = view.findViewById(R.id.tv_reserve_visit_time);
        LinearLayout layoutReserveTime = view.findViewById(R.id.layout_reserve_time);
        tvType.setText(String.format(getString(R.string.txt_space), bean.getName()));
        //已完成（已上传报告）
        if (bean.getStatus() == CHECK_TYPE_STATUS_COMPLETE) {
            reportList.add(bean);
        }
        //订单和服务项均为已取消状态
        if (checkDetailBean.getStatus() != CHECK_ORDER_STATUS_CANCEL && bean.getStatus() == CHECK_TYPE_STATUS_CANCEL) {
            tvType.append(appendImage(bean.getStatus(), bean.getName()));
            tvType.setSelected(true);
            imageView.setSelected(true);
        } else {
            tvType.setSelected(false);
            imageView.setSelected(false);
        }
        if (showAdvice(bean)) {
            layoutReserveTime.setVisibility(View.VISIBLE);
            reserveTime.setText(bean.getOrderAt());
            editAdvice.setVisibility(View.VISIBLE);
            editAdvice.setOnClickListener(v -> {
                Intent intent = new Intent(ReservationServiceDetailActivity.this,
                        AddDiagnosisActivity.class);
                intent.putExtra(CommonData.KEY_PUBLIC_STRING, bean.getName());
                intent.putExtra(CommonData.KEY_ORDER_ID, orderNo);
                intent.putExtra(CommonData.KEY_CHECK_TYPE_ID, bean.getId());
                startActivityForResult(intent, REQUEST_CODE_EDIT_ADVICE);
            });
        } else {
            editAdvice.setVisibility(View.GONE);
            layoutReserveTime.setVisibility(View.GONE);
        }
    }

    /**
     * 服务包下的服务项
     */
    private View servicePackage(CheckTypeByDetailBean childBean, View childView) {
        //服务名称
        TextView tvType = childView.findViewById(R.id.tv_check_type);
        //填写诊断意见
        TextView editAdvice = childView.findViewById(R.id.tv_check_edit);
        //预约就诊时间
        TextView reserveTime = childView.findViewById(R.id.tv_reserve_visit_time);
        LinearLayout layoutReserveTime = childView.findViewById(R.id.layout_reserve_time);
        tvType.setText(String.format(getString(R.string.txt_space), childBean.getName()));
        //已完成（已上传报告）
        if (childBean.getStatus() == CHECK_TYPE_STATUS_COMPLETE) {
            reportList.add(childBean);
        }
        //订单和服务项均为已取消状态
        if (checkDetailBean.getStatus() != CHECK_ORDER_STATUS_CANCEL &&
                childBean.getStatus() == CHECK_TYPE_STATUS_CANCEL) {
            tvType.append(appendImage(childBean.getStatus(), childBean.getName()));
            tvType.setSelected(true);
        } else {
            tvType.setSelected(false);
        }
        if (showAdvice(childBean)) {
            layoutReserveTime.setVisibility(View.VISIBLE);
            reserveTime.setText(childBean.getOrderAt());
            editAdvice.setVisibility(View.VISIBLE);
            editAdvice.setOnClickListener(v -> {
                Intent intent = new Intent(ReservationServiceDetailActivity.this,
                        AddDiagnosisActivity.class);
                intent.putExtra(CommonData.KEY_PUBLIC_STRING, childBean.getName());
                intent.putExtra(CommonData.KEY_ORDER_ID, orderNo);
                intent.putExtra(CommonData.KEY_CHECK_TYPE_ID, childBean.getId());
                startActivityForResult(intent, REQUEST_CODE_EDIT_ADVICE);
            });
        } else {
            editAdvice.setVisibility(View.GONE);
            layoutReserveTime.setVisibility(View.GONE);
        }
        return childView;
    }

    /**
     * 判断是否显示 填写诊断意见及预约就诊时间
     * 该服务回执方为医生&& 该服务的开单医生是当前账号医生 && 服务项状态为【待上传报告】
     */
    private boolean showAdvice(CheckTypeByDetailBean bean) {
        return TextUtils.equals(checkDetailBean.getDoctorCode(), loginBean.getDoctorCode()) &&
                bean.getStatus() == CHECK_TYPE_STATUS_PAID && bean.getSuggestionType() == SUGGESTION_TYPE_DOCTOR;
    }

    /**
     * 添加检查报告
     */
    private void addCheckReport() {
        layoutCheckReport.removeAllViews();
        for (int i = 0; i < newReportUrls.size(); i++) {
            CheckTypeByDetailBean bean = newReportUrls.get(i);
            View view = getLayoutInflater().inflate(R.layout.item_check_report, null);
            TextView textView = view.findViewById(R.id.tv_check_report_name);
            textView.setText(bean.getName());
            view.setTag(i);
            view.setOnClickListener(v -> {
                if (bean.getSuggestionType() == SUGGESTION_TYPE_DOCTOR) {
                    Intent intent = new Intent(ReservationServiceDetailActivity.this,
                            DiagnosisDetailActivity.class);
                    intent.putExtra(CommonData.KEY_CHECK_REPORT_LIST,
                            getDoctorReportList(bean.getId()));
                    intent.putExtra(CommonData.KEY_PUBLIC, curPosition);
                    startActivity(intent);
                } else {
                    FileDisplayActivity.show(this, getOtherReportList(), (Integer) v.getTag());
                }
            });
            layoutCheckReport.addView(view);
        }
    }

    /**
     * 一个检查项可能存在多个报告  拆分报告
     */
    private void splitReportUrl() {
        newReportUrls = new ArrayList<>();
        for (int i = 0; i < reportList.size(); i++) {
            CheckTypeByDetailBean bean = reportList.get(i);
            String reportUrl = bean.getReport();
            //医生回执的不拆分
            if (bean.getSuggestionType() == SUGGESTION_TYPE_DOCTOR) {
                String name = bean.getName();
                bean.setName(String.format(getString(R.string.txt_report_name), name));
                bean.setReport(reportUrl);
                newReportUrls.add(bean);
                continue;
            }
            String[] reportUrls = reportUrl.split(";");
            if (reportUrls.length > 1) {
                for (int j = 0; j < reportUrls.length; j++) {
                    CheckTypeByDetailBean checkTypeByDetailBean = new CheckTypeByDetailBean();
                    String name = bean.getName();
                    checkTypeByDetailBean.setName(
                            String.format(getString(R.string.txt_report_name_by_num), name,
                                    (j + 1)));
                    checkTypeByDetailBean.setReport(reportUrls[j]);
                    checkTypeByDetailBean.setSuggestionType(bean.getSuggestionType());
                    checkTypeByDetailBean.setId(bean.getId());
                    newReportUrls.add(checkTypeByDetailBean);
                }
            } else {
                String name = bean.getName();
                bean.setName(String.format(getString(R.string.txt_report_name), name));
                bean.setReport(reportUrls[0]);
                newReportUrls.add(bean);
            }
        }
    }

    int curPosition = -1;

    /**
     * 获取医生诊断意见数据
     */
    private ArrayList<CheckTypeByDetailBean> getDoctorReportList(int id) {
        ArrayList<CheckTypeByDetailBean> newData = new ArrayList<>();
        for (int i = 0; i < reportList.size(); i++) {
            CheckTypeByDetailBean bean = reportList.get(i);
            if (bean.getSuggestionType() == SUGGESTION_TYPE_DOCTOR) {
                newData.add(bean);
                //当前点击的position
                if (bean.getId() == id) {
                    curPosition = newData.size() - 1;
                }
            }
        }
        return newData;
    }

    /**
     * 获取其他报告数据
     */
    private ArrayList<CheckTypeByDetailBean> getOtherReportList() {
        ArrayList<CheckTypeByDetailBean> newData = new ArrayList<>();
        for (CheckTypeByDetailBean bean : newReportUrls) {
            if (bean.getSuggestionType() != SUGGESTION_TYPE_DOCTOR) {
                newData.add(bean);
            }
        }
        return newData;
    }

    private SpannableString appendImage(int status, String showText) {
        CenterImageSpan imgSpan;
        switch (status) {
            case CHECK_TYPE_STATUS_WAIT_PAY:
                imgSpan = new CenterImageSpan(this, bitmapNoReach);
                break;
            case CHECK_TYPE_STATUS_COMPLETE:
                imgSpan = new CenterImageSpan(this, bitmapReach);
                break;
            case CHECK_TYPE_STATUS_CANCEL:
                imgSpan = new CenterImageSpan(this, bitmapCancel);
                break;
            default:
                imgSpan = new CenterImageSpan(this, bitmapCancel);
                break;
        }
        SpannableString spanString = new SpannableString(showText);
        spanString.setSpan(imgSpan, 0, showText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanString;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_EDIT_ADVICE) {
            getReserveCheckOrderDetail();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bitmapReach != null) {
            bitmapReach.recycle();
            bitmapReach = null;
        }
        if (bitmapNoReach != null) {
            bitmapNoReach.recycle();
            bitmapNoReach = null;
        }
        if (bitmapCancel != null) {
            bitmapCancel.recycle();
            bitmapCancel = null;
        }
    }
}
