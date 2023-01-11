package com.volcengine.tos;

import com.volcengine.tos.comm.TosHeader;
import com.volcengine.tos.internal.RequestOptionsBuilder;
import com.volcengine.tos.internal.model.HttpRange;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Deprecated
public class RequestOptions {

    /**
     * 设置 http header : Content-Length
     * @param length 长度
     * @return
     */
    public static RequestOptionsBuilder withContentLength(long length) {
        return builder -> builder.withContentLength(length);
    }

    /**
     * 设置 http header : Content-Type
     * @param contentType
     * @return
     */
    public static RequestOptionsBuilder withContentType(String contentType) {
        return builder -> builder.withHeader(TosHeader.HEADER_CONTENT_TYPE, contentType);
    }

    /**
     * SDK 自动根据后缀识别content-type。通过 withAutoRecognizeContentType(false) 关闭
      */

    public static RequestOptionsBuilder withAutoRecognizeContentType(boolean autoRecognized) {
        return builder -> builder.setAutoRecognizeContentType(autoRecognized);
    }

    public static RequestOptionsBuilder withCacheControl(String cacheControl) {
        return builder -> builder.withHeader(TosHeader.HEADER_CACHE_CONTROL, cacheControl);
    }

    public static RequestOptionsBuilder withContentDisposition(String contentDisposition) {
        return builder -> builder.withHeader(TosHeader.HEADER_CONTENT_DISPOSITION, contentDisposition);
    }

    public static RequestOptionsBuilder withContentEncoding(String contentEncoding) {
        return builder -> builder.withHeader(TosHeader.HEADER_CONTENT_ENCODING, contentEncoding);
    }

    public static RequestOptionsBuilder withContentLanguage(String contentLanguage) {
        return builder -> builder.withHeader(TosHeader.HEADER_CONTENT_LANGUAGE, contentLanguage);
    }

    public static RequestOptionsBuilder withContentMD5(String contentMD5) {
        return builder -> builder.withHeader(TosHeader.HEADER_CONTENT_MD5, contentMD5);
    }

    public static RequestOptionsBuilder withContentSHA256(String contentSHA256) {
        return builder -> builder.withHeader(TosHeader.HEADER_CONTENT_SHA256, contentSHA256);
    }

    public static RequestOptionsBuilder withExpires(Duration expires) {
        return builder -> builder.withHeader(TosHeader.HEADER_EXPIRES, expires.toString());
    }

    /**
     * 设置自定义的服务端加密算法
     * @param ssecAlgorithm 服务端加密算法
     * @param ssecKey 加密密钥
     * @param ssecKeyMD5 加密密钥MD5
     * @return
     */
    public static RequestOptionsBuilder withServerSideEncryptionCustomer(String ssecAlgorithm, String ssecKey, String ssecKeyMD5) {
        return builder -> {
            builder.withHeader(TosHeader.HEADER_SSE_CUSTOMER_ALGORITHM, ssecAlgorithm);
            builder.withHeader(TosHeader.HEADER_SSE_CUSTOMER_KEY, ssecKey);
            builder.withHeader(TosHeader.HEADER_SSE_CUSTOMER_KEY_MD5, ssecKeyMD5);
        };
    }

    /**
     * 设置服务端加密算法
     * @param serverSideEncryption 服务端加密算法
     * @return
     */

    public static RequestOptionsBuilder withServerSideEncryption(String serverSideEncryption) {
        return builder -> builder.withHeader(TosHeader.HEADER_SSE, serverSideEncryption);
    }

    public static RequestOptionsBuilder withIfModifiedSince(ZonedDateTime since) {
        return builder -> builder.withHeader(TosHeader.HEADER_IF_MODIFIED_SINCE, DateTimeFormatter.RFC_1123_DATE_TIME.withZone(ZoneId.of("GMT")).format(since));
    }

    public static RequestOptionsBuilder withIfUnmodifiedSince(ZonedDateTime since) {
        return builder -> builder.withHeader(TosHeader.HEADER_IF_UNMODIFIED_SINCE, DateTimeFormatter.RFC_1123_DATE_TIME.withZone(ZoneId.of("GMT")).format(since));
    }

    public static RequestOptionsBuilder withIfMatch(String ifMatch) {
        return builder -> builder.withHeader(TosHeader.HEADER_IF_MATCH, ifMatch);
    }

    public static RequestOptionsBuilder withIfNoneMatch(String ifNoneMatch) {
        return builder -> builder.withHeader(TosHeader.HEADER_IF_NONE_MATCH, ifNoneMatch);
    }

    public static RequestOptionsBuilder withCopySourceIfMatch(String ifMatch) {
        return builder -> builder.withHeader(TosHeader.HEADER_COPY_SOURCE_IF_MATCH, ifMatch);
    }

    public static RequestOptionsBuilder withCopySourceIfNoneMatch(String ifNoneMatch) {
        return builder -> builder.withHeader(TosHeader.HEADER_COPY_SOURCE_IF_NONE_MATCH, ifNoneMatch);
    }

    public static RequestOptionsBuilder withCopySourceIfModifiedSince(String ifModifiedSince) {
        return builder -> builder.withHeader(TosHeader.HEADER_COPY_SOURCE_IF_MODIFIED_SINCE, ifModifiedSince);
    }

    public static RequestOptionsBuilder withCopySourceIfUnmodifiedSince(String ifUnmodifiedSince) {
        return builder -> builder.withHeader(TosHeader.HEADER_COPY_SOURCE_IF_UNMODIFIED_SINCE, ifUnmodifiedSince);
    }

    public static RequestOptionsBuilder withMeta(String key, String value) {
        return builder -> builder.withHeader(TosHeader.HEADER_META_PREFIX+key, value);
    }

    public static RequestOptionsBuilder withRange(long start, long end){
        return builder -> {
            builder.withHeader(TosHeader.HEADER_RANGE, new HttpRange().setStart(start).setEnd(end).toString());
        };
    }

    /**
     * 设置对象版本号
     * @param versionID 版本号
     * @return
     */
    public static RequestOptionsBuilder withVersionID(String versionID) {
        return builder -> builder.withQuery("versionId", versionID);
    }

    /**
     * 设置对象存储类型
     * @param storageClass 存储类型 'STANDARD|IA'
     * @return
     */
    public static RequestOptionsBuilder withStorageClass(String storageClass) {
        return builder -> builder.withHeader(TosHeader.HEADER_STORAGE_CLASS, storageClass);
    }

    public static RequestOptionsBuilder withMetadataDirective(String directive) {
        return builder -> builder.withHeader(TosHeader.HEADER_METADATA_DIRECTIVE, directive);
    }

    public static RequestOptionsBuilder withACL(String acl) {
        return builder -> builder.withHeader(TosHeader.HEADER_ACL, acl);
    }

    public static RequestOptionsBuilder withACLGrantFullControl(String grantFullControl) {
        return builder -> builder.withHeader(TosHeader.HEADER_GRANT_FULL_CONTROL, grantFullControl);
    }

    public static RequestOptionsBuilder withACLGrantRead(String grantRead) {
        return builder -> builder.withHeader(TosHeader.HEADER_GRANT_READ, grantRead);
    }

    public static RequestOptionsBuilder withACLGrantReadAcp(String grantReadAcp) {
        return builder -> builder.withHeader(TosHeader.HEADER_GRANT_READ_ACP, grantReadAcp);
    }

    public static RequestOptionsBuilder withACLGrantWrite(String grantWrite) {
        return builder -> builder.withHeader(TosHeader.HEADER_GRANT_WRITE, grantWrite);
    }

    public static RequestOptionsBuilder withACLGrantWriteAcp(String grantWriteAcp) {
        return builder -> builder.withHeader(TosHeader.HEADER_GRANT_WRITE_ACP, grantWriteAcp);
    }

    public static RequestOptionsBuilder withWebsiteRedirectLocation(String redirectLocation) {
        return builder -> builder.withHeader(TosHeader.HEADER_WEBSITE_REDIRECT_LOCATION, redirectLocation);
    }

    public static RequestOptionsBuilder withPreHashCrc64ecma(String preHashCrc64ecma) {
        return builder -> builder.setPreHashCrc64ecma(preHashCrc64ecma);
    }

    public static RequestOptionsBuilder withHeader(String key, String value) {
        return builder -> builder.withHeader(key, value);
    }

    public static RequestOptionsBuilder withQuery(String key, String value) {
        return builder -> builder.withQuery(key, value);
    }
}