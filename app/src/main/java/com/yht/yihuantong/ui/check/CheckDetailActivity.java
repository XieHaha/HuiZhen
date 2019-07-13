package com.yht.yihuantong.ui.check;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yanzhenjie.nohttp.Headers;
import com.yanzhenjie.nohttp.download.DownloadListener;
import com.yht.frame.api.DirHelper;
import com.yht.frame.api.FileTransferServer;
import com.yht.frame.data.BaseData;
import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.CheckOrderStatus;
import com.yht.frame.data.CheckTypeStatus;
import com.yht.frame.data.CommonData;
import com.yht.frame.data.Tasks;
import com.yht.frame.data.base.CheckDetailBean;
import com.yht.frame.data.base.CheckTypeByDetailBean;
import com.yht.frame.http.retrofit.RequestUtils;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.utils.ToastUtil;
import com.yht.frame.utils.glide.GlideHelper;
import com.yht.frame.widgets.dialog.HintDialog;
import com.yht.frame.widgets.dialog.PercentDialog;
import com.yht.frame.widgets.view.CenterImageSpan;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ui.x5.FileDisplayActivity;
import com.yht.yihuantong.utils.FileUrlUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/6/14 10:56
 * @des 预约服务详情
 */
public class CheckDetailActivity extends BaseActivity implements CheckOrderStatus, CheckTypeStatus {
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
     * 下载进度
     */
    private PercentDialog percentDialog;
    /**
     * 文件总大小
     */
    private long fileSize;
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
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        initPercentView();
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

    /**
     * 检查项状态图
     */
    private void initBitmap() {
        bitmapCancel = BitmapFactory.decodeResource(getApplication().getResources(), R.mipmap.ic_tag_cancel);
        bitmapNoReach = BitmapFactory.decodeResource(getApplication().getResources(), R.mipmap.ic_tag_noreach);
        bitmapReach = BitmapFactory.decodeResource(getApplication().getResources(), R.mipmap.ic_tag_reach);
    }

    /**
     * 下载进度条
     */
    private void initPercentView() {
        percentDialog = new PercentDialog(this);
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
            case CHECK_ORDER_STATUS_INCOMPLETE:
                ivCheckStatus.setImageResource(R.mipmap.ic_check_incomplete);
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
                layoutCancelResult.setVisibility(View.VISIBLE);
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

    @OnClick(R.id.tv_check_next)
    public void onViewClicked() {
        new HintDialog(this).setPhone(checkDetailBean.getPatientMobile())
                            .setOnEnterClickListener(() -> callPhone(checkDetailBean.getPatientMobile()))
                            .show();
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        if (task == Tasks.GET_RESERVE_CHECK_ORDER_DETAIL) {
            checkDetailBean = (CheckDetailBean)response.getData();
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
                tvCheckReport.setText(
                        String.format(getString(R.string.txt_report_num), reportList.size(), checkTypeList.size()));
                //拆分报告
                splitReportUrl();
                //添加报告
                addCheckReport();
            }
            else {
                layoutCheckReportRoot.setVisibility(View.GONE);
            }
        }
        else {
            layoutCheckReportRoot.setVisibility(View.GONE);
        }
    }

    /**
     * 添加检查项
     *
     * @param bean
     * @return
     */
    private View addCheckType(CheckTypeByDetailBean bean) {
        View view = getLayoutInflater().inflate(R.layout.item_check_by_detail, null);
        ImageView imageView = view.findViewById(R.id.iv_check_type_dot);
        imageView.setVisibility(View.VISIBLE);
        TextView tvType = view.findViewById(R.id.tv_check_type);
        tvType.setText(String.format(getString(R.string.txt_space), bean.getName()));
        //已完成（已上传报告）
        if (bean.getStatus() == CHECK_TYPE_STATUS_COMPLETE) {
            reportList.add(bean);
        }
        if (bean.getStatus() == CHECK_TYPE_STATUS_CANCEL) {
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
        return view;
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
            view.setOnClickListener(v -> downReportFile(bean.getReport(), (Integer)view.getTag()));
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
            String[] reportUrls = reportUrl.split(";");
            if (reportUrls.length > 1) {
                for (int j = 0; j < reportUrls.length; j++) {
                    CheckTypeByDetailBean checkTypeByDetailBean = new CheckTypeByDetailBean();
                    String name = bean.getName();
                    checkTypeByDetailBean.setName(
                            String.format(getString(R.string.txt_report_name_by_num), name, (j + 1)));
                    checkTypeByDetailBean.setReport(reportUrls[j]);
                    newReportUrls.add(checkTypeByDetailBean);
                }
            }
            else {
                String name = bean.getName();
                bean.setName(String.format(getString(R.string.txt_report_name), name));
                bean.setReport(reportUrls[0]);
                newReportUrls.add(bean);
            }
        }
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

    private void downReportFile(String url, int position) {
        String fileName = url.substring(url.lastIndexOf("/") + 1);
        String filePath = DirHelper.getPathFile() + "/" + fileName;
        File file = new File(filePath);
        if (file != null && file.exists()) {
            FileDisplayActivity.show(CheckDetailActivity.this, newReportUrls, position);
        }
        else {
            FileTransferServer.getInstance(this)
                              .downloadFile(1, loginBean.getToken(), url, DirHelper.getPathFile(), fileName,
                                            new DownloadListener() {
                                                @Override
                                                public void onDownloadError(int what, Exception exception) {
                                                    ToastUtil.toast(CheckDetailActivity.this, "download error");
                                                }

                                                @Override
                                                public void onStart(int what, boolean isResume, long rangeSize,
                                                        Headers responseHeaders, long allCount) {
                                                    fileSize = allCount;
                                                    percentDialog.show();
                                                    //show dialog
                                                }

                                                @Override
                                                public void onProgress(int what, int progress, long fileCount,
                                                        long speed) {
                                                    percentDialog.setProgressValue(fileSize, fileCount);
                                                }

                                                @Override
                                                public void onFinish(int what, String filePath) {
                                                    //跳转webview
                                                    percentDialog.dismiss();
                                                    FileDisplayActivity.show(CheckDetailActivity.this, newReportUrls,
                                                                             position);
                                                }

                                                @Override
                                                public void onCancel(int what) {
                                                }
                                            });
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
