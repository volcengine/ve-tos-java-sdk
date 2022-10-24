package com.volcengine.tos.model.object;

import com.volcengine.tos.comm.event.DataTransferListener;
import com.volcengine.tos.comm.ratelimit.RateLimiter;

import java.util.Map;

public class PutObjectBasicInput {
    private String bucket;
    private String key;

    private ObjectMetaRequestOptions options;

    private DataTransferListener dataTransferListener;

    /** 客户端限速，单位 Byte/s **/
    private RateLimiter rateLimiter;

    public String getBucket() {
        return bucket;
    }

    public String getKey() {
        return key;
    }

    public ObjectMetaRequestOptions getOptions() {
        return options;
    }

    public DataTransferListener getDataTransferListener() {
        return dataTransferListener;
    }

    public Map<String, String> getAllSettedHeaders() {
        return options == null ? null : options.headers();
    }

    public PutObjectBasicInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public PutObjectBasicInput setKey(String key) {
        this.key = key;
        return this;
    }

    public PutObjectBasicInput setOptions(ObjectMetaRequestOptions options) {
        this.options = options;
        return this;
    }

    public PutObjectBasicInput setDataTransferListener(DataTransferListener dataTransferListener) {
        this.dataTransferListener = dataTransferListener;
        return this;
    }

    public RateLimiter getRateLimiter() {
        return rateLimiter;
    }

    public PutObjectBasicInput setRateLimiter(RateLimiter rateLimiter) {
        this.rateLimiter = rateLimiter;
        return this;
    }

    @Override
    public String toString() {
        return "PutObjectBasicInput{" +
                "bucket='" + bucket + '\'' +
                ", key='" + key + '\'' +
                ", options=" + options +
                ", dataTransferListener=" + dataTransferListener +
                ", rateLimit=" + rateLimiter +
                '}';
    }

    public static PutObjectBasicInputBuilder builder() {
        return new PutObjectBasicInputBuilder();
    }

    public static final class PutObjectBasicInputBuilder {
        private String bucket;
        private String key;
        private ObjectMetaRequestOptions options;
        private DataTransferListener dataTransferListener;
        private RateLimiter rateLimit;

        private PutObjectBasicInputBuilder() {
        }

        public PutObjectBasicInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public PutObjectBasicInputBuilder key(String key) {
            this.key = key;
            return this;
        }

        public PutObjectBasicInputBuilder options(ObjectMetaRequestOptions options) {
            this.options = options;
            return this;
        }

        public PutObjectBasicInputBuilder dataTransferListener(DataTransferListener dataTransferListener) {
            this.dataTransferListener = dataTransferListener;
            return this;
        }

        public PutObjectBasicInputBuilder rateLimiter(RateLimiter rateLimiter) {
            this.rateLimit = rateLimiter;
            return this;
        }

        public PutObjectBasicInput build() {
            PutObjectBasicInput putObjectBasicInput = new PutObjectBasicInput();
            putObjectBasicInput.key = this.key;
            putObjectBasicInput.bucket = this.bucket;
            putObjectBasicInput.dataTransferListener = this.dataTransferListener;
            putObjectBasicInput.options = this.options;
            putObjectBasicInput.rateLimiter = this.rateLimit;
            return putObjectBasicInput;
        }
    }
}
