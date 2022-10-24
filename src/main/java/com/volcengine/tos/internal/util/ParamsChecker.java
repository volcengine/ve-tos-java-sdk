package com.volcengine.tos.internal.util;

import com.volcengine.tos.internal.Consts;
import okhttp3.HttpUrl;

import java.io.UnsupportedEncodingException;
import java.util.*;

public class ParamsChecker {
    private static final String BUCKET_INVALID_PREFIX_SUFFIX = "-";
    private static final String BUCKET_NAME_PATTERN = "^[a-z0-9][a-z0-9-]{1,61}[a-z0-9]$";
    private static final List<String> INVALID_PREFIX_LIST = Arrays.asList("/", "\\");
    private static final List<String> INVALID_OBJECT_KEY_LIST = Arrays.asList(".", "..", "%2e", "%2e.", ".%2e", "%2e%2e");

    public static List<String> parseFromEndpoint(String endpoint) {
        ensureNotNull(endpoint, "endpoint is null");
        List<String> ret = new ArrayList<>(2);
        String scheme = Consts.SCHEME_HTTPS;
        String host = null;
        if (endpoint.startsWith(Consts.SCHEME_HTTPS) || endpoint.startsWith(Consts.SCHEME_HTTP)) {
            HttpUrl url = HttpUrl.parse(endpoint);
            if (url != null) {
                scheme = url.scheme();
                host = url.host();
            }
        } else {
            host = endpoint;
        }
        if (host == null) {
            throw new IllegalArgumentException("invalid endpoint");
        }
        ret.add(scheme);
        ret.add(host);
        return ret;
    }

    public static void isValidBucketName(String name) {
        if (StringUtils.isEmpty(name) || name.length() < Consts.MIN_BUCKET_NAME_LENGTH
                || name.length() > Consts.MAX_BUCKET_NAME_LENGTH) {
            throw new IllegalArgumentException("invalid bucket name, the length must be [3, 63]");
        }
        if (name.startsWith(BUCKET_INVALID_PREFIX_SUFFIX) || name.endsWith(BUCKET_INVALID_PREFIX_SUFFIX)) {
            throw new IllegalArgumentException("invalid bucket name, the bucket name can be neither " +
                    "starting with '-' nor ending with '-'");
        }
        if (!name.matches(BUCKET_NAME_PATTERN)) {
            throw new IllegalArgumentException("invalid bucket name, the character set is illegal");
        }
    }

    public static void isValidKey(String key) {
        if (StringUtils.isEmpty(key) || key.length() > Consts.MAX_OBJECT_KEY_LENGTH) {
            throw new IllegalArgumentException("invalid object name, the length must be [1,696]");
        }

        for (String prefix : INVALID_PREFIX_LIST) {
            if (key.startsWith(prefix)) {
                throw new IllegalArgumentException("invalid object name, the object name can not start with / or \\");
            }
        }

        isDotOrDotDot(key);

        char[] ck = key.toCharArray();
        for (char c : ck) {
            if (c < 32 || c == 127){
                throw new IllegalArgumentException("object key should not contain invisible unicode characters");
            }
        }

        isUTF8String(key);
    }

    private static void isDotOrDotDot(String objectKey) {
        // okhttp 不支持 . 或 .. 提前过滤
        for (String key : INVALID_OBJECT_KEY_LIST) {
            if (key.equalsIgnoreCase(objectKey)) {
                throw new IllegalArgumentException("object key should not be . or ..");
            }
        }
    }

    private static void isUTF8String(String objectKey) {
        try {
            objectKey.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("object key should be utf-8 encode");
        }
    }

    public static void isValidBucketNameAndKey(String bucket, String key) {
        isValidBucketName(bucket);
        isValidKey(key);
    }

    public static void ensureNotNull(Object object, String paramName) {
        Objects.requireNonNull(object, "null " + paramName);
    }

    public static void isValidPartNumber(int partNum) {
        if (partNum < Consts.MIN_PART_NUM || partNum > Consts.MAX_PART_NUM) {
            throw new IllegalArgumentException("part number should be between [1, 10000]");
        }
    }
}
