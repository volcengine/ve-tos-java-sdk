package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.RequestInfo;

public class GetAudioConvertTemplateOutput {
    @JsonIgnore
    private RequestInfo requestInfo;
    
    @JsonProperty("Tag")
    private String tag;
    
    @JsonProperty("ID")
    private String id;
    
    @JsonProperty("Name")
    private String name;
    
    @JsonProperty("AudioConvertConfig")
    private AudioConvertConfig audioConvertConfig;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public GetAudioConvertTemplateOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public String getTag() {
        return tag;
    }

    public GetAudioConvertTemplateOutput setTag(String tag) {
        this.tag = tag;
        return this;
    }

    public String getId() {
        return id;
    }

    public GetAudioConvertTemplateOutput setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public GetAudioConvertTemplateOutput setName(String name) {
        this.name = name;
        return this;
    }

    public AudioConvertConfig getAudioConvertConfig() {
        return audioConvertConfig;
    }

    public GetAudioConvertTemplateOutput setAudioConvertConfig(AudioConvertConfig audioConvertConfig) {
        this.audioConvertConfig = audioConvertConfig;
        return this;
    }

    @Override
    public String toString() {
        return "GetAudioConvertTemplateOutput{" +
                "requestInfo=" + requestInfo +
                ", tag='" + tag + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", audioConvertConfig=" + audioConvertConfig +
                '}';
    }
}