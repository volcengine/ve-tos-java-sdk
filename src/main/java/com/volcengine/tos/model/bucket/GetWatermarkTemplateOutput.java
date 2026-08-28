package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.RequestInfo;

public class GetWatermarkTemplateOutput {
    @JsonIgnore
    private RequestInfo requestInfo;

    @JsonProperty("Tag")
    private String tag;

    @JsonProperty("ID")
    private String id;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("WatermarkConfig")
    private Watermark watermarkConfig;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public GetWatermarkTemplateOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public String getTag() {
        return tag;
    }

    public GetWatermarkTemplateOutput setTag(String tag) {
        this.tag = tag;
        return this;
    }

    public String getId() {
        return id;
    }

    public GetWatermarkTemplateOutput setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public GetWatermarkTemplateOutput setName(String name) {
        this.name = name;
        return this;
    }

    public Watermark getWatermarkConfig() {
        return watermarkConfig;
    }

    public GetWatermarkTemplateOutput setWatermarkConfig(Watermark watermarkConfig) {
        this.watermarkConfig = watermarkConfig;
        return this;
    }

    @Override
    public String toString() {
        return "GetWatermarkTemplateOutput{" +
                "requestInfo=" + requestInfo +
                ", tag='" + tag + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", watermarkConfig=" + watermarkConfig +
                '}';
    }
}
