package com.yht.yihuantong.ui.personal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yht.frame.data.BaseData;
import com.yht.frame.data.CommonData;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.utils.glide.GlideHelper;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ui.auth.AddInfoActivity;
import com.yht.yihuantong.utils.FileUrlUtil;

import butterknife.BindView;
import butterknife.OnClick;

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
    @BindView(R.id.tv_introduction)
    TextView tvIntroduction;
    /**
     * 编辑个人简介
     */
    public static final int REQUEST_CODE_INTRODUCTION = 100;

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
        bindData();
    }

    /**
     * 数据初始化
     */
    private void bindData() {
        tvInfoName.setText(loginBean.getDoctorName());
        tvInfoSex.setText(loginBean.getSex() == BaseData.BASE_MALE
                          ? getString(R.string.txt_sex_male)
                          : getString(R.string.txt_sex_female));
        tvInfoPhone.setText(BaseUtils.asteriskUserPhone(loginBean.getMobile()));
        tvInfoTitle.setText(loginBean.getJobTitle());
        tvInfoDepart.setText(loginBean.getDepartmentName());
        tvInfoHospital.setText(loginBean.getHospitalName());
        if (!TextUtils.isEmpty(loginBean.getIntroduce())) {
            tvIntroduction.setHint("");
            tvIntroduction.setText(loginBean.getIntroduce());
        }
        Glide.with(this)
             .load(FileUrlUtil.addTokenToUrl(loginBean.getPhoto()))
             .apply(GlideHelper.getOptions(BaseUtils.dp2px(this, 4)))
             .into(ivInfoImg);
    }

    @OnClick(R.id.layout_introduction)
    public void onViewClicked() {
        Intent intent = new Intent(this, AddInfoActivity.class);
        intent.putExtra(CommonData.KEY_PUBLIC_STRING, loginBean.getIntroduce());
        intent.putExtra(CommonData.KEY_INTENT_BOOLEAN, true);
        startActivityForResult(intent, REQUEST_CODE_INTRODUCTION);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_INTRODUCTION) {
            String introduce = data.getStringExtra(CommonData.KEY_PUBLIC_STRING);
            tvIntroduction.setText(introduce);
            if (!TextUtils.isEmpty(introduce)) {
                tvIntroduction.setHint("");
            }
            else {
                tvIntroduction.setHint(R.string.txt_introduction_hint);
            }
            loginBean = getLoginBean();
        }
    }
}
