package com.yht.yihuantong.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yht.frame.data.bean.SelectCheckTypeBean;
import com.yht.frame.utils.BaseUtils;
import com.yht.yihuantong.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 顿顿
 * @date 19/8/12 17:56
 * @description 服务项  服务包
 */
public class ShopCheckTypeAdapter extends BaseAdapter {
    private Context context;
    /**
     * 服务项、服务包列表
     */
    private List<SelectCheckTypeBean> list = new ArrayList<>();

    public ShopCheckTypeAdapter(Context context) {
        this.context = context;
    }

    public void setList(List<SelectCheckTypeBean> list) {
        if (list == null) {
            list = new ArrayList<>();
        }
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_check_shop, parent, false);
            holder.tvName = convertView.findViewById(R.id.tv_check_type_name);
            holder.tvPrice = convertView.findViewById(R.id.tv_price);
            holder.ivDelete = convertView.findViewById(R.id.iv_delete);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder)convertView.getTag();
        }
        init(holder, position);
        return convertView;
    }

    private void init(ViewHolder holder, int position) {
        SelectCheckTypeBean item = list.get(position);
        holder.tvName.setText(item.getProjectName());
        holder.tvPrice.setText(
                String.format(context.getString(R.string.txt_price), BaseUtils.getPrice(item.getPrice())));
        holder.ivDelete.setOnClickListener(v -> {
            if (onServiceDeleteListener != null) {
                onServiceDeleteListener.onServiceDelete(item);
            }
        });
    }

    private class ViewHolder {
        private TextView tvName, tvPrice;
        private ImageView ivDelete;
    }

    private OnServiceDeleteListener onServiceDeleteListener;

    public void setOnServiceDeleteListener(OnServiceDeleteListener onServiceDeleteListener) {
        this.onServiceDeleteListener = onServiceDeleteListener;
    }

    public interface OnServiceDeleteListener {
        void onServiceDelete(SelectCheckTypeBean code);
    }
}
