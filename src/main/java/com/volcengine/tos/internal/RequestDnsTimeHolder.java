package com.volcengine.tos.internal;

public class RequestDnsTimeHolder {

    private static final ThreadLocal<Long> startTime = new ThreadLocal<>();
    private static final ThreadLocal<Long> endTime = new ThreadLocal<>();

    public static void setTime(long start, long end) {
        startTime.set(start);
        endTime.set(end);
    }

    public static void clear() {
        startTime.remove();
        endTime.remove();
    }

    public static Long getStart() {
        return startTime.get();
    }

    public static Long getEnd() {
        return endTime.get();
    }
}
