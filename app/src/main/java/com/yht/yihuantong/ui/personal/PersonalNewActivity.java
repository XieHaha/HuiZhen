package com.yht.yihuantong.ui.personal;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.utils.glide.GlideHelper;
import com.yht.frame.widgets.dialog.HintDialog;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ui.hospital.CooperateHospitalListActivity;
import com.yht.yihuantong.utils.FileUrlUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/7/15 16:33
 * @description
 */
public class PersonalNewActivity extends BaseActivity {
    @BindView(R.id.iv_head_img)
    ImageView ivHeadImg;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_depart)
    TextView tvDepart;
    @BindView(R.id.tv_hospital)
    TextView tvHospital;

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_personal_new;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        tvName.setText(loginBean.getDoctorName());
        tvDepart.setText(loginBean.getDepartmentName());
        tvHospital.setText(loginBean.getHospitalName());
        Glide.with(this)
             .load(FileUrlUtil.addTokenToUrl(loginBean.getPhoto()))
             .apply(GlideHelper.getOptions(BaseUtils.dp2px(this, 4)))
             .into(ivHeadImg);
    }

    @OnClick({ R.id.layout_personal_info, R.id.layout_personal_cooperate_hospital, R.id.layout_setting, R.id.tv_exit })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_personal_info:
                startActivity(new Intent(this, PersonalInfoActivity.class));
                break;
            case R.id.layout_personal_cooperate_hospital:
                startActivity(new Intent(this, CooperateHospitalListActivity.class));
                break;
            case R.id.layout_setting:
                startActivity(new Intent(this, SettingActivity.class));
                break;
            case R.id.tv_exit:
                new HintDialog(this).setTitleString(getString(R.string.txt_app_name))
                                    .setContentString(getString(R.string.txt_set_exit_sure))
                                    .setEnterBtnTxt(getString(R.string.txt_exit))
                                    .setOnEnterClickListener(() -> exit())
                                    .show();
                break;
            default:
                break;
        }
    }
}
