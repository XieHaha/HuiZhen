package com.yht.yihuantong.ui.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yht.frame.data.bean.SelectCheckTypeParentBean;
import com.yht.frame.widgets.recyclerview.FullListView;
import com.yht.yihuantong.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 顿顿
 * @date 19/6/5 14:25
 * @des 检查项目
 */
public class SelectCheckTypeParentAdapter extends BaseQuickAdapter<SelectCheckTypeParentBean, BaseViewHolder> {
    /**
     * 已选择code
     */
    private ArrayList<String> selectCodes = new ArrayList<>();

    public SelectCheckTypeParentAdapter(int layoutResId, @Nullable List<SelectCheckTypeParentBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SelectCheckTypeParentBean item) {
        helper.setText(R.id.tv_hospital_name, item.getHospitalName());
        FullListView listView = helper.getView(R.id.full_list_view);
        SelectCheckTypeAdapter selectCheckTypeAdapter = new SelectCheckTypeAdapter(mContext);
        selectCheckTypeAdapter.setSelectCodes(selectCodes);
        selectCheckTypeAdapter.setList(item.getProductPackageList());
        listView.setOnItemClickListener((parent, view, position, id) -> {
            String newCode = item.getProductPackageList().get(position).getProjectCode();
            if (selectCodes.contains(newCode)) {
                selectCodes.remove(newCode);
            }
            else {
                selectCodes.add(newCode);
            }
            selectCheckTypeAdapter.setSelectCodes(selectCodes);
            selectCheckTypeAdapter.notifyDataSetChanged();
            if (onSelectedCallback != null) {
                onSelectedCallback.onSelected(selectCodes);
            }
        });
        listView.setAdapter(selectCheckTypeAdapter);
    }

    public void setSelectCodes(ArrayList<String> selectCodes) {
        this.selectCodes = selectCodes;
    }

    public interface OnSelectedCallback {
        /**
         * 选择回调
         *
         * @param data 已选择的code
         */
        void onSelected(ArrayList<String> data);
    }

    private OnSelectedCallback onSelectedCallback;

    public void setOnSelectedCallback(OnSelectedCallback onSelectedCallback) {
        this.onSelectedCallback = onSelectedCallback;
    }
}
