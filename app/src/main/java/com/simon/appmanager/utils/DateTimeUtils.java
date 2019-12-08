package com.simon.appmanager.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Simon
 * @Description DateTimeUtils的辅助类
 * @date createTime: 2016-12-10
 */
@SuppressWarnings("all")
public class DateTimeUtils {

    /**
     * 日期统一格式
     */
    private final static SimpleDateFormat sdf_simple = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");
    private final static SimpleDateFormat sdf_l = new SimpleDateFormat(
            "yyyy年MM月dd日 HH:mm:ss");
    private final static SimpleDateFormat sdf_ymd = new SimpleDateFormat(
            "yyyy-MM-dd");
    private final static SimpleDateFormat sdf_hm = new SimpleDateFormat(
            "HH:mm");
    private final static SimpleDateFormat sdf_minute = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm");
    private final static SimpleDateFormat sdf_date_fever = new SimpleDateFormat(
            "yyyy.MM.dd");

    /* 获取系统当前时间-精确到秒{2018-01-01 00:00:00} */
    public static String getCurrentTime() {
        long currentTimeMillis = System.currentTimeMillis();
        String string = sdf_simple.format(currentTimeMillis);
        // Date date = new Date();
        // String str = format.format(date);
        return string;
    }

    /* 获取系统当前年-月-日{2018-01-01 01:01} */
    public static String getCurrentYMDHM() {
        long currentTimeMillis = System.currentTimeMillis();
        return sdf_minute.format(currentTimeMillis);
    }

    /* 获取系统当前年-月-日{2018-01-01} */
    public static String getCurrentYMD() {
        long currentTimeMillis = System.currentTimeMillis();
        return sdf_ymd.format(currentTimeMillis);
    }

    /* 获取系统当前时-分{01:01} */
    public static String getCurrentHM() {
        long currentTimeMillis = System.currentTimeMillis();
        return sdf_hm.format(currentTimeMillis);
    }

    /**
     * 获取系统当前时间-精确到分{2018-01-01 00:00}
     *
     * @return string
     */
    public static String getCurrentMinute() {
        long currentTimeMillis = System.currentTimeMillis();
        String string = sdf_minute.format(currentTimeMillis);
        return string;
    }

    /* 获取下一年{2018-01-01} */
    public static String getNextYear(String currentYear) {
        String nextYear = "2018-01-01";
        if (!TextUtils.isEmpty(currentYear)) {
            try {
                Date date = sdf_ymd.parse(currentYear); // 将当前时间格式化
                // 显示输入的日期
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                cal.add(Calendar.YEAR, 1); // 当前时间加1秒
                date = cal.getTime();
                nextYear = sdf_ymd.format(date); // 加一秒后的时间
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return nextYear;
    }

    /* 获取上一天{2018-01-01} */
    public static String getLastDay() {
        String nextDay = "1970-01-01";
        try {
            Date date = sdf_simple.parse(getCurrentTime()); // 将当前时间格式化
            // 显示输入的日期
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.DAY_OF_MONTH, -1);
            date = cal.getTime();
            nextDay = sdf_simple.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return nextDay;
    }

    /* 获取下一天{2018-01-01} */
    public static String getNextDay(String currentDay, int day) {
        String nextDay = "1970-01-01";
        try {
            Date date = sdf_ymd.parse(currentDay); // 将当前时间格式化
            // 显示输入的日期
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.DAY_OF_MONTH, day);
            date = cal.getTime();
            nextDay = sdf_ymd.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return nextDay;
    }

    /* 获取下一小时{01:01} */
    public static String getNextHour(String currentHour, int hour) {
        String nextYear = "01:01";
        try {
            Date date = sdf_hm.parse(currentHour); // 将当前时间格式化
            // 显示输入的日期
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.HOUR, hour);
            date = cal.getTime();
            nextYear = sdf_hm.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return nextYear;
    }

    /* 获取下一秒的时间 */
    public static String getNextSecond(String currentSecond) {
        String nextSecondDate = "";
        if (!TextUtils.isEmpty(currentSecond)) {
            try {
                Date date = sdf_simple.parse(currentSecond); // 将当前时间格式化
                // 显示输入的日期
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                cal.add(Calendar.SECOND, 1); // 当前时间加1秒
                date = cal.getTime();
                nextSecondDate = sdf_simple.format(date); // 加一秒后的时间
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return nextSecondDate;
    }

    /**
     * 将时间戳转换成日期
     *
     * @param currentTimeMillis
     * @return
     */
    public static String convertMsecToDate(String currentTimeMillis) {
        long l = Long.parseLong(currentTimeMillis);
        String string = sdf_simple.format(l);
        return string;
    }

    public static String convertMsecToYMD(String currentTimeMillis) {
        long l = Long.parseLong(currentTimeMillis);
        String string = sdf_date_fever.format(l);
        return string;
    }

    /* 将日期转换成时间戳 {2018-01-01 01:01:01}*/
    public static Long convertDateToMsec(String dateTime) {
        Date date = new Date();
        try {
            date = sdf_simple.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    /* 将日期转换成时间戳 {2018-01-01 01:01:01}*/
    public static Long convertYMDToMsec(String dateTime) {
        Date date = new Date();
        try {
            date = sdf_ymd.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    /*  将日期转换成时间戳{2018-01-01 01:01} */
    public static Long convertYMDHMToMsec(String dateTime) {
        Date date = new Date();
        try {
            date = sdf_minute.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    /*  将时间转换成时间戳{01:01} */
    public static Long convertHMToMsec(String time) {
        Date date = new Date();
        try {
            date = sdf_hm.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    /* 将日期转换成指定日期{2018-01-01 01:01}*/
    public static String convertCustomDate(String dateTime) {
        Date date = new Date();
        String time = "";
        try {
            date = sdf_simple.parse(dateTime);
            time = sdf_minute.format(date.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    /* 将日期转换成指定日期{2018-01-01}*/
    public static String convertCustomDateYMD(String dateTime) {
        Date date = new Date();
        String time = "";
        try {
            date = sdf_simple.parse(dateTime);
            time = sdf_ymd.format(date.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    /**
     * 获取剩余时间 几天几时几分几秒
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static String getRemainTime(String startTime, String endTime) {
        String remainTime = "0"; // 剩余时间
        long dayMsec = 1000 * 24 * 60 * 60;// 一天的毫秒数
        long hourMsec = 1000 * 60 * 60;// 一小时的毫秒数
        long minuteMsec = 1000 * 60;// 一分钟的毫秒数
        long secondMsec = 1000;// 一秒钟的毫秒数
        long diffMsec; // 毫秒差

        if (startTime != null && !startTime.equals("") && endTime != null
                && !endTime.equals("")) {
            try {
                // 获得两个时间的毫秒时间差异
                diffMsec = sdf_simple.parse(endTime).getTime()
                        - sdf_simple.parse(startTime).getTime();
                if (diffMsec > 0) {
                    /* 判断结束时间是否大于开始时间 */
                    long diffDay = diffMsec / dayMsec;// 计算差多少天
                    long diffHour = diffMsec % dayMsec / hourMsec;// 计算差多少小时
                    long diffMin = diffMsec % dayMsec % hourMsec / minuteMsec;// 计算差多少分钟
                    long diffSec = diffMsec % dayMsec % dayMsec % minuteMsec
                            / secondMsec;// 计算差多少秒//输出结果
                    remainTime = diffDay + "天" + diffHour + "时" + diffMin + "分"
                            + diffSec + "秒";
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return remainTime;
    }

    /**
     * 判断选择时间是否超出当前时间
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static boolean getRemainTimeYMD(String startTime, String endTime) {
        boolean flag = false;
        long dayMsec = 1000 * 24 * 60 * 60;// 一天的毫秒数
        long hourMsec = 1000 * 60 * 60;// 一小时的毫秒数
        long minuteMsec = 1000 * 60;// 一分钟的毫秒数
        long secondMsec = 1000;// 一秒钟的毫秒数
        long diffMsec; // 毫秒差
        if (!TextUtils.isEmpty(startTime) && !TextUtils.isEmpty(startTime)) {
            try {
                // 获得两个时间的毫秒时间差异
                diffMsec = sdf_ymd.parse(endTime).getTime()
                        - sdf_ymd.parse(startTime).getTime();
                if (diffMsec > 0) {
                    flag = false;
                } else {
                    flag = true;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return flag;
    }

    /**
     * 判断选择时间是否超出当前时间
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static boolean getRemainTimeHM(String startTime, String endTime) {
        boolean flag = false;
        long dayMsec = 1000 * 24 * 60 * 60;// 一天的毫秒数
        long hourMsec = 1000 * 60 * 60;// 一小时的毫秒数
        long minuteMsec = 1000 * 60;// 一分钟的毫秒数
        long secondMsec = 1000;// 一秒钟的毫秒数
        long diffMsec; // 毫秒差
        if (!TextUtils.isEmpty(startTime) && !TextUtils.isEmpty(startTime)) {
            try {
                // 获得两个时间的毫秒时间差异
                diffMsec = sdf_hm.parse(endTime).getTime()
                        - sdf_hm.parse(startTime).getTime();
                if (diffMsec > 0) {
                    flag = false;
                } else {
                    flag = true;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return flag;
    }

    public static String getRemainTimeYMDHM(String startTime, String endTime) {
        String remainTime = "0时" + "0分"; // 剩余时间
        long dayMsec = 1000 * 24 * 60 * 60;// 一天的毫秒数
        long hourMsec = 1000 * 60 * 60;// 一小时的毫秒数
        long minuteMsec = 1000 * 60;// 一分钟的毫秒数
        long secondMsec = 1000;// 一秒钟的毫秒数
        long diffMsec; // 毫秒差
        if (!TextUtils.isEmpty(startTime) && !TextUtils.isEmpty(endTime)) {
            try {
                // 获得两个时间的毫秒时间差异
                diffMsec = sdf_minute.parse(endTime).getTime() - sdf_minute.parse(startTime).getTime();
                if (diffMsec > 0) {
                    /* 判断结束时间是否大于开始时间 */
                    long diffHour = diffMsec % dayMsec / hourMsec;// 计算差多少小时
                    long diffMin = diffMsec % dayMsec % hourMsec / minuteMsec;// 计算差多少分钟
                    if (diffHour == 0) {
                        remainTime = diffMin + "分";
                    } else {
                        remainTime = diffHour + "时" + diffMin + "分";
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return remainTime;
    }

    public static boolean getRemainTimeBooleanYMDHM(String startTime, String endTime) {
        boolean flag = false;
        String remainTime = "0时" + "0分"; // 剩余时间
        long dayMsec = 1000 * 24 * 60 * 60;// 一天的毫秒数
        long hourMsec = 1000 * 60 * 60;// 一小时的毫秒数
        long minuteMsec = 1000 * 60;// 一分钟的毫秒数
        long secondMsec = 1000;// 一秒钟的毫秒数
        long diffMsec; // 毫秒差
        if (!TextUtils.isEmpty(startTime) && !TextUtils.isEmpty(endTime)) {
            try {
                // 获得两个时间的毫秒时间差异
                diffMsec = sdf_minute.parse(endTime).getTime() - sdf_minute.parse(startTime).getTime();
                if (diffMsec > 0) {
                    flag = false;
                } else {
                    flag = true;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return flag;
    }

    public static boolean getRemainTimeYMDHMFlag(String startTime, String endTime) {
        boolean flag = false;
        if (!TextUtils.isEmpty(startTime) && !TextUtils.isEmpty(endTime)) {
            try {
                // 获得两个时间的毫秒时间差异
                long diffMsec = sdf_minute.parse(endTime).getTime() - sdf_minute.parse(startTime).getTime();
                if (diffMsec > 0) {/* 判断结束时间是否大于开始时间 */
                    flag = true;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return flag;
    }

    /*
     *计算time2减去time1的差值 差值只设置 几天 几个小时 或 几分钟
     * 根据差值返回多长之间前或多长时间后
     * */
    public static String getDistanceTime(String startTime, String endTime) {
        long time1 = Long.parseLong(startTime);
        long time2 = Long.parseLong(endTime);
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        long diff;
        String flag;
        if (time1 < time2) {
            diff = time2 - time1;
            flag = "前";
        } else {
            diff = time1 - time2;
            flag = "后";
        }
        day = diff / (24 * 60 * 60 * 1000);
        hour = (diff / (60 * 60 * 1000) - day * 24);
        min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
        sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        if (day != 0) return day + "天" + flag;
        if (hour != 0) return hour + "小时" + flag;
        if (min != 0) return min + "分钟" + flag;
        return "刚刚";
    }

    /**
     * 获取最后一次温度 1分钟内是否有温度
     *
     * @param currentTime
     * @param HistoryTime
     * @return
     */
    public static boolean getLastTemp(long time1, long time2) {
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        long diff;
        diff = time1 - time2;
        day = diff / (24 * 60 * 60 * 1000);
        hour = (diff / (60 * 60 * 1000) - day * 24);
        min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
        sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        if (day != 0) return false;
        if (hour != 0) return false;
        if (min != 0) return false;
        return true;
    }

    /**
     * 获取10分钟内是否有保存数据
     *
     * @return
     */
    public static boolean getLastTenMinute(String currentTime, String historyTime) {
        long time1 = convertDateToMsec(currentTime);
        long time2 = convertDateToMsec(historyTime);
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        long diff;
        diff = time1 - time2;
        day = diff / (24 * 60 * 60 * 1000);
        hour = (diff / (60 * 60 * 1000) - day * 24);
        min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
        sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        if (day != 0) return false;
        if (hour != 0) return false;
        if (min > 9) return false;
        //  if (sec != 0 && sec < 60) return true;
        return true;
    }

    /*************************************************↓补齐历史温度缺失数据↓*****************************************************/
    public static long getRemainTimeFromHistory(String startTime, String endTime) {
        long count = 0;
        long dayMsec = 1000 * 24 * 60 * 60;// 一天的毫秒数
        long hourMsec = 1000 * 60 * 60;// 一小时的毫秒数
        long minuteMsec = 1000 * 60;// 一分钟的毫秒数
        long secondMsec = 1000;// 一秒钟的毫秒数
        long diffMsec; // 毫秒差

        if (startTime != null && !startTime.equals("") && endTime != null
                && !endTime.equals("")) {
            try {
                // 获得两个时间的毫秒时间差异
                diffMsec = sdf_minute.parse(endTime).getTime()
                        - sdf_minute.parse(startTime).getTime();
                if (diffMsec > 0) {
                    /* 判断结束时间是否大于开始时间 */
                    long diffDay = diffMsec / dayMsec;// 计算差多少天
                    long diffHour = diffMsec % dayMsec / hourMsec;// 计算差多少小时
                    long diffMin = diffMsec % dayMsec % hourMsec / minuteMsec;// 计算差多少分钟
                    long diffSec = diffMsec % dayMsec % dayMsec % minuteMsec
                            / secondMsec;// 计算差多少秒//输出结果
                    count = diffHour * 60 + diffMin;
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return count;
    }

    public static String getNextMinute(String currentDate, int next) {
        String nextSecondDate = "";
        if (!TextUtils.isEmpty(currentDate)) {
            try {
                Date date = sdf_minute.parse(currentDate); // 将当前时间格式化
                // 显示输入的日期
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                cal.add(Calendar.MINUTE, next); // 当前时间加1秒
                date = cal.getTime();
                nextSecondDate = sdf_minute.format(date); // 加一秒后的时间
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return nextSecondDate;
    }

    public static Long convertDateToMinute(String dateTime) {
        Date date = new Date();
        try {
            date = sdf_minute.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    /*************************************************↓补齐历史温度缺失数据↓****************************************************/
}
