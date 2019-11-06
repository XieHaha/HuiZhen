package com.yht.yihuantong.ui.hospital;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.TextView;

import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.CommonData;
import com.yht.frame.data.Tasks;
import com.yht.frame.data.bean.HospitalProductBean;
import com.yht.frame.data.bean.ProductBean;
import com.yht.frame.http.retrofit.RequestUtils;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.widgets.textview.JustifiedTextView;
import com.yht.yihuantong.R;

import butterknife.BindView;

/**
 * @author 顿顿
 * @date 19/8/15 14:05
 * @des 医院服务项详情
 */
public class ServiceDetailActivity extends BaseActivity {
    @BindView(R.id.tv_project_introduction)
    JustifiedTextView tvProjectIntroduction;
    @BindView(R.id.tv_project_notice)
    JustifiedTextView tvProjectNotice;
    @BindView(R.id.tv_project_name)
    TextView tvProjectName;
    private String code;
    private HospitalProductBean hospitalProductBean;

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_product_detail;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        if (getIntent() != null) {
            code = getIntent().getStringExtra(CommonData.KEY_ORDER_ID);
            hospitalProductBean = (HospitalProductBean)getIntent().getSerializableExtra(
                    CommonData.KEY_HOSPITAL_PRODUCT_BEAN);
        }
        if (hospitalProductBean == null) {
            getProjectDetail();
        }
        else {
            tvProjectName.setText(hospitalProductBean.getName());
            tvProjectIntroduction.setText(TextUtils.isEmpty(hospitalProductBean.getDescription()) ? getString(
                    R.string.txt_project_introduction_hint) : hospitalProductBean.getDescription());
            tvProjectNotice.setText(TextUtils.isEmpty(hospitalProductBean.getNotice())
                                    ? getString(R.string.txt_project_notice_hint)
                                    : hospitalProductBean.getNotice());
        }
    }

    /**
     * 详情
     */
    private void getProjectDetail() {
        RequestUtils.queryProductDetail(this, loginBean.getToken(), code, this);
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        if (task == Tasks.GET_COOPERATE_HOSPITAL_PROJECT_DETAIL) {
            ProductBean productBean = (ProductBean)response.getData();
            tvProjectName.setText(productBean.getProductName());
            tvProjectIntroduction.setText(TextUtils.isEmpty(productBean.getDescription())
                                          ? getString(R.string.txt_project_introduction_hint)
                                          : productBean.getDescription());
            tvProjectNotice.setText(TextUtils.isEmpty(productBean.getNotice())
                                    ? getString(R.string.txt_project_notice_hint)
                                    : productBean.getNotice());
        }
    }
}
