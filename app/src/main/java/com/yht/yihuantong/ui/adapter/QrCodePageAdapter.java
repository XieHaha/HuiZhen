package com.yht.yihuantong.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.yht.frame.data.bean.LoginBean;
import com.yht.frame.data.bean.QrCodeBean;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.utils.glide.GlideHelper;
import com.yht.yihuantong.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 顿顿
 * @date 19/8/14 10:21
 * @description 二维码
 */
public class QrCodePageAdapter extends PagerAdapter {
    private Context context;
    private LoginBean loginBean;
    private List<QrCodeBean> list = new ArrayList<>();

    public QrCodePageAdapter(Context context, LoginBean loginBean) {
        this.context = context;
        this.loginBean = loginBean;
    }

    public void setList(List<QrCodeBean> list) {
        if (list == null) {
            list = new ArrayList<>();
        }
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_qr_code, container, false);
        container.addView(view);
        bind(position, view);
        return view;
    }

    private void bind(int position, View view) {
        QrCodeBean item = list.get(position);
        ImageView header = view.findViewById(R.id.iv_head_img);
        TextView name = view.findViewById(R.id.tv_name);
        TextView jobTitle = view.findViewById(R.id.tv_title);
        TextView scanTitle = view.findViewById(R.id.tv_scan_title);
        ImageView barCodeImageView = view.findViewById(R.id.iv_qr);
        //基础信息
        Glide.with(context)
                .load(loginBean.getPhoto())
                .apply(GlideHelper.getOptions(BaseUtils.dp2px(context, 4)))
                .into(header);
        name.setText(loginBean.getDoctorName());
        jobTitle.setText(loginBean.getJobTitle());
        scanTitle.setText(item.getTitle());
        Glide.with(context).load(item.getContent()).apply(GlideHelper.getOptionsPic(1)).into(barCodeImageView);
    }
}
