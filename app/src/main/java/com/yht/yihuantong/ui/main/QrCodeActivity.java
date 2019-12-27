package com.yht.yihuantong.ui.main;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.CommonData;
import com.yht.frame.data.Tasks;
import com.yht.frame.data.bean.QrCodeBean;
import com.yht.frame.http.retrofit.RequestUtils;
import com.yht.frame.ui.BaseActivity;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ui.adapter.QrCodePageAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/8/13 17:17
 * @description 我的二维码
 */
public class QrCodeActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.iv_bg)
    ImageView ivBg;
    @BindView(R.id.iv_left)
    ImageView ivLeft;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    private QrCodePageAdapter qrCodePageAdapter;
    private ArrayList<QrCodeBean> qrCodeBeans = new ArrayList<>();
    /**
     * TRUE 显示医生二维码
     */
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
        if (getIntent() != null) {
            mode = getIntent().getBooleanExtra(CommonData.KEY_INTENT_BOOLEAN, false);
        }
        qrCodePageAdapter = new QrCodePageAdapter(this, loginBean);
        viewPager.setAdapter(qrCodePageAdapter);
        viewPager.addOnPageChangeListener(this);
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        getDoctorQrCode();
    }

    /**
     * 获取二维码
     */
    private void getDoctorQrCode() {
        RequestUtils.getDoctorQrCode(this, loginBean.getToken(), this);
    }

    /**
     * 初始化二维码数据
     */
    private ArrayList<QrCodeBean> initQrCodeData(QrCodeBean bean) {
        qrCodeBeans.clear();
        QrCodeBean one = new QrCodeBean();
        one.setTitle(getString(R.string.txt_wechat_scan_hint));
        one.setMode(getString(R.string.txt_menu_patient));
        if (bean != null) {
            one.setContent(bean.getWxQr().getWxQrUrl());
        }
        QrCodeBean two = new QrCodeBean();
        two.setTitle(getString(R.string.txt_scan_hint));
        two.setMode(getString(R.string.txt_menu_doctor));
        if (bean != null) {
            two.setContent(bean.getUserQr());
        }
        qrCodeBeans.add(one);
        qrCodeBeans.add(two);
        qrCodePageAdapter.setList(qrCodeBeans);
        qrCodePageAdapter.notifyDataSetChanged();
        if (mode) {
            viewPager.setCurrentItem(1);
        }else {
            ivLeft.setSelected(true);
        }
        return qrCodeBeans;
    }

    @OnClick(R.id.public_title_bar_back)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        if (task == Tasks.GET_DOCTOR_QR_CODE) {
            QrCodeBean bean = (QrCodeBean) response.getData();
            initQrCodeData(bean);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case BASE_ZERO:
                ivBg.setImageResource(R.mipmap.pic_patient_bg);
                ivLeft.setSelected(true);
                ivRight.setSelected(false);
                break;
            case BASE_ONE:
                ivBg.setImageResource(R.mipmap.pic_doctor_bg);
                ivLeft.setSelected(false);
                ivRight.setSelected(true);
                break;
            default:
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
