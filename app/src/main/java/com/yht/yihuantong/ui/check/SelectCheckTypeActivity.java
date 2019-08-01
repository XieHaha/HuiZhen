package com.yht.yihuantong.ui.check;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yht.frame.data.BaseData;
import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.CommonData;
import com.yht.frame.data.Tasks;
import com.yht.frame.data.bean.SelectCheckTypeBean;
import com.yht.frame.http.retrofit.RequestUtils;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.widgets.edittext.AbstractTextWatcher;
import com.yht.frame.widgets.edittext.SuperEditText;
import com.yht.frame.widgets.recyclerview.loadview.CustomLoadMoreView;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ui.adapter.SelectCheckTypeAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author 顿顿
 * @date 19/6/4 17:53
 * @des 医院选择
 */
public class SelectCheckTypeActivity extends BaseActivity
        implements BaseQuickAdapter.RequestLoadMoreListener, BaseQuickAdapter.OnItemClickListener {
    @BindView(R.id.et_search_check_type)
    SuperEditText etSearchCheckType;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    private SelectCheckTypeAdapter selectCheckTypeAdapter;
    private List<SelectCheckTypeBean> selectCheckTypeBeans = new ArrayList<>();
    /**
     * 当前选中的检查项
     */
    private SelectCheckTypeBean curSelectCheckTypeBean;
    /**
     * 搜索key
     */
    private String searchKey;
    /**
     * 页码
     */
    private int page = 1;

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
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        selectCheckTypeAdapter = new SelectCheckTypeAdapter(R.layout.item_check_select, selectCheckTypeBeans);
        selectCheckTypeAdapter.setOnItemClickListener(this);
        selectCheckTypeAdapter.setLoadMoreView(new CustomLoadMoreView());
        selectCheckTypeAdapter.setOnLoadMoreListener(this, recyclerview);
        recyclerview.setAdapter(selectCheckTypeAdapter);
        getCheckTypeList();
    }

    @Override
    public void initListener() {
        super.initListener();
        etSearchCheckType.addTextChangedListener(new AbstractTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                page = 1;
                searchKey = s.toString().trim();
                getCheckTypeList();
            }
        });
    }

    /**
     * 获取检查项 全部
     */
    private void getCheckTypeList() {
        RequestUtils.getCheckTypeList(this, loginBean.getToken(), loginBean.getDoctorCode(), searchKey, page, this);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        hideSoftInputFromWindow();
        curSelectCheckTypeBean = selectCheckTypeBeans.get(position);
        Intent intent = new Intent();
        intent.putExtra(CommonData.KEY_RESERVE_CHECK_TYPE_BEAN, curSelectCheckTypeBean);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        if (task == Tasks.GET_CHECK_TYPE) {
            List<SelectCheckTypeBean> list = (List<SelectCheckTypeBean>)response.getData();
            if (list == null) {
                list = new ArrayList<>();
            }
            if (page == BaseData.BASE_ONE) {
                selectCheckTypeBeans.clear();
            }
            selectCheckTypeBeans.addAll(list);
            selectCheckTypeAdapter.setNewData(selectCheckTypeBeans);
            if (list.size() >= 20) {
                selectCheckTypeAdapter.loadMoreComplete();
            }
            else {
                selectCheckTypeAdapter.loadMoreEnd();
            }
        }
    }

    @Override
    public void onLoadMoreRequested() {
        page++;
        getCheckTypeList();
    }
}