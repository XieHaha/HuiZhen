package com.zyc.doctor.ui.main.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yht.frame.data.bean.PatientBean;
import com.yht.frame.ui.BaseFragment;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.widgets.edittext.SuperEditText;
import com.yht.frame.widgets.recyclerview.ControlScrollLayoutManager;
import com.yht.frame.widgets.recyclerview.IndexBar;
import com.yht.frame.widgets.recyclerview.SideBar;
import com.yht.frame.widgets.recyclerview.decoration.CustomItemDecoration;
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
public class PatientFragment extends BaseFragment
        implements SuperEditText.OnDeleteClickListener, View.OnFocusChangeListener,
                   BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemChildClickListener {
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
    private ControlScrollLayoutManager layoutManager;
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
        recyclerview.setLayoutManager(layoutManager = new ControlScrollLayoutManager(getContext(), recyclerview));
        layoutManager.setCanAutoScroll(true);
        recyclerview.addItemDecoration(decoration = new CustomItemDecoration(getContext()));
        initData();
        initAdapter();
    }

    /**
     * 适配器
     */
    private void initAdapter() {
        patientAdapter = new PatientAdapter(R.layout.item_patient, patientBeans);
        patientAdapter.setOnItemClickListener(this);
        patientAdapter.setOnItemChildClickListener(this);
        View headerView = LayoutInflater.from(getContext()).inflate(R.layout.view_patient_header, null);
        initHeaderView(headerView);
        patientAdapter.addHeaderView(headerView);
        View footerView = LayoutInflater.from(getContext()).inflate(R.layout.view_bottom_space, null);
        patientAdapter.addFooterView(footerView);
        recyclerview.setAdapter(patientAdapter);
    }

    /**
     * 头部搜索框
     *
     * @param headerView
     */
    private void initHeaderView(View headerView) {
        layoutSearch = headerView.findViewById(R.id.layout_search);
        searchEdit = headerView.findViewById(R.id.et_search_patient);
        tvCancel = headerView.findViewById(R.id.et_search_cancel);
        //让EditText失去焦点，然后获取点击事件
        searchEdit.setOnFocusChangeListener(this);
        searchEdit.setOnDeleteClickListener(this);
        searchEdit.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
    }

    private void initData() {
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
            case R.id.et_search_patient:
                searchEdit.setFocusable(true);
                searchEdit.setFocusableInTouchMode(true);
                searchEdit.requestFocus();
                showSoftInputFromWindow(getContext(), searchEdit);
                reduce();
                break;
            case R.id.et_search_cancel:
                searchEdit.setFocusable(false);
                searchEdit.setFocusableInTouchMode(false);
                searchEdit.setText("");
                hideSoftInputFromWindow(getContext(), searchEdit);
                expand();
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:15828456584");
        intent.setData(data);
        startActivity(intent);
    }

    private AutoTransition mSet;
    private int type = -1;

    private void expand() {
        type = 1;
        //设置伸展状态时的布局
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)layoutSearch.getLayoutParams();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.setMargins(0, 0, 0, 0);
        layoutSearch.setLayoutParams(params);
        //设置动画
        beginDelayedTransition(layoutSearch);
    }

    private void reduce() {
        type = 2;
        //设置收缩状态时的布局
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)layoutSearch.getLayoutParams();
        params.setMargins(0, 0, BaseUtils.dp2px(getContext(), 50), 0);
        layoutSearch.setLayoutParams(params);
        //设置动画
        beginDelayedTransition(layoutSearch);
    }

    void beginDelayedTransition(ViewGroup view) {
        if (type == 1) {
            tvCancel.setVisibility(View.GONE);
        }
        else {
            tvCancel.postDelayed(() -> tvCancel.setVisibility(View.VISIBLE), 500);
        }
        mSet = new AutoTransition();
        //设置动画持续时间
        mSet.setDuration(500);
        // 开始动画
        TransitionManager.beginDelayedTransition(view, mSet);
    }

    @Override
    public void onDeleteClick() {
        layoutManager.scrollToPositionWithOffset(0, 0);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (layoutManager == null) { return; }
        if (hasFocus) {
            layoutManager.setCanAutoScroll(false);
        }
        else {
            layoutManager.setCanAutoScroll(true);
        }
    }
}
