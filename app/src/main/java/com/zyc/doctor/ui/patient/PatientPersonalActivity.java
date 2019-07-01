package com.zyc.doctor.ui.patient;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyphenate.easeui.EaseConstant;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.widgets.view.AbstractOnPageChangeListener;
import com.yht.frame.widgets.view.ViewPrepared;
import com.zyc.doctor.R;
import com.zyc.doctor.chat.EaseChatFragment;
import com.zyc.doctor.ui.adapter.ViewPagerAdapter;
import com.zyc.doctor.ui.patient.fragment.PatientInfoFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.autosize.utils.ScreenUtils;

/**
 * @author 顿顿
 * @date 19/6/27 14:17
 * @des
 */
public class PatientPersonalActivity extends BaseActivity {
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
    @BindView(R.id.public_title_bar_title)
    TextView publicTitleBarTitle;
    /**
     * 在线聊天
     */
    private EaseChatFragment easeChatFragment;

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_patient_personal;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        new ViewPrepared().asyncPrepare(tvLeft, (w, h) -> {
            ViewGroup.LayoutParams params = viewBar.getLayoutParams();
            params.width = w + BaseUtils.dp2px(this, 12);
            viewBar.setLayoutParams(params);
            viewBar.setTranslationX(calcViewBarOffset());
        });
        publicTitleBarTitle.setText("患者姓名");
        initFragment();
    }

    @Override
    public void initListener() {
        super.initListener();
        viewPager.addOnPageChangeListener(new AbstractOnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                int offset = calcViewBarOffset();
                viewBar.setTranslationX((position * viewBar.getWidth() + offset + position * offset * 2) +
                                        (positionOffset * (offset * 2 + viewBar.getWidth())));
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position == 0) {
                    titleBar(true);
                }
                else {
                    titleBar(false);
                }
            }
        });
    }

    /**
     * 碎片初始化
     */
    private void initFragment() {
        //患者信息
        PatientInfoFragment patientInfoFragment = new PatientInfoFragment();
        PatientInfoFragment patientInfoFragment1 = new PatientInfoFragment();
        //在线聊天
        easeChatFragment = new EaseChatFragment();
        //传入参数
        Bundle args = new Bundle();
        args.putInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
        args.putString(EaseConstant.EXTRA_USER_ID, "18582317119_p");
        easeChatFragment.hideTitleBar();
        easeChatFragment.setArguments(args);
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(patientInfoFragment);
        fragmentList.add(easeChatFragment);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(viewPagerAdapter);
        titleBar(true);
    }

    @OnClick({ R.id.layout_left, R.id.layout_right })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_left:
                titleBar(true);
                break;
            case R.id.layout_right:
                titleBar(false);
                break;
            default:
                break;
        }
    }

    /**
     * titlebar处理
     *
     * @param one true 为默认
     */
    private void titleBar(boolean one) {
        if (one) {
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
        int width = ScreenUtils.getScreenSize(this)[0];
        return (width - viewBar.getWidth() * 2) / 4;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        if (easeChatFragment != null) {
            easeChatFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onPermissionNeedExplanation(@NonNull String permissionName) {
        if (easeChatFragment != null) {
            easeChatFragment.onPermissionNeedExplanation(permissionName);
        }
    }

    @Override
    public void onNoPermissionNeeded(@NonNull Object permissionName) {
        super.onNoPermissionNeeded(permissionName);
        if (easeChatFragment != null) {
            easeChatFragment.onNoPermissionNeeded(permissionName);
        }
    }
}
