package com.yht.yihuantong.ui.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yht.frame.data.bean.SelectCheckTypeBean;
import com.yht.frame.data.bean.SelectCheckTypeChildBean;
import com.yht.frame.utils.BaseUtils;
import com.yht.yihuantong.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 顿顿
 * @date 19/8/12 17:56
 * @description 服务项  服务包
 */
public class SelectCheckTypeRvAdapter extends BaseQuickAdapter<SelectCheckTypeBean,
        BaseViewHolder> {

    private ArrayList<String> selectedCodes = new ArrayList<>();

    public void setSelectedCodes(ArrayList<String> selectedCodes) {
        this.selectedCodes = selectedCodes;
    }

    public SelectCheckTypeRvAdapter(int layoutResId, @Nullable List<SelectCheckTypeBean> data) {
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
                    item.getHospitalName());
        } else {
            lastBean = mData.get(position - 1);
            if (TextUtils.equals(lastBean.getHospitalCode(), item.getHospitalCode())) {
                helper.setGone(R.id.layout_hospital_title, false);
            } else {
                helper.setVisible(R.id.layout_hospital_title, true).setText(R.id.tv_hospital_name
                        , item.getHospitalName());
            }
        }
        //处理底部圆角问题
        boolean end = mData.size() - 1 == position || nextBean == null ||
                !TextUtils.equals(nextBean.getHospitalCode(), item.getHospitalCode());
        if (end) {
            helper.setVisible(R.id.view_line, false).setVisible(R.id.tv_space, true);
        } else {
            helper.setVisible(R.id.view_line, true).setVisible(R.id.tv_space, false);
        }
        //数据绑定
        helper.setText(R.id.tv_check_type_name, item.getProjectName())
                .setText(R.id.tv_price, String.format(mContext.getString(R.string.txt_price),
                        BaseUtils.getPrice(item.getPrice())));
        ImageView ivSelect = helper.getView(R.id.iv_select);
        if (selectedCodes.contains(item.getProjectCode())) {
            ivSelect.setSelected(true);
        } else {
            ivSelect.setSelected(false);
        }
        List<SelectCheckTypeChildBean> childBeans =
                item.getChildServiceTypes(item.getProjectCode());
        LinearLayout layoutCheck = helper.getView(R.id.layout_check);
        layoutCheck.removeAllViews();
        if (childBeans != null && childBeans.size() > 0) {
            for (SelectCheckTypeChildBean childBean : childBeans) {
                addServiceType(layoutCheck, childBean);
            }
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
