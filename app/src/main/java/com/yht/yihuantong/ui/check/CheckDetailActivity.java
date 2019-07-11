package com.yht.yihuantong.ui.check;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yht.frame.api.DirHelper;
import com.yht.frame.data.CommonData;
import com.yht.frame.ui.BaseActivity;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ui.x5.FileDisplayActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/6/14 10:56
 * @des 预约检查详情
 */
public class CheckDetailActivity extends BaseActivity {
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
        if (isShowBottom) {
            layoutBottom.setVisibility(View.VISIBLE);
            layoutTop.setVisibility(View.GONE);
        }
        else {
            layoutBottom.setVisibility(View.GONE);
            layoutContact.setVisibility(View.GONE);
            layoutTop.setVisibility(View.VISIBLE);
        }
        initCheckType();
        initCheckReport();
    }

    /**
     * 检查项目
     */
    private void initCheckType() {
        for (int i = 0; i < 3; i++) {
            View view = getLayoutInflater().inflate(R.layout.item_check_type, null);
            TextView textView = view.findViewById(R.id.tv_check_type_name);
            ImageView imageDot = view.findViewById(R.id.iv_check_type_dot);
            ImageView imageView = view.findViewById(R.id.iv_check_type_status);
            textView.setText("测试");
            if (i == 1) {
                imageView.setVisibility(View.VISIBLE);
                textView.setSelected(true);
                imageDot.setSelected(true);
            }
            else {
                imageView.setVisibility(View.GONE);
                textView.setSelected(false);
                imageDot.setSelected(false);
            }
            layoutCheckType.addView(view);
        }
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
        int tag = (int)v.getTag();
        switch (tag) {
            case 0:
                FileDisplayActivity.show(this, DirHelper.getPathFile() + "/appstore.txt", "TXT文件加载");
                break;
            case 1:
                FileDisplayActivity.show(this, DirHelper.getPathFile() + "/Android.pdf", "PDF文件加载");
                break;
            case 2:
                FileDisplayActivity.show(this, DirHelper.getPathFile() + "/word.docx", "WORD文件加载");
                break;
            default:
                break;
        }
    }
}
