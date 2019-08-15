package com.yht.yihuantong.ui.hospital;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yht.frame.ui.BaseActivity;
import com.yht.frame.widgets.recyclerview.FullListView;
import com.yht.frame.widgets.textview.JustifiedTextView;
import com.yht.yihuantong.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/8/15 14:05
 * @des 医院详情
 */
public class HospitalDetailActivity extends BaseActivity {
    @BindView(R.id.public_title_bar_title)
    TextView publicTitleBarTitle;
    @BindView(R.id.iv_status)
    ImageView ivStatus;
    @BindView(R.id.tv_hospital_address)
    TextView tvHospitalAddress;
    @BindView(R.id.tv_hospital_business)
    TextView tvHospitalBusiness;
    @BindView(R.id.tv_hospital_project)
    TextView tvHospitalProject;
    @BindView(R.id.full_list_view)
    FullListView fullListView;
    @BindView(R.id.tv_hospital_introduction)
    JustifiedTextView tvHospitalIntroduction;
    @BindView(R.id.tv_more)
    TextView tvMore;

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_hospital_detail;
    }

    @OnClick({ R.id.layout_more_project, R.id.tv_more })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_more_project:
                Intent intent = new Intent(this, HospitalProjectListActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_more:
                break;
            default:
                break;
        }
    }
}
