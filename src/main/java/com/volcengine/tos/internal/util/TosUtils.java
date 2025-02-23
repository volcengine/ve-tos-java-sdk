package com.volcengine.tos.internal.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.volcengine.tos.TosClientException;
import com.volcengine.tos.internal.Consts;
import okhttp3.ConnectionPool;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import static com.volcengine.tos.internal.Consts.*;

public class TosUtils {
    private static final String USER_AGENT = String.format("%s/%s (%s/%s;%s)",
            SDK_NAME, SDK_VERSION, OS_NAME, OS_ARCH, JAVA_VERSION);
    private static final ObjectMapper JSON = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .enable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE);

    private static final Pattern chinesePattern = Pattern.compile("[\\x{4e00}-\\x{9fa5}]");

    @Deprecated
    private static final long MAX_PRE_SIGNED_TTL = 604800;

    public static final long DEFAULT_PRE_SIGNED_TTL = 3600;

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
            SUPPORTED_REGION.put("ap-southeast-1", Arrays.asList("tos-ap-southeast-1.volces.com"));
            return SUPPORTED_REGION;
        }
    }

    public static String genUuid() {
        return new UUID(System.currentTimeMillis(), System.nanoTime()).toString();
    }

    public static String encodeHeader(String str) {
        return tryEncodeValue("", str);
    }

    public static String encodeChinese(String value) {
        if (value == null || value.length() == 0) {
            return value;
        }
        StringBuilder sb = new StringBuilder();
        String target;
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            target = String.valueOf(value.charAt(i));
            if (chinesePattern.matcher(target).matches()) {
                try {
                    sb.append(URLEncoder.encode(target, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    throw new TosClientException("tos: encode chinese failed", e);
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static String decodeHeader(String str) {
        return tryDecodeValue("", str);
    }

    /**
     * tryEncodeValue 如果 value 包含中文，会对 value 进行编码
     * 对于 URLEncoder 库，编码时 " " 会被编成 "+"，sdk 会强制转成 "%20"
     */
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

    /**
     * tryDecodeValue 对 value 尝试解码，如果解码后的值不包含中文，则返回原值，否则返回解码后的值
     * 对于 URLDecoder 库，解码时 "+" 会被解成 " "，sdk 会强制解成 "+"
     * 对于解码抛异常的场景，直接返回原值
     */
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
        } catch (UnsupportedEncodingException | IllegalArgumentException e) {
            getLogger().debug("tos: unsupported http header value in key: {}", key, e);
            return value;
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
        if (expires < 0) {
            throw new TosClientException("tos: invalid preSignedUrl expires, should be larger than 0.", null);
        }
        return ttl;
    }

    public static Map<String, String> parseMeta(List<Map<String, String>> userMeta, boolean disableEncodingMeta) {
        if (userMeta == null || userMeta.isEmpty()) {
            return Collections.emptyMap();
        }

        Map<String, String> meta = new HashMap<>(userMeta.size());
        String key;
        String value;
        for (Map<String, String> item : userMeta) {
            key = item.get("Key");
            value = item.get("Value");
            if (!disableEncodingMeta) {
                key = TosUtils.decodeHeader(key);
                value = TosUtils.decodeHeader(value);
            }
            meta.put(key, value);
        }
        return meta;
    }

    public static byte[] longToByteArray(long value) {
        byte[] byteArray = new byte[8];

        for (int i = 0; i < byteArray.length; i++) {
            byteArray[i] = (byte) (value >> 8 * (byteArray.length - 1 - i));
        }

        return byteArray;
    }

    public static OkHttpClient.Builder ignoreCertificate(OkHttpClient.Builder builder) throws TosClientException {
        TosUtils.getLogger().info("tos: ignore ssl certificate verification");
        try {
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[]{};
                        }
                    }
            };
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new SecureRandom());
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier((hostname, session) -> true);
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new TosClientException("tos: set ignoreCertificate failed", e);
        }
        return builder;
    }

    public static OkHttpClient defaultOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setMaxRequests(Consts.DEFAULT_MAX_CONNECTIONS);
        dispatcher.setMaxRequestsPerHost(Consts.DEFAULT_MAX_CONNECTIONS);
        ConnectionPool connectionPool = new ConnectionPool(Consts.DEFAULT_MAX_CONNECTIONS,
                Consts.DEFAULT_IDLE_CONNECTION_TIME_MILLS, TimeUnit.MILLISECONDS);
        builder = TosUtils.ignoreCertificate(builder);
        return builder.dispatcher(dispatcher)
                .connectionPool(connectionPool)
                .retryOnConnectionFailure(false)
                .readTimeout(Consts.DEFAULT_READ_TIMEOUT_MILLS, TimeUnit.MILLISECONDS)
                .writeTimeout(Consts.DEFAULT_WRITE_TIMEOUT_MILLS, TimeUnit.MILLISECONDS)
                .connectTimeout(Consts.DEFAULT_CONNECT_TIMEOUT_MILLS, TimeUnit.MILLISECONDS)
                .followRedirects(false)
                .followSslRedirects(false)
                .build();
    }
}
