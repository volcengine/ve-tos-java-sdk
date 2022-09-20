package com.volcengine.tos.model.bucket;

import com.volcengine.tos.comm.common.StorageClassType;
import com.volcengine.tos.model.RequestInfo;

import java.io.Serializable;

@Deprecated
public class HeadBucketOutput implements Serializable {
    private RequestInfo requestInfo;
    private String region;
    private String storageClass;

    public HeadBucketOutput(RequestInfo requestInfo, String region, String storageClass) {
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

    public String getStorageClass() {
        return storageClass;
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
