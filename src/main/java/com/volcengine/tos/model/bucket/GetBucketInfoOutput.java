package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.RequestInfo;

public class GetBucketInfoOutput {
    @JsonIgnore
    private RequestInfo requestInfo;

    @JsonProperty("Bucket")
    private BucketInfo bucketInfo;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public GetBucketInfoOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public BucketInfo getBucketInfo() {
        return bucketInfo;
    }

    public GetBucketInfoOutput setBucketInfo(BucketInfo bucketInfo) {
        this.bucketInfo = bucketInfo;
        return this;
    }
}
