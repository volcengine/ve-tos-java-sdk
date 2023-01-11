package com.volcengine.tos.model.object;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.RequestInfo;

public class PutFetchTaskOutput {
    @JsonIgnore
    private RequestInfo requestInfo;
    @JsonProperty("TaskId")
    private String taskID;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public PutFetchTaskOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public String getTaskID() {
        return taskID;
    }

    public PutFetchTaskOutput setTaskID(String taskID) {
        this.taskID = taskID;
        return this;
    }

    @Override
    public String toString() {
        return "PutFetchTaskOutput{" +
                "requestInfo=" + requestInfo +
                ", taskID='" + taskID + '\'' +
                '}';
    }
}
