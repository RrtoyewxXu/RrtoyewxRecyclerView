package com.rrtoyewx.recyclerviewlibrary.utils;

/**
 * Created by Rrtoyewx on 16/8/18.
 * DateUtil
 */
public class DateUtil {
    public static final long SECOND = 1000L;
    public static final long MIN = 60 * SECOND;
    public static final long HOUR = 60 * MIN;
    public static final long DAY = 24 * HOUR;
    public static final long MONTH = 30 * DAY;
    public static final long YEAR = 12 * MONTH;

    public static final String SECOND_SUFFIX = "秒";
    public static final String MIN_SUFFIX = "分";
    public static final String HOUR_SUFFIX = "小时";
    public static final String DAY_SUFFIX = "天";
    public static final String MONTH_SUFFIX = "月";
    public static final String YEAR_SUFFIX = "年";

    public static final String convertTime(long time) {
        if (time <= MIN) {
            return String.valueOf(time / 1000) + SECOND_SUFFIX;
        }

        if (time <= HOUR) {
            return String.valueOf(time / 60 / 1000) + MIN_SUFFIX;
        }

        if (time <= DAY) {
            return String.valueOf(time / 60 / 60 / 1000) + HOUR_SUFFIX;
        }

        if (time <= MONTH) {
            return String.valueOf(time / 24 / 60 / 60 / 1000) + DAY_SUFFIX;
        }

        if (time <= YEAR) {
            return String.valueOf(time / 30 / 24 / 60 / 60 / 1000) + MONTH_SUFFIX;
        } else {
            return String.valueOf(time / 12 / 30 / 24 / 60 / 60 / 1000) + YEAR_SUFFIX;
        }
    }
}
