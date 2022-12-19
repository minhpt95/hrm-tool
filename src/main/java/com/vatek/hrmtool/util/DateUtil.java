package com.vatek.hrmtool.util;

import com.vatek.hrmtool.constant.DateConstant;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

@Log4j2
public class DateUtil {

    private static final Marker marker = MarkerManager.getMarker("DateUtil");
    public static Date convertInstantToDate (Instant instant)
    {
        return Date.from(instant);
    }

    public static Instant convertStringDateToInstant(String stringDate){
        return convertStringDateToInstant(stringDate,null);
    }

    public static Instant convertStringDateToInstant(String stringDate,String datePattern){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePattern != null ? datePattern : DateConstant.DD_MM_YYYY);
        try {
            return convertDateToInstant(simpleDateFormat.parse(stringDate));
        } catch (ParseException e) {
            log.error(marker,"convertStringDateToInstant",e);
            return null;
        }
    }

    public static Instant convertDateToInstant(Date date){
        return date.toInstant();
    }

    public static Instant getInstantNow(){
        return Instant.now();
    }
}
