package com.yht.yihuantong.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yht.frame.widgets.view.ExpandableLayout;
import com.yht.yihuantong.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 顿顿
 * @date 19/6/18 14:11
 * @des
 */
public class CheckTypeSelectAdapter extends RecyclerView.Adapter<CheckTypeSelectAdapter.ViewHolder> {
    private static final int UNSELECTED = -1;
    private RecyclerView recyclerView;
    private int selectedItem = UNSELECTED;
    private Context context;
    private List<String> datas = new ArrayList<>();

    public void setDatas(List<String> datas) {
        if (datas == null) {
            datas = new ArrayList<>();
        }
        this.datas = datas;
    }

    public CheckTypeSelectAdapter(Context context, RecyclerView recyclerView) {
        this.context = context;
        this.recyclerView = recyclerView;
    }

    @Override
    public CheckTypeSelectAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_check_select, parent, false);
        return new CheckTypeSelectAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CheckTypeSelectAdapter.ViewHolder holder, int position) {
        holder.bind();
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, ExpandableLayout.OnExpansionUpdateListener {
        private ExpandableLayout expandableLayout;
        private LinearLayout layout;
        private TextView tvCheckType;
        private View viewLine;

        private ViewHolder(View itemView) {
            super(itemView);
            tvCheckType = itemView.findViewById(R.id.tv_check_type_name);
            viewLine = itemView.findViewById(R.id.view_line);
            layout = itemView.findViewById(R.id.layout);
            for (int i = 0; i < 5; i++) {
                View view = LayoutInflater.from(context).inflate(R.layout.item_check_type_child, null);
                view.setTag(i);
                view.setOnClickListener(this);
                layout.addView(view);
            }
            expandableLayout = itemView.findViewById(R.id.expandable_layout);
            expandableLayout.setOnExpansionUpdateListener(this);
            tvCheckType.setOnClickListener(this);
        }

        private void bind() {
            int position = getAdapterPosition();
            boolean isSelected = position == selectedItem;
            expandableLayout.setExpanded(isSelected, false);
        }

        @Override
        public void onClick(View view) {
            if (view instanceof TextView) {
                CheckTypeSelectAdapter.ViewHolder holder = (CheckTypeSelectAdapter.ViewHolder)recyclerView.findViewHolderForAdapterPosition(
                        selectedItem);
                if (holder != null) {
                    holder.expandableLayout.collapse();
                    //在动画完成时处理
                    holder.viewLine.postOnAnimationDelayed(() -> holder.viewLine.setVisibility(View.INVISIBLE), 300);
                    holder.tvCheckType.setSelected(false);
                }
                int position = getAdapterPosition();
                if (position == selectedItem) {
                    selectedItem = UNSELECTED;
                }
                else {
                    expandableLayout.expand();
                    tvCheckType.setSelected(true);
                    viewLine.setVisibility(View.VISIBLE);
                    selectedItem = position;
                }
            }
            else if (view instanceof LinearLayout) {
                if (onCheckItemClickListener != null) {
                    onCheckItemClickListener.onCheckItemClick(getAdapterPosition(), (Integer)view.getTag());
                }
            }
        }

        @Override
        public void onExpansionUpdate(float expansionFraction, int state) {
            if (state == ExpandableLayout.State.EXPANDING) {
                recyclerView.smoothScrollToPosition(getAdapterPosition());
            }
        }
    }

    private OnCheckItemClickListener onCheckItemClickListener;

    public void setOnCheckItemClickListener(OnCheckItemClickListener onCheckItemClickListener) {
        this.onCheckItemClickListener = onCheckItemClickListener;
    }

    public interface OnCheckItemClickListener {
        /**
         * 点击事件回调
         *
         * @param parentPosition 检查项目
         * @param position       医院
         */
        void onCheckItemClick(int parentPosition, int position);
    }
}
