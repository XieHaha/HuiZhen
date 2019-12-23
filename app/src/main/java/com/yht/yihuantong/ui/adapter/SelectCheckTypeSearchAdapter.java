package com.yht.yihuantong.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yht.frame.data.BaseData;
import com.yht.frame.data.bean.SelectCheckTypeBean;
import com.yht.frame.data.bean.SelectCheckTypeChildBean;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.widgets.view.CenterImageSpan;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ZycApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 顿顿
 * @date 19/8/12 17:56
 * @description 服务项  服务包
 */
public class SelectCheckTypeSearchAdapter extends BaseAdapter {
    private Context context;
    private Bitmap bitmap;
    /**
     * 服务项、服务包列表
     */
    private List<SelectCheckTypeBean> list = new ArrayList<>();

    public SelectCheckTypeSearchAdapter(Context context) {
        this.context = context;
    }

    public void setList(List<SelectCheckTypeBean> list) {
        if (list == null) {
            list = new ArrayList<>();
        }
        this.list = list;
        notifyDataSetChanged();
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_check_select, parent
                    , false);
            holder.tvName = convertView.findViewById(R.id.tv_check_type_name);
            holder.tvPrice = convertView.findViewById(R.id.tv_price);
            holder.ivSelect = convertView.findViewById(R.id.iv_select);
            holder.layoutCheck = convertView.findViewById(R.id.layout_check);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        init(holder, position);
        return convertView;
    }

    private void init(ViewHolder holder, int position) {
        SelectCheckTypeBean item = list.get(position);
        holder.tvName.setText(String.format(context.getString(R.string.txt_space),
                item.getProjectName()));
        //1、服务包；2、服务包线上支付；3、服务包配置了不可退款。
        if (item.getType() == BaseData.BASE_TWO && item.getPayType() == BaseData.BASE_ONE && item.getRefundType() == BaseData.BASE_ZERO) {
            holder.tvName.append(appendImage(item.getProjectName()));
        }
        holder.tvPrice.setText(
                String.format(context.getString(R.string.txt_price),
                        BaseUtils.getPrice(item.getPrice())));
        ArrayList<String> selectCodes = ZycApplication.getInstance().getSelectCodes();
        if (selectCodes != null && selectCodes.contains(item.getProjectCode())) {
            holder.ivSelect.setSelected(true);
        } else {
            holder.ivSelect.setSelected(false);
        }
        List<SelectCheckTypeChildBean> childBeans = item.getProductInfoList();
        holder.layoutCheck.removeAllViews();
        if (childBeans != null && childBeans.size() > 0) {
            for (SelectCheckTypeChildBean childBean : childBeans) {
                addServiceType(holder, childBean);
            }
        }
    }

    private class ViewHolder {
        private TextView tvName, tvPrice;
        private ImageView ivSelect;
        private LinearLayout layoutCheck;
    }

    private void addServiceType(ViewHolder holder, SelectCheckTypeChildBean childBean) {
        View convertView = LayoutInflater.from(context).inflate(R.layout.item_child_service, null);
        TextView tvContent = convertView.findViewById(R.id.tv_content);
        TextView tvNum = convertView.findViewById(R.id.tv_num);
        tvContent.setText(childBean.getProductName());
        tvNum.setText(String.format(context.getString(R.string.txt_amount),
                childBean.getProductCount()));
        holder.layoutCheck.addView(convertView);
    }

    private SpannableString appendImage(String showText) {
        CenterImageSpan imgSpan = new CenterImageSpan(context, bitmap);
        SpannableString spanString = new SpannableString(showText);
        spanString.setSpan(imgSpan, 0, showText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanString;
    }
}
