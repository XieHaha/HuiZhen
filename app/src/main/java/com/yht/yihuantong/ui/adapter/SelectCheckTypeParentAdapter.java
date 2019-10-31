package com.yht.yihuantong.ui.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yht.frame.data.bean.SelectCheckTypeParentBean;
import com.yht.frame.widgets.recyclerview.FullListView;
import com.yht.yihuantong.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 顿顿
 * @date 19/6/5 14:25
 * @des 检查项目
 */
public class SelectCheckTypeParentAdapter extends BaseQuickAdapter<SelectCheckTypeParentBean, BaseViewHolder> {
    /**
     * 已选择code
     */
    private Map<String, ArrayList<String>> listMap = new HashMap<>();

    public SelectCheckTypeParentAdapter(int layoutResId, @Nullable List<SelectCheckTypeParentBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SelectCheckTypeParentBean item) {
        helper.setText(R.id.tv_hospital_name, item.getHospitalName());
        FullListView listView = helper.getView(R.id.full_list_view);
        SelectCheckTypeAdapter selectCheckTypeAdapter = new SelectCheckTypeAdapter(mContext);
        selectCheckTypeAdapter.setList(item.getProductPackageList());
        selectCheckTypeAdapter.setSelectCodes(listMap.get(item.getHospitalCode()));
        listView.setOnItemClickListener((parent, view, position, id) -> {
            ArrayList<String> childCodes;
            String newCode = item.getProductPackageList().get(position).getProjectCode();
            if (listMap.containsKey(item.getHospitalCode())) {
                childCodes = listMap.get(item.getHospitalCode());
                if (childCodes.contains(newCode)) {
                    childCodes.remove(newCode);
                }
                else {
                    childCodes.add(newCode);
                }
            }
            else {
                childCodes = new ArrayList<>();
                childCodes.add(newCode);
            }
            //如果已经没有已选中的服务项，需移除key
            if (childCodes.size() == 0) {
                listMap.remove(item.getHospitalCode());
            }
            else {
                listMap.put(item.getHospitalCode(), childCodes);
            }
            selectCheckTypeAdapter.setSelectCodes(listMap.get(item.getHospitalCode()));
            if (onSelectedCallback != null) {
                onSelectedCallback.onSelected(listMap);
            }
        });
        listView.setAdapter(selectCheckTypeAdapter);
    }

    public interface OnSelectedCallback {
        /**
         * 选择回调
         *
         * @param data 已选择的code
         */
        void onSelected(Map<String, ArrayList<String>> data);
    }

    private OnSelectedCallback onSelectedCallback;

    public void setOnSelectedCallback(OnSelectedCallback onSelectedCallback) {
        this.onSelectedCallback = onSelectedCallback;
    }
}
