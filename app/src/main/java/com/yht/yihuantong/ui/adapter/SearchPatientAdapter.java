package com.yht.yihuantong.ui.adapter;

import android.content.Context;
import androidx.core.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yht.frame.data.bean.PatientBean;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.widgets.edittext.SuperEditText;
import com.yht.yihuantong.R;
import com.yht.yihuantong.utils.text.BankCardTextWatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 顿顿
 * @date 19/8/12 17:56
 * @description 预约  搜索候选
 */
public class SearchPatientAdapter extends BaseAdapter {
    private Context context;
    private String searchKey;
    private List<PatientBean> list = new ArrayList<>();

    public SearchPatientAdapter(Context context) {
        this.context = context;
    }

    public void setList(List<PatientBean> list) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_search_patient, parent, false);
            holder.tvName = convertView.findViewById(R.id.tv_patient_name);
            holder.etIdCard = convertView.findViewById(R.id.tv_id_card);
            BankCardTextWatcher.bind(holder.etIdCard, 20);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder)convertView.getTag();
        }
        init(holder, position);
        return convertView;
    }

    private void init(ViewHolder holder, int position) {
        PatientBean bean = list.get(position);
        holder.etIdCard.setText(BaseUtils.asteriskUserCard(bean.getIdCard(),8));
        holder.tvName.setText(setKeyWordColor(bean.getName(), searchKey == null ? "" : searchKey));
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
        private SuperEditText etIdCard;
    }
}
