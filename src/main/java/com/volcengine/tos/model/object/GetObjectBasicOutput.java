package com.volcengine.tos.model.object;

import com.volcengine.tos.comm.TosHeader;
import com.volcengine.tos.comm.common.StorageClassType;
import com.volcengine.tos.internal.TosResponse;
import com.volcengine.tos.internal.util.DateConverter;
import com.volcengine.tos.internal.util.TosUtils;
import com.volcengine.tos.internal.util.TypeConverter;
import com.volcengine.tos.model.RequestInfo;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class GetObjectBasicOutput {
    private RequestInfo requestInfo;

    private String contentRange;
    private String etag;
    private String lastModified;
    private boolean deleteMarker;

    private String ssecAlgorithm;
    private String ssecKeyMD5;
    private String versionID;
    private String websiteRedirectLocation;
    private String objectType;
    private String hashCrc64ecma;
    private String storageClass;
    private Map<String, String> customMetadata;

    private long contentLength;
    private String cacheControl;
    private String contentDisposition;
    private String contentEncoding;
    private String contentLanguage;
    private String contentType;
    private String expires;
    private String contentMD5;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public GetObjectBasicOutput setRequestInfo(RequestInfo info) {
        this.requestInfo = info;
        return this;
    }

    public String getContentRange() {
        return contentRange;
    }

    public String getEtag() {
        return etag;
    }

    public String getLastModified() {
        return lastModified;
    }

    public Date getLastModifiedInDate() {
        return DateConverter.rfc1123StringToDate(lastModified);
    }

    public boolean isDeleteMarker() {
        return deleteMarker;
    }

    public String getSsecAlgorithm() {
        return ssecAlgorithm;
    }

    public String getSsecKeyMD5() {
        return ssecKeyMD5;
    }

    public String getVersionID() {
        return versionID;
    }

    public String getWebsiteRedirectLocation() {
        return websiteRedirectLocation;
    }

    public String getObjectType() {
        return objectType;
    }

    public String getHashCrc64ecma() {
        return hashCrc64ecma;
    }

    public StorageClassType getStorageClass() {
        return TypeConverter.convertStorageClassType(storageClass);
    }

    public Map<String, String> getCustomMetadata() {
        return customMetadata;
    }

    public long getContentLength() {
        return contentLength;
    }

    public String getCacheControl() {
        return this.cacheControl;
    }

    public String getContentDisposition() {
        return contentDisposition;
    }

    public String getContentEncoding() {
        return contentEncoding;
    }

    public String getContentLanguage() {
        return contentLanguage;
    }

    public String getContentType() {
        return contentType;
    }

    public String getExpires() {
        return expires;
    }

    public Date getExpiresInDate() {
        return DateConverter.rfc1123StringToDate(expires);
    }

    public String getContentMD5() {
        return contentMD5;
    }

    public GetObjectBasicOutput parseFromTosResponse(TosResponse response) {
        this.contentLength = response.getContentLength();
        this.contentType = response.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_CONTENT_TYPE);
        this.contentMD5 = response.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_CONTENT_MD5);
        this.contentLanguage = response.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_CONTENT_LANGUAGE);
        this.contentEncoding = response.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_CONTENT_ENCODING);
        this.contentDisposition = response.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_CONTENT_DISPOSITION);
        this.lastModified = response.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_LAST_MODIFIED);
        this.cacheControl = response.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_CACHE_CONTROL);
        this.expires = response.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_EXPIRES);
        this.etag = response.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_ETAG);
        this.versionID = response.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_VERSIONID);
        this.deleteMarker = Boolean.parseBoolean(response.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_DELETE_MARKER));
        this.objectType = response.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_OBJECT_TYPE);
        this.storageClass = response.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_STORAGE_CLASS);
        this.customMetadata = parseCustomMetadata(response.getHeaders());
        this.ssecAlgorithm = response.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_SSE_CUSTOMER_ALGORITHM);
        this.ssecKeyMD5 = response.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_SSE_CUSTOMER_KEY_MD5);
        this.websiteRedirectLocation = response.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_WEBSITE_REDIRECT_LOCATION);
        this.hashCrc64ecma = response.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_CRC64);
        this.storageClass = response.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_STORAGE_CLASS);
        this.contentRange = response.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_CONTENT_RANGE);
        return this;
    }

    private Map<String, String> parseCustomMetadata(Map<String, String> headers){
        if (headers == null) {
            return null;
        }
        Map<String, String> meta = null;
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            String key = entry.getKey();
            if (key.startsWith(TosHeader.HEADER_META_PREFIX.toLowerCase())) {
                if (meta == null) {
                    meta = new HashMap<>();
                }
                String kk = key.substring(TosHeader.HEADER_META_PREFIX.length());
                meta.put(kk, TosUtils.tryDecodeValue(key, headers.get(key)));
            }
        }
        return meta;
    }
    @Override
    public String toString() {
        return "GetObjectBasicOutput{" +
                "requestInfo=" + requestInfo +
                ", contentRange='" + contentRange + '\'' +
                ", etag='" + etag + '\'' +
                ", lastModified=" + lastModified +
                ", deleteMarker=" + deleteMarker +
                ", ssecAlgorithm='" + ssecAlgorithm + '\'' +
                ", ssecKeyMD5='" + ssecKeyMD5 + '\'' +
                ", versionID='" + versionID + '\'' +
                ", websiteRedirectLocation='" + websiteRedirectLocation + '\'' +
                ", objectType='" + objectType + '\'' +
                ", hashCrc64ecma=" + hashCrc64ecma +
                ", storageClass=" + storageClass +
                ", metadata=" + customMetadata +
                ", cacheControl='" + cacheControl + '\'' +
                ", contentDisposition='" + contentDisposition + '\'' +
                ", contentEncoding='" + contentEncoding + '\'' +
                ", contentLanguage='" + contentLanguage + '\'' +
                ", contentType='" + contentType + '\'' +
                ", expires=" + expires + '\'' +
                '}';
    }
}
