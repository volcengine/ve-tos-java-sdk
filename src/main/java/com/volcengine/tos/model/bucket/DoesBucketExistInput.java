package com.volcengine.tos.model.bucket;

import com.volcengine.tos.model.GenericInput;

public class DoesBucketExistInput extends GenericInput {

    private String bucket;

    public DoesBucketExistInput(){
    }

    public DoesBucketExistInput(String bucket) {
        this.bucket = bucket;
    }

    public String getBucket() {
        return bucket;
    }

    public DoesBucketExistInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    @Override
    public String toString() {
        return "DoesBucketExistInput{" +
                "bucket=" + bucket +
                '}';
    }

    public static DoesBucketExistInputBuilder builder() {
        return new DoesBucketExistInputBuilder();
    }

    public static final class DoesBucketExistInputBuilder {
        private String bucket;

        private DoesBucketExistInputBuilder() {
        }

        public DoesBucketExistInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public DoesBucketExistInput build() {
            DoesBucketExistInput doesBucketExistInput = new DoesBucketExistInput();
            doesBucketExistInput.setBucket(this.bucket);
            return doesBucketExistInput;
        }
    }
}

