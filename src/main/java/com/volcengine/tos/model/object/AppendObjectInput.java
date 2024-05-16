package com.volcengine.tos.model.object;

import com.volcengine.tos.comm.event.DataTransferListener;
import com.volcengine.tos.comm.ratelimit.RateLimiter;

import java.io.InputStream;
import java.util.Map;
import java.util.Objects;

public class AppendObjectInput {
    private String bucket;
    private String key;
    private long offset;
    private long contentLength;

    private transient InputStream content;

    private ObjectMetaRequestOptions options;

    private DataTransferListener dataTransferListener;

    private String preHashCrc64ecma;

    /** 客户端限速，单位 Byte/s **/
    private RateLimiter rateLimiter;

    private String ifMatch;

    public Map<String, String> getAllSettedHeaders() {
        return Objects.isNull(options) ? null : options.headers();
    }

    public String getBucket() {
        return bucket;
    }

    public String getKey() {
        return key;
    }

    public long getOffset() {
        return offset;
    }

    public long getContentLength() {
        return contentLength;
    }

    public InputStream getContent() {
        return content;
    }

    public ObjectMetaRequestOptions getOptions() {
        return options;
    }

    public DataTransferListener getDataTransferListener() {
        return dataTransferListener;
    }

    public String getPreHashCrc64ecma() {
        return preHashCrc64ecma;
    }

    public AppendObjectInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public AppendObjectInput setKey(String key) {
        this.key = key;
        return this;
    }

    public AppendObjectInput setOffset(long offset) {
        this.offset = offset;
        return this;
    }

    public AppendObjectInput setContentLength(long contentLength) {
        this.contentLength = contentLength;
        return this;
    }

    public AppendObjectInput setContent(InputStream content) {
        this.content = content;
        return this;
    }

    public AppendObjectInput setOptions(ObjectMetaRequestOptions options) {
        this.options = options;
        return this;
    }

    public AppendObjectInput setDataTransferListener(DataTransferListener dataTransferListener) {
        this.dataTransferListener = dataTransferListener;
        return this;
    }

    public AppendObjectInput setPreHashCrc64ecma(String preHashCrc64ecma) {
        this.preHashCrc64ecma = preHashCrc64ecma;
        return this;
    }

    public RateLimiter getRateLimiter() {
        return rateLimiter;
    }

    public AppendObjectInput setRateLimiter(RateLimiter rateLimiter) {
        this.rateLimiter = rateLimiter;
        return this;
    }

    public String getIfMatch() {
        return ifMatch;
    }

    public AppendObjectInput setIfMatch(String ifMatch) {
        this.ifMatch = ifMatch;
        return this;
    }

    @Override
    public String toString() {
        return "AppendObjectInput{" +
                "bucket='" + bucket + '\'' +
                ", key='" + key + '\'' +
                ", offset=" + offset +
                ", contentLength=" + contentLength +
                ", content=" + content +
                ", options=" + options +
                ", dataTransferListener=" + dataTransferListener +
                ", preHashCrc64ecma='" + preHashCrc64ecma + '\'' +
                ", rateLimit=" + rateLimiter +
                '}';
    }

    public static AppendObjectInputBuilder builder() {
        return new AppendObjectInputBuilder();
    }

    public static final class AppendObjectInputBuilder {
        private String bucket;
        private String key;
        private long offset;
        private long contentLength;
        private transient InputStream content;
        private ObjectMetaRequestOptions options;
        private DataTransferListener dataTransferListener;
        private String preHashCrc64ecma;
        private RateLimiter rateLimiter;
        private String ifMatch;

        private AppendObjectInputBuilder() {
        }

        public AppendObjectInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public AppendObjectInputBuilder key(String key) {
            this.key = key;
            return this;
        }

        public AppendObjectInputBuilder offset(long offset) {
            this.offset = offset;
            return this;
        }

        public AppendObjectInputBuilder contentLength(long contentLength) {
            this.contentLength = contentLength;
            return this;
        }

        public AppendObjectInputBuilder content(InputStream content) {
            this.content = content;
            return this;
        }

        public AppendObjectInputBuilder options(ObjectMetaRequestOptions options) {
            this.options = options;
            return this;
        }

        public AppendObjectInputBuilder dataTransferListener(DataTransferListener dataTransferListener) {
            this.dataTransferListener = dataTransferListener;
            return this;
        }

        public AppendObjectInputBuilder preHashCrc64ecma(String preHashCrc64ecma) {
            this.preHashCrc64ecma = preHashCrc64ecma;
            return this;
        }

        public AppendObjectInputBuilder rateLimit(RateLimiter rateLimiter) {
            this.rateLimiter = rateLimiter;
            return this;
        }

        public AppendObjectInputBuilder ifMatch(String ifMatch) {
            this.ifMatch = ifMatch;
            return this;
        }

        public AppendObjectInput build() {
            AppendObjectInput appendObjectInput = new AppendObjectInput();
            appendObjectInput.key = this.key;
            appendObjectInput.content = this.content;
            appendObjectInput.preHashCrc64ecma = this.preHashCrc64ecma;
            appendObjectInput.offset = this.offset;
            appendObjectInput.contentLength = this.contentLength;
            appendObjectInput.bucket = this.bucket;
            appendObjectInput.dataTransferListener = this.dataTransferListener;
            appendObjectInput.rateLimiter = this.rateLimiter;
            appendObjectInput.options = this.options;
            appendObjectInput.ifMatch = this.ifMatch;
            return appendObjectInput;
        }
    }
}
