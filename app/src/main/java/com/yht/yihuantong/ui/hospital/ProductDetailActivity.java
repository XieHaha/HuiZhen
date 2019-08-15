package com.yht.yihuantong.ui.hospital;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.TextView;

import com.yht.frame.data.CommonData;
import com.yht.frame.data.bean.HospitalProductBean;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.widgets.textview.JustifiedTextView;
import com.yht.yihuantong.R;

import butterknife.BindView;

/**
 * @author 顿顿
 * @date 19/8/15 14:05
 * @des 医院服务项详情
 */
public class ProductDetailActivity extends BaseActivity {
    @BindView(R.id.public_title_bar_title)
    TextView publicTitleBarTitle;
    @BindView(R.id.tv_project_introduction)
    JustifiedTextView tvProjectIntroduction;
    @BindView(R.id.tv_project_notice)
    JustifiedTextView tvProjectNotice;

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
            HospitalProductBean bean = (HospitalProductBean)getIntent().getSerializableExtra(
                    CommonData.KEY_HOSPITAL_PRODUCT_BEAN);
            String name = getIntent().getStringExtra(CommonData.KEY_PUBLIC_STRING);
            publicTitleBarTitle.setText(name);
            if (bean != null) {
                if (!TextUtils.isEmpty(bean.getDescription())) {
                    tvProjectIntroduction.setText(bean.getDescription());
                }
                if (!TextUtils.isEmpty(bean.getNotice())) {
                    tvProjectNotice.setText(bean.getNotice());
                }
            }
        }
    }
}
