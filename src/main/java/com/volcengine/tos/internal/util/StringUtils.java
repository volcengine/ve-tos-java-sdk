package com.volcengine.tos.internal.util;

import com.volcengine.tos.TosClientException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
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

    // 不区分大小写比较是否相等
    public static boolean equalsIgnoreCase(String src, String dst) {
        if (src == null) {
            return dst == null;
        } else {
            return src.equalsIgnoreCase(dst);
        }
    }

    // 不区分大小写比较是否 startsWith
    public static boolean startWithIgnoreCase(String src, String dst) {
        if (src == null) {
            return false;
        } else {
            return dst != null &&
                    src.toLowerCase().startsWith(dst.toLowerCase());
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

    public static String toString(InputStream stream, String content) {
        try {
            return readStream(stream).toString("UTF-8");
        } catch (IOException e) {
            throw new TosClientException("tos: read " + content + " from body failed", e);
        }
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

    public static byte[] getBytesUtf8(final String string) {
        return getBytes(string, StandardCharsets.UTF_8);
    }

    private static byte[] getBytes(final String string, final Charset charset) {
        if (string == null) {
            return null;
        }
        return string.getBytes(charset);
    }

    public static String newStringUtf8(final byte[] bytes) {
        return newString(bytes, StandardCharsets.UTF_8);
    }

    private static String newString(final byte[] bytes, final Charset charset) {
        return bytes == null ? null : new String(bytes, charset);
    }
}
