package com.zyc.doctor.ui.main.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.yht.frame.ui.BaseFragment;
import com.zyc.doctor.R;
import com.zyc.doctor.ui.auth.AuthDoctorActivity;
import com.zyc.doctor.ui.capture.CaptureQrCodeActivity;
import com.zyc.doctor.ui.personal.PersonalActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/5/17 14:55
 * @des 工作室
 */
public class WorkerFragment extends BaseFragment {
    @BindView(R.id.button)
    Button button;
    @BindView(R.id.button1)
    Button button1;
    @BindView(R.id.status_bar_fix)
    View statusBarFix;
    @BindView(R.id.public_main_title)
    TextView publicMainTitle;
    @BindView(R.id.public_main_title_scan)
    ImageView publicMainTitleScan;

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
        button.setOnClickListener(v -> startActivity(new Intent(getContext(), PersonalActivity.class)));
        button1.setOnClickListener(v -> startActivity(new Intent(getContext(), AuthDoctorActivity.class)));
    }

    @OnClick(R.id.public_main_title_scan)
    public void onViewClicked() {
        new IntentIntegrator(getActivity()).setCaptureActivity(CaptureQrCodeActivity.class)
                                           .setPrompt(getString(R.string.txt_camera_hint))
                                           .setBarcodeImageEnabled(false)
                                           .initiateScan();
    }
}
