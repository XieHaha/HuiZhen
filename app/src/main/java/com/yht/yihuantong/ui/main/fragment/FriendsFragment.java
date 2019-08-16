package com.yht.yihuantong.ui.main.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yht.frame.api.ApiManager;
import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.Tasks;
import com.yht.frame.ui.BaseFragment;
import com.yht.frame.widgets.edittext.AbstractTextWatcher;
import com.yht.frame.widgets.edittext.SuperEditText;
import com.yht.frame.widgets.view.AbstractOnPageChangeListener;
import com.yht.frame.widgets.viewpager.CustomScrollViewPager;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ui.adapter.ViewPagerAdapter;
import com.yht.yihuantong.ui.doctor.DoctorFragment;
import com.yht.yihuantong.ui.main.listener.OnSearchListener;
import com.yht.yihuantong.ui.patient.fragment.PatientFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/5/17 14:55
 * @des 好友
 */
public class FriendsFragment extends BaseFragment implements OnSearchListener {
    @BindView(R.id.status_bar_fix)
    View statusBarFix;
    @BindView(R.id.layout_title)
    LinearLayout layoutTitle;
    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.layout_left)
    RelativeLayout layoutLeft;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.layout_right)
    RelativeLayout layoutRight;
    @BindView(R.id.view_bar)
    View viewBar;
    @BindView(R.id.view_pager)
    CustomScrollViewPager viewPager;
    @BindView(R.id.et_search)
    SuperEditText etSearch;
    @BindView(R.id.layout_search)
    RelativeLayout layoutSearch;
    @BindView(R.id.layout_bg)
    RelativeLayout layoutBg;
    /**
     * 患者列表
     */
    private PatientFragment patientFragment;
    /**
     * 医生列表
     */
    private DoctorFragment doctorFragment;
    /**
     * 搜索源  1为患者  2为医生
     */
    private int searchSource = -1;

    @Override
    public int getLayoutID() {
        return R.layout.fragment_friends;
    }

    @Override
    public void initView(View view, @NonNull Bundle savedInstanceState) {
        super.initView(view, savedInstanceState);
        statusBarFix.setLayoutParams(
                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStateBarHeight(getActivity())));
        initFragment();
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        iNotifyChangeListenerServer = ApiManager.getInstance().getServer();
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
                    titleBar(BASE_ZERO);
                }
                else {
                    titleBar(BASE_ONE);
                }
            }
        });
        etSearch.addTextChangedListener(new AbstractTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s.toString())) {
                    layoutBg.setVisibility(View.VISIBLE);
                    if (searchSource == BASE_ONE) {
                        patientFragment.getPatientsByLocal();
                    }
                    else {
                        doctorFragment.getDoctorsByLocal();
                    }
                }
                else {
                    layoutBg.setVisibility(View.GONE);
                    if (searchSource == BASE_ONE) {
                        patientFragment.sortSearchData(s.toString());
                    }
                    else {
                        doctorFragment.sortSearchData(s.toString());
                    }
                }
            }
        });
    }

    /**
     * 碎片初始化
     */
    private void initFragment() {
        //患者列表
        patientFragment = new PatientFragment();
        patientFragment.setOnSearchListener(this);
        //医生列表
        doctorFragment = new DoctorFragment();
        doctorFragment.setOnSearchListener(this);
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(patientFragment);
        fragmentList.add(doctorFragment);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager(), fragmentList);
        viewPager.setAdapter(viewPagerAdapter);
        titleBar(BASE_ZERO);
    }

    /**
     * 开启搜索
     */
    public void openSearch() {
        viewPager.setScrollEnabled(false);
        layoutSearch.setVisibility(View.VISIBLE);
        layoutBg.setVisibility(View.VISIBLE);
        etSearch.requestFocus();
        showSoftInputFromWindow(getContext(), etSearch);
        displaySearchLayout();
    }

    /**
     * 关闭搜索
     */
    public void closeSearch() {
        viewPager.setScrollEnabled(true);
        if (searchSource == BASE_ONE) {
            patientFragment.closeSearch();
        }
        else {
            doctorFragment.closeSearch();
        }
        etSearch.setText("");
        //隐藏软键盘
        hideSoftInputFromWindow(getContext(), etSearch);
        //开启隐藏动画
        hideSearchLayout();
    }

    /**
     * 显示搜索框
     */
    private void displaySearchLayout() {
        Animation toUp = AnimationUtils.loadAnimation(getContext(), R.anim.anim_top_in);
        Animation alpha = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
        layoutBg.startAnimation(alpha);
        layoutSearch.startAnimation(toUp);
    }

    /**
     * 隐藏搜索框
     */
    private void hideSearchLayout() {
        Animation toUp = AnimationUtils.loadAnimation(getContext(), R.anim.anim_top_out);
        Animation alpha = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out);
        toUp.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                layoutSearch.setVisibility(View.GONE);
                layoutBg.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        layoutBg.startAnimation(alpha);
        layoutSearch.startAnimation(toUp);
    }

    /**
     * titlebar处理
     *
     * @param one true 为默认
     */
    private void titleBar(int one) {
        if (one == BASE_ZERO) {
            viewPager.setCurrentItem(0);
            tvLeft.setSelected(true);
            tvLeft.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            tvRight.setSelected(false);
            tvRight.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        }
        else {
            viewPager.setCurrentItem(1);
            tvLeft.setSelected(false);
            tvLeft.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            tvRight.setSelected(true);
            tvRight.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        }
    }

    @OnClick({ R.id.layout_left, R.id.layout_right, R.id.tv_search_cancel, R.id.layout_bg })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_left:
                titleBar(BASE_ZERO);
                break;
            case R.id.layout_right:
                titleBar(BASE_ONE);
                break;
            case R.id.tv_search_cancel:
            case R.id.layout_bg:
                closeSearch();
                break;
            default:
                break;
        }
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        switch (task) {
            default:
                break;
        }
    }

    @Override
    public void onSearch(int mode, int num) {
        searchSource = mode;
        openSearch();
        switch (mode) {
            case BASE_ONE:
                etSearch.setHint(String.format(getString(R.string.txt_patient_search_hint), num));
                break;
            case BASE_TWO:
                etSearch.setHint(String.format(getString(R.string.txt_doctor_search_hint), num));
                break;
            default:
                break;
        }
    }

    /**
     * 计算游标位移量
     */
    private int calcViewBarOffset() {
        //获取控件宽度
        int width = layoutTitle.getMeasuredWidth();
        return (width - viewBar.getWidth() * 2) / 4;
    }
}
