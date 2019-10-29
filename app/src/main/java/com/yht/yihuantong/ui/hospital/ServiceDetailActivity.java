package com.yht.yihuantong.ui.hospital;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.CommonData;
import com.yht.frame.data.Tasks;
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
    private String projectCode;

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
            projectCode = getIntent().getStringExtra(CommonData.KEY_PUBLIC_STRING);
        }
        getProjectDetail();
    }

    /**
     * 详情
     */
    private void getProjectDetail() {
        RequestUtils.queryProductDetail(this, loginBean.getToken(), projectCode, this);
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        if (task == Tasks.GET_COOPERATE_HOSPITAL_PROJECT_DETAIL) {
        }
    }
}
