package com.volcengine.tos.model.bucket;

import com.volcengine.tos.model.RequestInfo;

import java.io.Serializable;

public class CreateBucketV2Output implements Serializable {
    private RequestInfo requestInfo;
    private String location;

    public CreateBucketV2Output(RequestInfo requestInfo, String location) {
        this.requestInfo = requestInfo;
        this.location = location;
    }

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public CreateBucketV2Output setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public String getLocation() {
        return location;
    }

    public CreateBucketV2Output setLocation(String location) {
        this.location = location;
        return this;
    }

    @Override
    public String toString(){
        if (this.location == null) {
            return "";
        }
        return "{ Location: "+this.location+" }";
    }
}
