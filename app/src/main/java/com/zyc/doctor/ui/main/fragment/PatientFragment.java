package com.zyc.doctor.ui.main.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yht.frame.data.bean.PatientBean;
import com.yht.frame.ui.BaseFragment;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.widgets.edittext.SuperEditText;
import com.yht.frame.widgets.recyclerview.CustomItemDecoration;
import com.yht.frame.widgets.recyclerview.IndexBar;
import com.yht.frame.widgets.recyclerview.SideBar;
import com.yht.frame.widgets.recyclerview.animator.SlideInOutLeftItemAnimator;
import com.zyc.doctor.R;
import com.zyc.doctor.ui.adapter.PatientAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author 顿顿
 * @date 19/5/17 14:55
 * @des 患者列表
 */
public class PatientFragment extends BaseFragment implements SuperEditText.OndeleteClickListener {
    @BindView(R.id.status_bar_fix)
    View statusBarFix;
    @BindView(R.id.public_main_title)
    TextView publicMainTitle;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.side_bar)
    SideBar sideBar;
    @BindView(R.id.index_bar)
    IndexBar indexBar;
    private TextView tvCancel;
    private SuperEditText searchEdit;
    private LinearLayout layoutSearch;
    /**
     * 适配器
     */
    private PatientAdapter patientAdapter;
    /**
     * recycler
     */
    private LinearLayoutManager layoutManager;
    /**
     * 分隔线
     */
    private CustomItemDecoration decoration;
    /**
     *
     */
    private List<PatientBean> patientBeans = new ArrayList<>();

    @Override
    public int getLayoutID() {
        return R.layout.fragment_patient;
    }

    @Override
    public void initView(View view, @NonNull Bundle savedInstanceState) {
        super.initView(view, savedInstanceState);
        publicMainTitle.setText(R.string.title_patient);
        statusBarFix.setLayoutParams(
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStateBarHeight(getActivity())));
        initEvents();
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.addItemDecoration(decoration = new CustomItemDecoration(getContext()));
        recyclerview.setItemAnimator(new SlideInOutLeftItemAnimator(recyclerview));
        initDatas();
        patientAdapter = new PatientAdapter(R.layout.item_patient, patientBeans);
        View headerView = LayoutInflater.from(getContext()).inflate(R.layout.view_patient_header, null);
        initHeaderView(headerView);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_bottom_space, null);
        patientAdapter.addHeaderView(headerView);
        patientAdapter.addFooterView(view);
        recyclerview.setAdapter(patientAdapter);
    }

    /**
     * 头部搜索框
     *
     * @param headerView
     */
    private void initHeaderView(View headerView) {
        layoutSearch = headerView.findViewById(R.id.layout_search);
        searchEdit = headerView.findViewById(R.id.et_search_hospital);
        tvCancel = headerView.findViewById(R.id.et_search_cancel);
        //让EditText失去焦点，然后获取点击事件
        searchEdit.setFocusable(false);
        searchEdit.setFocusableInTouchMode(false);
        searchEdit.setOndeleteClickListener(this);
        searchEdit.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
    }

    private void initDatas() {
        String[] names = {
                "孙尚香", "安其拉", "白起", "不知火舞", "@小马快跑", "_德玛西亚之力_", "妲己", "狄仁杰", "典韦", "韩信", "老夫子", "刘邦", "刘禅", "鲁班七号",
                "墨子", "孙膑", "孙尚香", "孙悟空", "项羽", "亚瑟", "周瑜", "庄周", "蔡文姬", "甄姬", "廉颇", "程咬金", "后羿", "扁鹊", "钟无艳", "小乔",
                "王昭君", "虞姬", "李元芳", "张飞", "刘备", "牛魔", "张良", "兰陵王", "露娜", "貂蝉", "达摩", "曹操", "芈月", "荆轲", "高渐离", "钟馗",
                "花木兰", "关羽", "李白", "宫本武藏", "吕布", "嬴政", "娜可露露", "武则天", "赵云", "姜子牙", };
        for (String name : names) {
            PatientBean bean = new PatientBean();
            bean.setName(name);
            patientBeans.add(bean);
        }
        //对数据源进行排序
        BaseUtils.sortData(patientBeans);
        //返回一个包含所有Tag字母在内的字符串并赋值给tagsStr
        String tagsStr = BaseUtils.getTags(patientBeans);
        sideBar.setIndexStr(tagsStr);
        decoration.setDatas(patientBeans, tagsStr);
    }

    /**
     * 列表侧边栏附表滚动
     */
    public void initEvents() {
        sideBar.setIndexChangeListener(tag -> {
            if (TextUtils.isEmpty(tag) || patientBeans.size() <= 0) { return; }
            for (int i = 0; i < patientBeans.size(); i++) {
                if (tag.equals(patientBeans.get(i).getIndexTag())) {
                    layoutManager.scrollToPositionWithOffset(i + 1, 0);
                    return;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.et_search_hospital:
                searchEdit.setFocusable(true);
                searchEdit.setFocusableInTouchMode(true);
                searchEdit.requestFocus();
                showSoftInputFromWindow(getContext(), searchEdit);
                reduce();
                break;
            case R.id.et_search_cancel:
                searchEdit.setFocusable(false);
                searchEdit.setFocusableInTouchMode(false);
                hideSoftInputFromWindow(getContext(), searchEdit);
                expand();
                break;
            default:
                break;
        }
    }

    private AutoTransition mSet;
    private int type = -1;

    private void expand() {
        type = 1;
        //设置伸展状态时的布局
        RelativeLayout.LayoutParams LayoutParams = (RelativeLayout.LayoutParams)layoutSearch.getLayoutParams();
        LayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        LayoutParams.setMargins(0, 0, 0, 0);
        layoutSearch.setLayoutParams(LayoutParams);
        //设置动画
        beginDelayedTransition(layoutSearch);
    }

    private void reduce() {
        type = 2;
        //设置收缩状态时的布局
        RelativeLayout.LayoutParams LayoutParams = (RelativeLayout.LayoutParams)layoutSearch.getLayoutParams();
        LayoutParams.setMargins(0, 0, BaseUtils.dp2px(getContext(), 50), 0);
        layoutSearch.setLayoutParams(LayoutParams);
        //设置动画
        beginDelayedTransition(layoutSearch);
    }

    void beginDelayedTransition(ViewGroup view) {
        if (type == 1) {
            tvCancel.setVisibility(View.GONE);
        }
        else {
            tvCancel.setVisibility(View.VISIBLE);
        }
        mSet = new AutoTransition();
        //设置动画持续时间
        mSet.setDuration(500);
        // 开始动画
        TransitionManager.beginDelayedTransition(view, mSet);
    }

    @Override
    public void OnDeleteClick() {
        layoutManager.scrollToPositionWithOffset(0, 0);
    }
}
