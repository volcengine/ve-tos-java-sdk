package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.comm.common.JobStateType;
import com.volcengine.tos.model.RequestInfo;

public class GetAnimationJobOutput {
    @JsonIgnore
    private RequestInfo requestInfo;

    @JsonProperty("JobID")
    private String jobID;

    @JsonProperty("CreateTime")
    private String createTime;

    @JsonProperty("StartTime")
    private String startTime;

    @JsonProperty("EndTime")
    private String endTime;

    @JsonProperty("Callback")
    private String callback;

    @JsonProperty("State")
    private JobStateType state;

    @JsonProperty("Code")
    private Integer code;

    @JsonProperty("Message")
    private String message;

    @JsonProperty("Input")
    private ConvertJobInput input;

    @JsonProperty("AnimationConfig")
    private AnimationConfig animationConfig;

    @JsonProperty("Output")
    private ConvertJobOutput output;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public GetAnimationJobOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public String getJobID() {
        return jobID;
    }

    public GetAnimationJobOutput setJobID(String jobID) {
        this.jobID = jobID;
        return this;
    }

    public String getCreateTime() {
        return createTime;
    }

    public GetAnimationJobOutput setCreateTime(String createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getStartTime() {
        return startTime;
    }

    public GetAnimationJobOutput setStartTime(String startTime) {
        this.startTime = startTime;
        return this;
    }

    public String getEndTime() {
        return endTime;
    }

    public GetAnimationJobOutput setEndTime(String endTime) {
        this.endTime = endTime;
        return this;
    }

    public String getCallback() {
        return callback;
    }

    public GetAnimationJobOutput setCallback(String callback) {
        this.callback = callback;
        return this;
    }

    public JobStateType getState() {
        return state;
    }

    public GetAnimationJobOutput setState(JobStateType state) {
        this.state = state;
        return this;
    }

    public Integer getCode() {
        return code;
    }

    public GetAnimationJobOutput setCode(Integer code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public GetAnimationJobOutput setMessage(String message) {
        this.message = message;
        return this;
    }

    public ConvertJobInput getInput() {
        return input;
    }

    public GetAnimationJobOutput setInput(ConvertJobInput input) {
        this.input = input;
        return this;
    }

    public AnimationConfig getAnimationConfig() {
        return animationConfig;
    }

    public GetAnimationJobOutput setAnimationConfig(AnimationConfig animationConfig) {
        this.animationConfig = animationConfig;
        return this;
    }

    public ConvertJobOutput getOutput() {
        return output;
    }

    public GetAnimationJobOutput setOutput(ConvertJobOutput output) {
        this.output = output;
        return this;
    }

    @Override
    public String toString() {
        return "GetAnimationJobOutput{" +
                "requestInfo=" + requestInfo +
                ", jobID='" + jobID + '\'' +
                ", createTime='" + createTime + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", callback='" + callback + '\'' +
                ", state=" + state +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", input=" + input +
                ", animationConfig=" + animationConfig +
                ", output=" + output +
                '}';
    }
}
