package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.RequestInfo;

public class PutAudioConvertTemplateOutput {
    @JsonIgnore
    private RequestInfo requestInfo;
    
    @JsonProperty("ID")
    private String id;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public PutAudioConvertTemplateOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public String getId() {
        return id;
    }

    public PutAudioConvertTemplateOutput setId(String id) {
        this.id = id;
        return this;
    }

    @Override
    public String toString() {
        return "PutAudioConvertTemplateOutput{" +
                "requestInfo=" + requestInfo +
                ", id='" + id + '\'' +
                '}';
    }
}