package com.vatek.hrmtool.util;

import java.time.Instant;
import java.util.Date;

public class DateUtil {
    public static Date convertInstantToDate (Instant instant)
    {
        return Date.from(instant);
    }

    public static Instant convertDateToInstant(Date date){
        return date.toInstant();
    }

    public static Instant getInstantNow(){
        return Instant.now();
    }
}
