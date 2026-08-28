package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.volcengine.tos.model.RequestInfo;

public class DeleteAudioConvertTemplateOutput {
    @JsonIgnore
    private RequestInfo requestInfo;
    
    public RequestInfo getRequestInfo() {
        return requestInfo;
    }
    
    public DeleteAudioConvertTemplateOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }
    
    @Override
    public String toString() {
        return "DeleteAudioConvertTemplateOutput{" +
                "requestInfo=" + requestInfo +
                '}';
    }
}