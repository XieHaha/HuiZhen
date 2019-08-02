package com.yht.yihuantong.ui.auth;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yht.frame.api.ApiManager;
import com.yht.frame.api.notify.IChange;
import com.yht.frame.api.notify.RegisterType;
import com.yht.frame.data.BaseData;
import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.Tasks;
import com.yht.frame.data.bean.DoctorAuthBean;
import com.yht.frame.data.bean.LoginBean;
import com.yht.frame.data.type.DocAuthStatus;
import com.yht.frame.http.retrofit.RequestUtils;
import com.yht.frame.ui.AppManager;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.utils.SharePreferenceUtil;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ZycApplication;
import com.yht.yihuantong.jpush.TagAliasOperatorHelper;
import com.yht.yihuantong.ui.auth.fragment.AuthBaseFragment;
import com.yht.yihuantong.ui.auth.fragment.AuthLicenseFragment;
import com.yht.yihuantong.ui.auth.fragment.AuthResultFragment;
import com.yht.yihuantong.ui.auth.listener.OnAuthStepListener;
import com.yht.yihuantong.ui.main.MainActivity;

import butterknife.BindView;

import static com.yht.yihuantong.jpush.TagAliasOperatorHelper.ACTION_SET;

/**
 * @author 顿顿
 * @date 19/6/3 16:24
 * @des 医生认证
 */
public class AuthDoctorActivity extends BaseActivity implements OnAuthStepListener {
    @BindView(R.id.iv_base)
    ImageView ivAuthBase;
    @BindView(R.id.tv_base)
    TextView tvAuthBase;
    @BindView(R.id.layout_base)
    LinearLayout layoutAuthBase;
    @BindView(R.id.iv_two)
    ImageView ivAuthLicense;
    @BindView(R.id.tv_two)
    TextView tvAuthLicense;
    @BindView(R.id.layout_two)
    LinearLayout layoutAuthLicense;
    @BindView(R.id.iv_end)
    ImageView ivAuthResult;
    @BindView(R.id.tv_end)
    TextView tvAuthResult;
    @BindView(R.id.layout_end)
    LinearLayout layoutAuthResult;
    @BindView(R.id.view_base)
    View viewAuthBase;
    @BindView(R.id.view_two_left)
    View viewAuthLicenseLeft;
    @BindView(R.id.view_two_right)
    View viewAuthLicenseRight;
    @BindView(R.id.view_end)
    View viewAuthResult;
    /**
     * 碎片管理
     */
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    /**
     * 基本信息
     */
    private AuthBaseFragment authBaseFragment;
    /**
     * 医生执照
     */
    private AuthLicenseFragment authLicenseFragment;
    /**
     * 认证结果
     */
    private AuthResultFragment authResultFragment;
    /**
     * 认证数据
     */
    private DoctorAuthBean doctorAuthBean;
    /**
     * 当前碎片
     */
    private int curPage;
    /**
     * 当前审核状态
     */
    private int curAuthStatus = -1;
    /**
     * 是否为重新认证
     */
    private boolean authAgain;
    /**
     * 根据推送消息判断，
     */
    private boolean pushStatus;
    /**
     * 认证状态
     */
    private IChange<Integer> authStatus = data -> {
        if (data == DocAuthStatus.AUTH_FAILD) {
            pushStatus = true;
        }
        runOnUiThread(() -> getDoctorAuth());
    };

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_auth;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        iNotifyChangeListenerServer = ApiManager.getInstance().getServer();
        findViewById(R.id.public_title_bar_back).setOnClickListener(this);
        fragmentManager = getSupportFragmentManager();
        if (loginBean != null) {
            curAuthStatus = loginBean.getApprovalStatus();
            getDoctorAuth();
            setJPushAlias(loginBean.getDoctorCode());
        }
    }

    @Override
    public void initListener() {
        super.initListener();
        //注册监听
        iNotifyChangeListenerServer.registerDoctorAuthStatusChangeListener(authStatus, RegisterType.REGISTER);
    }

    /**
     * 提交医生认证
     */
    private void submitDoctorAuth() {
        RequestUtils.submitDoctorAuth(this, doctorAuthBean, loginBean.getToken(), loginBean.getMobile(), BaseData.ADMIN,
                                      this);
    }

    /**
     * 获取已提交的认证信息
     */
    private void getDoctorAuth() {
        RequestUtils.getDoctorAuth(this, loginBean.getToken(), loginBean.getMobile(), this);
    }

    /**
     * 极光alias推送设置
     *
     * @param alias
     */
    private void setJPushAlias(String alias) {
        TagAliasOperatorHelper.TagAliasBean tagAliasBean = new TagAliasOperatorHelper.TagAliasBean();
        tagAliasBean.action = ACTION_SET;
        tagAliasBean.alias = alias;
        tagAliasBean.isAliasAction = true;
        TagAliasOperatorHelper.getInstance().handleAction(getApplicationContext(), BASE_ONE, tagAliasBean);
    }

    private void tabAuthBaseView() {
        transaction = fragmentManager.beginTransaction();
        hideAll(transaction);
        if (authBaseFragment == null) {
            authBaseFragment = new AuthBaseFragment();
            authBaseFragment.setOnAuthStepListener(this);
            transaction.add(R.id.layout_frame_root, authBaseFragment);
        }
        else {
            transaction.show(authBaseFragment);
            authBaseFragment.onResume();
        }
        authBaseFragment.setDoctorAuthBean(doctorAuthBean);
        transaction.commitAllowingStateLoss();
        selectTab(BASE_ZERO);
    }

    private void tabAuthLicenseView() {
        transaction = fragmentManager.beginTransaction();
        hideAll(transaction);
        if (authLicenseFragment == null) {
            authLicenseFragment = new AuthLicenseFragment();
            authLicenseFragment.setOnAuthStepListener(this);
            transaction.add(R.id.layout_frame_root, authLicenseFragment);
        }
        else {
            transaction.show(authLicenseFragment);
            authLicenseFragment.onResume();
        }
        authLicenseFragment.setDoctorAuthBean(doctorAuthBean);
        transaction.commitAllowingStateLoss();
        selectTab(BASE_ONE);
    }

    private void tabAuthResultView() {
        setResult(RESULT_OK);
        transaction = fragmentManager.beginTransaction();
        hideAll(transaction);
        if (authResultFragment == null) {
            authResultFragment = new AuthResultFragment();
            authResultFragment.setOnAuthStepListener(this);
            transaction.add(R.id.layout_frame_root, authResultFragment);
        }
        else {
            transaction.show(authResultFragment);
            authResultFragment.onResume();
        }
        transaction.commitAllowingStateLoss();
        selectTab(BASE_TWO);
    }

    /**
     * 隐藏所有碎片
     */
    private void hideAll(FragmentTransaction transaction) {
        if (authBaseFragment != null) {
            transaction.hide(authBaseFragment);
        }
        if (authLicenseFragment != null) {
            transaction.hide(authLicenseFragment);
        }
        if (authResultFragment != null) {
            transaction.hide(authResultFragment);
        }
    }

    /**
     * 选中tab
     */
    private void selectTab(int index) {
        switch (index) {
            case BASE_ZERO:
                curPage = BASE_ZERO;
                //0
                tvAuthBase.setSelected(true);
                viewAuthBase.setSelected(false);
                ivAuthBase.setImageResource(R.mipmap.ic_step_sel);
                tvAuthBase.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                //1
                tvAuthLicense.setSelected(false);
                viewAuthLicenseLeft.setSelected(false);
                viewAuthLicenseRight.setSelected(false);
                ivAuthLicense.setImageResource(R.mipmap.ic_step_def);
                tvAuthLicense.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                //2
                layoutAuthResult.setSelected(false);
                ivAuthResult.setImageResource(R.mipmap.ic_step_def);
                tvAuthResult.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                break;
            case BASE_ONE:
                curPage = BASE_ONE;
                //0
                viewAuthBase.setSelected(true);
                tvAuthBase.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                ivAuthBase.setImageResource(R.mipmap.ic_step_finish);
                //1
                tvAuthLicense.setSelected(true);
                viewAuthLicenseLeft.setSelected(true);
                viewAuthLicenseRight.setSelected(false);
                ivAuthLicense.setImageResource(R.mipmap.ic_step_sel);
                tvAuthLicense.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                //2
                layoutAuthResult.setSelected(false);
                ivAuthResult.setImageResource(R.mipmap.ic_step_def);
                tvAuthResult.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                break;
            case BASE_TWO:
                curPage = BASE_TWO;
                //0
                viewAuthBase.setSelected(true);
                tvAuthBase.setSelected(true);
                tvAuthBase.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                ivAuthBase.setImageResource(R.mipmap.ic_step_finish);
                //1
                tvAuthLicense.setSelected(true);
                viewAuthLicenseLeft.setSelected(true);
                viewAuthLicenseRight.setSelected(true);
                tvAuthLicense.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                ivAuthLicense.setImageResource(R.mipmap.ic_step_finish);
                //2
                layoutAuthResult.setSelected(true);
                ivAuthResult.setImageResource(R.mipmap.ic_step_finish);
                tvAuthResult.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.public_title_bar_back) {
            if (finishPage()) {
                finish();
            }
        }
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        switch (task) {
            case SUBMIT_DOCTOR_AUTH:
                //提交成功后需要更新用户code、token
                LoginBean bean = (LoginBean)response.getData();
                loginBean.setToken(bean.getToken());
                loginBean.setDoctorCode(bean.getDoctorCode());
                //提交成功后需要更新本地认证状态
                loginBean.setApprovalStatus(DocAuthStatus.AUTH_WAITTING);
                ZycApplication.getInstance().setLoginBean(loginBean);
                //跳转到认证结果
                tabAuthResultView();
                break;
            case GET_DOCTOR_AUTH:
                doctorAuthBean = (DoctorAuthBean)response.getData();
                if (doctorAuthBean == null) { return; }
                //如果已经认证成功
                if (doctorAuthBean.getApprovalStatus() == DocAuthStatus.AUTH_SUCCESS) {
                    loginBean.setDoctorName(doctorAuthBean.getDoctorName());
                    loginBean.setDoctorCode(doctorAuthBean.getDoctorCode());
                    loginBean.setPhoto(doctorAuthBean.getDoctorPhoto());
                    loginBean.setMobile(doctorAuthBean.getDoctorPhone());
                    loginBean.setSex(doctorAuthBean.getDoctorSex());
                    loginBean.setJobTitle(doctorAuthBean.getJobTitle());
                    loginBean.setHospitalName(doctorAuthBean.getLastApplyHospitalName());
                    loginBean.setHospitalCode(doctorAuthBean.getLastApplyHospitalCode());
                    loginBean.setDepartmentName(doctorAuthBean.getLastApplyDepartmentName());
                    loginBean.setDoctorName(doctorAuthBean.getDoctorName());
                    loginBean.setApprovalStatus(doctorAuthBean.getApprovalStatus());
                    ZycApplication.getInstance().setLoginBean(loginBean);
                    AppManager.getInstance().finishAllActivity();
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    return;
                }
                else if (doctorAuthBean.getApprovalStatus() == DocAuthStatus.AUTH_FAILD) {
                    loginBean.setApprovalStatus(doctorAuthBean.getApprovalStatus());
                    loginBean.setRejectReason(doctorAuthBean.getApprovalRejectReason());
                    ZycApplication.getInstance().setLoginBean(loginBean);
                    if (authAgain && !pushStatus) {
                        tabAuthBaseView();
                    }
                    else {
                        pushStatus = false;
                        tabAuthResultView();
                    }
                }
                else if (doctorAuthBean.getApprovalStatus() == DocAuthStatus.AUTH_WAITTING) {
                    loginBean.setApprovalStatus(doctorAuthBean.getApprovalStatus());
                    loginBean.setRejectReason(doctorAuthBean.getApprovalRejectReason());
                    ZycApplication.getInstance().setLoginBean(loginBean);
                    tabAuthResultView();
                }
                else {
                    tabAuthBaseView();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onAuthOne(DoctorAuthBean bean) {
        this.doctorAuthBean = bean;
        tabAuthLicenseView();
    }

    @Override
    public void onAuthTwo(int type, DoctorAuthBean bean) {
        if (type == BASE_ONE) {
            tabAuthBaseView();
        }
        else {
            doctorAuthBean = bean;
            submitDoctorAuth();
        }
    }

    @Override
    public void onAuthThree() {
        //重新认证 去拉取已有的认证信息
        authAgain = true;
        getDoctorAuth();
    }

    /**
     * 页面逻辑处理
     *
     * @return
     */
    private boolean finishPage() {
        if (curPage == 1) {
            curPage = 0;
            tabAuthBaseView();
            return false;
        }
        //返回登录页面 清除登录信息
        SharePreferenceUtil.clear(this);
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (!finishPage()) {
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //注销监听
        iNotifyChangeListenerServer.registerDoctorAuthStatusChangeListener(authStatus, RegisterType.UNREGISTER);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        switch (curPage) {
            case BASE_ZERO:
                if (authBaseFragment != null) {
                    authBaseFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
                }
                break;
            case BASE_ONE:
                if (authLicenseFragment != null) {
                    authLicenseFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onPermissionNeedExplanation(@NonNull String permissionName) {
        switch (curPage) {
            case BASE_ZERO:
                if (authBaseFragment != null) {
                    authBaseFragment.onPermissionNeedExplanation(permissionName);
                }
                break;
            case BASE_ONE:
                if (authLicenseFragment != null) {
                    authLicenseFragment.onPermissionNeedExplanation(permissionName);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onNoPermissionNeeded(@NonNull Object permissionName) {
        switch (curPage) {
            case BASE_ZERO:
                if (authBaseFragment != null) {
                    authBaseFragment.onNoPermissionNeeded(permissionName);
                }
                break;
            case BASE_ONE:
                if (authLicenseFragment != null) {
                    authLicenseFragment.onNoPermissionNeeded(permissionName);
                }
                break;
            default:
                break;
        }
    }
}
