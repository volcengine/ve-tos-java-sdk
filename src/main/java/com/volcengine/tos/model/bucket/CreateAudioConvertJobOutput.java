package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.RequestInfo;

public class CreateAudioConvertJobOutput {
    @JsonProperty("JobId")
    private String jobId;
    @JsonIgnore
    private RequestInfo requestInfo;

    public String getJobId() {
        return jobId;
    }

    public CreateAudioConvertJobOutput setJobId(String jobId) {
        this.jobId = jobId;
        return this;
    }

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public CreateAudioConvertJobOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    @Override
    public String toString() {
        return "CreateAudioConvertJobOutput{" +
                "jobId='" + jobId + '\'' +
                ", requestInfo=" + requestInfo +
                '}';
    }
}