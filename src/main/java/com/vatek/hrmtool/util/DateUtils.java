package com.vatek.hrmtool.util;

import com.vatek.hrmtool.constant.DateConstant;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

@Log4j2
public class DateUtils {

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

    public static Instant getEndDateOfMonth(int month,int year){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH,month - 1);
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.DATE,calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return calendar.toInstant();
    }

    public static Instant getStartDateOfMonth(int month,int year){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH,month - 1);
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.DATE,calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        return calendar.toInstant();
    }

    public static String convertInstantToStringDate(Instant stringDate){
        return convertInstantToStringDate(stringDate,null);
    }

    public static String convertInstantToStringDate(Instant instant, String datePattern){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(datePattern != null ? datePattern : DateConstant.DD_MM_YYYY).withZone(ZoneId.systemDefault());
        return dateTimeFormatter.format(instant);
    }

    public static Instant convertDateToInstant(Date date){
        return date.toInstant();
    }

    public static Instant getInstantNow(){
        return Instant.now();
    }
}
