package com.volcengine.tos.model.object;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.RequestInfo;

public class GetFetchTaskOutput {
    @JsonIgnore
    private RequestInfo requestInfo;
    @JsonProperty("State")
    private String state;
    @JsonProperty("Err")
    private String err;
    @JsonProperty("Task")
    private FetchTask task;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public GetFetchTaskOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public FetchTask getTask() {
        return task;
    }

    public void setTask(FetchTask task) {
        this.task = task;
    }

    public String getErr() {
        return err;
    }

    public void setErr(String err) {
        this.err = err;
    }

    @Override
    public String toString() {
        return "GetFetchTaskOutput{" +
                "requestInfo=" + requestInfo +
                ", state='" + state + '\'' +
                ", err='" + err + '\'' +
                ", task=" + task +
                '}';
    }
}
