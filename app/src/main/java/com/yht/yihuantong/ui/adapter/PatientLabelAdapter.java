package com.yht.yihuantong.ui.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yht.frame.data.bean.LabelBean;
import com.yht.frame.data.bean.PatientBean;
import com.yht.yihuantong.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 顿顿
 * @date 19/6/5 14:25
 * @description 居民标签
 */
public class PatientLabelAdapter extends BaseQuickAdapter<LabelBean, BaseViewHolder> {
    public PatientLabelAdapter(int layoutResId, @Nullable List<LabelBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, LabelBean item) {
        helper.setText(R.id.tv_label_name,
                       String.format(mContext.getString(R.string.txt_label_title), item.getTagName(),
                                     item.getPatientNum()));
        ArrayList<PatientBean> list = item.getPatientList();
        if (list != null && list.size() > 0) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < list.size(); i++) {
                PatientBean bean = list.get(i);
                builder.append(bean.getName());
                if (list.size() - 1 > i) {
                    builder.append("，");
                }
            }
            helper.setText(R.id.tv_patient_name, builder.toString());
        }
        else {
            helper.setText(R.id.tv_patient_name, "");
        }
    }
}
