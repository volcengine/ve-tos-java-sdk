package com.volcengine.tos.model.object;

import com.volcengine.tos.model.RequestInfo;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;

@Deprecated
public class ListMultipartUploadsOutput {
    private RequestInfo requestInfo;
    @JsonProperty("Bucket")
    private String bucket;
    @JsonProperty("KeyMarker")
    private String keyMarker;
    @JsonProperty("NextKeyMarker")
    private String nextKeyMarker;
    @JsonProperty("UploadIdMarker")
    private String uploadIDMarker;
    @JsonProperty("NextUploadIdMarker")
    private String nextUploadIdMarker;
    @JsonProperty("Delimiter")
    private String delimiter;
    @JsonProperty("Prefix")
    private String prefix;
    @JsonProperty("MaxUploads")
    private int maxUploads;
    @JsonProperty("IsTruncated")
    private boolean isTruncated;
    @JsonProperty("Uploads")
    private UploadInfo[] upload;
    @JsonProperty("CommonPrefixes")
    private UploadCommonPrefix[] commonPrefixes;

    public ListMultipartUploadsOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public String getBucket() {
        return bucket;
    }

    public ListMultipartUploadsOutput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public String getKeyMarker() {
        return keyMarker;
    }

    public ListMultipartUploadsOutput setKeyMarker(String keyMarker) {
        this.keyMarker = keyMarker;
        return this;
    }

    public String getNextKeyMarker() {
        return nextKeyMarker;
    }

    public ListMultipartUploadsOutput setNextKeyMarker(String nextKeyMarker) {
        this.nextKeyMarker = nextKeyMarker;
        return this;
    }

    public String getUploadIDMarker() {
        return uploadIDMarker;
    }

    public ListMultipartUploadsOutput setUploadIDMarker(String uploadIDMarker) {
        this.uploadIDMarker = uploadIDMarker;
        return this;
    }

    public String getNextUploadIdMarker() {
        return nextUploadIdMarker;
    }

    public ListMultipartUploadsOutput setNextUploadIdMarker(String nextUploadIdMarker) {
        this.nextUploadIdMarker = nextUploadIdMarker;
        return this;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public ListMultipartUploadsOutput setDelimiter(String delimiter) {
        this.delimiter = delimiter;
        return this;
    }

    public String getPrefix() {
        return prefix;
    }

    public ListMultipartUploadsOutput setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public int getMaxUploads() {
        return maxUploads;
    }

    public ListMultipartUploadsOutput setMaxUploads(int maxUploads) {
        this.maxUploads = maxUploads;
        return this;
    }

    public boolean isTruncated() {
        return isTruncated;
    }

    public ListMultipartUploadsOutput setTruncated(boolean truncated) {
        isTruncated = truncated;
        return this;
    }

    public UploadInfo[] getUpload() {
        return upload;
    }

    public ListMultipartUploadsOutput setUpload(UploadInfo[] upload) {
        this.upload = upload;
        return this;
    }

    public UploadCommonPrefix[] getCommonPrefixes() {
        return commonPrefixes;
    }

    public ListMultipartUploadsOutput setCommonPrefixes(UploadCommonPrefix[] commonPrefixes) {
        this.commonPrefixes = commonPrefixes;
        return this;
    }

    @Override
    public String toString() {
        return "ListMultipartUploadsOutput{" +
                "requestInfo=" + requestInfo +
                ", bucket='" + bucket + '\'' +
                ", keyMarker='" + keyMarker + '\'' +
                ", nextKeyMarker='" + nextKeyMarker + '\'' +
                ", uploadIDMarker='" + uploadIDMarker + '\'' +
                ", nextUploadIdMarker='" + nextUploadIdMarker + '\'' +
                ", delimiter='" + delimiter + '\'' +
                ", prefix='" + prefix + '\'' +
                ", maxUploads=" + maxUploads +
                ", isTruncated=" + isTruncated +
                ", upload=" + Arrays.toString(upload) +
                ", commonPrefixes=" + Arrays.toString(commonPrefixes) +
                '}';
    }
}
