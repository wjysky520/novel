package com.wjysky.utils;

import com.wjysky.enums.ErrorEnum;
import com.wjysky.exception.ApiException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * @author : 王俊元(wjysky520@gmail.com)
 * @ClassName : DateUtil
 * @Description : 日期工具类
 * @Date : 2023-08-16 18:21:13
 */
public class DateUtil {

    public static final String DATE_FORMAT_101 = "yyyy-MM-dd HH:mm:ss";

    public static final String DATE_FORMAT_201 = "yyyyMMddHHmmss";

    public static final String DATE_FORMAT_301 = "yyyy年MM月dd日HH时mm分ss秒";

    public static final String DATE_FORMAT_102 = "yyyy-MM-dd";

    public static final String DATE_FORMAT_202 = "yyyyMMdd";

    public static final String DATE_FORMAT_302 = "yyyy年MM月dd日";

    public static final String DATE_FORMAT_103 = "yyyy-MM";

    public static final String DATE_FORMAT_203 = "yyyyMM";

    public static final String DATE_FORMAT_303 = "yyyy年MM月";

    public static final String DATE_FORMAT_104 = "HH:mm:ss";

    public static final String DATE_FORMAT_204 = "HHmmss";

    public static final String DATE_FORMAT_304 = "HH时mm分ss秒";

    public static final String DATE_FORMAT_105 = "HH:mm";

    public static final String DATE_FORMAT_205 = "HHmm";

    public static final String DATE_FORMAT_3015 = "HH时mm分";

    /**
     *
     * @ClassName DateUtil
     * @Title date2Str
     * @Description 日期转
     * @param format
     * @author 王俊元(wangjy1@belink.com)
     * @Date 2023/8/17 10:05
     * @Return java.lang.String
     * @throws 
    **/
    public static String date2Str(String format) {
        return DateTimeFormatter.ofPattern(format).format(LocalDateTime.now());
    }

    public static String localDate2Str(LocalDate localDate, String format) {
        return DateTimeFormatter.ofPattern(format).format(localDate);
    }

    public static String localDateTime2Str(LocalDateTime localDateTime, String format) {
        return DateTimeFormatter.ofPattern(format).format(localDateTime);
    }

    public static Long getTimeDiff(LocalDateTime time1, LocalDateTime time2, ChronoUnit unit) {
        Duration duration = Duration.between(time1, time2);
        switch (unit) {
            case MILLIS: // 毫秒
                return duration.toMillis();
            case SECONDS: // 秒
                return BigDecimal.valueOf(duration.toMillis()).divide(BigDecimal.valueOf(1000L), 0, RoundingMode.DOWN).longValue();
            case MINUTES: // 分钟
                return duration.toMinutes();
            case HOURS: // 小时
                return duration.toHours();
            case DAYS: // 毫秒
                return duration.toDays();
            default:
                throw new ApiException(ErrorEnum.NOT_SUPPORT_DATE_UNIT);
        }
    }

    public static void main(String[] args) {
        System.out.println(LocalDate.now().getDayOfMonth());
    }
}