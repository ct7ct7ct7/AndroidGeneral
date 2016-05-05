package com.anson.andorid_general_library;

import android.text.format.Time;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Anson on 2015/3/25.
 */
public class CommonTime {

    /**
     * 將時間字串轉為long <br>
     * @param strDate 格式 : MM-dd-yyyy HH:mm
     * @return long
     */
    public static long strToDateTime(String strDate) {
        if(strDate!=null){
            ParsePosition pos = new ParsePosition(0);
            Date data = new SimpleDateFormat("MM-dd-yyyy HH:mm").parse(strDate, pos);
            return data.getTime();
        }else{
            return 0;
        }

    }


    /**
     * 取得時間<br>
     * 字串format格式如下 : <br>
     * 取得年 yyyy<br>
     * 月 MM<br>
     * 日 dd <br>
     * 時 HH<br>
     * 分 mm<br>
     * 秒 ss<br>
     * <br>
     * 注意:有區分大小寫
     */
    public static String getDateFormat(Date date, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        String strFormatter = formatter.format(date);
        return strFormatter;
    }
    /**
     * 取得顯示給使用者看的時間字串
     * @param timeMillis 時間毫秒
     * @return 格式化後的時間
     */
    public static String getShowDateTime(long timeMillis) {
        DateFormat dateTimeFormat = SimpleDateFormat.getDateTimeInstance();
        return dateTimeFormat.format(new Date(timeMillis));
    }

    /**
     * 取 年月日
     * @param timeMillis 時間毫秒
     * @return yyyyMMdd
     */
    public static String getDate(long timeMillis) {
        // 格式化時間
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(timeMillis);
        Date date = c.getTime();
        String year = getDateFormat(date, "yyyy");
        String month = getDateFormat(date, "MM");
        String day = getDateFormat(date, "dd");
        return year+month+day;
    }

    /** 取得目前時間 */
    public static String getNowTime() {
        Time t = new Time();
        t.setToNow();
        int year = t.year;
        int month = t.month + 1;
        int day = t.monthDay;
        int hour = t.hour;
        int minutes = t.minute;
        return month + "-" + day + "-" + year + "  " + hour + ":" + minutes;
    }
    /** 取得年 */
    public static int getYear() {
        Time t = new Time();
        t.setToNow();
        return t.year;
    }
    /** 取得月 */
    public static int getMonth() {
        Time t = new Time();
        t.setToNow();
        return t.month;
    }
    /** 取得日 */
    public static int getDay() {
        Time t = new Time();
        t.setToNow();
        return t.monthDay;
    }
}
