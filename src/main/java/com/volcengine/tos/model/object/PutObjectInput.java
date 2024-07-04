package com.volcengine.tos.model.object;

import com.volcengine.tos.comm.event.DataTransferListener;
import com.volcengine.tos.comm.ratelimit.RateLimiter;
import com.volcengine.tos.model.GenericInput;

import java.io.InputStream;

public class PutObjectInput extends GenericInput {
    private PutObjectBasicInput putObjectBasicInput = new PutObjectBasicInput();
    private InputStream content;

    public PutObjectInput() {
    }

    @Deprecated
    public PutObjectBasicInput getPutObjectBasicInput() {
        return putObjectBasicInput;
    }

    public InputStream getContent() {
        return content;
    }

    @Deprecated
    public PutObjectInput setPutObjectBasicInput(PutObjectBasicInput putObjectBasicInput) {
        this.putObjectBasicInput = putObjectBasicInput;
        return this;
    }

    public PutObjectInput setContent(InputStream content) {
        this.content = content;
        return this;
    }

    public String getBucket() {
        return putObjectBasicInput.getBucket();
    }

    public PutObjectInput setBucket(String bucket) {
        this.putObjectBasicInput.setBucket(bucket);
        return this;
    }

    public String getKey() {
        return putObjectBasicInput.getKey();
    }

    public PutObjectInput setKey(String key) {
        this.putObjectBasicInput.setKey(key);
        return this;
    }

    public long getContentLength() {
        return putObjectBasicInput.getContentLength();
    }

    public PutObjectInput setContentLength(long contentLength) {
        this.putObjectBasicInput.setContentLength(contentLength);
        return this;
    }

    public ObjectMetaRequestOptions getOptions() {
        return putObjectBasicInput.getOptions();
    }

    public PutObjectInput setOptions(ObjectMetaRequestOptions options) {
        this.putObjectBasicInput.setOptions(options);
        return this;
    }

    public DataTransferListener getDataTransferListener() {
        return putObjectBasicInput.getDataTransferListener();
    }

    public PutObjectInput setDataTransferListener(DataTransferListener dataTransferListener) {
        this.putObjectBasicInput.setDataTransferListener(dataTransferListener);
        return this;
    }

    public RateLimiter getRateLimiter() {
        return putObjectBasicInput.getRateLimiter();
    }

    public PutObjectInput setRateLimiter(RateLimiter rateLimiter) {
        this.putObjectBasicInput.setRateLimiter(rateLimiter);
        return this;
    }

    public String getCallback() {
        return putObjectBasicInput.getCallback();
    }

    public PutObjectInput setCallback(String callback) {
        this.putObjectBasicInput.setCallback(callback);
        return this;
    }

    public String getCallbackVar() {
        return putObjectBasicInput.getCallbackVar();
    }

    public PutObjectInput setCallbackVar(String callbackVar) {
        this.putObjectBasicInput.setCallbackVar(callbackVar);
        return this;
    }

    public int getReadLimit() {
        return putObjectBasicInput.getReadLimit();
    }

    public PutObjectInput setReadLimit(int readLimit) {
        this.putObjectBasicInput.setReadLimit(readLimit);
        return this;
    }

    public boolean isForbidOverwrite() {
        return this.putObjectBasicInput.isForbidOverwrite();
    }

    public PutObjectInput setForbidOverwrite(boolean forbidOverwrite) {
        this.putObjectBasicInput.setForbidOverwrite(forbidOverwrite);
        return this;
    }

    public String getIfMatch() {
        return this.putObjectBasicInput.getIfMatch();
    }

    public PutObjectInput setIfMatch(String ifMatch) {
        this.putObjectBasicInput.setIfMatch(ifMatch);
        return this;
    }

    public String getTagging() {
        return this.putObjectBasicInput.getTagging();
    }

    public PutObjectInput setTagging(String tagging) {
        this.putObjectBasicInput.setTagging(tagging);
        return this;
    }

    @Override
    public String toString() {
        return "PutObjectInput{" +
                "bucket='" + getBucket() + '\'' +
                ", key='" + getKey() + '\'' +
                ", contentLength=" + getContentLength() +
                ", options=" + getOptions() +
                ", dataTransferListener=" + getDataTransferListener() +
                ", rateLimit=" + getRateLimiter() +
                ", callback='" + getCallback() + '\'' +
                ", callbackVar='" + getCallbackVar() + '\'' +
                '}';
    }

    public static PutObjectInputBuilder builder() {
        return new PutObjectInputBuilder();
    }

    public static final class PutObjectInputBuilder {
        private PutObjectBasicInput putObjectBasicInput = new PutObjectBasicInput();
        private InputStream content;

        private PutObjectInputBuilder() {
        }

        @Deprecated
        public PutObjectInputBuilder putObjectBasicInput(PutObjectBasicInput putObjectBasicInput) {
            this.putObjectBasicInput = putObjectBasicInput;
            return this;
        }

        public PutObjectInputBuilder content(InputStream content) {
            this.content = content;
            return this;
        }

        public PutObjectInputBuilder bucket(String bucket) {
            this.putObjectBasicInput.setBucket(bucket);
            return this;
        }

        public PutObjectInputBuilder key(String key) {
            this.putObjectBasicInput.setKey(key);
            return this;
        }

        public PutObjectInputBuilder contentLength(long contentLength) {
            this.putObjectBasicInput.setContentLength(contentLength);
            return this;
        }

        public PutObjectInputBuilder options(ObjectMetaRequestOptions options) {
            this.putObjectBasicInput.setOptions(options);
            return this;
        }

        public PutObjectInputBuilder dataTransferListener(DataTransferListener dataTransferListener) {
            this.putObjectBasicInput.setDataTransferListener(dataTransferListener);
            return this;
        }

        public PutObjectInputBuilder rateLimiter(RateLimiter rateLimiter) {
            this.putObjectBasicInput.setRateLimiter(rateLimiter);
            return this;
        }

        public PutObjectInputBuilder callback(String callback) {
            this.putObjectBasicInput.setCallback(callback);
            return this;
        }

        public PutObjectInputBuilder callbackVar(String callbackVar) {
            this.putObjectBasicInput.setCallbackVar(callbackVar);
            return this;
        }

        public PutObjectInputBuilder readLimit(int readLimit) {
            this.putObjectBasicInput.setReadLimit(readLimit);
            return this;
        }

        public PutObjectInputBuilder forbidOverwrite(boolean forbidOverwrite) {
            this.putObjectBasicInput.setForbidOverwrite(forbidOverwrite);
            return this;
        }

        public PutObjectInputBuilder ifMatch(String ifMatch) {
            this.putObjectBasicInput.setIfMatch(ifMatch);
            return this;
        }

        public PutObjectInput build() {
            PutObjectInput putObjectInput = new PutObjectInput();
            putObjectInput.setPutObjectBasicInput(putObjectBasicInput);
            putObjectInput.setContent(content);
            return putObjectInput;
        }
    }
}
