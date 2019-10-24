package com.yht.yihuantong.ui.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.yht.frame.data.BaseData;
import com.yht.frame.data.bean.RemoteDepartBean;
import com.yht.frame.data.bean.RemoteDepartTitleBean;
import com.yht.yihuantong.R;

import java.util.List;

/**
 * @author 顿顿
 * @date 19/6/5 14:25
 * @description 远程会诊 科室选择列表
 */
public class RemoteDepartAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder>
        implements BaseData {
    public RemoteDepartAdapter(List<MultiItemEntity> data) {
        super(data);
        addItemType(BASE_ZERO, R.layout.item_remote_depart_title);
        addItemType(BASE_ONE, R.layout.item_remote_depart);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultiItemEntity item) {
        switch (helper.getItemViewType()) {
            case BASE_ZERO:
                RemoteDepartTitleBean bean = (RemoteDepartTitleBean)item;
                if (bean.getSelectedNum() > 0) {
                    helper.setText(R.id.tv_title, bean.getHospitalName() +
                                                  String.format(mContext.getString(R.string.txt_num),
                                                                bean.getSelectedNum()));
                }
                else {
                    helper.setText(R.id.tv_title, bean.getHospitalName());
                }
                helper.setImageResource(R.id.iv_tab, bean.isExpanded() ? R.mipmap.ic_up : R.mipmap.ic_down);
                helper.itemView.setOnClickListener(v -> {
                    int pos = helper.getAdapterPosition();
                    if (bean.isExpanded()) {
                        collapse(pos);
                    }
                    else {
                        expand(pos);
                    }
                });
                break;
            case BASE_ONE:
                RemoteDepartBean remoteDepartBean = (RemoteDepartBean)item;
                helper.setText(R.id.tv_depart, remoteDepartBean.getDepartmentName());
                helper.itemView.setOnClickListener(v -> {
                    ImageView imageView = helper.getView(R.id.iv_select);
                    imageView.setSelected(!imageView.isSelected());
                    calcSelectNum(getParentPosition(remoteDepartBean), imageView.isSelected());
                    if (onRemoteDepartSelectListener != null) {
                        onRemoteDepartSelectListener.onRemoteDepartSelect(remoteDepartBean);
                    }
                });
                break;
            default:
                break;
        }
    }

    /**
     * 计算单个医院下科室选中情况
     */
    private void calcSelectNum(int parentPosition, boolean selected) {
        RemoteDepartTitleBean titleBean = (RemoteDepartTitleBean)getData().get(parentPosition);
        int num = titleBean.getSelectedNum();
        if (selected) {
            num++;
        }
        else {
            num--;
        }
        titleBean.setSelectedNum(num);
        notifyDataSetChanged();
    }

    private OnRemoteDepartSelectListener onRemoteDepartSelectListener;

    public void setOnRemoteDepartSelectListener(OnRemoteDepartSelectListener onRemoteDepartSelectListener) {
        this.onRemoteDepartSelectListener = onRemoteDepartSelectListener;
    }

    public interface OnRemoteDepartSelectListener {
        void onRemoteDepartSelect(RemoteDepartBean remoteDepartBean);
    }
}