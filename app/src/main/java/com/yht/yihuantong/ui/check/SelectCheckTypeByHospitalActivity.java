package com.yht.yihuantong.ui.check;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

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
import com.yht.yihuantong.ui.adapter.SelectCheckTypeByHospitalAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.yht.yihuantong.ui.auth.fragment.AuthBaseFragment.REQUEST_CODE_HOSPITAL;

/**
 * @author 顿顿
 * @date 19/6/4 17:53
 * @des 选择医院下所有检查项目
 */
public class SelectCheckTypeByHospitalActivity extends BaseActivity
        implements BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.RequestLoadMoreListener {
    @BindView(R.id.et_search_check_type)
    SuperEditText etSearchCheckType;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.public_title_bar_more)
    TextView publicTitleBarMore;
    @BindView(R.id.public_title_bar_title)
    TextView publicTitleBarTitle;
    private SelectCheckTypeByHospitalAdapter selectAdapter;
    /**
     * 检查项列表
     */
    private List<SelectCheckTypeBean> selectCheckTypeBeans = new ArrayList<>();
    /**
     * 选中的检查项index
     */
    private ArrayList<Integer> selectPosition = new ArrayList<>();
    /**
     * 搜索关键字
     */
    private String searchKey;
    /**
     * 选中医院code
     */
    private String curHospitalCode;
    /**
     * 已经选中的检查项
     */
    private String selectCheckType;
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
        return R.layout.act_select_check_type_by_hospital;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        initTitle();
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        selectAdapter = new SelectCheckTypeByHospitalAdapter(R.layout.item_check_by_hospital_select,
                                                             selectCheckTypeBeans);
        selectAdapter.setLoadMoreView(new CustomLoadMoreView());
        selectAdapter.setOnLoadMoreListener(this, recyclerview);
        selectAdapter.setOnItemClickListener(this);
        recyclerview.setAdapter(selectAdapter);
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        if (getIntent() != null) {
            curHospitalCode = getIntent().getStringExtra(CommonData.KEY_HOSPITAL_CODE);
            selectCheckType = getIntent().getStringExtra(CommonData.KEY_PUBLIC);
        }
        getCheckTypeByHospitalList();
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
                    getCheckTypeByHospitalList();
                }
                else {
                    searchKey = "";
                    getCheckTypeByHospitalList();
                }
            }
        });
    }

    /**
     * 获取医院下检查项 全部
     */
    private void getCheckTypeByHospitalList() {
        RequestUtils.getCheckTypeByHospitalList(this, loginBean.getToken(), curHospitalCode, searchKey, selectCheckType,
                                                page, this);
    }

    /**
     * title处理
     */
    private void initTitle() {
        if (getIntent() != null) {
            String title = getIntent().getStringExtra(CommonData.KEY_TITLE);
            publicTitleBarTitle.setText(title);
        }
        //右边按钮
        publicTitleBarMore.setVisibility(View.VISIBLE);
        publicTitleBarMore.setText(R.string.txt_add);
    }

    @OnClick(R.id.public_title_bar_more)
    public void onViewClicked() {
        if (publicTitleBarMore.isSelected()) {
            hideSoftInputFromWindow(etSearchCheckType);
            Intent intent = new Intent();
            ArrayList<SelectCheckTypeBean> list = new ArrayList<>();
            for (Integer index : selectPosition) {
                list.add(selectCheckTypeBeans.get(index));
            }
            intent.putExtra(CommonData.KEY_RESERVE_CHECK_TYPE_LIST, list);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        if (task == Tasks.GET_CHECK_TYPE_BY_HOSPITAL) {
            List<SelectCheckTypeBean> list = (List<SelectCheckTypeBean>)response.getData();
            if (page == BaseData.BASE_ONE) {
                selectCheckTypeBeans.clear();
            }
            selectCheckTypeBeans.addAll(list);
            selectAdapter.setNewData(selectCheckTypeBeans);
            if (list != null && list.size() == BaseData.BASE_PAGE_DATA_NUM) {
                selectAdapter.loadMoreComplete();
            }
            else {
                selectAdapter.loadMoreEnd();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_HOSPITAL) {
            setResult(RESULT_OK, data);
            finish();
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Integer integer = position;
        if (selectPosition.contains(integer)) {
            selectPosition.remove(integer);
        }
        else {
            selectPosition.add(integer);
        }
        if (selectPosition.size() > 0) {
            publicTitleBarMore.setSelected(true);
        }
        else {
            publicTitleBarMore.setSelected(false);
        }
        selectAdapter.setSelectPosition(selectPosition);
        selectAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoadMoreRequested() {
        page++;
        getCheckTypeByHospitalList();
    }
}
