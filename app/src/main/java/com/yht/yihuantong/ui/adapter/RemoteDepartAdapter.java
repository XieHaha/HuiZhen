package com.yht.yihuantong.ui.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.yht.frame.data.BaseData;
import com.yht.frame.data.bean.RemoteDepartBean;
import com.yht.frame.data.bean.RemoteDepartTitleBean;
import com.yht.yihuantong.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 顿顿
 * @date 19/6/5 14:25
 * @description 远程会诊 科室选择列表
 */
public class RemoteDepartAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder>
        implements BaseData {
    /**
     * 已选择的科室
     */
    private ArrayList<Integer> selectedRemoteDepartIds = new ArrayList<>();

    public RemoteDepartAdapter(List<MultiItemEntity> data) {
        super(data);
        addItemType(BASE_ZERO, R.layout.item_remote_depart_title);
        addItemType(BASE_ONE, R.layout.item_remote_depart);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultiItemEntity item) {
        switch (helper.getItemViewType()) {
            case BASE_ZERO:
                RemoteDepartTitleBean bean = (RemoteDepartTitleBean) item;
                if (bean.getSelectedNum() > 0) {
                    helper.setText(R.id.tv_title, bean.getHospitalName() +
                            String.format(mContext.getString(R.string.txt_num),
                                    bean.getSelectedNum()));
                } else {
                    helper.setText(R.id.tv_title, bean.getHospitalName());
                }
                helper.setImageResource(R.id.iv_tab, bean.isExpanded() ? R.mipmap.ic_up :
                        R.mipmap.ic_down);
                helper.itemView.setOnClickListener(v -> {
                    int pos = helper.getAdapterPosition();
                    if (bean.isExpanded()) {
                        collapse(pos);
                    } else {
                        expand(pos);
                    }
                });
                break;
            case BASE_ONE:
                RemoteDepartBean remoteDepartBean = (RemoteDepartBean) item;
                TextView tvDepart = helper.getView(R.id.tv_depart);
                ImageView image = helper.getView(R.id.iv_select);
                tvDepart.setText(remoteDepartBean.getDepartmentName());
                if (remoteDepartBean.isFree()) {
                    tvDepart.setSelected(true);
                    image.setVisibility(View.VISIBLE);
                } else {
                    tvDepart.setSelected(false);
                    image.setVisibility(View.INVISIBLE);
                    tvDepart.append(mContext.getString(R.string.txt_reservation_error));
                }
                if (selectedRemoteDepartIds.contains(remoteDepartBean.getDepartmentId())) {
                    image.setSelected(true);
                    if (onRemoteDepartSelectListener != null) {
                        //二次选择时，历史数据回填
                        onRemoteDepartSelectListener.addRemoteDepartHistory(remoteDepartBean);
                    }
                } else {
                    image.setSelected(false);
                }
                //点击事件处理
                helper.itemView.setOnClickListener(v -> {
                    if (onRemoteDepartSelectListener != null && remoteDepartBean.isFree()) {
                        RemoteDepartTitleBean parentBean =
                                (RemoteDepartTitleBean) getData().get(getParentPosition(item));
                        onRemoteDepartSelectListener.onRemoteDepartSelect(remoteDepartBean,
                                parentBean);
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
        RemoteDepartTitleBean titleBean = (RemoteDepartTitleBean) getData().get(parentPosition);
        int num = titleBean.getSelectedNum();
        if (selected) {
            num++;
        } else {
            num--;
        }
        titleBean.setSelectedNum(num);
        notifyDataSetChanged();
    }

    public void setSelectedRemoteDepartIds(ArrayList<Integer> selectedRemoteDepartPositions) {
        this.selectedRemoteDepartIds = selectedRemoteDepartPositions;
    }

    private OnRemoteDepartSelectListener onRemoteDepartSelectListener;

    public void setOnRemoteDepartSelectListener(OnRemoteDepartSelectListener onRemoteDepartSelectListener) {
        this.onRemoteDepartSelectListener = onRemoteDepartSelectListener;
    }

    public interface OnRemoteDepartSelectListener {
        /**
         * 已选科室
         *
         * @param bean       数据
         * @param parentBean 数据
         */
        void onRemoteDepartSelect(RemoteDepartBean bean, RemoteDepartTitleBean parentBean);

        /**
         * 历史数据回填
         *
         * @param bean 科室
         */
        void addRemoteDepartHistory(RemoteDepartBean bean);
    }
}