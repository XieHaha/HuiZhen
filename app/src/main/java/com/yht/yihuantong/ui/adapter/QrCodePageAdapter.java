package com.yht.yihuantong.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yht.frame.data.bean.QrCodeBean;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.utils.ScreenUtils;
import com.yht.frame.utils.glide.GlideHelper;
import com.yht.frame.widgets.qrcode.BarCodeImageView;
import com.yht.yihuantong.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 顿顿
 * @date 19/8/14 10:21
 * @des 二维码
 */
public class QrCodePageAdapter extends PagerAdapter {
    private Context context;
    private List<QrCodeBean> list = new ArrayList<>();

    public QrCodePageAdapter(Context context) {
        this.context = context;
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

    @Override
    public float getPageWidth(int position) {
        super.getPageWidth(position);
        float width = 1.0f;
        //这个方法有些童鞋可能熟悉，这个是ViewPager在测量确定子View所占宽度的时候用到的，
        //由于在instantiateItem方法中第一个和最后一个特殊处理了，导致在展示的时候，
        //第一个的右边间距过大，最后一个左边间距过大，为了调整这部分差异，
        //需要实现这个方法，并且对第一个和最后一个宽度进行计算
        //下边代码中150，正是第一个和最后一个异常间距的差异值
        width = (float)(ScreenUtils.getScreenWidth(context) - BaseUtils.dp2px(context, 35)) /
                ScreenUtils.getScreenWidth(context);
        return width;
    }

    private void bind(int position, View view) {
        QrCodeBean item = list.get(position);
        ImageView header = view.findViewById(R.id.iv_head_img);
        TextView name = view.findViewById(R.id.tv_name);
        TextView jobTitle = view.findViewById(R.id.tv_title);
        TextView scanTitle = view.findViewById(R.id.tv_scan_title);
        TextView scanMode = view.findViewById(R.id.tv_scan_mode);
        BarCodeImageView barCodeImageView = view.findViewById(R.id.iv_qr);
        //基础信息
        Glide.with(context)
             .load(item.getHeader())
             .apply(GlideHelper.getOptions(BaseUtils.dp2px(context, 4)))
             .into(header);
        name.setText(item.getName());
        jobTitle.setText(item.getJobTitle());
        //二维码信息
        //这里设置第一页靠左，最后一页靠右，其他居中的效果
        if (position == 0) {
            view.setPadding(BaseUtils.dp2px(context, 8), BaseUtils.dp2px(context, 23), -BaseUtils.dp2px(context, 2),
                            (int)(ScreenUtils.getScreenHeight(context) * 0.136));
        }
        else {
            view.setPadding(-BaseUtils.dp2px(context, 2), BaseUtils.dp2px(context, 23), BaseUtils.dp2px(context, 8),
                            (int)(ScreenUtils.getScreenHeight(context) * 0.136));
        }
        if (position == 0) {
            scanMode.setBackgroundResource(R.drawable.corner8_2bc593_bg);
        }
        else {
            scanMode.setBackgroundResource(R.drawable.corner8_1491fc_bg);
        }
        scanMode.setText(item.getMode());
        scanTitle.setText(item.getTitle());
        barCodeImageView.setContent(item.getContent());
    }
}
