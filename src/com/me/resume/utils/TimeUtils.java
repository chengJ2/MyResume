package com.me.resume.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

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
