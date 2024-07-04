package com.volcengine.tos.model.object;

import com.volcengine.tos.comm.event.DataTransferListener;
import com.volcengine.tos.comm.ratelimit.RateLimiter;
import com.volcengine.tos.model.GenericInput;

import java.io.InputStream;

public class ModifyObjectInput extends GenericInput {
    private String bucket;
    private String key;
    private long offset;
    private InputStream content;

    private long contentLength = -1;
    private DataTransferListener dataTransferListener;
    private RateLimiter rateLimiter;
    private long trafficLimit;

    public String getBucket() {
        return bucket;
    }

    public ModifyObjectInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public String getKey() {
        return key;
    }

    public ModifyObjectInput setKey(String key) {
        this.key = key;
        return this;
    }

    public long getOffset() {
        return offset;
    }

    public ModifyObjectInput setOffset(long offset) {
        this.offset = offset;
        return this;
    }

    public InputStream getContent() {
        return content;
    }

    public ModifyObjectInput setContent(InputStream content) {
        this.content = content;
        return this;
    }

    public long getContentLength() {
        return contentLength;
    }

    public ModifyObjectInput setContentLength(long contentLength) {
        this.contentLength = contentLength;
        return this;
    }

    public DataTransferListener getDataTransferListener() {
        return dataTransferListener;
    }

    public ModifyObjectInput setDataTransferListener(DataTransferListener dataTransferListener) {
        this.dataTransferListener = dataTransferListener;
        return this;
    }

    public RateLimiter getRateLimiter() {
        return rateLimiter;
    }

    public ModifyObjectInput setRateLimiter(RateLimiter rateLimiter) {
        this.rateLimiter = rateLimiter;
        return this;
    }

    public long getTrafficLimit() {
        return trafficLimit;
    }

    public ModifyObjectInput setTrafficLimit(long trafficLimit) {
        this.trafficLimit = trafficLimit;
        return this;
    }

    @Override
    public String toString() {
        return "ModifyObjectInput{" +
                "bucket='" + bucket + '\'' +
                ", key='" + key + '\'' +
                ", offset=" + offset +
                ", content=" + content +
                ", contentLength=" + contentLength +
                ", dataTransferListener=" + dataTransferListener +
                ", rateLimiter=" + rateLimiter +
                ", trafficLimit=" + trafficLimit +
                '}';
    }
}
