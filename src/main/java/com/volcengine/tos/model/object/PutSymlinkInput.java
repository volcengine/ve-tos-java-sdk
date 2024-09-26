package com.volcengine.tos.model.object;

import com.volcengine.tos.comm.common.ACLType;
import com.volcengine.tos.comm.common.StorageClassType;
import com.volcengine.tos.model.GenericInput;

import java.util.Date;
import java.util.Map;

public class PutSymlinkInput extends GenericInput {
    private String bucket;
    private String key;
    private String symlinkTargetKey;
    private String symlinkTargetBucket;
    private boolean forbidOverwrite;
    private ACLType acl;
    private Map<String, String> meta;
    private StorageClassType storageClass;
    private String cacheControl;
    private String contentDisposition;
    private String contentEncoding;
    private String contentLanguage;
    private String contentType;
    private Date expires;
    private String tagging;

    public String getBucket() {
        return bucket;
    }

    public PutSymlinkInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public String getKey() {
        return key;
    }

    public PutSymlinkInput setKey(String key) {
        this.key = key;
        return this;
    }

    public String getSymlinkTargetKey() {
        return symlinkTargetKey;
    }

    public PutSymlinkInput setSymlinkTargetKey(String symlinkTargetKey) {
        this.symlinkTargetKey = symlinkTargetKey;
        return this;
    }

    public String getSymlinkTargetBucket() {
        return symlinkTargetBucket;
    }

    public PutSymlinkInput setSymlinkTargetBucket(String symlinkTargetBucket) {
        this.symlinkTargetBucket = symlinkTargetBucket;
        return this;
    }

    public boolean isForbidOverwrite() {
        return forbidOverwrite;
    }

    public PutSymlinkInput setForbidOverwrite(boolean forbidOverwrite) {
        this.forbidOverwrite = forbidOverwrite;
        return this;
    }

    public ACLType getAcl() {
        return acl;
    }

    public PutSymlinkInput setAcl(ACLType acl) {
        this.acl = acl;
        return this;
    }

    public Map<String, String> getMeta() {
        return meta;
    }

    public PutSymlinkInput setMeta(Map<String, String> meta) {
        this.meta = meta;
        return this;
    }

    public StorageClassType getStorageClass() {
        return storageClass;
    }

    public PutSymlinkInput setStorageClass(StorageClassType storageClass) {
        this.storageClass = storageClass;
        return this;
    }


    public String getCacheControl() {
        return cacheControl;
    }

    public PutSymlinkInput setCacheControl(String cacheControl) {
        this.cacheControl = cacheControl;
        return this;
    }

    public String getContentDisposition() {
        return contentDisposition;
    }

    public PutSymlinkInput setContentDisposition(String contentDisposition) {
        this.contentDisposition = contentDisposition;
        return this;
    }

    public String getContentEncoding() {
        return contentEncoding;
    }

    public PutSymlinkInput setContentEncoding(String contentEncoding) {
        this.contentEncoding = contentEncoding;
        return this;
    }

    public String getContentLanguage() {
        return contentLanguage;
    }

    public PutSymlinkInput setContentLanguage(String contentLanguage) {
        this.contentLanguage = contentLanguage;
        return this;
    }

    public String getContentType() {
        return contentType;
    }

    public PutSymlinkInput setContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public Date getExpires() {
        return expires;
    }

    public PutSymlinkInput setExpires(Date expires) {
        this.expires = expires;
        return this;
    }

    public String getTagging() {
        return tagging;
    }

    public PutSymlinkInput setTagging(String tagging) {
        this.tagging = tagging;
        return this;
    }

    @Override
    public String toString() {
        return "PutSymlinkInput{" +
                "bucket='" + bucket + '\'' +
                ", key='" + key + '\'' +
                ", symlinkTargetKey='" + symlinkTargetKey + '\'' +
                ", symlinkTargetBucket='" + symlinkTargetBucket + '\'' +
                ", forbidOverwrite=" + forbidOverwrite +
                ", acl=" + acl +
                ", meta=" + meta +
                ", storageClass=" + storageClass +
                ", cacheControl='" + cacheControl + '\'' +
                ", contentDisposition='" + contentDisposition + '\'' +
                ", contentEncoding='" + contentEncoding + '\'' +
                ", contentLanguage='" + contentLanguage + '\'' +
                ", contentType='" + contentType + '\'' +
                ", expires=" + expires +
                ", tagging='" + tagging + '\'' +
                '}';
    }
}
