package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.RequestInfo;

public class GetBucketLocationOutput {
    @JsonIgnore
    private RequestInfo requestInfo;
    @JsonProperty("Region")
    private String region;
    @JsonProperty("ExtranetEndpoint")
    private String extranetEndpoint;
    @JsonProperty("IntranetEndpoint")
    private String intranetEndpoint;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public GetBucketLocationOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public String getRegion() {
        return region;
    }

    public GetBucketLocationOutput setRegion(String region) {
        this.region = region;
        return this;
    }

    public String getExtranetEndpoint() {
        return extranetEndpoint;
    }

    public GetBucketLocationOutput setExtranetEndpoint(String extranetEndpoint) {
        this.extranetEndpoint = extranetEndpoint;
        return this;
    }

    public String getIntranetEndpoint() {
        return intranetEndpoint;
    }

    public GetBucketLocationOutput setIntranetEndpoint(String intranetEndpoint) {
        this.intranetEndpoint = intranetEndpoint;
        return this;
    }

    @Override
    public String toString() {
        return "GetBucketLocationOutput{" +
                "requestInfo=" + requestInfo +
                ", region='" + region + '\'' +
                ", extranetEndpoint='" + extranetEndpoint + '\'' +
                ", intranetEndpoint='" + intranetEndpoint + '\'' +
                '}';
    }
}
