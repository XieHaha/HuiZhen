package com.yht.frame.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.yht.frame.R;
import com.yht.frame.api.notify.INotifyChangeListenerServer;
import com.yht.frame.data.BaseData;
import com.yht.frame.data.BaseNetConfig;
import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.CommonData;
import com.yht.frame.data.Tasks;
import com.yht.frame.data.base.LoginBean;
import com.yht.frame.http.listener.ResponseListener;
import com.yht.frame.permission.OnPermissionCallback;
import com.yht.frame.permission.Permission;
import com.yht.frame.permission.PermissionHelper;
import com.yht.frame.utils.HuiZhenLog;
import com.yht.frame.utils.SharePreferenceUtil;
import com.yht.frame.utils.StatusBarUtil;
import com.yht.frame.utils.ToastUtil;
import com.yht.frame.widgets.LoadViewHelper;
import com.yht.frame.widgets.dialog.HintDialog;
import com.yht.frame.widgets.dialog.LoadingDialog;

import butterknife.ButterKnife;

/**
 * activity基类
 *
 * @author DUNDUN
 */
public abstract class BaseActivity extends RxAppCompatActivity
        implements UiInterface, BaseData, ResponseListener<BaseResponse>, View.OnClickListener, OnPermissionCallback {
    public static final String TAG = "ZYC";
    /**
     * load view
     */
    private LoadingDialog loadingView;
    /**
     * 登录数据
     */
    protected LoginBean loginBean;
    /**
     * 轻量级存储
     */
    protected SharePreferenceUtil sharePreferenceUtil;
    protected LoadViewHelper loadViewHelper;
    /**
     * 监听器
     */
    public INotifyChangeListenerServer iNotifyChangeListenerServer;
    /**
     * 权限管理类
     */
    protected PermissionHelper permissionHelper;
    private boolean isRequest = true;
    private boolean isRequestPhone = true;
    private boolean isRequestCamera = true;
    private boolean isRequestRecord = true;
    /**
     * 返回按钮对象
     */
    protected ImageView backBtn;
    /**
     * 标题
     */
    private TextView tvTitle;

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getInstance().addActivity(this);
        befordCreateView(savedInstanceState);
        int layoutID = getLayoutID();
        if (layoutID != 0) {
            setContentView(layoutID);
        }
        else {
            setContentView(getLayoutView());
        }
        ButterKnife.bind(this);
        loginBean = getLoginBean();
        sharePreferenceUtil = new SharePreferenceUtil(this);
        /**
         * 权限管理类
         */
        permissionHelper = PermissionHelper.getInstance(this);
        init(savedInstanceState);
    }

    /**
     * 方法回调顺序
     * 1.initView
     * 3.initData
     * 4.initListener
     *
     * @param savedInstanceState
     */
    private void init(@NonNull Bundle savedInstanceState) {
        initBaseViews();
        if (isInitBackBtn()) {
            initBackBtn();
        }
        if (isInitStatusBar()) {
            initStatusBar();
        }
        initView(savedInstanceState);
        initData(savedInstanceState);
        initListener();
    }

    private void initBaseViews() {
        try {
            backBtn = findViewById(R.id.public_title_bar_back);
            tvTitle = findViewById(R.id.public_title_bar_title);
        }
        catch (Exception e) {
            HuiZhenLog.e(getClass().getSimpleName(), e.getMessage());
        }
    }

    /**
     * 初始化back按钮事件，及title名称赋值
     */
    private void initBackBtn() {
        try {
            backBtn.setVisibility(View.VISIBLE);
            backBtn.setOnClickListener(v -> {
                hideSoftInputFromWindow(v);
                finish();
            });
            tvTitle.setText(getTitle().toString());
        }
        catch (Exception e) {
            HuiZhenLog.e(getClass().getSimpleName(), e.getMessage());
        }
    }

    /**
     * 状态栏处理
     */
    private void initStatusBar() {
        StatusBarUtil.statuBarLightMode(this);
    }

    /**
     * 是否初始化返回按钮
     *
     * @return 如果不想baseactivity自动设置监听返回按钮的话就传回null，
     * 系统则不会自动设置监听,但是会初始化控件
     */
    protected boolean isInitBackBtn() {
        return false;
    }

    protected boolean isInitStatusBar() {
        return true;
    }

    private void initLoadingView() {
        loadingView = new LoadingDialog(this);
    }

    /**
     * 显示进度条
     */
    public void showLoadingView() {
        showLoadingView(true);
    }

    /**
     * 显示进度条
     *
     * @param cancel 是否可取消
     */
    public void showLoadingView(final boolean cancel) {
        runOnUiThread(() -> {
            if (loadingView == null) {
                initLoadingView();
            }
            loadingView.setCancelable(cancel);
            loadingView.setCanceledOnTouchOutside(cancel);
            if (!loadingView.isShowing()) {
                loadingView.show();
            }
        });
    }

    /**
     * 关闭进度条
     */
    public void closeLoadingView() {
        runOnUiThread(() -> {
            if (loadingView == null) {
                return;
            }
            if (!loadingView.isShowing()) {
                return;
            }
            loadingView.setCancelable(true);
            loadingView.setCanceledOnTouchOutside(true);
            loadingView.dismiss();
        });
    }

    /**
     * 初始化login数据
     *
     * @return
     */
    public LoginBean getLoginBean() {
        String userStr = (String)SharePreferenceUtil.getObject(this, CommonData.KEY_LOGIN_BEAN, "");
        if (!TextUtils.isEmpty(userStr)) {
            loginBean = new Gson().fromJson(userStr, LoginBean.class);
        }
        return loginBean;
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
     * 修改状态栏为全透明
     *
     * @param activity
     */
    @TargetApi(19)
    public void transparencyBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView()
                  .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = activity.getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * 隐藏软键盘
     */
    public void hideSoftInputFromWindow(View editText) {
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    /**
     * 打开软键盘
     */
    public void showSoftInputFromWindow(View editText) {
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
    }

    /**
     * 拨打电话（跳转到拨号界面，用户手动点击拨打）
     *
     * @param phoneNum 电话号码
     */
    public void callPhone(String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        startActivity(intent);
    }

    /**
     * 退出登录
     */
    public void exit() {
        Intent intent = new Intent(BaseData.BASE_SIGN_OUT_ACTION);
        intent.setPackage(getPackageName());
        sendBroadcast(intent);
    }

    /**
     * token失效
     */
    public void token() {
        Intent intent = new Intent(BaseData.BASE_TOKEN_ERROR_ACTION);
        intent.setPackage(getPackageName());
        sendBroadcast(intent);
    }

    /**
     * 得到返回按钮控件
     */
    public ImageView getBackBtnView() {
        return backBtn;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getInstance().removeActivity(this);
    }

    @Override
    public void onClick(View v) {
        onClick(v, v.getId());
    }

    /**
     * 单击回调
     *
     * @param v       点击的view
     * @param clickID 点击的控件id
     */
    public void onClick(View v, int clickID) {
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
    public void befordCreateView(@NonNull Bundle savedInstanceState) {
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        permissionHelper.request(new String[] { Permission.STORAGE_WRITE });
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
    }

    @Override
    public void initListener() {
    }

    @Override
    public void beforeCreateView(@NonNull Bundle savedInstanceState) {
    }

    /**
     * ==============================网络回调
     */
    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
    }

    @Override
    public void onResponseCode(Tasks task, BaseResponse response) {
        if (response.getCode() == BaseNetConfig.REQUEST_TOKEN_ERROR) {
            token();
        }
        else {
            ToastUtil.toast(this, response.getMsg());
        }
    }

    @Override
    public void onResponseError(Tasks task, Exception e) {
        ToastUtil.toast(this, e.getMessage());
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        if (permissions == null) {
            return;
        }
        for (String per : permissions) {
            if (Permission.STORAGE_WRITE.equals(per)) {
                permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
                return;
            }
            if (Permission.READ_PHONE_STATE.equals(per)) {
                permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
                return;
            }
            if (Permission.CAMERA.equals(per)) {
                permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
                return;
            }
            if (Permission.RECORD_AUDIO.equals(per)) {
                permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
                return;
            }
        }
    }

    @Override
    public void onPermissionGranted(@NonNull String[] permissionName) {
        onNoPermissionNeeded(permissionName);
    }

    @Override
    public void onPermissionDeclined(@NonNull String[] permissionName) {
        if (permissionName == null) {
            return;
        }
        for (String permission : permissionName) {
            if (Permission.STORAGE_WRITE.equals(permission)) {
                ToastUtil.toast(getApplicationContext(), R.string.dialog_no_storage_permission_tip);
                break;
            }
            if (Permission.READ_PHONE_STATE.equals(permission)) {
                ToastUtil.toast(getApplicationContext(), R.string.dialog_no_read_phone_state_tip);
                break;
            }
            if (Permission.CAMERA.equals(permission)) {
                ToastUtil.toast(getApplicationContext(), R.string.dialog_no_camera_permission_tip);
                break;
            }
            if (Permission.RECORD_AUDIO.equals(permission)) {
                ToastUtil.toast(getApplicationContext(), R.string.dialog_no_audio_permission_tip);
                break;
            }
        }
    }

    @Override
    public void onPermissionPreGranted(@NonNull String permissionsName) {
        onNoPermissionNeeded(permissionsName);
    }

    @Override
    public void onPermissionNeedExplanation(@NonNull String permissionName) {
        if (isRequest) {
            isRequest = false;
            permissionHelper.requestAfterExplanation(Permission.STORAGE_WRITE);
        }
        if (isRequestPhone) {
            isRequestPhone = false;
            permissionHelper.requestAfterExplanation(Permission.READ_PHONE_STATE);
        }
        if (isRequestCamera) {
            isRequestCamera = false;
            permissionHelper.requestAfterExplanation(Permission.CAMERA);
        }
        if (isRequestRecord) {
            isRequestRecord = false;
            permissionHelper.requestAfterExplanation(Permission.RECORD_AUDIO);
        }
    }

    @Override
    public void onPermissionReallyDeclined(@NonNull String permissionName) {
        HintDialog dialog = new HintDialog(this);
        dialog.setEnterBtnTxt(getString(R.string.txt_open));
        dialog.setEnterSelect(true);
        dialog.setOnEnterClickListener(() -> PermissionHelper.toPermissionSetting(getBaseContext()));
        switch (permissionName) {
            case Permission.STORAGE_WRITE:
                dialog.setContentString(getString(R.string.dialog_no_storage_permission_tip));
                break;
            case Permission.READ_PHONE_STATE:
                dialog.setContentString(getString(R.string.dialog_no_read_phone_state_tip));
                break;
            case Permission.CAMERA:
                dialog.setContentString(getString(R.string.dialog_no_camera_permission_tip));
                break;
            case Permission.RECORD_AUDIO:
                dialog.setContentString(getString(R.string.dialog_no_audio_permission_tip));
                break;
            default:
                break;
        }
        dialog.show();
    }

    @Override
    public void onNoPermissionNeeded(@NonNull Object permissionName) {
        isRequest = true;
        isRequestPhone = true;
        isRequestCamera = true;
        isRequestRecord = true;
    }
}
