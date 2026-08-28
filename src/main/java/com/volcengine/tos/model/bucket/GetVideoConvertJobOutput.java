package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.RequestInfo;

public class GetVideoConvertJobOutput {
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
    
    @JsonProperty("State")
    private String state;
    
    @JsonProperty("Code")
    private int code;
    
    @JsonProperty("Message")
    private String message;
    
    @JsonProperty("Input")
    private ConvertJobInput input;
    
    @JsonProperty("TranscodeConfig")
    private TranscodeConfig transcodeConfig;
    
    @JsonProperty("Output")
    private ConvertJobOutput output;
    
    public RequestInfo getRequestInfo() {
        return requestInfo;
    }
    
    public GetVideoConvertJobOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }
    
    public String getJobID() {
        return jobID;
    }
    
    public GetVideoConvertJobOutput setJobID(String jobID) {
        this.jobID = jobID;
        return this;
    }
    
    public String getCreateTime() {
        return createTime;
    }
    
    public GetVideoConvertJobOutput setCreateTime(String createTime) {
        this.createTime = createTime;
        return this;
    }
    
    public String getStartTime() {
        return startTime;
    }
    
    public GetVideoConvertJobOutput setStartTime(String startTime) {
        this.startTime = startTime;
        return this;
    }
    
    public String getEndTime() {
        return endTime;
    }
    
    public GetVideoConvertJobOutput setEndTime(String endTime) {
        this.endTime = endTime;
        return this;
    }
    
    public String getState() {
        return state;
    }
    
    public GetVideoConvertJobOutput setState(String state) {
        this.state = state;
        return this;
    }
    
    public int getCode() {
        return code;
    }
    
    public GetVideoConvertJobOutput setCode(int code) {
        this.code = code;
        return this;
    }
    
    public String getMessage() {
        return message;
    }
    
    public GetVideoConvertJobOutput setMessage(String message) {
        this.message = message;
        return this;
    }
    
    public ConvertJobInput getInput() {
        return input;
    }
    
    public GetVideoConvertJobOutput setInput(ConvertJobInput input) {
        this.input = input;
        return this;
    }
    
    public TranscodeConfig getTranscodeConfig() {
        return transcodeConfig;
    }
    
    public GetVideoConvertJobOutput setTranscodeConfig(TranscodeConfig transcodeConfig) {
        this.transcodeConfig = transcodeConfig;
        return this;
    }
    
    public ConvertJobOutput getOutput() {
        return output;
    }
    
    public GetVideoConvertJobOutput setOutput(ConvertJobOutput output) {
        this.output = output;
        return this;
    }

    @Override
    public String toString() {
        return "GetVideoConvertJobOutput{" +
                "requestInfo=" + requestInfo +
                ", jobID='" + jobID + '\'' +
                ", createTime='" + createTime + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", state='" + state + '\'' +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", input=" + input +
                ", transcodeConfig=" + transcodeConfig +
                ", output=" + output +
                '}';
    }
}