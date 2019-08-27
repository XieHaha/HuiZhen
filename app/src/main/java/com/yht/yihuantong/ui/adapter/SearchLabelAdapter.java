package com.yht.yihuantong.ui.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yht.yihuantong.R;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 顿顿
 * @date 19/8/12 17:56
 * @description 标签  搜索候选
 */
public class SearchLabelAdapter extends BaseAdapter {
    private Context context;
    private String searchKey;
    private List<String> list = new ArrayList<>();

    public SearchLabelAdapter(Context context) {
        this.context = context;
    }

    public void setList(List<String> list) {
        if (list == null) {
            list = new ArrayList<>();
        }
        this.list = list;
        notifyDataSetChanged();
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_search_label, parent, false);
            holder.tvName = convertView.findViewById(R.id.tv_label_name);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder)convertView.getTag();
        }
        init(holder, list.get(position));
        return convertView;
    }

    private void init(ViewHolder holder, String value) {
        holder.tvName.setText(setKeyWordColor(value, searchKey == null ? "" : searchKey));
    }

    /**
     * 设置搜索关键字高亮
     *
     * @param content 原文本内容
     */
    private SpannableString setKeyWordColor(String content, String searchKey) {
        SpannableString s = new SpannableString(content);
        Pattern p = Pattern.compile(searchKey);
        Matcher m = p.matcher(s);
        while (m.find()) {
            int start = m.start();
            int end = m.end();
            s.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.color_1491fc)), start, end,
                      Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return s;
    }

    private class ViewHolder {
        private TextView tvName;
    }
}
