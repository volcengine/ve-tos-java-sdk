package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.RequestInfo;

public class CreateAsyncProcessTaskOutput {
    @JsonIgnore
    private RequestInfo requestInfo;

    @JsonProperty("JobId")
    private String jobId;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public CreateAsyncProcessTaskOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public String getJobId() {
        return jobId;
    }

    public CreateAsyncProcessTaskOutput setJobId(String jobId) {
        this.jobId = jobId;
        return this;
    }

    @Override
    public String toString() {
        return "CreateAsyncProcessTaskOutput{" +
                "requestInfo=" + requestInfo +
                ", jobId='" + jobId + '\'' +
                '}';
    }
}
