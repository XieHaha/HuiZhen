package com.yht.yihuantong.ui.hint;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.yht.frame.data.CommonData;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.utils.SharePreferenceUtil;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ZycApplication;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/8/1 20:21
 * @description
 */
public class UrlManagerActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    @BindView(R.id.list_view)
    ListView listView;
    @BindView(R.id.edit_url)
    EditText editUrl;
    private MyAdapter adapter;
    private ArrayList<String> urls = new ArrayList<>();

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_url_manager;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        urls.add("https://doctor-pre.med-value.com/");
        urls.add("http://doctor-t.med-value.com/api/");
        urls.add("http://patient-t.med-value.com/api/");
        urls.add("http://192.168.0.126:7071/");
        urls.add("http://192.168.0.100:7071/");
        urls.add("http://192.168.0.111:7071/");
        adapter = new MyAdapter();
        listView.setOnItemClickListener(this);
        listView.setAdapter(adapter);
    }

    @OnClick(R.id.tv_add)
    public void onViewClicked() {
        String url = editUrl.getText().toString().trim();
        if (!TextUtils.isEmpty(url)) {
            update(url);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        update(urls.get(position));
    }

    private void update(String baseUrl) {
        new SharePreferenceUtil(this).putAlwaysString(CommonData.KEY_BASE_URL, baseUrl);
        ZycApplication.getInstance().updateBaseUrl(baseUrl);
        finish();
    }

    class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return urls.size();
        }

        @Override
        public Object getItem(int position) {
            return urls.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.item_url, parent, false);
                holder = new ViewHolder();
                holder.textView = convertView.findViewById(R.id.text);
                convertView.setTag(holder);
            }
            else {
                holder = (ViewHolder)convertView.getTag();
            }
            holder.textView.setText(urls.get(position));
            return convertView;
        }
    }

    class ViewHolder {
        public TextView textView;
    }
}
