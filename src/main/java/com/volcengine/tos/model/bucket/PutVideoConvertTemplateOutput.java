package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.RequestInfo;

public class PutVideoConvertTemplateOutput {
    @JsonProperty("ID")
    private String id;
    
    @JsonIgnore
    private RequestInfo requestInfo;

    public String getId() {
        return id;
    }

    public PutVideoConvertTemplateOutput setId(String id) {
        this.id = id;
        return this;
    }

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public PutVideoConvertTemplateOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    @Override
    public String toString() {
        return "PutVideoConvertTemplateOutput{" +
                "id='" + id + '\'' +
                ", requestInfo=" + requestInfo +
                '}';
    }
}