package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.volcengine.tos.model.RequestInfo;

public class GetBucketInventoryOutput extends BucketInventoryConfiguration{
    @JsonIgnore
    private RequestInfo requestInfo;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public GetBucketInventoryOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    @Override
    public String toString() {
        return "GetBucketInventoryOutput{" +
                "requestInfo=" + requestInfo +
                ", id='" + id + '\'' +
                ", isEnabled=" + isEnabled +
                ", filter=" + filter +
                ", destination=" + destination +
                ", schedule=" + schedule +
                ", includedObjectVersions=" + includedObjectVersions +
                ", optionalFields=" + optionalFields +
                '}';
    }
}
