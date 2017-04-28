package com.shutup.circle.common;

import android.content.res.Resources;

import com.shutup.circle.R;

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

    public static String formatMeaningfulDate(Resources resources, Date date) {
        Date current = new Date();
        Long seconds = (current.getTime()-date.getTime())/1000;
        Long days = seconds / (24*60*60);
        if (days >=1 && days <=3 ) {
            return resources.getString(R.string.daysAgo,days);
        }else if (days > 3){
            return formatDate(date);
        }else {
            Long hours = seconds / 3600 ;
            if (hours >= 1) {
                return resources.getString(R.string.hoursAgo,hours);
            }else {
                Long minutes = seconds / 60;
                if (minutes >= 1) {
                    return resources.getString(R.string.minutesAgo,minutes);
                }else {
                    if (seconds > 0){
                        return resources.getString(R.string.secondsAgo,seconds);
                    }else {
                        return formatDate(date);
                    }
                }
            }
        }
    }
}
