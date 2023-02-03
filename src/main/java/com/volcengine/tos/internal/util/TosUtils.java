package com.volcengine.tos.internal.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.volcengine.tos.TosClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static com.volcengine.tos.internal.Consts.*;

public class TosUtils {
    private static final String USER_AGENT = String.format("%s/%s (%s/%s;%s)", SDK_NAME, SDK_VERSION,
            System.getProperty("os.name"), System.getProperty("os.arch"), System.getProperty("java.version", "0"));
    private static final ObjectMapper JSON = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .enable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE);

    private static final long MAX_PRE_SIGNED_TTL = 604800;
    private static final long DEFAULT_PRE_SIGNED_TTL = 3600;

    private static Map<String, List<String>> SUPPORTED_REGION = null;

    private static final int baseDelay = 1;
    private static final int maxDelay = 10;
    private static final double factor = 1.6;
    private static final double jitter = 0.2;
    private static final ThreadLocalRandom random = ThreadLocalRandom.current();

    private static final Logger logger = LoggerFactory.getLogger(SDK_LOG_NAMESPACE);

    public static Logger getLogger() {
        return logger;
    }

    public static String getUserAgent() {
        return USER_AGENT;
    }

    public static ObjectMapper getJsonMapper() {
        return JSON;
    }

    public static Map<String, List<String>> getSupportedRegion() {
        return getInstance();
    }

    private static Map<String, List<String>> getInstance() throws TosClientException {
        if (SUPPORTED_REGION != null){
            return SUPPORTED_REGION;
        }
        synchronized (TosUtils.class) {
            SUPPORTED_REGION = new HashMap<>(3);
            SUPPORTED_REGION.put("cn-beijing", Arrays.asList("tos-cn-beijing.volces.com"));
            SUPPORTED_REGION.put("cn-guangzhou", Arrays.asList("tos-cn-guangzhou.volces.com"));
            SUPPORTED_REGION.put("cn-shanghai", Arrays.asList("tos-cn-shanghai.volces.com"));
            return SUPPORTED_REGION;
        }
    }

    public static String tryEncodeValue(String key, String value) {
        if (value == null || value.length() == 0) {
            return value;
        }
        String encodedValue = value;
        if (containChinese(value)) {
            try {
                encodedValue = URLEncoder.encode(value, "UTF-8")
                        .replace("+", "%20")
                        .replace("*", "%2A")
                        .replace("~", "%7E")
                        .replace("/", "%2F");
            } catch (UnsupportedEncodingException e) {
                throw new TosClientException("tos: unsupported http header value in key: "+key, e);
            }
        }
        return encodedValue;
    }

    public static String tryDecodeValue(String key, String value) {
        if (key == null || value == null) {
            return null;
        }
        String decodedValue;
        try {
            decodedValue = URLDecoder.decode(value, "UTF-8");
            if (!containChinese(decodedValue)) {
                // try to decode Chinese, if the value does not contain Chinese,
                // just skip it.
                return value;
            }
            if (value.contains("+") && decodedValue.contains(" ")) {
                decodedValue = decodedValue.replace(" ", "+");
            }
        } catch (UnsupportedEncodingException e) {
            throw new TosClientException("tos: unsupported http header value in key: "+key, e);
        }
        return decodedValue;
    }

    private static boolean containChinese(String value) {
        if (value == null || value.length() == 0) {
            return false;
        }
        for (char c : value.toCharArray()) {
            if (c <= '\u001f' || c >= '\u007f') {
                return true;
            }
        }
        return false;
    }

    private static final boolean[] nonEscape = new boolean[256];
    private static final byte[] escapeChar = "0123456789ABCDEF".getBytes(StandardCharsets.UTF_8);

    static {
        for (byte ch = 'a'; ch <= 'z'; ch++) {
            nonEscape[ch] = true;
        }
        for (byte ch = 'A'; ch <= 'Z'; ch++) {
            nonEscape[ch] = true;
        }
        for (byte ch = '0'; ch <= '9'; ch++) {
            nonEscape[ch] = true;
        }
        nonEscape['-'] = true;
        nonEscape['_'] = true;
        nonEscape['.'] = true;
        nonEscape['~'] = true;
    }

    /**
     * 输入 null 会返回 ""
     *
     * @param in          输入字符串
     * @param encodeSlash 是否编码 /
     * @return 编码之后的string
     */
    public static String uriEncode(String in, boolean encodeSlash) {
        int hexCount = 0;
        byte[] inBytes = in.getBytes(StandardCharsets.UTF_8);
        for (byte b : inBytes) {
            int uintByte = b & 0xFF;
            if (b == '/') {
                if (encodeSlash) {
                    hexCount++;
                }
            } else if (!nonEscape[uintByte]) {
                hexCount++;
            }
        }
        byte[] encoded = new byte[inBytes.length+2*hexCount];
        for (int i = 0, j = 0; i < inBytes.length; i++) {
            int uintByte = inBytes[i] & 0xFF;
            if (uintByte == '/'){
                if (encodeSlash) {
                    encoded[j] = '%';
                    encoded[j+1] = '2';
                    encoded[j+2] = 'F';
                    j += 3;
                } else{
                    encoded[j] = inBytes[i];
                    j++;
                }
            } else if (!nonEscape[uintByte]) {
                encoded[j] = '%';
                encoded[j+1] = escapeChar[uintByte >> 4];
                encoded[j+2] = escapeChar[uintByte & 15];
                j += 3;
            } else {
                encoded[j] = inBytes[i];
                j++;
            }
        }
        return new String(encoded, StandardCharsets.UTF_8);
    }

    public static long backoff(int retries) {
        // return in mill seconds
        if (retries == 0) {
            return baseDelay * 1000;
        }
        double backoff = baseDelay, max = maxDelay;
        while (backoff < max && retries > 0) {
            backoff *= factor;
            retries--;
        }
        if (backoff > max) {
            backoff = max;
        }
        backoff *= 1 + jitter*(random.nextDouble(1) * 2 - 1);
        if (backoff < 0) {
            return 0;
        }
        return (long)(backoff * 1000);
    }

    public static String convertInteger(int value) {
        if (value == 0) {
            return null;
        }
        return String.valueOf(value);
    }

    public static long validateAndGetTtl(long expires) {
        long ttl = expires;
        if (expires == 0) {
            ttl = DEFAULT_PRE_SIGNED_TTL;
        }
        if (expires > MAX_PRE_SIGNED_TTL || expires < 0) {
            throw new TosClientException("tos: invalid preSignedUrl expires, should not be in [1, 604800].", null);
        }
        return ttl;
    }
}
