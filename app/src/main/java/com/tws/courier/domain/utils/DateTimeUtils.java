package com.tws.courier.domain.utils;

import android.text.TextUtils;
import android.text.format.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * The type Date time utils.
 */
public class DateTimeUtils {

    public static final String DATE_FORMAT_USER = "dd MMMM, yyyy";
    public static final String DATE_FORMAT_USER_2 = "EEE dd MMMM, yyyy";
    public static final String DATE_FORMAT_USER_3 = "dd MMM, yyyy";
    public static final String DATE_FORMAT_SERVER = "dd-MM-yyyy";
    public static final String DATE_FORMAT_SERVER_2 = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_SERVER_3 = "HH:mm dd-MM-yyyy";
    public static final String TIME_FORMAT_USER = "hh:mm a";
    public static final String TIME_FORMAT_USER_2 = "KK:mm a";
    public static final String TIME_STAMP = "dd MMM yyyy HH:mm a";
    public static final String DATE_TIME_FORMAT_USER = "dd MMM yyyy hh:mm a";
    public static final String DATE_TIME_FORMAT_USER2 = "hh:mm a dd MMM yyyy ";
    public static final String DATE_TIME_FORMAT_USER3 = "dd MMM yyyy, KK:mm a";

    public static String getTime12HourString(int hourOfDay, int minute) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        return getSimpleDateFormat(TIME_FORMAT_USER).format(c.getTime());
    }

    public static Date getTimeFromStringForUser(String timeString) {
        if (TextUtils.isEmpty(timeString)) return null;
        return getDateFromString(timeString, TIME_FORMAT_USER);
    }

    public static Date getDateFromStringForUser(String dateString) {
        if (TextUtils.isEmpty(dateString)) return null;
        return getDateFromString(dateString, DATE_FORMAT_USER);
    }

    public static Date getDateFromStringForSever(String dateString) {
        if (TextUtils.isEmpty(dateString)) return null;
        return getDateFromString(dateString, DATE_FORMAT_SERVER);
    }

    public static String getDateStringFromDateForSever(Date date) {
        if (date == null) return null;
        return getDateStringFromDate(date, DATE_FORMAT_SERVER);
    }

    public static String convertDateStringToSeverDateString(String dateString, String currentDateFormat) {
        if (TextUtils.isEmpty(dateString)) return null;
        return getDateStringFromDate(getDateFromString(dateString, currentDateFormat), DATE_FORMAT_SERVER);
    }

    public static String convertDateStringToAnotherDateString(String dateString, String currentDateFormat,
                                                              String anotherDateFormat) {
        if (TextUtils.isEmpty(dateString)) return null;
        return getDateStringFromDate(getDateFromString(dateString, currentDateFormat), anotherDateFormat);
    }

    public static Date getDateFromString(String dateString, String pattern) {
        Date date = null;
        try {
            date = getSimpleDateFormat(pattern).parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static SimpleDateFormat getSimpleDateFormat(String pattern) {
        if (TextUtils.isEmpty(pattern)) throw new RuntimeException("pattern is null or empty");
        return new SimpleDateFormat(pattern, Locale.getDefault());
    }

    public static boolean checkDateIsToday(String dateString, String dateFormat) {
        if (TextUtils.isEmpty(dateString)) return false;
        Date date = DateTimeUtils.getDateFromString(dateString, dateFormat);
        return date != null && DateUtils.isToday(date.getTime());
    }

    public static boolean checkDateIsToday(Date date) {
        return date != null && DateUtils.isToday(date.getTime());
    }

    public static String getDateStringFromDate(Date date, String pattern) {
        return getSimpleDateFormat(pattern).format(date);
    }

    public static String getZodiacSign(int month, int day) {
        if ((month == 12 && day >= 22 && day <= 31) || (month == 1 && day >= 1 && day <= 19))
            return "Capricorn";
        else if ((month == 1 && day >= 20 && day <= 31) || (month == 2 && day >= 1 && day <= 17))
            return "Aquarius";
        else if ((month == 2 && day >= 18 && day <= 29) || (month == 3 && day >= 1 && day <= 19))
            return "Pisces";
        else if ((month == 3 && day >= 20 && day <= 31) || (month == 4 && day >= 1 && day <= 19))
            return "Aries";
        else if ((month == 4 && day >= 20 && day <= 30) || (month == 5 && day >= 1 && day <= 20))
            return "Taurus";
        else if ((month == 5 && day >= 21 && day <= 31) || (month == 6 && day >= 1 && day <= 20))
            return "Gemini";
        else if ((month == 6 && day >= 21 && day <= 30) || (month == 7 && day >= 1 && day <= 22))
            return "Cancer";
        else if ((month == 7 && day >= 23 && day <= 31) || (month == 8 && day >= 1 && day <= 22))
            return "Leo";
        else if ((month == 8 && day >= 23 && day <= 31) || (month == 9 && day >= 1 && day <= 22))
            return "Virgo";
        else if ((month == 9 && day >= 23 && day <= 30) || (month == 10 && day >= 1 && day <= 22))
            return "Libra";
        else if ((month == 10 && day >= 23 && day <= 31) || (month == 11 && day >= 1 && day <= 21))
            return "Scorpio";
        else if ((month == 11 && day >= 22 && day <= 30) || (month == 12 && day >= 1 && day <= 21))
            return "Sagittarius";
        else return "Illegal date";
    }
}