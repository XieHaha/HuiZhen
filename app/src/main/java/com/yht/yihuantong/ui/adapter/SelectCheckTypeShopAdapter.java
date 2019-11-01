package com.yht.yihuantong.ui.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yht.frame.data.bean.SelectCheckTypeParentBean;
import com.yht.frame.widgets.recyclerview.FullListView;
import com.yht.yihuantong.R;

import java.util.List;

/**
 * @author 顿顿
 * @date 19/6/5 14:25
 * @des 检查项目
 */
public class SelectCheckTypeShopAdapter extends BaseQuickAdapter<SelectCheckTypeParentBean, BaseViewHolder> {
    private ShopCheckTypeAdapter.OnServiceDeleteListener listener;

    public SelectCheckTypeShopAdapter(int layoutResId, @Nullable List<SelectCheckTypeParentBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SelectCheckTypeParentBean item) {
        helper.setText(R.id.tv_hospital_name, item.getHospitalName());
        FullListView listView = helper.getView(R.id.full_list_view);
        ShopCheckTypeAdapter adapter = new ShopCheckTypeAdapter(mContext);
        adapter.setOnServiceDeleteListener(listener);
        adapter.setList(item.getProductPackageList());
        listView.setAdapter(adapter);
    }

    public void setOnServiceDeleteListener(ShopCheckTypeAdapter.OnServiceDeleteListener listener) {
        this.listener = listener;
    }
}
