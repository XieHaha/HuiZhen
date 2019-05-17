package com.yht.frame.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.Tasks;
import com.yht.frame.http.listener.ResponseListener;
import com.yht.frame.utils.ToastUtil;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author dundun
 */
public abstract class BaseFragment extends Fragment
        implements UiInterface, ResponseListener<BaseResponse>, View.OnClickListener {
    /**
     * 注解
     */
    protected Unbinder unbinder;

    @Override
    public final void onCreate(Bundle savedInstanceState) {
        beforeCreateView(savedInstanceState);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        int layoutID = getLayoutID();
        if (layoutID != 0) {
            view = inflater.inflate(getLayoutID(), null);
        }
        else {
            view = getLayoutView();
        }
        unbinder = ButterKnife.bind(this, view);
        init(view, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * 获取状态栏高度,在页面还没有显示出来之前
     *
     * @param a
     * @return
     */
    public static int getStateBarHeight(Activity a) {
        int result = 0;
        int resourceId = a.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = a.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 方法回调顺序
     * 1.initView
     * 2.initClss
     * 3.initData
     * 4.initListener
     *
     * @param savedInstanceState
     */
    private void init(@NonNull View view, @NonNull Bundle savedInstanceState) {
        initView(view, savedInstanceState);
        initObject(savedInstanceState);
        initData(savedInstanceState);
        initListener();
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AppManager.getInstance().removeActivity(getActivity());
    }

    /**
     * 默认不适用此方法，在子类里可以重构他
     */
    @Override
    public View getLayoutView() {
        return null;
    }
    //=====================setContentView 前回调

    @Override
    public void beforeCreateView(@NonNull Bundle savedInstanceState) {
    }

    public void initView(View view, @NonNull Bundle savedInstanceState) {
    }

    @Override
    public void initObject(@NonNull Bundle savedInstanceState) {
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
    }

    @Override
    public void initListener() {
    }

    @Override
    public void befordCreateView(@NonNull Bundle savedInstanceState) {
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
    }

    @Override
    public void onResponseError(Tasks task, Exception e) {
        ToastUtil.toast(getActivity(), e.getMessage());
    }

    @Override
    public void onResponseCode(Tasks task, BaseResponse response) {
    }

    @Override
    public void onResponseStart(Tasks task) {
    }

    @Override
    public void onResponseEnd(Tasks task) {
    }

    @Override
    public void onResponseCancel(Tasks task) {
    }
}
