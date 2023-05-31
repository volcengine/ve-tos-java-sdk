package com.volcengine.tos.internal.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class DateConverter {
    private static final DateTimeFormatter RFC1123 = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH).withZone(ZoneId.of("GMT"));
    private static final DateTimeFormatter ISO8601 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    private static final DateTimeFormatter RFC3339 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    public static String dateToRFC1123String(Date date) {
        if (date == null) {
            return null;
        }
        return RFC1123.format(date.toInstant().atZone(ZoneOffset.UTC));
    }

    public static Date rfc1123StringToDate(String rfc1123Time) {
        return StringUtils.isNotEmpty(rfc1123Time) ? Date.from(Instant.from(RFC1123.parse(rfc1123Time))) : null;
    }

    public static String dateToRFC3339String(Date date) {
        if (date == null) {
            return null;
        }
        return RFC3339.format(date.toInstant().atZone(ZoneOffset.UTC));
    }

    public static Date rfc3339StringToDate(String rfc1123Time) {
        return StringUtils.isNotEmpty(rfc1123Time) ? Date.from(Instant.from(RFC3339.parse(rfc1123Time))) : null;
    }

    public static Date iso8601StringToDate(String iso8601Time) {
        if (StringUtils.isEmpty(iso8601Time)) {
            return null;
        }
        LocalDateTime parse = LocalDateTime.parse(iso8601Time, ISO8601);
        return Date.from(parse.atZone(ZoneOffset.UTC).toInstant());
    }

    public static String dateToISO8601String(Date date) {
        if (date == null) {
            return null;
        }
        return ISO8601.format(date.toInstant().atZone(ZoneOffset.UTC));
    }
}
