/**
 * 版权所有 违者必究
 * by chenxh
 */
package com.rzico.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class DateUtils {

    /*日志记录器*/
    private static Logger logger = LoggerFactory.getLogger(DateUtils.class);

    /**
     * 数据库存储的时间格式串，如yyyymmdd 或yyyymmddHHMiSS
     */
    public static final int DB_STORE_DATE = 1;
    /**
     * 用连字符-分隔的时间时间格式串，如yyyy-mm-dd 或yyyy-mm-dd HH:Mi:SS
     */
    public static final int HYPHEN_DISPLAY_DATE = 2;
    /**
     * 用连字符.分隔的时间时间格式串，如yyyy.mm.dd 或yyyy.mm.dd HH:Mi:SS
     */
    public static final int DOT_DISPLAY_DATE = 3;
    /**
     * 用中文字符分隔的时间格式串，如yyyy年mm月dd 或yyyy年mm月dd HH:Mi:SS
     */
    public static final int CN_DISPLAY_DATE = 4;

    /**
     * 用中文字符分隔的时间格式串，mm月dd日
     */
    public static final int SIMPLE_DISPLAY_DATE = 0;
    /**
     * **数据库存储时间格式串,如yyyymmddHHMissSS*
     */
    public static final int DB_LONG_DATE = 5;

    /**
     * **编码生成格式串,如yyMMdd*
     */
    public static final int SIMPLE_DB_DATE = -1;
    /**
     * **编码生成格式串,如HH:Mi:SS*
     */
    public static final int SIMPLE_DISPLAY_TIME = 6;

    /**
     * 得到精确到秒的格式化当前时间串
     *
     * @return 当前时间格式化时间串
     */
    public static String getCurrTimeStr(int formatType) {
        return getTimeStr(new Date(), formatType);
    }

    public static String getTimeStrOfStoreFormat(long date) {
        return getTimeStr(new Date(date), DateUtils.DB_STORE_DATE);
    }

    /**
     * 返回数据存在格式的当前时间，格式为或yyyymmddHHMiSS
     *
     * @return
     */
    public static String getCurrTimeStrOfStoreFormat() {
        return getCurrTimeStr(DB_STORE_DATE);
    }

    /**
     * 返回数据存在格式的当前时间，格式为或yyyymmdd
     *
     * @return
     */
    public static String getCurrDateStrOfStoreFormat() {
        return getCurrDateStr(DB_STORE_DATE);
    }

    /**
     * 得到精确到秒的格式化时间串
     *
     * @param date       指定时间
     * @return 指定时间的格式化时间串
     */
    public static String getTimeStr(Date date, int formatType) {
        if (formatType < DB_STORE_DATE || formatType > SIMPLE_DISPLAY_TIME) {
            throw new IllegalArgumentException("时间格式化类型不是合法的值。");
        } else {
            String formatStr = null;
            switch (formatType) {
                case DB_STORE_DATE:
                    formatStr = "yyyyMMddHHmmss";
                    break;
                case HYPHEN_DISPLAY_DATE:
                    formatStr = "yyyy'-'MM'-'dd HH:mm:ss";
                    break;
                case DOT_DISPLAY_DATE:
                    formatStr = "yyyy.MM.dd HH:mm:ss";
                    break;
                case CN_DISPLAY_DATE:
                    formatStr = "yyyy'年'MM'月'dd'日' HH:mm:ss";
                    break;
                case DB_LONG_DATE:
                    formatStr = "yyyyMMddHHmmssSS";
                    break;
                case SIMPLE_DISPLAY_TIME:
                    formatStr = "HH:mm:ss";
                    break;
            }
            SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
            return sdf.format(date);
        }
    }

    /**
     * 得到精确到天的当前格式化日期串
     *
     * @return
     */
    public static String getCurrDateStr(int formatType) {
        return getDateStr(new Date(), formatType);
    }

    /**
     * 得到精确到天的指定时间格式化日期串
     *
     * @param date       指定时间
     * @return 指定时间格式化日期串
     */
    public static String getDateStr(Date date, int formatType) {

        if (formatType < SIMPLE_DB_DATE || formatType > CN_DISPLAY_DATE) {
            throw new IllegalArgumentException("时间格式化类型不是合法的值。");
        } else {
            String formatStr = null;
            switch (formatType) {
                case DB_STORE_DATE:
                    formatStr = "yyyyMMdd";
                    break;
                case HYPHEN_DISPLAY_DATE:
                    formatStr = "yyyy-MM-dd";
                    break;
                case DOT_DISPLAY_DATE:
                    formatStr = "yyyy.MM.dd";
                    break;
                case CN_DISPLAY_DATE:
                    formatStr = "yyyy'年'MM'月'dd'日'";
                    break;
                case SIMPLE_DISPLAY_DATE:
                    formatStr = "MM'月'dd'日'";
                    break;
                case SIMPLE_DB_DATE:
                    formatStr = "yyMMdd";
                    break;
            }
            SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
            return sdf.format(date);
        }
    }

    /**
     * 得到精确到月的当前时间格式化年月串
     *
     * @return 精确到月当前时间格式化年月串
     */
    public static String getYearMonthStr(int formatType) {
        return getYearMonthStr(new Date(), formatType);
    }

    /**
     * 得到精确到月的指定时间格式化年月串
     *
     * @param date       指定的时间
     * @return 精确到月当前时间格式化年月串
     */
    public static String getYearMonthStr(Date date, int formatType) {
        if (formatType < DB_STORE_DATE || formatType > CN_DISPLAY_DATE) {
            throw new IllegalArgumentException("时间格式化类型不是合法的值。");
        } else {
            String formatStr = null;
            switch (formatType) {
                case DB_STORE_DATE:
                    formatStr = "yyyyMM";
                    break;
                case HYPHEN_DISPLAY_DATE:
                    formatStr = "yyyy-MM";
                    break;
                case DOT_DISPLAY_DATE:
                    formatStr = "yyyy.MM";
                    break;
                case CN_DISPLAY_DATE:
                    formatStr = "yyyy'年'MM'月'";
                    break;
            }
            SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
            return sdf.format(date);
        }
    }

    /**
     * 将显示格式的时间字符串转换为数据库存储的类型
     *
     * @param dateStr 最小4位，最大19位。显示的时间格式时间串如:2004-12-12
     * @return 数据库存储的时间字符串
     */
    public static String toStoreStr(String dateStr) {
        if (dateStr == null || dateStr.trim().equals("")) {
            return "";
        }
        StringBuffer strBuf = new StringBuffer();
        for (int i = 0; i < dateStr.length(); i++) {
            if (dateStr.charAt(i) >= '0' && dateStr.charAt(i) <= '9') {
                strBuf.append(dateStr.charAt(i));
            }
        }
        return strBuf.toString();
    }

    /**
     * 将生日存储的时间格式转化为年龄（周岁，小数点后不计）
     *
     * @param birthdayStr 生日字段  "yyyymmdd"
     * @return 年龄
     */
    public static String birthdayToAge(String birthdayStr) {
        if (birthdayStr == null || birthdayStr.length() < 6) {
            return "";
        } else {
            int birthYear = Integer.parseInt(birthdayStr.substring(0, 4));
            int birthMonth = Integer.parseInt(birthdayStr.substring(4, 6));
            Calendar cal = new GregorianCalendar();
            int currYear = cal.get(Calendar.YEAR);
            int currMonth = cal.get(Calendar.MONTH);
            int age = currYear - birthYear;
            age -= (currMonth < birthMonth) ? 1 : 0;
            return "" + age;
        }
    }

    /**
     * 功能描述:
     * 将年龄转换为生日
     *
     * @param age int
     * @return String
     */
    public static String ageToBirthday(int age) {

        String currDateStr = DateUtils.getYearMonthStr(DB_STORE_DATE);
        int currDateInt = Integer.parseInt(currDateStr);
        int ageDateInt = currDateInt - age * 100;
        return String.valueOf(ageDateInt);
    }


    /**
     * 功能描述:
     * 增加一年
     *
     * @return String
     */
    public static String nextAgeDate(String birDate) {

        int currDateInt = Integer.parseInt(birDate);
        int ageDateInt = currDateInt + 100;
        return String.valueOf(ageDateInt);
    }


    /**
     * @param dateTimeStr String 格式化的时间串
     * @param detal       int  增加或减少的时间
     * @param field       int 参见Calendar中关于时间字段属性的定义
     * @return String 返回的
     */
    public static String add(String dateTimeStr, int formatType, int detal,
                             int field) {
        if (dateTimeStr == null || dateTimeStr.length() < 6) {
            return dateTimeStr;
        } else {
            try {
                String formatStr = null;
                switch (formatType) {
                    case DB_STORE_DATE:
                        formatStr = "yyyyMMddHHmmss";
                        break;
                    case HYPHEN_DISPLAY_DATE:
                        formatStr = "yyyy-MM-dd HH:mm:ss";
                        break;
                    case DOT_DISPLAY_DATE:
                        formatStr = "yyyy.MM.dd HH:mm:ss";
                        break;
                    case CN_DISPLAY_DATE:
                        formatStr = "yyyy'年'MM'月' HH：mm：ss";
                        break;
                    default:
                        formatStr = "yyyyMMddHHmmss";
                        break;
                }

                formatStr = formatStr.substring(0, dateTimeStr.length());
                SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
                Date d = sdf.parse(dateTimeStr);
                GregorianCalendar g = new GregorianCalendar();
                g.setTime(d);
                g.add(field, detal);
                d = g.getTime();
                return sdf.format(d);
            } catch (ParseException ex) {
                ex.printStackTrace();
                return dateTimeStr;
            }
        }
    }


    /**
     * 对日期时间对象进行调整
     * @param date
     * @param CALENDARFIELD <pre>
     * 年 Calendar.YEAR
     * 月 Calendar.MONTH
     * 日 Calendar.DATE
     * 时 Calendar.HOUR
     * 分 Calendar.MINUTE
     * 秒 Calendar.SECOND
     * </pre>
     * @param amount 调整数量，>0表向后调整（明天，明年），<0表向前调整（昨天，去年）
     * @return
     * @author:chenxy
     */
    public static Date transpositionDate(Date date, int CALENDARFIELD, int amount) {
        if (null == date) {
            return date;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(CALENDARFIELD, amount);
        return calendar.getTime();
    }


//    /**
//     * @param date Date 时间
//     * @param detal int 增加的时间
//     * @param field int 参见Calendar中关于时间字段属性的定义
//     * @return Date
//     */
//    public static Date add(Date date, int detal, int field)
//    {
//        Calendar g = new GregorianCalendar();
//        g.setTime(date);
//        g.add(field, detal);
//        return g.getTime();
//    }

    /**
     * 日期、时间格式化
     *
     * @param date   Date
     *               将要被格式化的日期对象
     * @param outFmt String
     *               返回样式，参照类说明，如：yyyy年MM月dd日
     * @return String
     *         格式化后的日期、时间字符串，data为null时返回null，outFmt非法时返回yyyyMMdd格式
     */
    public static String getDateFormat(Date date, String outFmt) {
        if (null == date) {
            return null;
        }
        if (null == outFmt || "".equals(outFmt.trim())) { //outFmt非法
            outFmt = "yyyyMMdd";
        }

        String retu = null;
        SimpleDateFormat dateFormat = null;
        try {
            dateFormat = new SimpleDateFormat(outFmt);
        } catch (IllegalArgumentException iaex) { //outFmt非法
            dateFormat = new SimpleDateFormat("yyyyMMdd");
        }
        retu = dateFormat.format(date);

        dateFormat = null;

        return retu;
    }

    /**
     * 把日期时间对象格式化为yyyyMMdd样式
     *
     * @param date Date
     *             将要被格式化的日期对象
     * @return String
     *         格式化后的日期、时间字符串，如：20041001
     */
    public static String getDateFormat(Date date) {
        return getDateFormat(date, "yyyyMMdd");
    }

    /**
     * 把系统当前日期时间格式化为指定的样式
     *
     * @param outFmt String
     *               返回样式，参照类说明，如：yyyy年MM月dd日
     * @return String
     *         格式化后的日期、时间字符串，如：2004年10月01日
     */
    public static String getDateFormat(String outFmt) {
        return getDateFormat(new Date(), outFmt);
    }

    /**
     * 把系统当前日期时间格式化为默认样式yyyyMMdd
     *
     * @return String
     *         格式化后的日期、时间字符串，如：20041001
     */
    public static String getDateFormat() {
        return getDateFormat(new Date(), "yyyyMMdd");
    }

    /**
     * 日期、时间格式化
     *
     * @param millis long
     *               the number of milliseconds（毫秒） since January 1, 1970, 00:00:00 GMT.
     * @param outFmt String
     *               返回样式，参照类说明，如：yyyy年MM月dd日
     * @return String
     *         格式化后的日期、时间字符串
     */
    public static String getDateFormat(long millis, String outFmt) {
        Date d = new Date(millis);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        String retu = getDateFormat(calendar.getTime(), outFmt);
        calendar = null;
        return retu;
    }

    /**
     * 日期、时间格式化
     *
     * @param datestr String
     *                存在一定格式的日期、时间字符串，如：20041001、200410011503
     * @param inFmt   String
     *                对datestr参数格式说明，参照类说明，如：yyyyMMdd、yyyyMMddHHmm
     * @param outFmt  String
     *                返回样式，参照类说明，如：yyyy年MM月dd日
     * @return String
     *         格式化后的日期、时间字符串，如：2004年10月01日、2004年10月01日
     *         <BR>输出样式outFmt非法时，使用yyyyMMdd格式输出
     */
    public static String getDateFormat(String datestr, String inFmt, String outFmt) {
        if (null == datestr || "".equals(datestr.trim())) {
            return datestr;
        }

        if (null == inFmt || "".equals(inFmt.trim())) {
            return datestr;
        }

        if (null == outFmt || "".equals(outFmt.trim())) { //输出样式非法
            outFmt = "yyyyMMdd";
        }
        Date inDate;
        try {
            inDate = getDate(datestr, inFmt);
        } catch (Exception e) {
            return "";
        }
        if (null == inDate) { //根据inFmt分析datestr时抛出异常
            return datestr;
        }

        String retu = getDateFormat(inDate, outFmt);
        inDate = null;
        return retu;
    }

    /**
     * 把日期时间字符串，按inFmt样式转化为日期对象，然后格式化为默认样式yyyyMMdd
     *
     * @param datestr String
     *                存在一定格式的日期、时间字符串，如：20041001、200410011503
     * @param inFmt   String
     *                对datestr参数格式说明，参照类说明，如：yyyyMMdd、yyyyMMddHHmm
     * @return String
     *         格式化后的日期、时间字符串，如：20041001、20041001
     */
    public static String getDateFormat(String datestr, String inFmt) {
        return getDateFormat(datestr, inFmt, "yyyyMMdd");
    }

    /**
     * 根据inFmt的样式，日期时间字符串转化为日期时间对象
     *
     * @param datestr String
     *                日期时间字符串，如：20041001、2004年10月01日 15:03
     * @param format  String
     *                对datestr参数格式说明，参照类说明，如yyyyMMdd、yyyy年MM月dd日 HH:mm
     * @return Date
     *         日期时间对象，格式inFmt非法时，使用yyyyMMdd格式
     */
    public static Date getDate(String datestr, String format) {
        if (StringUtils.isEmpty(datestr)) {
            return null;
        }
        if (StringUtils.isEmpty(format)) { //inFmt非法
            format = "yyyyMMdd";
        }

        try {
//依据inFmt格式把日期字符串转化为日期对象
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            dateFormat.setLenient(true);
            return dateFormat.parse(datestr);
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * 对日期时间对象进行调整，实现如昨天是几号，去年的今天星期几等.
     * <BR>例子：
     * <pre>
     * <blockquote>
     * 计算去年今天星期几
     * Date date = DateUtils.addDate(new Date(),Calendar.YEAR,-1);
     * System.out.println(DateUtils.getDateFormat(date,"E"));
     * 打印60天后是什么日期，并显示为 yyyy-MM-dd 星期
     * Date date = DateUtils.addDate(new Date(),Calendar.DATE,60);
     * System.out.println(DateUtils.getDateFormat(date,"yyyy-MM-dd E"));
     * </blockquote>
     * </pre>
     *
     * @param date          Date
     *                      需要调整的日期时间对象
     * @param CALENDARFIELD int
     *                      对日期时间对象以什么单位进行调整：
     *                      <pre>
     *                                                                                                                                                                                                                                                                                                                                                                                                                <blockquote>
     *                                                                                                                                                                                                                                                                                                                                                                                                                年 Calendar.YEAR
     *                                                                                                                                                                                                                                                                                                                                                                                                                月 Calendar.MONTH
     *                                                                                                                                                                                                                                                                                                                                                                                                                日 Calendar.DATE
     *                                                                                                                                                                                                                                                                                                                                                                                                                时 Calendar.HOUR
     *                                                                                                                                                                                                                                                                                                                                                                                                                分 Calendar.MINUTE
     *                                                                                                                                                                                                                                                                                                                                                                                                                秒 Calendar.SECOND
     *                                                                                                                                                                                                                                                                                                                                                                                                                </blockquote>
     *                                                                                                                                                                                                                                                                                                                                                                                                                </pre>
     * @param amount        int
     *                      调整数量，>0表向后调整（明天，明年），<0表向前调整（昨天，去年）
     * @return Date
     *         调整后的日期时间对象
     */
    public static Date addDate(Date date, int CALENDARFIELD, int amount) {
        if (null == date) {
            return date;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(CALENDARFIELD, amount);
        return calendar.getTime();
    }

    /**
     * 对日期时间对象进行调整.
     *
     * @param datestr       String
     *                      需要调整的日期时间字符串，它的格式为yyyyMMdd
     * @param CALENDARFIELD int
     *                      对日期时间对象以什么单位进行调整
     * @param amount        int
     *                      调整数量
     * @return Date
     *         调整后的日期时间对象
     * @see #addDate(Date, int, int)
     */
    public static Date addDate(String datestr, int CALENDARFIELD, int amount) {
        return addDate(getDate(datestr, "yyyyMMdd"), CALENDARFIELD, amount);
    }

    /**
     * 根据出生日期，计算出在某一个日期的年龄
     *
     * @param birthday Date
     *                 出生日期时间对象
     * @param date2    Date
     *                 计算日期对象
     * @return int
     *         返回date2那一天出生日期为birthday的年龄，如果birthday大于date2则返回-1
     */
    public static int getAge(Date birthday, Date date2) {
        if (null == birthday || null == date2) {
            return -1;
        }

        if (birthday.after(date2)) { //birthday大于date2
            return -1;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");

        int ibdYear = Integer.parseInt(sdf.format(birthday));
        int idate2Year = Integer.parseInt(sdf.format(date2));

        if (ibdYear < 0 || idate2Year < 0) {
            return -1;
        }
        if (ibdYear > idate2Year) {
            return -1;
        }

        return idate2Year - ibdYear + 1;
    }

    /**
     * 根据出生日期，计算出当前的年龄
     *
     * @param birthday Date
     *                 出生日期时间对象
     * @return int
     *         返回出生日期为birthday的年龄，如果birthday大于当前系统日期则返回-1
     */
    public static int getAge(Date birthday) {
        return getAge(birthday, new Date());
    }

    /**
     * 根据出生日期，计算出当前的年龄
     *
     * @param birthdaystr String
     *                    出生日期时间字符串，其格式一定为yyyyMMdd
     * @return int
     *         返回出生日期为birthday的年龄，如果birthday大于当前系统日期则返回-1
     */
    public static int getAge(String birthdaystr) {
        return getAge(getDate(birthdaystr, "yyyyMMdd"));
    }


    /**
     * 获得最近三天的第一天日期
     *
     * @param format
     * @return
     */
    public static String getThreeDayAgoBegin(String format) {
        SimpleDateFormat simpleDateFormat = null;
        if (!"".equals(format.trim()) && null != format.trim()) {
            simpleDateFormat = new SimpleDateFormat(format);
        } else {
            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        }
        return simpleDateFormat.format(addDate(new Date(), Calendar.DATE, -2));
    }

    /**
     * 获得未来三天的第一天日期
     *
     * @param format
     * @return
     */
    public static String getThreeDayAfterBegin(String format) {
        SimpleDateFormat simpleDateFormat = null;
        if (!"".equals(format.trim()) && null != format.trim()) {
            simpleDateFormat = new SimpleDateFormat(format);
        } else {
            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        }
        return simpleDateFormat.format(addDate(new Date(), Calendar.DATE, +3));
    }

    /**
     * 获得当前日期与本周日相差的天数
     *
     * @return
     */
    public static int getMondayPlus() {
        Calendar cd = Calendar.getInstance();
        // 获得今天是一周的第几天，星期日是第一天，星期二是第二天......
        int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK) - 1;         //因为按中国礼拜一作为第一天所以这里减1
        if (dayOfWeek == 1) {
            return 0;
        } else {
            return 1 - dayOfWeek;
        }
    }

    /**
     * 获得本周第一天的日期
     *
     * @param format
     * @return
     */
    public static String getCurrentWeekBegin(String format) {
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.setTime(now);
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        SimpleDateFormat simpleDateFormat = null;
        if (!"".equals(format.trim()) && null != format.trim()) {
            simpleDateFormat = new SimpleDateFormat(format);
        } else {
            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        }
        return simpleDateFormat.format(cal.getTime());
    }


    /**
     * 获取当前日期是周几
     *
     * @param currentDate
     * @return
     */
    public static String getDayOfWeek(Date currentDate) {

        Calendar cal = Calendar.getInstance();

        String[] weekDays = { "7", "1", "2", "3", "4", "5", "6" };

        try {

            cal.setTime(currentDate);

        } catch (Exception e) {

            e.printStackTrace();

        }

        int w = cal.get(Calendar.DAY_OF_WEEK) - 1; // 指示一个星期中的某天。

        if (w < 0)

            w = 0;

        return weekDays[w];
    }


    /**
     * 获得本周星期日的日期
     *
     * @param format
     * @return
     */
    public static String getCurrentWeekEnd(String format) {
        Calendar c = Calendar.getInstance();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        SimpleDateFormat simpleDateFormat = null;
        if (!"".equals(format.trim()) && null != format.trim()) {
            simpleDateFormat = new SimpleDateFormat(format);
        } else {
            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        }
        return simpleDateFormat.format(c.getTime());
    }

    /**
     * 获取当前月份的第一天
     *
     * @param format
     * @return
     */
    public static String getCurrentMonthBegin(String format) {
        SimpleDateFormat simpleDateFormat = null;
        if (!"".equals(format.trim()) && null != format.trim()) {
            simpleDateFormat = new SimpleDateFormat(format);
        } else {
            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        }
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        return simpleDateFormat.format(calendar.getTime());
    }

    /**
     * 获取当前月的最后一天
     *
     * @param format
     * @return
     */
    public static String getCurrentMonthEnd(String format) {
        SimpleDateFormat simpleDateFormat = null;
        if (!"".equals(format.trim()) && null != format.trim()) {
            simpleDateFormat = new SimpleDateFormat(format);
        } else {
            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        }
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return simpleDateFormat.format(calendar.getTime());
    }

    /**
     * 获得指定月份的第一天
     *
     * @param strdate
     * @param inFrm
     * @param outFrm
     * @return
     */
    public static String getAssignedMonthBegin(String strdate, String inFrm, String outFrm) {
        Date date = getDate(strdate, inFrm);
        if ("".equals(outFrm.trim()) || null == outFrm.trim()) {
            outFrm = "yyy-MM-dd";
        }
        return getDateFormat(getDateFormat(date, "yyyy-MM") + "-01", "yyy-MM-dd", outFrm);
    }

    /**
     * 获得指定月份最后一天
     *
     * @param strdate
     * @param inFrm
     * @param outFrm
     * @return
     */
    public static String getAssignedMonthEnd(String strdate, String inFrm, String outFrm) {
        Date date = getDate(strdate, inFrm);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        SimpleDateFormat simpleDateFormat = null;
        if (!"".equals(outFrm.trim()) && null != outFrm.trim()) {
            simpleDateFormat = new SimpleDateFormat(outFrm);
        } else {
            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        }
        return simpleDateFormat.format(calendar.getTime());
    }

    /**
     * 获取一个月前的第一天的日期
     *
     * @return
     */
    public static String getOneMonthBeforeTime() {
        Date oneMonthBefore = addDate(new Date(), Calendar.DAY_OF_YEAR, -30);
        return getDateFormat(oneMonthBefore, "yyyyMMddHHmmss");
    }

    /**
     * 获取未来一个月的第一天的日期
     *
     * @return
     */
    public static String getOneMonthAfterTime() {
        Date oneMonthBefore = addDate(new Date(), Calendar.DAY_OF_YEAR, +30);
        return getDateFormat(oneMonthBefore, "yyyyMMddHHmmss");
    }

    /**
     * 获取七天前的日期
     *
     * @return
     */
    public static String getOneWeekBeforeTime() {
        Date oneMonthBefore = addDate(new Date(), Calendar.DAY_OF_YEAR, -7);
        return getDateFormat(oneMonthBefore, "yyyyMMddHHmmss");
    }

    /**
     * 获取七天后的日期
     *
     * @return
     */
    public static String getOneWeekAfterTime() {
        Date oneMonthBefore = addDate(new Date(), Calendar.DAY_OF_YEAR, +7);
        return getDateFormat(oneMonthBefore, "yyyyMMddHHmmss");
    }

    /**
     * 获取三个月前的第一天的日期
     *
     * @param format
     * @return
     */
    public static String getThreeMonthAgoBegin(String format) {
        Date threeMonthAgo = addDate(new Date(), Calendar.MONTH, -2);

        String threeMonthAgoStr = getDateFormat(threeMonthAgo, "yyyy-MM-dd");
        return getAssignedMonthBegin(threeMonthAgoStr, "yyyy-MM-dd", format);
    }

    /**
     * 获取未来三个月的第一天的日期
     *
     * @param format
     * @return
     */
    public static String getThreeMonthAfterBegin(String format) {
        Date threeMonthAgo = addDate(new Date(), Calendar.MONTH, +3);

        String threeMonthAgoStr = getDateFormat(threeMonthAgo, "yyyy-MM-dd");
        return getAssignedMonthBegin(threeMonthAgoStr, "yyyy-MM-dd", format);
    }

    /**
     * 获取六个月前的第一天的日期
     *
     * @param format
     * @return
     */
    public static String getSixMonthAgoBegin(String format) {
        Date sixMonthAgo = addDate(new Date(), Calendar.MONTH, -5);

        String sixMonthAgoStr = getDateFormat(sixMonthAgo, "yyyy-MM-dd");
        return getAssignedMonthBegin(sixMonthAgoStr, "yyyy-MM-dd", format);
    }

    /**
     * 获取一年前的第一天的日期
     *
     * @param format
     * @return
     */
    public static String getOneYearAgoBegin(String format) {
        Date oneYearAgo = addDate(new Date(), Calendar.YEAR, -1);

        String oneYearAgoStr = getDateFormat(oneYearAgo, "yyyy-MM-dd");
        return oneYearAgoStr;
    }

    /**
     * 获取一年前的第一天的日期
     *
     * @return
     */
    public static String getYesterday() {
        Date yesterday = addDate(new Date(), Calendar.DAY_OF_YEAR, -1);
        String yesterdayStr = getDateFormat(yesterday, "yyyyMMdd");
        return yesterdayStr;
    }

    /**
     * 判断当前日期是否在数据上报日期的范围内
     *
     * @return
     */
    public static boolean isOverTimeOfReport() {
        String currentDateStr = getDateFormat(new Date(), "yyyyMMdd");
        String currentMonthBeginStr = getCurrentMonthBegin("yyyyMMdd");
        Date reportEndDate = addDate(currentMonthBeginStr, Calendar.DAY_OF_MONTH, 15);
        String reportEndDateStr = getDateStr(reportEndDate, DateUtils.DB_STORE_DATE);
        return Integer.parseInt(currentDateStr) >= Integer.parseInt(currentMonthBeginStr) && Integer.parseInt(currentDateStr) <= Integer.parseInt(reportEndDateStr);
    }

    /**
     * 判断当前时期是否在数据上报日期的范围内
     *
     * @param currentDateStr
     * @return
     */
    public static boolean isOverTimeOfReport(String currentDateStr) {
        String currentMonthBeginStr = getCurrentMonthBegin("yyyyMMdd");
        Date reportEndDate = addDate(currentMonthBeginStr, Calendar.DAY_OF_MONTH, 15);
        String reportEndDateStr = getDateStr(reportEndDate, DateUtils.DB_STORE_DATE);
        return Integer.parseInt(currentDateStr) >= Integer.parseInt(currentMonthBeginStr) && Integer.parseInt(currentDateStr) <= Integer.parseInt(reportEndDateStr);
    }

    /**
     * 获取数据库中的时间值，并转换成系统前台展示的事件格式
     *
     * @param inputStr
     * @return
     * @throws ParseException
     */
    public static String getForegroundDateFormat(String inputStr) {
        return DateUtils.getDateFormat(inputStr, "yyyyMMddHHmmss", "yyyy'-'MM'-'dd");
    }

    /**
     * 获取当前年份
     *
     * @return
     */
    public static int getCurrentYear() {
        Date now = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(now);
        return c.get(Calendar.YEAR);
    }

    public static boolean isValidDate(String dateStr) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        simpleDateFormat.setLenient(false);
        try {
            simpleDateFormat.parse(dateStr);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void main(String[] args) throws Throwable {

        String id="350211";

        System.out.println(id.substring(0, 2));
        System.out.println(id.substring(0, 4));


        System.out.println(DateUtils.getThreeDayAfterBegin("yyyyMMddHHmmss"));
        System.out.println(DateUtils.getOneWeekAfterTime());
        System.out.println(DateUtils.getOneMonthAfterTime());
        System.out.println(DateUtils.getThreeMonthAfterBegin("yyyyMMddHHmmss"));
    }

}