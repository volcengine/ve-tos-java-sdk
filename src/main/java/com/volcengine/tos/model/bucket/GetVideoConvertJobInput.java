package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.volcengine.tos.model.GenericInput;

public class GetVideoConvertJobInput extends GenericInput {
    @JsonIgnore
    private String bucket;
    
    @JsonIgnore
    private String jobId;
    
    public String getBucket() {
        return bucket;
    }
    
    public GetVideoConvertJobInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }
    
    public String getJobId() {
        return jobId;
    }
    
    public GetVideoConvertJobInput setJobId(String jobId) {
        this.jobId = jobId;
        return this;
    }
    
    public static GetVideoConvertJobInputBuilder builder() {
        return new GetVideoConvertJobInputBuilder();
    }
    
    public static class GetVideoConvertJobInputBuilder {
        private String bucket;
        private String jobId;
        
        public GetVideoConvertJobInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }
        
        public GetVideoConvertJobInputBuilder jobId(String jobId) {
            this.jobId = jobId;
            return this;
        }
        
        public GetVideoConvertJobInput build() {
            GetVideoConvertJobInput input = new GetVideoConvertJobInput();
            input.setBucket(bucket);
            input.setJobId(jobId);
            return input;
        }
    }
}