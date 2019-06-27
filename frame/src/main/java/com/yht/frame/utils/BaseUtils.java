package com.yht.frame.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.TypedValue;

import com.github.promeg.pinyinhelper.Pinyin;
import com.yht.frame.data.BaseData;
import com.yht.frame.data.bean.PatientBean;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 工具类
 *
 * @author dundun
 */
public class BaseUtils {
    private static final String TAG = BaseUtils.class.getSimpleName();
    private static final String REGEX_PHONE = "^((13[0-9])|(14[0-9])|(15[0-9])|(16[0-9])|(17[0-9])|(18[0-9])|(19[0-9]))\\d{8}$";
    private static final String REGEX_CARD_NUM = "(^[1-8][0-7]{2}\\d{3}([12]\\d{3})(0[1-9]|1[012])(0[1-9]|[12]\\d|3[01])\\d{3}([0-9Xx])$)";
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    private static final int BUFFER_SIZE = 1024 * 2;

    /**
     * 网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvaliable(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return null != networkInfo && networkInfo.isConnected();
    }

    /**
     * 将dp值转换为像素值
     */
    public static int dp2px(Context context, int dp) {
        return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                                              context.getResources().getDisplayMetrics());
    }

    public static int px2dp(Context context, float pxValue) {
        return (int)(pxValue / context.getResources().getDisplayMetrics().density + 0.5f);
    }

    /**
     * 字符串转换为java.util.Date<br>
     * 支持格式为yyyy/MM/dd HH:mm:ss 如 '2002/1/1 17:55:00'<br>
     * yyyy/MM/dd HH:mm:ss pm 如 '2002/1/1 17:55:00 pm'<br>
     * yyyy-MM-dd HH:mm:ss 如 '2002-1-1 17:55:00' <br>
     * yyyy-MM-dd HH:mm:ss am 如 '2002-1-1 17:55:00 am' <br>
     *
     * @param time String 字符串<br>
     * @return Date 日期<br>
     */
    public static Date formatDate(String time) {
        time = time.trim();
        String formatter = "yyyy-MM-dd HH:mm:ss";
        if ((time.indexOf("/") > -1) && (time.indexOf(" ") > -1)) {
            formatter = "yyyy/MM/dd HH:mm:ss";
        }
        else if ((time.indexOf("/") > -1) && (time.indexOf("am") > -1) || (time.indexOf("pm") > -1)) {
            formatter = "yyyy/MM/dd KK:mm:ss a";
        }
        else if ((time.indexOf("-") > -1) && (time.indexOf("am") > -1) || (time.indexOf("pm") > -1)) {
            formatter = "yyyy-MM-dd KK:mm:ss a";
        }
        return formatDate(time, formatter);
    }

    /**
     * 字符串转换为java.util.Date<br>
     *
     * @param time   String 字符串<br>
     * @param format String 字符串<br>
     * @return Date 日期<br>
     */
    public static Date formatDate(String time, String format) {
        SimpleDateFormat formatter;
        time = time.trim();
        formatter = new SimpleDateFormat(format, Locale.getDefault());
        Date ctime = formatter.parse(time, new ParsePosition(0));
        return ctime;
    }

    /**
     * 时间戳转为北京时间
     *
     * @param time
     * @param format
     * @return
     */
    public static String formatDate(long time, String format) {
        if (format != null) {
            return new SimpleDateFormat(format).format(new Date(time));
        }
        return "";
    }

    public static String getAge(long time) {
        try {
            Date birthDay = new Date(time);
            Calendar cal = Calendar.getInstance();
            if (cal.before(birthDay)) {
                throw new IllegalArgumentException("The birthDay is before Now.It's unbelievable!");
            }
            int yearNow = cal.get(Calendar.YEAR);
            int monthNow = cal.get(Calendar.MONTH);
            int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
            cal.setTime(birthDay);
            int yearBirth = cal.get(Calendar.YEAR);
            int monthBirth = cal.get(Calendar.MONTH);
            int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
            int age = yearNow - yearBirth;
            if (monthNow <= monthBirth) {
                if (monthNow == monthBirth) {
                    if (dayOfMonthNow < dayOfMonthBirth) {
                        age--;
                    }
                }
                else {
                    age--;
                }
            }
            return String.valueOf(age);
        }
        catch (IllegalArgumentException e) {
            LogUtils.w(TAG, "IllegalArgumentException error", e);
            return "0";
        }
    }

    /**
     * 判断是否是手机号码
     *
     * @param str
     * @return
     */
    public static boolean isMobileNumber(String str) {
        if (!TextUtils.isEmpty(str)) {
            Matcher phoneMatcher = Pattern.compile(REGEX_PHONE).matcher(str);
            if (phoneMatcher.matches()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否身份证号
     *
     * @param str
     * @return
     */
    public static boolean isCardNum(String str) {
        if (!TextUtils.isEmpty(str)) {
            Matcher phoneMatcher = Pattern.compile(REGEX_CARD_NUM).matcher(str);
            if (phoneMatcher.matches()) {
                return true;
            }
        }
        return false;
    }

    public static int copy(InputStream input, OutputStream output) throws Exception, IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        BufferedInputStream in = new BufferedInputStream(input, BUFFER_SIZE);
        BufferedOutputStream out = new BufferedOutputStream(output, BUFFER_SIZE);
        int count = 0, n = 0;
        try {
            while ((n = in.read(buffer, 0, BUFFER_SIZE)) != -1) {
                out.write(buffer, 0, n);
                count += n;
            }
            out.flush();
        }
        finally {
            try {
                out.close();
            }
            catch (IOException e) {
                LogUtils.e(TAG, e.getMessage());
            }
            try {
                in.close();
            }
            catch (IOException e) {
                LogUtils.e(TAG, e.getMessage());
            }
        }
        return count;
    }

    /**
     * 对数据进行排序
     *
     * @param list 要进行排序的数据源
     */
    public static void sortData(List<PatientBean> list) {
        if (list == null || list.size() == 0) { return; }
        for (int i = 0; i < list.size(); i++) {
            PatientBean bean = list.get(i);
            String tag = Pinyin.toPinyin(bean.getName().substring(0, 1).charAt(0)).substring(0, 1);
            if (tag.matches("[A-Z]")) {
                bean.setIndexTag(tag);
            }
            else {
                bean.setIndexTag("#");
            }
        }
        Collections.sort(list, (o1, o2) -> {
            if ("#".equals(o1.getIndexTag())) {
                return 1;
            }
            else if ("#".equals(o2.getIndexTag())) {
                return -1;
            }
            else {
                return o1.getIndexTag().compareTo(o2.getIndexTag());
            }
        });
    }

    /**
     * @param beans 数据源
     * @return tags 返回一个包含所有Tag字母在内的字符串
     */
    public static String getTags(List<PatientBean> beans) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < beans.size(); i++) {
            if (!builder.toString().contains(beans.get(i).getIndexTag())) {
                builder.append(beans.get(i).getIndexTag());
            }
        }
        return builder.toString();
    }

    /**
     * 根据身份证的号码算出当前身份证持有者的年龄 18位身份证
     *
     * @return
     */
    public static String getAgeByCard(String idCard) {
        if (TextUtils.isEmpty(idCard) || idCard.length() != BaseData.BASE_ID_CARD_LENGTH) {
            return "";
        }
        // 得到年份
        String year = idCard.substring(6).substring(0, 4);
        // 得到月份
        String yue = idCard.substring(10).substring(0, 2);
        // 得到当前的系统时间
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        // 当前年份
        String fyear = format.format(date).substring(0, 4);
        // 月份
        String fyue = format.format(date).substring(5, 7);
        int age = 0;
        // 当前月份大于用户出身的月份表示已过生
        if (Integer.parseInt(yue) <= Integer.parseInt(fyue)) {
            age = Integer.parseInt(fyear) - Integer.parseInt(year) + 1;
        }
        // 当前用户还没过生
        else {
            age = Integer.parseInt(fyear) - Integer.parseInt(year);
        }
        return String.valueOf(age);
    }

    /**
     * 根据身份证号判断性别
     *
     * @param idCard
     * @return
     */
    public static String getSexByCard(String idCard) {
        if (TextUtils.isEmpty(idCard) || idCard.length() != BaseData.BASE_ID_CARD_LENGTH) {
            return "";
        }
        String sex;
        if (Integer.parseInt(idCard.substring(16).substring(0, 1)) % 2 == 0) {
            sex = "女";
        }
        else {
            sex = "男";
        }
        return sex;
    }
}
