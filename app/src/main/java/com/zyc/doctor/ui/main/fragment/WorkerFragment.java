package com.zyc.doctor.ui.main.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.yht.frame.permission.Permission;
import com.yht.frame.ui.BaseFragment;
import com.yht.frame.utils.ToastUtil;
import com.yht.frame.widgets.dialog.HintDialog;
import com.yht.frame.widgets.dialog.InputDialog;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.common.Constant;
import com.zyc.doctor.R;
import com.zyc.doctor.ui.check.CheckHistoryActivity;
import com.zyc.doctor.ui.check.ReservationCheckActivity;
import com.zyc.doctor.ui.personal.PersonalActivity;

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
    /**
     * 扫码
     */
    private static final int REQUEST_CODE_SCAN = 100;

    @Override
    public int getLayoutID() {
        return R.layout.fragment_worker;
    }

    @Override
    public void initView(View view, @NonNull Bundle savedInstanceState) {
        super.initView(view, savedInstanceState);
        statusBarFix.setLayoutParams(
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStateBarHeight(getActivity())));
        publicMainTitle.setText("医生");
        publicMainTitleScan.setVisibility(View.VISIBLE);
        initFlipper();
    }

    /**
     * 广告轮播
     */
    private void initFlipper() {
        for (int i = 0; i < 4; i++) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_flipper, null);
            TextView textView = view.findViewById(R.id.tv_flipper);
            textView.setText(i + "秒带你玩转会珍3.0");
            view.setTag(i);
            viewFlipper.addView(view);
        }
    }

    @OnClick({
            R.id.public_main_title_scan, R.id.layout_personal_base, R.id.layout_check, R.id.layout_transfer,
            R.id.view_flipper, R.id.layout_initiate_check, R.id.layout_initiate_transfer, R.id.layout_accepted_transfer,
            R.id.layout_transfer_apply })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.public_main_title_scan:
                permissionHelper.request(new String[] { Permission.CAMERA });
                break;
            case R.id.layout_personal_base:
                startActivity(new Intent(getContext(), PersonalActivity.class));
                break;
            case R.id.layout_check:
                startActivity(new Intent(getContext(), CheckHistoryActivity.class));
                break;
            case R.id.layout_transfer:
                new HintDialog(getContext()).setContentString(
                        "测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试")
                                            .show();
                break;
            case R.id.layout_transfer_apply:
                new InputDialog(getContext()).Builder()
                                             .setEnterBtnTxt("再想想")
                                             .setEnterSelect(true)
                                             .setCancleBtnTxt("确认取消")
                                             .show();
                break;
            case R.id.view_flipper:
                break;
            case R.id.layout_initiate_check:
                startActivity(new Intent(getContext(), ReservationCheckActivity.class));
                break;
            case R.id.layout_initiate_transfer:
                break;
            case R.id.layout_accepted_transfer:
                break;
            default:
                break;
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
        if (requestCode == REQUEST_CODE_SCAN) {
            if (data != null) {
                String content = data.getStringExtra(Constant.CODED_CONTENT);
                ToastUtil.toast(getContext(), content);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
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
