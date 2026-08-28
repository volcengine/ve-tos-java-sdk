package com.volcengine.tos.model.bucket;

import com.volcengine.tos.model.RequestInfo;

public class GetAudioConvertJobOutput {
    private RequestInfo requestInfo;
    
    @com.fasterxml.jackson.annotation.JsonProperty("JobID")
    private String jobID;
    
    @com.fasterxml.jackson.annotation.JsonProperty("CreateTime")
    private String createTime;
    
    @com.fasterxml.jackson.annotation.JsonProperty("StartTime")
    private String startTime;
    
    @com.fasterxml.jackson.annotation.JsonProperty("EndTime")
    private String endTime;
    
    @com.fasterxml.jackson.annotation.JsonProperty("State")
    private String state;
    
    @com.fasterxml.jackson.annotation.JsonProperty("Code")
    private Integer code;
    
    @com.fasterxml.jackson.annotation.JsonProperty("Message")
    private String message;
    
    @com.fasterxml.jackson.annotation.JsonProperty("Input")
    private ConvertJobInput input;
    
    @com.fasterxml.jackson.annotation.JsonProperty("AudioConvertConfig")
    private AudioConvertConfig audioConvertConfig;
    
    @com.fasterxml.jackson.annotation.JsonProperty("Output")
    private ConvertJobOutput output;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public GetAudioConvertJobOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public String getJobID() {
        return jobID;
    }

    public GetAudioConvertJobOutput setJobID(String jobID) {
        this.jobID = jobID;
        return this;
    }

    public String getCreateTime() {
        return createTime;
    }

    public GetAudioConvertJobOutput setCreateTime(String createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getStartTime() {
        return startTime;
    }

    public GetAudioConvertJobOutput setStartTime(String startTime) {
        this.startTime = startTime;
        return this;
    }

    public String getEndTime() {
        return endTime;
    }

    public GetAudioConvertJobOutput setEndTime(String endTime) {
        this.endTime = endTime;
        return this;
    }

    public String getState() {
        return state;
    }

    public GetAudioConvertJobOutput setState(String state) {
        this.state = state;
        return this;
    }

    public Integer getCode() {
        return code;
    }

    public GetAudioConvertJobOutput setCode(Integer code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public GetAudioConvertJobOutput setMessage(String message) {
        this.message = message;
        return this;
    }

    public ConvertJobInput getInput() {
        return input;
    }

    public GetAudioConvertJobOutput setInput(ConvertJobInput input) {
        this.input = input;
        return this;
    }

    public AudioConvertConfig getAudioConvertConfig() {
        return audioConvertConfig;
    }

    public GetAudioConvertJobOutput setAudioConvertConfig(AudioConvertConfig audioConvertConfig) {
        this.audioConvertConfig = audioConvertConfig;
        return this;
    }

    public ConvertJobOutput getOutput() {
        return output;
    }

    public GetAudioConvertJobOutput setOutput(ConvertJobOutput output) {
        this.output = output;
        return this;
    }

    @Override
    public String toString() {
        return "GetAudioConvertJobOutput{" +
                "requestInfo=" + requestInfo +
                ", jobID='" + jobID + '\'' +
                ", createTime='" + createTime + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", state='" + state + '\'' +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", input=" + input +
                ", audioConvertConfig=" + audioConvertConfig +
                ", output=" + output +
                '}';
    }
}