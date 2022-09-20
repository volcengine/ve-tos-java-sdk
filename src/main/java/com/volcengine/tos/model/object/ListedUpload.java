package com.volcengine.tos.model.object;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.comm.common.StorageClassType;
import com.volcengine.tos.model.acl.Owner;

import java.util.Date;

public class ListedUpload {
    @JsonProperty("Key")
    private String key;
    @JsonProperty("UploadId")
    private String uploadID;
    @JsonProperty("Owner")
    private Owner owner;
    @JsonProperty("StorageClass")
    private StorageClassType storageClass;
    @JsonProperty("Initiated")
    private Date initiated;

    public String getKey() {
        return key;
    }

    public ListedUpload setKey(String key) {
        this.key = key;
        return this;
    }

    public String getUploadID() {
        return uploadID;
    }

    public ListedUpload setUploadID(String uploadID) {
        this.uploadID = uploadID;
        return this;
    }

    public Owner getOwner() {
        return owner;
    }

    public ListedUpload setOwner(Owner owner) {
        this.owner = owner;
        return this;
    }

    public StorageClassType getStorageClass() {
        return storageClass;
    }

    public ListedUpload setStorageClass(StorageClassType storageClass) {
        this.storageClass = storageClass;
        return this;
    }

    public Date getInitiated() {
        return initiated;
    }

    public ListedUpload setInitiated(Date initiated) {
        this.initiated = initiated;
        return this;
    }

    @Override
    public String toString() {
        return "ListedUpload{" +
                "key='" + key + '\'' +
                ", uploadID='" + uploadID + '\'' +
                ", owner=" + owner +
                ", storageClass=" + storageClass +
                ", initiated=" + initiated +
                '}';
    }
}
