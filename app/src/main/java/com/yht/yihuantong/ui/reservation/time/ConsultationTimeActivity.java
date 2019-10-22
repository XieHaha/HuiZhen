package com.yht.yihuantong.ui.reservation.time;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.utils.ToastUtil;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ui.adapter.TimeSelectionAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.tv_appointed)
    TextView tvAppointed;
    @BindView(R.id.tv_optional)
    TextView tvOptional;
    @BindView(R.id.iv_subtract)
    ImageView ivSubtract;
    @BindView(R.id.iv_add)
    ImageView ivAdd;
    private int tempYear, tempMonth;
    /**
     * 时间条
     */
    private TimeSelectionAdapter timeSelectionAdapter;
    private ArrayList<String> times = new ArrayList<>();

    @Override
    protected boolean isInitBackBtn() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.act_consultation_time;
    }

    @Override
    public void initView(@NonNull Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        for (int i = 0; i < 38; i++) {
            if (i % 2 == 0) {
                times.add(i / 2 + 6 + "时");
            }
            else {
                times.add("");
            }
        }
        timeSelectionAdapter = new TimeSelectionAdapter(R.layout.item_time_selection, times);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(timeSelectionAdapter);
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        tempYear = calendarView.getCurYear();
        tempMonth = calendarView.getCurMonth();
        //设置范围 当前时间往后50年
        calendarView.setRange(tempYear, tempMonth, calendarView.getCurDay(), tempYear + 50, 12, 31);
        //设置显示起始星期
        calendarView.setWeekStarWithMon();
        //初始化
        tvYear.setText(String.format(getString(R.string.txt_year_and_month), tempYear, tempMonth));
        ivLeft.setSelected(false);
        ivRight.setSelected(true);
        initScheme();
    }

    @Override
    public void initListener() {
        super.initListener();
        calendarView.setOnMonthChangeListener(this);
        calendarView.setOnCalendarSelectListener(this);
    }

    /**
     * 主要处理当天以前的置灰
     */
    private void initScheme() {
        Map<String, Calendar> map = new HashMap<>();
        int curDay = calendarView.getCurDay();
        for (int i = 1; i < curDay; i++) {
            map.put(getSchemeCalendar(i).toString(), getSchemeCalendar(i));
        }
        calendarView.setSchemeDate(map);
    }

    private Calendar getSchemeCalendar(int day) {
        Calendar calendar = new Calendar();
        calendar.setYear(tempYear);
        calendar.setMonth(tempMonth);
        calendar.setDay(day);
        return calendar;
    }

    @OnClick({
            R.id.iv_left, R.id.iv_right, R.id.iv_subtract, R.id.iv_add, R.id.tv_clear_optional, R.id.tv_verify_time })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_left:
                if (ivLeft.isSelected()) {
                    calendarView.scrollToPre(true);
                }
                break;
            case R.id.iv_right:
                if (ivRight.isSelected()) { calendarView.scrollToNext(true); }
                break;
            case R.id.iv_subtract:
                break;
            case R.id.iv_add:
                break;
            case R.id.tv_clear_optional:
                break;
            case R.id.tv_verify_time:
                break;
            default:
                break;
        }
    }

    @Override
    public void onCalendarOutOfRange(Calendar calendar) {
        ToastUtil.toast(this, "不可选择");
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onCalendarSelect(Calendar calendar, boolean isClick) {
        tvYear.setText(String.format(getString(R.string.txt_year_and_month), calendar.getYear(), calendar.getMonth()));
    }

    @Override
    public void onMonthChange(int year, int month) {
        if (tempYear < year) {
            ivLeft.setSelected(true);
        }
        else if (tempYear == year) {
            if (tempMonth < month) {
                ivLeft.setSelected(true);
            }
            else {
                ivLeft.setSelected(false);
            }
        }
        else {
            ivLeft.setSelected(false);
            ivRight.setSelected(false);
        }
    }
}
