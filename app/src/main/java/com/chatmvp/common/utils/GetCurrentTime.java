package com.chatmvp.common.utils;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;

/**
 * Created by Administrator on 2016/10/20.
 */
public class GetCurrentTime {

    @SuppressLint("SimpleDateFormat")
    public static String returnTime() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        String date = sDateFormat.format(new java.util.Date());
        return date;
    }
}
