package com.volcengine.tos.model.object;

import com.volcengine.tos.model.RequestInfo;
import com.volcengine.tos.model.acl.Owner;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;

public class ListUploadedPartsOutput {
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
    @JsonProperty("NextPartNumberMarker")
    private int nextPartNumberMarker;
    @JsonProperty("MaxParts")
    private int maxParts;
    @JsonProperty("IsTruncated")
    private boolean isTruncated;
    @JsonProperty("StorageClass")
    private String storageClass;
    @JsonProperty("Owner")
    private Owner owner;
    @JsonProperty("Parts")
    private UploadedPart[] uploadedParts;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public ListUploadedPartsOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public String getBucket() {
        return bucket;
    }

    public ListUploadedPartsOutput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public String getKey() {
        return key;
    }

    public ListUploadedPartsOutput setKey(String key) {
        this.key = key;
        return this;
    }

    public String getUploadID() {
        return uploadID;
    }

    public ListUploadedPartsOutput setUploadID(String uploadID) {
        this.uploadID = uploadID;
        return this;
    }

    public int getPartNumberMarker() {
        return partNumberMarker;
    }

    public ListUploadedPartsOutput setPartNumberMarker(int partNumberMarker) {
        this.partNumberMarker = partNumberMarker;
        return this;
    }

    public int getNextPartNumberMarker() {
        return nextPartNumberMarker;
    }

    public ListUploadedPartsOutput setNextPartNumberMarker(int nextPartNumberMarker) {
        this.nextPartNumberMarker = nextPartNumberMarker;
        return this;
    }

    public int getMaxParts() {
        return maxParts;
    }

    public ListUploadedPartsOutput setMaxParts(int maxParts) {
        this.maxParts = maxParts;
        return this;
    }

    public boolean isTruncated() {
        return isTruncated;
    }

    public ListUploadedPartsOutput setTruncated(boolean truncated) {
        isTruncated = truncated;
        return this;
    }

    public String getStorageClass() {
        return storageClass;
    }

    public ListUploadedPartsOutput setStorageClass(String storageClass) {
        this.storageClass = storageClass;
        return this;
    }

    public Owner getOwner() {
        return owner;
    }

    public ListUploadedPartsOutput setOwner(Owner owner) {
        this.owner = owner;
        return this;
    }

    public UploadedPart[] getUploadedParts() {
        return uploadedParts;
    }

    public ListUploadedPartsOutput setUploadedParts(UploadedPart[] uploadedParts) {
        this.uploadedParts = uploadedParts;
        return this;
    }

    @Override
    public String toString() {
        return "ListUploadedPartsOutput{" +
                "requestInfo=" + requestInfo +
                ", bucket='" + bucket + '\'' +
                ", key='" + key + '\'' +
                ", uploadID='" + uploadID + '\'' +
                ", partNumberMarker=" + partNumberMarker +
                ", nextPartNumberMarker=" + nextPartNumberMarker +
                ", maxParts=" + maxParts +
                ", isTruncated=" + isTruncated +
                ", storageClass='" + storageClass + '\'' +
                ", owner=" + owner +
                ", uploadedParts=" + Arrays.toString(uploadedParts) +
                '}';
    }
}
