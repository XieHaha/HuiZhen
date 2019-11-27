package com.yht.yihuantong.ui.adapter;

import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yht.frame.data.bean.SelectCheckTypeBean;
import com.yht.frame.utils.BaseUtils;
import com.yht.yihuantong.R;

import java.util.List;

/**
 * @author 顿顿
 * @date 19/8/12 17:56
 * @description 服务项  服务包
 */
public class SelectCheckTypeShopAdapter extends BaseQuickAdapter<SelectCheckTypeBean,
        BaseViewHolder> {

    public SelectCheckTypeShopAdapter(int layoutResId, @Nullable List<SelectCheckTypeBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SelectCheckTypeBean item) {
        int position = helper.getAdapterPosition();
        SelectCheckTypeBean lastBean, nextBean = null;
        if (mData.size() - 1 > position) {
            nextBean = mData.get(position + 1);
        }
        if (position == 0) {
            helper.setVisible(R.id.layout_hospital_title, true).setText(R.id.tv_hospital_name,
                    item.getHospitalName()).setVisible(R.id.view_line1, false);
        } else {
            lastBean = mData.get(position - 1);
            if (TextUtils.equals(lastBean.getHospitalCode(), item.getHospitalCode())) {
                helper.setGone(R.id.layout_hospital_title, false);
            } else {
                helper.setVisible(R.id.layout_hospital_title, true).setText(R.id.tv_hospital_name
                        , item.getHospitalName()).setVisible(R.id.view_line1, true);
            }
        }
        boolean end = mData.size() - 1 == position || nextBean == null ||
                !TextUtils.equals(nextBean.getHospitalCode(), item.getHospitalCode());
        if (end) {
            helper.setVisible(R.id.view_line, false);
        } else {
            helper.setVisible(R.id.view_line, true);
        }
        helper.setText(R.id.tv_check_type_name, item.getProjectName())
                .setText(R.id.tv_price, String.format(mContext.getString(R.string.txt_price),
                        BaseUtils.getPrice(item.getPrice()))).addOnClickListener(R.id.iv_delete);
    }

}
