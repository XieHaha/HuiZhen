package com.yht.yihuantong.ui.main.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.yht.frame.data.base.BannerBean;
import com.yht.frame.data.base.OrderNumStatisticsBean;
import com.yht.frame.http.retrofit.RequestUtils;
import com.yht.frame.permission.Permission;
import com.yht.frame.ui.BaseFragment;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.utils.ToastUtil;
import com.yht.frame.utils.glide.GlideHelper;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ui.check.CheckHistoryActivity;
import com.yht.yihuantong.ui.personal.PersonalActivity;
import com.yht.yihuantong.ui.reservation.ReservationCheckOrTransferActivity;
import com.yht.yihuantong.ui.transfer.TransferInitiateListActivity;
import com.yht.yihuantong.ui.transfer.TransferReceiveListActivity;
import com.yht.yihuantong.utils.ImageUrlUtil;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.common.Constant;

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
    private List<BannerBean> bannerBeans;
    /**
     * 扫码
     */
    private static final int REQUEST_CODE_SCAN = 100;

    @Override
    public int getLayoutID() {
        return R.layout.fragment_worker;
    }

    @Override
    public void onResume() {
        super.onResume();
        getStudioOrderStatistics();
    }

    @Override
    public void initView(View view, @NonNull Bundle savedInstanceState) {
        super.initView(view, savedInstanceState);
        statusBarFix.setLayoutParams(
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStateBarHeight(getActivity())));
        publicMainTitleScan.setVisibility(View.VISIBLE);
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        publicMainTitle.setText(loginBean.getDoctorName());
        tvPersonalDepart.setText(loginBean.getDepartmentName());
        tvPersonalHospital.setText(loginBean.getHospitalName());
        Glide.with(this)
             .load(ImageUrlUtil.append(loginBean.getPhoto()))
             .apply(GlideHelper.getOptions(BaseUtils.dp2px(getContext(), 4)))
             .into(ivPersonalImage);
        getBanner();
    }

    /**
     * 获取所有订单数量
     */
    private void getStudioOrderStatistics() {
        RequestUtils.getStudioOrderStatistics(getContext(), loginBean.getToken(), this);
    }

    /**
     * banner
     */
    private void getBanner() {
        RequestUtils.getBanner(getContext(), loginBean.getToken(), this);
    }

    /**
     * 广告轮播
     */
    private void initFlipper() {
        viewFlipper.removeAllViews();
        for (int i = 0; i < bannerBeans.size(); i++) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_flipper, null);
            TextView textView = view.findViewById(R.id.tv_flipper);
            textView.setText(bannerBeans.get(i).getBannerRemark());
            viewFlipper.addView(view);
        }
    }

    /**
     * 订单数量
     */
    private void initStatistics() {
        tvInitiateCheckNum.setText(String.valueOf(orderNumStatisticsBean.getInitiateOrderCheck()));
        tvInitiateTransferNum.setText(String.valueOf(orderNumStatisticsBean.getInitiateOrderTransfer()));
        tvAcceptedTransferNum.setText(String.valueOf(orderNumStatisticsBean.getReceiveOrderTransfer()));
        if (orderNumStatisticsBean.getPendingOrderTransfer() != BaseData.BASE_ZERO) {
            tvReceivingTransferNum.setText(String.valueOf(orderNumStatisticsBean.getPendingOrderTransfer()));
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
                startActivity(new Intent(getContext(), PersonalActivity.class));
                break;
            case R.id.layout_check:
                intent = new Intent(getContext(), ReservationCheckOrTransferActivity.class);
                startActivity(intent);
                break;
            case R.id.layout_transfer:
                intent = new Intent(getContext(), ReservationCheckOrTransferActivity.class);
                intent.putExtra(CommonData.KEY_CHECK_OR_TRANSFER, true);
                startActivity(intent);
                break;
            case R.id.layout_transfer_apply:
                intent = new Intent(getContext(), TransferReceiveListActivity.class);
                startActivity(intent);
                break;
            case R.id.view_flipper:
                ToastUtil.toast(getContext(), bannerBeans.get(viewFlipper.getDisplayedChild()).getBannerRemark());
                break;
            case R.id.layout_initiate_check:
                startActivity(new Intent(getContext(), CheckHistoryActivity.class));
                break;
            case R.id.layout_initiate_transfer:
                startActivity(new Intent(getContext(), TransferInitiateListActivity.class));
                break;
            case R.id.layout_accepted_transfer:
                intent = new Intent(getContext(), TransferReceiveListActivity.class);
                intent.putExtra(CommonData.KEY_PUBLIC, true);
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
                if (bannerBeans != null) {
                    initFlipper();
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
