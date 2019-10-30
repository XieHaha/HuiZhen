package com.yht.yihuantong.ui.check;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yht.frame.api.ThreadPoolHelper;
import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.Tasks;
import com.yht.frame.data.bean.SelectCheckTypeBean;
import com.yht.frame.data.bean.SelectCheckTypeChildBean;
import com.yht.frame.data.bean.SelectCheckTypeParentBean;
import com.yht.frame.http.retrofit.RequestUtils;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.widgets.edittext.AbstractTextWatcher;
import com.yht.frame.widgets.edittext.SuperEditText;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ui.adapter.SelectCheckTypeParentAdapter;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/6/4 17:53
 * @des 医院选择
 */
public class SelectCheckTypeActivity extends BaseActivity implements SelectCheckTypeParentAdapter.OnSelectedCallback {
    @BindView(R.id.et_search_check_type)
    SuperEditText etSearchCheckType;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.tv_hospital_btn)
    TextView tvHospitalBtn;
    @BindView(R.id.layout_all_hospital)
    FrameLayout layoutAllHospital;
    @BindView(R.id.tv_service_btn)
    TextView tvServiceBtn;
    @BindView(R.id.layout_all_service)
    FrameLayout layoutAllService;
    @BindView(R.id.layout_filter)
    LinearLayout layoutFilter;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.hospital_recycler_view)
    RecyclerView hospitalRecyclerView;
    @BindView(R.id.tv_selected)
    TextView tvSelected;
    @BindView(R.id.tv_next)
    TextView tvNext;
    private SelectCheckTypeParentAdapter selectCheckTypeParentAdapter;
    /**
     * 医院数据
     */
    private List<SelectCheckTypeParentBean> selectCheckTypeParentBeans = new ArrayList<>();
    private Map<Integer, ArrayList<Integer>> selectedPositions;

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_select_check_type;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        selectCheckTypeParentAdapter = new SelectCheckTypeParentAdapter(R.layout.item_check_select_root,
                                                                        selectCheckTypeParentBeans);
        selectCheckTypeParentAdapter.setOnSelectedCallback(this);
        recyclerView.setAdapter(selectCheckTypeParentAdapter);
        getCheckTypeListByLocal();
        //本地没有数据
        if (selectCheckTypeParentBeans.size() == 0) {
            getCheckTypeList();
        }
    }

    @Override
    public void initListener() {
        super.initListener();
        etSearchCheckType.addTextChangedListener(new AbstractTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
            }
        });
    }

    /**
     * 获取本地数据
     */
    private void getCheckTypeListByLocal() {
        selectCheckTypeParentBeans = LitePal.findAll(SelectCheckTypeParentBean.class, true);
        selectCheckTypeParentAdapter.setNewData(selectCheckTypeParentBeans);
    }

    /**
     * 获取检查项 全部
     */
    private void getCheckTypeList() {
        RequestUtils.getCheckTypeList(this, loginBean.getToken(), this);
    }

    @OnClick({ R.id.tv_cancel, R.id.layout_all_hospital, R.id.layout_all_service, R.id.tv_selected, R.id.tv_next })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                finish();
                break;
            case R.id.layout_all_hospital:
                break;
            case R.id.layout_all_service:
                break;
            case R.id.tv_selected:
                break;
            case R.id.tv_next:
                break;
            default:
                break;
        }
    }

    @Override
    public void onSelected(Map<Integer, ArrayList<Integer>> positions) {
        selectedPositions = positions;
        if (positions.size() > 0) {
            tvNext.setSelected(true);
        }
        else { tvNext.setSelected(false); }
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        if (task == Tasks.GET_CHECK_TYPE) {
            List<SelectCheckTypeParentBean> list = (List<SelectCheckTypeParentBean>)response.getData();
            saveLocal(list);
            selectCheckTypeParentAdapter.setNewData(list);
        }
    }

    /**
     * 已选择的服务包、服务项数据
     */
    private void selectedServiceData() {
    }

    /**
     * 保存到数据库
     */
    private void saveLocal(List<SelectCheckTypeParentBean> list) {
        //保存数据
        ThreadPoolHelper.getInstance().execInSingle(() -> {
            LitePal.deleteAll(SelectCheckTypeParentBean.class);
            LitePal.deleteAll(SelectCheckTypeBean.class);
            LitePal.deleteAll(SelectCheckTypeChildBean.class);
            for (int i = 0; i < list.size(); i++) {
                SelectCheckTypeParentBean parentBean = list.get(i);
                ArrayList<SelectCheckTypeBean> oneBeans = parentBean.getProductPackageList();
                for (int j = 0; j < oneBeans.size(); j++) {
                    SelectCheckTypeBean oneBean = oneBeans.get(j);
                    List<SelectCheckTypeChildBean> twoBeans = oneBean.getProductInfoList();
                    if (twoBeans != null) {
                        List<SelectCheckTypeChildBean> newTwoBeans = new ArrayList<>();
                        for (int k = 0; k < twoBeans.size(); k++) {
                            SelectCheckTypeChildBean twoBean = twoBeans.get(k);
                            twoBean.setParentId(oneBean.getProjectCode());
                            newTwoBeans.add(twoBean);
                        }
                        LitePal.saveAll(newTwoBeans);
                    }
                }
                LitePal.saveAll(oneBeans);
            }
            LitePal.saveAll(list);
        });
    }
}