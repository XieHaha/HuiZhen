package com.yht.yihuantong.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yht.yihuantong.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 顿顿
 * @date 19/6/19 16:37
 * @des
 */
public class CheckTypeListviewAdapter extends BaseAdapter {
    private Context context;
    private List<String> data;

    public CheckTypeListviewAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<String> data) {
        if (data == null) {
            data = new ArrayList<>();
        }
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_check_type_delete, parent, false);
            holder.tvCheckTypeName = convertView.findViewById(R.id.tv_check_type_name);
            holder.ivDelete = convertView.findViewById(R.id.iv_delete);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder)convertView.getTag();
        }
        initView(holder, position);
        return convertView;
    }

    /**
     * 数据处理
     *
     * @param holder
     * @param position
     */
    private void initView(ViewHolder holder, int position) {
        holder.tvCheckTypeName.setText(data.get(position));
        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onDeleteClickListener != null) {
                    onDeleteClickListener.onDelete(position);
                }
            }
        });
    }

    class ViewHolder {
        private TextView tvCheckTypeName;
        private ImageView ivDelete;
    }

    private OnDeleteClickListener onDeleteClickListener;

    public void setOnDeleteClickListener(OnDeleteClickListener onDeleteClickListener) {
        this.onDeleteClickListener = onDeleteClickListener;
    }

    public interface OnDeleteClickListener {
        void onDelete(int position);
    }
}
