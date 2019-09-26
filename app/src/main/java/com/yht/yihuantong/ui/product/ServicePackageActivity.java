package com.yht.yihuantong.ui.product;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yht.frame.data.bean.HospitalProductBean;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.widgets.recyclerview.FullListView;
import com.yht.frame.widgets.textview.ExpandTextView;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ui.reservation.remote.ReservationRemoteActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/9/26 11:09
 * @description
 */
public class ServicePackageActivity extends BaseActivity {
    @BindView(R.id.public_title_bar_title)
    TextView publicTitleBarTitle;
    @BindView(R.id.tv_introduction)
    ExpandTextView tvIntroduction;
    @BindView(R.id.list_view)
    FullListView listView;
    @BindView(R.id.tv_notice)
    TextView tvNotice;
    private ProductAdapter productAdapter;
    /**
     * 服务包内容
     */
    private List<HospitalProductBean> hospitalProductBeans = new ArrayList<>();

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_service_package;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        productAdapter = new ProductAdapter();
        listView.setAdapter(productAdapter);
        for (int i = 0; i < 5; i++) {
            hospitalProductBeans.add(new HospitalProductBean());
        }
    }

    @OnClick(R.id.tv_check_next)
    public void onViewClicked() {
        Intent intent = new Intent(this, ReservationRemoteActivity.class);
        startActivity(intent);
    }

    private class ProductAdapter extends BaseAdapter {
        ViewHolder holder;

        @Override
        public int getCount() {
            return hospitalProductBeans.size();
        }

        @Override
        public Object getItem(int position) {
            return hospitalProductBeans.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = getLayoutInflater().inflate(R.layout.item_hospital_project, parent, false);
                holder.tvContent = convertView.findViewById(R.id.tv_content);
                holder.tvNum = convertView.findViewById(R.id.tv_num);
                holder.ivRight = convertView.findViewById(R.id.iv_right);
                holder.line = convertView.findViewById(R.id.view);
                convertView.setTag(holder);
            }
            else {
                holder = (ViewHolder)convertView.getTag();
            }
            holder.tvContent.setText("服务项");
            holder.tvNum.setVisibility(View.VISIBLE);
            holder.tvNum.setText("x2");
            holder.ivRight.setVisibility(View.GONE);
            holder.line.setVisibility(View.GONE);
            return convertView;
        }
    }

    private class ViewHolder {
        private TextView tvContent, tvNum;
        private ImageView ivRight;
        private View line;
    }
}
