package com.volcengine.tos.model.object;

import com.volcengine.tos.comm.event.DataTransferListener;
import com.volcengine.tos.comm.ratelimit.RateLimiter;
import com.volcengine.tos.model.GenericInput;

import java.io.InputStream;

public class UploadPartV2Input extends GenericInput {
    private UploadPartBasicInput uploadPartBasicInput = new UploadPartBasicInput();
    private transient InputStream content;
    private long contentLength = -1;

    public InputStream getContent() {
        return content;
    }

    public long getContentLength() {
        return contentLength;
    }

    public UploadPartV2Input setContent(InputStream content) {
        this.content = content;
        return this;
    }

    public UploadPartV2Input setContentLength(long contentLength) {
        this.contentLength = contentLength;
        return this;
    }

    public String getBucket() {
        return uploadPartBasicInput.getBucket();
    }

    public UploadPartV2Input setBucket(String bucket) {
        this.uploadPartBasicInput.setBucket(bucket);
        return this;
    }

    public String getKey() {
        return uploadPartBasicInput.getKey();
    }

    public UploadPartV2Input setKey(String key) {
        this.uploadPartBasicInput.setKey(key);
        return this;
    }

    public String getUploadID() {
        return uploadPartBasicInput.getUploadID();
    }

    public UploadPartV2Input setUploadID(String uploadID) {
        this.uploadPartBasicInput.setUploadID(uploadID);
        return this;
    }

    public int getPartNumber() {
        return uploadPartBasicInput.getPartNumber();
    }

    public UploadPartV2Input setPartNumber(int partNumber) {
        this.uploadPartBasicInput.setPartNumber(partNumber);
        return this;
    }

    public ObjectMetaRequestOptions getOptions() {
        return uploadPartBasicInput.getOptions();
    }

    public UploadPartV2Input setOptions(ObjectMetaRequestOptions options) {
        this.uploadPartBasicInput.setOptions(options);
        return this;
    }

    public DataTransferListener getDataTransferListener() {
        return uploadPartBasicInput.getDataTransferListener();
    }

    public UploadPartV2Input setDataTransferListener(DataTransferListener dataTransferListener) {
        this.uploadPartBasicInput.setDataTransferListener(dataTransferListener);
        return this;
    }

    public RateLimiter getRateLimiter() {
        return uploadPartBasicInput.getRateLimiter();
    }

    public UploadPartV2Input setRateLimiter(RateLimiter rateLimiter) {
        this.uploadPartBasicInput.setRateLimiter(rateLimiter);
        return this;
    }

    public int getReadLimit() {
        return uploadPartBasicInput.getReadLimit();
    }

    public UploadPartV2Input setReadLimit(int readLimit) {
        this.uploadPartBasicInput.setReadLimit(readLimit);
        return this;
    }

    @Override
    public String toString() {
        return "UploadPartV2Input{" +
                "bucket='" + getBucket() + '\'' +
                ", key='" + getKey() + '\'' +
                ", uploadID='" + getUploadID() + '\'' +
                ", partNumber=" + getPartNumber() +
                ", options=" + getOptions() +
                ", dataTransferListener=" + getDataTransferListener() +
                ", rateLimit=" + getRateLimiter() +
                ", contentLength=" + contentLength +
                '}';
    }

    public static UploadPartV2InputBuilder builder() {
        return new UploadPartV2InputBuilder();
    }

    public static final class UploadPartV2InputBuilder {
        private UploadPartBasicInput uploadPartBasicInput = new UploadPartBasicInput();
        private transient InputStream content;
        private long contentLength = -1;

        private UploadPartV2InputBuilder() {
        }

        @Deprecated
        public UploadPartV2InputBuilder uploadPartBasicInput(UploadPartBasicInput uploadPartBasicInput) {
            this.uploadPartBasicInput = uploadPartBasicInput;
            return this;
        }

        public UploadPartV2InputBuilder content(InputStream content) {
            this.content = content;
            return this;
        }

        public UploadPartV2InputBuilder contentLength(long contentLength) {
            this.contentLength = contentLength;
            return this;
        }

        public UploadPartV2InputBuilder bucket(String bucket) {
            this.uploadPartBasicInput.setBucket(bucket);
            return this;
        }

        public UploadPartV2InputBuilder key(String key) {
            this.uploadPartBasicInput.setKey(key);
            return this;
        }

        public UploadPartV2InputBuilder uploadID(String uploadID) {
            this.uploadPartBasicInput.setUploadID(uploadID);
            return this;
        }

        public UploadPartV2InputBuilder partNumber(int partNumber) {
            this.uploadPartBasicInput.setPartNumber(partNumber);
            return this;
        }

        public UploadPartV2InputBuilder options(ObjectMetaRequestOptions options) {
            this.uploadPartBasicInput.setOptions(options);
            return this;
        }

        public UploadPartV2InputBuilder dataTransferListener(DataTransferListener dataTransferListener) {
            this.uploadPartBasicInput.setDataTransferListener(dataTransferListener);
            return this;
        }

        public UploadPartV2InputBuilder rateLimiter(RateLimiter rateLimiter) {
            this.uploadPartBasicInput.setRateLimiter(rateLimiter);
            return this;
        }

        public UploadPartV2InputBuilder readLimit(int readLimit) {
            this.uploadPartBasicInput.setReadLimit(readLimit);
            return this;
        }

        public UploadPartV2Input build() {
            UploadPartV2Input uploadPartV2Input = new UploadPartV2Input();
            uploadPartV2Input.uploadPartBasicInput = this.uploadPartBasicInput;
            uploadPartV2Input.content = this.content;
            uploadPartV2Input.contentLength = this.contentLength;
            return uploadPartV2Input;
        }
    }

    @Deprecated
    public UploadPartBasicInput getUploadPartBasicInput() {
        return uploadPartBasicInput;
    }

    @Deprecated
    public UploadPartV2Input setUploadPartBasicInput(UploadPartBasicInput uploadPartBasicInput) {
        this.uploadPartBasicInput = uploadPartBasicInput;
        return this;
    }
}
