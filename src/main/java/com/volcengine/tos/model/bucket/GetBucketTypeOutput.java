package com.volcengine.tos.model.bucket;

import com.volcengine.tos.comm.common.AzRedundancyType;
import com.volcengine.tos.comm.common.BucketType;
import com.volcengine.tos.comm.common.StorageClassType;
import com.volcengine.tos.model.RequestInfo;

public class GetBucketTypeOutput {
    private RequestInfo requestInfo;
    private BucketType bucketType;
    private StorageClassType storageClassType;
    private AzRedundancyType azRedundancyType;
    private String projectName;
    private long ExpireAt;

    public GetBucketTypeOutput() {
    }

    public GetBucketTypeOutput(RequestInfo requestInfo, BucketType bucketType, StorageClassType storageClassType,
                               AzRedundancyType azRedundancyType, String projectName, long expireAt) {
        this.requestInfo = requestInfo;
        this.bucketType = bucketType;
        this.storageClassType = storageClassType;
        this.azRedundancyType = azRedundancyType;
        this.projectName = projectName;
        ExpireAt = expireAt;
    }

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public GetBucketTypeOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public BucketType getBucketType() {
        return bucketType;
    }

    public GetBucketTypeOutput setBucketType(BucketType bucketType) {
        this.bucketType = bucketType;
        return this;
    }

    public StorageClassType getStorageClassType() {
        return storageClassType;
    }

    public GetBucketTypeOutput setStorageClassType(StorageClassType storageClassType) {
        this.storageClassType = storageClassType;
        return this;
    }

    public AzRedundancyType getAzRedundancyType() {
        return azRedundancyType;
    }

    public GetBucketTypeOutput setAzRedundancyType(AzRedundancyType azRedundancyType) {
        this.azRedundancyType = azRedundancyType;
        return this;
    }

    public String getProjectName() {
        return projectName;
    }

    public GetBucketTypeOutput setProjectName(String projectName) {
        this.projectName = projectName;
        return this;
    }

    public long getExpireAt() {
        return ExpireAt;
    }

    public GetBucketTypeOutput setExpireAt(long expireAt) {
        ExpireAt = expireAt;
        return this;
    }

    @Override
    public String toString() {
        return "GetBucketTypeOutput{" +
                "requestInfo=" + requestInfo +
                ", bucketType=" + bucketType +
                ", storageClassType=" + storageClassType +
                ", azRedundancyType=" + azRedundancyType +
                ", projectName='" + projectName + '\'' +
                ", ExpireAt=" + ExpireAt +
                '}';
    }
}