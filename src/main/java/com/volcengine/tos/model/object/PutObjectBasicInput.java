package com.volcengine.tos.model.object;

import com.volcengine.tos.comm.event.DataTransferListener;
import com.volcengine.tos.comm.ratelimit.RateLimiter;
import com.volcengine.tos.model.GenericInput;

import java.util.Map;

public class PutObjectBasicInput extends GenericInput {
    private String bucket;
    private String key;
    private long contentLength = -1;
    private int readLimit;

    private ObjectMetaRequestOptions options;

    private DataTransferListener dataTransferListener;

    private String callback;
    private String callbackVar;
    private boolean forbidOverwrite;
    private String ifMatch;
    private String tagging;
    private long objectExpires = -1;


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

    public long getContentLength() {
        return contentLength;
    }

    public PutObjectBasicInput setContentLength(long contentLength) {
        this.contentLength = contentLength;
        return this;
    }

    public String getCallback() {
        return callback;
    }

    public PutObjectBasicInput setCallback(String callback) {
        this.callback = callback;
        return this;
    }

    public String getCallbackVar() {
        return callbackVar;
    }

    public PutObjectBasicInput setCallbackVar(String callbackVar) {
        this.callbackVar = callbackVar;
        return this;
    }

    public int getReadLimit() {
        return readLimit;
    }

    public PutObjectBasicInput setReadLimit(int readLimit) {
        this.readLimit = readLimit;
        return this;
    }

    public boolean isForbidOverwrite() {
        return forbidOverwrite;
    }

    public PutObjectBasicInput setForbidOverwrite(boolean forbidOverwrite) {
        this.forbidOverwrite = forbidOverwrite;
        return this;
    }

    public String getIfMatch() {
        return ifMatch;
    }

    public PutObjectBasicInput setIfMatch(String ifMatch) {
        this.ifMatch = ifMatch;
        return this;
    }

    public String getTagging() {
        return tagging;
    }

    public PutObjectBasicInput setTagging(String tagging) {
        this.tagging = tagging;
        return this;
    }

    public long getObjectExpires() {
        return objectExpires;
    }

    public PutObjectBasicInput setObjectExpires(long objectExpires) {
        this.objectExpires = objectExpires;
        return this;
    }

    @Override
    public String toString() {
        return "PutObjectBasicInput{" +
                "bucket='" + bucket + '\'' +
                ", key='" + key + '\'' +
                ", contentLength=" + contentLength +
                ", readLimit=" + readLimit +
                ", options=" + options +
                ", dataTransferListener=" + dataTransferListener +
                ", callback='" + callback + '\'' +
                ", callbackVar='" + callbackVar + '\'' +
                ", forbidOverwrite=" + forbidOverwrite +
                ", ifMatch='" + ifMatch + '\'' +
                ", tagging='" + tagging + '\'' +
                ", objectExpires='" + objectExpires + '\'' +
                ", rateLimiter=" + rateLimiter +
                '}';
    }

    public static PutObjectBasicInputBuilder builder() {
        return new PutObjectBasicInputBuilder();
    }

    public static final class PutObjectBasicInputBuilder {
        private String bucket;
        private String key;
        private long contentLength = -1;
        private ObjectMetaRequestOptions options;
        private DataTransferListener dataTransferListener;
        private RateLimiter rateLimit;
        private String callback;
        private String callbackVar;
        private boolean forbidOverwrite;
        private String ifMatch;
        private String tagging;
        private long objectExpires = -1;

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

        public PutObjectBasicInputBuilder contentLength(long contentLength) {
            this.contentLength = contentLength;
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

        public PutObjectBasicInputBuilder callback(String callback) {
            this.callback = callback;
            return this;
        }

        public PutObjectBasicInputBuilder callbackVar(String callbackVar) {
            this.callbackVar = callbackVar;
            return this;
        }

        public PutObjectBasicInputBuilder forbidOverwrite(boolean forbidOverwrite) {
            this.forbidOverwrite = forbidOverwrite;
            return this;
        }

        public PutObjectBasicInputBuilder ifMatch(String ifMatch) {
            this.ifMatch = ifMatch;
            return this;
        }

        public PutObjectBasicInputBuilder tagging(String tagging) {
            this.tagging = tagging;
            return this;
        }

        public PutObjectBasicInputBuilder objectExpires(long objectExpires) {
            this.objectExpires = objectExpires;
            return this;
        }


        public PutObjectBasicInput build() {
            PutObjectBasicInput putObjectBasicInput = new PutObjectBasicInput();
            putObjectBasicInput.key = this.key;
            putObjectBasicInput.bucket = this.bucket;
            putObjectBasicInput.contentLength = this.contentLength;
            putObjectBasicInput.dataTransferListener = this.dataTransferListener;
            putObjectBasicInput.options = this.options;
            putObjectBasicInput.rateLimiter = this.rateLimit;
            putObjectBasicInput.callback = this.callback;
            putObjectBasicInput.callbackVar = this.callbackVar;
            putObjectBasicInput.forbidOverwrite = this.forbidOverwrite;
            putObjectBasicInput.ifMatch = this.ifMatch;
            putObjectBasicInput.tagging = this.tagging;
            putObjectBasicInput.objectExpires = this.objectExpires;
            return putObjectBasicInput;
        }
    }
}
