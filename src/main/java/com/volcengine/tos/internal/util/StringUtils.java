package com.volcengine.tos.internal.util;

import com.volcengine.tos.TosClientException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class StringUtils {
    private static final String ALPHANUMERIC_STR="0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public static String removeStart(String str, String remove) {
        if (isNotEmpty(str) && isNotEmpty(remove)) {
            return str.startsWith(remove) ? str.substring(remove.length()) : str;
        } else {
            return str;
        }
    }

    public static boolean isEmpty(String cs) {
        return cs == null || cs.length() == 0;
    }

    public static boolean isNotEmpty(String cs) {
        return cs != null && cs.length() > 0;
    }

    public static boolean equals(String src, String dst) {
        if (src == null) {
            return dst == null;
        } else {
            return src.equals(dst);
        }
    }

    public static boolean equals(byte[] src, byte[] dst) {
        if (src == null) {
            return dst == null;
        } else {
            return Arrays.equals(src, dst);
        }
    }

    // only for test
    public static String randomString(int length){
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for(int i = 0;i < length; i++){
            int number = random.nextInt(62);
            sb.append(ALPHANUMERIC_STR.charAt(number));
        }
        return sb.toString();
    }

    public static String toString(InputStream stream) throws IOException {
        return readStream(stream).toString("UTF-8");
    }

    public static byte[] toByteArray(InputStream stream) throws IOException {
        if (stream == null) {
            return null;
        }
        return readStream(stream).toByteArray();
    }

    private static ByteArrayOutputStream readStream(InputStream stream) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = stream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        return result;
    }

    public static String join(List<String> strs, String concat) {
        if (isEmpty(concat)) {
            throw new TosClientException("concat is empty", null);
        }
        if (strs == null || strs.size() == 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(strs.get(0));
        for (int i = 1; i < strs.size(); i++) {
            sb.append(concat);
            sb.append(strs.get(i));
        }
        return sb.toString();
    }
}
