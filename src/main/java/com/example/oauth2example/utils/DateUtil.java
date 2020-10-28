package com.example.oauth2example.utils;

import java.util.Date;

public class DateUtil {
    public static Date clearMillisecond(Date date) {
        date.setTime((date.getTime() / 1000) * 1000);
        return date;
    }
}
