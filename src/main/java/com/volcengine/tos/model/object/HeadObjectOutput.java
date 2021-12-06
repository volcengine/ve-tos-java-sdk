package com.volcengine.tos.model.object;

import com.volcengine.tos.TosResponse;
import com.volcengine.tos.model.RequestInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class HeadObjectOutput {
    @JsonIgnore
    private RequestInfo requestInfo;
    private String contentRange;
    private ObjectMeta objectMeta;

    public HeadObjectOutput setObjectMeta(TosResponse response) {
        this.objectMeta = new ObjectMeta().fromResponse(response);
        return this;
    }

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public HeadObjectOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public String getContentRange() {
        return contentRange;
    }

    public HeadObjectOutput setContentRange(String contentRange) {
        this.contentRange = contentRange;
        return this;
    }

    public ObjectMeta getObjectMeta() {
        return objectMeta;
    }

    public HeadObjectOutput setObjectMeta(ObjectMeta objectMeta) {
        this.objectMeta = objectMeta;
        return this;
    }

    @Override
    public String toString() {
        return "HeadObjectOutput{" +
                "requestInfo=" + requestInfo +
                ", contentRange='" + contentRange + '\'' +
                ", objectMeta=" + objectMeta +
                '}';
    }
}
