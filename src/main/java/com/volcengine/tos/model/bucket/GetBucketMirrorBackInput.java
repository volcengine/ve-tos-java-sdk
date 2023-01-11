package com.volcengine.tos.model.bucket;

public class GetBucketMirrorBackInput {
    private String bucket;

    public String getBucket() {
        return bucket;
    }

    public GetBucketMirrorBackInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    @Override
    public String toString() {
        return "GetBucketMirrorBackInput{" +
                "bucket='" + bucket + '\'' +
                '}';
    }

    public static GetBucketMirrorBackInputBuilder builder() {
        return new GetBucketMirrorBackInputBuilder();
    }

    public static final class GetBucketMirrorBackInputBuilder {
        private String bucket;

        private GetBucketMirrorBackInputBuilder() {
        }

        public GetBucketMirrorBackInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public GetBucketMirrorBackInput build() {
            GetBucketMirrorBackInput getBucketMirrorBackInput = new GetBucketMirrorBackInput();
            getBucketMirrorBackInput.setBucket(bucket);
            return getBucketMirrorBackInput;
        }
    }
}
