package com.yht.yihuantong.ui.main;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.yht.frame.data.BaseData;
import com.yht.frame.data.CommonData;
import com.yht.frame.data.bean.VersionBean;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.utils.HuiZhenLog;
import com.yht.frame.utils.ToastUtil;
import com.yht.yihuantong.R;
import com.yht.yihuantong.jpush.TagAliasOperatorHelper;
import com.yht.yihuantong.ui.dialog.UpdateDialog;
import com.yht.yihuantong.ui.main.fragment.MessageFragment;
import com.yht.yihuantong.ui.main.fragment.PatientFragment;
import com.yht.yihuantong.ui.main.fragment.WorkerFragment;
import com.yht.yihuantong.version.presenter.VersionPresenter;

import butterknife.BindView;

import static com.yht.yihuantong.jpush.TagAliasOperatorHelper.ACTION_SET;

/**
 * @author dundun
 */
public class MainActivity extends BaseActivity
        implements VersionPresenter.VersionViewListener, UpdateDialog.OnEnterClickListener {
    @BindView(R.id.act_main_tab1)
    LinearLayout actMainTab1;
    @BindView(R.id.act_main_tab3)
    LinearLayout actMainTab3;
    @BindView(R.id.act_main_tab2)
    LinearLayout actMainTab2;
    @BindView(R.id.tv_message)
    TextView tvMessage;
    @BindView(R.id.tv_patient)
    TextView tvPatient;
    @BindView(R.id.tv_worker)
    TextView tvWorker;
    /**
     * 碎片管理
     */
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    /**
     * 消息碎片
     */
    private MessageFragment messageFragment;
    /**
     * 工作室碎片
     */
    private WorkerFragment workerFragment;
    /**
     * 患者碎片
     */
    private PatientFragment patientFragment;
    /**
     * 版本检测
     */
    private VersionPresenter mVersionPresenter;
    /**
     * 版本弹窗
     */
    private UpdateDialog updateDialog;

    @Override
    public int getLayoutID() {
        return R.layout.act_main;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        //检查更新
        mVersionPresenter = new VersionPresenter(this, "");
        mVersionPresenter.setVersionViewListener(this);
        mVersionPresenter.init();
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        //状态栏透明
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        initTab();
        //环信登录
        loginEaseChat();
        setJPushAlias(loginBean.getDoctorCode());
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

    @Override
    public void initListener() {
        super.initListener();
        actMainTab1.setOnClickListener(this);
        actMainTab2.setOnClickListener(this);
        actMainTab3.setOnClickListener(this);
    }

    /**
     * 初始化tabs
     */
    private void initTab() {
        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        tabWorkerView();
    }

    /**
     * 登录环信聊天
     */
    private void loginEaseChat() {
        boolean isLogin = false;
        if (getIntent() != null) {
            isLogin = getIntent().getBooleanExtra(CommonData.KEY_EASE_LOGIN_STATUS, false);
        }
        //避免重复登录
        if (isLogin) {
            return;
        }
        EMClient.getInstance().login(loginBean.getDoctorCode(), BaseData.BASE_EASE_DEFAULT_PWD, new EMCallBack() {
            @Override
            public void onSuccess() {
                runOnUiThread(() -> HuiZhenLog.i(TAG, getString(R.string.txt_login_ease_success)));
            }

            @Override
            public void onProgress(int progress, String status) {
            }

            @Override
            public void onError(int code, String message) {
                HuiZhenLog.i(TAG, getString(R.string.txt_login_ease_error));
                ToastUtil.toast(MainActivity.this, R.string.txt_login_ease_error);
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.act_main_tab1:
                tabMessageView();
                break;
            case R.id.act_main_tab2:
                tabWorkerView();
                break;
            case R.id.act_main_tab3:
                tabPatientView();
                break;
            default:
                tabMessageView();
                break;
        }
    }

    private void tabMessageView() {
        transaction = fragmentManager.beginTransaction();
        hideAll(transaction);
        if (messageFragment == null) {
            messageFragment = new MessageFragment();
            transaction.add(R.id.act_main_tab_frameLayout, messageFragment);
        }
        else {
            transaction.show(messageFragment);
            messageFragment.onResume();
        }
        transaction.commitAllowingStateLoss();
        selectTab(0);
    }

    private void tabWorkerView() {
        transaction = fragmentManager.beginTransaction();
        hideAll(transaction);
        if (workerFragment == null) {
            workerFragment = new WorkerFragment();
            transaction.add(R.id.act_main_tab_frameLayout, workerFragment);
        }
        else {
            transaction.show(workerFragment);
            workerFragment.onResume();
        }
        transaction.commitAllowingStateLoss();
        selectTab(1);
    }

    private void tabPatientView() {
        transaction = fragmentManager.beginTransaction();
        hideAll(transaction);
        if (patientFragment == null) {
            patientFragment = new PatientFragment();
            transaction.add(R.id.act_main_tab_frameLayout, patientFragment);
        }
        else {
            transaction.show(patientFragment);
            patientFragment.onResume();
        }
        transaction.commitAllowingStateLoss();
        selectTab(2);
    }

    /**
     * 隐藏所有碎片
     */
    private void hideAll(FragmentTransaction transaction) {
        if (messageFragment != null) {
            transaction.hide(messageFragment);
        }
        if (workerFragment != null) {
            transaction.hide(workerFragment);
        }
        if (patientFragment != null) {
            transaction.hide(patientFragment);
        }
    }

    /**
     * 选中tab
     */
    private void selectTab(int index) {
        switch (index) {
            case 0:
                tvMessage.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                tvWorker.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                tvPatient.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                actMainTab1.setSelected(true);
                actMainTab2.setSelected(false);
                actMainTab3.setSelected(false);
                break;
            case 1:
                tvMessage.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                tvWorker.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                tvPatient.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                actMainTab1.setSelected(false);
                actMainTab2.setSelected(true);
                actMainTab3.setSelected(false);
                break;
            case 2:
                tvMessage.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                tvWorker.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                tvPatient.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                actMainTab1.setSelected(false);
                actMainTab2.setSelected(false);
                actMainTab3.setSelected(true);
                break;
            default:
                tvMessage.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                tvWorker.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                tvPatient.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                actMainTab1.setSelected(true);
                actMainTab2.setSelected(false);
                actMainTab3.setSelected(false);
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        if (workerFragment != null) {
            workerFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onPermissionNeedExplanation(@NonNull String permissionName) {
        if (workerFragment != null) {
            workerFragment.onPermissionNeedExplanation(permissionName);
        }
    }

    @Override
    public void onNoPermissionNeeded(@NonNull Object permissionName) {
        if (workerFragment != null) {
            workerFragment.onNoPermissionNeeded(permissionName);
        }
    }

    /*********************版本更新回调*************************/
    @Override
    public void updateVersion(VersionBean version, int mode, boolean isDownLoading) {
        if (mode == -1) {
            ToastUtil.toast(this, R.string.toast_version_update_hint);
            return;
        }
        updateDialog = new UpdateDialog(this);
        updateDialog.setCancelable(false);
        updateDialog.setUpdateMode(mode).setData(version.getNotes());
        updateDialog.setOnEnterClickListener(this);
        updateDialog.show();
    }

    @Override
    public void updateLoading(long total, long current) {
        if (updateDialog != null && updateDialog.isShowing()) {
            updateDialog.setProgressValue(total, current);
        }
    }

    /**
     * 当无可用网络时回调
     */
    @Override
    public void updateNetWorkError() {
    }

    @Override
    public void onEnter(boolean isMustUpdate) {
        mVersionPresenter.getNewAPK(isMustUpdate);
        ToastUtil.toast(this, R.string.txt_download_hint);
    }

    /**
     * 返回键 后台运行
     * 如果前一个activity未finish  会导致无法返回到后台
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
