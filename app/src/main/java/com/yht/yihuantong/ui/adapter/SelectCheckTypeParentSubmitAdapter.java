package com.yht.yihuantong.ui.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yht.frame.data.bean.SelectCheckTypeBean;
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
public class SelectCheckTypeParentSubmitAdapter extends BaseQuickAdapter<SelectCheckTypeParentBean, BaseViewHolder> {
    /**
     * 已选择code
     */
    private ArrayList<String> selectCodes = new ArrayList<>();

    public SelectCheckTypeParentSubmitAdapter(int layoutResId, @Nullable List<SelectCheckTypeParentBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SelectCheckTypeParentBean item) {
        helper.setText(R.id.tv_hospital_name, item.getHospitalName());
        FullListView listView = helper.getView(R.id.full_list_view);
        SelectCheckTypeSubmitAdapter adapter = new SelectCheckTypeSubmitAdapter(mContext);
        adapter.setList(item.getProductPackageList());
        listView.setOnItemClickListener((parent, view, position, id) -> {
            SelectCheckTypeBean bean = item.getProductPackageList().get(position);
            String newCode = bean.getProjectCode();
            if (selectCodes.contains(newCode)) {
                selectCodes.remove(newCode);
            }
            else {
                selectCodes.add(newCode);
            }
            if (onSelectedCallback != null) {
                onSelectedCallback.onSelectedParent(item, bean);
            }
            adapter.notifyDataSetChanged();
        });
        listView.setAdapter(adapter);
    }

    public interface OnSelectedCallback {
        /**
         * 主列表回调
         *
         * @param patentBean 医院
         * @param bean       医院下服务包或服务项
         */
        void onSelectedParent(SelectCheckTypeParentBean patentBean, SelectCheckTypeBean bean);
    }

    private OnSelectedCallback onSelectedCallback;

    public void setOnSelectedCallback(OnSelectedCallback onSelectedCallback) {
        this.onSelectedCallback = onSelectedCallback;
    }
}
