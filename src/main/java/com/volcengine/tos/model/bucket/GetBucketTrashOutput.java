package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.RequestInfo;

public class GetBucketTrashOutput {
    @JsonIgnore
    private RequestInfo requestInfo;
    @JsonProperty("Trash")
    private BucketTrash trash;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public GetBucketTrashOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public BucketTrash getTrash() {
        return trash;
    }

    public GetBucketTrashOutput setTrash(BucketTrash trash) {
        this.trash = trash;
        return this;
    }

    @Override
    public String toString() {
        return "GetBucketTrashOutput{" +
                "requestInfo=" + requestInfo +
                ", trash=" + trash +
                '}';
    }
}
