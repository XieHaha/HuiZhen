package com.yht.yihuantong.ui.auth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yht.frame.api.LitePalHelper;
import com.yht.frame.data.BaseData;
import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.CommonData;
import com.yht.frame.data.Tasks;
import com.yht.frame.data.bean.HospitalBean;
import com.yht.frame.http.retrofit.RequestUtils;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.utils.ToastUtil;
import com.yht.frame.widgets.edittext.AbstractTextWatcher;
import com.yht.frame.widgets.edittext.SuperEditText;
import com.yht.frame.widgets.recyclerview.loadview.CustomLoadMoreView;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ui.adapter.HospitalSelectAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;

import static com.yht.yihuantong.ui.auth.fragment.AuthBaseFragment.REQUEST_CODE_HOSPITAL;

/**
 * @author 顿顿
 * @date 19/6/4 17:53
 * @description 医院选择 认证
 */
public class SelectHospitalByAuthActivity extends BaseActivity
        implements BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.RequestLoadMoreListener,
                   SuperEditText.OnDeleteClickListener {
    @BindView(R.id.et_search_hospital)
    SuperEditText etSearchHospital;
    @BindView(R.id.rv_hospital)
    RecyclerView rvHospital;
    @BindView(R.id.tv_add_hospital_next)
    TextView tvAddHospitalNext;
    @BindView(R.id.layout_none_hospital)
    LinearLayout layoutNoneHospital;
    private HospitalSelectAdapter hospitalAdapter;
    /**
     * 搜索结果
     */
    private List<HospitalBean> searchHospitals;

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_select_hospital_by_auth;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        getHospitalListByAuth();
        rvHospital.setVisibility(View.GONE);
        rvHospital.setLayoutManager(new LinearLayoutManager(this));
        hospitalAdapter = new HospitalSelectAdapter(R.layout.item_hospital, searchHospitals);
        hospitalAdapter.setOnItemClickListener(this);
        hospitalAdapter.setLoadMoreView(new CustomLoadMoreView());
        rvHospital.setAdapter(hospitalAdapter);
    }

    @Override
    public void initListener() {
        super.initListener();
        //最大输入长度
        etSearchHospital.setFilters(new InputFilter[] { emojiFilter, new InputFilter.LengthFilter(20) });
        etSearchHospital.addTextChangedListener(new AbstractTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                String tag = s.toString().trim();
                if (TextUtils.isEmpty(tag)) {
                    rvHospital.setVisibility(View.GONE);
                }
                else {
                    searchHospital(tag);
                }
            }
        });
        etSearchHospital.setOnDeleteClickListener(this);
        rvHospital.setOnFlingListener(new RecyclerView.OnFlingListener() {
            @Override
            public boolean onFling(int velocityX, int velocityY) {
                hideSoftInputFromWindow();
                return false;
            }
        });
    }

    /**
     * 获取医院列表
     */
    private void getHospitalListByAuth() {
        RequestUtils.getHospitalListByAuth(this, loginBean.getToken(), this);
    }

    /**
     * 模糊搜索
     */
    private void searchHospital(String tag) {
        searchHospitals = LitePalHelper.findHospitals(tag);
        if (searchHospitals != null && searchHospitals.size() > 0) {
            rvHospital.setVisibility(View.VISIBLE);
            layoutNoneHospital.setVisibility(View.GONE);
            hospitalAdapter.setNewData(searchHospitals);
            if (searchHospitals.size() > BaseData.BASE_PAGE_DATA_NUM) {
                hospitalAdapter.loadMoreEnd();
            }
            else {
                hospitalAdapter.setEnableLoadMore(false);
            }
        }
        else {
            rvHospital.setVisibility(View.GONE);
            layoutNoneHospital.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.tv_add_hospital_next)
    public void onViewClicked() {
        Intent intent = new Intent(this, AddInfoActivity.class);
        intent.putExtra(CommonData.KEY_PUBLIC_STRING, etSearchHospital.getText().toString().trim());
        startActivityForResult(intent, REQUEST_CODE_HOSPITAL);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        hideSoftInputFromWindow();
        Intent intent = new Intent();
        intent.putExtra(CommonData.KEY_HOSPITAL_BEAN, searchHospitals.get(position));
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        if (task == Tasks.GET_HOSPITAL_LIST_BY_AUTH) {
            ArrayList<HospitalBean> hospitals = (ArrayList<HospitalBean>)response.getData();
            //更新数据库
            new LitePalHelper<HospitalBean>().updateAll(hospitals, HospitalBean.class);
        }
    }

    @Override
    public void onDeleteClick() {
        layoutNoneHospital.setVisibility(View.GONE);
        rvHospital.setVisibility(View.GONE);
    }

    /**
     * 加载更多
     */
    @Override
    public void onLoadMoreRequested() {
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

    InputFilter emojiFilter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            Matcher emojiMatcher = emoji.matcher(source);
            if (emojiMatcher.find()) {
                ToastUtil.toast(SelectHospitalByAuthActivity.this, com.yht.frame.R.string.txt_not_support_input);
                return "";
            }
            return null;
        }
        Pattern emoji = Pattern.compile(BaseUtils.filterImoji, Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
    };
}
