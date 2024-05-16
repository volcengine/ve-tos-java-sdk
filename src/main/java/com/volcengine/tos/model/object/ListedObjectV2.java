package com.volcengine.tos.model.object;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.internal.util.TosUtils;
import com.volcengine.tos.model.acl.Owner;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class ListedObjectV2 {
    @JsonProperty("Key")
    private String key;
    @JsonProperty("LastModified")
    private Date lastModified;
    @JsonProperty("ETag")
    private String etag;
    @JsonProperty("Size")
    private long size;
    @JsonProperty("Owner")
    private Owner owner;
    @JsonProperty("StorageClass")
    private String storageClass;
    @JsonProperty("Type")
    private String type;
    @JsonProperty("HashCrc64ecma")
    private String hashCrc64ecma;
    @JsonProperty("HashCrc32c")
    private String hashCrc32c;
    @JsonProperty("UserMeta")
    private List<Map<String, String>> userMeta;
    @JsonIgnore
    private boolean disableEncodingMeta;

    public String getKey() {
        return key;
    }

    public ListedObjectV2 setKey(String key) {
        this.key = key;
        return this;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public ListedObjectV2 setLastModified(Date lastModified) {
        this.lastModified = lastModified;
        return this;
    }

    public String getEtag() {
        return etag;
    }

    public ListedObjectV2 setEtag(String etag) {
        this.etag = etag;
        return this;
    }

    public long getSize() {
        return size;
    }

    public ListedObjectV2 setSize(long size) {
        this.size = size;
        return this;
    }

    public Owner getOwner() {
        return owner;
    }

    public ListedObjectV2 setOwner(Owner owner) {
        this.owner = owner;
        return this;
    }

    public String getStorageClass() {
        return storageClass;
    }

    public ListedObjectV2 setStorageClass(String storageClass) {
        this.storageClass = storageClass;
        return this;
    }

    public String getType() {
        return type;
    }

    public ListedObjectV2 setType(String type) {
        this.type = type;
        return this;
    }

    public String getHashCrc64ecma() {
        return hashCrc64ecma;
    }

    public ListedObjectV2 setHashCrc64ecma(String hashCrc64ecma) {
        this.hashCrc64ecma = hashCrc64ecma;
        return this;
    }

    public String getHashCrc32c() {
        return hashCrc32c;
    }

    public ListedObjectV2 setHashCrc32c(String hashCrc32c) {
        this.hashCrc32c = hashCrc32c;
        return this;
    }

    public Map<String, String> getMeta() {
        return TosUtils.parseMeta(this.userMeta, disableEncodingMeta);
    }

    @Override
    public String toString() {
        return "ListedObjectV2{" +
                "key='" + key + '\'' +
                ", lastModified=" + lastModified +
                ", etag='" + etag + '\'' +
                ", size=" + size +
                ", owner=" + owner +
                ", storageClass='" + storageClass + '\'' +
                ", type='" + type + '\'' +
                ", hashCrc64ecma='" + hashCrc64ecma + '\'' +
                ", hashCrc32c='" + hashCrc32c + '\'' +
                ", meta=" + getMeta() +
                '}';
    }
}
