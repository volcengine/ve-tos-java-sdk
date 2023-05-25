package com.volcengine.tos.internal.util;

import com.volcengine.tos.TosClientException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateConverter {
    public static final TimeZone GMT_TIMEZONE = TimeZone.getTimeZone("GMT");
    private static final String RFC1123 = "EEE, dd MMM yyyy HH:mm:ss z";
    private static final String ISO8601 = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    private static final String RFC3339 = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    private static final String yyyyMMdd = "yyyyMMdd";
    private static final String iso8601Layout = "yyyyMMdd'T'HHmmss'Z'";

    public static String dateToRFC1123String(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat df = new SimpleDateFormat(RFC1123, Locale.US);
        df.setTimeZone(GMT_TIMEZONE);
        return df.format(date);
    }

    public static Date rfc1123StringToDate(String rfc1123Time) {
        if (StringUtils.isEmpty(rfc1123Time)) {
            return null;
        }
        SimpleDateFormat df = new SimpleDateFormat(RFC1123, Locale.US);
        df.setTimeZone(GMT_TIMEZONE);
        try {
            return df.parse(rfc1123Time);
        } catch (ParseException e) {
            throw new TosClientException("tos: parse rfc1123 time failed", e);
        }
    }

    public static String dateToRFC3339String(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat df = new SimpleDateFormat(RFC3339, Locale.US);
        df.setTimeZone(GMT_TIMEZONE);
        return df.format(date);
    }

    public static Date rfc3339StringToDate(String rfc3339Time) {
        if (StringUtils.isEmpty(rfc3339Time)) {
            return null;
        }
        SimpleDateFormat df = new SimpleDateFormat(RFC3339, Locale.US);
        df.setTimeZone(GMT_TIMEZONE);
        try {
            return df.parse(rfc3339Time);
        } catch (ParseException e) {
            throw new TosClientException("tos: parse rfc3339 time failed", e);
        }
    }

    public static Date iso8601StringToDate(String iso8601Time) {
        if (StringUtils.isEmpty(iso8601Time)) {
            return null;
        }
        SimpleDateFormat df = new SimpleDateFormat(ISO8601, Locale.US);
        df.setTimeZone(GMT_TIMEZONE);
        try {
            return df.parse(iso8601Time);
        } catch (ParseException e) {
            throw new TosClientException("tos: parse iso8601 time failed", e);
        }
    }

    public static String dateToISO8601String(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat df = new SimpleDateFormat(ISO8601, Locale.US);
        df.setTimeZone(GMT_TIMEZONE);
        return df.format(date);
    }

    /**
     * for signature
     */
    public static String dateToFullIso8601Str(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat df = new SimpleDateFormat(iso8601Layout, Locale.US);
        df.setTimeZone(GMT_TIMEZONE);
        return df.format(date);
    }

    public static Date fullIso8601StrToDate(String iso8601Str) {
        if (StringUtils.isEmpty(iso8601Str)) {
            return null;
        }
        SimpleDateFormat df = new SimpleDateFormat(iso8601Layout, Locale.US);
        df.setTimeZone(GMT_TIMEZONE);
        try {
            return df.parse(iso8601Str);
        } catch (ParseException e) {
            throw new TosClientException("tos: parse iso8601 time failed", e);
        }
    }

    /**
     * for signature
     */
    public static String dateToShortDateStr(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat df = new SimpleDateFormat(yyyyMMdd, Locale.US);
        df.setTimeZone(GMT_TIMEZONE);
        return df.format(date);
    }

    public static Date addDateTime(Date date, int seconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, seconds);
        return calendar.getTime();
    }
}
