package com.volcengine.tos.model.object;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.acl.Owner;

@Deprecated
public class ListedObject {
    @JsonProperty("Key")
    private String key;
    @JsonProperty("LastModified")
    private String lastModified;
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

    public String getKey() {
        return key;
    }

    public ListedObject setKey(String key) {
        this.key = key;
        return this;
    }

    public String getLastModified() {
        return lastModified;
    }

    public ListedObject setLastModified(String lastModified) {
        this.lastModified = lastModified;
        return this;
    }

    public String getEtag() {
        return etag;
    }

    public ListedObject setEtag(String etag) {
        this.etag = etag;
        return this;
    }

    public long getSize() {
        return size;
    }

    public ListedObject setSize(long size) {
        this.size = size;
        return this;
    }

    public Owner getOwner() {
        return owner;
    }

    public ListedObject setOwner(Owner owner) {
        this.owner = owner;
        return this;
    }

    public String getStorageClass() {
        return storageClass;
    }

    public ListedObject setStorageClass(String storageClass) {
        this.storageClass = storageClass;
        return this;
    }

    @Deprecated
    public String getType() {
        return type;
    }

    @Deprecated
    public ListedObject setType(String type) {
        this.type = type;
        return this;
    }

    public String getObjectType() {
        return type;
    }

    @JsonIgnore
    public ListedObject setObjectType(String type) {
        this.type = type;
        return this;
    }

    @Override
    public String toString() {
        return "ListedObject{" +
                "key='" + key + '\'' +
                ", lastModified='" + lastModified + '\'' +
                ", etag='" + etag + '\'' +
                ", size=" + size +
                ", owner=" + owner +
                ", storageClass='" + storageClass + '\'' +
                ", objectType='" + type + '\'' +
                '}';
    }
}
