package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.RequestInfo;

public class GetVideoConvertTemplateOutput {
    @JsonIgnore
    private RequestInfo requestInfo;

    @JsonProperty("Tag")
    private String tag;

    @JsonProperty("ID")
    private String id;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("TranscodeConfig")
    private Transcode transcodeConfig;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public GetVideoConvertTemplateOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public String getTag() {
        return tag;
    }

    public GetVideoConvertTemplateOutput setTag(String tag) {
        this.tag = tag;
        return this;
    }

    public String getId() {
        return id;
    }

    public GetVideoConvertTemplateOutput setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public GetVideoConvertTemplateOutput setName(String name) {
        this.name = name;
        return this;
    }

    public Transcode getTranscodeConfig() {
        return transcodeConfig;
    }

    public GetVideoConvertTemplateOutput setTranscodeConfig(Transcode transcodeConfig) {
        this.transcodeConfig = transcodeConfig;
        return this;
    }

    @Override
    public String toString() {
        return "GetVideoConvertTemplateOutput{" +
                "requestInfo=" + requestInfo +
                ", tag='" + tag + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", transcodeConfig=" + transcodeConfig +
                '}';
    }
}