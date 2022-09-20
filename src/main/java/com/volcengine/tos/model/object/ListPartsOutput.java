package com.volcengine.tos.model.object;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.comm.common.StorageClassType;
import com.volcengine.tos.model.RequestInfo;
import com.volcengine.tos.model.acl.Owner;

import java.util.List;

public class ListPartsOutput {
    @JsonIgnore
    private RequestInfo requestInfo;
    @JsonProperty("Bucket")
    private String bucket;
    @JsonProperty("Key")
    private String key;
    @JsonProperty("UploadId")
    private String uploadID;
    @JsonProperty("PartNumberMarker")
    private int partNumberMarker;
    @JsonProperty("MaxParts")
    private int maxParts;
    @JsonProperty("IsTruncated")
    private boolean isTruncated;
    @JsonProperty("StorageClass")
    private StorageClassType storageClass;
    @JsonProperty("NextPartNumberMarker")
    private int nextPartNumberMarker;
    @JsonProperty("Owner")
    private Owner owner;
    @JsonProperty("Parts")
    private List<UploadedPartV2> uploadedParts;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public ListPartsOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public String getBucket() {
        return bucket;
    }

    public ListPartsOutput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public String getKey() {
        return key;
    }

    public ListPartsOutput setKey(String key) {
        this.key = key;
        return this;
    }

    public String getUploadID() {
        return uploadID;
    }

    public ListPartsOutput setUploadID(String uploadID) {
        this.uploadID = uploadID;
        return this;
    }

    public int getPartNumberMarker() {
        return partNumberMarker;
    }

    public ListPartsOutput setPartNumberMarker(int partNumberMarker) {
        this.partNumberMarker = partNumberMarker;
        return this;
    }

    public int getMaxParts() {
        return maxParts;
    }

    public ListPartsOutput setMaxParts(int maxParts) {
        this.maxParts = maxParts;
        return this;
    }

    public boolean isTruncated() {
        return isTruncated;
    }

    public ListPartsOutput setTruncated(boolean truncated) {
        isTruncated = truncated;
        return this;
    }

    public StorageClassType getStorageClass() {
        return storageClass;
    }

    public ListPartsOutput setStorageClass(StorageClassType storageClass) {
        this.storageClass = storageClass;
        return this;
    }

    public int getNextPartNumberMarker() {
        return nextPartNumberMarker;
    }

    public ListPartsOutput setNextPartNumberMarker(int nextPartNumberMarker) {
        this.nextPartNumberMarker = nextPartNumberMarker;
        return this;
    }

    public Owner getOwner() {
        return owner;
    }

    public ListPartsOutput setOwner(Owner owner) {
        this.owner = owner;
        return this;
    }

    public List<UploadedPartV2> getUploadedParts() {
        return uploadedParts;
    }

    public ListPartsOutput setUploadedParts(List<UploadedPartV2> uploadedParts) {
        this.uploadedParts = uploadedParts;
        return this;
    }

    @Override
    public String toString() {
        return "ListPartsOutput{" +
                "requestInfo=" + requestInfo +
                ", bucket='" + bucket + '\'' +
                ", key='" + key + '\'' +
                ", uploadID='" + uploadID + '\'' +
                ", partNumberMarker=" + partNumberMarker +
                ", maxParts=" + maxParts +
                ", isTruncated=" + isTruncated +
                ", storageClass='" + storageClass + '\'' +
                ", nextPartNumberMarker=" + nextPartNumberMarker +
                ", owner=" + owner +
                ", uploadedParts=" + uploadedParts +
                '}';
    }
}
