package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.RequestInfo;

import java.util.List;

public class GetBucketNotificationOutput {
    @JsonIgnore
    private RequestInfo requestInfo;
    @JsonProperty("CloudFunctionConfigurations")
    private List<CloudFunctionConfiguration> cloudFunctionConfiguration;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public GetBucketNotificationOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public List<CloudFunctionConfiguration> getCloudFunctionConfiguration() {
        return cloudFunctionConfiguration;
    }

    public GetBucketNotificationOutput setCloudFunctionConfiguration(List<CloudFunctionConfiguration> cloudFunctionConfiguration) {
        this.cloudFunctionConfiguration = cloudFunctionConfiguration;
        return this;
    }

    @Override
    public String toString() {
        return "GetBucketNotificationOutput{" +
                "requestInfo=" + requestInfo +
                ", cloudFunctionConfiguration=" + cloudFunctionConfiguration +
                '}';
    }
}
