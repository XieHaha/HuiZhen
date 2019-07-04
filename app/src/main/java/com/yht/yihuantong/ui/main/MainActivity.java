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
import com.yht.frame.data.bean.PatientBean;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.utils.LogUtils;
import com.yht.frame.utils.ToastUtil;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ui.main.fragment.MessageFragment;
import com.yht.yihuantong.ui.main.fragment.PatientFragment;
import com.yht.yihuantong.ui.main.fragment.WorkerFragment;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author dundun
 */
public class MainActivity extends BaseActivity {
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

    @Override
    public int getLayoutID() {
        return R.layout.act_main;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        //状态栏透明
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        initTab();
        //环信登录
        loginEaseChat();
        //测试数据 存储
        savePatient();
    }

    List<PatientBean> patientBeans;

    private void savePatient() {
        patientBeans = new ArrayList<>();
        //数据存储
        for (int i = 0; i < 3; i++) {
            if (i == 0) {
                PatientBean bean = new PatientBean();
                bean.setPatientId("18408245131_d");
                bean.setName("测试名字1");
            }
            else {
                PatientBean bean = new PatientBean();
                bean.setPatientId("");
                bean.setName("");
            }
        }
        DataSupport.deleteAll(PatientBean.class);
        DataSupport.saveAll(patientBeans);
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
        EMClient.getInstance().login("15828456584_d", BaseData.BASE_EASE_DEFAULT_PWD, new EMCallBack() {
            @Override
            public void onSuccess() {
                runOnUiThread(() -> {
                    EMClient.getInstance().chatManager().loadAllConversations();
                    LogUtils.i(TAG, getString(R.string.txt_login_ease_success));
                });
            }

            @Override
            public void onProgress(int progress, String status) {
            }

            @Override
            public void onError(int code, String message) {
                LogUtils.i(TAG, getString(R.string.txt_login_ease_error));
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
