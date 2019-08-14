package com.yht.yihuantong.ui.doctor;

import android.widget.ImageView;
import android.widget.TextView;

import com.yht.frame.ui.BaseActivity;
import com.yht.frame.widgets.textview.JustifiedTextView;
import com.yht.yihuantong.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/8/14 15:52
 * @des
 */
public class DoctorInfoActivity extends BaseActivity {
    @BindView(R.id.public_title_bar_title)
    TextView publicTitleBarTitle;
    @BindView(R.id.iv_head_img)
    ImageView ivHeadImg;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_depart)
    TextView tvDepart;
    @BindView(R.id.tv_hospital)
    TextView tvHospital;
    @BindView(R.id.tv_introduction)
    JustifiedTextView tvIntroduction;

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_doctor_info;
    }

    @OnClick(R.id.tv_next)
    public void onViewClicked() {}
}
