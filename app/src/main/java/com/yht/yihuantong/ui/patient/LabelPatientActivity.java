package com.yht.yihuantong.ui.patient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lijiankun24.shadowlayout.ShadowLayout;
import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.CommonData;
import com.yht.frame.data.Tasks;
import com.yht.frame.data.bean.LabelBean;
import com.yht.frame.data.bean.PatientBean;
import com.yht.frame.http.retrofit.RequestUtils;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.widgets.dialog.HintDialog;
import com.yht.frame.widgets.recyclerview.SideBar;
import com.yht.frame.widgets.recyclerview.decoration.SideBarItemDecoration;
import com.yht.frame.widgets.recyclerview.loadview.CustomLoadMoreView;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ui.adapter.PatientAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author 顿顿
 * @date 19/5/17 14:55
 * @description 标签分组下的居民
 */
public class LabelPatientActivity extends BaseActivity
        implements BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemChildClickListener {
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.tv_none_patient)
    TextView tvNonePatient;
    @BindView(R.id.side_bar)
    SideBar sideBar;
    @BindView(R.id.tv_index)
    TextView tvIndex;
    @BindView(R.id.layout_index)
    ShadowLayout layoutIndex;
    @BindView(R.id.public_title_bar_title)
    TextView publicTitleBarTitle;
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
    private SideBarItemDecoration decoration;
    /**
     * tag显示
     */
    private Runnable mDelay;
    private LabelBean labelBean;
    /**
     * 所有居民数据
     */
    private List<PatientBean> patientBeans = new ArrayList<>();
    /**
     * 是否需要重新获取
     */
    private boolean isUpdate;
    /**
     * 标签刷新
     */
    public static final int REQUEST_CODE_UPDATE_LABEL = 100;

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_label_patient;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        recyclerview.setLayoutManager(layoutManager = new LinearLayoutManager(this));
        recyclerview.addItemDecoration(decoration = new SideBarItemDecoration(this));
        recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                //第一个可见的位置
                if (patientBeans != null && patientBeans.size() > 0) {
                    sideBar.setCurPosition(
                            patientBeans.get(layoutManager.findFirstVisibleItemPosition()).getIndexTag());
                }
            }
        });
        initEvents();
        initAdapter();
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        if (getIntent() != null) {
            labelBean = (LabelBean)getIntent().getSerializableExtra(CommonData.KEY_LABEL_BEAN);
            isUpdate = getIntent().getBooleanExtra(CommonData.KEY_INTENT_BOOLEAN, false);
        }
        if (labelBean != null) {
            publicTitleBarTitle.setText(labelBean.getTagName());
            if (isUpdate) {
                getPatientByLabel(labelBean.getId());
            }
            else {
                patientBeans = labelBean.getPatientList();
                sortData();
            }
        }
    }

    /**
     * 根据标签获取居民
     */
    private void getPatientByLabel(long tagId) {
        RequestUtils.getPatientByLabel(this, loginBean.getToken(), tagId, this);
    }

    /**
     * 适配器
     */
    private void initAdapter() {
        patientAdapter = new PatientAdapter(R.layout.item_patient, patientBeans);
        View space = getLayoutInflater().inflate(R.layout.view_space, null, false);
        patientAdapter.addHeaderView(space);
        patientAdapter.setOnItemClickListener(this);
        patientAdapter.setOnItemChildClickListener(this);
        patientAdapter.setLoadMoreView(new CustomLoadMoreView());
        recyclerview.setAdapter(patientAdapter);
    }

    /**
     * 对数据进行排序
     */
    private void sortData() {
        if (patientBeans != null && patientBeans.size() > 0) {
            recyclerview.setVisibility(View.VISIBLE);
            tvNonePatient.setVisibility(View.GONE);
        }
        else {
            recyclerview.setVisibility(View.GONE);
            tvNonePatient.setVisibility(View.VISIBLE);
            tvNonePatient.setText(R.string.txt_none_patient);
        }
        //对数据源进行排序
        BaseUtils.sortPatientData(patientBeans);
        //返回一个包含所有Tag字母在内的字符串并赋值给tagsStr
        String tagsStr = BaseUtils.getTags(patientBeans);
        sideBar.setIndexStr(tagsStr, 0);
        decoration.setDatas(patientBeans, tagsStr);
        //更新数据源
        patientAdapter.setNewData(patientBeans);
    }

    /**
     * 列表侧边栏附表滚动
     */
    public void initEvents() {
        sideBar.setIndexChangeListener(new SideBar.IndexChangeListener() {
            @Override
            public void indexChanged(String tag) {
                if (TextUtils.isEmpty(tag) || patientBeans.size() <= 0) { return; }
                for (int i = 0; i < patientBeans.size(); i++) {
                    if (tag.equals(patientBeans.get(i).getIndexTag())) {
                        layoutManager.scrollToPositionWithOffset(i + 1, 0);
                        return;
                    }
                }
            }

            @Override
            public void indexShow(float y, String tag, int position) {
                indexBarVisible(tag, true);
            }

            @Override
            public void indexHide() {
                if (mDelay != null) {
                    layoutIndex.removeCallbacks(mDelay);
                }
                layoutIndex.postDelayed(mDelay = () -> indexBarVisible("", false), 1000);
            }
        });
    }

    private void indexBarVisible(String text, boolean show) {
        if (show) {
            tvIndex.setText(text);
            layoutIndex.setVisibility(View.VISIBLE);
        }
        else {
            layoutIndex.setVisibility(View.GONE);
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Intent intent = new Intent(this, ChatContainerActivity.class);
        intent.putExtra(CommonData.KEY_CHAT_ID, patientBeans.get(position).getCode());
        intent.putExtra(CommonData.KEY_CHAT_NAME, patientBeans.get(position).getName());
        startActivityForResult(intent, REQUEST_CODE_UPDATE_LABEL);
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        new HintDialog(this).setPhone(getString(R.string.txt_contact_patient_phone),
                                      patientBeans.get(position).getMobile())
                            .setOnEnterClickListener(() -> callPhone(patientBeans.get(position).getMobile()))
                            .show();
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        if (task == Tasks.GET_PATIENT_BY_LABEL) {
            List<LabelBean> list = (List<LabelBean>)response.getData();
            if (list == null) {
                list = new ArrayList<>();
            }
            if (list.size() > 0) {
                patientBeans = list.get(0).getPatientList();
            }
            else {
                patientBeans = new ArrayList<>();
            }
            recyclerview.post(this::sortData);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_UPDATE_LABEL) {
            getPatientByLabel(labelBean.getId());
            setResult(RESULT_OK);
        }
    }
}
