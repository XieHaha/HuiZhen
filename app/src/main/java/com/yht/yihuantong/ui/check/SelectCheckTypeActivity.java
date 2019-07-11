package com.yht.yihuantong.ui.check;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.yht.frame.api.LitePalHelper;
import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.CommonData;
import com.yht.frame.data.Tasks;
import com.yht.frame.data.base.SelectCheckTypeBean;
import com.yht.frame.http.retrofit.RequestUtils;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.widgets.edittext.AbstractTextWatcher;
import com.yht.frame.widgets.edittext.SuperEditText;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ui.adapter.CheckTypeSelectAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author 顿顿
 * @date 19/6/4 17:53
 * @des 医院选择
 */
public class SelectCheckTypeActivity extends BaseActivity implements CheckTypeSelectAdapter.OnCheckItemClickListener {
    @BindView(R.id.et_search_check_type)
    SuperEditText etSearchCheckType;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    private CheckTypeSelectAdapter checkTypeSelectAdapter;
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
        checkTypeSelectAdapter = new CheckTypeSelectAdapter(this, recyclerview);
        checkTypeSelectAdapter.setDatas(selectCheckTypeBeans);
        checkTypeSelectAdapter.setOnCheckItemClickListener(this);
        recyclerview.setAdapter(checkTypeSelectAdapter);
        getCheckTypeList();
    }

    @Override
    public void initListener() {
        super.initListener();
        etSearchCheckType.addTextChangedListener(new AbstractTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                if (!TextUtils.isEmpty(s)) {
                    searchKey = s.toString();
                    //搜索查询
                    getCheckTypeList();
                }
                else {
                    searchKey = "";
                    getCheckTypeList();
                }
            }
        });
    }

    /**
     * 获取检查项 全部
     */
    private void getCheckTypeList() {
        RequestUtils.getCheckTypeList(this, token, code, searchKey, page, this);
    }

    @Override
    public void onCheckItemClick(int parentPosition, int position) {
        curSelectCheckTypeBean = selectCheckTypeBeans.get(parentPosition);
        Intent intent = new Intent();
        intent.putExtra(CommonData.KEY_RESERVE_CHECK_TYPE_BEAN, curSelectCheckTypeBean);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        if (task == Tasks.GET_CHECK_TYPE) {
            selectCheckTypeBeans = (List<SelectCheckTypeBean>)response.getData();
            checkTypeSelectAdapter.setDatas(selectCheckTypeBeans);
            new LitePalHelper().updateAll(selectCheckTypeBeans, SelectCheckTypeBean.class);
        }
    }
}