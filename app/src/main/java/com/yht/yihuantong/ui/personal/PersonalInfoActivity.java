package com.yht.yihuantong.ui.personal;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yht.frame.data.BaseData;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.utils.BaseUtils;
import com.yht.yihuantong.R;
import com.yht.frame.utils.glide.GlideHelper;
import com.yht.yihuantong.utils.ImageUrlUtil;

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

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        tvInfoName.setText(loginBean.getDoctorName());
        tvInfoSex.setText(
                loginBean.getSex() == BaseData.BASE_MALE ? getString(R.string.txt_sex_male) : getString(R.string.txt_sex_female));
        tvInfoPhone.setText(loginBean.getMobile());
        tvInfoTitle.setText(loginBean.getJobTitle());
        tvInfoDepart.setText(loginBean.getDepartmentName());
        tvInfoHospital.setText(loginBean.getHospitalName());
        Glide.with(this)
             .load(ImageUrlUtil.append(loginBean.getPhoto()))
             .apply(GlideHelper.getOptions(BaseUtils.dp2px(this, 4)))
             .into(ivInfoImg);
    }
}
