package com.yht.yihuantong.utils;

import android.content.Context;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.yht.frame.widgets.picker.builder.TimePickerBuilder;
import com.yht.frame.widgets.picker.view.TimePickerView;
import com.yht.yihuantong.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author 顿顿
 * @date 19/6/28 16:40
 * @description
 */
public class TimePickerHelper {
    private static TimePickerView timePickerView;

    public static void showTimePicker(Context context, CallBack callBack) {
        Calendar startDate = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH");
        String time = simpleDateFormat.format(new Date());
        String[] strings = time.split("-");
        startDate.set(Integer.parseInt(strings[0]), Integer.parseInt(strings[1]) - 1,
                Integer.parseInt(strings[02]),
                Integer.parseInt(strings[3]) + 1, 0);
        Calendar endDate = Calendar.getInstance();
        int endMonth = Integer.parseInt(strings[1]) == 1 ? 12 : Integer.parseInt(strings[1]) - 1;
        endDate.set(Integer.parseInt(strings[0]) + 1, endMonth, 31, 23, 0);
        //时间选择器 ，自定义布局
        timePickerView = new TimePickerBuilder(context, (date, v) -> {
            //选中事件回调
            if (callBack != null) {
                callBack.result(date);
            }
        }).setRangDate(startDate, endDate)
                .setLayoutRes(R.layout.view_time_picker, v -> {
                    final TextView tvSubmit = v.findViewById(R.id.tv_sure);
                    TextView ivCancel = v.findViewById(R.id.tv_cancel);
                    tvSubmit.setOnClickListener(v1 -> {
                        timePickerView.returnData();
                        timePickerView.dismiss();
                    });
                    ivCancel.setOnClickListener(v12 -> timePickerView.dismiss());
                })
                .setType(new boolean[]{false, true, true, true, false, false})
                .setLabel("年", "月", "日", "时", "分", "秒")
                .setLineSpacingMultiplier(2.5f)
                .setContentTextSize(18)
                .setTextColorCenter(ContextCompat.getColor(context, R.color.color_373d4d))
                .setTextColorOut(ContextCompat.getColor(context, R.color.color_a1a8b3))
                .setTextXOffset(0, 0, 0, 0, 0, 0)
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setDividerColor(ContextCompat.getColor(context, R.color.color_a1a8b3))
                .build();
        timePickerView.show();
    }

    public interface CallBack {
        void result(Date date);
    }
}
