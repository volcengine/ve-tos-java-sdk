package com.volcengine.tos.model.object;

import com.volcengine.tos.model.acl.Owner;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ListedObjectVersion {
    @JsonProperty("ETag")
    private String etag;
    @JsonProperty("IsLatest")
    private boolean isLatest;
    @JsonProperty("Key")
    private String key;
    @JsonProperty("LastModified")
    private String lastModified;
    @JsonProperty("Owner")
    private Owner owner;
    @JsonProperty("Size")
    private long size;
    @JsonProperty("StorageClass")
    private String storageClass;
    @JsonProperty("Type")
    private String type;
    @JsonProperty("VersionId")
    private String versionID;

    public String getEtag() {
        return etag;
    }

    public ListedObjectVersion setEtag(String etag) {
        this.etag = etag;
        return this;
    }

    public boolean isLatest() {
        return isLatest;
    }

    public ListedObjectVersion setLatest(boolean latest) {
        isLatest = latest;
        return this;
    }

    public String getKey() {
        return key;
    }

    public ListedObjectVersion setKey(String key) {
        this.key = key;
        return this;
    }

    public String getLastModified() {
        return lastModified;
    }

    public ListedObjectVersion setLastModified(String lastModified) {
        this.lastModified = lastModified;
        return this;
    }

    public Owner getOwner() {
        return owner;
    }

    public ListedObjectVersion setOwner(Owner owner) {
        this.owner = owner;
        return this;
    }

    public long getSize() {
        return size;
    }

    public ListedObjectVersion setSize(long size) {
        this.size = size;
        return this;
    }

    public String getStorageClass() {
        return storageClass;
    }

    public ListedObjectVersion setStorageClass(String storageClass) {
        this.storageClass = storageClass;
        return this;
    }

    public String getType() {
        return type;
    }

    public ListedObjectVersion setType(String type) {
        this.type = type;
        return this;
    }

    public String getVersionID() {
        return versionID;
    }

    public ListedObjectVersion setVersionID(String versionID) {
        this.versionID = versionID;
        return this;
    }

    @Override
    public String toString() {
        return "ListedObjectVersion{" +
                "etag='" + etag + '\'' +
                ", isLatest=" + isLatest +
                ", key='" + key + '\'' +
                ", lastModified='" + lastModified + '\'' +
                ", owner=" + owner +
                ", size=" + size +
                ", storageClass='" + storageClass + '\'' +
                ", type='" + type + '\'' +
                ", versionID='" + versionID + '\'' +
                '}';
    }
}
