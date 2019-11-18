package com.yht.yihuantong.ui.reservation.service;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;
import com.yht.frame.data.BaseNetConfig;
import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.CommonData;
import com.yht.frame.data.bean.HealthPackageBean;
import com.yht.frame.data.bean.HealthPackageDetailBean;
import com.yht.frame.data.bean.PatientBean;
import com.yht.frame.data.bean.ProductBean;
import com.yht.frame.data.bean.ReserveCheckBean;
import com.yht.frame.data.bean.ReserveCheckTypeBean;
import com.yht.frame.data.bean.SelectCheckTypeBean;
import com.yht.frame.data.bean.SelectCheckTypeChildBean;
import com.yht.frame.data.bean.SelectCheckTypeParentBean;
import com.yht.frame.data.bean.ServiceSubmitErrorBean;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.utils.HuiZhenLog;
import com.yht.frame.utils.ToastUtil;
import com.yht.frame.widgets.dialog.HintDialog;
import com.yht.frame.widgets.dialog.ErrorServiceListDialog;
import com.yht.yihuantong.BuildConfig;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ZycApplication;
import com.yht.yihuantong.ui.check.listener.OnCheckListener;
import com.yht.yihuantong.ui.reservation.ReservationSuccessActivity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;

/**
 * @author 顿顿
 * @date 19/6/14 14:05
 * @des 新增 预约检查 预约转诊
 */
public class ReservationServiceActivity extends BaseActivity implements OnCheckListener {
    @BindView(R.id.iv_base)
    ImageView ivReservationBase;
    @BindView(R.id.tv_base)
    TextView tvReservationBase;
    @BindView(R.id.layout_base)
    LinearLayout layoutReservationBase;
    @BindView(R.id.iv_two)
    ImageView ivReservationMaterial;
    @BindView(R.id.tv_two)
    TextView tvReservationMaterial;
    @BindView(R.id.layout_two)
    LinearLayout layoutReservationMaterial;
    @BindView(R.id.iv_end)
    ImageView ivReservationResult;
    @BindView(R.id.tv_end)
    TextView tvReservationResult;
    @BindView(R.id.layout_end)
    LinearLayout layoutReservationResult;
    @BindView(R.id.view_base)
    View viewReservationBase;
    @BindView(R.id.view_two_left)
    View viewReservationLicenseLeft;
    @BindView(R.id.view_two_right)
    View viewReservationLicenseRight;
    @BindView(R.id.view_end)
    View viewReservationResult;
    @BindView(R.id.public_title_bar_more)
    TextView publicTitleBarMore;
    @BindView(R.id.layout_frame_root)
    FrameLayout layoutFrameRoot;
    /**
     * 碎片管理
     */
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    /**
     * 基本信息
     */
    private ServiceIdentifyFragment identifyFragment;
    /**
     * 完善资料
     */
    private ServiceMaterialFragment materialFragment;
    /**
     * 确认提交(预约检查)
     */
    private ServiceSubmitFragment submitCheckFragment;
    /**
     * 居民回填数据
     */
    private PatientBean patientBean;
    /**
     * 当前预约检查数据
     */
    private ReserveCheckBean reserveCheckBean;
    /**
     * 医院code
     */
    private HealthPackageBean healthPackageBean;
    /**
     * 服务包详情 (预约该服务)
     */
    private HealthPackageDetailBean healthPackageDetailBean;
    /**
     * 医院数据  (预约该服务)
     */
    private SelectCheckTypeParentBean parentBean;
    /**
     * 当前碎片
     */
    private int curPage;

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_reservation_check;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        fragmentManager = getSupportFragmentManager();
        if (getIntent() != null) {
            //居民详情页面回传数据
            patientBean = (PatientBean)getIntent().getSerializableExtra(CommonData.KEY_PATIENT_BEAN);
            healthPackageDetailBean = (HealthPackageDetailBean)getIntent().getSerializableExtra(
                    CommonData.KEY_HOSPITAL_PRODUCT_BEAN);
            healthPackageBean = (HealthPackageBean)getIntent().getSerializableExtra(CommonData.KEY_HOSPITAL_BEAN);
        }
        initTitlePage();
        //预约该服务所选数据
        initServiceData();
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        if (hasHistoryData()) {
            //直接进入到第二步
            tabReservationLicenseView();
        }
        else {
            tabReservationBaseView();
        }
    }

    /**
     * 预约该服务
     */
    private void initServiceData() {
        if (healthPackageDetailBean != null && healthPackageBean != null) {
            //服务包
            SelectCheckTypeBean selectCheckTypeBean = new SelectCheckTypeBean();
            selectCheckTypeBean.setPrice(healthPackageDetailBean.getSuggestPrice());
            selectCheckTypeBean.setProjectCode(healthPackageDetailBean.getPackageCode());
            selectCheckTypeBean.setProjectName(healthPackageDetailBean.getPackageName());
            //服务包下服务项
            ArrayList<SelectCheckTypeChildBean> childList = new ArrayList<>();
            ArrayList<ProductBean> oldList = healthPackageDetailBean.getProductInfoList();
            if (oldList != null) {
                for (ProductBean productBean : oldList) {
                    SelectCheckTypeChildBean childBean = new SelectCheckTypeChildBean();
                    childBean.setProductCode(productBean.getProductCode());
                    childBean.setProductName(productBean.getProductName());
                    childBean.setProductCount(String.valueOf(productBean.getCount()));
                    childList.add(childBean);
                }
            }
            selectCheckTypeBean.setProductInfoList(childList);
            ArrayList<SelectCheckTypeBean> list = new ArrayList<>();
            list.add(selectCheckTypeBean);
            //医院数据
            parentBean = new SelectCheckTypeParentBean();
            parentBean.setHospitalCode(healthPackageBean.getHospitalCode());
            parentBean.setHospitalName(healthPackageBean.getHospitalName());
            parentBean.setProductPackageList(list);
        }
    }

    /**
     * 新增预约检查订单
     */
    private void addReserveCheckOrder() {
        RequestQueue queue = NoHttp.getRequestQueueInstance();
        final Request<String> request = NoHttp.createStringRequest(BuildConfig.BASE_BASIC_URL + "order-check/create",
                                                                   RequestMethod.POST);
        String params = new Gson().toJson(reserveCheckBean);
        request.setDefineRequestBodyForJson(params);
        HuiZhenLog.i("HTTP", "params:" + params);
        request.addHeader("token", loginBean.getToken());
        request.setConnectTimeout(30000);
        request.setReadTimeout(30000);
        queue.add(1, request, new OnResponseListener<String>() {
            @Override
            public void onStart(int what) {
                showLoadingView();
            }

            @Override
            public void onSucceed(int what, Response<String> response) {
                String s = response.get();
                submitSuccess(s);
            }

            @Override
            public void onFailed(int what, Response<String> response) {
                closeLoadingView();
            }

            @Override
            public void onFinish(int what) {
                closeLoadingView();
            }
        });
    }

    private void submitSuccess(String s) {
        HuiZhenLog.i("HTTP", "response:" + s);
        // 动态生成所需的java类的类型
        Type type = new TypeToken<BaseResponse<List<ServiceSubmitErrorBean>>>() { }.getType();
        Gson gson = new Gson();
        BaseResponse<List<ServiceSubmitErrorBean>> baseResponse;
        //获取code
        JsonObject jsonObject = new JsonParser().parse(s).getAsJsonObject();
        int code = jsonObject.get("code").getAsInt();
        if (code == BaseNetConfig.REQUEST_SUCCESS) {
            //提交成功需要保存最近使用的服务项
            saveRecentlyUsedService();
            startActivity(new Intent(this, ReservationSuccessActivity.class));
            finish();
        }
        //服务项、服务包状态错误
        else if (code == BaseNetConfig.REQUEST_SUBMIT_SERVICE_STATUS_ERROR) {
            baseResponse = gson.fromJson(s, type);
            showSubmitErrorDialog(baseResponse.getData(), true);
        }
        //服务项、服务包价格错误
        else if (code == BaseNetConfig.REQUEST_SUBMIT_SERVICE_PRICE_ERROR) {
            baseResponse = gson.fromJson(s, type);
            showSubmitErrorDialog(baseResponse.getData(), false);
        }
        //token失效
        else if (code == BaseNetConfig.REQUEST_TOKEN_ERROR) {
            token(jsonObject.get("msg").getAsString());
        }
        //账号禁用
        else if (code == BaseNetConfig.REQUEST_ACCOUNT_ERROR) {
            accountError();
        }
        else {
            ToastUtil.toast(this, jsonObject.get("msg").getAsString());
        }
    }

    private void showSubmitErrorDialog(List<ServiceSubmitErrorBean> list, boolean hideRight) {
        ErrorServiceListDialog listDialog = new ErrorServiceListDialog(this);
        if (hideRight) {
            listDialog.setContentString(getString(R.string.txt_service_submit_status_error)).setHideRight(true);
        }
        else {
            listDialog.setContentString(getString(R.string.txt_service_submit_price_error));
        }
        listDialog.setData(list).setOnNextClickListener(new ErrorServiceListDialog.OnNextClickListener() {
            @Override
            public void onLeftClick() {
                if (submitCheckFragment != null) {
                    submitCheckFragment.reselect();
                }
            }

            @Override
            public void onRightClick() {
                continueSubmit(list);
            }
        }).show();
    }

    /**
     * 保存最近使用的服务项 服务包
     */
    private void saveRecentlyUsedService() {
        Set<String> localData = sharePreferenceUtil.getStringSet(CommonData.KEY_RECENTLY_USED_SERVICE);
        if (localData != null) {
            localData = new HashSet<>(localData);
        }
        else {
            localData = new HashSet<>();
        }
        List<String> codes = ZycApplication.getInstance().getSelectCodes();
        localData.addAll(codes);
        sharePreferenceUtil.putStringSet(CommonData.KEY_RECENTLY_USED_SERVICE, localData);
        //清除临时数据
        ZycApplication.getInstance().clearSelectCodes();
    }

    /**
     * title处理
     */
    private void initTitlePage() {
        //自定义返回键实践处理
        findViewById(R.id.public_title_bar_back).setOnClickListener(this);
        publicTitleBarMore.setVisibility(View.VISIBLE);
        publicTitleBarMore.setText(R.string.txt_contact);
        publicTitleBarMore.setOnClickListener(this);
        tvReservationBase.setText(R.string.txt_identify);
        tvReservationMaterial.setText(R.string.txt_material);
        tvReservationResult.setText(R.string.txt_sure_submit);
    }

    /**
     * 已有数据回填（包括居民基本数据或者订单数据）
     * 返回true  表示有数据需要回填
     */
    private boolean hasHistoryData() {
        if (patientBean != null) {
            initPatientBaseData();
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * 居民基本数据回填 (从居民详情页进入)
     */
    private void initPatientBaseData() {
        //预约检查
        reserveCheckBean = new ReserveCheckBean();
        reserveCheckBean.setPatientName(patientBean.getName());
        reserveCheckBean.setIdCardNo(patientBean.getIdCard());
        reserveCheckBean.setPatientCode(patientBean.getCode());
        reserveCheckBean.setSex(patientBean.getSex());
        reserveCheckBean.setAge(patientBean.getAge());
        reserveCheckBean.setPhone(patientBean.getMobile());
        reserveCheckBean.setPastHistory(patientBean.getPast());
        reserveCheckBean.setFamilyHistory(patientBean.getFamily());
        reserveCheckBean.setAllergyHistory(patientBean.getAllergy());
        reserveCheckBean.setIsBind(patientBean.getIsBind());
    }

    private void tabReservationBaseView() {
        fragmentTransaction = fragmentManager.beginTransaction();
        hideAll(fragmentTransaction);
        if (identifyFragment == null) {
            identifyFragment = new ServiceIdentifyFragment();
            identifyFragment.setOnCheckListener(this);
            identifyFragment.setReserveCheckBean(reserveCheckBean);
            fragmentTransaction.add(R.id.layout_frame_root, identifyFragment);
        }
        else {
            fragmentTransaction.show(identifyFragment);
            identifyFragment.setReserveCheckBean(reserveCheckBean);
            identifyFragment.onResume();
        }
        fragmentTransaction.commitAllowingStateLoss();
        selectTab(BASE_ZERO);
    }

    private void tabReservationLicenseView() {
        fragmentTransaction = fragmentManager.beginTransaction();
        hideAll(fragmentTransaction);
        if (materialFragment == null) {
            materialFragment = new ServiceMaterialFragment();
            materialFragment.setOnCheckListener(this);
            materialFragment.setReserveCheckBean(reserveCheckBean);
            fragmentTransaction.add(R.id.layout_frame_root, materialFragment);
        }
        else {
            fragmentTransaction.show(materialFragment);
            materialFragment.setReserveCheckBean(reserveCheckBean);
            materialFragment.onResume();
        }
        fragmentTransaction.commitAllowingStateLoss();
        selectTab(BASE_ONE);
    }

    /**
     * 预约检查提交碎片
     */
    private void tabCheckResultView() {
        fragmentTransaction = fragmentManager.beginTransaction();
        hideAll(fragmentTransaction);
        if (submitCheckFragment == null) {
            submitCheckFragment = new ServiceSubmitFragment();
            submitCheckFragment.setOnCheckListener(this);
            submitCheckFragment.setReserveCheckBean(reserveCheckBean);
            if (parentBean != null) {
                submitCheckFragment.setParentBean(parentBean);
            }
            fragmentTransaction.add(R.id.layout_frame_root, submitCheckFragment);
        }
        else {
            fragmentTransaction.show(submitCheckFragment);
            submitCheckFragment.setReserveCheckBean(reserveCheckBean);
            submitCheckFragment.onResume();
        }
        fragmentTransaction.commitAllowingStateLoss();
        selectTab(BASE_TWO);
    }

    /**
     * 隐藏所有碎片
     */
    private void hideAll(FragmentTransaction transaction) {
        if (identifyFragment != null) {
            transaction.hide(identifyFragment);
        }
        if (materialFragment != null) {
            transaction.hide(materialFragment);
        }
        if (submitCheckFragment != null) {
            transaction.hide(submitCheckFragment);
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
                tvReservationBase.setSelected(true);
                viewReservationBase.setSelected(false);
                ivReservationBase.setImageResource(R.mipmap.ic_step_sel);
                tvReservationBase.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                //1
                tvReservationMaterial.setSelected(false);
                viewReservationLicenseLeft.setSelected(false);
                ivReservationMaterial.setImageResource(R.mipmap.ic_step_def);
                tvReservationMaterial.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                break;
            case BASE_ONE:
                curPage = BASE_ONE;
                //0
                viewReservationBase.setSelected(true);
                tvReservationBase.setSelected(true);
                tvReservationBase.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                ivReservationBase.setImageResource(R.mipmap.ic_step_finish);
                //1
                tvReservationMaterial.setSelected(true);
                viewReservationLicenseLeft.setSelected(true);
                viewReservationLicenseRight.setSelected(false);
                ivReservationMaterial.setImageResource(R.mipmap.ic_step_sel);
                tvReservationMaterial.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                //2
                layoutReservationResult.setSelected(false);
                ivReservationResult.setImageResource(R.mipmap.ic_step_def);
                tvReservationResult.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                break;
            case BASE_TWO:
                curPage = BASE_TWO;
                //1
                viewReservationLicenseRight.setSelected(true);
                tvReservationMaterial.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                ivReservationMaterial.setImageResource(R.mipmap.ic_step_finish);
                //2
                layoutReservationResult.setSelected(true);
                ivReservationResult.setImageResource(R.mipmap.ic_step_sel);
                tvReservationResult.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.public_title_bar_back:
                if (finishPage()) {
                    hideSoftInputFromWindow();
                    finish();
                }
                break;
            case R.id.public_title_bar_more:
                new HintDialog(this).setPhone(getString(R.string.txt_contact_service),
                                              getString(R.string.txt_contact_service_phone), false)
                                    .setOnEnterClickListener(
                                            () -> callPhone(getString(R.string.txt_contact_service_phone)))
                                    .show();
                break;
            default:
                break;
        }
    }

    @Override
    public void onCheckStepOne(ReserveCheckBean bean) {
        reserveCheckBean = bean;
        tabReservationLicenseView();
    }

    @Override
    public void onCheckStepTwo(ReserveCheckBean bean) {
        reserveCheckBean = bean;
        tabCheckResultView();
    }

    @Override
    public void onCheckStepThree(ReserveCheckBean bean) {
        reserveCheckBean = bean;
        addReserveCheckOrder();
    }

    /**
     * 继续提交(忽略价格变动)
     */
    private void continueSubmit(List<ServiceSubmitErrorBean> list) {
        //强制更新检查项
        sharePreferenceUtil.putBoolean(CommonData.KEY_RESERVE_CHECK_UPDATE, true);
        ArrayList<ReserveCheckTypeBean> checkTypeBeans = reserveCheckBean.getCheckTrans();
        for (ReserveCheckTypeBean bean : checkTypeBeans) {
            for (ServiceSubmitErrorBean errorBean : list) {
                if (TextUtils.equals(bean.getProductCode(), errorBean.getCode())) {
                    bean.setPrice(errorBean.getNewPrice());
                }
            }
        }
        addReserveCheckOrder();
    }

    /**
     * 页面逻辑处理
     *
     * @return bool
     */
    private boolean finishPage() {
        if (curPage == BASE_TWO) {
            curPage = 1;
            tabReservationLicenseView();
            return false;
        }
        else if (curPage == 1) {
            if (patientBean != null) {
                return true;
            }
            if (materialFragment != null) {
                reserveCheckBean = materialFragment.getReserveCheckBean();
            }
            curPage = 0;
            tabReservationBaseView();
            return false;
        }
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        switch (curPage) {
            case BASE_ZERO:
                if (identifyFragment != null) {
                    identifyFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
                }
                break;
            case BASE_TWO:
                if (submitCheckFragment != null) {
                    submitCheckFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
                if (identifyFragment != null) {
                    identifyFragment.onPermissionNeedExplanation(permissionName);
                }
                break;
            case BASE_TWO:
                if (submitCheckFragment != null) {
                    submitCheckFragment.onPermissionNeedExplanation(permissionName);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onNoPermissionNeeded(@NonNull Object permissionName) {
        super.onNoPermissionNeeded(permissionName);
        switch (curPage) {
            case BASE_ZERO:
                if (identifyFragment != null) {
                    identifyFragment.onNoPermissionNeeded(permissionName);
                }
                break;
            case BASE_TWO:
                if (submitCheckFragment != null) {
                    submitCheckFragment.onNoPermissionNeeded(permissionName);
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //清除已选数据
        ZycApplication.getInstance().clearSelectCodes();
    }
}
