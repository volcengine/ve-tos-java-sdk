package com.volcengine.tos.model.object;

import com.volcengine.tos.internal.TosResponse;
import com.volcengine.tos.comm.TosHeader;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Deprecated
public class ObjectMeta {
    private long contentLength;
    private String contentType;
    private String contentMD5;
    private String contentLanguage;
    private String contentEncoding;
    private String contentDisposition;
    private String lastModified;
    private String cacheControl;
    private String expires;
    private String etags;
    private String versionID;
    private boolean deleteMarker;
    // "" or "appendable"
    private String objectType;
    private String storageClass;
    private String restore;
    private Map<String, String> metadata;
    private String mirrorTag;
    private String sseCustomerAlgorithm;
    private String sseCustomerKeyMD5;
    private String csType;
    private String crc64;

    public ObjectMeta fromResponse(TosResponse res) {
        this.contentLength = res.getContentLength();
        this.contentType = res.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_CONTENT_TYPE);
        this.contentMD5 = res.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_CONTENT_MD5);
        this.contentLanguage = res.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_CONTENT_LANGUAGE);
        this.contentEncoding = res.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_CONTENT_ENCODING);
        this.contentDisposition = res.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_CONTENT_DISPOSITION);
        this.lastModified = res.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_LAST_MODIFIED);
        this.cacheControl = res.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_CACHE_CONTROL);
        this.expires = res.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_EXPIRES);
        this.etags = res.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_ETAG);
        this.versionID = res.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_VERSIONID);
        this.deleteMarker = Boolean.parseBoolean(res.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_DELETE_MARKER));
        this.objectType = res.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_OBJECT_TYPE);
        this.storageClass = res.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_STORAGE_CLASS);
        this.restore = res.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_RESTORE);
        this.metadata = userMetadata(res.getHeaders());
        this.mirrorTag = res.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_MIRROR_TAG);
        this.sseCustomerAlgorithm = res.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_SSE_CUSTOMER_ALGORITHM);
        this.sseCustomerKeyMD5 = res.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_SSE_CUSTOMER_KEY_MD5);
        this.csType = res.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_CS_TYPE);
        this.crc64 = res.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_CRC64);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ObjectMeta that = (ObjectMeta) o;
        return contentLength == that.contentLength && deleteMarker == that.deleteMarker
                && Objects.equals(contentType, that.contentType) && Objects.equals(contentMD5, that.contentMD5)
                && Objects.equals(contentLanguage, that.contentLanguage) && Objects.equals(contentEncoding, that.contentEncoding)
                && Objects.equals(contentDisposition, that.contentDisposition) && Objects.equals(lastModified, that.lastModified)
                && Objects.equals(cacheControl, that.cacheControl) && Objects.equals(expires, that.expires)
                && Objects.equals(etags, that.etags) && Objects.equals(versionID, that.versionID)
                && Objects.equals(objectType, that.objectType) && Objects.equals(storageClass, that.storageClass)
                && Objects.equals(restore, that.restore) && Objects.equals(metadata, that.metadata)
                && Objects.equals(mirrorTag, that.mirrorTag) && Objects.equals(sseCustomerAlgorithm, that.sseCustomerAlgorithm)
                && Objects.equals(sseCustomerKeyMD5, that.sseCustomerKeyMD5) && Objects.equals(csType, that.csType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contentLength, contentType, contentMD5, contentLanguage, contentEncoding, contentDisposition, lastModified, cacheControl, expires, etags, versionID, deleteMarker, objectType, storageClass, restore, metadata, mirrorTag, sseCustomerAlgorithm, sseCustomerKeyMD5, csType);
    }

    private Map<String, String> userMetadata(Map<String, String> headers){
        Map<String, String> meta = new HashMap<>();
        for (String key : headers.keySet()) {
            if (key.startsWith(TosHeader.HEADER_META_PREFIX.toLowerCase())){
                String kk = key.substring(TosHeader.HEADER_META_PREFIX.length());
                meta.put(kk, headers.get(key));
            }
        }
        return meta;
    }

    public long getContentLength() {
        return contentLength;
    }

    public ObjectMeta setContentLength(long contentLength) {
        this.contentLength = contentLength;
        return this;
    }

    public String getContentType() {
        return contentType;
    }

    public ObjectMeta setContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public String getContentMD5() {
        return contentMD5;
    }

    public ObjectMeta setContentMD5(String contentMD5) {
        this.contentMD5 = contentMD5;
        return this;
    }

    public String getContentLanguage() {
        return contentLanguage;
    }

    public ObjectMeta setContentLanguage(String contentLanguage) {
        this.contentLanguage = contentLanguage;
        return this;
    }

    public String getContentEncoding() {
        return contentEncoding;
    }

    public ObjectMeta setContentEncoding(String contentEncoding) {
        this.contentEncoding = contentEncoding;
        return this;
    }

    public String getContentDisposition() {
        return contentDisposition;
    }

    public ObjectMeta setContentDisposition(String contentDisposition) {
        this.contentDisposition = contentDisposition;
        return this;
    }

    public String getLastModified() {
        return lastModified;
    }

    public ObjectMeta setLastModified(String lastModified) {
        this.lastModified = lastModified;
        return this;
    }

    public String getCacheControl() {
        return cacheControl;
    }

    public ObjectMeta setCacheControl(String cacheControl) {
        this.cacheControl = cacheControl;
        return this;
    }

    public String getExpires() {
        return expires;
    }

    public ObjectMeta setExpires(String expires) {
        this.expires = expires;
        return this;
    }

    public String getEtags() {
        return etags;
    }

    public ObjectMeta setEtags(String etags) {
        this.etags = etags;
        return this;
    }

    public String getVersionID() {
        return versionID;
    }

    public ObjectMeta setVersionID(String versionID) {
        this.versionID = versionID;
        return this;
    }

    public boolean isDeleteMarker() {
        return deleteMarker;
    }

    public ObjectMeta setDeleteMarker(boolean deleteMarker) {
        this.deleteMarker = deleteMarker;
        return this;
    }

    public String getObjectType() {
        return objectType;
    }

    public ObjectMeta setObjectType(String objectType) {
        this.objectType = objectType;
        return this;
    }

    public String getStorageClass() {
        return storageClass;
    }

    public ObjectMeta setStorageClass(String storageClass) {
        this.storageClass = storageClass;
        return this;
    }

    public String getRestore() {
        return restore;
    }

    public ObjectMeta setRestore(String restore) {
        this.restore = restore;
        return this;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public ObjectMeta setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
        return this;
    }

    public String getMirrorTag() {
        return mirrorTag;
    }

    public ObjectMeta setMirrorTag(String mirrorTag) {
        this.mirrorTag = mirrorTag;
        return this;
    }

    public String getSseCustomerAlgorithm() {
        return sseCustomerAlgorithm;
    }

    public ObjectMeta setSseCustomerAlgorithm(String sseCustomerAlgorithm) {
        this.sseCustomerAlgorithm = sseCustomerAlgorithm;
        return this;
    }

    public String getSseCustomerKeyMD5() {
        return sseCustomerKeyMD5;
    }

    public ObjectMeta setSseCustomerKeyMD5(String sseCustomerKeyMD5) {
        this.sseCustomerKeyMD5 = sseCustomerKeyMD5;
        return this;
    }

    public String getCsType() {
        return csType;
    }

    public ObjectMeta setCsType(String csType) {
        this.csType = csType;
        return this;
    }

    public String getCrc64() {
        return crc64;
    }

    public ObjectMeta setCrc64(String crc64) {
        this.crc64 = crc64;
        return this;
    }

    @Override
    public String toString() {
        return "ObjectMeta{" +
                "contentLength=" + contentLength +
                ", contentType='" + contentType + '\'' +
                ", contentMD5='" + contentMD5 + '\'' +
                ", contentLanguage='" + contentLanguage + '\'' +
                ", contentEncoding='" + contentEncoding + '\'' +
                ", contentDisposition='" + contentDisposition + '\'' +
                ", lastModified='" + lastModified + '\'' +
                ", cacheControl='" + cacheControl + '\'' +
                ", expires='" + expires + '\'' +
                ", etags='" + etags + '\'' +
                ", versionID='" + versionID + '\'' +
                ", deleteMarker=" + deleteMarker +
                ", objectType='" + objectType + '\'' +
                ", storageClass='" + storageClass + '\'' +
                ", restore='" + restore + '\'' +
                ", metadata=" + metadata +
                ", mirrorTag='" + mirrorTag + '\'' +
                ", sseCustomerAlgorithm='" + sseCustomerAlgorithm + '\'' +
                ", sseCustomerKeyMD5='" + sseCustomerKeyMD5 + '\'' +
                ", csType='" + csType + '\'' +
                ", crc64=" + crc64 +
                '}';
    }
}
