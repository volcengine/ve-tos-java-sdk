package com.volcengine.tos.model.bucket;

import com.volcengine.tos.model.RequestInfo;

import java.io.Serializable;

public class HeadBucketOutput implements Serializable {
    private RequestInfo requestInfo;
    private String region;

    public HeadBucketOutput(RequestInfo requestInfo, String region) {
        this.requestInfo = requestInfo;
        this.region = region;
    }

    public String getRegion() {
        return region;
    }

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }
}
