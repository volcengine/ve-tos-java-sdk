package com.volcengine.tos.auth;

import com.volcengine.tos.TosRequest;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@FunctionalInterface
interface signingHeader{
    boolean isSigningHeader(String key, boolean isSigningQuery);
}

@FunctionalInterface
interface signKey{
    byte[] signingKey(SignKeyInfo info);
}

public class SignV4 implements Signer {
    private static final Logger LOG = LoggerFactory.getLogger(SignV4.class);

    static final String emptySHA256 = "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855";
    static final String unsignedPayload = "UNSIGNED-PAYLOAD";
    static final String signPrefix = "TOS4-HMAC-SHA256";
    static final String authorization = "Authorization";
    static final DateTimeFormatter yyyyMMdd = DateTimeFormatter.ofPattern("yyyyMMdd");
    static final DateTimeFormatter iso8601Layout = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'");

    static final String v4Algorithm = "X-Tos-Algorithm";
    static final String v4Credential = "X-Tos-Credential";
    static final String v4Date = "X-Tos-Date";
    static final String v4Expires = "X-Tos-Expires";
    static final String v4SignedHeaders = "X-Tos-SignedHeaders";
    static final String v4Signature = "X-Tos-Signature";
    static final String v4SignatureLower = "x-tos-signature";
    static final String v4ContentSHA256 = "X-Tos-Content-Sha256";
    static final String v4SecurityToken = "X-Tos-Security-Token";

    static final String v4Prefix = "x-tos";

    private Credentials credentials;
    private final String region;
    private signingHeader signingHeader;
    private Predicate<String> signingQuery;
    private Supplier<Instant> now;
    private signKey signKey;

    public SignV4(Credentials credentials, String region) {
        this.credentials = credentials;
        this.region = region;
        this.signingHeader = SignV4::defaultSigningHeaderV4;
        this.signingQuery = SignV4::defaultSigningQueryV4;
        this.now = SignV4::defaultUTCNow;
        this.signKey = SignV4::signKey;
    }

    public Supplier<Instant> getNow() {
        return now;
    }

    public void setNow(Supplier<Instant> date) {
        this.now = date;
    }

    @Override
    public Map<String, String> signHeader(TosRequest req) {
        Objects.requireNonNull(req.getHost(), "request host is null");
        Map<String, String> signed = new HashMap<>(4);
        OffsetDateTime now = this.now.get().atOffset(ZoneOffset.UTC);
        String date = now.format(iso8601Layout);
        String contentSha256 = req.getHeaders().get(v4ContentSHA256);

        Map<String, String> header = req.getHeaders();
        List<Map.Entry<String, String>> signedHeader = this.signedHeader(header, false);
        signedHeader.add(new SimpleEntry<>(v4Date.toLowerCase(), date));
        signedHeader.add(new SimpleEntry<>("date", date));
        signedHeader.add(new SimpleEntry<>("host", req.getHost()));

        Credential cred = this.credentials.credential();
        if (StringUtils.isNotEmpty(cred.getSecurityToken())) {
            signedHeader.add(new SimpleEntry<>(v4SecurityToken.toLowerCase(), cred.getSecurityToken()));
            signed.put(v4SecurityToken, cred.getSecurityToken());
        }
        signedHeader.sort(Map.Entry.comparingByKey());
        List<Map.Entry<String, String>> signedQuery = this.signedQuery(req.getQuery(), null);
        String sign = this.doSign(req.getMethod(), req.getPath(), contentSha256, signedHeader, signedQuery, now, cred);
        String credential = String.format("%s/%s/%s/tos/request", cred.getAccessKeyId(), now.format(yyyyMMdd), this.region);
        String keys = signedHeader.stream().map(Map.Entry::getKey).sorted().collect(Collectors.joining(";"));
        String auth = String.format("TOS4-HMAC-SHA256 Credential=%s,SignedHeaders=%s,Signature=%s", credential, keys, sign);

        signed.put(authorization, auth);
        signed.put(v4Date, date);
        signed.put("Date", date);
        return signed;
    }

    @Override
    public Map<String, String> signQuery(TosRequest req, Duration ttl) {
        OffsetDateTime now = this.now.get().atOffset(ZoneOffset.UTC);
        String date = now.format(iso8601Layout);
        Map<String, String> query = req.getQuery();
        Map<String, String> extra = new HashMap<>();

        Credential cred = this.credentials.credential();
        String credential = String.format("%s/%s/%s/tos/request", cred.getAccessKeyId(), now.format(yyyyMMdd), this.region);
        extra.put(v4Algorithm, signPrefix);
        extra.put(v4Credential, credential);
        extra.put(v4Date, date);
        extra.put(v4Expires, String.valueOf(ttl.toMillis() / 1000));
        if (StringUtils.isNotEmpty(cred.getSecurityToken())) {
            extra.put(v4SecurityToken, cred.getSecurityToken());
        }
//        extra.put(v4SignedHeaders, "host"); // 目前只有host

        List<Map.Entry<String, String>> signedHeader = this.signedHeader(req.getHeaders(), true);

        String host = req.getHost();
        if (StringUtils.isEmpty(host)) {
            throw new IllegalArgumentException("params.getHost() get null/empty");
        }
        signedHeader.add(new SimpleEntry<>("host", host));
        signedHeader.sort(Map.Entry.comparingByKey());

        String keys = signedHeader.stream().map(Map.Entry::getKey).sorted().collect(Collectors.joining(";"));
        extra.put(v4SignedHeaders, keys);
        List<Map.Entry<String, String>> signedQuery = this.signedQuery(query, extra);
        String sign = this.doSign(req.getMethod(), req.getPath(), unsignedPayload, signedHeader, signedQuery, now, cred);
        extra.put(v4Signature, sign);

        return extra;
    }

    public SignV4 withSignKey(signKey signKey) {
        this.signKey = signKey;
        return this;
    }

    public static Instant defaultUTCNow() {
        return Instant.now();
    }

    /**
     * @param key header key
     * @return boolean, 需要加入签名中 or not
     */
    public static boolean defaultSigningHeaderV4(String key, boolean isSigningQuery) {
        if (StringUtils.isEmpty(key)) {
            return false;
        }
        return ("content-type".equals(key) && !isSigningQuery) || key.startsWith(v4Prefix);
    }

    /**
     * @param key query key
     * @return boolean, 需要加入签名中 or not
     */
    public static boolean defaultSigningQueryV4(String key) {
        return !v4SignatureLower.equals(key);
    }

    /**
     * 返回的数据没有排序
     */
    private List<Map.Entry<String, String>> signedHeader(Map<String, String> header, boolean isSignedQuery) {
        ArrayList<Map.Entry<String, String>> signed = new ArrayList<>(10);
        if (header == null || header.isEmpty()) {
            return signed;
        }
        header.forEach((key, value) -> {
            if (StringUtils.isNotEmpty(key)) {
                String kk = key.toLowerCase();
                if (this.signingHeader.isSigningHeader(kk, isSignedQuery)) {
                    value = value == null ? "" : value;
                    value = String.join(" ", StringUtils.split(value)); // 目前只支持一个header value
                    signed.add(new SimpleEntry<>(kk, value));
                }
            }
        });
        return signed;
    }

    /**
     * 返回的数据没有排序
     */
    private List<Map.Entry<String, String>> signedQuery(Map<String, String> query, Map<String, String> extra) {
        ArrayList<Map.Entry<String, String>> signed = new ArrayList<>(10);
        if (query != null) {
            query.forEach((k, v) -> {
                if (this.signingQuery.test(k.toLowerCase())) {
                    signed.add(new SimpleEntry<>(k, v));
                }
            });
        }

        if (extra != null) {
            extra.forEach((k, v) -> {
                if (this.signingQuery.test(k.toLowerCase())) {
                    signed.add(new SimpleEntry<>(k, v));
                }
            });
        }

        return signed;
    }

    private String canonicalRequest(String method, String path, String contentSha256,
                                    List<Map.Entry<String, String>> header,
                                    List<Map.Entry<String, String>> query) {
        final char split = '\n';
        StringBuilder buf = new StringBuilder(512);

        buf.append(method);
        buf.append(split);

        buf.append(encodePath(path));
        buf.append(split);

        buf.append(encodeQuery(query));
        buf.append(split);

        if (header == null) {
            header = Collections.emptyList();
        }

        ArrayList<String> keys = new ArrayList<>(header.size());
        header.sort(Map.Entry.comparingByKey());
        for (Map.Entry<String, String> entry : header) {
            String key = entry.getKey();
            keys.add(key);

            buf.append(key);
            buf.append(':');
            // 暂时只支持一个value
            buf.append(entry.getValue() == null ? "" : entry.getValue());
            buf.append('\n');
        }
        buf.append(split); // header

        buf.append(String.join(";", keys));
        buf.append(split);

        if (StringUtils.isNotEmpty(contentSha256)) {
            buf.append(contentSha256);
        } else {
            buf.append(emptySHA256);
        }
        return buf.toString();
    }

    static byte[] signKey(SignKeyInfo info) {
        byte[] date = hmacSha256(info.getCredential().getAccessKeySecret().getBytes(StandardCharsets.UTF_8),
                info.getDate().getBytes(StandardCharsets.UTF_8));
        byte[] region = hmacSha256(date, info.getRegion().getBytes(StandardCharsets.UTF_8));
        byte[] service = hmacSha256(region, "tos".getBytes(StandardCharsets.UTF_8));
        return hmacSha256(service, "request".getBytes(StandardCharsets.UTF_8));
    }

    private String doSign(String method, String path, String contentSha256,
                          List<Map.Entry<String, String>> header,
                          List<Map.Entry<String, String>> query,
                          OffsetDateTime now, Credential cred) {
        final char split = '\n';

        String req = this.canonicalRequest(method, path, contentSha256, header, query);

        StringBuilder buf = new StringBuilder(signPrefix.length() + 128);

        buf.append(signPrefix);
        buf.append(split);

        buf.append(now.format(iso8601Layout));
        buf.append(split);

        String date = now.format(yyyyMMdd);
        buf.append(date).append('/')
                .append(this.region).append("/tos/request");
        buf.append(split);

        byte[] sum = sha256(req);
        buf.append(toHex(sum));
        LOG.debug("string to sign:\n {}", buf.toString());
        byte[] signK = signKey(new SignKeyInfo(date, this.region, cred));
        byte[] sign = hmacSha256(signK, buf.toString().getBytes(StandardCharsets.UTF_8));
        return String.valueOf(toHex(sign));
    }

    private static String encodePath(String path) {
        if (path == null || path.isEmpty()) {
            return "/";
        }
        return uriEncode(path, false);
    }

    private static String encodeQuery(List<Map.Entry<String, String>> query) {
        if (query == null || query.isEmpty()) {
            return "";
        }
        StringBuilder buf = new StringBuilder(512);
        query.sort(Map.Entry.comparingByKey());
        for (Map.Entry<String, String> kv : query) {
            String keyEscaped = uriEncode(kv.getKey(), true);
            if (buf.length() > 0){
                buf.append('&');
            }
            buf.append(keyEscaped);
            buf.append('=');
            buf.append(uriEncode(kv.getValue() == null ? "" : kv.getValue(), true));
        }
        return buf.toString();
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

    static byte[] hmacSha256(byte[] key, byte[] value) {
        try {
            Mac hmac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(key, "HmacSHA256");
            hmac.init(secretKey);
            return hmac.doFinal(value);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public static byte[] sha256(String data) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(data.getBytes(StandardCharsets.UTF_8));
            return md.digest();
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    private static final char[] HEX = "0123456789abcdef".toCharArray();

    private static char[] toHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX[v >>> 4];
            hexChars[j * 2 + 1] = HEX[v & 0x0F];
        }
        return hexChars;
    }
}