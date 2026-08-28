package com.volcengine.tos.model.bucket;

import com.volcengine.tos.model.GenericInput;

public class GetAudioConvertJobInput extends GenericInput {
    private String bucket;
    private String jobId;

    public String getBucket() {
        return bucket;
    }

    public GetAudioConvertJobInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public String getJobId() {
        return jobId;
    }

    public GetAudioConvertJobInput setJobId(String jobId) {
        this.jobId = jobId;
        return this;
    }

    public static GetAudioConvertJobInputBuilder builder() {
        return new GetAudioConvertJobInputBuilder();
    }

    public static class GetAudioConvertJobInputBuilder {
        private String bucket;
        private String jobId;

        public GetAudioConvertJobInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public GetAudioConvertJobInputBuilder jobId(String jobId) {
            this.jobId = jobId;
            return this;
        }

        public GetAudioConvertJobInput build() {
            GetAudioConvertJobInput input = new GetAudioConvertJobInput();
            input.setBucket(bucket);
            input.setJobId(jobId);
            return input;
        }
    }

    @Override
    public String toString() {
        return "GetAudioConvertJobInput{" +
                "bucket='" + bucket + '\'' +
                ", jobId='" + jobId + '\'' +
                '}';
    }
}