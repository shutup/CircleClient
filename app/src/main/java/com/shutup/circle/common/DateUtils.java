package com.shutup.circle.common;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by shutup on 2017/4/12.
 */

public class DateUtils {
    public static String formatDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);
    }
}
