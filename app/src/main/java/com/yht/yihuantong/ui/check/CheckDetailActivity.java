package com.yht.yihuantong.ui.check;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yht.frame.data.BaseData;
import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.CheckTypeStatus;
import com.yht.frame.data.CommonData;
import com.yht.frame.data.OrderStatus;
import com.yht.frame.data.Tasks;
import com.yht.frame.data.base.CheckDetailBean;
import com.yht.frame.data.base.CheckTypeByDetailBean;
import com.yht.frame.http.retrofit.RequestUtils;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.utils.glide.GlideHelper;
import com.yht.yihuantong.R;
import com.yht.yihuantong.utils.ImageUrlUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/6/14 10:56
 * @des 预约检查详情
 */
public class CheckDetailActivity extends BaseActivity implements OrderStatus, CheckTypeStatus {
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
    @BindView(R.id.tv_check_pregnancy)
    TextView tvCheckPregnancy;
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
    /**
     * 检查详情
     */
    private CheckDetailBean checkDetailBean;
    /**
     * 检查项
     */
    private ArrayList<CheckTypeByDetailBean> checkTypeList;
    private Bitmap bitmapCancel, bitmapNoreach, bitmapReach;
    /**
     * 订单
     */
    private String orderNo;
    /**
     * 头部底部判断
     */
    private boolean isShowBottom;

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_check_detail;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        if (getIntent() != null) {
            isShowBottom = getIntent().getBooleanExtra(CommonData.KEY_PUBLIC, false);
            orderNo = getIntent().getStringExtra(CommonData.KEY_ORDER_ID);
        }
        initBitmap();
        initBasePage();
        getReserveCheckOrderDetail();
    }

    private void initBitmap() {
        bitmapCancel = BitmapFactory.decodeResource(getApplication().getResources(), R.mipmap.ic_tag_cancel);
        bitmapNoreach = BitmapFactory.decodeResource(getApplication().getResources(), R.mipmap.ic_tag_noreach);
        bitmapReach = BitmapFactory.decodeResource(getApplication().getResources(), R.mipmap.ic_tag_reach);
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
        }
        else {
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
        Glide.with(this)
             .load(ImageUrlUtil.append(checkDetailBean.getPatientPhoto()))
             .apply(GlideHelper.getOptions(BaseUtils.dp2px(this, 4)))
             .into(ivPatientImg);
        tvPatientName.setText(checkDetailBean.getPatientName());
        tvPatientSex.setText(checkDetailBean.getSex() == BaseData.BASE_ONE
                             ? getString(R.string.txt_sex_male)
                             : getString(R.string.txt_sex_female));
        tvPatientAge.setText(String.valueOf(checkDetailBean.getPatientAge()));
        if (isShowBottom) {
            tvCheckDoctorBottom.setText(checkDetailBean.getDoctorName());
            tvCheckTimeBottom.setText(checkDetailBean.getCreateAt());
            tvCheckDiagnosisBottom.setText(checkDetailBean.getInitResult());
        }
        else {
            tvCheckDoctorTop.setText(checkDetailBean.getDoctorName());
            tvCheckTimeTop.setText(checkDetailBean.getCreateAt());
            tvCheckDiagnosisTop.setText(checkDetailBean.getInitResult());
        }
        int status = checkDetailBean.getStatus();
        switch (status) {
            case ORDER_STATUS_INCOMPLETE:
                ivCheckStatus.setImageResource(R.mipmap.ic_check_incomplete);
                layoutCancelResult.setVisibility(View.GONE);
                layoutCheckReportRoot.setVisibility(View.GONE);
                tvCheckStatus.setText(getString(R.string.txt_status_incomplete));
                break;
            case ORDER_STATUS_COMPLETE:
                ivCheckStatus.setImageResource(R.mipmap.ic_status_complete);
                layoutCheckReportRoot.setVisibility(View.VISIBLE);
                layoutCancelResult.setVisibility(View.GONE);
                tvCheckStatus.setText(getString(R.string.txt_status_complete));
                break;
            default:
                ivCheckStatus.setImageResource(R.mipmap.ic_status_cancel);
                layoutCancelResult.setVisibility(View.VISIBLE);
                layoutCheckReportRoot.setVisibility(View.VISIBLE);
                tvCheckStatus.setText(getString(R.string.txt_status_cancel));
                break;
        }
        tvCheckHospital.setText(checkDetailBean.getTargetHospitalName());
        //是否备孕
        tvCheckPregnancy.setText(checkDetailBean.getIsPregnancy() == BaseData.BASE_ZERO
                                 ? getString(R.string.txt_yes)
                                 : getString(R.string.txt_no));
        //缴费类型
        int payType = checkDetailBean.getPayType();
        if (payType == BaseData.BASE_ZERO) {
            tvCheckPayment.setText(getString(R.string.txt_self_pay));
        }
        else if (payType == BaseData.BASE_ONE) {
            tvCheckPayment.setText(getString(R.string.txt_medicare));
        }
        else {
            tvCheckPayment.setText(getString(R.string.txt_self_medicare));
        }
    }

    /**
     * 检查项目
     */
    private void initCheckType() {
        checkTypeList = checkDetailBean.getTrans();
        if (checkTypeList != null && checkTypeList.size() > 0) {
            for (int i = 0; i < checkTypeList.size(); i++) {
                CheckTypeByDetailBean bean = checkTypeList.get(i);
                layoutCheckType.addView(addCheckType(i, bean));
            }
        }
    }

    /**
     * 添加检查项
     *
     * @param i
     * @param bean
     * @return
     */
    private View addCheckType(int i, CheckTypeByDetailBean bean) {
        View view = getLayoutInflater().inflate(R.layout.item_check_by_detail, null);
        ImageView imageView = view.findViewById(R.id.iv_check_type_dot);
        imageView.setVisibility(View.VISIBLE);
        TextView tvType = view.findViewById(R.id.tv_check_type);
        tvType.setText(String.format(getString(R.string.txt_space), bean.getName()));
        if (bean.getStatus() == CHECK_STATUS_CANCEL) {
            tvType.append(appendImage(bean.getStatus(), bean.getName()));
            tvType.setSelected(true);
            imageView.setSelected(true);
        }
        else {
            tvType.setSelected(false);
            imageView.setSelected(false);
        }
        //隐藏价格
        view.findViewById(R.id.tv_check_price).setVisibility(View.GONE);
        view.setTag(i);
        view.setOnClickListener(this);
        return view;
    }

    /**
     * 检查报告
     */
    private void initCheckReport() {
        for (int i = 0; i < 3; i++) {
            View view = getLayoutInflater().inflate(R.layout.item_check_report, null);
            TextView textView = view.findViewById(R.id.tv_check_report_name);
            textView.setText("检查报告");
            view.setTag(i);
            view.setOnClickListener(this);
            layoutCheckReport.addView(view);
        }
    }

    @OnClick(R.id.tv_check_next)
    public void onViewClicked() {
        //TODO 联系患者
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        switch (task) {
            case GET_RESERVE_CHECK_ORDER_DETAIL:
                checkDetailBean = (CheckDetailBean)response.getData();
                initDetailData();
                break;
            default:
                break;
        }
    }

    private SpannableString appendImage(int status, String showText) {
        ImageSpan imgSpan;
        switch (status) {
            case CHECK_STATUS_WAIT_PAY:
                imgSpan = new ImageSpan(this, bitmapNoreach);
                break;
            case CHECK_STATUS_COMPLETE:
                imgSpan = new ImageSpan(this, bitmapReach);
                break;
            case CHECK_STATUS_CANCEL:
                imgSpan = new ImageSpan(this, bitmapCancel);
                break;
            default:
                imgSpan = new ImageSpan(this, bitmapCancel);
                break;
        }
        SpannableString spanString = new SpannableString(showText);
        spanString.setSpan(imgSpan, 0, showText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanString;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bitmapReach != null) {
            bitmapReach.recycle();
            bitmapReach = null;
        }
        if (bitmapNoreach != null) {
            bitmapNoreach.recycle();
            bitmapNoreach = null;
        }
        if (bitmapCancel != null) {
            bitmapCancel.recycle();
            bitmapCancel = null;
        }
    }
}
