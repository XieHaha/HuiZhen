package com.yht.yihuantong.ui.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yht.yihuantong.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 顿顿
 * @date 19/6/5 14:25
 * @des 医院下检查项目 搜索适配器
 */
public class CheckTypeByHospitalSelectAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    private List<Integer> selectPosition = new ArrayList<>();

    public void setSelectPosition(List<Integer> selectPosition) {
        this.selectPosition = selectPosition;
    }

    public CheckTypeByHospitalSelectAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        ImageView imageView = helper.getView(R.id.iv_select);
        Integer curPosition = helper.getLayoutPosition();
        if (selectPosition.contains(curPosition)) {
            imageView.setSelected(true);
        }
        else {
            imageView.setSelected(false);
        }
        helper.setText(R.id.rb_check_type, item);
        //        helper.setText(R.id.tv_check_price, item);
    }
}
