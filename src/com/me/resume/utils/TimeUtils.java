package com.me.resume.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import com.me.resume.model.CustomDate;
import com.me.resume.tools.L;

/**
 * 
 * @ClassName: TimeUtils
 * @Description: 时间通用类
 * @date 2015/11/5 下午2:17:23
 */
public class TimeUtils {
    /**
     * DEFAULT_FORMAT_STRING
     */
    public static final String DEFAULT_FORMAT_STRING = "yyyy-MM-dd HH:mm:ss";
    
    /**
     * ThreadLocal<SimpleDateFormat> dateFormater
     */
    private final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DEFAULT_FORMAT_STRING);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            return simpleDateFormat;
        }
    };
    
    /**
     * Month Minute
     */
    public static final String YEAR_MONTH_DAY_FORMAT_STRING = "yyyy-MM-dd";
    public static final String YEAR_MONTH_FORMAT_STRING = "yyyy/MM";

    /**
     * Month Minute
     */
    public static final String MONTH_AND_Minute_FORMAT_STRING = "MM-dd HH:mm";
    /**
     * ThreadLocal<SimpleDateFormat> dateFormater
     */
    private final static ThreadLocal<SimpleDateFormat> dateFormaterMonthAndMinute = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(MONTH_AND_Minute_FORMAT_STRING);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            return simpleDateFormat;
        }
    };

    /**
     * 
     * @Title:TimeUtils
     * @Description: 比较两个时间大小
     * @author Comsys-WH1510032
     * @return 返回类型  
     * @param DATE1
     * @param DATE2
     * @return
     */
    public static long compareDate(String DATE1, String DATE2) {
        DateFormat df = new SimpleDateFormat(SHORT_FORMAT_STRING);
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            
            long between=(dt2.getTime()-dt1.getTime())/1000;//除以1000是为了转换成秒
            long day=between/(24*3600);
//            long hour1=between%(24*3600)/3600;
//            long minute1=between/600/60;
//            long second1=between/60;
            return day;
        }catch(Exception e){
        	L.d(e.getMessage());
        	return 0;
        }
    }
    
    /**
     * SHORT_FORMAT_STRING
     */
    public static final String SHORT_FORMAT_STRING = "yyyy-MM-dd";
    /**
     * ThreadLocal<SimpleDateFormat> dateFormaterShort
     */
    private final static ThreadLocal<SimpleDateFormat> dateFormaterShort = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(SHORT_FORMAT_STRING);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            return simpleDateFormat;
        }
    };

    /**
     * Year
     */
    public static final String YEAR_FORMAT_STRING = "yyyy";
    public static final String DAY_FORMAT_STRING = "dd";
    /**
     * ThreadLocal<SimpleDateFormat> dateFormaterShort
     */
    private final static ThreadLocal<SimpleDateFormat> dateFormaterYear = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(YEAR_FORMAT_STRING);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            return simpleDateFormat;
        }
    };


    /**
     * Month And Day
     */
    public static final String MONTH_AND_DAY_FORMAT_STRING = "MM-dd";
    /**
     * ThreadLocal<SimpleDateFormat> dateFormaterShort
     */
    private final static ThreadLocal<SimpleDateFormat> dateFormaterMonthAndDay = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(MONTH_AND_DAY_FORMAT_STRING);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            return simpleDateFormat;
        }
    };

    /**
     * Hour And Minute
     */
    private static final String HOUR_AND_MINUTE_FORMAT_STRING = "HH:mm";
    /**
     * ThreadLocal<SimpleDateFormat> dateFormaterShort
     */
    private static final ThreadLocal<SimpleDateFormat> dateFormaterHourAndMinute = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(HOUR_AND_MINUTE_FORMAT_STRING);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            return simpleDateFormat;
        }
    };

    /**
     * Group Create Time
     */
    private static final String GROUP_CREATE_TIME_FORMAT_STRING = "yyyyMMdd HH:mm:ss";
    /**
     * ThreadLocal<SimpleDateFormat> dateFormaterGroupCreateTime
     */
    private static final ThreadLocal<SimpleDateFormat> dateFormaterGroupCreateTime = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(GROUP_CREATE_TIME_FORMAT_STRING);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            return simpleDateFormat;
        }
    };

    /**
     * UTC_FORMAT
     */
    public static final String UTC_FORMAT_STRING = "yyyyMMdd'T'HH:mm:ss";
    /**
     * TAG
     */
    private static final String TAG = "SITimeUtils";

    /**
     * long time to string
     *
     * @param timeInMillis
     * @param dateFormat
     * @return
     */
    public static String getTime(long timeInMillis, SimpleDateFormat dateFormat) {
        return dateFormat.format(new Date(timeInMillis + TimeZone.getDefault().getRawOffset()));
    }
    
    public static String getCurTime(long timeInMillis, SimpleDateFormat dateFormat) {
        return dateFormat.format(new Date(timeInMillis));
    }

    /**
     * get current time in milliseconds
     *
     * @return
     */
    public static String getCurrentTimeInString(SimpleDateFormat dateFormat) {
        return getCurTime(getCurrentTimeInLong(), dateFormat);
    }
    
    /**
    * get current time in milliseconds
    *
    * @return
    */
   public static String getCurrentTimeString() {
	   SimpleDateFormat simpleDateFormat = new SimpleDateFormat(SHORT_FORMAT_STRING);
       simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
       return getCurTime(getCurrentTimeInLong(), simpleDateFormat);
   }

    /**
     * 群组创建时间格式
     *
     * @param timeInMillis 消息时间
     * @return 创建日期格式时间
     */
    public static String getGroupCreateTime(long timeInMillis) {
        return getTime(timeInMillis, dateFormaterGroupCreateTime);
    }

    /**
     * long time to string, format is {@link #DEFAULT_FORMAT_STRING}
     *
     * @param timeInMillis
     * @return
     */
    public static String getTime(long timeInMillis) {
        return getTime(timeInMillis, dateFormater);
    }

    /**
     * long time to string, format is {@link #DEFAULT_FORMAT_STRING} or isToday format is {@link #HOUR_AND_MINUTE_FORMAT_STRING}
     *
     * @param timeInMillis long
     * @return String date
     */
    public static String getTimeForMsg(long timeInMillis) {
        String date = getTime(timeInMillis, dateFormater);
        if (isToday(date)) {
            return getHourAndMinute(timeInMillis);
        } else if (isThisYear(date)) {
            return getMonthAndMinute(timeInMillis);
        }
        return date;
    }

    /**
     * long time to string, format is {@link #DEFAULT_FORMAT_STRING}
     *
     * @param timeInMillis
     * @return
     */
    public static String getMonthAndDayTime(long timeInMillis) {
        return getTime(timeInMillis, dateFormaterMonthAndDay);
    }

    /**
     * long time to string, format is {@link #HOUR_AND_MINUTE_FORMAT_STRING}
     *
     * @param timeInMillis
     * @return
     */
    public static String getHourAndMinute(long timeInMillis) {
        return getTime(timeInMillis, dateFormaterHourAndMinute);
    }

    /**
     * long time to string, format is {@link #MONTH_AND_Minute_FORMAT_STRING}
     *
     * @param timeInMillis
     * @return
     */
    public static String getMonthAndMinute(long timeInMillis) {
        return getTime(timeInMillis, dateFormaterMonthAndMinute);
    }

    /**
     * long time to string
     *
     * @param timeInMillis
     * @param dateFormat
     * @return
     */
    public static String getTime(long timeInMillis, ThreadLocal<SimpleDateFormat> dateFormat) {
        return dateFormat.get().format(new Date(timeInMillis + TimeZone.getDefault().getRawOffset()));
    }

    /**
     * get current time in milliseconds
     *
     * @return
     */
    public static long getCurrentTimeInLong() {
        return System.currentTimeMillis();
    }

    /**
     * get current time in milliseconds, format is {@link #DEFAULT_FORMAT_STRING}
     *
     * @return
     */
    public static String getCurrentTimeInString() {
        return getTime(getCurrentTimeInLong());
    }

    /**
     * 将群组创建时间改为date
     *
     * @param createTime
     * @return
     */
    public static Date groupCreateTimeToDate(String createTime) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(UTC_FORMAT_STRING);
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = sdf.parse(createTime);
            return date;
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * date to long
     *
     * @param createTime String
     */
    public static long groupCreateTimeToLong(String createTime) {
        if (createTime == null) {
            return 0;
        }
        Date date = groupCreateTimeToDate(createTime);
        if (date == null) {
            return 0;
        }
        return date.getTime();
    }

    /**
     * 将字符串转位日期类型
     *
     * @param sDate
     * @return
     */
    public static Date toDate(String sDate) {
        try {
        	SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULT_FORMAT_STRING);
            return dateFormat.parse(sDate);
        } catch (ParseException e) {
            return null;
        }
    }
    
    /**
     * 将字符串转位日期类型
     *
     * @param sDate
     * @return
     */
    public static String toStrDate(String sDate) {
    	Date d = null ; 
    	SimpleDateFormat dateFormat1 = new SimpleDateFormat(YEAR_MONTH_DAY_FORMAT_STRING);
    	SimpleDateFormat dateFormat2 = new SimpleDateFormat(YEAR_MONTH_FORMAT_STRING);
        try {
        	d = dateFormat1.parse(sDate); 
            return dateFormat2.format(d);
        } catch (ParseException e) {
        	e.printStackTrace() ;    
        }
        return null;
    }
    
    /**
     * 以友好的方式显示时间
     * @param sDate
     * @return
     */
    public static String showTimeFriendly(String sDate) {
        Date time = toDate(sDate);
        if (time == null) {
            return "unknown";
        }
        String ftime = "";
        long lt = time.getTime() / 86400000;
        long ct = getCurrentTimeInLong() / 86400000;
        
        int days = (int) (ct - lt);
        if (days == 0) {
            int hour = (int) ((getCurrentTimeInLong() - time.getTime()) / 3600000);
            if (hour == 0)
                ftime = Math.max((getCurrentTimeInLong() - time.getTime()) / 60000, 1)
                        + "分钟前";
            else
                ftime = hour + "小时前";
        } else if (days == 1) {
            ftime = "昨天";
        } else if (days == 2) {
            ftime = "前天";
        } else if (days > 2 && days <= 10) {
            ftime = days + "天前";
        } else if (days > 10) {
            ftime = sDate;
        }
        return ftime;
    }

    /**
     * 判断给定字符串时间是否为今日
     *
     * @param sDate
     * @return boolean
     */

    public static boolean isToday(String sDate) {
        boolean b = false;
        Date time = toDate(sDate);
        Date today = new Date();
        if (time != null) {
            String nowDate = dateFormaterShort.get().format(today);
            String timeDate = dateFormaterShort.get().format(time);
            if (nowDate.equals(timeDate)) {
                b = true;
            }
        }
        return b;
    }

    /**
     * 判断给定字符串时间是否为今年
     *
     * @param sDate
     * @return boolean
     */

    public static boolean isThisYear(String sDate) {
        boolean b = false;
        Date time = toDate(sDate);
        Date today = new Date();
        if (time != null) {
            String nowDate = dateFormaterYear.get().format(today);
            String timeDate = dateFormaterYear.get().format(time);
            if (nowDate.equals(timeDate)) {
                b = true;
            }
        }
        return b;
    }
    
    /**
     * 
     * @Title:TimeUtils
     * @Description: 获取今年
     * @return year
     */
    public static String theYear() {
        Date today = new Date();
        String year = dateFormaterYear.get().format(today);
        return year;
    }
    
    /**
     * 
     * @Title:TimeUtils
     * @Description: 获取今天
     */
    public static String theToday() {
        Date today = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DAY_FORMAT_STRING);
        String day = simpleDateFormat.format(today);
        return day;
    }
    
    public static int getYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }
 
    public static int getMonth() {
        return Calendar.getInstance().get(Calendar.MONTH) + 1;
    }
 
    public static int getCurrentMonthDay() {
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }
 
    public static int getWeekDay() {
        return Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
    }
    
    public static CustomDate getNextSunday() {
        
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 7 - getWeekDay()+1);
        CustomDate date = new CustomDate(c.get(Calendar.YEAR),
                c.get(Calendar.MONTH)+1, c.get(Calendar.DAY_OF_MONTH));
        return date;
    }
 
    public static int[] getWeekSunday(int year, int month, int day, int pervious) {
        int[] time = new int[3];
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        c.add(Calendar.DAY_OF_MONTH, pervious);
        time[0] = c.get(Calendar.YEAR);
        time[1] = c.get(Calendar.MONTH )+1;
        time[2] = c.get(Calendar.DAY_OF_MONTH);
        return time;
 
    }
 
    public static int getWeekDayFromDate(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getDateFromString(year, month));
        int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (week_index < 0) {
            week_index = 0;
        }
        return week_index;
    }
    
    public static int getMonthDays(int year, int month) {
        if (month > 12) {
            month = 1;
            year += 1;
        } else if (month < 1) {
            month = 12;
            year -= 1;
        }
        int[] arr = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
        int days = 0;
 
        if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
            arr[1] = 29; // 闰年2月29天
        }
 
        try {
            days = arr[month - 1];
        } catch (Exception e) {
            e.getStackTrace();
        }
 
        return days;
    }
    
    public static boolean isCurrentMonth(CustomDate date){
        return(date.year == getYear() &&
                date.month == getMonth());
    }
    
    public static long[] calElement(int y, int m, int d){
        long[] nongDate = new long[7];
        int i = 0, temp = 0, leap = 0;
        //Date baseDate = new Date(0, 0, 31);
        Date baseDate = new GregorianCalendar(0+1900,0,31).getTime();
        //Date objDate = new Date(y - 1900, m - 1, d);
        Date objDate = new GregorianCalendar(y,m-1,d).getTime();
        long offset = (objDate.getTime() - baseDate.getTime()) / 86400000L;
        nongDate[5] = offset + 40;
        nongDate[4] = 14;

        for (i = 1900; i < 2050 && offset > 0; i++) {
            temp = lYearDays(i);
            offset -= temp;
            nongDate[4] += 12;
        }
        if (offset < 0) {
            offset += temp;
            i--;
            nongDate[4] -= 12;
        }
        nongDate[0] = i;
        nongDate[3] = i - 1864;
        leap = leapMonth(i); // 闰哪个月
        nongDate[6] = 0;

        for (i = 1; i < 13 && offset > 0; i++) {
            // 闰月
            if (leap > 0 && i == (leap + 1) && nongDate[6] == 0) {
                --i;
                nongDate[6] = 1;
                temp = leapDays((int) nongDate[0]);
            } else {
                temp = monthDays((int) nongDate[0], i);
            }

            // 解除闰月
            if (nongDate[6] == 1 && i == (leap + 1))
                nongDate[6] = 0;
            offset -= temp;
            if (nongDate[6] == 0)
                nongDate[4]++;
        }

        if (offset == 0 && leap > 0 && i == leap + 1) {
            if (nongDate[6] == 1) {
                nongDate[6] = 0;
            } else {
                nongDate[6] = 1;
                --i;
                --nongDate[4];
            }
        }
        if (offset < 0) {
            offset += temp;
            --i;
            --nongDate[4];
        }
        nongDate[1] = i;
        nongDate[2] = offset + 1;
        return nongDate;
    }
    

    /**
     * 传回农历 y年的总天数
     * @param y
     * @return
     */
    final private static int lYearDays(int y){
        int i, sum = 348;
        for (i = 0x8000; i > 0x8; i >>= 1) {
            if ((lunarInfo[y - 1900] & i) != 0)
                sum += 1;
        }
        return (sum + leapDays(y));
    }

    /**
     * 传回农历 y年闰月的天数
     * @param y
     * @return
     */
    final private static int leapDays(int y){
        if (leapMonth(y) != 0) {
            if ((lunarInfo[y - 1900] & 0x10000) != 0)
                return 30;
            else
                return 29;
        } else
            return 0;
    }

    /**
     * 传回农历 y年闰哪个月 1-12 , 没闰传回 0
     * @param y
     * @return
     */
    final private static int leapMonth(int y){
        return (int) (lunarInfo[y - 1900] & 0xf);
    }

    /**
     * 传回农历 y年m月的总天数
     * @param y
     * @param m
     * @return
     */
    final private static int monthDays(int y, int m){
        if ((lunarInfo[y - 1900] & (0x10000 >> m)) == 0)
            return 29;
        else
            return 30;
    }
    
    public static String getChinaDayString(int day) {  
        String chineseTen[] = { "初", "十", "廿", "卅" };  
        int n = day % 10 == 0 ? 9 : day % 10 - 1;  
        if (day > 30)  
            return "";  
        if (day == 10)  
            return "初十";  
        else  
            return chineseTen[day / 10] + chineseNumber[n];  
    }
         
    final static String chineseNumber[] = { "一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二" };
    
    final private static long[] lunarInfo = new long[] { 0x04bd8, 0x04ae0, 0x0a570,
        0x054d5, 0x0d260, 0x0d950, 0x16554, 0x056a0, 0x09ad0, 0x055d2,
        0x04ae0, 0x0a5b6, 0x0a4d0, 0x0d250, 0x1d255, 0x0b540, 0x0d6a0,
        0x0ada2, 0x095b0, 0x14977, 0x04970, 0x0a4b0, 0x0b4b5, 0x06a50,
        0x06d40, 0x1ab54, 0x02b60, 0x09570, 0x052f2, 0x04970, 0x06566,
        0x0d4a0, 0x0ea50, 0x06e95, 0x05ad0, 0x02b60, 0x186e3, 0x092e0,
        0x1c8d7, 0x0c950, 0x0d4a0, 0x1d8a6, 0x0b550, 0x056a0, 0x1a5b4,
        0x025d0, 0x092d0, 0x0d2b2, 0x0a950, 0x0b557, 0x06ca0, 0x0b550,
        0x15355, 0x04da0, 0x0a5d0, 0x14573, 0x052d0, 0x0a9a8, 0x0e950,
        0x06aa0, 0x0aea6, 0x0ab50, 0x04b60, 0x0aae4, 0x0a570, 0x05260,
        0x0f263, 0x0d950, 0x05b57, 0x056a0, 0x096d0, 0x04dd5, 0x04ad0,
        0x0a4d0, 0x0d4d4, 0x0d250, 0x0d558, 0x0b540, 0x0b5a0, 0x195a6,
        0x095b0, 0x049b0, 0x0a974, 0x0a4b0, 0x0b27a, 0x06a50, 0x06d40,
        0x0af46, 0x0ab60, 0x09570, 0x04af5, 0x04970, 0x064b0, 0x074a3,
        0x0ea50, 0x06b58, 0x055c0, 0x0ab60, 0x096d5, 0x092e0, 0x0c960,
        0x0d954, 0x0d4a0, 0x0da50, 0x07552, 0x056a0, 0x0abb7, 0x025d0,
        0x092d0, 0x0cab5, 0x0a950, 0x0b4a0, 0x0baa4, 0x0ad50, 0x055d9,
        0x04ba0, 0x0a5b0, 0x15176, 0x052b0, 0x0a930, 0x07954, 0x06aa0,
        0x0ad50, 0x05b52, 0x04b60, 0x0a6e6, 0x0a4e0, 0x0d260, 0x0ea65,
        0x0d530, 0x05aa0, 0x076a3, 0x096d0, 0x04bd7, 0x04ad0, 0x0a4d0,
        0x1d0b6, 0x0d250, 0x0d520, 0x0dd45, 0x0b5a0, 0x056d0, 0x055b2,
        0x049b0, 0x0a577, 0x0a4b0, 0x0aa50, 0x1b255, 0x06d20, 0x0ada0 };
    
    
    public static Date getDateFromString(int year, int month) {
        String dateString = year + "-" + (month > 9 ? month : (0 + month))
                + -01;
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            date = sdf.parse(dateString);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }
        return date;
    }
    
    public static String getWeekOfDate(String pTime) {
    	SimpleDateFormat format = new SimpleDateFormat(SHORT_FORMAT_STRING);
    	String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
    	Calendar cal = Calendar.getInstance();
    	try {
			cal.setTime(format.parse(pTime));
			int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
	    	if (w < 0)
	    		w = 0;
	    	 return weekDays[w];
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "星期日";
    }
 }
