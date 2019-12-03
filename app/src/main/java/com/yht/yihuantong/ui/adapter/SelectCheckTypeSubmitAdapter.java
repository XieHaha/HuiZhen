package com.yht.yihuantong.ui.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yht.frame.data.bean.SelectCheckTypeBean;
import com.yht.frame.data.bean.SelectCheckTypeChildBean;
import com.yht.yihuantong.R;

import java.util.List;

/**
 * @author 顿顿
 * @date 19/8/12 17:56
 * @description 服务项  服务包
 */
public class SelectCheckTypeSubmitAdapter extends BaseQuickAdapter<SelectCheckTypeBean,
        BaseViewHolder> {

    public SelectCheckTypeSubmitAdapter(int layoutResId, @Nullable List<SelectCheckTypeBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SelectCheckTypeBean item) {
        int position = helper.getAdapterPosition();
        SelectCheckTypeBean lastBean, nextBean = null;
        if (mData.size() - 1 > position) {
            nextBean = mData.get(position + 1);
        }
        //处理医院显示问题
        if (position == 0) {
            helper.setVisible(R.id.layout_hospital_title, true).setText(R.id.tv_hospital_name,
                    item.getHospitalName()).setGone(R.id.view_line, false);
        } else {
            lastBean = mData.get(position - 1);
            if (TextUtils.equals(lastBean.getHospitalCode(), item.getHospitalCode())) {
                helper.setGone(R.id.layout_hospital_title, false);
            } else {
                helper.setVisible(R.id.layout_hospital_title, true).setText(R.id.tv_hospital_name
                        , item.getHospitalName()).setVisible(R.id.view_line, true);
            }
        }
        helper.setText(R.id.tv_check_type_name, item.getProjectName());

        LinearLayout layoutCheck = helper.getView(R.id.layout_check);
        List<SelectCheckTypeChildBean> childBeans = item.getProductInfoList();
        layoutCheck.removeAllViews();
        if (childBeans != null && childBeans.size() > 0) {
            layoutCheck.setVisibility(View.VISIBLE);
            for (SelectCheckTypeChildBean childBean : childBeans) {
                addServiceType(layoutCheck, childBean);
            }
        } else {
            layoutCheck.setVisibility(View.GONE);
        }
    }

    private void addServiceType(LinearLayout layoutCheck, SelectCheckTypeChildBean childBean) {
        View convertView = LayoutInflater.from(mContext).inflate(R.layout.item_child_service, null);
        TextView tvContent = convertView.findViewById(R.id.tv_content);
        TextView tvNum = convertView.findViewById(R.id.tv_num);
        tvContent.setText(childBean.getProductName());
        tvNum.setText(String.format(mContext.getString(R.string.txt_amount),
                childBean.getProductCount()));
        layoutCheck.addView(convertView);
    }
}
