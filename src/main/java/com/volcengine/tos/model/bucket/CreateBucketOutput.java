package com.volcengine.tos.model.bucket;

import com.volcengine.tos.model.RequestInfo;
import java.io.Serializable;

@Deprecated
public class CreateBucketOutput implements Serializable {
    private RequestInfo requestInfo;
    private String location;

    public CreateBucketOutput(RequestInfo requestInfo, String location) {
        this.requestInfo = requestInfo;
        this.location = location;
    }

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public CreateBucketOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public String getLocation() {
        return location;
    }

    public CreateBucketOutput setLocation(String location) {
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
