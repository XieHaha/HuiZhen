package com.zyc.doctor.ui.transfer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.yht.frame.data.CommonData;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.widgets.picker.builder.TimePickerBuilder;
import com.yht.frame.widgets.picker.listener.CustomListener;
import com.yht.frame.widgets.picker.view.TimePickerView;
import com.zyc.doctor.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
     * 时间选择器
     */
    private TimePickerView timePickerView;
    /**
     * true 为变更接诊信息  false为接诊
     */
    private boolean isEditReceive;
    /**
     * 接诊医院  预约就诊时间
     */
    private String receiveHospital, reserveTime;
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
                String hospital = data.getStringExtra(CommonData.KEY_HOSPITAL_BEAN);
                tvHospital.setText(hospital);
                tvHospital.setSelected(true);
            }
        }
    }

    private void initCustomTimePicker() {
        /**
         * @description
         *
         * 注意事项：
         * 1.自定义布局中，id为 optionspicker 或者 timepicker 的布局以及其子控件必须要有，否则会报空指针.
         * 具体可参考demo 里面的两个自定义layout布局。
         * 2.因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
         * setRangDate方法控制起始终止时间(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
         */
        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        Calendar startDate = Calendar.getInstance();
        startDate.set(2014, 1, 23);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2027, 2, 28);
        //时间选择器 ，自定义布局
        timePickerView = new TimePickerBuilder(this, (date, v) -> {//选中事件回调
            tvTime.setText(getTime(date));
        })
                /*.setType(TimePickerView.Type.ALL)//default is all
                .setCancelText("Cancel")
                .setSubmitText("Sure")
                .setContentTextSize(18)
                .setTitleSize(20)
                .setTitleText("Title")
                .setTitleColor(Color.BLACK)
               /*.setDividerColor(Color.WHITE)//设置分割线的颜色
                .setTextColorCenter(Color.LTGRAY)//设置选中项的颜色
                .setLineSpacingMultiplier(1.6f)//设置两横线之间的间隔倍数
                .setTitleBgColor(Color.DKGRAY)//标题背景颜色 Night mode
                .setBgColor(Color.BLACK)//滚轮背景颜色 Night mode
                .setSubmitColor(Color.WHITE)
                .setCancelColor(Color.WHITE)*/
                /*.animGravity(Gravity.RIGHT)// default is center*/.setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setLayoutRes(R.layout.view_time_picker, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = v.findViewById(R.id.tv_sure);
                        TextView ivCancel = v.findViewById(R.id.tv_cancel);
                        tvSubmit.setOnClickListener(v1 -> {
                            timePickerView.returnData();
                            timePickerView.dismiss();
                        });
                        ivCancel.setOnClickListener(v12 -> timePickerView.dismiss());
                    }
                })
                .setType(new boolean[] { false, true, true, true, false, false })
                .setLabel("年", "月", "日", "时", "分", "秒")
                .setLineSpacingMultiplier(2.5f)
                .setContentTextSize(18)
                .setTextColorCenter(ContextCompat.getColor(this, R.color.color_373d4d))
                .setTextColorOut(ContextCompat.getColor(this, R.color.color_a1a8b3))
                .setTextXOffset(0, 0, 0, 0, 0, 0)
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setDividerColor(ContextCompat.getColor(this, R.color.color_a1a8b3))
                .build();
        timePickerView.show();
    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
        Log.d("getTime()", "choice date millis: " + date.getTime());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }
}
