package com.rrtoyewx.recyclerviewlibrary.utils;

import android.content.Context;

/**
 * Created by Rrtoyewx on 16/8/17.
 * 屏幕工具类
 */
public class ScreenUtil {


    public static int dpToPx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    public static int pxToDp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
