package com.yht.yihuantong.ui.hospital;

import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yht.frame.data.BaseData;
import com.yht.frame.data.CommonData;
import com.yht.frame.data.bean.CooperateHospitalBean;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.utils.ScreenUtils;
import com.yht.frame.widgets.view.AbstractOnPageChangeListener;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ui.adapter.ViewPagerAdapter;
import com.yht.yihuantong.ui.hospital.fragment.HospitalServiceFragment;
import com.yht.yihuantong.ui.hospital.fragment.HospitalServicePackageFragment;
import com.yht.yihuantong.ui.transfer.fragment.TransferWaitFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/6/27 14:17
 * @description 我收到的转诊
 */
public class HospitalServiceListActivity extends BaseActivity
        implements TransferWaitFragment.OnPendingTransferOrderListener {
    @BindView(R.id.public_title_bar_title)
    TextView publicTitleBarTitle;
    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.layout_button)
    LinearLayout layoutButton;
    @BindView(R.id.view_bar)
    View viewBar;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.tv_receiving_transfer_num)
    TextView tvReceivingTransferNum;
    @BindView(R.id.layout_receiving_transfer_num)
    RelativeLayout layoutReceivingTransferNum;
    /**
     * 当前医院
     */
    private CooperateHospitalBean curHospital;

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_hospital_service;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        if (getIntent() != null) {
            curHospital = (CooperateHospitalBean)getIntent().getSerializableExtra(CommonData.KEY_HOSPITAL_BEAN);
            int num = getIntent().getIntExtra(CommonData.KEY_PUBLIC, 0);
            publicTitleBarTitle.setText(String.format(getString(R.string.title_hospital_product),
                                                      num > BaseData.BASE_MEAASGE_DISPLAY_NUM ? getString(
                                                              R.string.txt_max_num) : String.valueOf(num)));
        }
        initFragment();
    }

    @Override
    public void initListener() {
        super.initListener();
        viewPager.addOnPageChangeListener(new AbstractOnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                int offset = calcViewBarOffset();
                move(position, offset, positionOffset);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position == 0) {
                    titleBar(false);
                }
                else {
                    titleBar(true);
                }
            }
        });
    }

    /**
     * 碎片初始化
     */
    private void initFragment() {
        viewBar.setTranslationX(calcViewBarOffset());
        //服务包
        HospitalServicePackageFragment hospitalServicePackageFragment = new HospitalServicePackageFragment();
        hospitalServicePackageFragment.setCurHospital(curHospital);
        //服务项
        HospitalServiceFragment hospitalServiceFragment = new HospitalServiceFragment();
        hospitalServiceFragment.setCurHospital(curHospital);
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(hospitalServicePackageFragment);
        fragmentList.add(hospitalServiceFragment);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(viewPagerAdapter);
        titleBar(false);
    }

    @Override
    public void onPendingTransferOrder(int num) {
        if (num > 0) {
            layoutReceivingTransferNum.setVisibility(View.VISIBLE);
            tvReceivingTransferNum.setText(num > BaseData.BASE_MEAASGE_DISPLAY_MIDDLE_NUM
                                           ? getString(R.string.txt_middle_num)
                                           : String.valueOf(num));
        }
        else {
            layoutReceivingTransferNum.setVisibility(View.GONE);
        }
    }

    @OnClick({ R.id.layout_left, R.id.layout_right })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_left:
                titleBar(false);
                break;
            case R.id.layout_right:
                titleBar(true);
                break;
            default:
                break;
        }
    }

    /**
     * titlebar处理
     *
     * @param index true
     */
    private void titleBar(boolean index) {
        if (!index) {
            viewPager.setCurrentItem(0);
            tvLeft.setSelected(true);
            tvLeft.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            tvRight.setSelected(false);
            tvRight.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        }
        else {
            viewPager.setCurrentItem(1);
            tvLeft.setSelected(false);
            tvLeft.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            tvRight.setSelected(true);
            tvRight.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        }
    }

    /**
     * 计算游标位移量
     */
    private int calcViewBarOffset() {
        //获取屏幕宽度
        int width = ScreenUtils.getScreenWidth(this);
        return (width - viewBar.getWidth() * 2) / 4;
    }

    private void move(int position, int offset, float positionOffset) {
        viewBar.setTranslationX((position * viewBar.getWidth() + offset + position * offset * 2) +
                                (positionOffset * (offset * 2 + viewBar.getWidth())));
    }
}
