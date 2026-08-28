package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.RequestInfo;

import java.util.List;

public class ListAudioConvertTemplatesOutput {
    @JsonIgnore
    private RequestInfo requestInfo;
    
    @JsonProperty("AudioConvertTemplates")
    private List<AudioConvertTemplate> audioConvertTemplates;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public ListAudioConvertTemplatesOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public List<AudioConvertTemplate> getAudioConvertTemplates() {
        return audioConvertTemplates;
    }

    public ListAudioConvertTemplatesOutput setAudioConvertTemplates(List<AudioConvertTemplate> audioConvertTemplates) {
        this.audioConvertTemplates = audioConvertTemplates;
        return this;
    }

    @Override
    public String toString() {
        return "ListAudioConvertTemplatesOutput{" +
                "requestInfo=" + requestInfo +
                ", audioConvertTemplates=" + audioConvertTemplates +
                '}';
    }
}