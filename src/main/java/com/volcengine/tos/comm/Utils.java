package com.volcengine.tos.comm;

import com.volcengine.tos.internal.util.CRC64Utils;
import com.volcengine.tos.internal.util.StringUtils;

public class Utils {
    /**
     * Used for validating whether the client crc64 is same as the HashCrc64Ecma returned
     * from the server.
     * The server returns unsigned long crc64 value, but there is no unsigned long number
     * on the Java platform, this method compares two value by parsing the long value to
     * unsigned long formatted String.
     * @param clientCrc64 the client calculated crc64 value in signed long.
     * @param serverHashCrc64Ecma the server returned crc64 value in unsigned long String.
     * @return if they are the same.
     */
    public static boolean isSameHashCrc64Ecma(long clientCrc64, String serverHashCrc64Ecma) {
        return StringUtils.equals(CRC64Utils.longToUnsignedLongString(clientCrc64), serverHashCrc64Ecma);
    }
}
