package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.RequestInfo;

public class CreateVideoConvertJobOutput {
    @JsonProperty("Code")
    private String code;

    @JsonProperty("Message")
    private String message;

    @JsonProperty("JobId")
    private String jobId;

    @JsonIgnore
    private RequestInfo requestInfo;

    public String getCode() {
        return code;
    }

    public CreateVideoConvertJobOutput setCode(String code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public CreateVideoConvertJobOutput setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getJobId() {
        return jobId;
    }

    public CreateVideoConvertJobOutput setJobId(String jobId) {
        this.jobId = jobId;
        return this;
    }

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public CreateVideoConvertJobOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    @Override
    public String toString() {
        return "CreateVideoConvertJobOutput{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", jobId='" + jobId + '\'' +
                ", requestInfo=" + requestInfo +
                '}';
    }
}
