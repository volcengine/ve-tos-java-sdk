package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.RequestInfo;

import java.util.List;

public class ListVideoConvertTemplatesOutput {
    @JsonIgnore
    private RequestInfo requestInfo;

    @JsonProperty("VideoConvertTemplates")
    private List<VideoConvertTemplate> videoConvertTemplates;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public ListVideoConvertTemplatesOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public List<VideoConvertTemplate> getVideoConvertTemplates() {
        return videoConvertTemplates;
    }

    public ListVideoConvertTemplatesOutput setVideoConvertTemplates(List<VideoConvertTemplate> videoConvertTemplates) {
        this.videoConvertTemplates = videoConvertTemplates;
        return this;
    }

    @Override
    public String toString() {
        return "ListVideoConvertTemplatesOutput{" +
                "requestInfo=" + requestInfo +
                ", videoConvertTemplates=" + videoConvertTemplates +
                '}';
    }
}