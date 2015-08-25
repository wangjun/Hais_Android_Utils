package pw.hais.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by LZ on 2014/12/31.
 */
public class TimeUtil {
    /**
     * 将时间戳转为日期
     *
     * @param time
     * @return
     */
    public static String getStrTime(String time) {
        String re_StrTime = null;
        SimpleDateFormat sdf = null;
        if (time == null || "".equals(time)) {
            return "";
        }
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        long loc_time = Long.valueOf(time);
        re_StrTime = sdf.format(new Date(loc_time * 1000L));
        return re_StrTime;
    }


    //转换中文对应的时段
    public static String convertNowHour2CN(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH");
        String hourString = sdf.format(date);
        int hour = Integer.parseInt(hourString);
        if (hour < 6) {
            return "凌晨";
        } else if (hour >= 6 && hour < 12) {
            return "早上";
        } else if (hour == 12) {
            return "中午";
        } else if (hour > 12 && hour <= 18) {
            return "下午";
        } else if (hour >= 19) {
            return "晚上";
        }
        return "";
    }

    //剩余秒数转换
    public static String convertSecond2Day(int time) {
        int day = time / 86400;
        int hour = (time - 86400 * day) / 3600;
        int min = (time - 86400 * day - 3600 * hour) / 60;
        int sec = (time - 86400 * day - 3600 * hour - 60 * min);
        StringBuilder sb = new StringBuilder();
        sb.append(day);
        sb.append("天");
        sb.append(hour);
        sb.append("时");
        sb.append(min);
        sb.append("分");
        sb.append(sec);
        sb.append("秒");
        return sb.toString();
    }

}
