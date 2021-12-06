package com.volcengine.tos.model.object;

import com.volcengine.tos.model.acl.Owner;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UploadInfo {
    @JsonProperty("Key")
    private String key;
    @JsonProperty("UploadId")
    private String uploadID;
    @JsonProperty("Owner")
    private Owner owner;
    @JsonProperty("StorageClass")
    private String storageClass;
    @JsonProperty("Initiated")
    private String initiated;

    public String getKey() {
        return key;
    }

    public UploadInfo setKey(String key) {
        this.key = key;
        return this;
    }

    public String getUploadID() {
        return uploadID;
    }

    public UploadInfo setUploadID(String uploadID) {
        this.uploadID = uploadID;
        return this;
    }

    public Owner getOwner() {
        return owner;
    }

    public UploadInfo setOwner(Owner owner) {
        this.owner = owner;
        return this;
    }

    public String getStorageClass() {
        return storageClass;
    }

    public UploadInfo setStorageClass(String storageClass) {
        this.storageClass = storageClass;
        return this;
    }

    public String getInitiated() {
        return initiated;
    }

    public UploadInfo setInitiated(String initiated) {
        this.initiated = initiated;
        return this;
    }

    @Override
    public String toString() {
        return "UploadInfo{" +
                "key='" + key + '\'' +
                ", uploadID='" + uploadID + '\'' +
                ", owner=" + owner +
                ", storageClass='" + storageClass + '\'' +
                ", initiated='" + initiated + '\'' +
                '}';
    }
}
