package com.volcengine.tos.model.object;

import com.volcengine.tos.TosClientException;
import com.volcengine.tos.comm.TosHeader;
import com.volcengine.tos.comm.common.ACLType;
import com.volcengine.tos.comm.common.StorageClassType;
import com.volcengine.tos.internal.Consts;
import com.volcengine.tos.internal.model.HttpRange;
import com.volcengine.tos.internal.util.DateConverter;
import com.volcengine.tos.internal.util.StringUtils;
import com.volcengine.tos.internal.util.TypeConverter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * ObjectMetaRequestOptions set Http Headers for Http Request.
 * All the Chinese-Character header value will be urlEncoded.
 */
public class ObjectMetaRequestOptions {
    /**
     * for http common header: "Cache-Control"
     */
    private String cacheControl;
    /**
     * for http common header: "Content-Disposition"
     */
    private String contentDisposition;
    /**
     * for http common header: "Content-Encoding"
     */
    private String contentEncoding;
    /**
     * for http common header: "Content-Language"
     */
    private String contentLanguage;
    /**
     * for http common header: "Content-Type"
     */
    private String contentType;
    /**
     * For http common header: "Expires", format in RFC1123 GMT
     */
    private Date expires;
    /**
     * for http common header: "If-Match"
     */
    private String ifMatch;
    /**
     * for http common header: "If-Modified-Since", format in RFC1123 GMT
     */
    private Date ifModifiedSince;
    /**
     * for http common header: "If-None-Match"
     */
    private String ifNoneMatch;
    /**
     * for http common header: "If-Unmodified-Since", format in RFC1123 GMT.
     */
    private Date ifUnmodifiedSince;

    /**
     * for http common header: "Content-Length"
     */
    private long contentLength;

    /**
     * for http common header: "Content-Md5"
     */
    private String contentMD5;

    /**
     * for http common header: "Range"
     */
    private long rangeStart;
    private long rangeEnd;

    /**
     * for tos custom defined metadata header, will set to X-Tos-Meta-*
     */
    private Map<String, String> customMetadata;

    /**
     * for acl grant header: "x-tos-acl"
     */
    private ACLType aclType;
    /**
     * for acl grant header: "x-tos-grant-full-control"
     */
    private String grantFullControl;
    /**
     * for acl grant header: "x-tos-grant-read"
     */
    private String grantRead;
    /**
     * for acl grant header: "x-tos-grant-read-acp"
     */
    private String grantReadAcp;
    /**
     * for acl grant header: "x-tos-grant-write"
     */
    private String grantWrite;
    /**
     * for acl grant header: "x-tos-grant-write-acp"
     */
    private String grantWriteAcp;

    /**
     * for encryption relative headers: "x-tos-server-side-encryption-customer-algorithm"
     */
    private String ssecAlgorithm;
    /**
     * for encryption relative headers: "x-tos-server-side-encryption-customer-key"
     */
    private String ssecKey;
    /**
     * for encryption relative headers: "x-tos-server-side-encryption-customer-key-MD5"
     */
    private String ssecKeyMD5;
    /**
     * for encryption relative headers: "x-tos-server-side-encryption"
     */
    private String serverSideEncryption;

    /**
     * for websiteRedirectLocation headers: "x-tos-website-redirect-location"
     */
    private String websiteRedirectLocation;

    /**
     * for storageClass headers: "x-tos-storage-class"
     */
    private StorageClassType storageClass;

    /**
     * for trafficLimit headers: "x-tos-traffic-limit"
     */
    private long trafficLimit;

    private Map<String, String> headers = new HashMap<>();

    public Map<String, String> headers() {
        if (customMetadata != null) {
            for (Map.Entry<String, String> entry : customMetadata.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (StringUtils.isNotEmpty(value)) {
                    this.headers.put(TosHeader.HEADER_META_PREFIX+key, value);
                }
            }
        }
        return headers;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public ObjectMetaRequestOptions setHeaders(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    @Override
    public String toString() {
        return "ObjectMetaRequestOptions{" +
                "cacheControl='" + cacheControl + '\'' +
                ", contentDisposition='" + contentDisposition + '\'' +
                ", contentEncoding='" + contentEncoding + '\'' +
                ", contentLanguage='" + contentLanguage + '\'' +
                ", contentType='" + contentType + '\'' +
                ", expires=" + expires +
                ", ifMatch='" + ifMatch + '\'' +
                ", ifModifiedSince=" + ifModifiedSince +
                ", ifNoneMatch='" + ifNoneMatch + '\'' +
                ", ifUnmodifiedSince=" + ifUnmodifiedSince +
                ", contentLength=" + contentLength +
                ", contentMD5='" + contentMD5 + '\'' +
                ", rangeStart=" + rangeStart +
                ", rangeEnd=" + rangeEnd +
                ", customMetadata=" + customMetadata +
                ", aclType=" + aclType +
                ", grantFullControl='" + grantFullControl + '\'' +
                ", grantRead='" + grantRead + '\'' +
                ", grantReadAcp='" + grantReadAcp + '\'' +
                ", grantWrite='" + grantWrite + '\'' +
                ", grantWriteAcp='" + grantWriteAcp + '\'' +
                ", ssecAlgorithm='" + ssecAlgorithm + '\'' +
                ", ssecKey='" + ssecKey + '\'' +
                ", ssecKeyMD5='" + ssecKeyMD5 + '\'' +
                ", serverSideEncryption='" + serverSideEncryption + '\'' +
                ", websiteRedirectLocation='" + websiteRedirectLocation + '\'' +
                ", storageClass=" + storageClass +
                ", trafficLimit=" + trafficLimit +
                '}';
    }

    public static ObjectMetaRequestOptionsBuilder builder() {
        return new ObjectMetaRequestOptionsBuilder();
    }

    public String getCacheControl() {
        return headers.get(TosHeader.HEADER_CACHE_CONTROL);
    }

    public String getContentDisposition() {
        return headers.get(TosHeader.HEADER_CONTENT_DISPOSITION);
    }

    public String getContentEncoding() {
        return headers.get(TosHeader.HEADER_CONTENT_ENCODING);
    }

    public String getContentLanguage() {
        return headers.get(TosHeader.HEADER_CONTENT_LANGUAGE);
    }

    public String getContentType() {
        return headers.get(TosHeader.HEADER_CONTENT_TYPE);
    }

    public Date getExpires() {
        return DateConverter.rfc1123StringToDate(headers.get(TosHeader.HEADER_EXPIRES));
    }

    public String getIfMatch() {
        return headers.get(TosHeader.HEADER_IF_MATCH);
    }

    public Date getIfModifiedSince() {
        return DateConverter.rfc1123StringToDate(headers.get(TosHeader.HEADER_IF_MODIFIED_SINCE));
    }

    public String getIfNoneMatch() {
        return headers.get(TosHeader.HEADER_IF_NONE_MATCH);
    }

    public Date getIfUnmodifiedSince() {
        return DateConverter.rfc1123StringToDate(headers.get(TosHeader.HEADER_IF_UNMODIFIED_SINCE));
    }

    public long getContentLength() {
        String clStr = headers.get(TosHeader.HEADER_CONTENT_LENGTH);
        if (StringUtils.isEmpty(clStr)) {
            return 0;
        }
        return Long.parseLong(clStr);
    }

    public String getRange() {
        return headers.get(TosHeader.HEADER_RANGE);
    }

    public String getContentMD5() {
        return headers.get(TosHeader.HEADER_CONTENT_MD5);
    }

    public Map<String, String> getCustomMetadata() {
        return customMetadata;
    }

    public ACLType getAclType() {
        return TypeConverter.convertACLType(headers.get(TosHeader.HEADER_ACL));
    }

    public String getGrantFullControl() {
        return headers.get(TosHeader.HEADER_GRANT_FULL_CONTROL);
    }

    public String getGrantRead() {
        return headers.get(TosHeader.HEADER_GRANT_READ);
    }

    public String getGrantReadAcp() {
        return headers.get(TosHeader.HEADER_GRANT_READ_ACP);
    }

    public String getGrantWrite() {
        return headers.get(TosHeader.HEADER_GRANT_WRITE);
    }

    public String getGrantWriteAcp() {
        return headers.get(TosHeader.HEADER_GRANT_WRITE_ACP);
    }

    public String getSsecAlgorithm() {
        return headers.get(TosHeader.HEADER_SSE_CUSTOMER_ALGORITHM);
    }

    public String getSsecKey() {
        return headers.get(TosHeader.HEADER_SSE_CUSTOMER_KEY);
    }

    public String getSsecKeyMD5() {
        return headers.get(TosHeader.HEADER_SSE_CUSTOMER_KEY_MD5);
    }

    public String getServerSideEncryption() {
        return headers.get(TosHeader.HEADER_SSE);
    }

    public String getWebsiteRedirectLocation() {
        return headers.get(TosHeader.HEADER_WEBSITE_REDIRECT_LOCATION);
    }

    public StorageClassType getStorageClass() {
        return TypeConverter.convertStorageClassType(headers.get(TosHeader.HEADER_STORAGE_CLASS));
    }

    public long getTrafficLimit() {
        String tlStr = headers.get(TosHeader.HEADER_TRAFFIC_LIMIT);
        if (StringUtils.isEmpty(tlStr)) {
            return 0;
        }
        return Long.parseLong(tlStr);
    }

    public ObjectMetaRequestOptions setCacheControl(String cacheControl) {
        withHeader(TosHeader.HEADER_CACHE_CONTROL, cacheControl);
        return this;
    }

    public ObjectMetaRequestOptions setContentDisposition(String contentDisposition) {
        withHeader(TosHeader.HEADER_CONTENT_DISPOSITION, contentDisposition);
        return this;
    }

    public ObjectMetaRequestOptions setContentEncoding(String contentEncoding) {
        withHeader(TosHeader.HEADER_CONTENT_ENCODING, contentEncoding);
        return this;
    }

    public ObjectMetaRequestOptions setContentLanguage(String contentLanguage) {
        withHeader(TosHeader.HEADER_CONTENT_LANGUAGE, contentLanguage);
        return this;
    }

    public ObjectMetaRequestOptions setContentType(String contentType) {
        withHeader(TosHeader.HEADER_CONTENT_TYPE, contentType);
        return this;
    }

    public ObjectMetaRequestOptions setExpires(Date expires) {
        if (expires == null) {
            return this;
        }
        withHeader(TosHeader.HEADER_EXPIRES, DateConverter.dateToRFC1123String(expires));
        return this;
    }

    public ObjectMetaRequestOptions setIfMatch(String ifMatch) {
        withHeader(TosHeader.HEADER_IF_MATCH, ifMatch);
        return this;
    }

    public ObjectMetaRequestOptions setIfModifiedSince(Date ifModifiedSince) {
        if (ifModifiedSince == null) {
            return this;
        }
        withHeader(TosHeader.HEADER_IF_MODIFIED_SINCE, DateConverter.dateToRFC1123String(ifModifiedSince));
        return this;
    }

    public ObjectMetaRequestOptions setIfNoneMatch(String ifNoneMatch) {
        withHeader(TosHeader.HEADER_IF_NONE_MATCH, ifNoneMatch);
        return this;
    }

    public ObjectMetaRequestOptions setIfUnmodifiedSince(Date ifUnmodifiedSince) {
        if (ifUnmodifiedSince == null) {
            return this;
        }
        withHeader(TosHeader.HEADER_IF_UNMODIFIED_SINCE, DateConverter.dateToRFC1123String(ifUnmodifiedSince));
        return this;
    }

    public ObjectMetaRequestOptions setContentLength(long contentLength) {
        withHeader(TosHeader.HEADER_CONTENT_LENGTH, String.valueOf(contentLength));
        return this;
    }

    public ObjectMetaRequestOptions setContentMD5(String contentMD5) {
        withHeader(TosHeader.HEADER_CONTENT_MD5, contentMD5);
        return this;
    }

    public ObjectMetaRequestOptions setRange(long rangeStart, long rangeEnd) {
        withHeader(TosHeader.HEADER_RANGE, new HttpRange().setStart(rangeStart).setEnd(rangeEnd).toString());
        return this;
    }

    public ObjectMetaRequestOptions setCustomMetadata(Map<String, String> customMetadata) {
        this.customMetadata = customMetadata;
        return this;
    }

    public ObjectMetaRequestOptions setAclType(ACLType aclType) {
        withHeader(TosHeader.HEADER_ACL, aclType == null ? null : aclType.toString());
        return this;
    }

    public ObjectMetaRequestOptions setGrantFullControl(String grantFullControl) {
        withHeader(TosHeader.HEADER_GRANT_FULL_CONTROL, grantFullControl);
        return this;
    }

    public ObjectMetaRequestOptions setGrantRead(String grantRead) {
        withHeader(TosHeader.HEADER_GRANT_READ, grantRead);
        return this;
    }

    public ObjectMetaRequestOptions setGrantReadAcp(String grantReadAcp) {
        withHeader(TosHeader.HEADER_GRANT_READ_ACP, grantReadAcp);
        return this;
    }

    public ObjectMetaRequestOptions setGrantWrite(String grantWrite) {
        withHeader(TosHeader.HEADER_GRANT_WRITE, grantWrite);
        return this;
    }

    public ObjectMetaRequestOptions setGrantWriteAcp(String grantWriteAcp) {
        withHeader(TosHeader.HEADER_GRANT_WRITE_ACP, grantWriteAcp);
        return this;
    }

    public ObjectMetaRequestOptions setSsecAlgorithm(String ssecAlgorithm) {
        // only support AES256
        if (StringUtils.isNotEmpty(ssecAlgorithm) &&
                Consts.CUSTOM_SERVER_SIDE_ENCRYPTION_ALGORITHM_LIST.contains(ssecAlgorithm)) {
            withHeader(TosHeader.HEADER_SSE_CUSTOMER_ALGORITHM, ssecAlgorithm);
        } else {
            throw new TosClientException("invalid encryption-decryption algorithm", null);
        }
        return this;
    }

    public ObjectMetaRequestOptions setSsecKey(String ssecKey) {
        withHeader(TosHeader.HEADER_SSE_CUSTOMER_KEY, ssecKey);
        return this;
    }

    public ObjectMetaRequestOptions setSsecKeyMD5(String ssecKeyMD5) {
        withHeader(TosHeader.HEADER_SSE_CUSTOMER_KEY_MD5, ssecKeyMD5);
        return this;
    }

    public ObjectMetaRequestOptions setServerSideEncryption(String serverSideEncryption) {
        // only support AES256
        if (StringUtils.isNotEmpty(serverSideEncryption) &&
                Consts.CUSTOM_SERVER_SIDE_ENCRYPTION_ALGORITHM_LIST.contains(serverSideEncryption)) {
            withHeader(TosHeader.HEADER_SSE, serverSideEncryption);
        } else {
            throw new TosClientException("invalid serverSideEncryption input, only support AES256", null);
        }
        return this;
    }

    public ObjectMetaRequestOptions setWebsiteRedirectLocation(String websiteRedirectLocation) {
        withHeader(TosHeader.HEADER_WEBSITE_REDIRECT_LOCATION, websiteRedirectLocation);
        return this;
    }

    public ObjectMetaRequestOptions setStorageClass(StorageClassType storageClass) {
        withHeader(TosHeader.HEADER_STORAGE_CLASS, storageClass == null ? null : storageClass.toString());
        return this;
    }

    public ObjectMetaRequestOptions setTrafficLimit(long trafficLimit) {
        withHeader(TosHeader.HEADER_TRAFFIC_LIMIT, String.valueOf(trafficLimit));
        return this;
    }

    private void withHeader(String key, String value){
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(value)) {
            return;
        }
        this.headers.put(key, value);
    }

    public static final class ObjectMetaRequestOptionsBuilder {
        private Map<String, String> headers;
        private Map<String, String> customMetaData;
        private Date expires;
        private Date ifModifiedSince;
        private Date ifUnmodifiedSince;

        private ObjectMetaRequestOptionsBuilder() {
            headers = new HashMap<>();
        }

        private void withHeader(String key, String value){
            if (StringUtils.isEmpty(key) || StringUtils.isEmpty(value)) {
                return;
            }
            this.headers.put(key, value);
        }

        public ObjectMetaRequestOptionsBuilder cacheControl(String cacheControl) {
            withHeader(TosHeader.HEADER_CACHE_CONTROL, cacheControl);
            return this;
        }

        public ObjectMetaRequestOptionsBuilder contentDisposition(String contentDisposition) {
            withHeader(TosHeader.HEADER_CONTENT_DISPOSITION, contentDisposition);
            return this;
        }

        public ObjectMetaRequestOptionsBuilder contentEncoding(String contentEncoding) {
            withHeader(TosHeader.HEADER_CONTENT_ENCODING, contentEncoding);
            return this;
        }

        public ObjectMetaRequestOptionsBuilder contentLanguage(String contentLanguage) {
            withHeader(TosHeader.HEADER_CONTENT_LANGUAGE, contentLanguage);
            return this;
        }

        public ObjectMetaRequestOptionsBuilder contentType(String contentType) {
            withHeader(TosHeader.HEADER_CONTENT_TYPE, contentType);
            return this;
        }

        public ObjectMetaRequestOptionsBuilder expires(Date expires) {
            this.expires = expires;
            return this;
        }

        public ObjectMetaRequestOptionsBuilder ifMatch(String ifMatch) {
            withHeader(TosHeader.HEADER_IF_MATCH, ifMatch);
            return this;
        }

        public ObjectMetaRequestOptionsBuilder ifModifiedSince(Date ifModifiedSince) {
            this.ifModifiedSince = ifModifiedSince;
            return this;
        }

        public ObjectMetaRequestOptionsBuilder ifNoneMatch(String ifNoneMatch) {
            withHeader(TosHeader.HEADER_IF_NONE_MATCH, ifNoneMatch);
            return this;
        }

        public ObjectMetaRequestOptionsBuilder ifUnmodifiedSince(Date ifUnmodifiedSince) {
            this.ifUnmodifiedSince = ifUnmodifiedSince;
            return this;
        }

        public ObjectMetaRequestOptionsBuilder contentLength(long contentLength) {
            withHeader(TosHeader.HEADER_CONTENT_LENGTH, String.valueOf(contentLength));
            return this;
        }

        public ObjectMetaRequestOptionsBuilder contentMD5(String contentMD5) {
            withHeader(TosHeader.HEADER_CONTENT_MD5, contentMD5);
            return this;
        }

        public ObjectMetaRequestOptionsBuilder range(long rangeStart, long rangeEnd) {
            withHeader(TosHeader.HEADER_RANGE, new HttpRange().setStart(rangeStart).setEnd(rangeEnd).toString());
            return this;
        }

        public ObjectMetaRequestOptionsBuilder customMetadata(Map<String, String> customMetadata) {
            this.customMetaData = customMetadata;
            return this;
        }

        public ObjectMetaRequestOptionsBuilder aclType(ACLType aclType) {
            withHeader(TosHeader.HEADER_ACL, aclType == null ? null : aclType.toString());
            return this;
        }

        public ObjectMetaRequestOptionsBuilder grantFullControl(String grantFullControl) {
            withHeader(TosHeader.HEADER_GRANT_FULL_CONTROL, grantFullControl);
            return this;
        }

        public ObjectMetaRequestOptionsBuilder grantRead(String grantRead) {
            withHeader(TosHeader.HEADER_GRANT_READ, grantRead);
            return this;
        }

        public ObjectMetaRequestOptionsBuilder grantReadAcp(String grantReadAcp) {
            withHeader(TosHeader.HEADER_GRANT_READ_ACP, grantReadAcp);
            return this;
        }

        public ObjectMetaRequestOptionsBuilder grantWrite(String grantWrite) {
            withHeader(TosHeader.HEADER_GRANT_WRITE, grantWrite);
            return this;
        }

        public ObjectMetaRequestOptionsBuilder grantWriteAcp(String grantWriteAcp) {
            withHeader(TosHeader.HEADER_GRANT_WRITE_ACP, grantWriteAcp);
            return this;
        }

        public ObjectMetaRequestOptionsBuilder ssecAlgorithm(String ssecAlgorithm) {
            // only support AES256
            if (StringUtils.isNotEmpty(ssecAlgorithm) &&
                    Consts.CUSTOM_SERVER_SIDE_ENCRYPTION_ALGORITHM_LIST.contains(ssecAlgorithm)) {
                withHeader(TosHeader.HEADER_SSE_CUSTOMER_ALGORITHM, ssecAlgorithm);
            } else {
                throw new TosClientException("invalid encryption-decryption algorithm", null);
            }
            return this;
        }

        public ObjectMetaRequestOptionsBuilder ssecKey(String ssecKey) {
            withHeader(TosHeader.HEADER_SSE_CUSTOMER_KEY, ssecKey);
            return this;
        }

        public ObjectMetaRequestOptionsBuilder ssecKeyMD5(String ssecKeyMD5) {
            withHeader(TosHeader.HEADER_SSE_CUSTOMER_KEY_MD5, ssecKeyMD5);
            return this;
        }

        public ObjectMetaRequestOptionsBuilder serverSideEncryption(String serverSideEncryption) {
            // only support AES256
            if (StringUtils.isNotEmpty(serverSideEncryption) &&
                    Consts.CUSTOM_SERVER_SIDE_ENCRYPTION_ALGORITHM_LIST.contains(serverSideEncryption)) {
                withHeader(TosHeader.HEADER_SSE, serverSideEncryption);
            } else {
                throw new TosClientException("invalid serverSideEncryption input, only support AES256", null);
            }
            return this;
        }

        public ObjectMetaRequestOptionsBuilder websiteRedirectLocation(String websiteRedirectLocation) {
            withHeader(TosHeader.HEADER_WEBSITE_REDIRECT_LOCATION, websiteRedirectLocation);
            return this;
        }

        public ObjectMetaRequestOptionsBuilder storageClass(StorageClassType storageClass) {
            withHeader(TosHeader.HEADER_STORAGE_CLASS, storageClass == null ? null : storageClass.toString());
            return this;
        }

        public ObjectMetaRequestOptionsBuilder trafficLimit(long trafficLimit) {
            withHeader(TosHeader.HEADER_TRAFFIC_LIMIT, String.valueOf(trafficLimit));
            return this;
        }

        public ObjectMetaRequestOptions build() {
            withTimeHeader(TosHeader.HEADER_EXPIRES, this.expires);
            withTimeHeader(TosHeader.HEADER_IF_MODIFIED_SINCE, this.ifModifiedSince);
            withTimeHeader(TosHeader.HEADER_IF_UNMODIFIED_SINCE, this.ifUnmodifiedSince);

            ObjectMetaRequestOptions objectMetaRequestOptions = new ObjectMetaRequestOptions();
            objectMetaRequestOptions.headers = this.headers;
            objectMetaRequestOptions.customMetadata = this.customMetaData;
            return objectMetaRequestOptions;
        }

        private void withTimeHeader(String key, Date date) {
            if (date == null) {
                return;
            }
            withHeader(key, DateConverter.dateToRFC1123String(date));
        }
    }
}
