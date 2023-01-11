package com.volcengine.tos.internal.util;

import com.volcengine.tos.TosClientException;
import com.volcengine.tos.auth.SignKeyInfo;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.format.DateTimeFormatter;

public class SigningUtils {
    public static final String emptySHA256 = "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855";
    public static final String unsignedPayload = "UNSIGNED-PAYLOAD";
    public static final String signPrefix = "TOS4-HMAC-SHA256";
    public static final String authorization = "Authorization";
    public static final DateTimeFormatter yyyyMMdd = DateTimeFormatter.ofPattern("yyyyMMdd");
    public static final DateTimeFormatter iso8601Layout = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'");
    public static final DateTimeFormatter serverTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
    public static final String v4Algorithm = "X-Tos-Algorithm";
    public static final String v4Credential = "X-Tos-Credential";
    public static final String v4Date = "X-Tos-Date";
    public static final String v4Expires = "X-Tos-Expires";
    public static final String v4SignedHeaders = "X-Tos-SignedHeaders";
    public static final String v4Signature = "X-Tos-Signature";
    public static final String v4SignatureLower = "x-tos-signature";
    public static final String v4ContentSHA256 = "X-Tos-Content-Sha256";
    public static final String v4SecurityToken = "X-Tos-Security-Token";
    public static final String v4Prefix = "x-tos";
    public static final String signConditionBucket = "bucket";
    public static final String signConditionKey = "key";
    public static final String signConditionRange = "content-length-range";
    public static final char[] HEX = "0123456789abcdef".toCharArray();

    public static byte[] signKey(SignKeyInfo info) {
        byte[] date = hmacSha256(info.getCredential().getAccessKeySecret().getBytes(StandardCharsets.UTF_8),
                info.getDate().getBytes(StandardCharsets.UTF_8));
        byte[] region = hmacSha256(date, info.getRegion().getBytes(StandardCharsets.UTF_8));
        byte[] service = hmacSha256(region, "tos".getBytes(StandardCharsets.UTF_8));
        return hmacSha256(service, "request".getBytes(StandardCharsets.UTF_8));
    }

    public static byte[] hmacSha256(byte[] key, byte[] value) {
        try {
            Mac hmac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(key, "HmacSHA256");
            hmac.init(secretKey);
            return hmac.doFinal(value);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new TosClientException("tos: compute hmac-sha256 failed", e);
        }
    }

    public static byte[] sha256(String data) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(data.getBytes(StandardCharsets.UTF_8));
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new TosClientException("tos: compute sha256 failed.", e);
        }
    }

    public static char[] toHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX[v >>> 4];
            hexChars[j * 2 + 1] = HEX[v & 0x0F];
        }
        return hexChars;
    }
}
