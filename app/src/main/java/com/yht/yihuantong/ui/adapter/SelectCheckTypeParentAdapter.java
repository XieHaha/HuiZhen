package com.yht.yihuantong.ui.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;

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
     * 已选择position
     */
    private Map<Integer, ArrayList<Integer>> positions = new HashMap<>();

    public SelectCheckTypeParentAdapter(int layoutResId, @Nullable List<SelectCheckTypeParentBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SelectCheckTypeParentBean item) {
        helper.setText(R.id.tv_hospital_name, item.getHospitalName());
        FullListView listView = helper.getView(R.id.full_list_view);
        SelectCheckTypeAdapter selectCheckTypeAdapter = new SelectCheckTypeAdapter(mContext);
        selectCheckTypeAdapter.setList(item.getProductPackageList());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int parentPosition = helper.getAdapterPosition();
                ArrayList<Integer> childPositions;
                if (positions.containsKey(parentPosition)) {
                    childPositions = positions.get(parentPosition);
                    if (childPositions.contains(position)) {
                        childPositions.remove(Integer.valueOf(position));
                        //如果已经没有已选中的服务项，需移除key
                        if (childPositions.size() == 0) {
                            positions.remove(parentPosition);
                        }
                    }
                    else {
                        childPositions.add(position);
                    }
                }
                else {
                    childPositions = new ArrayList<>();
                    childPositions.add(position);
                }
                positions.put(parentPosition, childPositions);
                selectCheckTypeAdapter.setSelectPositions(positions.get(parentPosition));
                if (onSelectedCallback != null) {
                    onSelectedCallback.onSelected(positions);
                }
            }
        });
        listView.setAdapter(selectCheckTypeAdapter);
    }

    public interface OnSelectedCallback {
        /**
         * 选择回调
         *
         * @param positions 已选择的坐标
         */
        void onSelected(Map<Integer, ArrayList<Integer>> positions);
    }

    private OnSelectedCallback onSelectedCallback;

    public void setOnSelectedCallback(OnSelectedCallback onSelectedCallback) {
        this.onSelectedCallback = onSelectedCallback;
    }
}
