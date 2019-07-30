package com.yht.yihuantong.ui.main.fragment;

import android.app.Activity;
import android.content.Intent;
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
import com.yht.frame.data.BaseData;
import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.CommonData;
import com.yht.frame.data.Tasks;
import com.yht.frame.data.bean.BannerBean;
import com.yht.frame.data.bean.OrderNumStatisticsBean;
import com.yht.frame.data.bean.ReservationValidateBean;
import com.yht.frame.http.retrofit.RequestUtils;
import com.yht.frame.permission.Permission;
import com.yht.frame.ui.BaseFragment;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.utils.ToastUtil;
import com.yht.frame.utils.glide.GlideHelper;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ZycApplication;
import com.yht.yihuantong.ui.NotifyHintActivity;
import com.yht.yihuantong.ui.WebViewActivity;
import com.yht.yihuantong.ui.check.ServiceHistoryActivity;
import com.yht.yihuantong.ui.personal.PersonalNewActivity;
import com.yht.yihuantong.ui.reservation.ReservationDisableActivity;
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
public class WorkerFragment extends BaseFragment {
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
    @BindView(R.id.tv_accepted_transfer_num)
    TextView tvAcceptedTransferNum;
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

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isPrepared) {
            if (getUserVisibleHint()) {
                fillNetWorkData();
            }
            else if (viewFlipper.isFlipping()) {
                viewFlipper.stopFlipping();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        fillNetWorkData();
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
        publicMainTitleScan.setVisibility(View.INVISIBLE);
        initFlipper();
        view.postOnAnimationDelayed(() -> initNotifyHint(), 2000);
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        publicMainTitle.setText(loginBean.getDoctorName());
        tvPersonalDepart.setText(loginBean.getDepartmentName());
        tvPersonalHospital.setText(loginBean.getHospitalName());
        Glide.with(this)
             .load(FileUrlUtil.addTokenToUrl(loginBean.getPhoto()))
             .apply(GlideHelper.getOptions(BaseUtils.dp2px(getContext(), 4)))
             .into(ivPersonalImage);
    }

    @Override
    public void fillNetWorkData() {
        super.fillNetWorkData();
        getBanner();
        getStudioOrderStatistics();
        getValidateHospitalList();
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
     * 是否需要提示通知权限   只显示一次
     */
    private void initNotifyHint() {
        int type = sharePreferenceUtil.getAlwaysInteger(CommonData.KEY_NOTIFICATION_CONTROL);
        //表示用户未操作过
        if (type != BASE_TWO) {
            if (!NotifySettingUtils.hasNotify(getActivity())) {
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
            bannerBeans.add(bean);
        }
        for (int i = 0; i < bannerBeans.size(); i++) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_flipper, null);
            TextView textView = view.findViewById(R.id.tv_flipper);
            textView.setText(bannerBeans.get(i).getBannerRemark());
            viewFlipper.addView(view);
        }
        viewFlipper.startFlipping();
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
        tvAcceptedTransferNum.setText(
                orderNumStatisticsBean.getReceiveOrderTransfer() <= BaseData.BASE_MEAASGE_DISPLAY_NUM ? String.valueOf(
                        orderNumStatisticsBean.getReceiveOrderTransfer()) : getString(R.string.txt_max_num));
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

    @OnClick({
            R.id.public_main_title_scan, R.id.layout_personal_base, R.id.layout_check, R.id.layout_transfer,
            R.id.view_flipper, R.id.layout_initiate_check, R.id.layout_initiate_transfer, R.id.layout_accepted_transfer,
            R.id.layout_transfer_apply })
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.public_main_title_scan:
                permissionHelper.request(new String[] { Permission.CAMERA });
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
            case R.id.layout_transfer_apply:
                intent = new Intent(getContext(), TransferReceiveListActivity.class);
                intent.putExtra(CommonData.KEY_PUBLIC, true);
                startActivity(intent);
                break;
            case R.id.view_flipper:
                BannerBean bean = bannerBeans.get(viewFlipper.getDisplayedChild());
                String url = bean.getBannerUrl();
                if (!TextUtils.isEmpty(url)) {
                    intent = new Intent(getContext(), WebViewActivity.class);
                    intent.putExtra(CommonData.KEY_PUBLIC, url);
                    intent.putExtra(CommonData.KEY_TITLE, bean.getBannerRemark());
                    startActivity(intent);
                }
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
            case R.id.layout_accepted_transfer:
                intent = new Intent(getContext(), TransferReceiveListActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
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
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_SCAN) {
            if (data != null) {
                String content = data.getStringExtra(Constant.CODED_CONTENT);
                ToastUtil.toast(getContext(), content);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
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
    public void onNoPermissionNeeded(@NonNull Object permissionName) {
        if (permissionName instanceof String[]) {
            if (isSamePermission(Permission.CAMERA, ((String[])permissionName)[0])) {
                openScan();
            }
        }
    }
}
