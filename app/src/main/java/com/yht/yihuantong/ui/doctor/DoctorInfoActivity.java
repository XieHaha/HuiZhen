package com.yht.yihuantong.ui.doctor;

import android.os.Bundle;
import androidx.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yht.frame.api.notify.NotifyChangeListenerManager;
import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.CommonData;
import com.yht.frame.data.Tasks;
import com.yht.frame.data.bean.DoctorQrCodeBean;
import com.yht.frame.http.retrofit.RequestUtils;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.utils.ToastUtil;
import com.yht.frame.utils.glide.GlideHelper;
import com.yht.frame.widgets.textview.JustifiedTextView;
import com.yht.yihuantong.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/8/14 15:52
 * @description
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
    @BindView(R.id.tv_more)
    TextView tvMore;
    private DoctorQrCodeBean doctorBean;
    /**
     * 最多显示行数
     */
    public static final int MAX_NUM = 5;

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_doctor_info;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        if (getIntent() != null) {
            doctorBean = (DoctorQrCodeBean)getIntent().getSerializableExtra(CommonData.KEY_DOCTOR_QR_CODE_BEAN);
        }
        if (doctorBean != null) { bindData(); }
    }

    /**
     * 扫码获取医生信息
     */
    private void addDoctorFriend() {
        RequestUtils.addDoctorFriend(this, loginBean.getToken(), doctorBean.getCode(), this);
    }

    private void bindData() {
        Glide.with(this)
             .load(doctorBean.getPhoto())
             .apply(GlideHelper.getOptions(BaseUtils.dp2px(this, 4)))
             .into(ivHeadImg);
        tvName.setText(doctorBean.getName());
        tvTitle.setText(doctorBean.getJobTitle());
        tvDepart.setText(doctorBean.getDepartmentName());
        tvHospital.setText(doctorBean.getHospitalName());
        tvIntroduction.setText(doctorBean.getIntroduce());
        tvIntroduction.post(() -> {
            if (tvIntroduction.getLineCount() > MAX_NUM) {
                tvMore.setVisibility(View.VISIBLE);
                initIntroduction(true);
            }
            else {
                tvMore.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 简介  展开or收起
     *
     * @param mode true为展开
     */
    private void initIntroduction(boolean mode) {
        if (mode) {
            tvMore.setSelected(false);
            tvMore.setText(R.string.txt_pack_down);
            tvIntroduction.setMaxLines(MAX_NUM);
        }
        else {
            tvMore.setSelected(true);
            tvMore.setText(R.string.txt_pack_up);
            tvIntroduction.setMaxLines(Integer.MAX_VALUE);
        }
    }

    @OnClick({ R.id.tv_next, R.id.tv_more })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_next:
                if (doctorBean != null) { addDoctorFriend(); }
                break;
            case R.id.tv_more:
                initIntroduction(tvMore.isSelected());
                break;
            default:
                break;
        }
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        if (task == Tasks.ADD_DOCTOR_FRIEND) {
            //医生添加成功后  刷新医生列表
            NotifyChangeListenerManager.getInstance().notifyDoctorStatusChange("");
            ToastUtil.toast(this, response.getMsg());
            finish();
        }
    }
}
