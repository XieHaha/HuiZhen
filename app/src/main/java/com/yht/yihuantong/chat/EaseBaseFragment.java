package com.yht.yihuantong.chat;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.hyphenate.easeui.widget.EaseTitleBar;
import com.yht.frame.permission.OnPermissionCallback;
import com.yht.frame.permission.PermissionHelper;

/**
 * @author dundun
 */
public abstract class EaseBaseFragment extends Fragment implements OnPermissionCallback {
    protected EaseTitleBar titleBar;
    protected InputMethodManager inputMethodManager;
    /**
     * 动态权限
     */
    public PermissionHelper permissionHelper;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        inputMethodManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        //noinspection ConstantConditions
        titleBar = (EaseTitleBar)getView().findViewById(com.hyphenate.easeui.R.id.title_bar);
        permissionHelper = PermissionHelper.getInstance(getActivity());
        initView();
        setUpView();
    }

    public void showTitleBar() {
        if (titleBar != null) {
            titleBar.setVisibility(View.VISIBLE);
        }
    }

    public void hideTitleBar() {
        if (titleBar != null) {
            titleBar.setVisibility(View.GONE);
        }
    }

    protected void hideSoftKeyboard() {
        if (getActivity().getWindow().getAttributes().softInputMode !=
            WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getActivity().getCurrentFocus() != null) {
                inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                                                           InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    protected abstract void initView();

    protected abstract void setUpView();

    /**
     * 2019年7月1日18:26:58 增加
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onPermissionGranted(@NonNull String[] permissionName) {
    }

    @Override
    public void onPermissionDeclined(@NonNull String[] permissionName) {
    }

    @Override
    public void onPermissionPreGranted(@NonNull String permissionsName) {
    }

    @Override
    public void onPermissionNeedExplanation(@NonNull String permissionName) {
        permissionHelper.requestAfterExplanation(permissionName);
    }

    @Override
    public void onPermissionReallyDeclined(@NonNull String permissionName) {
    }

    @Override
    public void onNoPermissionNeeded(@NonNull Object permissionName) {
    }

    public boolean isSamePermission(String o, String n) {
        if (TextUtils.isEmpty(o) || TextUtils.isEmpty(n)) {
            return false;
        }
        if (o.equals(n)) {
            return true;
        }
        return false;
    }
}
