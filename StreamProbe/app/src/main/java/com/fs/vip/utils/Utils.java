package com.fs.vip.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {
    public static String getDateToString(String milSecond) {
        String pattern = "MM dd,yyyy";
        SimpleDateFormat sdr = new SimpleDateFormat(pattern, Locale.ENGLISH);
        int i = Integer.parseInt(milSecond);
        String times = sdr.format(new Date(i * 1000L));
        return times;
    }

}
