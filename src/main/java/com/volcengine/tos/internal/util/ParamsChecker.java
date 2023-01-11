package com.volcengine.tos.internal.util;

import com.volcengine.tos.TosClientException;
import com.volcengine.tos.comm.HttpMethod;
import com.volcengine.tos.internal.Consts;
import okhttp3.HttpUrl;

import java.io.UnsupportedEncodingException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ParamsChecker {
    private static final String BUCKET_INVALID_PREFIX_SUFFIX = "-";
    private static final String BUCKET_NAME_PATTERN = "^[a-z0-9][a-z0-9-]{1,61}[a-z0-9]$";
    private static final List<String> INVALID_PREFIX_LIST = Arrays.asList("/", "\\");
    private static final List<String> INVALID_OBJECT_KEY_LIST = Arrays.asList(".", "..", "%2e", "%2e.", ".%2e", "%2e%2e");
    private static final String IP_V6_PATTERN = "^([\\da-fA-F]{1,4}:){7}[\\da-fA-F]{1,4}|:((:[\\da−fA−F]1,4)1,6|:)|:((:[\\da−fA−F]1,4)1,6|:)|" +
            "^[\\da-fA-F]{1,4}:((:[\\da-fA-F]{1,4}){1,5}|:)|([\\da−fA−F]1,4:)2((:[\\da−fA−F]1,4)1,4|:)|([\\da−fA−F]1,4:)2((:[\\da−fA−F]1,4)1,4|:)|" +
            "^([\\da-fA-F]{1,4}:){3}((:[\\da-fA-F]{1,4}){1,3}|:)|([\\da−fA−F]1,4:)4((:[\\da−fA−F]1,4)1,2|:)|([\\da−fA−F]1,4:)4((:[\\da−fA−F]1,4)1,2|:)|" +
            "^([\\da-fA-F]{1,4}:){5}:([\\da-fA-F]{1,4})?|([\\da−fA−F]1,4:)6:|([\\da−fA−F]1,4:)6:";

    public static List<String> parseFromEndpoint(String endpoint) {
        ensureNotNull(endpoint, "endpoint");
        List<String> ret = new ArrayList<>(2);
        String scheme = Consts.SCHEME_HTTPS;
        String host = null;
        if (endpoint.startsWith(Consts.SCHEME_HTTPS + "://")) {
            host = endpoint.substring((Consts.SCHEME_HTTPS + "://").length());
        } else if (endpoint.startsWith(Consts.SCHEME_HTTP)) {
            scheme = Consts.SCHEME_HTTP;
            host = endpoint.substring((Consts.SCHEME_HTTP + "://").length());
        } else {
            host = endpoint;
        }
        ret.add(scheme);
        ret.add(host);
        return ret;
    }

    public static boolean isLocalhostOrIpAddress(String host) {
        if (StringUtils.isEmpty(host)) {
            return false;
        }
        try{
            HttpUrl url = HttpUrl.parse(Consts.SCHEME_HTTPS + "://" + host);
            if (url == null || StringUtils.isEmpty(url.host())) {
                return false;
            }
            InetAddress addr = InetAddress.getByName(url.host());
            boolean validAddress = StringUtils.equals(addr.getHostAddress(), url.host()) ||
                    StringUtils.equals(url.host(), "localhost");
            if (validAddress) {
                return true;
            }
            if (addr instanceof Inet6Address) {
                // 如果是被压缩的 ipv6 地址，validAddress 会为 false，但实际上可能是同一个地址
                // 这里简单用正则匹配，如果两者都是 ipv6 地址，认为他们相同。
                return addr.getHostAddress().matches(IP_V6_PATTERN) && url.host().matches(IP_V6_PATTERN);
            }
            return false;
        } catch (UnknownHostException e) {
            return false;
        }
    }

    public static int parsePort(String host) {
        if (StringUtils.isEmpty(host)) {
            return 0;
        }
        // parse port
        int port = Consts.DEFAULT_HTTPS_PORT;
        int idx = host.indexOf(":");
        if (idx == -1) {
            return port;
        }
        String portStr;
        if (host.contains(".")) {
            // ipv4 127.0.0.1:8080
            portStr = host.substring(idx + 1);
        } else if (host.contains("]:")) {
            // ipv6 [FE80:0:0:0:0123:0456:0789:0abc]:8080
            portStr = host.split("]:")[1];
        } else {
            // host:port
            portStr = host.substring(idx + 1);
        }
        try{
            port = Integer.parseInt(portStr);
        } catch (NumberFormatException e) {
            // set port by default
        }
        return port;
    }

    public static void isValidBucketName(String name) {
        if (StringUtils.isEmpty(name) || name.length() < Consts.MIN_BUCKET_NAME_LENGTH
                || name.length() > Consts.MAX_BUCKET_NAME_LENGTH) {
            throw new TosClientException("invalid bucket name, the length must be [3, 63]", null);
        }
        if (name.startsWith(BUCKET_INVALID_PREFIX_SUFFIX) || name.endsWith(BUCKET_INVALID_PREFIX_SUFFIX)) {
            throw new TosClientException("invalid bucket name, the bucket name can be neither " +
                    "starting with '-' nor ending with '-'", null);
        }
        if (!name.matches(BUCKET_NAME_PATTERN)) {
            throw new TosClientException("invalid bucket name, the character set is illegal", null);
        }
    }

    public static void isValidKey(String key) {
        if (StringUtils.isEmpty(key) || key.length() > Consts.MAX_OBJECT_KEY_LENGTH) {
            throw new TosClientException("invalid object name, the length must be [1,696]", null);
        }

        for (String prefix : INVALID_PREFIX_LIST) {
            if (key.startsWith(prefix)) {
                throw new TosClientException("invalid object name, the object name can not start with / or \\", null);
            }
        }

        isDotOrDotDot(key);

        char[] ck = key.toCharArray();
        for (char c : ck) {
            if (c < 32 || c == 127){
                throw new TosClientException("object key should not contain invisible unicode characters", null);
            }
        }

        isUTF8String(key);
    }

    private static void isDotOrDotDot(String objectKey) {
        // okhttp 不支持 . 或 .. 提前过滤
        for (String key : INVALID_OBJECT_KEY_LIST) {
            if (key.equalsIgnoreCase(objectKey)) {
                throw new TosClientException("object key should not be . or ..", null);
            }
        }
    }

    private static void isUTF8String(String objectKey) {
        try {
            objectKey.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new TosClientException("object key should be utf-8 encode", e);
        }
    }

    public static void isValidBucketNameAndKey(String bucket, String key) {
        isValidBucketName(bucket);
        isValidKey(key);
    }

    public static void ensureNotNull(Object object, String paramName) {
        if (object == null) {
            throw new TosClientException("empty " + paramName, null);
        }
    }

    public static void isValidPartNumber(int partNum) {
        if (partNum < Consts.MIN_PART_NUM || partNum > Consts.MAX_PART_NUM) {
            throw new TosClientException("part number should be between [1, 10000]", null);
        }
    }

    public static void isValidHttpMethod(String method) {
        ensureNotNull(method, "HttpMethod");
        if (StringUtils.equals(method, HttpMethod.GET) || StringUtils.equals(method, HttpMethod.PUT)
                || StringUtils.equals(method, HttpMethod.POST) || StringUtils.equals(method, HttpMethod.HEAD)
                || StringUtils.equals(method, HttpMethod.DELETE)) {
            return;
        }
        throw new TosClientException("method input is invalid", null);
    }
}
