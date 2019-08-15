package com.yht.yihuantong.ui.hospital;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.yht.frame.data.BaseData;
import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.CommonData;
import com.yht.frame.data.Tasks;
import com.yht.frame.data.bean.BaseListBean;
import com.yht.frame.data.bean.CooperateHospitalBean;
import com.yht.frame.data.bean.HospitalProductBean;
import com.yht.frame.http.retrofit.RequestUtils;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.widgets.textview.JustifiedTextView;
import com.yht.yihuantong.R;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/8/15 14:05
 * @des 医院详情
 */
public class HospitalDetailActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    @BindView(R.id.public_title_bar_title)
    TextView publicTitleBarTitle;
    @BindView(R.id.iv_status)
    ImageView ivStatus;
    @BindView(R.id.tv_hospital_address)
    TextView tvHospitalAddress;
    @BindView(R.id.tv_hospital_business)
    TextView tvHospitalBusiness;
    @BindView(R.id.tv_hospital_project)
    TextView tvHospitalProject;
    @BindView(R.id.full_list_view)
    ListView fullListView;
    @BindView(R.id.tv_hospital_introduction)
    JustifiedTextView tvHospitalIntroduction;
    @BindView(R.id.tv_more)
    TextView tvMore;
    @BindView(R.id.layout_product)
    LinearLayout layoutProduct;
    @BindView(R.id.tv_product_more)
    TextView tvProductMore;
    /**
     * 服务项的适配器
     */
    private ProductAdapter productAdapter;
    /**
     * 当前医院
     */
    private CooperateHospitalBean curHospital;
    private List<HospitalProductBean> hospitalProductBeans = new ArrayList<>();
    /**
     * 最多显示服务线数量
     */
    public static final int MAX_NUM = 5;

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_hospital_detail;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        if (getIntent() != null) {
            curHospital = (CooperateHospitalBean)getIntent().getSerializableExtra(CommonData.KEY_HOSPITAL_BEAN);
        }
        productAdapter = new ProductAdapter();
        fullListView.setOnItemClickListener(this);
        fullListView.setAdapter(productAdapter);
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        if (curHospital != null) {
            publicTitleBarTitle.setText(curHospital.getHospitalName());
            tvHospitalAddress.setText(curHospital.getAddress());
            tvHospitalBusiness.setText(StringUtils.join(curHospital.getServiceList(), ","));
            tvHospitalProject.setText(String.format(getString(R.string.txt_item), hospitalProductBeans.size()));
            tvHospitalIntroduction.setText(curHospital.getIntroduce());
            tvHospitalIntroduction.post(() -> {
                if (tvHospitalIntroduction.getLineCount() > MAX_NUM) {
                    tvMore.setVisibility(View.VISIBLE);
                    initIntroduction(true);
                }
                else {
                    tvMore.setVisibility(View.GONE);
                }
            });
        }
    }

    @Override
    public void fillNetWorkData() {
        super.fillNetWorkData();
        getCooperateHospitalProjectList();
    }

    /**
     * 获取合作医院下服务项
     */
    private void getCooperateHospitalProjectList() {
        RequestUtils.getCooperateHospitalProjectList(this, loginBean.getToken(), curHospital.getHospitalCode(),
                                                     BaseData.BASE_PAGE_DATA_NUM, 1, false, this);
    }

    /**
     * 简介  展开or收起
     *
     * @param mode true为展开
     */
    private void initIntroduction(boolean mode) {
        if (mode) {
            tvMore.setSelected(false);
            tvMore.setText(R.string.txt_pack_down);
            tvHospitalIntroduction.setMaxLines(MAX_NUM);
        }
        else {
            tvMore.setSelected(true);
            tvMore.setText(R.string.txt_pack_up);
            tvHospitalIntroduction.setMaxLines(Integer.MAX_VALUE);
        }
    }

    @OnClick({ R.id.layout_more_project, R.id.tv_more })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_more_project:
                Intent intent = new Intent(this, HospitalProductListActivity.class);
                intent.putExtra(CommonData.KEY_HOSPITAL_BEAN, curHospital);
                startActivity(intent);
                break;
            case R.id.tv_more:
                initIntroduction(tvMore.isSelected());
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, ProductDetailActivity.class);
        intent.putExtra(CommonData.KEY_PUBLIC_STRING, curHospital.getHospitalName());
        intent.putExtra(CommonData.KEY_HOSPITAL_PRODUCT_BEAN, hospitalProductBeans.get(position));
        startActivity(intent);
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        if (task == Tasks.GET_COOPERATE_HOSPITAL_PROJECT_LIST) {
            BaseListBean<HospitalProductBean> baseListBean = (BaseListBean<HospitalProductBean>)response.getData();
            if (baseListBean.getRecords() != null && baseListBean.getRecords().size() > 0) {
                layoutProduct.setVisibility(View.VISIBLE);
                hospitalProductBeans.addAll(baseListBean.getRecords());
                if (hospitalProductBeans.size() > MAX_NUM) {
                    tvProductMore.setVisibility(View.VISIBLE);
                }
                else {
                    tvProductMore.setVisibility(View.GONE);
                }
                setListViewHeightBasedOnChildren(fullListView, productAdapter, hospitalProductBeans.size() > MAX_NUM
                                                                               ? MAX_NUM
                                                                               : hospitalProductBeans.size());
            }
            else {
                layoutProduct.setVisibility(View.GONE);
            }
            tvHospitalProject.setText(String.format(getString(R.string.txt_item), hospitalProductBeans.size()));
            productAdapter.notifyDataSetChanged();
        }
    }

    private class ProductAdapter extends BaseAdapter {
        ViewHolder holder;

        @Override
        public int getCount() {
            return Math.min(MAX_NUM, hospitalProductBeans.size());
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
                convertView = LayoutInflater.from(HospitalDetailActivity.this)
                                            .inflate(R.layout.item_hospital_project, parent, false);
                holder.tvContent = convertView.findViewById(R.id.tv_content);
                holder.line = convertView.findViewById(R.id.view);
                convertView.setTag(holder);
            }
            else {
                holder = (ViewHolder)convertView.getTag();
            }
            holder.tvContent.setText(hospitalProductBeans.get(position).getName());
            if (position == hospitalProductBeans.size() - 1 || position == MAX_NUM - 1) {
                holder.line.setVisibility(View.GONE);
            }
            else {
                holder.line.setVisibility(View.VISIBLE);
            }
            return convertView;
        }
    }

    private class ViewHolder {
        private TextView tvContent;
        private View line;
    }

    /**
     * 设置高度
     *
     * @param listView
     */
    private void setListViewHeightBasedOnChildren(ListView listView, BaseAdapter baseAdapter, int count) {
        if (listView == null || baseAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < count; i++) {
            View listItem = baseAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (count - 1));
        listView.setLayoutParams(params);
    }
}
