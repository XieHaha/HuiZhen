package com.yht.yihuantong.ui.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yht.frame.data.bean.DoctorInfoBean;
import com.yht.yihuantong.R;

import java.util.List;

/**
 * @author 顿顿
 * @date 19/6/5 14:25
 * @des 医生适配器
 */
public class DoctorAdapter extends BaseQuickAdapter<DoctorInfoBean, BaseViewHolder> {
    public DoctorAdapter(int layoutResId, @Nullable List<DoctorInfoBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DoctorInfoBean item) {
        helper.setText(R.id.tv_receiving_doctor_name, item.getDoctorName())
              .setText(R.id.tv_receiving_doctor_title, item.getJobTitle())
              .setText(R.id.tv_receiving_doctor_hospital_depart,
                       item.getHospitalName() + "  " + item.getDepartmentName())
              .addOnClickListener(R.id.iv_patient_call);
    }
}
