package com.yht.yihuantong.ui.main;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

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
public class QrCodeActivity extends BaseActivity {
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.status_bar_fix)
    View statusBarFix;
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
        statusBarFix.setLayoutParams(
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStateBarHeight(this)));
        if (getIntent() != null) {
            mode = getIntent().getBooleanExtra(CommonData.KEY_INTENT_BOOLEAN, false);
        }
        qrCodePageAdapter = new QrCodePageAdapter(this, loginBean);
        viewPager.setAdapter(qrCodePageAdapter);
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
        if (bean != null) { one.setContent(bean.getWxQr().getWxQrUrl()); }
        QrCodeBean two = new QrCodeBean();
        two.setTitle(getString(R.string.txt_scan_hint));
        two.setMode(getString(R.string.txt_menu_doctor));
        if (bean != null) { two.setContent(bean.getUserQr()); }
        qrCodeBeans.add(one);
        qrCodeBeans.add(two);
        qrCodePageAdapter.setList(qrCodeBeans);
        qrCodePageAdapter.notifyDataSetChanged();
        if (mode) {
            viewPager.setCurrentItem(1);
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
            QrCodeBean bean = (QrCodeBean)response.getData();
            initQrCodeData(bean);
        }
    }
}
