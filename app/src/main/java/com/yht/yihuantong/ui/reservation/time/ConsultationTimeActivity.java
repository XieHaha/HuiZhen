package com.yht.yihuantong.ui.reservation.time;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;
import com.yht.frame.ui.BaseActivity;
import com.yht.yihuantong.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author 顿顿
 * @date 19/10/18 17:38
 * @description 远程会诊  时间选择
 */
public class ConsultationTimeActivity extends BaseActivity
        implements CalendarView.OnCalendarSelectListener, CalendarView.OnMonthChangeListener {
    @BindView(R.id.iv_left)
    ImageView ivLeft;
    @BindView(R.id.tv_year)
    TextView tvYear;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    @BindView(R.id.calendarView)
    CalendarView calendarView;
    @BindView(R.id.calendarLayout)
    CalendarLayout calendarLayout;
    private int tempYear, tempMonth;

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_consultation_time;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        tempYear = calendarView.getCurYear();
        tempMonth = calendarView.getCurMonth();
        //设置范围 当前时间往后50年
        calendarView.setRange(tempYear, tempMonth, 1, tempYear + 50, 12, 31);
        //设置显示起始星期
        calendarView.setWeekStarWithMon();
        tvYear.setText(String.format(getString(R.string.txt_year_and_month), tempYear, tempMonth));
        ivLeft.setSelected(false);
        ivRight.setSelected(true);
    }

    @Override
    public void initListener() {
        super.initListener();
        calendarView.setOnMonthChangeListener(this);
        calendarView.setOnCalendarSelectListener(this);
    }

    @OnClick({ R.id.iv_left, R.id.iv_right })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_left:
                //                if (ivLeft.isSelected()) {
                calendarView.scrollToPre();
                //                }
                break;
            case R.id.iv_right:
                calendarView.scrollToNext();
                break;
            default:
                break;
        }
    }

    /**
     * 修改月份后回调
     */
    private void onMonthModify() {
    }

    @Override
    public void onCalendarOutOfRange(Calendar calendar) {
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onCalendarSelect(Calendar calendar, boolean isClick) {
        tvYear.setText(String.format(getString(R.string.txt_year_and_month), calendar.getYear(), calendar.getMonth()));
    }

    @Override
    public void onMonthChange(int year, int month) {
        if (tempYear > calendarView.getCurYear()) {
            ivLeft.setSelected(true);
        }
        else if (tempYear == calendarView.getCurYear()) {
            if (tempMonth > calendarView.getCurMonth()) {
                ivLeft.setSelected(true);
            }
            else {
                ivLeft.setSelected(false);
            }
        }
        else {
            ivLeft.setSelected(false);
        }
    }
}
