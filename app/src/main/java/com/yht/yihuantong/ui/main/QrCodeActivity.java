package com.yht.yihuantong.ui.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.yht.frame.data.CommonData;
import com.yht.frame.data.bean.QrCodeBean;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.widgets.qrcode.QrCodeTransformer;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ui.adapter.QrCodePageAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/8/13 17:17
 * @des 我的二维码
 */
public class QrCodeActivity extends BaseActivity {
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.status_bar_fix)
    View statusBarFix;
    private QrCodePageAdapter qrCodePageAdapter;
    private boolean mode;

    @Override
    protected boolean isInitStatusBar() {
        return false;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_qr_code;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        statusBarFix.setLayoutParams(
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStateBarHeight(this)));
        if (getIntent() != null) {
            mode = getIntent().getBooleanExtra(CommonData.KEY_INTENT_BOOLEAN, false);
        }
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        qrCodePageAdapter = new QrCodePageAdapter(this);
        qrCodePageAdapter.setList(initQrCodeData());
        viewPager.setPageTransformer(true, new QrCodeTransformer(this));
        viewPager.setAdapter(qrCodePageAdapter);
        if (mode) {
            viewPager.setCurrentItem(1);
        }
    }

    /**
     * 初始化二维码数据
     */
    private ArrayList<QrCodeBean> initQrCodeData() {
        ArrayList<QrCodeBean> list = new ArrayList<>();
        QrCodeBean one = new QrCodeBean();
        one.setHeader(loginBean.getPhoto());
        one.setName(loginBean.getDoctorName());
        one.setJobTitle(loginBean.getJobTitle());
        one.setTitle(getString(R.string.txt_wechat_scan_hint));
        one.setMode(getString(R.string.txt_menu_patient));
        one.setContent("患者添加我");
        QrCodeBean two = new QrCodeBean();
        two.setHeader(loginBean.getPhoto());
        two.setName(loginBean.getDoctorName());
        two.setJobTitle(loginBean.getJobTitle());
        two.setTitle(getString(R.string.txt_scan_hint));
        two.setMode(getString(R.string.txt_menu_doctor));
        two.setContent("医生添加我");
        list.add(one);
        list.add(two);
        return list;
    }

    @OnClick(R.id.public_title_bar_back)
    public void onViewClicked() {
        finish();
    }
}
