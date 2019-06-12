package com.zyc.doctor.ui.main.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yht.frame.data.bean.PatientBean;
import com.yht.frame.ui.BaseFragment;
import com.yht.frame.utils.BaseUtils;
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
public class PatientFragment extends BaseFragment {
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
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_bottom_space, null);
        patientAdapter.addHeaderView(headerView);
        patientAdapter.addFooterView(view);
        recyclerview.setAdapter(patientAdapter);
    }

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
}
