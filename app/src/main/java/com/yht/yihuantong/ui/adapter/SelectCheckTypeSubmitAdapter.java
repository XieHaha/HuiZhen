package com.yht.yihuantong.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yht.frame.data.bean.SelectCheckTypeBean;
import com.yht.frame.data.bean.SelectCheckTypeChildBean;
import com.yht.yihuantong.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 顿顿
 * @date 19/8/12 17:56
 * @description 服务项  服务包
 */
public class SelectCheckTypeSubmitAdapter extends BaseAdapter {
    private Context context;
    /**
     * 服务项、服务包列表
     */
    private List<SelectCheckTypeBean> list = new ArrayList<>();

    public SelectCheckTypeSubmitAdapter(Context context) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_check_select_submit, parent, false);
            holder.tvName = convertView.findViewById(R.id.tv_check_type_name);
            holder.layoutCheck = convertView.findViewById(R.id.layout_check);
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
        List<SelectCheckTypeChildBean> childBeans = item.getChildServiceTypes(item.getProjectCode());
        holder.layoutCheck.removeAllViews();
        if (childBeans != null && childBeans.size() > 0) {
            holder.layoutCheck.setVisibility(View.VISIBLE);
            for (SelectCheckTypeChildBean childBean : childBeans) {
                addServiceType(holder, childBean);
            }
        }
        else {
            holder.layoutCheck.setVisibility(View.GONE);
        }
    }

    private class ViewHolder {
        private TextView tvName;
        private LinearLayout layoutCheck;
    }

    private void addServiceType(ViewHolder holder, SelectCheckTypeChildBean childBean) {
        View convertView = LayoutInflater.from(context).inflate(R.layout.item_child_service, null);
        TextView tvContent = convertView.findViewById(R.id.tv_content);
        TextView tvNum = convertView.findViewById(R.id.tv_num);
        tvContent.setText(childBean.getProductName());
        tvNum.setText(String.format(context.getString(R.string.txt_amount), childBean.getProductCount()));
        holder.layoutCheck.addView(convertView);
    }
}