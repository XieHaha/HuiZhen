package com.yht.yihuantong.ui.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yht.frame.data.bean.SelectCheckTypeBean;
import com.yht.frame.utils.BaseUtils;
import com.yht.yihuantong.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 顿顿
 * @date 19/6/5 14:25
 * @des 医院下检查项目 搜索适配器
 */
public class SelectCheckTypeByHospitalAdapter extends BaseQuickAdapter<SelectCheckTypeBean, BaseViewHolder> {
    private List<Integer> selectPosition = new ArrayList<>();

    public void setSelectPosition(List<Integer> selectPosition) {
        this.selectPosition = selectPosition;
    }

    public SelectCheckTypeByHospitalAdapter(int layoutResId, @Nullable List<SelectCheckTypeBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SelectCheckTypeBean item) {
        ImageView imageView = helper.getView(R.id.iv_select);
        Integer curPosition = helper.getLayoutPosition();
        if (selectPosition.contains(curPosition)) {
            imageView.setSelected(true);
        }
        else {
            imageView.setSelected(false);
        }
        helper.setText(R.id.rb_check_type, item.getProjectName());
        helper.setText(R.id.tv_check_price,
                       String.format(mContext.getString(R.string.txt_price), BaseUtils.getPrice(item.getPrice())));
    }
}
