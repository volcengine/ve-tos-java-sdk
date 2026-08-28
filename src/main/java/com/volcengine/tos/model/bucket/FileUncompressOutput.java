package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.RequestInfo;

public class FileUncompressOutput {
    @JsonIgnore
    private RequestInfo requestInfo;

    @JsonProperty("Code")
    private String code;

    @JsonProperty("JobId")
    private String jobId;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public FileUncompressOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public String getCode() {
        return code;
    }

    public FileUncompressOutput setCode(String code) {
        this.code = code;
        return this;
    }

    public String getJobId() {
        return jobId;
    }

    public FileUncompressOutput setJobId(String jobId) {
        this.jobId = jobId;
        return this;
    }

    @Override
    public String toString() {
        return "FileUncompressOutput{" +
                "requestInfo=" + requestInfo +
                ", code='" + code + '\'' +
                ", jobId='" + jobId + '\'' +
                '}';
    }
}
