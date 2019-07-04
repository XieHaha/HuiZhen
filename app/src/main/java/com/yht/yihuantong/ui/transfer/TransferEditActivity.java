package com.yht.yihuantong.ui.transfer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.yht.frame.data.CommonData;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.widgets.edittext.AbstractTextWatcher;
import com.yht.yihuantong.R;
import com.yht.yihuantong.utils.TimePickerHelper;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/6/27 19:41
 * @des 变更接诊信息
 */
public class TransferEditActivity extends BaseActivity {
    @BindView(R.id.tv_hospital)
    TextView tvHospital;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.et_notice)
    EditText etNotice;
    @BindView(R.id.tv_notice_num)
    TextView tvNoticeNum;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    @BindView(R.id.public_title_bar_title)
    TextView publicTitleBarTitle;
    @BindView(R.id.tv_not_required)
    TextView tvNotRequired;
    /**
     * true 为变更接诊信息  false为接诊
     */
    private boolean isEditReceive;
    /**
     * 接诊医院  预约就诊时间
     */
    private String receiveHospital = "", reserveTime = "", noticeText = "";
    /**
     * 选择医院
     */
    public static final int REQUEST_CODE_HOSPITAL = 100;

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_transfer_edit;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        if (getIntent() != null) {
            isEditReceive = getIntent().getBooleanExtra(CommonData.KEY_RECEIVE_OR_EDIT_VISIT, false);
            if (isEditReceive) {
                receiveHospital = getIntent().getStringExtra(CommonData.KEY_RECEIVE_HOSPITAL);
                reserveTime = getIntent().getStringExtra(CommonData.KEY_RESERVE_TIME);
                initPage();
            }
        }
    }

    @Override
    public void initListener() {
        super.initListener();
        etNotice.addTextChangedListener(new AbstractTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                noticeText = s.toString();
                tvNoticeNum.setText(String.format(getString(R.string.txt_calc_num), noticeText.length()));
                initNextButton();
            }
        });
    }

    /**
     * 界面处理
     */
    private void initPage() {
        publicTitleBarTitle.setText(R.string.title_edit_transfer);
        tvNotRequired.setVisibility(View.GONE);
        tvHospital.setText(receiveHospital);
        tvHospital.setSelected(true);
        tvTime.setText(reserveTime);
        tvTime.setSelected(true);
    }

    /**
     * 判断
     */
    private void initNextButton() {
        if (!TextUtils.isEmpty(noticeText) && !TextUtils.isEmpty(receiveHospital) && !TextUtils.isEmpty(reserveTime)) {
            tvSubmit.setSelected(true);
        }
        else {
            tvSubmit.setSelected(false);
        }
    }

    @OnClick({ R.id.layout_hospital, R.id.layout_time, R.id.tv_submit })
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.layout_hospital:
                intent = new Intent(this, SelectHospitalByTransferActivity.class);
                startActivityForResult(intent, REQUEST_CODE_HOSPITAL);
                break;
            case R.id.layout_time:
                initCustomTimePicker();
                break;
            case R.id.tv_submit:
                if (tvSubmit.isSelected()) {
                    finish();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_HOSPITAL) {
            if (data != null) {
                receiveHospital = data.getStringExtra(CommonData.KEY_HOSPITAL_BEAN);
                tvHospital.setText(receiveHospital);
                tvHospital.setSelected(true);
                initNextButton();
            }
        }
    }

    private void initCustomTimePicker() {
        TimePickerHelper.showTimePicker(this, date -> {
            reserveTime = BaseUtils.formatDate(date, BaseUtils.YYYY_MM_DD_HH_MM);
            tvTime.setText(reserveTime);
            tvTime.setSelected(true);
            initNextButton();
        });
    }
}
