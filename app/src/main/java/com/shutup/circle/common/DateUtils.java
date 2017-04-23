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

    public static String formatMeanningfulDate(Date date) {
        Date current = new Date();
        Long seconds = (current.getTime()-date.getTime())/1000;
        Long days = seconds / (24*60*60);
        if (days >=1 && days <=3 ) {
            return days + "天前";
        }else if (days > 3){
            return formatDate(date);
        }else {
            Long hours = seconds / 3600 ;
            if (hours >= 1) {
                return hours + "小时前";
            }else {
                Long minutes = seconds / 60;
                if (minutes >= 1) {
                    return minutes + "分钟前";
                }else {
                    return seconds + "秒前";
                }
            }
        }
    }
}
