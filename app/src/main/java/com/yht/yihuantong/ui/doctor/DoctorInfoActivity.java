package com.yht.yihuantong.ui.doctor;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.CommonData;
import com.yht.frame.data.Tasks;
import com.yht.frame.data.bean.DoctorBean;
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
    private DoctorBean doctorBean;
    /**
     * 扫码结果
     */
    private String qrId;

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
            qrId = getIntent().getStringExtra(CommonData.KEY_DOCTOR_ID);
        }
        getDoctorByQrId();
    }

    /**
     * 扫码获取医生信息
     */
    private void getDoctorByQrId() {
        RequestUtils.getDoctorByQrId(this, loginBean.getToken(), qrId, this);
    }

    /**
     * 扫码获取医生信息
     */
    private void addDoctorFriend() {
        RequestUtils.addDoctorFriend(this, loginBean.getToken(), doctorBean.getDoctorCode(), this);
    }

    private void bindData() {
        Glide.with(this)
             .load(doctorBean.getPhoto())
             .apply(GlideHelper.getOptions(BaseUtils.dp2px(this, 4)))
             .into(ivHeadImg);
        tvName.setText(doctorBean.getDoctorName());
        tvTitle.setText(doctorBean.getJobTitle());
        tvDepart.setText(doctorBean.getDepartmentName());
        tvHospital.setText(doctorBean.getHospitalName());
        //        tvIntroduction.setText(doctorBean.get);
    }

    @OnClick(R.id.tv_next)
    public void onViewClicked() {
        if (doctorBean != null) { addDoctorFriend(); }
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        switch (task) {
            case GET_DOCTOR_BY_QR_ID:
                doctorBean = (DoctorBean)response.getData();
                bindData();
                break;
            case ADD_DOCTOR_FRIEND:
                ToastUtil.toast(this, response.getMsg());
                finish();
                break;
            default:
                break;
        }
    }
}
