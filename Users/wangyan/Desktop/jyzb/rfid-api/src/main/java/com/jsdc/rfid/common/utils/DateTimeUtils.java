package com.jsdc.rfid.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author : lb
 * @date: 2023/3/10 10:07
 * desc :
 **/

public class DateTimeUtils {
    /**
     * 默认日期格式
     */
    public static String DEFAULT_FORMAT = "yyyy-MM-dd";

    public static String DEFAULT_FORMAT_DATA_TIME = "yyyy-MM-dd HH:mm:ss";

    /**
     * 通过时间秒毫秒数判断两个时间的天数间隔 @cz
     *
     * @param date1 开始时间
     * @param date2 结束时间
     * @return 天数
     */
    public static int differentDaysByMillisecond(Date date1, Date date2) {
        return (int) ((date2.getTime() - date1.getTime()) / (1000 * 3600 * 24));
    }

    /**
     * 获取上一年年份
     *
     * @return
     */
    public static Integer getLastYear() {
        SimpleDateFormat formats = new SimpleDateFormat("yyyy");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.YEAR, -1);
        Date date = c.getTime();
        // Date类型转String类型
        String format = formats.format(date);
        // String类型转int类型
        int parseInt = Integer.parseInt(format);
        return parseInt;
    }

    /**
     * 选择不同的模板吗，转换字符串 @cz
     *
     * @param date 日期对象
     * @return Date 日期字符串
     */
    public static Date parseDateChoseSimple(String date, String simple) {
        SimpleDateFormat f = new SimpleDateFormat(simple);
        try {
            return f.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 选择不同的模板吗，转换日期 @cz
     *
     * @param date 日期对象
     * @return String 日期字符串
     */
    public static String formatDateChoseSimple(Date date, String simple) {
        SimpleDateFormat f = new SimpleDateFormat(simple);
        return f.format(date);
    }

    /**
     * 格式化日期
     *
     * @param date 日期对象
     * @return String 日期字符串
     */
    public static String formatDate(Date date) {
        SimpleDateFormat f = new SimpleDateFormat(DEFAULT_FORMAT);
        return f.format(date);
    }

    /**
     * 获取当年的第一天
     *
     * @param
     * @return
     */
    public static Date getCurrYearFirst() {
        Calendar currCal = Calendar.getInstance();
        int currentYear = currCal.get(Calendar.YEAR);
        return getYearFirst(currentYear);
    }

    /**
     * 获取当年的最后一天
     *
     * @param
     * @return
     */
    public static Date getCurrYearLast() {
        Calendar currCal = Calendar.getInstance();
        int currentYear = currCal.get(Calendar.YEAR);
        return getYearLast(currentYear);
    }

    /**
     * 获取某年第一天日期
     *
     * @param year 年份
     * @return Date
     */
    public static Date getYearFirst(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        Date currYearFirst = calendar.getTime();
        String nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(currYearFirst);
        return currYearFirst;
    }

    /**
     * 获取某年第一天日期
     *
     * @param year 年份
     * @return String
     */
    public static String getYearFirstStr(int year) {
        Date currYearFirst = getYearFirst(year);
        String nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(currYearFirst);
        return nowTime;
    }

    /**
     * 获取某年最后一天日期
     *
     * @param year 年份
     * @return Date
     */
    public static Date getYearLast(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.roll(Calendar.DAY_OF_YEAR, -1);
        return calendar.getTime();
    }

    public static String getCurrYearLast(int year) {
        Date date = getYearLast(year);
        String nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
        return nowTime;
    }

    /**
     * @Description: 当前时间上几年
     * @param: [time, fmt]
     * @return: java.lang.String
     * @author: csx
     * @date: 2023/3/10
     * @time: 14:41
     */
    public static String getLastTime(String time, String fmt, int y, int m, int d) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(fmt);
        Calendar ca = Calendar.getInstance();//得到一个Calendar的实例
        ca.setTime(sdf.parse(time));   //设置时间为当前时间
        ca.add(Calendar.YEAR, y); //年份+1
        ca.add(Calendar.MONTH, m); // 月份-1
        ca.add(Calendar.DATE, d); // 日期-1
        return sdf.format(ca.getTime());
    }


    public static String getMonthStartTimeStr() {
        SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_FORMAT_DATA_TIME);
        return sdf.format(getMonthStartTime());
    }
    public static Date getMonthStartTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static String getMonthLastTimeStr() {
        SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_FORMAT_DATA_TIME);
        return sdf.format(getMonthLastTime());
    }

    public static Date getMonthLastTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

        /**
         * 获取当天的开始时间和结束时间
         */
//    public static void main(String[] args) throws ParseException {
//        System.out.println(getMonthStartTimeStr());
//        System.out.println(getMonthLastTimeStr());
//
//    }

}