package com.yht.yihuantong.ui.main.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.umeng.analytics.AnalyticsConfig;
import com.yht.frame.api.ApiManager;
import com.yht.frame.api.notify.IChange;
import com.yht.frame.api.notify.NotifyChangeListenerManager;
import com.yht.frame.api.notify.RegisterType;
import com.yht.frame.data.BaseData;
import com.yht.frame.data.BaseNetConfig;
import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.CommonData;
import com.yht.frame.data.Tasks;
import com.yht.frame.data.bean.BannerBean;
import com.yht.frame.data.bean.DoctorQrCodeBean;
import com.yht.frame.data.bean.OrderNumStatisticsBean;
import com.yht.frame.data.bean.PatientBean;
import com.yht.frame.data.bean.ReservationValidateBean;
import com.yht.frame.http.retrofit.RequestUtils;
import com.yht.frame.permission.Permission;
import com.yht.frame.ui.BaseFragment;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.utils.TimeUtil;
import com.yht.frame.utils.glide.GlideHelper;
import com.yht.frame.widgets.menu.MenuItem;
import com.yht.frame.widgets.menu.TopRightMenu;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ZycApplication;
import com.yht.yihuantong.ui.WebViewActivity;
import com.yht.yihuantong.ui.check.ServiceHistoryActivity;
import com.yht.yihuantong.ui.doctor.DoctorInfoActivity;
import com.yht.yihuantong.ui.hint.NotifyHintActivity;
import com.yht.yihuantong.ui.hospital.HealthManageActivity;
import com.yht.yihuantong.ui.main.QrCodeActivity;
import com.yht.yihuantong.ui.patient.ChatContainerActivity;
import com.yht.yihuantong.ui.personal.PersonalNewActivity;
import com.yht.yihuantong.ui.remote.ErrorActivity;
import com.yht.yihuantong.ui.remote.RemoteHistoryActivity;
import com.yht.yihuantong.ui.remote.RemoteLoginActivity;
import com.yht.yihuantong.ui.reservation.ReservationDisableActivity;
import com.yht.yihuantong.ui.reservation.remote.ReservationRemoteActivity;
import com.yht.yihuantong.ui.reservation.service.ReservationServiceActivity;
import com.yht.yihuantong.ui.reservation.transfer.ReservationTransferActivity;
import com.yht.yihuantong.ui.transfer.TransferInitiateListActivity;
import com.yht.yihuantong.ui.transfer.TransferReceiveListActivity;
import com.yht.yihuantong.utils.FileUrlUtil;
import com.yht.yihuantong.utils.NotifySettingUtils;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.common.Constant;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/5/17 14:55
 * @des 工作室
 */
public class WorkerFragment extends BaseFragment implements TopRightMenu.OnMenuItemClickListener {
    @BindView(R.id.status_bar_fix)
    View statusBarFix;
    @BindView(R.id.public_main_title)
    TextView publicMainTitle;
    @BindView(R.id.public_main_title_scan)
    ImageView publicMainTitleScan;
    @BindView(R.id.tv_personal_depart)
    TextView tvPersonalDepart;
    @BindView(R.id.tv_personal_hospital)
    TextView tvPersonalHospital;
    @BindView(R.id.iv_personal_image)
    ImageView ivPersonalImage;
    @BindView(R.id.tv_initiate_check_num)
    TextView tvInitiateCheckNum;
    @BindView(R.id.tv_initiate_transfer_num)
    TextView tvInitiateTransferNum;
    @BindView(R.id.tv_initiate_remote_num)
    TextView tvInitiateRemoteNum;
    @BindView(R.id.view_flipper)
    ViewFlipper viewFlipper;
    @BindView(R.id.tv_receiving_transfer_num)
    TextView tvReceivingTransferNum;
    @BindView(R.id.layout_receiving_transfer_num)
    RelativeLayout layoutReceivingTransferNum;
    /**
     * 订单统计
     */
    private OrderNumStatisticsBean orderNumStatisticsBean;
    /**
     * 广告banner
     */
    private List<BannerBean> bannerBeans;
    /**
     * 扫码
     */
    private static final int REQUEST_CODE_SCAN = 100;
    /**
     * 消息红点
     */
    private IChange<String> transferApply = data -> getStudioOrderStatistics();

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isPrepared) {
            if (getUserVisibleHint()) {
                fillData();
            }
            else if (viewFlipper.isFlipping()) {
                viewFlipper.stopFlipping();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //当前用户可见才刷新列表
        if (getUserVisibleHint()) {
            fillData();
        }
    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_worker;
    }

    @Override
    public void initView(View view, @NonNull Bundle savedInstanceState) {
        super.initView(view, savedInstanceState);
        statusBarFix.setLayoutParams(
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStateBarHeight(getActivity())));
        publicMainTitleScan.setVisibility(View.VISIBLE);
        iNotifyChangeListenerServer = ApiManager.getInstance().getServer();
        view.postOnAnimationDelayed(this::initNotifyHint, 2000);
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        publicMainTitle.setText(loginBean.getDoctorName());
        tvPersonalDepart.setText(loginBean.getDepartmentName());
        tvPersonalHospital.setText(loginBean.getHospitalName());
        Glide.with(this)
             .load(FileUrlUtil.addTokenToUrl(loginBean.getPhoto()))
             .apply(GlideHelper.getOptions(BaseUtils.dp2px(Objects.requireNonNull(getContext()), 4)))
             .into(ivPersonalImage);
    }

    @Override
    public void initListener() {
        super.initListener();
        iNotifyChangeListenerServer.registerDoctorTransferPatientListener(transferApply, RegisterType.REGISTER);
    }

    public void fillData() {
        if (BaseUtils.isNetworkAvailable(Objects.requireNonNull(getContext()))) {
            getBanner();
        }
        else {
            initFlipper();
        }
        getStudioOrderStatistics();
        getValidateHospitalList();
    }

    /**
     * 扫码后获取居民信息
     */
    private void getPatientByQrId(String qrId) {
        RequestUtils.getPatientByQrId(getContext(), loginBean.getToken(), qrId, BaseData.BASE_ONE, this);
    }

    /**
     * 扫码后获取医生信息
     */
    private void getDoctorByQrId(String qrId) {
        RequestUtils.getDoctorByQrId(getContext(), loginBean.getToken(), qrId, this);
    }

    /**
     * banner
     */
    private void getBanner() {
        RequestUtils.getBanner(getContext(), loginBean.getToken(), this);
    }

    /**
     * 获取所有订单数量
     */
    private void getStudioOrderStatistics() {
        RequestUtils.getStudioOrderStatistics(getContext(), loginBean.getToken(), this);
    }

    /**
     * 校验医生是否有预约检查和预约转诊的合作医院
     */
    private void getValidateHospitalList() {
        RequestUtils.getValidateHospitalList(getContext(), loginBean.getToken(), this);
    }

    /**
     * 是否需要提示通知权限   没有通知权限（每周提醒一次）
     */
    private void initNotifyHint() {
        if (!NotifySettingUtils.hasNotify(Objects.requireNonNull(getActivity()))) {
            long date = sharePreferenceUtil.getAlwaysLong(CommonData.KEY_NOTIFICATION_CONTROL);
            long diff = System.currentTimeMillis() - date - TimeUtil.TIME_WEEK;
            if (date == 0 || diff > 0) {
                startActivity(new Intent(getContext(), NotifyHintActivity.class));
                getActivity().overridePendingTransition(R.anim.actionsheet_dialog_in, R.anim.keep);
            }
        }
    }

    /**
     * 广告轮播
     */
    private void initFlipper() {
        viewFlipper.removeAllViews();
        //添加默认值
        if (bannerBeans == null || bannerBeans.size() == 0) {
            bannerBeans = new ArrayList<>();
            BannerBean bean = new BannerBean();
            bean.setBannerRemark(getString(R.string.txt_view_flipper_hint));
            bean.setBannerId(-1);
            bannerBeans.add(bean);
        }
        for (int i = 0; i < bannerBeans.size(); i++) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_flipper, null);
            TextView textView = view.findViewById(R.id.tv_flipper);
            textView.setText(bannerBeans.get(i).getBannerRemark());
            viewFlipper.addView(view);
        }
        if (bannerBeans.size() > 1) {
            viewFlipper.postDelayed(() -> viewFlipper.startFlipping(), 200);
        }
    }

    /**
     * 广告条详情
     */
    private void flipperDetail() {
        if (bannerBeans != null && bannerBeans.size() > 0) {
            BannerBean bean = bannerBeans.get(viewFlipper.getDisplayedChild());
            if (bean.getBannerId() != -1) {
                Intent intent = new Intent(getContext(), WebViewActivity.class);
                intent.putExtra(CommonData.KEY_PUBLIC,
                                ZycApplication.getInstance().getBaseUrl() + BaseNetConfig.BASE_BASIC_BANNER_URL +
                                bean.getBannerId());
                intent.putExtra(CommonData.KEY_TITLE, bean.getBannerRemark());
                startActivity(intent);
            }
        }
    }

    /**
     * 订单数量
     */
    private void initStatistics() {
        tvInitiateCheckNum.setText(
                orderNumStatisticsBean.getInitiateOrderCheck() <= BaseData.BASE_MEAASGE_DISPLAY_NUM ? String.valueOf(
                        orderNumStatisticsBean.getInitiateOrderCheck()) : getString(R.string.txt_max_num));
        tvInitiateTransferNum.setText(
                orderNumStatisticsBean.getInitiateOrderTransfer() <= BaseData.BASE_MEAASGE_DISPLAY_NUM ? String.valueOf(
                        orderNumStatisticsBean.getInitiateOrderTransfer()) : getString(R.string.txt_max_num));
        tvInitiateRemoteNum.setText(
                orderNumStatisticsBean.getInitiateRemoteCheck() <= BaseData.BASE_MEAASGE_DISPLAY_NUM ? String.valueOf(
                        orderNumStatisticsBean.getInitiateRemoteCheck()) : getString(R.string.txt_max_num));
        if (orderNumStatisticsBean.getPendingOrderTransfer() != BaseData.BASE_ZERO) {
            tvReceivingTransferNum.setText(
                    orderNumStatisticsBean.getPendingOrderTransfer() <= BaseData.BASE_MEAASGE_DISPLAY_NUM
                    ? String.valueOf(orderNumStatisticsBean.getPendingOrderTransfer())
                    : getString(R.string.txt_max_num));
            layoutReceivingTransferNum.setVisibility(View.VISIBLE);
        }
        else {
            layoutReceivingTransferNum.setVisibility(View.GONE);
        }
    }

    private void initMenu() {
        TopRightMenu mTopRightMenu = new TopRightMenu(getActivity());
        List<MenuItem> menuItems = new ArrayList<>();
        menuItems.add(new MenuItem(R.mipmap.ic_patient_richscan, getString(R.string.txt_menu_patient)));
        menuItems.add(new MenuItem(R.mipmap.ic_doctor_richscan, getString(R.string.txt_menu_doctor)));
        menuItems.add(new MenuItem(R.mipmap.ic_richscan, getString(R.string.title_camera_scan)));
        mTopRightMenu.setHeight(BaseUtils.dp2px(getContext(), 200))
                     .addMenuList(menuItems)
                     .setOnMenuItemClickListener(this)
                     .showAsDropDown(publicMainTitleScan, -BaseUtils.dp2px(getContext(), 124), 10);
    }

    @OnClick({
            R.id.public_main_title_scan, R.id.layout_personal_base, R.id.layout_check, R.id.layout_transfer,
            R.id.layout_remote, R.id.view_flipper, R.id.layout_initiate_check, R.id.layout_initiate_transfer,
            R.id.layout_initiate_remote, R.id.layout_transfer_apply, R.id.layout_health_manager })
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.public_main_title_scan:
                initMenu();
                break;
            case R.id.layout_personal_base:
                startActivity(new Intent(getContext(), PersonalNewActivity.class));
                break;
            case R.id.layout_check:
                if (ZycApplication.getInstance().isServiceAble()) {
                    intent = new Intent(getContext(), ReservationServiceActivity.class);
                    startActivity(intent);
                }
                else {
                    intent = new Intent(getContext(), ReservationDisableActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.layout_transfer:
                if (ZycApplication.getInstance().isTransferAble()) {
                    intent = new Intent(getContext(), ReservationTransferActivity.class);
                    startActivity(intent);
                }
                else {
                    intent = new Intent(getContext(), ReservationDisableActivity.class);
                    intent.putExtra(CommonData.KEY_CHECK_OR_TRANSFER, true);
                    startActivity(intent);
                }
                break;
            case R.id.layout_remote:
                if (ZycApplication.getInstance().isRemoteAble()) {
                    intent = new Intent(getContext(), ReservationRemoteActivity.class);
                    startActivity(intent);
                }
                else {
                    intent = new Intent(getContext(), ReservationDisableActivity.class);
                    intent.putExtra(CommonData.KEY_CHECK_OR_TRANSFER, true);
                    startActivity(intent);
                }
                break;
            case R.id.layout_transfer_apply:
                intent = new Intent(getContext(), TransferReceiveListActivity.class);
                intent.putExtra(CommonData.KEY_PUBLIC, true);
                startActivity(intent);
                break;
            case R.id.view_flipper:
                flipperDetail();
                break;
            case R.id.layout_initiate_check:
                intent = new Intent(getContext(), ServiceHistoryActivity.class);
                if (orderNumStatisticsBean != null) {
                    intent.putExtra(CommonData.KEY_PUBLIC, orderNumStatisticsBean.getInitiateOrderCheck());
                }
                startActivity(intent);
                break;
            case R.id.layout_initiate_transfer:
                intent = new Intent(getContext(), TransferInitiateListActivity.class);
                if (orderNumStatisticsBean != null) {
                    intent.putExtra(CommonData.KEY_PUBLIC, orderNumStatisticsBean.getInitiateOrderTransfer());
                }
                startActivity(intent);
                break;
            case R.id.layout_initiate_remote:
                intent = new Intent(getContext(), RemoteHistoryActivity.class);
                if (orderNumStatisticsBean != null) {
                    intent.putExtra(CommonData.KEY_PUBLIC, orderNumStatisticsBean.getInitiateRemoteCheck());
                }
                startActivity(intent);
                break;
            case R.id.layout_health_manager:
                intent = new Intent(getContext(), HealthManageActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    /**
     * 菜单
     *
     * @param position index
     */
    @Override
    public void onMenuItemClick(int position) {
        Intent intent;
        switch (position) {
            case BASE_ZERO:
                intent = new Intent(getContext(), QrCodeActivity.class);
                startActivity(intent);
                break;
            case BASE_ONE:
                intent = new Intent(getContext(), QrCodeActivity.class);
                intent.putExtra(CommonData.KEY_INTENT_BOOLEAN, true);
                startActivity(intent);
                break;
            case BASE_TWO:
                permissionHelper.request(new String[] { Permission.CAMERA });
                break;
            default:
                break;
        }
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        Intent intent;
        switch (task) {
            case GET_STUDIO_ORDER_STATISTICS:
                orderNumStatisticsBean = (OrderNumStatisticsBean)response.getData();
                initStatistics();
                break;
            case GET_BANNER:
                bannerBeans = (List<BannerBean>)response.getData();
                initFlipper();
                break;
            case GET_VALIDATE_HOSPITAL_LIST:
                ReservationValidateBean bean = (ReservationValidateBean)response.getData();
                if (bean != null) {
                    ZycApplication.getInstance().setServiceAble(bean.isJc());
                    ZycApplication.getInstance().setTransferAble(bean.isZz());
                    ZycApplication.getInstance().setRemoteAble(bean.isRemote());
                }
                break;
            case GET_PATIENT_BY_QR_ID:
                PatientBean patientBean = (PatientBean)response.getData();
                //添加成功  刷新居民列表
                NotifyChangeListenerManager.getInstance().notifyPatientListChanged("");
                //跳转到居民信息界面
                intent = new Intent(getContext(), ChatContainerActivity.class);
                intent.putExtra(CommonData.KEY_CHAT_ID, patientBean.getCode());
                intent.putExtra(CommonData.KEY_CHAT_NAME, patientBean.getName());
                startActivity(intent);
                break;
            case GET_DOCTOR_BY_QR_ID:
                DoctorQrCodeBean doctorBean = (DoctorQrCodeBean)response.getData();
                if (doctorBean.isFriend()) {
                    intent = new Intent(getContext(), ChatContainerActivity.class);
                    intent.putExtra(CommonData.KEY_CHAT_ID, doctorBean.getCode());
                    intent.putExtra(CommonData.KEY_CHAT_NAME, doctorBean.getName());
                    intent.putExtra(CommonData.KEY_DOCTOR_CHAT, true);
                    startActivity(intent);
                }
                else {
                    intent = new Intent(getContext(), DoctorInfoActivity.class);
                    intent.putExtra(CommonData.KEY_DOCTOR_QR_CODE_BEAN, doctorBean);
                    startActivity(intent);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onResponseError(Tasks task, Exception e) {
        super.onResponseError(task, e);
        if (task == Tasks.GET_BANNER) {
            initFlipper();
        }
    }

    /**
     * 关于远程会诊url处理
     */
    private String initRemoteUrl() {
        String baseBasicRemoteUrl = BaseNetConfig.BASE_BASIC_REMOTE_URL;
        String channel = AnalyticsConfig.getChannel(getContext());
        if (!TextUtils.isEmpty(channel)) {
            switch (channel) {
                case "zyc":
                    return baseBasicRemoteUrl.replace("hsp", "hsp-pre");
                case "beta":
                    return baseBasicRemoteUrl.replace("hsp", "hsp-t").replace("https", "http");
                default:
                    return baseBasicRemoteUrl;
            }
        }
        else {
            return baseBasicRemoteUrl;
        }
    }

    /**
     * 开启扫一扫
     */
    private void openScan() {
        Intent intent = new Intent(getContext(), CaptureActivity.class);
        startActivityForResult(intent, REQUEST_CODE_SCAN);
        Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.keep, R.anim.keep);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        Intent intent;
        if (requestCode == REQUEST_CODE_SCAN && data != null) {
            String content = data.getStringExtra(Constant.CODED_CONTENT);
            if (!TextUtils.isEmpty(content)) {
                if (content.startsWith(BaseData.BASE_REMOTE) || content.startsWith(BASE_REMOTE_ADVICE)) {
                    intent = new Intent(getContext(), RemoteLoginActivity.class);
                    intent.putExtra(CommonData.KEY_PUBLIC_STRING, content);
                    startActivity(intent);
                }
                else {
                    //居民、医生二维码
                    Uri uri = Uri.parse(content);
                    if (uri != null && !uri.isOpaque()) {
                        String mode = uri.getQueryParameter("t");
                        String value = uri.getQueryParameter("p");
                        if (!TextUtils.isEmpty(value) && !TextUtils.isEmpty(mode)) {
                            //1为医生  2为居民
                            if (BASE_STRING_ONE_TAG.equals(mode)) {
                                getDoctorByQrId(value);
                            }
                            else {
                                getPatientByQrId(value);
                            }
                        }
                        else {
                            qrError();
                        }
                    }
                    else {
                        qrError();
                    }
                }
            }
            else {
                qrError();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void qrError() {
        startActivityForResult(new Intent(getContext(), ErrorActivity.class), REQUEST_CODE_SCAN);
    }

    @Override
    public void onNoPermissionNeeded(@NonNull Object permissionName) {
        if (permissionName instanceof String[]) {
            if (isSamePermission(Permission.CAMERA, ((String[])permissionName)[0])) {
                openScan();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        iNotifyChangeListenerServer.registerDoctorTransferPatientListener(transferApply, RegisterType.UNREGISTER);
    }
}
