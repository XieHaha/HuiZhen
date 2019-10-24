package com.yht.yihuantong.ui.reservation.time;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;
import com.yht.frame.data.bean.TimeBarBean;
import com.yht.frame.http.retrofit.RequestUtils;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.utils.HuiZhenLog;
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
        implements CalendarView.OnCalendarSelectListener, CalendarView.OnMonthChangeListener,
                   BaseQuickAdapter.OnItemChildClickListener {
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
    @BindView(R.id.tv_selected_hours)
    TextView tvSelectedHours;
    @BindView(R.id.iv_subtract)
    ImageView ivSubtract;
    @BindView(R.id.iv_add)
    ImageView ivAdd;
    private int tempYear, tempMonth;
    /**
     * 时间条
     */
    private TimeSelectionAdapter timeSelectionAdapter;
    private ArrayList<TimeBarBean> timeBarBeans = new ArrayList<>();
    /**
     * 当前选中时间点position
     */
    private ArrayList<Integer> selectPositions = new ArrayList<>();
    /**
     * 开始选中时间点
     */
    private int startSelectedPosition = -1;
    /**
     * 时间可选起点
     */
    private int startPosition = -1;
    /**
     * 显示的起始时间
     */
    private final int START_HOUR = 6;

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
        initHourData();
        timeSelectionAdapter = new TimeSelectionAdapter(R.layout.item_time_selection, timeBarBeans);
        timeSelectionAdapter.setOnItemChildClickListener(this);
        //水平显示
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(timeSelectionAdapter);
        tvSelectedHours.setText(String.format(getString(R.string.txt_selected_hours), "0.0"));
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        initCalendarView();
        initScheme();
    }

    @Override
    public void initListener() {
        super.initListener();
        calendarView.setOnMonthChangeListener(this);
        calendarView.setOnCalendarSelectListener(this);
    }

    @Override
    public void fillNetWorkData() {
        getRemoteTime();
    }

    /**
     * 在当天日期查询已经有的预约时间信息
     */
    private void getRemoteTime() {
        RequestUtils.getRemoteTime(this, loginBean.getToken(), "", this);
    }

    /**
     * 初始化需要显示的小时数据
     */
    private void initHourData() {
        //总共需要显示的时间bar数量
        int allTimeBar = 36;
        for (int i = 0; i < allTimeBar; i++) {
            TimeBarBean bean = new TimeBarBean();
            bean.setHour(i / 2 + START_HOUR);
            bean.setPosition(i);
            if (i % 2 == 0) {
                bean.setHourString(i / 2 + START_HOUR + "时");
            }
            else {
                bean.setHourString("");
            }
            timeBarBeans.add(bean);
        }
    }

    /**
     * 日历初始化
     */
    private void initCalendarView() {
        tempYear = calendarView.getCurYear();
        tempMonth = calendarView.getCurMonth();
        //设置范围 当前时间往后50年
        calendarView.setRange(tempYear, tempMonth, calendarView.getCurDay(), tempYear + 50, 12, 31);
        //设置显示起始星期
        calendarView.setWeekStarWithMon();
        //设置高
        calendarView.setCalendarItemHeight(BaseUtils.dp2px(this, 32));
        //初始化
        tvYear.setText(String.format(getString(R.string.txt_year_and_month), tempYear, tempMonth));
        ivLeft.setSelected(false);
        ivRight.setSelected(true);
        //获取当前时间
        java.util.Calendar todayCalendar = java.util.Calendar.getInstance();
        int hour = todayCalendar.get(java.util.Calendar.HOUR_OF_DAY);
        int minute = todayCalendar.get(java.util.Calendar.MINUTE);
        if (hour >= START_HOUR) {
            startPosition = (hour - START_HOUR) * 2 + (minute >= 30 ? 1 : 0);
            timeSelectionAdapter.setRange(startPosition);
        }
        else {
            startPosition = 0;
            timeSelectionAdapter.setRange(startPosition);
        }
        recyclerView.scrollToPosition(startPosition);
    }

    /**
     * 处理当天以前的置灰
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

    /**
     * 加时间
     */
    private void add() {
        ivAdd.setSelected(true);
        ivSubtract.setSelected(true);
        //把已选时间点添加到列表中
        selectPositions.add(startSelectedPosition + selectPositions.size());
        updateSelectedHours();
    }

    /**
     * 减时间
     */
    private void subtract() {
        if (selectPositions.size() <= 0) {
            ivAdd.setSelected(false);
            ivSubtract.setSelected(false);
        }
        updateSelectedHours();
    }

    /**
     * 更新已选小时
     */
    private void updateSelectedHours() {
        //计算已选时间
        String selectedHours = String.valueOf(0.5 * selectPositions.size());
        tvSelectedHours.setText(String.format(getString(R.string.txt_selected_hours), selectedHours));
        timeSelectionAdapter.setSelectPositions(selectPositions);
        timeSelectionAdapter.notifyDataSetChanged();
        //多次添加后加个滚动
        recyclerView.scrollToPosition(startSelectedPosition + selectPositions.size());
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        if (position <= startPosition) { return; }
        //清除已选时间
        selectPositions.clear();
        startSelectedPosition = position;
        add();
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
                if (ivSubtract.isSelected()) {
                    //移除最后一位
                    selectPositions.remove(selectPositions.size() - 1);
                    subtract();
                }
                break;
            case R.id.iv_add:
                if (ivAdd.isSelected()) {
                    add();
                }
                break;
            case R.id.tv_clear_optional:
                selectPositions.clear();
                subtract();
                break;
            case R.id.tv_verify_time:
                HuiZhenLog.i(TAG, "startHour:" + timeBarBeans.get(selectPositions.get(0)).getHour() + "endHour:" +
                                  timeBarBeans.get(timeBarBeans.size() - 1).getHour());
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
        //改变日期后 清除已选的
        selectPositions.clear();
        subtract();
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
