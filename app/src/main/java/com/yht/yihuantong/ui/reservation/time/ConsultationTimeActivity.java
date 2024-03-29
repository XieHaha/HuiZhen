package com.yht.yihuantong.ui.reservation.time;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;
import com.yht.frame.data.BaseResponse;
import com.yht.frame.data.CommonData;
import com.yht.frame.data.Tasks;
import com.yht.frame.data.bean.RemoteHourBean;
import com.yht.frame.data.bean.TimeBarBean;
import com.yht.frame.http.retrofit.RequestUtils;
import com.yht.frame.ui.BaseActivity;
import com.yht.frame.utils.BaseUtils;
import com.yht.frame.utils.ToastUtil;
import com.yht.yihuantong.R;
import com.yht.yihuantong.ui.adapter.TimeSelectionAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
    @BindView(R.id.tv_verify_time)
    TextView tvVerifyTime;
    /**
     * 当前的年月
     */
    private int nowYear, nowMonth, nowDay;
    /**
     * 选中的时间
     */
    private Calendar selectCalendar;
    private LinearLayoutManager layoutManager;
    /**
     * 时间条
     */
    private TimeSelectionAdapter timeSelectionAdapter;
    private ArrayList<TimeBarBean> timeBarBeans = new ArrayList<>();
    /**
     * 已被预约的时间
     */
    private ArrayList<RemoteHourBean> appointedHours;
    /**
     * 已被预约时间点position
     */
    private ArrayList<Integer> appointedPositions = new ArrayList<>();
    /**
     * 当前选中时间点position
     */
    private ArrayList<Integer> selectPositions = new ArrayList<>();
    /**
     * 常量值
     */
    private List<String> hour, hourText;
    /**
     * 远程会诊时间范围
     */
    private String date, startHour, endHour;
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
    /**
     * 总共需要显示的时间bar数量
     */
    private final int ALL_TIME_BAR = 36;

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
        if (getIntent() != null) {
            date = getIntent().getStringExtra(CommonData.KEY_REMOTE_DATE);
            startHour = getIntent().getStringExtra(CommonData.KEY_REMOTE_START_HOUR);
            endHour = getIntent().getStringExtra(CommonData.KEY_REMOTE_END_HOUR);
        }
        initHourData();
        timeSelectionAdapter = new TimeSelectionAdapter(R.layout.item_time_selection, timeBarBeans);
        timeSelectionAdapter.setOnItemChildClickListener(this);
        //水平显示
        recyclerView.setLayoutManager(
                layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,
                        false));
        recyclerView.setAdapter(timeSelectionAdapter);
        //已选
        tvSelectedHours.setText(String.format(getString(R.string.txt_selected_hours), "0.0"));
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        initCalendarView();
        initScheme();
        //已选时间回填
        if (!TextUtils.isEmpty(date)) {
            calcSelected();
        }
    }

    @Override
    public void initListener() {
        super.initListener();
        calendarView.setOnMonthChangeListener(this);
        calendarView.setOnCalendarSelectListener(this);
    }

    @Override
    public void fillNetWorkData() {
        //获取当前日期的预约情况
        getRemoteTime(BaseUtils.formatDate(System.currentTimeMillis(), BaseUtils.YYYY_MM_DD));
    }

    /**
     * 在当天日期查询已经有的预约时间信息
     */
    private void getRemoteTime(String date) {
        RequestUtils.getRemoteTime(this, loginBean.getToken(), date, this);
    }

    /**
     * 日历初始化
     */
    private void initCalendarView() {
        nowYear = calendarView.getCurYear();
        nowMonth = calendarView.getCurMonth();
        nowDay = calendarView.getCurDay();
        //设置范围 当前时间往后50年
        calendarView.setRange(nowYear, nowMonth, calendarView.getCurDay(), nowYear + 50, 12, 31);
        //设置显示起始星期
        calendarView.setWeekStarWithMon();
        //设置高
        calendarView.setCalendarItemHeight(BaseUtils.dp2px(this, 40));
        //初始化
        tvYear.setText(String.format(getString(R.string.txt_year_and_month), nowYear, nowMonth));
        ivLeft.setSelected(false);
        ivRight.setSelected(true);
    }

    /**
     * 初始化需要显示的小时数据
     */
    private void initHourData() {
        hour = Arrays.asList(getResources().getStringArray(R.array.hour));
        hourText = Arrays.asList(getResources().getStringArray(R.array.hourText));
        for (int i = 0; i < ALL_TIME_BAR; i++) {
            TimeBarBean bean = new TimeBarBean();
            bean.setPosition(i);
            bean.setHourString(hour.get(i));
            if (i % 2 == 0) {
                bean.setHourTxt(hourText.get(i / 2));
            }
            timeBarBeans.add(bean);
        }
    }

    /**
     * 计算当前已选的时间
     */
    private void calcSelected() {
        int startPosition = hour.indexOf(startHour);
        startSelectedPosition = startPosition;
        int endPosition;
        if (TextUtils.equals(endHour, "23:59")) {
            endPosition = hour.size();
        } else {
            endPosition = hour.indexOf(endHour);
        }
        int size = endPosition - startPosition;
        for (int j = 0; j < size; j++) {
            selectPositions.add(startPosition);
            startPosition++;
        }
        if (selectPositions.size() > 0) {
            ivSubtract.setSelected(true);
            tvVerifyTime.setSelected(true);
            int total = startSelectedPosition + selectPositions.size();
            //可预约时间已经超过24点  不可再加.
            if (total == ALL_TIME_BAR) {
                ivAdd.setSelected(false);
            } else {
                ivAdd.setSelected(true);
            }
            //计算已选时间
            String selectedHours = String.valueOf(selectPositions.size() / 2f);
            tvSelectedHours.setText(String.format(getString(R.string.txt_selected_hours),
                    selectedHours));
        }
    }

    /**
     * 统计已被预约的时间段
     */
    private void calcAppointed() {
        appointedPositions.clear();
        if (appointedHours == null || appointedHours.size() == 0) {
            return;
        }
        String[] hour = getResources().getStringArray(R.array.hour);
        List<String> hourStrings = Arrays.asList(hour);
        for (int i = 0; i < appointedHours.size(); i++) {
            RemoteHourBean bean = appointedHours.get(i);
            //如果结束时间已经比当前时间小，就不再统计
            if (bean.getEndAt() < System.currentTimeMillis()) {
                continue;
            }
            String startHour = BaseUtils.formatDate(bean.getStartAt(), BaseUtils.HH_MM);
            String endHour = BaseUtils.formatDate(bean.getEndAt(), BaseUtils.HH_MM);
            int startPosition = hourStrings.indexOf(startHour);
            int size = hourStrings.indexOf(endHour) - startPosition;
            for (int j = 0; j < size; j++) {
                appointedPositions.add(startPosition);
                startPosition++;
            }
        }
    }

    /**
     * 计算可选时间范围 （已过期时间及已预约时间 ）
     */
    private void calcHourRange() {
        selectCalendar = calendarView.getSelectedCalendar();
        boolean today =
                selectCalendar.getDay() == nowDay && selectCalendar.getMonth() == nowMonth &&
                        selectCalendar.getYear() == nowYear;
        int hour, minute;
        if (today) {
            //获取当前时间
            java.util.Calendar todayCalendar = java.util.Calendar.getInstance();
            hour = todayCalendar.get(java.util.Calendar.HOUR_OF_DAY);
            minute = todayCalendar.get(java.util.Calendar.MINUTE);
        } else {
            //默认从6点开始
            hour = 5;
            minute = 59;
        }
        //计算可选时段的开始时间
        if (hour >= START_HOUR) {
            //当前时间大于6点
            startPosition = (hour - START_HOUR) * 2 + (minute >= 30 ? 2 : 1);
        } else {
            //当前时间在6点之前
            startPosition = 0;
        }
        //设置时段范围
        timeSelectionAdapter.setRange(startPosition);
        timeSelectionAdapter.setRangePosition(appointedPositions);
        timeSelectionAdapter.setSelectPositions(selectPositions);
        timeSelectionAdapter.notifyDataSetChanged();
        //滚动至可选时段
        layoutManager.scrollToPositionWithOffset(startPosition > 1 ? startPosition - 1 :
                startPosition, 0);
        //可选
        tvOptional.setText(String.format(getString(R.string.txt_optional),
                String.valueOf((24 - hour - 1) + (minute >= 30 ? 0 : 0.5))));
    }

    /**
     * 处理当天以前的置灰
     */
    private void initScheme() {
        Map<String, Calendar> map = new HashMap<>(16);
        int curDay = calendarView.getCurDay();
        for (int i = 1; i < curDay; i++) {
            map.put(getSchemeCalendar(i).toString(), getSchemeCalendar(i));
        }
        calendarView.setSchemeDate(map);
    }

    private Calendar getSchemeCalendar(int day) {
        Calendar calendar = new Calendar();
        calendar.setYear(nowYear);
        calendar.setMonth(nowMonth);
        calendar.setDay(day);
        return calendar;
    }

    /**
     * 加时间
     */
    private void add(boolean scroll) {
        ivSubtract.setSelected(true);
        tvVerifyTime.setSelected(true);
        int total = startSelectedPosition + selectPositions.size();
        //添加时间 （不可再添加已预约时间段）
        if (appointedPositions.contains(total)) {
            ToastUtil.toast(this, R.string.txt_add_hour_error_hint);
            return;
        }
        //把已选时间点添加到列表中
        selectPositions.add(total);
        total = startSelectedPosition + selectPositions.size();
        //可预约时间已经超过24点  不可再加.
        if (total == ALL_TIME_BAR) {
            ivAdd.setSelected(false);
        } else {
            ivAdd.setSelected(true);
        }
        updateSelectedHours(scroll);
    }

    /**
     * 清除时间或减少时间
     */
    private void subtract(boolean scroll) {
        if (selectPositions.size() <= 0) {
            ivAdd.setSelected(false);
            ivSubtract.setSelected(false);
            tvVerifyTime.setSelected(false);
        } else {
            ivAdd.setSelected(true);
        }
        updateSelectedHours(scroll);
    }

    /**
     * 更新已选小时
     */
    private void updateSelectedHours(boolean scroll) {
        //计算已选时间
        String selectedHours = String.valueOf(0.5 * selectPositions.size());
        tvSelectedHours.setText(String.format(getString(R.string.txt_selected_hours),
                selectedHours));
        timeSelectionAdapter.setSelectPositions(selectPositions);
        timeSelectionAdapter.notifyDataSetChanged();
        //多次添加后加个滚动
        //        if (scroll) {
        //            int scrollPosition = (startSelectedPosition + selectPositions.size()) > 2
        //                                 ? startSelectedPosition + selectPositions.size() - 2
        //                                 : startSelectedPosition + selectPositions.size();
        //            layoutManager.scrollToPositionWithOffset(scrollPosition, 0);
        //        }
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        if (position < startPosition || appointedPositions.contains(position)) {
            return;
        }
        //清除已选时间
        selectPositions.clear();
        startSelectedPosition = position;
        add(false);
    }

    @OnClick({
            R.id.iv_left, R.id.iv_right, R.id.iv_subtract, R.id.iv_add, R.id.tv_clear_optional,
            R.id.tv_verify_time})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_left:
                if (ivLeft.isSelected()) {
                    calendarView.scrollToPre(true);
                }
                break;
            case R.id.iv_right:
                if (ivRight.isSelected()) {
                    calendarView.scrollToNext(true);
                }
                break;
            case R.id.iv_subtract:
                if (ivSubtract.isSelected()) {
                    //移除最后一位
                    selectPositions.remove(selectPositions.size() - 1);
                    subtract(false);
                } else {
                    ToastUtil.toast(this, R.string.txt_add_hour_hint);
                }
                break;
            case R.id.iv_add:
                if (ivAdd.isSelected()) {
                    add(true);
                } else {
                    if (selectPositions != null && selectPositions.size() > 0) {
                        ToastUtil.toast(this, "超过限制");
                    } else {
                        ToastUtil.toast(this, R.string.txt_add_hour_hint);
                    }
                }
                break;
            case R.id.tv_clear_optional:
                selectPositions.clear();
                subtract(false);
                break;
            case R.id.tv_verify_time:
                if (tvVerifyTime.isSelected()) {
                    selectHourFinish();
                } else {
                    ToastUtil.toast(this, R.string.txt_add_hour_empty_hint);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 确认时间
     */
    private void selectHourFinish() {
        //获取当前选择的日期
        Calendar calendar = calendarView.getSelectedCalendar();
        String date = calendar.getYear() + "-" + calendar.getMonth() + "-" + calendar.getDay();
        //开始时间
        String startHour = timeBarBeans.get(selectPositions.get(0)).getHourString();
        //截止时间
        String endHour;
        int position = selectPositions.get(selectPositions.size() - 1);
        if (position >= timeBarBeans.size() - 1) {
            endHour = "23:59";
        } else {
            endHour = timeBarBeans.get(position + 1).getHourString();
        }
        //回调到科室列表
        Intent intent = new Intent();
        intent.putExtra(CommonData.KEY_REMOTE_DATE, date);
        intent.putExtra(CommonData.KEY_REMOTE_START_HOUR, startHour);
        intent.putExtra(CommonData.KEY_REMOTE_END_HOUR, endHour);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onCalendarOutOfRange(Calendar calendar) {
        ToastUtil.toast(this, "不可选择");
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onCalendarSelect(Calendar calendar, boolean isClick) {
        tvYear.setText(String.format(getString(R.string.txt_year_and_month), calendar.getYear(),
                calendar.getMonth()));
        //改变日期后 清除已选的
        selectPositions.clear();
        subtract(false);
        //重新计算可选时间区间,获取选择日期的预约情况
        getRemoteTime(calendar.getYear() + "-" + calendar.getMonth() + "-" + calendar.getDay());
    }

    @Override
    public void onMonthChange(int year, int month) {
        if (nowYear < year) {
            ivLeft.setSelected(true);
        } else if (nowYear == year) {
            if (nowMonth < month) {
                ivLeft.setSelected(true);
            } else {
                ivLeft.setSelected(false);
            }
        } else {
            ivLeft.setSelected(false);
            ivRight.setSelected(false);
        }
    }

    @Override
    public void onResponseSuccess(Tasks task, BaseResponse response) {
        super.onResponseSuccess(task, response);
        if (task == Tasks.GET_REMOTE_TIME) {
            appointedHours = (ArrayList<RemoteHourBean>) response.getData();
            //统计已被预约的时间段
            calcAppointed();
            //统计可以预约的开始时间
            calcHourRange();
        }
    }
}
