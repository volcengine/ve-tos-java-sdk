package com.volcengine.tos.model.bucket;

import com.volcengine.tos.comm.common.StorageClassType;
import com.volcengine.tos.model.RequestInfo;

import java.io.Serializable;

public class HeadBucketV2Output implements Serializable {
    private RequestInfo requestInfo;
    private String region;
    private StorageClassType storageClass;

    public HeadBucketV2Output() {
    }

    public HeadBucketV2Output(RequestInfo requestInfo, String region, StorageClassType storageClass) {
        this.requestInfo = requestInfo;
        this.region = region;
        this.storageClass = storageClass;
    }

    public String getRegion() {
        return region;
    }

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public StorageClassType getStorageClass() {
        return storageClass;
    }

    public HeadBucketV2Output setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public HeadBucketV2Output setRegion(String region) {
        this.region = region;
        return this;
    }

    public HeadBucketV2Output setStorageClass(StorageClassType storageClass) {
        this.storageClass = storageClass;
        return this;
    }

    @Override
    public String toString() {
        return "HeadBucketOutput{" +
                "requestInfo=" + requestInfo +
                ", region='" + region + '\'' +
                ", storageClass=" + storageClass +
                '}';
    }
}
