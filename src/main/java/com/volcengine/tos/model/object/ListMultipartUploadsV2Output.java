package com.volcengine.tos.model.object;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.RequestInfo;

import java.util.Arrays;
import java.util.List;

public class ListMultipartUploadsV2Output {
    @JsonIgnore
    private RequestInfo requestInfo;
    @JsonProperty("Bucket")
    private String bucket;
    @JsonProperty("Prefix")
    private String prefix;
    @JsonProperty("KeyMarker")
    private String keyMarker;
    @JsonProperty("UploadIdMarker")
    private String uploadIDMarker;
    @JsonProperty("MaxUploads")
    private int maxUploads;
    @JsonProperty("Delimiter")
    private String delimiter;
    @JsonProperty("IsTruncated")
    private boolean isTruncated;
    @JsonProperty("EncodingType")
    private String encodingType;
    @JsonProperty("NextKeyMarker")
    private String nextKeyMarker;
    @JsonProperty("NextUploadIdMarker")
    private String nextUploadIdMarker;
    @JsonProperty("CommonPrefixes")
    private List<ListedCommonPrefix> commonPrefixes;
    @JsonProperty("Uploads")
    private List<ListedUpload> uploads;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public ListMultipartUploadsV2Output setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public String getBucket() {
        return bucket;
    }

    public ListMultipartUploadsV2Output setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public String getPrefix() {
        return prefix;
    }

    public ListMultipartUploadsV2Output setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public String getKeyMarker() {
        return keyMarker;
    }

    public ListMultipartUploadsV2Output setKeyMarker(String keyMarker) {
        this.keyMarker = keyMarker;
        return this;
    }

    public String getUploadIDMarker() {
        return uploadIDMarker;
    }

    public ListMultipartUploadsV2Output setUploadIDMarker(String uploadIDMarker) {
        this.uploadIDMarker = uploadIDMarker;
        return this;
    }

    public int getMaxUploads() {
        return maxUploads;
    }

    public ListMultipartUploadsV2Output setMaxUploads(int maxUploads) {
        this.maxUploads = maxUploads;
        return this;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public ListMultipartUploadsV2Output setDelimiter(String delimiter) {
        this.delimiter = delimiter;
        return this;
    }

    public boolean isTruncated() {
        return isTruncated;
    }

    public ListMultipartUploadsV2Output setTruncated(boolean truncated) {
        isTruncated = truncated;
        return this;
    }

    public String getEncodingType() {
        return encodingType;
    }

    public ListMultipartUploadsV2Output setEncodingType(String encodingType) {
        this.encodingType = encodingType;
        return this;
    }

    public String getNextKeyMarker() {
        return nextKeyMarker;
    }

    public ListMultipartUploadsV2Output setNextKeyMarker(String nextKeyMarker) {
        this.nextKeyMarker = nextKeyMarker;
        return this;
    }

    public String getNextUploadIdMarker() {
        return nextUploadIdMarker;
    }

    public ListMultipartUploadsV2Output setNextUploadIdMarker(String nextUploadIdMarker) {
        this.nextUploadIdMarker = nextUploadIdMarker;
        return this;
    }

    public List<ListedCommonPrefix> getCommonPrefixes() {
        return commonPrefixes;
    }

    public ListMultipartUploadsV2Output setCommonPrefixes(List<ListedCommonPrefix> commonPrefixes) {
        this.commonPrefixes = commonPrefixes;
        return this;
    }

    public List<ListedUpload> getUploads() {
        return uploads;
    }

    public ListMultipartUploadsV2Output setUploads(List<ListedUpload> uploads) {
        this.uploads = uploads;
        return this;
    }

    @Override
    public String toString() {
        return "ListMultipartUploadsV2Output{" +
                "requestInfo=" + requestInfo +
                ", bucket='" + bucket + '\'' +
                ", prefix='" + prefix + '\'' +
                ", keyMarker='" + keyMarker + '\'' +
                ", uploadIDMarker='" + uploadIDMarker + '\'' +
                ", maxUploads=" + maxUploads +
                ", delimiter='" + delimiter + '\'' +
                ", isTruncated=" + isTruncated +
                ", encodingType='" + encodingType + '\'' +
                ", nextKeyMarker='" + nextKeyMarker + '\'' +
                ", nextUploadIdMarker='" + nextUploadIdMarker + '\'' +
                ", commonPrefixes=" + commonPrefixes +
                ", uploads=" + uploads +
                '}';
    }
}
