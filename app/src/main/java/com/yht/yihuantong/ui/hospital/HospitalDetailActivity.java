package com.yht.yihuantong.ui.hospital;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.CommonData;
import com.yht.frame.data.Tasks;
import com.yht.frame.data.bean.CooperateHospitalBean;
import com.yht.frame.data.bean.HospitalProjectBean;
import com.yht.frame.data.bean.HospitalProjectParentBean;
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
    /**
     * 服务项数据
     */
    private HospitalProjectParentBean hospitalProjectParentBean;
    /**
     * 服务项
     */
    private List<HospitalProjectBean> hospitalProjectBeans = new ArrayList<>();
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
            tvHospitalProject.setText(String.format(getString(R.string.txt_item), hospitalProjectBeans.size()));
            if (TextUtils.isEmpty(curHospital.getIntroduce())) {
                tvHospitalIntroduction.setText(R.string.txt_none);
            }
            else {
                tvHospitalIntroduction.setText(curHospital.getIntroduce());
            }
            tvHospitalIntroduction.post(() -> {
                if (tvHospitalIntroduction.getLineCount() > MAX_NUM) {
                    tvMore.setVisibility(View.VISIBLE);
                    initIntroduction(true);
                }
                else {
                    tvMore.setVisibility(View.GONE);
                }
            });
            if (BASE_STRING_ONE_TAG.equals(curHospital.getCooperateStatus())) {
                ivStatus.setImageResource(R.mipmap.ic_label_cooperation);
                ArrayList<String> values = new ArrayList<>();
                for (String string : curHospital.getServiceList()) {
                    switch (string) {
                        case "1":
                            values.add(getString(R.string.txt_reserve_check));
                            break;
                        case "2":
                            values.add(getString(R.string.txt_reserve_transfer));
                            break;
                        case "3":
                            values.add(getString(R.string.txt_remote_consultation));
                            break;
                        default:
                            break;
                    }
                }
                tvHospitalBusiness.setText(StringUtils.join(values, ","));
            }
            else {
                ivStatus.setImageResource(R.mipmap.ic_label_no_cooperation);
                tvHospitalBusiness.setText(R.string.txt_business_support_not);
            }
        }
    }

    @Override
    public void fillNetWorkData() {
        super.fillNetWorkData();
        getCooperateHospitalProjectList();
    }

    /**
     * 获取合作医院下服务项(只有5条数据)
     */
    private void getCooperateHospitalProjectList() {
        RequestUtils.getCooperateHospitalProjectList(this, loginBean.getToken(), curHospital.getHospitalCode(), this);
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
                if (tvProductMore.getVisibility() == View.VISIBLE) {
                    Intent intent = new Intent(this, HospitalServiceListActivity.class);
                    intent.putExtra(CommonData.KEY_HOSPITAL_BEAN, curHospital);
                    intent.putExtra(CommonData.KEY_PUBLIC, hospitalProjectParentBean.getCount());
                    startActivity(intent);
                }
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
        HospitalProjectBean bean = hospitalProjectBeans.get(position);
        int type = bean.getType();
        Intent intent;
        if (type == BASE_ONE) {
            //服务项
            intent = new Intent(this, ServiceDetailActivity.class);
        }
        else {
            //服务包
            intent = new Intent(this, ServicePackageDetailActivity.class);
        }
        intent.putExtra(CommonData.KEY_ORDER_ID, bean.getProjectCode());
        startActivity(intent);
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        if (task == Tasks.GET_COOPERATE_HOSPITAL_PROJECT_LIST) {
            hospitalProjectParentBean = (HospitalProjectParentBean)response.getData();
            if (hospitalProjectParentBean.getPackageProductInfoList() != null &&
                hospitalProjectParentBean.getPackageProductInfoList().size() > 0) {
                layoutProduct.setVisibility(View.VISIBLE);
                hospitalProjectBeans.addAll(hospitalProjectParentBean.getPackageProductInfoList());
                if (hospitalProjectParentBean.getCount() > MAX_NUM) {
                    tvProductMore.setVisibility(View.VISIBLE);
                }
                else {
                    tvProductMore.setVisibility(View.GONE);
                }
                setListViewHeightBasedOnChildren(fullListView, productAdapter, hospitalProjectBeans.size() > MAX_NUM
                                                                               ? MAX_NUM
                                                                               : hospitalProjectBeans.size());
            }
            else {
                layoutProduct.setVisibility(View.GONE);
            }
            tvHospitalProject.setText(
                    String.format(getString(R.string.txt_item), hospitalProjectParentBean.getCount()));
            productAdapter.notifyDataSetChanged();
        }
    }

    private class ProductAdapter extends BaseAdapter {
        ViewHolder holder;

        @Override
        public int getCount() {
            return Math.min(MAX_NUM, hospitalProjectBeans.size());
        }

        @Override
        public Object getItem(int position) {
            return hospitalProjectBeans.get(position);
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
            holder.tvContent.setText(hospitalProjectBeans.get(position).getProjectName());
            if (position == hospitalProjectBeans.size() - 1 || position == MAX_NUM - 1) {
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
