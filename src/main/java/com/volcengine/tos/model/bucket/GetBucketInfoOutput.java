package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.RequestInfo;

public class GetBucketInfoOutput {

    @JsonProperty("RequestInfo")
    private RequestInfo requestInfo;

    @JsonProperty("Bucket")
    private BucketInfo bucket;

    // Getter and Setter

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public void setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
    }

    public BucketInfo getBucket() {
        return bucket;
    }

    public void setBucket(BucketInfo bucket) {
        this.bucket = bucket;
    }
}
