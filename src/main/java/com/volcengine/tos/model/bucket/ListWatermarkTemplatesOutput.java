package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.RequestInfo;

import java.util.List;

public class ListWatermarkTemplatesOutput {
    @JsonIgnore
    private RequestInfo requestInfo;

    @JsonProperty("WatermarkTemplates")
    private List<WatermarkTemplate> watermarkTemplates;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public ListWatermarkTemplatesOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public List<WatermarkTemplate> getWatermarkTemplates() {
        return watermarkTemplates;
    }

    public ListWatermarkTemplatesOutput setWatermarkTemplates(List<WatermarkTemplate> watermarkTemplates) {
        this.watermarkTemplates = watermarkTemplates;
        return this;
    }

    @Override
    public String toString() {
        return "ListWatermarkTemplatesOutput{" +
                "requestInfo=" + requestInfo +
                ", watermarkTemplates=" + watermarkTemplates +
                '}';
    }
}
