package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.volcengine.tos.model.GenericInput;

public class GetBucketInfoInput extends GenericInput {
    @JsonIgnore
    private String bucket;

    public String getBucket() {
        return bucket;
    }

    public GetBucketInfoInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    @Override
    public String toString() {
        return "GetBucketInfoInput{" +
                "bucket='" + bucket + '\'' +
                '}';
    }


    public static final class GetBucketInfoInputBuilder {
        private String bucket;

        private GetBucketInfoInputBuilder() {
        }

        public static GetBucketInfoInputBuilder aGetBucketInfoInput() {
            return new GetBucketInfoInputBuilder();
        }

        public GetBucketInfoInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public GetBucketInfoInput build() {
            GetBucketInfoInput getBucketInfoInput = new GetBucketInfoInput();
            getBucketInfoInput.setBucket(bucket);
            return getBucketInfoInput;
        }
    }
}
