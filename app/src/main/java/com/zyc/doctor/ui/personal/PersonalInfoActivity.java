package com.zyc.doctor.ui.personal;

import android.widget.ImageView;
import android.widget.TextView;

import com.yht.frame.ui.BaseActivity;
import com.zyc.doctor.R;

import butterknife.BindView;

/**
 * @author 顿顿
 * @date 19/6/10 16:15
 * @des
 */
public class PersonalInfoActivity extends BaseActivity {
    @BindView(R.id.iv_info_img)
    ImageView ivInfoImg;
    @BindView(R.id.tv_info_name)
    TextView tvInfoName;
    @BindView(R.id.tv_info_sex)
    TextView tvInfoSex;
    @BindView(R.id.tv_info_phone)
    TextView tvInfoPhone;
    @BindView(R.id.tv_info_title)
    TextView tvInfoTitle;
    @BindView(R.id.tv_info_depart)
    TextView tvInfoDepart;
    @BindView(R.id.tv_info_hospital)
    TextView tvInfoHospital;

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_personal_info;
    }
}
