package com.volcengine.tos.model.object;

import com.volcengine.tos.internal.TosResponse;
import com.volcengine.tos.model.RequestInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

@Deprecated
public class GetObjectOutput implements Closeable, Serializable {
    @JsonIgnore
    private RequestInfo requestInfo;
    private String contentRange;
    @JsonIgnore
    private transient InputStream content;
    private ObjectMeta objectMeta;

    public GetObjectOutput setObjectMetaFromResponse(TosResponse response) {
        this.objectMeta = new ObjectMeta().fromResponse(response);
        return this;
    }

    @Override
    public void close() throws IOException {
        if (content != null) {
            content.close();
        }
    }

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public GetObjectOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public String getContentRange() {
        return contentRange;
    }

    public GetObjectOutput setContentRange(String contentRange) {
        this.contentRange = contentRange;
        return this;
    }

    public InputStream getContent() {
        return content;
    }

    public GetObjectOutput setContent(InputStream content) {
        this.content = content;
        return this;
    }

    public ObjectMeta getObjectMeta() {
        return objectMeta;
    }

    public GetObjectOutput setObjectMeta(ObjectMeta objectMeta) {
        this.objectMeta = objectMeta;
        return this;
    }

    @Override
    public String toString() {
        return "GetObjectOutput{" +
                "requestInfo=" + requestInfo +
                ", contentRange='" + contentRange + '\'' +
                ", content=" + content +
                ", objectMeta=" + objectMeta +
                '}';
    }
}
