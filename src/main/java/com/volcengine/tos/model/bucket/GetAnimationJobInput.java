package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.volcengine.tos.model.GenericInput;

public class GetAnimationJobInput extends GenericInput {
    @JsonIgnore
    private String bucket;

    @JsonIgnore
    private String key;

    @JsonIgnore
    private String jobID;

    public String getBucket() {
        return bucket;
    }

    public GetAnimationJobInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public String getKey() {
        return key;
    }

    public GetAnimationJobInput setKey(String key) {
        this.key = key;
        return this;
    }

    public String getJobID() {
        return jobID;
    }

    public String getJobId() {
        return jobID;
    }

    public GetAnimationJobInput setJobID(String jobID) {
        this.jobID = jobID;
        return this;
    }

    public GetAnimationJobInput setJobId(String jobId) {
        this.jobID = jobId;
        return this;
    }

    public static GetAnimationJobInputBuilder builder() {
        return new GetAnimationJobInputBuilder();
    }

    public static class GetAnimationJobInputBuilder {
        private String bucket;
        private String key;
        private String jobID;

        public GetAnimationJobInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public GetAnimationJobInputBuilder key(String key) {
            this.key = key;
            return this;
        }

        public GetAnimationJobInputBuilder jobID(String jobID) {
            this.jobID = jobID;
            return this;
        }

        public GetAnimationJobInputBuilder jobId(String jobId) {
            this.jobID = jobId;
            return this;
        }

        public GetAnimationJobInput build() {
            GetAnimationJobInput input = new GetAnimationJobInput();
            input.setBucket(bucket);
            input.setKey(key);
            input.setJobID(jobID);
            return input;
        }
    }
}
