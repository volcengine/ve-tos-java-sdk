package com.volcengine.tos.auth;

import com.volcengine.tos.TosClientException;
import com.volcengine.tos.internal.TosRequest;
import com.volcengine.tos.internal.util.ParamsChecker;
import com.volcengine.tos.internal.util.SigningUtils;
import com.volcengine.tos.internal.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.time.*;
import java.util.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.volcengine.tos.internal.util.TosUtils.uriEncode;

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

    private Credentials credentials;
    private final String region;
    private signingHeader signingHeader;
    private Predicate<String> signingQuery;
    private Supplier<Instant> now;
    private signKey signKey;

    public SignV4(Credentials credentials, String region) {
        ParamsChecker.ensureNotNull(credentials, "Credentials");
        ParamsChecker.ensureNotNull(region, "Region");
        this.credentials = credentials;
        this.region = region;
        this.signingHeader = SignV4::defaultSigningHeaderV4;
        this.signingQuery = SignV4::defaultSigningQueryV4;
        this.now = SignV4::defaultUTCNow;
        this.signKey = SigningUtils::signKey;
    }

    public Supplier<Instant> getNow() {
        return now;
    }

    public void setNow(Supplier<Instant> date) {
        this.now = date;
    }

    @Override
    public Credentials getCredential() {
        return this.credentials;
    }

    @Override
    public String getRegion() {
        return this.region;
    }

    @Override
    public Map<String, String> signHeader(TosRequest req) {
        ParamsChecker.ensureNotNull(req.getHost(), "host");
        Map<String, String> signed = new HashMap<>(4);
        OffsetDateTime now = this.now.get().atOffset(ZoneOffset.UTC);
        String date = now.format(SigningUtils.iso8601Layout);
        String contentSha256 = req.getHeaders().get(SigningUtils.v4ContentSHA256);

        Map<String, String> header = req.getHeaders();
        List<Map.Entry<String, String>> signedHeader = this.signedHeader(header, false);
        signedHeader.add(new SimpleEntry<>(SigningUtils.v4Date.toLowerCase(), date));
        signedHeader.add(new SimpleEntry<>("date", date));
        signedHeader.add(new SimpleEntry<>("host", req.getHost()));

        Credential cred = this.credentials.credential();
        if (StringUtils.isNotEmpty(cred.getSecurityToken())) {
            signedHeader.add(new SimpleEntry<>(SigningUtils.v4SecurityToken.toLowerCase(), cred.getSecurityToken()));
            signed.put(SigningUtils.v4SecurityToken, cred.getSecurityToken());
        }
        Collections.sort(signedHeader, new Comparator<Map.Entry<String, String>>() {
            @Override
            public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                return o1.getKey().compareTo(o2.getKey());
            }
        });
        List<Map.Entry<String, String>> signedQuery = this.signedQuery(req.getQuery(), null);
        String sign = this.doSign(req.getMethod(), req.getPath(), contentSha256, signedHeader, signedQuery, now, cred);
        String credential = String.format("%s/%s/%s/tos/request", cred.getAccessKeyId(), now.format(SigningUtils.yyyyMMdd), this.region);
        String keys = signedHeader.stream().map(Map.Entry::getKey).sorted().collect(Collectors.joining(";"));
        String auth = String.format("TOS4-HMAC-SHA256 Credential=%s,SignedHeaders=%s,Signature=%s", credential, keys, sign);

        signed.put(SigningUtils.authorization, auth);
        signed.put(SigningUtils.v4Date, date);
        signed.put("Date", date);
        return signed;
    }

    @Override
    public Map<String, String> signQuery(TosRequest req, Duration ttl) {
        OffsetDateTime now = this.now.get().atOffset(ZoneOffset.UTC);
        String date = now.format(SigningUtils.iso8601Layout);
        Map<String, String> query = req.getQuery();
        Map<String, String> extra = new HashMap<>();

        Credential cred = this.credentials.credential();
        String credential = String.format("%s/%s/%s/tos/request", cred.getAccessKeyId(), now.format(SigningUtils.yyyyMMdd), this.region);
        extra.put(SigningUtils.v4Algorithm, SigningUtils.signPrefix);
        extra.put(SigningUtils.v4Credential, credential);
        extra.put(SigningUtils.v4Date, date);
        extra.put(SigningUtils.v4Expires, String.valueOf(ttl.toMillis() / 1000));
        if (StringUtils.isNotEmpty(cred.getSecurityToken())) {
            extra.put(SigningUtils.v4SecurityToken, cred.getSecurityToken());
        }
//        extra.put(v4SignedHeaders, "host"); // 目前只有host

        List<Map.Entry<String, String>> signedHeader = this.signedHeader(req.getHeaders(), true);

        String host = req.getHost();
        if (StringUtils.isEmpty(host)) {
            throw new TosClientException("empty host", null);
        }
        signedHeader.add(new SimpleEntry<>("host", host));
        Collections.sort(signedHeader, new Comparator<Map.Entry<String, String>>() {
            @Override
            public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                return o1.getKey().compareTo(o2.getKey());
            }
        });

        String keys = signedHeader.stream().map(Map.Entry::getKey).sorted().collect(Collectors.joining(";"));
        extra.put(SigningUtils.v4SignedHeaders, keys);
        List<Map.Entry<String, String>> signedQuery = this.signedQuery(query, extra);
        String sign = this.doSign(req.getMethod(), req.getPath(), SigningUtils.unsignedPayload, signedHeader, signedQuery, now, cred);
        extra.put(SigningUtils.v4Signature, sign);

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
        return ("content-type".equals(key) && !isSigningQuery) || key.startsWith(SigningUtils.v4Prefix);
    }

    /**
     * @param key query key
     * @return boolean, 需要加入签名中 or not
     */
    public static boolean defaultSigningQueryV4(String key) {
        return !SigningUtils.v4SignatureLower.equals(key);
    }

    /**
     * 返回的数据没有排序
     */
    private List<Map.Entry<String, String>> signedHeader(Map<String, String> header, boolean isSignedQuery) {
        ArrayList<Map.Entry<String, String>> signed = new ArrayList<>(10);
        if (header == null || header.isEmpty()) {
            return signed;
        }
        for (Map.Entry<String, String> entry : header.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (StringUtils.isNotEmpty(key)) {
                String kk = key.toLowerCase();
                if (this.signingHeader.isSigningHeader(kk, isSignedQuery)) {
                    value = value == null ? "" : value;
                    signed.add(new SimpleEntry<>(kk, value));
                }
            }
        }
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
        Collections.sort(header, new Comparator<Map.Entry<String, String>>() {
            @Override
            public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                return o1.getKey().compareTo(o2.getKey());
            }
        });
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

        buf.append(StringUtils.join(keys, ";"));
        buf.append(split);

        if (StringUtils.isNotEmpty(contentSha256)) {
            buf.append(contentSha256);
        } else {
            buf.append(SigningUtils.emptySHA256);
        }
        return buf.toString();
    }

    private String doSign(String method, String path, String contentSha256,
                          List<Map.Entry<String, String>> header,
                          List<Map.Entry<String, String>> query,
                          OffsetDateTime now, Credential cred) {
        final char split = '\n';

        String req = this.canonicalRequest(method, path, contentSha256, header, query);

        LOG.debug("canonical request:\n{}", req);

        StringBuilder buf = new StringBuilder(SigningUtils.signPrefix.length() + 128);

        buf.append(SigningUtils.signPrefix);
        buf.append(split);

        buf.append(now.format(SigningUtils.iso8601Layout));
        buf.append(split);

        String date = now.format(SigningUtils.yyyyMMdd);
        buf.append(date).append('/')
                .append(this.region).append("/tos/request");
        buf.append(split);

        byte[] sum = SigningUtils.sha256(req);
        buf.append(SigningUtils.toHex(sum));
        LOG.debug("string to sign:\n {}", buf.toString());
        byte[] signK = SigningUtils.signKey(new SignKeyInfo(date, this.region, cred));
        byte[] sign = SigningUtils.hmacSha256(signK, buf.toString().getBytes(StandardCharsets.UTF_8));
        return String.valueOf(SigningUtils.toHex(sign));
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
        Collections.sort(query, new Comparator<Map.Entry<String, String>>() {
            @Override
            public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                return o1.getKey().compareTo(o2.getKey());
            }
        });
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

}